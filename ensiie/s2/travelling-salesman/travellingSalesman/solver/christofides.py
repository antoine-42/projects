from travellingSalesman.solver.solver import Solver, Path


class ChristofidesSolver(Solver):
    """Implementation of the Christofides algorithm.

    test5.csv:
        solution:1-3-4-5-2-1
        distance:4.414213562373095
        temps:0.003407716751098633 secondes
    test10.csv
        solution:1-2-4-5-6-9-10-3-8-7-1
        distance:99.13860358020509
        temps:0.008467912673950195 secondes
    berlin52:
        solution:1-22-31-18-3-17-21-42-7-2-30-23-20-50-29-16-44-34-35-36-49-39-40-37-38-24-48-46-47-26-27-13-14-52-28-12-51-11-25-4-6-5-15-43-33-9-10-8-41-19-45-32-1
        distance:8611.41625744787
        temps:0.05182600021362305 secondes
    qatar194:
        solution:1-25-23-13-14-17-26-24-21-18-33-28-29-22-45-57-64-60-69-74-78-75-72-76-71-80-87-82-62-59-36-63-65-20-85-86-98-90-89-94-99-101-104-111-130-127-125-126-132-134-137-140-145-156-149-146-142-70-77-79-81-84-83-88-92-95-124-123-128-120-121-117-129-135-133-131-136-143-148-155-151-160-166-171-170-167-180-185-178-181-177-184-188-193-191-192-189-190-194-187-186-183-168-165-175-173-174-179-172-169-163-161-164-176-182-158-159-162-147-152-153-150-144-141-157-154-139-138-116-115-112-110-100-108-107-105-106-97-96-93-91-103-102-109-113-114-119-122-118-6-8-16-11-7-4-2-3-5-9-10-12-15-19-30-32-31-35-42-50-49-55-44-46-41-38-43-40-34-39-47-51-37-27-48-52-54-53-56-58-61-67-73-66-68-1
        distance:12916.300114461586
        temps:1.0740315914154053 secondes

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
        hamiltonian_circuit_points = [self.vertices[i] for i in hamiltonian_circuit]
        return Path(hamiltonian_circuit_points)

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
                circuit_neighbors = [vertex_id
                                     for vertex_id, curr_neighbors in neighbors.items()
                                     if len(curr_neighbors) > 0 and vertex_id in eulerian_circuit]
                new_start = circuit_neighbors[0]
                sub_circuit, eulerian_graph = self.make_eulerian_circuit(eulerian_graph, new_start, neighbors)
                insert_index = eulerian_circuit.index(new_start)
                del(eulerian_circuit[insert_index])
                eulerian_circuit[insert_index:0] = sub_circuit
            else:
                curr_neighbor = curr_neighbors[0]
                eulerian_circuit.append(curr_id)
                eulerian_graph.remove(curr_neighbor[0])
                neighbors[curr_id].remove(curr_neighbor)
                curr_id = curr_neighbor[1]
                neighbors[curr_id] = [neighbor for neighbor in neighbors[curr_id] if neighbor[0] != curr_neighbor[0]]
        eulerian_circuit.append(curr_id)
        return eulerian_circuit, eulerian_graph

    def make_hamiltonian_circuit(self, eulerian_circuit):
        return [vertex
                for i, vertex in enumerate(eulerian_circuit)
                if vertex not in eulerian_circuit[:i]]


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
