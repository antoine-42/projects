J'ai fait un site web avec Django. Il est accessible sur: https://antoine42.pythonanywhere.com/
C'est un hébergeur gratuit, du coup le site ne sera peut etre plus disponible a partir du 1er juillet. Je suis en train de mettre en place un serveur, j'y mettrais mes projets dessus, l'adresse sera dans le repo github: https://github.com/antoine-42/projects/tree/master/ensiie/s2/projet_proba


Vous pouvez aussi le faire marcher vous meme, les fichiers nécéssaires sont dans le répertoire dm_proba_dujardin_antoine/sources/

J'ai mis le venv que j'ai utilisé dans les sources, donc sur linux c'est tres simple. Pour lancer le serveur, il faut juste faire:
$ ./venv/bin/python manage.py runserver

Pour les autres plateformes, c'est assez facile. Il faut installer django avec pip:
$ pip install django
et lancer le serveur:
$ python manage.py runserver
Si ca marche pas, vérifiez que vous utilisez l'installation de python sur laquelle vous avez installé django.

Si vous avez des problemes, n'hésitez pas a me contacter via mail sur antoinedujardin42@gmail.com , je serais plus réactif que via mon adresse ensiie qui se fait spammer
La documentation de django est tres complete si vous avez des problemes pendant que ne suis pas disponible : https://docs.djangoproject.com/fr/2.1/
