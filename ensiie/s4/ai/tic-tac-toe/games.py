"""Games, or Adversarial Search (Chapter 5)"""
import copy
from collections import namedtuple
import random
import heapq
import copy
import math

from utils import argmax

infinity = float('inf')
GameState = namedtuple('GameState', 'to_move, utility, board, moves')


# ______________________________________________________________________________
# Minimax Search


def f_eval(state, player):
    board_width = 3

    def line_score(board, curr_player, coordinate_generator):
        player_count = 0
        for j in range(1, board_width + 1):
            coordinates = coordinate_generator(j)
            if coordinates in board:
                if board[coordinates] == curr_player:
                    player_count += 1
                else:
                    player_count = 0
                    break
        if player_count == board_width:
            return infinity
        if player_count > 0:
            return 10 ** (player_count - 1)
        return 0

    def v_h_score(board, curr_player, horizontal: bool = True) -> int:
        score = 0
        for i in range(1, board_width + 1):
            score += line_score(board, curr_player, lambda j: (i, j) if horizontal else (j, i))
        return score

    def total_unfinished_lines_number(board, curr_player) -> int:
        return v_h_score(board, curr_player, True) \
               + v_h_score(board, curr_player, False) \
               + line_score(board, curr_player, lambda i: (i, i)) \
               + line_score(board, curr_player, lambda i: (i, board_width + 1 - i))

    if type(state) != GameState:
        return 0
    board_width = int(math.sqrt(len(state.board) + len(state.moves)))
    res = total_unfinished_lines_number(state.board, player)
    opponent = 'X' if player == 'O' else 'O'
    res -= total_unfinished_lines_number(state.board, opponent)
    return res


def minimax_decision(state, game, prof_max: int = 20):
    """Given a state in a game, calculate the best move by searching
    forward all the way to the terminal states. [Figure 5.3]"""
    global expandedNodes

    player = game.to_move(state)
    prof = 0
    expandedNodes = 0

    def max_value(state, prof, profMax):
        global expandedNodes
        prof += 1
        expandedNodes += 1
        if game.terminal_test(state):
            # print "profondeur atteinte {}".format(prof)
            return game.utility(state, player)
        if profMax - prof <= 0:
            return f_eval(state, player)
        v = -infinity
        for a in game.actions(state):
            v = max(v, min_value(game.result(state, a), prof, profMax))
        return v

    def min_value(state, prof, profMax):
        global expandedNodes
        prof += 1
        expandedNodes += 1
        if game.terminal_test(state):
            # print "profondeur atteinte {}".format(prof)
            return game.utility(state, player)
        if profMax - prof <= 0:
            return f_eval(state, player)
        v = infinity
        for a in game.actions(state):
            v = min(v, max_value(game.result(state, a), prof, profMax))
        return v

    # Body of minimax_decision:
    return argmax(game.actions(state),
                  key=lambda a: min_value(game.result(state, a), prof, prof_max))


# ______________________________________________________________________________


def alphabeta_search(state, game, prof_max: int = 20):
    """Search game to determine best action; use alpha-beta pruning.
    As in [Figure 5.7], this version searches all the way to the leaves."""
    global expandedNodes

    player = game.to_move(state)
    prof = 0
    expandedNodes = 0

    def sort_f_eval(a: tuple) -> int:
        """Play the a move to the current state, and return its f_eval result.
        """
        if type(state) == str:
            return 0
        a_state = copy.deepcopy(state)
        a_state.board[a] = player
        opponent = 'X' if player == 'O' else 'O'
        return f_eval(a_state, opponent)

    # Functions used by alphabeta
    def f_eval_key(action: (int, int)) -> int:
        if type(state) != GameState:
            return 0
        action_state = copy.deepcopy(state)
        action_state.board[action] = player
        return f_eval(action_state, player)

    def max_value(state, prof, alpha, beta):
        global expandedNodes
        prof += 1
        expandedNodes += 1
        if game.terminal_test(state):
            return game.utility(state, player)
        if prof_max - prof <= 0:
            return f_eval(state, player)
        v = -infinity
        actions = game.actions(state)
        actions.sort(key=sort_f_eval)
        for a in actions:
            v = max(v, min_value(game.result(state, a), prof, alpha, beta))
            if v >= beta:
                return v
            alpha = max(alpha, v)
        return v

    def min_value(state, prof, alpha, beta):

        global expandedNodes
        prof += 1
        expandedNodes += 1
        if game.terminal_test(state):
            return game.utility(state, player)
        if prof_max - prof <= 0:
            return f_eval(state, player)
        v = infinity
        actions = game.actions(state)
        actions.sort(key=sort_f_eval)
        for a in actions:
            v = min(v, max_value(game.result(state, a), prof, alpha, beta))
            if v <= alpha:
                return v
            beta = min(beta, v)
        return v

    # Body of alphabeta_cutoff_search:
    best_score = -infinity
    beta = infinity
    best_action = None
    for a in game.actions(state):
        v = min_value(game.result(state, a), prof, best_score, beta)
        if v > best_score:
            best_score = v
            best_action = a
    return best_action


def alphabeta_cutoff_search(state, game, d=4, cutoff_test=None, eval_fn=None):
    """Search game to determine best action; use alpha-beta pruning.
    This version cuts off search and uses an evaluation function."""

    player = game.to_move(state)

    def sort_f_eval(a: tuple) -> int:
        """Play the a move to the current state, and return its f_eval result.
        """
        if type(state) == str:
            return 0
        a_state = copy.deepcopy(state)
        a_state.board[a] = player
        opponent = 'X' if player == 'O' else 'O'
        return f_eval(a_state, opponent)

    # Functions used by alphabeta
    def max_value(state, alpha, beta, depth):
        if cutoff_test(state, depth):
            return eval_fn(state)
        v = -infinity
        actions = game.actions(state)
        actions.sort(key=sort_f_eval)
        for a in actions:
            v = max(v, min_value(game.result(state, a), alpha, beta, depth + 1))
            if v >= beta:
                return v
            alpha = max(alpha, v)
        return v

    def min_value(state, alpha, beta, depth):
        if cutoff_test(state, depth):
            return eval_fn(state)
        v = infinity
        actions = game.actions(state)
        actions.sort(key=sort_f_eval)
        for a in actions:
            v = min(v, max_value(game.result(state, a), alpha, beta, depth + 1))
            if v <= alpha:
                return v
            beta = min(beta, v)
        return v

    # Body of alphabeta_cutoff_search starts here:
    # The default test cuts off at depth d or at a terminal state
    cutoff_test = (cutoff_test or
                   (lambda state, depth: depth > d or game.terminal_test(state)))
    eval_fn = eval_fn or (lambda state: game.utility(state, player))
    best_score = -infinity
    beta = infinity
    best_action = None
    for a in game.actions(state):
        v = min_value(game.result(state, a), best_score, beta, 1)
        if v > best_score:
            best_score = v
            best_action = a
    return best_action


# ______________________________________________________________________________
# Players for Games


def query_player(game, state):
    """Make a move by querying standard input."""
    print("current state:")
    game.display(state)
    print("available moves: {}".format(game.actions(state)))
    print("")
    move_string = input('Your move? ')
    try:
        move = eval(move_string)
    except NameError:
        move = move_string
    return move


def random_player(game, state, prof_max=None):
    """A player that chooses a legal move at random."""
    return random.choice(game.actions(state))


def alphabeta_player(game, state, prof_max: int = 20):
    res = alphabeta_search(state, game, prof_max)
    print("noeuds developpes : " + str(expandedNodes))
    return res


def minimax_player(game, state, prof_max: int = 20):
    res = minimax_decision(state, game, prof_max)
    print("noeuds developpes : " + str(expandedNodes))
    return res


# ______________________________________________________________________________
# Some Sample Games


class Game:
    """A game is similar to a problem, but it has a utility for each
    state and a terminal test instead of a path cost and a goal
    test. To create a game, subclass this class and implement actions,
    result, utility, and terminal_test. You may override display and
    successors or you can inherit their default methods. You will also
    need to set the .initial attribute to the initial state; this can
    be done in the constructor."""

    def actions(self, state):
        """Return a list of the allowable moves at this point."""
        raise NotImplementedError

    def result(self, state, move):
        """Return the state that results from making a move from a state."""
        raise NotImplementedError

    def utility(self, state, player):
        """Return the value of this final state to player."""
        raise NotImplementedError

    def terminal_test(self, state):
        """Return True if this is a final state for the game."""
        return not self.actions(state)

    def to_move(self, state):
        """Return the player whose move it is in this state."""
        return state.to_move

    def display(self, state):
        """Print or otherwise display the state."""
        print(state)

    def __repr__(self):
        return '<{}>'.format(self.__class__.__name__)

    def play_game(self, *players, prof_max: int = None):
        """Play an n-person, move-alternating game."""
        state = self.initial
        while True:
            for player in players:
                if prof_max is not None:
                    move = player(self, state, prof_max)
                else:
                    move = player(self, state)
                state = self.result(state, move)
                if self.terminal_test(state):
                    self.display(state)
                    return self.utility(state, self.to_move(self.initial))


class Fig52Game(Game):
    """The game represented in [Figure 5.2]. Serves as a simple test case."""

    succs = dict(A=dict(a1='B', a2='C', a3='D'),
                 B=dict(b1='B1', b2='B2', b3='B3'),
                 C=dict(c1='C1', c2='C2', c3='C3'),
                 D=dict(d1='D1', d2='D2', d3='D3'))
    utils = dict(B1=3, B2=12, B3=8, C1=2, C2=4, C3=6, D1=14, D2=5, D3=2)
    initial = 'A'

    def actions(self, state):
        return list(self.succs.get(state, {}).keys())

    def result(self, state, move):
        return self.succs[state][move]

    def utility(self, state, player):
        if player == 'MAX':
            return self.utils[state]
        else:
            return -self.utils[state]

    def terminal_test(self, state):
        return state not in ('A', 'B', 'C', 'D')

    def to_move(self, state):
        return 'MIN' if state in 'BCD' else 'MAX'


class Fig52Extended(Game):
    """Similar to Fig52Game but bigger. Useful for visualisation"""

    succs = {i: dict(l=i * 3 + 1, m=i * 3 + 2, r=i * 3 + 3) for i in range(13)}
    utils = dict()

    def actions(self, state):
        return sorted(list(self.succs.get(state, {}).keys()))

    def result(self, state, move):
        return self.succs[state][move]

    def utility(self, state, player):
        if player == 'MAX':
            return self.utils[state]
        else:
            return -self.utils[state]

    def terminal_test(self, state):
        return state not in range(13)

    def to_move(self, state):
        return 'MIN' if state in {1, 2, 3} else 'MAX'


class TicTacToe(Game):
    """Play TicTacToe on an h x v board, with Max (first player) playing 'X'.
    A state has the player to move, a cached utility, a list of moves in
    the form of a list of (x, y) positions, and a board, in the form of
    a dict of {(x, y): Player} entries, where Player is 'X' or 'O'."""

    def __init__(self, h=3, v=3, k=3):
        self.h = h
        self.v = v
        self.k = k
        moves = [(x, y) for x in range(1, h + 1)
                 for y in range(1, v + 1)]
        self.initial = GameState(to_move='X', utility=0, board={}, moves=moves)

    def actions(self, state):
        """Legal moves are any square not yet taken."""
        return state.moves

    def result(self, state, move):
        if move not in state.moves:
            return state  # Illegal move has no effect
        board = state.board.copy()
        board[move] = state.to_move
        moves = list(state.moves)
        moves.remove(move)
        return GameState(to_move=('O' if state.to_move == 'X' else 'X'),
                         utility=self.compute_utility(board, move, state.to_move),
                         board=board, moves=moves)

    def utility(self, state, player):
        """Return the value to player; 10 for win, -10 for loss, 0 otherwise."""
        return state.utility if player == 'X' else -state.utility

    def terminal_test(self, state):
        """A state is terminal if it is won or there are no empty squares."""
        return state.utility != 0 or len(state.moves) == 0

    def display(self, state):
        board = state.board
        for x in range(1, self.h + 1):
            for y in range(1, self.v + 1):
                print(board.get((x, y), '.'), end=' ')
            print()

    def compute_utility(self, board, move, player):
        """If 'X' wins with this move, return 10; if 'O' wins return -10; else return 0."""
        if (self.k_in_row(board, move, player, (0, 1)) or
                self.k_in_row(board, move, player, (1, 0)) or
                self.k_in_row(board, move, player, (1, -1)) or
                self.k_in_row(board, move, player, (1, 1))):
            return +math.inf if player == 'X' else -math.inf
        else:
            return 0

    def k_in_row(self, board, move, player, delta_x_y):
        """Return true if there is a line through move on board for player."""
        (delta_x, delta_y) = delta_x_y
        x, y = move
        n = 0  # n is number of moves in row
        while board.get((x, y)) == player:
            n += 1
            x, y = x + delta_x, y + delta_y
        x, y = move
        while board.get((x, y)) == player:
            n += 1
            x, y = x - delta_x, y - delta_y
        n -= 1  # Because we counted move itself twice
        return n >= self.k


class ConnectFour(TicTacToe):
    """A TicTacToe-like game in which you can only make a move on the bottom
    row, or in a square directly above an occupied square.  Traditionally
    played on a 7x6 board and requiring 4 in a row."""

    def __init__(self, h=7, v=6, k=4):
        TicTacToe.__init__(self, h, v, k)

    def actions(self, state):
        return [(x, y) for (x, y) in state.moves
                if y == 1 or (x, y - 1) in state.board]
