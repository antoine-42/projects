import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;


class PanelSearch extends JPanel{
    private Reception window;

    private JTextField lastNameTextField;
    private JTextField nameTextField;

    private JTextField referencePt1TextField;
    private JTextField referencePt2TextField;
    private JTextField referencePt3TextField;
    private JTextField[] reservationSubPanels;

    private int reservationSubPanel = 0;


    PanelSearch(Reception window_){
        this.window = window_;

        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        Action actionSearchKeyInput = new ActionSearchReservation(this.window);
        this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, "enter");
        this.getActionMap().put("enter", actionSearchKeyInput);

        this.drawPanel();
    }

    void refresh(){
        lastNameTextField.requestFocusInWindow();
    }


    private void drawPanel(){
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.ipadx = 15;
        labelConstraints.ipady = 15;
        labelConstraints.weightx = 0;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.insets = Utils.MARGIN_NO_DOWN;

        GridBagConstraints smallLabelConstraints = new GridBagConstraints();
        smallLabelConstraints.ipadx = 0;
        smallLabelConstraints.ipady = 0;
        smallLabelConstraints.weightx = 0;
        smallLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        smallLabelConstraints.insets = Utils.MARGIN_NONE;

        GridBagConstraints textFieldConstraints = new GridBagConstraints();
        textFieldConstraints.ipadx = 10;
        textFieldConstraints.ipady = 10;
        textFieldConstraints.weightx = 1;
        textFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        textFieldConstraints.insets = Utils.MARGIN_NONE;

        GridBagConstraints panelConstraints = new GridBagConstraints();
        panelConstraints.ipadx = 0;
        panelConstraints.ipady = 0;
        panelConstraints.weightx = 1;
        panelConstraints.fill = GridBagConstraints.HORIZONTAL;


        JPanel fullNamePanel = Utils.createJPanel();
        panelConstraints.gridx = 0;
        panelConstraints.gridy = 0;
        panelConstraints.weightx = 1;
        panelConstraints.insets = Utils.MARGIN_RIGHT;
        this.add(fullNamePanel, panelConstraints);


        JLabel lastNameLabel = Utils.createContentJLabel("Nom du client");
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.gridwidth = 1;
        fullNamePanel.add(lastNameLabel, labelConstraints);

        this.lastNameTextField = Utils.createJTextField("Faure");
        textFieldConstraints.gridx = 0;
        textFieldConstraints.gridy = 1;
        fullNamePanel.add(lastNameTextField, textFieldConstraints);

        JLabel nameLabel = Utils.createContentJLabel("Prénom du client");
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 2;
        labelConstraints.gridwidth = 1;
        fullNamePanel.add(nameLabel, labelConstraints);

        this.nameTextField = Utils.createJTextField("Florian");
        textFieldConstraints.gridx = 0;
        textFieldConstraints.gridy = 3;
        fullNamePanel.add(nameTextField, textFieldConstraints);


        JPanel panelReference = Utils.createJPanel();
        panelConstraints.gridx = 1;
        panelConstraints.gridy = 0;
        panelConstraints.weightx = 3;
        panelConstraints.insets = Utils.MARGIN_LEFT;
        this.add(panelReference, panelConstraints);

        JLabel labelReferenceReservation = Utils.createContentJLabel("Référence de la réservation");
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        panelReference.add(labelReferenceReservation, labelConstraints);

        JPanel panelReferenceText = new JPanel(new GridBagLayout());
        panelReferenceText.setBackground(Color.WHITE);
        panelReferenceText.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        panelConstraints.gridx = 0;
        panelConstraints.gridy = 1;
        panelConstraints.weightx = 3;
        panelConstraints.insets = Utils.MARGIN_NONE;
        panelReference.add(panelReferenceText, panelConstraints);

        this.referencePt1TextField = Utils.createReferenceInputJTextField();
        this.referencePt1TextField.setDocument(new JTextFieldLimit(4, 0, this));
        textFieldConstraints.gridx = 0;
        textFieldConstraints.gridy = 0;
        panelReferenceText.add(referencePt1TextField, textFieldConstraints);

        JLabel dash1Label = Utils.createContentJLabel("-");
        smallLabelConstraints.gridx = 1;
        textFieldConstraints.gridy = 0;
        panelReferenceText.add(dash1Label, smallLabelConstraints);

        this.referencePt2TextField = Utils.createReferenceInputJTextField();
        this.referencePt2TextField.setDocument(new JTextFieldLimit(4, 1, this));
        textFieldConstraints.gridx = 2;
        textFieldConstraints.gridy = 0;
        panelReferenceText.add(referencePt2TextField, textFieldConstraints);

        JLabel dash2Label = Utils.createContentJLabel("-");
        smallLabelConstraints.gridx = 3;
        textFieldConstraints.gridy = 0;
        panelReferenceText.add(dash2Label, smallLabelConstraints);

        this.referencePt3TextField = Utils.createReferenceInputJTextField();
        this.referencePt3TextField.setDocument(new JTextFieldLimit(4));
        textFieldConstraints.gridx = 4;
        textFieldConstraints.gridy = 0;
        panelReferenceText.add(referencePt3TextField, textFieldConstraints);

        this.reservationSubPanels = new JTextField[]{
                this.referencePt1TextField,
                this.referencePt2TextField,
                this.referencePt3TextField
        };


        ActionSearchReservation reservationListener = new ActionSearchReservation(this.window);
        JButton searchButton = Utils.createJButton("Chercher", reservationListener);
        Utils.buttonConstraints.gridx = 0;
        Utils.buttonConstraints.gridy = 1;
        Utils.buttonConstraints.gridwidth = 2;
        Utils.buttonConstraints.anchor = GridBagConstraints.CENTER;
        this.add(searchButton, Utils.buttonConstraints);


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 3;
        constraints.weighty = 1;
        JPanel spacer = Utils.createJPanel();
        this.add(spacer, constraints);
    }

    void setReservationSubPanel(int n, String leftover){
        this.setReservationSubPanel(n);
        this.reservationSubPanels[this.reservationSubPanel].setText(this.reservationSubPanels[this.reservationSubPanel].getText() + leftover);
    }
    void setReservationSubPanel(int n){
        this.reservationSubPanel = n;
        this.reservationSubPanels[this.reservationSubPanel].requestFocus();
    }

    Boolean reservationNullOrEmpty(){
        for (JTextField field: this.reservationSubPanels) {
            if (field != null && !Utils.isNullOrEmpty(field.getText())){
                return false;
            }
        }
        return true;
    }
    String getReservation(){
        return this.referencePt1TextField.getText() + "-" + this.referencePt2TextField.getText() + "-" + this.referencePt3TextField.getText();
    }
    String getLastName(){
        return this.lastNameTextField.getText();
    }
    String getFirstName(){
        return this.nameTextField.getText();
    }

    void reset(){
        this.nameTextField.setText("");
        this.lastNameTextField.setText("");
        this.referencePt1TextField.setText("");
        this.referencePt2TextField.setText("");
        this.referencePt3TextField.setText("");
    }
}
