import sys
import itertools

from travellingSalesman.solver.solver import Solver


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
