"""Classes that handle program inputs and outputs.
"""

import csv
import sys
import time

from travellingSalesman.point import Point


class Reader:
    """Read a CSV file, returns points.

    The file is a csv formatted in this way:
    Header line     < ignored
    float,float     < at least one of those
    ...
    """
    def __init__(self, file):
        """Class constructor.

        :param file: str
        """
        self.file = file

    def read_file(self):
        """Read the file, return Point array.

        Skip the first row, then call read_rows(). Contains error handling.
        If errors are found, the program will exit with an error message.

        :return: [Point]
        """
        try:
            with open(self.file) as file:
                csv_reader = csv.reader(file)
                next(csv_reader)  # Skip header
                try:
                    points = self.read_rows(csv_reader)
                except ValueError as e:
                    sys.exit('Error reading file {}, line {}: {}'.format(self.file, csv_reader.line_num, e))
                except csv.Error as e:
                    sys.exit('Error reading file {}, line {}: {}'.format(self.file, csv_reader.line_num, e))
        except IOError as e:
            sys.exit('Error reading file {}: {}'.format(self.file, e))
        if len(points) < 1:
            sys.exit('Error reading file {}: no point found in file'.format(self.file))
        return points

    def read_rows(self, csv_reader):
        """Read the file rows.

        :param csv_reader: csv.reader
        :return: [Point]
        """
        points = []
        for n, row in enumerate(csv_reader):
            x, y = [float(i) for i in row]
            points.append(Point(n, x, y))
        return points


class Writer:
    """Outputs program result.
    """
    def __init__(self, file, solution, start_time):
        """Class constructor

        :param file: str
        :param solution: solver.Path
        :param start_time: time.time
        """
        self.file = file
        self.solution = solution
        self.start_time = start_time

        self.out = ""
        self.make_text()
        self.output()

    def make_text(self):
        """Create the output text.
        """
        self.out += "solution:{}-1\n".format("-".join([str(i + 1) for i in self.solution.get_order()]))
        self.out += "distance:{}\n".format(self.solution.length)
        self.out += "temps:{} secondes\n".format(time.time() - self.start_time)

    def output(self):
        """If an output file is set, print the output to it. otherwise, send it to stdout.
        """
        if self.file is None:
            print(self.out)
        else:
            with open(self.file, "w") as out_file:
                out_file.write(self.out)
