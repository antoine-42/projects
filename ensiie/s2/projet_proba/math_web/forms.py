from django import forms
from math_web.generators import deserialize_solver_object


class GetSettingsForm(forms.Form):
    form_name = forms.CharField(widget=forms.HiddenInput(), initial="get_settings")
    nb_questions = forms.IntegerField(label="Nombre de questions", min_value=1, max_value=100, initial=10)
    p1 = forms.FloatField(label="\(p_1\)", min_value=0, max_value=1, initial=0.2)
    p2 = forms.FloatField(label="\(p_2\)", min_value=0, max_value=1, initial=0.8)
    p21 = forms.FloatField(label="\(p_{21}\)", min_value=0, max_value=1, initial=0.2)
    p22 = forms.FloatField(label="\(p_{22}\)", min_value=0, max_value=1, initial=0.5)
    p23 = forms.FloatField(label="\(p_{23}\)", min_value=0, max_value=1, initial=0.3)
    cheat = forms.CharField(widget=forms.HiddenInput(), initial="nope")

    p_array = ["p1", "p2", "p21", "p22", "p23"]

    @staticmethod
    def validate_p(data):
        if not isinstance(data, float) or data is None:
            return False
        elif not 0 <= data <= 1:
            return False
        return True

    def clean(self):
        cleaned_data = super().clean()
        curr_p_array = [cleaned_data.get(p) for p in self.p_array]
        for p in curr_p_array:
            if not GetSettingsForm.validate_p(p):
                return
        p1, p2, p21, p22, p23 = curr_p_array

        errors = []
        if p1 + p2 != 1:
            errors.append(forms.ValidationError("\(p_1 + p_2 = %(p1)s + %(p2)s \\neq 1.\)", code='invalid', params={'p1': p1, 'p2': p2}))

        if p21 + p22 + p23 != 1:
            errors.append(forms.ValidationError("\(p_{21} + p_{22} + p_{23} = %(p21)s + %(p22)s + %(p23)s \\neq 1.\)", code='invalid', params={'p21': p21, 'p22': p22, 'p23': p23}))

        if len(errors) > 0:
            raise forms.ValidationError(errors)

    def __init__(self, *args, **kwargs):
        super(GetSettingsForm, self).__init__(*args, **kwargs)
        for visible in self.visible_fields():
            if visible.field.widget.input_type in ["number", "text", "email"]:
                visible.field.widget.attrs['class'] = 'form-control'


class GetResponseForm(forms.Form):
    form_name = forms.CharField(widget=forms.HiddenInput(), initial="get_response")

    def __init__(self, *args, questions_dict=None, cheat=False, **kwargs):
        super(GetResponseForm, self).__init__(*args, **kwargs)
        self.questions_dict = {key: [deserialize_solver_object(problem) for problem in problem_list]
                               for key, problem_list
                               in questions_dict.items()}

        question_n = 0
        self.fields_n2startpart = {}
        self.fields_n2part = []
        if cheat:
            self.fields_n2cheat = []
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
                    if cheat:
                        self.fields_n2cheat.append(question.mathjax_solution())
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
                        curr_xn_field.required = False
                        self.fields[curr_x_id] = curr_xn_field
                    question_n += 3
                    self.fields_n2part += [key] * 3
                    if cheat:
                        self.fields_n2cheat += [question.mathjax_solution()] * 3

