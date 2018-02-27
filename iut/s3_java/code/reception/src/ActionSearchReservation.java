import javax.swing.*;
import java.awt.event.*;

public class ActionSearchReservation extends AbstractAction{
	private Reception reception;

	ActionSearchReservation(Reception reception_){
		this.reception = reception_;
	}

	public void actionPerformed(ActionEvent e) {
	    System.out.println("enter");
		if (this.reception.step == 0){
            this.reception.searchReservation();
        }
	}
}