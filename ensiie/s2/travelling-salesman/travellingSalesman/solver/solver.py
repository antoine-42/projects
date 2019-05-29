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