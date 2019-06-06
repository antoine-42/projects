import sys
import abc


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


class Path:
    """Representation of a path.
    """
    lowest_length = sys.maxsize

    def __init__(self, vertices, length=None):
        """Class constructor.

        :param vertices: [Point]
        :param length: float, optional
        """
        if not isinstance(vertices, tuple):
            vertices = tuple(vertices)
        self.vertices = vertices
        self.length = length
        if self.length is None:
            self.compute_length()

    def compute_length(self):
        """Compute the length of this path.

        :return: float
        """
        self.length = 0
        points_len_compute = self.vertices + (self.vertices[0],)
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
        return [vertex.id for vertex in self.vertices]

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
