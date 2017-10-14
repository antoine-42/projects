.SUFFIXES: .java .class

.java.class:
	javac  $*.java

CLASSES = \
	Block.java \
	GameDisplay.java \
	GameManager.java \
	GameMouseListener.java \
	Grid.java \
	GridLoader.java \
	GridSaver.java \
	Group.java \
	InGameReplayButtonListener.java \
	InGameRestartButtonListener.java \
	InGameSaveButtonListener.java \
	SameGame.java \
	StartAdvancedSettingsCheckBoxListener.java \
	StartAdvancedSettingsTextFieldsListener.java \
	StartDifficultyComboBoxListener.java \
	StartDoneButtonListener.java \
	StartLoadAutosaveButtonListener.java \
	StartLoadButtonListener.java \
	StartSizeComboBoxListener.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

test: classes
	java SameGame
