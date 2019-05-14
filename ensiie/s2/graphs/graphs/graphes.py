class Graph:
    def __init__(self, array):
        self.array = array
        self.graph_width = len(self.array)

        self.adjacency_matrix = []
        self.edge_levels = [[]]
        self.grundy_levels = []
        self.roy_warshall_result = None
        self.nodes = []
        self.line_graph = []

        self.make_adjacency_matrix()
        self.edge_leveling()
        self.grundy()
        self.roy_warshall()
        self.make_nodes()
        self.welsh_powel()
        self.make_line_graph()

    def make_adjacency_matrix(self):
        self.adjacency_matrix = [[1 if j in self.array[i] else 0
                                  for j in range(self.graph_width)]
                                 for i in range(self.graph_width)]

    def roy_warshall(self):
        self.roy_warshall_result = [[*line] for line in self.adjacency_matrix]
        for i, line in enumerate(self.adjacency_matrix):
            for j, cell in enumerate(line):
                if cell == 1:
                    new_children = self.adjacency_matrix[j]
                    for k in range(len(new_children)):
                        if new_children[k] == 1:
                            self.roy_warshall_result[i][k] = 1

    def edge_leveling(self):
        self.edge_levels[0] = [i for i in range(self.graph_width)
                               if 1 not in [line[i] for line in self.adjacency_matrix]]
        for curr_level in self.edge_levels:
            curr_result = []
            for edge in curr_level:
                edge_next = [i
                             for i in range(self.graph_width)
                             if self.adjacency_matrix[edge][i] == 1]
                curr_result += [edge for edge in edge_next
                                if not Graph.object_in_sublist(edge, self.edge_levels)
                                and edge not in curr_result]
            if len(curr_result) > 0:
                self.edge_levels.append(curr_result)
        if self.edge_levels == [[]]:
            # Graph is a loop, so every edge is at level 0
            self.edge_levels = [[i for i in range(self.graph_width)]]

    @staticmethod
    def object_in_sublist(o, l):
        for sublist in l:
            if o in sublist:
                return True
        return False

    def grundy(self):
        for i, level in enumerate(self.edge_levels):
            self.grundy_levels += [i] * len(level)

    def make_nodes(self):
        self.nodes = [Node(i,
                           set([
                               j
                               for j, node in enumerate(line)
                               if node == 1
                           ] + [
                               j
                               for j, line in enumerate(self.adjacency_matrix)
                               if line[i] == 1
                           ])
                           )
                      for i, line in enumerate(self.adjacency_matrix)]

    def welsh_powel(self):
        uncolored_nodes = [*self.nodes]
        curr_color = 0
        while len(uncolored_nodes) > 0:
            uncolored_nodes = sorted(uncolored_nodes, reverse=True)
            curr_node = uncolored_nodes.pop(0)
            curr_node.color = curr_color
            curr_color_nodes = [curr_node.id]
            curr_color_neighbors = set(curr_node.neighbors)

            for i, node in enumerate(uncolored_nodes):
                if node.id not in curr_color_nodes and node.id not in curr_color_neighbors:
                    uncolored_nodes.pop(i)
                    node.color = curr_color
                    curr_color_nodes.append(node.id)
                    curr_color_neighbors.update(node.neighbors)
            curr_color += 1

    def make_line_graph(self):
        pass


class Node:
    def __init__(self, n, neighbors, color=None):
        self.id = n
        if neighbors is None:
            self.neighbors = []
        else:
            self.neighbors = list(neighbors)
        self.color = color

    def __eq__(self, other):
        if self.neighbors is None or other.neighbors is None:
            return False
        return len(self.neighbors) == len(other.neighbors)

    def __lt__(self, other):
        if self.neighbors is None or other.neighbors is None:
            return False
        return len(self.neighbors) < len(other.neighbors)

    def __str__(self):
        return "node {}: neighbors: {}, color: {}".format(self.id, self.neighbors, self.color)
