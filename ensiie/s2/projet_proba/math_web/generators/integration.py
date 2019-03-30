import warnings
import math

import numpy

import math_web.generators


class IntegrationSolver:
    """Compute the solution of an integral.
    """
    def __init__(self, a, b):
        """Initialize and solve the integral.

        :param a: float
        :param b: float
        """
        if a is None:
            self.a = math_web.generators.generate_uniform_random_number(-10, 10, 0.5)
        else:
            self.a = a
        if b is None:
            self.b = math_web.generators.generate_uniform_random_number(-10, 10, 0.5, [a])
        else:
            self.b = b
        self.solution = None

        self.solve()

    def solve(self):
        """Solve the integral.
        """
        pass

    def get_mathjax_function(self):
        string = "\\(I=\\int_{{{a:.2g}}} ^ {{{b:.2g}}}<>dx\\)".format(a=self.a, b=self.b)
        return string

    def get_mathjax_solution(self):
        return "\\({solution:.2g}\\)".format(solution=self.solution)

    def __str__(self):
        """Get a string representing the integral.

        :return: str
        """
        result = "Integration from {a} to {b} of f(x).\n".format(a=self.a, b=self.b)
        result += "Solution: {solution}".format(solution=self.solution)
        return result

    def to_dict(self):
        dict = self.__dict__
        for key, value in dict.items():
            if isinstance(value, numpy.int64):
                dict[key] = int(value)
            if isinstance(value, numpy.complex128):
                dict[key] = float(value)
        return dict


class PowerAIntegrationSolver(IntegrationSolver):
    """Compute the solution of an integral where f(x) = (cx−d)^α.
    """
    def __init__(self, a=None, b=None, c=None, d=None, alpha=None):
        """Initialize and solve the integral.

        :param a: float
        :param b: float
        :param c: float
        :param d: float
        :param alpha: float
        """
        if c is None:
            self.c = math_web.generators.generate_uniform_random_number(-10, 10, 0.5, 0)
        else:
            self.c = c
        if d is None:
            self.d = math_web.generators.generate_uniform_random_number(-10, 10, 0.5)
        else:
            self.d = d
        if alpha is None:
            self.alpha = math_web.generators.generate_uniform_random_number(-10, 10, 1, -1)
        else:
            self.alpha = alpha
        IntegrationSolver.__init__(self, a, b)

    def solve(self):
        """Solve the integral.
        """
        # + 0j: Convert to complex to avoid errors when alpha is not an integer
        member1 = numpy.power(self.b * self.c - self.d + 0j, self.alpha + 1)
        member2 = numpy.power(self.a * self.c - self.d + 0j, self.alpha + 1)
        self.solution = \
            1 / (self.c * (self.alpha + 1)) \
            * (
                    member1
                    - member2
            )

    def get_mathjax_function(self):
        string = IntegrationSolver.get_mathjax_function(self)
        string = string.replace(
            "<>",
            "({c:.2g}x{d:+.2g})^{{{alpha:.2g}}}".format(c=self.c, d=-self.d, alpha=self.alpha))
        return string

    def __str__(self):
        """Get a string representing the integral.

        :return: str
        """
        result = IntegrationSolver.__str__(self)
        result += "\nf(x) = ({c}x − {d})^{alpha}".format(c=self.c, d=self.d, alpha=self.alpha)
        return result


class PowerBIntegrationSolver(IntegrationSolver):
    """Compute the solution of an integral where f(x) = 1/(x − c).
    """
    def __init__(self, a=None, b=None, c=None):
        """Initialize and solve the integral.

        :param a: float
        :param b: float
        :param c: float
        """
        if c is None:
            # c can't be equal to a or b.
            if a is None:
                a = math_web.generators.generate_uniform_random_number(-10, 10, 0.5)
            if b is None:
                b = math_web.generators.generate_uniform_random_number(-10, 10, 0.5)
            self.c = math_web.generators.generate_uniform_random_number(-10, 10, 0.5, [a, b])
        else:
            self.c = c
        IntegrationSolver.__init__(self, a, b)

    def solve(self):
        """Solve the integral.
        """
        self.solution = \
            math.log(abs(self.b - self.c)) \
            - math.log(abs(self.a - self.c))

    def get_mathjax_function(self):
        string = IntegrationSolver.get_mathjax_function(self)
        string = string.replace(
            "<>",
            "{{1\\over{{x{c:+.2g}}}}}".format(c=-self.c))
        return string

    def __str__(self):
        """Get a string representing the integral.

        :return: str
        """
        result = IntegrationSolver.__str__(self)
        result += "\nf(x) = 1/(x − {c})".format(c=self.c)
        return result


class TrigonometricAIntegrationSolver(IntegrationSolver):
    """Compute the solution of an integral where f(x) = cos(c * x).
    """
    def __init__(self, a=None, b=None, c=None):
        """Initialize and solve the integral.

        :param a: float
        :param b: float
        :param c: float
        """
        if c is None:
            self.c = math_web.generators.generate_uniform_random_number(-10, 10, 0.5, 0)
        else:
            self.c = c
        IntegrationSolver.__init__(self, a, b)

    def solve(self):
        """Solve the integral.
        """
        self.solution = (
                       math.sin(self.b * self.c)
                       - math.sin(self.a * self.c)
               ) / self.c

    def get_mathjax_function(self):
        string = IntegrationSolver.get_mathjax_function(self)
        string = string.replace(
            "<>",
            "\\cos({c:.2g} * x)".format(c=self.c))
        return string

    def __str__(self):
        """Get a string representing the integral.

        :return: str
        """
        result = IntegrationSolver.__str__(self)
        result += "\nf(x) = cos({c} * x)".format(c=self.c)
        return result


class TrigonometricBIntegrationSolver(IntegrationSolver):
    """Compute the solution of an integral where f(x) = sin(c * x).
    """
    def __init__(self, a=None, b=None, c=None):
        """Initialize and solve the integral.

        :param a: float
        :param b: float
        :param c: float
        """
        if c is None:
            self.c = math_web.generators.generate_uniform_random_number(-10, 10, 0.5, 0)
        else:
            self.c = c
        IntegrationSolver.__init__(self, a, b)

    def solve(self):
        """Solve the integral.
        """
        self.solution = (
                       math.cos(self.b * self.c)
                       - math.cos(self.a * self.c)
               ) / self.c

    def get_mathjax_function(self):
        string = IntegrationSolver.get_mathjax_function(self)
        string = string.replace(
            "<>",
            "\\sin({c:.2g} * x)".format(c=self.c))
        return string

    def __str__(self):
        """Get a string representing the integral.

        :return: str
        """
        result = IntegrationSolver.__str__(self)
        result += "\nf(x) = sin({c} * x)".format(c=self.c)
        return result


class TrigonometricCIntegrationSolver(IntegrationSolver):
    """Compute the solution of an integral where f(x) = tan(c * x).
    """
    def __init__(self, a=None, b=None, c=None):
        """Initialize and solve the integral.

        :param a: float
        :param b: float
        :param c: float
        """
        if c is None:
            self.c = math_web.generators.generate_uniform_random_number(-10, 10, 0.5, 0)
        else:
            self.c = c
        IntegrationSolver.__init__(self, a, b)

    def solve(self):
        """Solve the integral.
        """
        self.solution = (
                       math.log(abs(math.cos(self.b * self.c)))
                       - math.log(abs(math.cos(self.a * self.c)))
               ) / self.c

    def get_mathjax_function(self):
        string = IntegrationSolver.get_mathjax_function(self)
        string = string.replace(
            "<>",
            "\\tan({c:.2g} * x)".format(c=self.c))
        return string

    def __str__(self):
        """Get a string representing the integral.

        :return: str
        """
        result = IntegrationSolver.__str__(self)
        result += "\nf(x) = tan({c} * x)".format(c=self.c)
        return result


class LogarithmicIntegrationSolver(IntegrationSolver):
    """Compute the solution of an integral where f(x) = ln(c * x).
    """
    def __init__(self, a=None, b=None, c=None):
        """Initialize and solve the integral.

        :param a: float
        :param b: float
        :param c: float
        """
        if c is None:
            self.c = math_web.generators.generate_uniform_random_number(0, 10, 0.5, 0)
        else:
            self.c = c
        if a is None:
            self.a = math_web.generators.generate_uniform_random_number(-10, 10, 0.5, [0])
        else:
            self.a = a
        if b is None:
            self.b = math_web.generators.generate_uniform_random_number(-10, 10, 0.5, [0, self.a])
        else:
            self.b = b
        IntegrationSolver.__init__(self, self.a, self.b)

    def solve(self):
        """Solve the integral.
        """
        # numpy.log(0) will throw a warning.
        with warnings.catch_warnings():
            warnings.simplefilter("ignore")
            self.solution = (
                    self.b * numpy.log(self.b * self.c)
                    - self.a * numpy.log(self.a * self.c)
                    - self.c * numpy.log(self.b - self.a)
            )

    def get_mathjax_function(self):
        string = IntegrationSolver.get_mathjax_function(self)
        string = string.replace(
            "<>",
            "\\ln({c:.2g} * x)".format(c=self.c))
        return string

    def __str__(self):
        """Get a string representing the integral.

        :return: str
        """
        result = IntegrationSolver.__str__(self)
        result += "\nf(x) = ln({c} * x)".format(c=self.c)
        return result
