import argparse
import time
import os

from travellingSalesman.inputOutput import Reader, Writer
from travellingSalesman.point import Point
from travellingSalesman.pvcPlot import plot
import travellingSalesman.solver


class Main:
    def __init__(self):
        """Main.
        """
        start_time = time.time()
        self.input_file = None
        self.output_file = None
        self.read_args()

        reader = Reader(self.input_file)
        self.points = reader.read_file()

        Point.distances = [[None] * len(self.points) for i in range(len(self.points))]

        solver = travellingSalesman.solver.OptimalSolver(self.points)
        self.solution = solver.solve()

        Writer(self.output_file, self.solution, start_time)
        plot(self.input_file, "{}-1".format("-".join([str(s + 1) for s in self.solution.get_order()])))

    def read_args(self):
        """Read the arguments.
        """
        parser = argparse.ArgumentParser()
        parser.add_argument('-i', '--input-file', type=str, required=True,
                            help="Input file.")
        parser.add_argument('-o', '--output-file', type=str,
                            help="Output file. If not set, program will output to stdout.")

        args = parser.parse_args()
        self.input_file = args.input_file
        self.output_file = args.output_file


if __name__ == "__main__":
    Main()
