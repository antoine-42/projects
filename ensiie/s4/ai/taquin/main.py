import game
import solver


def test_game():
    curr_game = game.Game()
    curr_game.fill()
    curr_game.display()
    curr_game.move(game.Direction.UP)
    curr_game.display()
    print(curr_game.h1())
    print(curr_game.h2())
    print(curr_game.h3s())
    print(curr_game.h3())


print_format_string = "{} Fini: {}, noeuds traités: {}, coups à jouer depuis l’état initial: {}"


def solve():
    s = solver.Solver()
    s.game.display()
    print(print_format_string.format('h3', *s.a_star(game.Heuristic.H3)))
    print(print_format_string.format('h3s', *s.a_star(game.Heuristic.H3s)))
    print(print_format_string.format('h2', *s.a_star(game.Heuristic.H2)))
    print(print_format_string.format('h1', *s.a_star(game.Heuristic.H1)))
    print(print_format_string.format('iterative deepening', *s.iterative_deepening_search()))
    print("depth first Fini: {0}, noeuds traités: {1}, coups à jouer depuis l’état initial: {3}".format(
        *s.depth_first_search(max_depth=15)))
    print(print_format_string.format('breadth first', *s.breadth_first_search()))


if __name__ == "__main__":
    # test_game()
    solve()
