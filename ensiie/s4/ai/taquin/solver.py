import heapq

from game import *


class Solver:
    def __init__(self):
        self.game = Game()
        self.game.fill()

    def breadth_first_search(self) -> (bool, int):
        """Breadth first search algorithm.

        Add the start state to the queue.
        While the queue is not empty:
            Add all mutations of the current state that have not already been tried to the queue.
            if the current mutation if finished, exit.

        :return: True if a finished game was reached, the number of processed nodes, and if the game was finished the
        final path cost.
        """
        queue = [self.game]
        processed_nodes_total = 0
        past_states = set()
        while len(queue) > 0:
            curr_parent = queue.pop(0)
            past_states.add(curr_parent)
            for curr_child in curr_parent.generate_children():
                processed_nodes_total += 1
                if curr_child.finished():
                    return True, processed_nodes_total, curr_child.cost
                # Don't go back to a previous state, that would be a loop. Can't have a loop in a tree.
                if curr_child not in past_states and curr_child not in queue:
                    queue.append(curr_child)
        return False, processed_nodes_total, -1

    def depth_first_search(
            self,
            game: Game = None,
            max_depth: int = None,
            past_states: set = None
    ) -> (bool, int):
        """Depth first search algorithm.

        If no state is provided, use the default one.
        Compute every mutation of this state that have not already been tried.
        For each mutation:
            If it is at the finished state, exit.
            Otherwise, call the function on it.

        :param game: start game.
        :param max_depth: max depth search.
        :param past_states: set of past states
        :return: True if a finished game was reached, the number of processed nodes, the past states, and if the game
        was finished the final path cost.
        """
        processed_nodes_total = 0
        if past_states is None:
            past_states = set()
        if game is None:
            game = self.game
        past_states.add(game)
        for curr_child in game.generate_children():
            processed_nodes_total += 1
            if curr_child.finished():
                return True, processed_nodes_total, past_states, curr_child.cost
            # Don't go back to a previous state, that would be a loop.
            if curr_child not in past_states and (max_depth is None or max_depth > 1):  # Also depth check.
                result, new_processed_nodes, new_past_states, cost = self.depth_first_search(
                    curr_child,
                    None if max_depth is None else max_depth - 1,
                    past_states
                )
                # merge past states with the new states
                past_states |= new_past_states
                processed_nodes_total += new_processed_nodes
                if result:
                    return True, processed_nodes_total, past_states, cost
        return False, processed_nodes_total, past_states, -1

    def iterative_deepening_search(self) -> (bool, int):
        """Iterative deepening depth first search algorithm.

        :return: True if a finished game was reached, the number of processed nodes, and if the game was finished the
        final path cost.
        """
        i = 0
        processed_nodes_total = 0
        result = False
        while not result:
            result, new_processed_nodes, past_states, cost = self.depth_first_search(max_depth=i)
            processed_nodes_total += new_processed_nodes
            i += 1
        return result, processed_nodes_total, cost

    def a_star(self, heuristic: Heuristic) -> (bool, int):
        """A* algorithm using the requested heuristic.

        The queue is automatically sorted using the heapq library, which uses Game.__gt__, which uses the chosen
        heuristic.

        :param heuristic:
        :return: True if a finished game was reached, the number of processed nodes, and if the game was finished the
        final path cost.
        """
        game = self.game.copy()
        game.heuristic = heuristic
        queue = []
        heapq.heappush(queue, game)
        past_states = set()
        new_processed_nodes = 0
        while len(queue) > 0:
            parent = heapq.heappop(queue)
            past_states.add(parent)
            for child in parent.generate_children():
                new_processed_nodes += 1
                if child.finished():
                    return True, new_processed_nodes, child.cost
                # Don't go back to a previous state, that would be a loop.
                if child not in queue and child not in past_states:
                    heapq.heappush(queue, child)
        return False, new_processed_nodes, -1
