import math
import functools


class Vertex:
    """Representation of a vertex.
    """

    def __init__(self, n, x, y):
        """Class constructor.

        :param n: int
        :param x: float
        :param y: float
        """
        self.id = n
        self.x = x
        self.y = y

    @functools.lru_cache(maxsize=1000000)
    def distance(self, other):
        """Compute the distance between this vertex and another one.

        :param other: Vertex
        :return: float
        """
        return math.sqrt((other.x - self.x) ** 2 + (other.y - self.y) ** 2)

    def __str__(self) -> str:
        """String representation of a vertex.

        :return: str
        """
        return "id: {id}, x: {x}, y: {y}".format(id=self.id, x=self.x, y=self.y)
