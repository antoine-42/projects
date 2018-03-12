.SUFFIXES: .java .class

.java.class:
	javac  $*.java

CLASSES = \
	Main.java \
	Graphe.java \
	Sommet.java \
	Arete.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

run: classes
	java Main
