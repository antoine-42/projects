import game
import solver


def test_game():
    curr_game = game.Game()
    curr_game.fill()
    curr_game.display()
    curr_game.move(1, 1, game.Move.UP)
    curr_game.display()
    print(curr_game.h1())
    print(curr_game.h2())
    print(curr_game.h3s())
    print(curr_game.h3())


def solve():
    s = solver.Solver()
    s.game.display()
    # print(s.breadth_first_search())
    print(s.depth_first_search(max_depth=15))
    # print(s.a_star(game.Heuristic.H1))
    # print(s.a_star(game.Heuristic.H2))
    # print(s.a_star(game.Heuristic.H3s))
    # print(s.a_star(game.Heuristic.H3))


if __name__ == "__main__":
    # test_game()
    solve()
