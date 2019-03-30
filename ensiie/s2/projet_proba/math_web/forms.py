from django import forms
from math_web.generators import deserialize_solver_object


class GetSettingsForm(forms.Form):
    form_name = forms.CharField(widget=forms.HiddenInput(), initial="get_settings")
    nb_questions = forms.IntegerField(label="Nombre de questions", min_value=0, max_value=42, initial=10)
    p1 = forms.FloatField(label="\(p_1\)", min_value=0, max_value=1, initial=0.2)
    p2 = forms.FloatField(label="\(p_2\)", min_value=0, max_value=1, initial=0.8)
    p21 = forms.FloatField(label="\(p_{21}\)", min_value=0, max_value=1, initial=0.2)
    p22 = forms.FloatField(label="\(p_{22}\)", min_value=0, max_value=1, initial=0.5)
    p23 = forms.FloatField(label="\(p_{23}\)", min_value=0, max_value=1, initial=0.3)

    def clean(self):
        pass

    def __init__(self, *args, **kwargs):
        super(GetSettingsForm, self).__init__(*args, **kwargs)
        for visible in self.visible_fields():
            if visible.field.widget.input_type in ["number", "text", "email"]:
                visible.field.widget.attrs['class'] = 'form-control'


class GetResponseForm(forms.Form):
    form_name = forms.CharField(widget=forms.HiddenInput(), initial="get_response")

    def __init__(self, *args, questions_dict=None, **kwargs):
        super(GetResponseForm, self).__init__(*args, **kwargs)
        self.questions_dict = {key: [deserialize_solver_object(problem) for problem in problem_list]
                               for key, problem_list
                               in questions_dict.items()}

        question_n = 0
        self.fields_n2startpart = {}
        self.fields_n2part = []
        for key, questions in self.questions_dict.items():
            if "_b" not in key and "_c" not in key:
                self.fields_n2startpart[question_n] = key
            for i, question in enumerate(questions):
                id = '{key}_{i}'.format(key=key, i=i)
                if key != "polynomial":
                    curr_response_field = forms.FloatField(label=question.get_mathjax_function())
                    curr_response_field.widget.attrs['class'] = 'form-control integral_solution'
                    self.fields[id] = curr_response_field
                    question_n += 1
                    self.fields_n2part.append(key)
                else:
                    CHOICES = [(0, 'Pas de solution'),
                               (1, 'Une solution'),
                               (2, 'Deux solutions')]
                    curr_nb_answers_select = forms.ChoiceField(
                        label=question.get_mathjax_function(), choices=CHOICES,
                        widget=forms.RadioSelect(attrs={'class': 'custom-control-input'})
                    )
                    self.fields["{id}_nb_roots".format(id=id)] = curr_nb_answers_select
                    for xn in range(2):
                        curr_x_id = "{id}_x{xn}".format(id=id, xn=xn)
                        curr_xn_field = forms.FloatField(label="\(x_{xn}\)".format(xn=xn))
                        curr_xn_field.widget.attrs['class'] = 'form-control polynomial_solution'
                        self.fields[curr_x_id] = curr_xn_field
                    question_n += 3
                    self.fields_n2part += [key] * 3

