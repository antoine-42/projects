from travellingSalesman.solver.solver import Solver, Path


class ChristofidesSolver(Solver):
    """Implementation of the Christofides algorithm.

    """
    def __init__(self, points):
        super().__init__(points)
        self.distance_matrix = [[0 for j in range(self.graph_size)] for i in range(self.graph_size)]
        self.make_distance_matrix()

    def solve(self):
        min_spanning_tree = self.make_min_spanning_tree()
        odd_degree_vertexes = self.find_odd_degree_vertices(min_spanning_tree)
        min_weight_matching = self.find_min_weight_matching(odd_degree_vertexes)
        eulerian_graph = min_spanning_tree + min_weight_matching
        eulerian_circuit = self.make_eulerian_circuit(eulerian_graph)[0]
        hamiltonian_circuit = self.make_hamiltonian_circuit(eulerian_circuit)
        hamiltonian_circuit_vertices = [self.vertices[i] for i in hamiltonian_circuit]
        circuit_length = self.calculate_distance(hamiltonian_circuit)
        return Path(hamiltonian_circuit_vertices, circuit_length)

    def make_distance_matrix(self):
        """Make a distance matrix with the points.

        """
        for src in self.vertices:
            for dst in self.vertices[src.id+1:]:
                self.distance_matrix[src.id][dst.id] = src.distance(dst)

    def make_min_spanning_tree(self):
        """Make a minimum spanning tree with Kruskal's algorithm.
        Maybe not the best, but it works, and is simple enough.

        Make a group from each vertex.
        Sort the edges by their length.
        For every edge:
            If this edge connects two unconnected groups, add it to the minimum spanning tree.
            Merge the groups.
            Stop when there is only one group.
        """
        edges = [Edge(x, y, cell)
                 for y, line in enumerate(self.distance_matrix)
                 for x, cell in enumerate(line)
                 if cell > 0]
        edges = sorted(edges)

        min_spanning_tree = []
        groups = ConnectedGroups([vertex.id for vertex in self.vertices])
        for edge in edges:
            if not groups.elements_in_same_group(edge.vertex1, edge.vertex2):
                groups.merge_elements_groups(edge.vertex1, edge.vertex2)
                min_spanning_tree.append(edge)
            if len(groups) == 1:
                break
        return min_spanning_tree

    def find_odd_degree_vertices(self, min_spanning_tree):
        """Make a list of the vertices with an odd degree.

        """
        degrees = [0 for i in range(self.graph_size)]
        for edge in min_spanning_tree:
            degrees[edge.vertex1] += 1
            degrees[edge.vertex2] += 1

        return [i
                for i, degree in enumerate(degrees)
                if degree % 2 != 0]

    def find_min_weight_matching(self, odd_degree_vertexes):
        """Make a complete graph from the odd degree vertices, and find a minimum weight perfect matching.

        The graph is complete and has an even number of vertices, so it's easy to compute a perfect matching.

        """
        edges = [Edge(x, y, cell)
                 for y, line in enumerate(self.distance_matrix)
                 for x, cell in enumerate(line)
                 if cell > 0 and x in odd_degree_vertexes and y in odd_degree_vertexes]
        edges = sorted(edges)

        min_weight_matching = []
        groups = ConnectedGroups(odd_degree_vertexes)
        for edge in edges:
            if not groups.elements_in_same_group(edge.vertex1, edge.vertex2) \
                    and len(groups.get_element_group(edge.vertex1)) == 1 \
                    and len(groups.get_element_group(edge.vertex2)) == 1:
                groups.merge_elements_groups(edge.vertex1, edge.vertex2)
                min_weight_matching.append(edge)
            if len(groups) == 1:
                break
        return min_weight_matching

    def make_eulerian_circuit(self, eulerian_graph, start_id=0, neighbors=None):
        """

        """
        eulerian_circuit = []

        curr_id = start_id

        sub_path = False
        if neighbors is None:
            neighbors = {vertex_id: [[edge, edge.vertex_in_edge(vertex_id)]
                                     for edge in eulerian_graph
                                     if edge.vertex_in_edge(vertex_id) is not None]
                         for vertex_id in range(self.graph_size)}
        else:
            sub_path = True

        while len(eulerian_graph) > 0:
            curr_neighbors = neighbors[curr_id]
            if len(curr_neighbors) == 0:
                if sub_path:
                    break
                new_start, curr_edge = self.get_vertex_with_neighbor(neighbors, eulerian_circuit, curr_id)
                sub_circuit, eulerian_graph = self.make_eulerian_circuit(eulerian_graph, new_start, neighbors)
                insert_index = eulerian_circuit.index(curr_edge)
                eulerian_circuit[insert_index:0] = sub_circuit
            else:
                curr_neighbor = curr_neighbors[0]
                curr_edge = curr_neighbor[0]
                eulerian_circuit.append(curr_edge)
                eulerian_graph.remove(curr_edge)
                neighbors[curr_id].remove(curr_neighbor)
                curr_id = curr_neighbor[1]
                neighbors[curr_id] = [neighbor for neighbor in neighbors[curr_id] if neighbor[0] != curr_neighbor[0]]
        return eulerian_circuit, eulerian_graph

    def get_vertex_with_neighbor(self, neighbors, eulerian_circuit, curr_id):
        eulerian_circuit_vertices = [edge.vertex1 for edge in eulerian_circuit] + [eulerian_circuit[-1].vertex2]
        for vertex_id, curr_neighbors in neighbors.items():
            if len(curr_neighbors) > 0 and vertex_id in eulerian_circuit_vertices:
                return vertex_id, curr_neighbors[0][0]

    def make_hamiltonian_circuit(self, eulerian_circuit):
        return [vertex
                for i, vertex in enumerate(eulerian_circuit)
                if vertex not in eulerian_circuit[:i]]

    def remove_crossings(self, circuit):
        pass

    def calculate_distance(self, circuit):
        length = 0
        circuit += [circuit[0]]
        for i in range(len(circuit) - 1):
            id_1 = circuit[i]
            id_2 = circuit[i + 1]
            if id_1 > id_2:
                id_1, id_2 = id_2, id_1
            length += self.distance_matrix[id_1][id_2]
        return length


class ConnectedGroups:
    """Class for representing the connected groups. It stores and helps make operations on a list of groups.

    """
    def __init__(self, ids):
        self.groups = [[num] for num in ids]

    def __getitem__(self, item):
        return self.groups[item]

    def merge_elements_groups(self, e1, e2):
        self.merge_groups(self.get_element_group_nb(e1),
                          self.get_element_group_nb(e2))

    def merge_groups(self, g1, g2):
        self.groups[g1] += self.groups[g2]
        del(self.groups[g2])

    def elements_in_same_group(self, e1, e2):
        return self.get_element_group_nb(e1) == self.get_element_group_nb(e2)

    def get_element_group(self, element):
        return self[self.get_element_group_nb(element)]

    def get_element_group_nb(self, element):
        if self.element_is_in_group(element):
            return [element in group for group in self.groups].index(True)
        return -1

    def element_is_in_group(self, element):
        return any(element in group for group in self.groups)

    def __len__(self):
        return len(self.groups)

    def __repr__(self):
        return str(self.groups)


class Edge:
    """Class for representing edges.

    """
    def __init__(self, vertex1, vertex2, length):
        self.vertex1 = vertex1
        self.vertex2 = vertex2
        self.length = length

    def vertex_in_edge(self, vertex):
        if vertex == self.vertex1:
            return self.vertex2
        if vertex == self.vertex2:
            return self.vertex1

    def __lt__(self, other):
        """Check whether another edge is smaller than this one.

        :param other: Path
        :return: bool
        """
        return self.length < other.length

    def __eq__(self, other):
        return self.vertex1 == other.vertex1 and self.vertex2 == other.vertex2

    def __ne__(self, other):
        return self.vertex1 != other.vertex1 or self.vertex2 != other.vertex2

    def __repr__(self):
        return "{} - {}: {}".format(self.vertex1, self.vertex2, self.length)
