import java.awt.event.*;

public class ListenerReturnButton implements ActionListener{
	private Reception reception;

	ListenerReturnButton(Reception reception_){
		this.reception = reception_;
	}

	public void actionPerformed(ActionEvent e) {
		this.reception.setStep(reception.step -1);
	}
}