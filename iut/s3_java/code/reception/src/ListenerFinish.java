import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListenerFinish implements ActionListener {
    private Reception reception;

    ListenerFinish(Reception reception_){
        this.reception = reception_;
    }

    public void actionPerformed(ActionEvent e) {
        this.reception.reset();
    }
}
