import numpy


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
