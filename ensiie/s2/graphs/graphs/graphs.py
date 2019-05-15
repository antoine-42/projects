"""Classes for representing and making computations on graphs.

"""


class Graph:
    """Class for representing and making computations on a graph.

    node_list: List of the next neighbor of the nodes in the graph: [[1, 2], [2], [3], [4], []].
    graph_width: Number of node in the graph.
    adjacency_matrix: Adjacency matrix of the graph: [[0, 1, 1, 0, 0], [0, 0, 1, 0, 0], [0, 0, 0, 1, 0], [0, 0, 0, 0, 1], [0, 0, 0, 0, 0]].
    edge_levels: List of how far each node is from a source: [[0], [1, 2], [3], [4]].
    grundy_levels: List of how far each node is from a source: [0, 1, 1, 2, 3].
    roy_warshall_result:
    nodes: List of the Nodes contained in the graph.
    line_graph: Graph where the nodes are the edges of the initial graph: [[1, 2], [2], [3], [4], []].
    """
    def __init__(self, node_list):
        """Graph initializer.

        Takes the node list as a start point to compute everything else.

        :param node_list:
        """
        self.node_list = node_list
        self.graph_width = len(self.node_list)

        self.adjacency_matrix = []
        self.edge_levels = [[]]
        self.grundy_levels = []
        self.roy_warshall_result = None
        self.nodes = []
        self.line_graph = None

        self.make_adjacency_matrix()
        self.edge_leveling()
        self.grundy()
        self.roy_warshall()
        self.make_nodes()
        self.welsh_powel()

    def make_adjacency_matrix(self):
        """Compute the adjacency matrix of the graph.

        """
        self.adjacency_matrix = [[1 if j in self.node_list[i] else 0
                                  for j in range(self.graph_width)]
                                 for i in range(self.graph_width)]

    def roy_warshall(self):
        """Computes the Roy-Warshall algorithm for the graph.

        """
        # Don't pollute the adjacency matrix
        self.roy_warshall_result = [list(line) for line in self.adjacency_matrix]
        for i, line in enumerate(self.adjacency_matrix):
            for j, cell in enumerate(line):
                if cell == 1:
                    new_children = self.adjacency_matrix[j]
                    for k in range(len(new_children)):
                        if new_children[k] == 1:
                            self.roy_warshall_result[i][k] = 1

    def edge_leveling(self):
        """Computes the level of the edges in the graph.

        """
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
        """Checks if an object is in a sublist of l.

        :param o: *
        :param l: [[]]
        :return: bool
        """
        for sublist in l:
            if o in sublist:
                return True
        return False

    def grundy(self):
        """Compute the Grundy algorithm for the graph.

        """
        for i, level in enumerate(self.edge_levels):
            self.grundy_levels += [i] * len(level)

    def make_nodes(self):
        """Make the nodes objects for this graph.

        For each line of the adjacency matrix:
            Turn it into a node:
                Make a list of the neighbors of this node:
                    children (all 1s in the current adjacency matrix line).
                    parents (all 1s in the current adjacency matrix column).
                Convert the list of neighbors into a set to eliminate duplicates.
                add Node(i, neighbors) to the list of nodes [self.nodes].
        """
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
        """Compute the Welsh-Powel algorithm for the graph.

        While all nodes have not been colored:
            Take a new color
            Try to assign it to as many node as possible, starting with the ones with the most neighbors.
        """
        uncolored_nodes = len(self.nodes)
        curr_color = 0
        while uncolored_nodes > 0:
            self.nodes = sorted(self.nodes, reverse=True)
            curr_color_nodes = []
            curr_color_neighbors = set()

            for i, node in enumerate(self.nodes):
                if node.color is None and node.id not in curr_color_nodes + list(curr_color_neighbors):
                    node.color = curr_color
                    curr_color_nodes.append(node.id)
                    curr_color_neighbors.update(node.neighbors)
                    uncolored_nodes -= 1
            curr_color += 1

    def make_line_graph(self):
        """Make a line graph from the graph

        in:  [[1, 2, 3], [4], [3], [4], []]        [[0, 1, 1, 1, 0], [0, 0, 0, 0, 1], [0, 0, 0, 1, 0], [0, 0, 0, 0, 1], [0, 0, 0, 0, 0]]
        out: [[1, 2, 3], [2, 4], [4, 5], [5], [5], []] [[0, 1, 1, 1, 0, 0], [0, 0, 0, 0, 0, 1], [0, 0, 0, 1, 1, 0], [0, 0, 0, 0, 1, 1], [0, 0, 0, 0, 1, 1], [0, 0, 0, 0, 0, 1]]
        """
        nodes = [(i, destination)
                 for i, destinations in enumerate(self.node_list)
                 for destination in destinations]

        line_graph_node_dict = {i: [i + j + 1
                                    for j, destination_node in enumerate(nodes[i + 1:])
                                    if not set(destination_node).isdisjoint(node)]
                                for i, node in enumerate(nodes)}
        line_graph_node_list = list(line_graph_node_dict.values())

        self.line_graph = Graph(line_graph_node_list)


class Node:
    """Class for representing a node of a graph.

    Stores the ID of its neighbors.

    """
    def __init__(self, n, neighbors, color=None):
        """Node initializer.

        :param n: int
        :param neighbors: [int]
        :param color: int|None
        """
        self.id = n
        if neighbors is None:
            self.neighbors = []
        else:
            self.neighbors = list(neighbors)
        self.color = color

    def __eq__(self, other):
        """Checks whether self == other.

        For this object, we only care about the neighbors.

        :param other: Node
        :return: bool
        """
        if self.neighbors is None or other.neighbors is None:
            return False
        return len(self.neighbors) == len(other.neighbors)

    def __lt__(self, other):
        """Checks whether self < other. If self < other, returns true, else false.

        :param other: Node
        :return: bool
        """
        if self.neighbors is None or other.neighbors is None:
            return False
        return len(self.neighbors) < len(other.neighbors)

    def __repr__(self):
        """String representation of a Node.

        :return: str
        """
        return "node {}: neighbors: {}, color: {}".format(self.id, self.neighbors, self.color)
