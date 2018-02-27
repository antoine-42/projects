.SUFFIXES: .java .class

.java.class:
	javac  $*.java

CLASSES = \
	Main.java \
	DBHistorique.java \
	DB.java \
	Fenetre.java \
	MenuActionListener.java \
	MenuDeroulant.java \
	Statistiques.java


default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

run: classes
	java -cp "../mariadb-client.jar:." Main
