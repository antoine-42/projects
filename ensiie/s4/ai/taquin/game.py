import copy
import enum
import random


def index_2d(l: list, e):
    """Get the position of an element in a 2d list.

    :param l: list.
    :param e: element.
    :return: x, y
    """
    for i, sublist in enumerate(l):
        if e in sublist:
            return i, sublist.index(e)
    raise ValueError("{} is not in list".format(e))


class Direction(enum.Enum):
    UP = 0
    RIGHT = 1
    DOWN = 2
    LEFT = 3


class Heuristic(enum.Enum):
    H1 = 0
    H2 = 1
    H3s = 2
    H3 = 3


class Game:
    FINISHED_BOARD = [
        [1, 2, 3],
        [8, None, 4],
        [7, 6, 5]
    ]

    def __init__(self,
                 width: int = 3,
                 height: int = 3,
                 cost: int = 0,
                 heuristic: Heuristic = Heuristic.H3):
        """Game constructor.

        :param width: Not meant to be changed from default.
        :param height: Not meant to be changed from default.
        :param cost: Cost to reach this state.
        :param heuristic: Heuristic used to compute the value of this board.
        """
        self.width = width
        self.height = height
        self.tile_number = self.width * self.height - 1
        self.board = [[None for i in range(self.width)] for i in range(self.height)]
        self.cost = cost
        self.heuristic = heuristic

    def fill(self):
        """Randomly fill the board.
        """
        remaining_choices = list(range(1, self.tile_number + 1)) + [None]
        for x in range(self.width):
            for y in range(self.height):
                curr_choice = random.choice(remaining_choices)
                remaining_choices.remove(curr_choice)
                self.board[x][y] = curr_choice

    def display(self):
        """Display the board
        """
        display = ""
        for line in self.board:
            for cell in line:
                if cell is None:
                    cell = "-"
                display += "{:<{width}} ".format(cell, width=len(str(self.tile_number)))
            display += "\n"
        print(display)

    def move(self,
             direction: Direction):
        """Move the None tile in a direction, and increment the cost.

        :param direction:
        :return:
        """
        x, y = index_2d(self.board, None)
        if direction == Direction.RIGHT:
            if y == self.height - 1:
                raise ValueError("Can't move this cell right")
            else:
                self.board[x][y], self.board[x][y + 1] = self.board[x][y + 1], self.board[x][y]
        elif direction == Direction.UP:
            if x == 0:
                raise ValueError("Can't move this cell up")
            else:
                self.board[x][y], self.board[x - 1][y] = self.board[x - 1][y], self.board[x][y]
        elif direction == Direction.LEFT:
            if y == 0:
                raise ValueError("Can't move this cell left")
            else:
                self.board[x][y], self.board[x][y - 1] = self.board[x][y - 1], self.board[x][y]
        elif direction == Direction.DOWN:
            if x == self.width - 1:
                raise ValueError("Can't move this cell down")
            else:
                self.board[x][y], self.board[x + 1][y] = self.board[x + 1][y], self.board[x][y]
        self.cost += 1

    def generate_children(self) -> list:
        """Generate all possible states that can be made from the current state of the board.

        :return: [Game]
        """
        children = []
        for move in Direction:
            curr_child = self.copy()
            try:
                curr_child.move(move)
            except ValueError:
                continue
            children.append(curr_child)
        return children

    def finished(self) -> bool:
        """Check whether the game is finished.

        :return: True if the game is finished, false otherwise.
        """
        if self.board == Game.FINISHED_BOARD:
            return True
        return False

    def copy(self):
        """Return a copy of this object.

        :return: Game
        """
        new_game = Game(self.width, self.height)
        new_game.board = copy.deepcopy(self.board)
        new_game.cost = self.cost
        new_game.heuristic = self.heuristic
        return new_game

    def h1(self) -> int:
        """Compute the number of incorrectly placed tiles.

        :return:
        """
        misplaced_number = 0
        for x in range(self.width):
            for y in range(self.height):
                if self.board[y][x] != Game.FINISHED_BOARD[y][x]:
                    misplaced_number += 1
        return misplaced_number

    def h2(self) -> int:
        """Compute the sum of the distances from the tiles to their correct position.

        :return:
        """
        total_distance = 0
        for x in range(self.width):
            for y in range(self.height):
                correct_x, correct_y = index_2d(Game.FINISHED_BOARD, self.board[x][y])
                total_distance += abs(correct_x - x) + abs(correct_y - y)
        return total_distance

    def h3s(self) -> int:
        """Compute the score of the board:
        If the center is not None: +1
        If a border tile's successor is not tile+1: +2

        :return:
        """
        score = 0
        for x in range(self.width):
            for y in range(self.height):
                if x == 1 and y == 1:
                    if self.board[x][y] is not None:
                        score += 1
                else:
                    successor = None
                    if x == 0 and y < 2:
                        successor = self.board[x][y + 1]
                    elif x < 2 and y == 2:
                        successor = self.board[x + 1][y]
                    elif x == 2 and y > 0:
                        successor = self.board[x][y - 1]
                    elif x > 0 and y == 0:
                        successor = self.board[x - 1][y]
                    if self.board[x][y] is None or successor is None or self.board[x][y] + 1 != successor:
                        score += 2
        return score

    def h3(self) -> int:
        """h2 + 3*h3s

        :return:
        """
        return self.h2() + 3 * self.h3s()

    def compute_heuristic(self) -> int:
        if self.heuristic == Heuristic.H1:
            return self.h1() + self.cost
        if self.heuristic == Heuristic.H2:
            return self.h2() + self.cost
        if self.heuristic == Heuristic.H3s:
            return self.h3s() + self.cost
        if self.heuristic == Heuristic.H3:
            return self.h3() + self.cost

    def __eq__(self, o: object) -> bool:
        return type(o) == Game and self.compute_heuristic() == o.compute_heuristic()

    def __gt__(self, o: object) -> bool:
        return type(o) == Game and self.compute_heuristic() > o.compute_heuristic()

    def __repr__(self):
        return str(self.board)

    def __hash__(self):
        return hash(str(self.board))
