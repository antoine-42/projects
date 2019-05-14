import numpy as np
import matplotlib.pyplot as plt
import csv
import sys
import re


def plot(filename, solution):
    # Parse file
    x = []
    y = []
    with open(filename, 'r') as csvfile:
        reader = csv.reader(csvfile, delimiter=',')
        for row in reader:
            if (len(row) > 1):
                x.append(row[0])
                y.append(row[1])

    # Plot the points
    plt.scatter(x, y)

    # Add solution lines to the plot
    currX = x[0]
    currY = y[0]
    for point in solution.split('-'):
        nextX = x[int(point) - 1]
        nextY = y[int(point) - 1]
        plt.plot([currX, nextX], [currY, nextY], 'r-', lw=2)
        currX = nextX
        currY = nextY

    # Show plot
    plt.xlabel('x')
    plt.ylabel('y')
    plt.show()


def usage():
    print("Mauvais nombre d'argument:")
    print("usage: python pvcPlot.py <data.csv> <1-x-x-x-x-1>")


def wrongSol():
    print("Solution non acceptee:")
    print("La solution doit etre au format 1-x-x-x-x-1")


if __name__ == "__main__":
    # Regex solution pattern
    pattern = re.compile("^1\-(\d+\-)+1$")

    # Arguments control
    if (len(sys.argv) != 3):
        print(len(sys.argv))
        usage()
    else:
        filename = sys.argv[1]
        solution = sys.argv[2]
        if (pattern.match(solution)):
            plot(filename, solution)
        else:
            wrongSol()
