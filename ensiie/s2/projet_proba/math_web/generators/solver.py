from math import log10, floor


class Solver:
    def __init__(self):
        self.solution = None

    @staticmethod
    def round_number(number, precision):
        return round(number, -int(floor(log10(abs(number)))) + (precision - 1))

    @staticmethod
    def check_result(solution, response, precision):
        return Solver.round_number(solution, precision) == Solver.round_number(response, precision)

    def check_results(self, response, precision):
        if isinstance(self.solution, list):
            if len(response) == len(self.solution):
                for i in range(len(self.solution)):
                    if not Solver.check_result(self.solution[i], response[i], precision):
                        return False
            else:
                return False
        else:
            if not Solver.check_result(self.solution, response, precision):
                return False
        return True
