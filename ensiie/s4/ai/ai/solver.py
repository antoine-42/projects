from taquin.game import *


class Solver:
    def __init__(self):
        self.game = Game()
        self.game.fill()

    def breadth_first_search(self) -> (bool, int):
        """Breadth first search algorithm.

        :return: True if a finished game was reached.
        """
        queue = [(self.game, 0)]
        traversed_nodes = 0
        while len(queue) > 0:
            curr_parent = queue.pop(0)
            curr_parent_game = curr_parent[0]
            curr_path_length = curr_parent[1]
            for x in range(curr_parent_game.width):
                for y in range(curr_parent_game.height):
                    # Don't iterate over all moves, otherwise we'll get a lot of duplicate states.
                    for move in [Move.UP, Move.RIGHT]:
                        curr_child = curr_parent_game.copy()
                        try:
                            curr_child.move(x, y, move)
                        except ValueError:
                            continue
                        traversed_nodes += 1
                        if curr_child.finished():
                            return True, traversed_nodes, curr_path_length
                        queue.append((curr_child, curr_path_length + 1))
        return False, traversed_nodes

    def depth_first_search(
            self,
            game: Game = None,
            max_depth: int = None,
            traversed_nodes: int = 0,
            path_length: int = 0
    ) -> (bool, int):
        """Depth first search algorithm.

        :param game: start game.
        :param max_depth: max depth search.
        :param traversed_nodes:
        :param path_length:
        :return: True if a finished game was reached.
        """
        if game is None:
            game = self.game
        for x in range(game.width):
            for y in range(game.height):
                # Don't iterate over all moves, otherwise we'll get a lot of duplicate states.
                for move in [Move.UP, Move.RIGHT]:
                    curr_child = game.copy()
                    try:
                        curr_child.move(x, y, move)
                    except ValueError:
                        continue
                    traversed_nodes += 1
                    if curr_child.finished():
                        return True, traversed_nodes, path_length
                    if max_depth is None or max_depth > 1:
                        result, new_traversed_nodes, result_path_length = self.depth_first_search(
                            curr_child,
                            None if max_depth is None else max_depth - 1,
                            path_length + 1)
                        traversed_nodes += new_traversed_nodes
                        if result:
                            return True, traversed_nodes, result_path_length
        return False, traversed_nodes, -1

    def iterative_deepening_search(self) -> (bool, int):
        """Iterative deepening depth first search algorithm.

        :return: True if a finished game was reached.
        """
        i = 0
        traversed_nodes = 0
        result = False
        while not result:
            result, new_traversed_nodes = self.depth_first_search(max_depth=i)
            traversed_nodes += new_traversed_nodes
            i += 1
        return result, traversed_nodes

