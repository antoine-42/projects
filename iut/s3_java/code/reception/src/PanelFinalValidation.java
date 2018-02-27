import javax.swing.*;
import java.awt.*;

class PanelFinalValidation extends JPanel {
    private Reception window;


    PanelFinalValidation(Reception window_){
        this.window = window_;

        this.drawPanel();
    }

    private void drawPanel(){
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);

        GridBagConstraints panelConstraints = new GridBagConstraints();
        panelConstraints.ipadx = 0;
        panelConstraints.ipady = 0;
        panelConstraints.gridwidth = 1;
        panelConstraints.weightx = 1;
        panelConstraints.fill = GridBagConstraints.BOTH;
    }

    void refresh(Room selected, Client client){
        this.removeAll();


        JLabel selectedRoomLabel = Utils.createContentJLabel("La chambre " + String.valueOf(selected.number) + " a bien ete attribuée à " + client.name + " " + client.lastName + ".");
        Utils.labelTitleConstraints.gridx = 0;
        Utils.labelTitleConstraints.gridy = 0;
        Utils.labelTitleConstraints.weightx = 1;
        Utils.labelTitleConstraints.fill = GridBagConstraints.BOTH;
        this.add(selectedRoomLabel, Utils.labelTitleConstraints);

        ListenerFinish finishListener = new ListenerFinish(this.window);
        JButton finishButton = Utils.createJButton("TERMINER", finishListener);
        Utils.buttonConstraints.gridx = 0;
        Utils.buttonConstraints.gridy = 1;
        Utils.buttonConstraints.weightx = 1;
        Utils.buttonConstraints.anchor = GridBagConstraints.CENTER;
        this.add(finishButton, Utils.buttonConstraints);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 2;
        constraints.weighty = 1;
        JPanel spacer = Utils.createJPanel();
        this.add(spacer, constraints);
    }
}
