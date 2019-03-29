from django import forms


class QuestionsForm(forms.Form):
    nb_questions = forms.IntegerField(label="Nombre de questions", min_value=0, max_value=42, initial=10)
    p1 = forms.FloatField(label="\(p_1\)", min_value=0, max_value=1, initial=0.2)
    p2 = forms.FloatField(label="\(p_2\)", min_value=0, max_value=1, initial=0.8)
    p21 = forms.FloatField(label="\(p_{21}\)", min_value=0, max_value=1, initial=0.2)
    p22 = forms.FloatField(label="\(p_{22}\)", min_value=0, max_value=1, initial=0.5)
    p23 = forms.FloatField(label="\(p_{23}\)", min_value=0, max_value=1, initial=0.3)

    def clean(self):
        pass

    def __init__(self, *args, **kwargs):
        super(QuestionsForm, self).__init__(*args, **kwargs)
        for visible in self.visible_fields():
            if visible.field.widget.input_type in ["number", "text", "email"]:
                visible.field.widget.attrs['class'] = 'form-control'


class ResponseForm(forms.Form):
    def __init__(self, fields, *args, **kwargs):
        super(ResponseForm, self).__init__(*args, **kwargs)
        n_key = 0
        for key, questions in fields.items():
            for i, question in enumerate(questions):
                id = '{key}_{i}'.format(key=key, i=i)
                if key != "polynomial":
                    curr_response_field = forms.FloatField(label=question)
                    curr_response_field.group = n_key
                    curr_response_field.widget.attrs['class'] = 'form-control integral_solution'
                    self.fields[id] = curr_response_field
                else:
                    CHOICES = [('no_solution', 'Pas de solution'),
                               ('1_solution', 'Une solution'),
                               ('2_solutions', 'Deux solutions')]
                    curr_nb_answers_select = forms.ChoiceField(choices=CHOICES, widget=forms.RadioSelect)
                    curr_nb_answers_select.group = n_key
                    self.fields[id + "_nb_questions"] = curr_nb_answers_select
                    for xn in range(2):
                        curr_x_id = "{id}_x{xn}".format(id=id, xn=xn)
                        curr_xn_field = forms.FloatField(label="\(x_{xn}\)".format(xn=xn))
                        curr_xn_field.group = n_key
                        curr_xn_field.widget.attrs['class'] = 'form-control'
                        self.fields[curr_x_id] = curr_xn_field
            n_key += 1
