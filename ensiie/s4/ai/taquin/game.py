import enum
import random


def index_2d(l: list, e):
    """Get the index of an element in a 2d list.

    :param l: list.
    :param e: element.
    :return: x, y
    """
    for i, sublist in enumerate(l):
        if e in sublist:
            return i, sublist.index(e)
    raise ValueError("{} is not in list".format(e))


class Move(enum.Enum):
    UP = 0
    RIGHT = 1
    DOWN = 2
    LEFT = 3


class Game:
    FINISHED_BOARD = [
        [1, 2, 3],
        [8, None, 4],
        [7, 6, 5]
    ]

    def __init__(self, width: int = 3, height: int = 3):
        """

        :param width:
        :param height:
        """
        self.width = width
        self.height = height
        self.tile_number = self.width * self.height - 1
        self.board = [[None for i in range(self.width)] for i in range(self.height)]

    def fill(self):
        """Randomly fill the board

        :return:
        """
        remaining_choices = list(range(1, self.tile_number + 1)) + [None]
        for x in range(self.width):
            for y in range(self.height):
                curr_choice = random.choice(remaining_choices)
                remaining_choices.remove(curr_choice)
                self.board[x][y] = curr_choice

    def display(self):
        """

        :return:
        """
        display = ""
        for line in self.board:
            for cell in line:
                if cell is None:
                    cell = "-"
                display += "{:<{width}} ".format(cell, width=len(str(self.tile_number)))
            display += "\n"
        print(display)

    def move(self, x: int, y: int, move: Move):
        """

        :param x:
        :param y:
        :param move:
        :return:
        """
        if move == Move.RIGHT:
            if y == self.height - 1:
                raise ValueError("Can't move this cell right")
            else:
                self.board[x][y], self.board[x][y + 1] = self.board[x][y + 1], self.board[x][y]
        elif move == Move.UP:
            if x == 0:
                raise ValueError("Can't move this cell up")
            else:
                self.board[x][y], self.board[x - 1][y] = self.board[x - 1][y], self.board[x][y]
        elif move == Move.LEFT:
            if y == 0:
                raise ValueError("Can't move this cell left")
            else:
                self.board[x][y], self.board[x][y - 1] = self.board[x][y - 1], self.board[x][y]
        elif move == Move.DOWN:
            if x == self.width - 1:
                raise ValueError("Can't move this cell down")
            else:
                self.board[x][y], self.board[x + 1][y] = self.board[x + 1][y], self.board[x][y]

    def finished(self) -> bool:
        """Check whether the game is finished

        :return: True if the game is finished, false otherwise
        """
        if self.board == Game.FINISHED_BOARD:
            return True
        return False

    def copy(self):
        """Return a copy of this object

        :return: Game
        """
        new_game = Game(self.width, self.height)
        new_game.board = self.board
        return new_game

    def h1(self) -> int:
        misplaced_number = 0
        for x in range(self.width):
            for y in range(self.height):
                if self.board[y][x] != Game.FINISHED_BOARD[y][x]:
                    misplaced_number += 1
        return misplaced_number

    def h2(self) -> int:
        total_distance = 0
        for x in range(self.width):
            for y in range(self.height):
                correct_x, correct_y = index_2d(Game.FINISHED_BOARD, self.board[x][y])
                total_distance += abs(correct_x - x) + abs(correct_y - y)
        return total_distance

    def h3s(self) -> int:
        score = 0
        for x in range(self.width):
            for y in range(self.height):
                if x == 1 and y == 1:
                    if self.board[x][y] is not None:
                        score += 1
                else:
                    successor = None
                    if x == 0 and y < 2:
                        successor = self.board[x + 1][y]
                    elif x < 2 and y == 2:
                        successor = self.board[x][y + 1]
                    elif x == 2 and y > 0:
                        successor = self.board[x - 1][y]
                    elif x > 0 and y == 0:
                        successor = self.board[x - 1][y]
                    if self.board[x][y] is None or successor is None or self.board[x][y] + 1 != successor:
                        score += 2
        return score

    def h3(self) -> int:
        return self.h2() + 3 * self.h3s()
