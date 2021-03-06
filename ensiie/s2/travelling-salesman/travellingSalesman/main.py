import os
import argparse
import time

from travellingSalesman.inputOutput import Reader, Writer
from travellingSalesman.vertex import Vertex
from travellingSalesman.pvcPlot import plot
import travellingSalesman.solver


class Main:
    def __init__(self):
        """Main.
        """
        self.input_path = None
        self.output_file = None
        start_time = time.time()
        self.read_args()

        if os.path.isdir(self.input_path):
            files = [os.path.join(self.input_path, file)
                     for file in os.listdir(self.input_path)
                     if file.endswith('.csv')]
            processed = 0
            total_len = 0
            for file in files:
                if os.path.isfile(file):
                    print(file.split("/")[-1])
                    total_len += self.process_file(file)
                    processed += 1
            print("Total time for {} files: {} seconds. Total computed paths length: {}".format(
                processed, time.time() - start_time, total_len))
        else:
            self.process_file(self.input_path)

    def process_file(self, file):
        start_time = time.time()
        try:
            reader = Reader(file)
            points = reader.read_file()

            solver = travellingSalesman.solver.ChristofidesSolver(points, True)
            solution = solver.solve()
        except Exception as e:
            print("Error while processing {}: {}\n".format(file, e))
            return

        Writer(self.output_file, solution, start_time)
        solution_text = "{}-1".format("-".join([str(s + 1) for s in solution.get_order()]))
        plot(file, solution_text)
        return solution.length

    def read_args(self):
        """Read the arguments.
        """
        parser = argparse.ArgumentParser()
        parser.add_argument('-i', '--input-file', type=str, required=True,
                            help="Input file or directory. If directory, all valid CSV files will be processed")
        parser.add_argument('-o', '--output-file', type=str,
                            help="Output file. If not set, program will output to stdout.")

        args = parser.parse_args()
        self.input_path = args.input_file
        self.output_file = args.output_file


if __name__ == "__main__":
    Main()
