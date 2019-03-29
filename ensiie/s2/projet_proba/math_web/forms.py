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


class ResponseForm:
    def __init__(self, fields, *args, **kwargs):
        super(ResponseForm, self).__init__(*args, **kwargs)
        for key, questions in fields.items:
            for i, question in enumerate(questions):
                id = '{key}_{i}'.format(key=key, i=i)
                if key != "polynomial":
                    self.fields[id] = forms.FloatField(label="question")
                else:
                    CHOICES = [('no_solution', 'Pas de solution'),
                               ('1_solution', 'Une solution'),
                               ('2_solutions', 'Deux solutions')]
                    self.fields[id + "_nb_questions"] = forms.ChoiceField(choices=CHOICES, widget=forms.RadioSelect)
                    for xn in range(2):
                        curr_x_id = "{id}_x{xn}".format(id=id, xn=xn)
                        self.fields[curr_x_id] = forms.FloatField(label="\(x_{xn}\)".format(xn=xn))
