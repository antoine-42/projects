import random
from django.shortcuts import render

import math_web.generators
from .forms import GetSettingsForm, GetResponseForm


def index(request):
    if request.method == 'POST':
        form_name = request.POST["form_name"]
        if form_name == "get_settings":
            form = GetSettingsForm(request.POST)
            if form.is_valid():
                return show_questions(request, form)
        elif form_name == "get_response":
            form = GetResponseForm(request.POST, questions_dict=request.session["questions"])
            form.data = request.POST
            if form.is_valid():
                return show_results(request, form)
    return get_settings(request)


def show_results(request, form):
    return render(request, 'math_web/show_questions.html')


def show_questions(request, form):
    nb_questions = form.cleaned_data['nb_questions']
    p1 = form.cleaned_data['p1']
    p2 = form.cleaned_data['p2']
    p21 = form.cleaned_data['p21']
    p22 = form.cleaned_data['p22']
    p23 = form.cleaned_data['p23']

    n1, n2 = get_repartition(nb_questions, [p1, p2])
    n21, n22, n23 = get_repartition(n2, [p21, p22, p23])
    n21a, n21b = get_repartition(n22, [1/2]*2)
    n22a, n22b, n22c = get_repartition(n22, [1/3]*3)

    problems = {
        "polynomial": [],
        "power_a": [],
        "power_b": [],
        "trig_a": [],
        "trig_b": [],
        "trig_c": [],
        "log": [],
    }
    for i in range(n1):
        problems["polynomial"].append(math_web.generators.RandomPolynomialSolver())
    for i in range(n21a):
        problems["power_a"].append(math_web.generators.PowerAIntegrationSolver())
    for i in range(n21b):
        problems["power_b"].append(math_web.generators.PowerBIntegrationSolver())
    for i in range(n22a):
        problems["trig_a"].append(math_web.generators.TrigonometricAIntegrationSolver())
    for i in range(n22b):
        problems["trig_b"].append(math_web.generators.TrigonometricBIntegrationSolver())
    for i in range(n22c):
        problems["trig_c"].append(math_web.generators.TrigonometricCIntegrationSolver())
    for i in range(n23):
        problems["log"].append(math_web.generators.LogarithmicIntegrationSolver())

    questions = {key: [{"class": problem.__class__.__name__, **problem.to_dict()} for problem in problem_list]
                 for key, problem_list
                 in problems.items()}
    request.session["questions"] = questions
    context = {
        "form": GetResponseForm(questions_dict=questions),
        "text": {
            "polynomial": "Equations du second degré",
            "power_a": "Intégrations de puissance",
            "trig_a": "Integrations trigonométriques",
            "log": "Integrations logarithmiques",
        }
    }
    return render(request, 'math_web/show_questions.html', context)


def get_repartition(total, probabilities=None, even_repartition_number=None):
    """Distribute a number according to probabilities.

    If even_repartition_number is set: set probabilities to [1 / even_repartition_number] * even_repartition_number
    Distribute total according to probabilities.

    :param total: int
    :param probabilities: [int], sum(probabilities) == 1
    :param even_repartition_number: int
    :return: [int]
    """
    if even_repartition_number is not None:
        if probabilities is not None:
            raise ValueError("Can't have probabilities array and even repartition length number at the same time.")
        else:
            probabilities = [1 / even_repartition_number] * even_repartition_number
    else:
        if probabilities is None:
            raise ValueError("Need either probabilities array or even repartition number.")
        elif sum(probabilities) != 1:
            raise ValueError("The sum of probabilities has to be 1.")

    repartition = [round(total * probability) for probability in probabilities]
    repartition_total = sum(repartition)
    if repartition_total > total:
        repartition = remove_from_lowest(repartition)
    elif repartition_total < total:
        repartition = add_to_highest(repartition)
    return repartition


def add_to_highest(repartition):
    """Add 1 to the highest value in an array.

    :param repartition: [int]
    :return: [int]
    """
    highest = repartition.index(max(repartition))
    repartition[highest] += 1
    return repartition


def remove_from_lowest(repartition):
    """Remove 1 from the lowest value in an array.

    :param repartition: [int]
    :return: [int]
    """
    lowest = repartition.index(min(repartition))
    repartition[lowest] -= 1
    return repartition


def get_settings(request):
    """Display a form asking the

    :param request:
    :return:
    """
    context = {
        "form": GetSettingsForm(),
    }
    return render(request, 'math_web/get_settings.html', context)
