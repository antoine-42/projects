.SUFFIXES: .java .class

.java.class:
	javac  $*.java

CLASSES = \
	Client.java \
	DBInternal.java \
	DB.java \
	DBReservations.java \
	JTextFieldLimit.java \
	ListenerFinish.java \
	ActionSearchReservation.java \
	ListenerReservationSelect.java \
	ListenerReturnButton.java \
	ListenerSelectRoom.java \
	Main.java \
	PanelFinalValidation.java \
	PanelResult.java \
	PanelRoomSelect.java \
	PanelSearch.java \
	PanelWindow.java \
	Reception.java \
	Reservation.java \
	Room.java \
	RoomType.java \
	Utils.java \
	ListenerCloseWindow.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

run: classes
	java -cp "../../mariadb-client.jar:." Main
