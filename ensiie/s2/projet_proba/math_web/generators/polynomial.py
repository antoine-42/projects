import math
import numpy

import math_web.generators
from math_web.generators.solver import Solver


class PolynomialSolver(Solver):
    """Compute the roots of a polynomial function.
    """
    def __init__(self, a, b, c):
        """Initialize and compute the roots.

        :param a: float
        :param b: float
        :param c: float
        """
        Solver.__init__(self)

        self.a = a
        self.b = b
        self.c = c
        self.delta = None
        self.solution = None
        self.points = 1

        self.compute_delta()
        self.compute_roots()

    def compute_delta(self):
        """Compute the value of delta.
        """
        self.delta = self.b ** 2 - 4 * self.a * self.c

    def np_compute_roots(self):
        """Compute the roots of the polynomial with Numpy.

        :return: [int]
        """
        results = numpy.roots([self.a, self.b, self.c])
        results = results[~numpy.iscomplex(results)]  # Remove complex results.
        results = results.tolist()  # Convert numpy type to list.
        results = list(set(results))  # Remove duplicates.
        results = sorted(results)
        return results

    def compute_roots(self):
        """Compute the roots of the polynomial.
        """
        # First degree polynomial
        if self.a == 0:
            if self.b == 0:
                self.solution = []
            else:
                self.solution = [-self.c / self.b]
        # Second degree polynomial
        elif self.delta < 0:
            self.solution = []
        elif self.delta == 0:
            self.solution = [self._compute_root(0)]
        elif self.delta > 0:
            self.solution = sorted([self._compute_root(self.delta, True),
                                    self._compute_root(self.delta)])

    def _compute_root(self, delta, negative_delta=False):
        """Compute a root of the polynomial.

        :param delta: Delta value that should be used.
        :param negative_delta: Should the delta value be negative?
        :return: int
        """
        sign = 1
        if negative_delta:
            sign = -1
        delta_root = sign * math.sqrt(delta)
        return (-self.b + delta_root) / (2 * self.a)

    def round_solutions(self, n):
        """Round the solutions.

        :param n: Precision.
        """
        self.solution = [round(solution, n) for solution in self.solution]

    def get_mathjax_function(self):
        return "\({a:.2g}x^2 {b:+.2g}x {c:+.2g} = 0\)".format(a=self.a, b=self.b, c=self.c)

    @staticmethod
    def get_mathjax_solution(solutions):
        string = ""
        if len(solutions) > 0:
            string += "\("
            for i, solution in enumerate(solutions):
                string += "x_{i} = {solution:.2g}, ".format(i=i, solution=solution)
            string = string[:-2] + "\)"
        else:
            string += "No root."
        return string

    def mathjax_solution(self):
        return PolynomialSolver.get_mathjax_solution(self.solution)

    def __str__(self):
        """Get a string representing the polynomial.

        :return: str
        """
        string = "Polynomial function {a}x² {b:+}x {c:+}\n".format(a=self.a, b=self.b, c=self.c)
        string += "∆ = {delta}\n".format(delta=self.delta)
        if len(self.solution) > 0:
            string += "Root"
            if len(self.solution) > 1:
                string += "s"
            string += ": " + ', '.join([str(solution) for solution in self.solution])
        else:
            string += "No root."
        return string

    def to_dict(self):
        dict = self.__dict__
        for key, value in dict.items():
            if isinstance(value, numpy.int64):
                dict[key] = int(value)
            if isinstance(value, numpy.complex128):
                dict[key] = float(value)
        return dict


class RandomPolynomialSolver(PolynomialSolver):
    """Compute the roots of a random polynomial function.
    """
    def __init__(self):
        """Generate random vales for the polynomial, and solve it.

        """
        a = math_web.generators.generate_uniform_random_number(-10, 10, 0.5)
        b = math_web.generators.generate_uniform_random_number(-10, 10, 0.5)
        c = math_web.generators.generate_uniform_random_number(-10, 10, 0.5)
        PolynomialSolver.__init__(self, a, b, c)
