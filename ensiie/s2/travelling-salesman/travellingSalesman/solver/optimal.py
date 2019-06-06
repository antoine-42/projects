import sys
import itertools

from travellingSalesman.solver.solver import Solver, Path


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
