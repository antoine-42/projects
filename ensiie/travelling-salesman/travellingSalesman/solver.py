import abc
import itertools
import math
import sys

from travellingSalesman.point import Point


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
            self.length += points_len_compute[i].cache_distance(points_len_compute[i + 1])
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
        self.points = points

    @abc.abstractmethod
    def solve(self):
        pass


class OptimalSolver(Solver):
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
        self.calculate_distances()
        best_length = sys.maxsize
        best_path = None
        for route in itertools.permutations(self.points[1:]):
            if route[0].id < route[-1].id:  # No mirror
                curr_path = Path((self.points[0], ) + route)
                if curr_path.length < best_length:
                    best_path = curr_path
                    best_length = curr_path.length
        return best_path

    def calculate_distances(self):
        for src in self.points:
            for dst in self.points[src.id+1:]:
                Point.distances[src.id][dst.id] = src.distance(dst)
                Point.distances[dst.id][src.id] = Point.distances[src.id][dst.id]


class HeuristicSolver(Solver):
    def solve(self):
        pass


class ChristofidesSolver(Solver):
    def solve(self):
        pass
