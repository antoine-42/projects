import math


class Point:
    """Representation of a point.
    """
    distances = {}

    def __init__(self, n, x, y):
        """Class constructor.

        :param n: int
        :param x: float
        :param y: float
        """
        self.id = n
        self.x = x
        self.y = y

    def cache_distance(self, other):
        """Compute the distance between this point and another one, with cache.

        :param other: Point
        :return: float
        """
        return Point.distances[self.id][other.id]

    def distance(self, other):
        """Compute the distance between this point and another one.

        :param other: Point
        :return: float
        """
        return math.sqrt((other.x - self.x) ** 2 + (other.y - self.y) ** 2)

    def __str__(self) -> str:
        """String representation of a point.

        :return: str
        """
        return "id: {id}, x: {x}, y: {y}".format(id=self.id, x=self.x, y=self.y)
