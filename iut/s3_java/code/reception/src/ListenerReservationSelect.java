import java.awt.event.*;

public class ListenerReservationSelect implements ActionListener{
    private Reception reception;
    private Reservation reservation;

    ListenerReservationSelect(Reception reception_, Reservation reservation_){
        this.reception = reception_;
        this.reservation = reservation_;
    }

    public void actionPerformed(ActionEvent e) {
        this.reception.selectReservation(this.reservation);
    }
}
