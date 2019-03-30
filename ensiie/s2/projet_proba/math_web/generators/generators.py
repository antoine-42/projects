import numpy
from math_web.generators.polynomial import PolynomialSolver
from math_web.generators.integration import PowerAIntegrationSolver, PowerBIntegrationSolver
from math_web.generators.integration import TrigonometricAIntegrationSolver, TrigonometricBIntegrationSolver, TrigonometricCIntegrationSolver
from math_web.generators.integration import LogarithmicIntegrationSolver


def generate_uniform_random_number(start, end, step, remove_values=None):
    """Generates a random number between start and end, with a step and without some values.

    :param start: float
    :param end: float
    :param step: float
    :param remove_values: [float]
    :return: float
    """
    possible = numpy.arange(start, end, step)
    if remove_values is not None:
        possible = numpy.setdiff1d(possible, remove_values)
    return numpy.random.choice(possible)


def deserialize_solver_object(json):
    if "class" not in json:
        return ValueError("A class field is required for deserialization")
    class_name = json["class"]
    if class_name == "RandomPolynomialSolver":
        args = {key: value for key, value in json.items() if key in ["a", "b", "c"]}
        return PolynomialSolver(**args)
    elif class_name == "PowerAIntegrationSolver":
        args = {key: value for key, value in json.items() if key in ["a", "b", "c", "d", "alpha"]}
        return PowerAIntegrationSolver(**args)
    elif class_name == "PowerBIntegrationSolver":
        args = {key: value for key, value in json.items() if key in ["a", "b", "c"]}
        return PowerBIntegrationSolver(**args)
    elif class_name == "TrigonometricAIntegrationSolver":
        args = {key: value for key, value in json.items() if key in ["a", "b", "c"]}
        return TrigonometricAIntegrationSolver(**args)
    elif class_name == "TrigonometricBIntegrationSolver":
        args = {key: value for key, value in json.items() if key in ["a", "b", "c"]}
        return TrigonometricBIntegrationSolver(**args)
    elif class_name == "TrigonometricCIntegrationSolver":
        args = {key: value for key, value in json.items() if key in ["a", "b", "c"]}
        return TrigonometricCIntegrationSolver(**args)
    elif class_name == "LogarithmicIntegrationSolver":
        args = {key: value for key, value in json.items() if key in ["a", "b", "c"]}
        return LogarithmicIntegrationSolver(**args)
    else:
        return ValueError("The class is not valid")
