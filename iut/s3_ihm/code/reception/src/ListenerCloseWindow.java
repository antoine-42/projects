import java.awt.event.*;

public class ListenerCloseWindow extends WindowAdapter {
    Reception reception;

    ListenerCloseWindow(Reception reception_){
        this.reception = reception_;
    }

    public void windowClosing(WindowEvent e)
    {
        this.reception.closeConnections();
        System.exit(0);
    }
}
