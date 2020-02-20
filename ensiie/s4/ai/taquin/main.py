import game
import solver

if __name__ == "__main__":
    solver = solver.Solver()
    # finish = solver.breadth_first_search()
    # finish = solver.depth_first_search(max_depth=5)
    finish = solver.a_star(game.Heuristic.H3)
    print(finish)

    # curr_game = game.Game()
    # curr_game.fill()
    # curr_game.display()
    # curr_game.move(1, 1, game.Move.UP)
    # curr_game.display()
    # print(curr_game.h1())
    # print(curr_game.h2())
    # print(curr_game.h3s())
    # print(curr_game.h3())
