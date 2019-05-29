import abc
import itertools
import math
import sys


class Path:
    """Representation of a path.
    """
    lowest_length = sys.maxsize

    def __init__(self, points, length=None):
        """Class constructor.

        :param points: [Point]
        :param length: float, optional
        """
        self.points = points
        self.length = length
        if self.length is None:
            self.compute_length()

    def compute_length(self):
        """Compute the length of this path.

        :return: float
        """
        self.length = 0
        points_len_compute = self.points + (self.points[0], )
        for i in range(len(points_len_compute) - 1):
            self.length += points_len_compute[i].distance(points_len_compute[i + 1])
            # If we know that it's the wrong path, don't keep going.
            if self.length > Path.lowest_length:
                return
        if self.length < Path.lowest_length:
            Path.lowest_length = self.length

    def get_order(self):
        """Get the order of points compared to the order in the csv file.

        :return: [int]
        """
        return [point.id for point in self.points]

    def __lt__(self, other):
        """Check whether another path is smaller than this one.

        :param other: Path
        :return: bool
        """
        return self.length < other.length

    def __key(self):
        return self.get_order(), self.get_order()[1:][::-1]

    def __hash__(self):
        return hash(self.__key())

    def __eq__(self, other):
        """Check whether another path is equal to this one.

        :param other: Path
        :return: bool
        """
        return (type(self) == type(other)
                and (self.get_order() == other.get_order()
                     or self.get_order()[1:][::-1] == other.get_order()[1:][::-1]))

    def __str__(self) -> str:
        """String representation of a path.

        :return: str
        """
        return "length: {len}, order: {order}".format(len=self.length, order=self.get_order())


class Solver(abc.ABC):
    """Base class for solving the shortest path.

    """
    def __init__(self, points):
        """Class constructor.

        :param points: [Point]
        """
        self.vertices = points
        self.graph_size = len(self.vertices)

    @abc.abstractmethod
    def solve(self):
        pass


class OptimalSolver(Solver):
    """Finds the optimal path by computing the length of every path.

    Run time:
        test10.csv: 0.5317709445953369 seconds
        test4.csv: 0.0020487308502197266 seconds
        burma14.csv: 3.4314851244952944 hours
    """
    def __init__(self, points):
        """Class constructor.

        :param points: [Point]
        """
        super().__init__(points)
        self.paths = None

    def solve(self):
        """Get the shortest path.

        :return: Path
        """
        best_length = sys.maxsize
        best_path = None
        for route in itertools.permutations(self.vertices[1:]):
            if route[0].id < route[-1].id:  # No mirror
                curr_path = Path((self.vertices[0],) + route)
                if curr_path.length < best_length:
                    best_path = curr_path
                    best_length = curr_path.length
        return best_path


class ChristofidesSolver(Solver):
    """Implementation of the Christofides algorithm.

    """
    def __init__(self, points):
        super().__init__(points)
        self.distance_matrix = [[0 for j in range(self.graph_size)] for i in range(self.graph_size)]
        self.min_spanning_tree = []
        self.odd_degree_vertexes = []
        self.min_weight_matching = []

    def solve(self):
        self.make_distance_matrix()
        self.make_min_spanning_tree()
        self.find_odd_degree_vertices()
        self.find_min_weight_matching()
        self.make_eulerian_circuit()
        self.make_hamiltonian_circuit()

    def make_distance_matrix(self):
        """Make a distance matrix with the points.

        """
        self.distance_matrix = [[src.distance(dst)
                                 for dst in self.vertices[src.id + 1:]]
                                for src in self.vertices]

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

        groups = ConnectedGroups([vertex.id for vertex in self.vertices])
        for edge in edges:
            if not groups.elements_in_same_group(edge.vertex1, edge.vertex2):
                groups.merge_elements_groups(edge.vertex1, edge.vertex2)
                self.min_spanning_tree.append(edge)
            if len(groups) == 1:
                break

    def find_odd_degree_vertices(self):
        """Make a list of the vertices with an odd degree.

        """
        degrees = [0 for i in range(self.graph_size)]
        for edge in self.min_spanning_tree:
            degrees[edge.vertex1] += 1
            degrees[edge.vertex2] += 1

        self.odd_degree_vertexes = [i
                                    for i, degree in enumerate(degrees)
                                    if degree % 2 != 0]

    def find_min_weight_matching(self):
        """

        """
        edges = [Edge(x, y, cell)
                 for y, line in enumerate(self.distance_matrix)
                 for x, cell in enumerate(line)
                 if cell > 0 and cell in self.odd_degree_vertexes]
        edges = sorted(edges)

        groups = ConnectedGroups(self.odd_degree_vertexes)
        for edge in edges:
            if not groups.elements_in_same_group(edge.vertex1, edge.vertex2) \
                    and len(groups.get_element_group(edge.vertex1)) == 1 \
                    and len(groups.get_element_group(edge.vertex2)) == 1:
                groups.merge_elements_groups(edge.vertex1, edge.vertex2)
                self.min_weight_matching.append(edge)
            if len(groups) == 1:
                break

    def make_eulerian_circuit(self):
        pass

    def make_hamiltonian_circuit(self):
        pass


class ConnectedGroups:
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
    def __init__(self, vertex1, vertex2, length):
        self.vertex1 = vertex1
        self.vertex2 = vertex2
        self.length = length

    def __lt__(self, other):
        """Check whether another path is smaller than this one.

        :param other: Path
        :return: bool
        """
        return self.length < other.length

    def __repr__(self):
        return "{} - {}: {}".format(self.vertex1, self.vertex2, self.length)
