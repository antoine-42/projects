import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

class PanelResult extends JScrollPane {
    private static final String labelTitles[] = {
            "Nom",
            "Prénom",
            "Référence",
            "Type de chambre",
            "Début du séjour",
            "Duree du séjour"
    };


    private Reception window;

    private JPanel content;

    private JLabel resultsLabel;
    private JLabel additionalResultsLabel;

    private JPanel panelTableauResults;
    private JPanel panelTableauAdditionalResults;


    PanelResult(Reception window_){
        this.window = window_;

        this.drawPanel();
    }


    private void drawPanel(){
        this.content = new JPanel(new GridBagLayout());
        this.content.setOpaque(false);

        this.setViewportView(this.content);
        this.setOpaque(false);
        this.getViewport().setOpaque(false);
        this.setBorder(Utils.EMPTY_BORDER);


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = Utils.MARGIN_NONE;
        constraints.anchor = GridBagConstraints.NORTH;


        this.resultsLabel = Utils.createTitleJLabel("Résultats");
        Utils.labelTitleConstraints.gridx = 0;
        Utils.labelTitleConstraints.gridy = 0;
        Utils.labelTitleConstraints.gridwidth = 1;
        this.content.add(resultsLabel, Utils.labelTitleConstraints);

        this.panelTableauResults = Utils.createJPanel();
        constraints.gridy = 1;
        this.content.add(this.panelTableauResults, constraints);

        this.additionalResultsLabel = Utils.createTitleJLabel("Autres réservations");
        Utils.labelTitleConstraints.gridx = 0;
        Utils.labelTitleConstraints.gridy = 3;
        Utils.labelTitleConstraints.gridwidth = 1;
        this.content.add(additionalResultsLabel, Utils.labelTitleConstraints);

        this.panelTableauAdditionalResults = Utils.createJPanel();
        constraints.gridy = 4;
        this.content.add(this.panelTableauAdditionalResults, constraints);

        JPanel spacer = Utils.createJPanel();
        constraints.gridy = 5;
        constraints.weighty = 1;
        this.content.add(spacer, constraints);
    }

    void refresh(Reservation[] reservations, Reservation[] additionalReservations){
        this.reset();

        if (reservations.length == 0) {
            this.resultsLabel.setText("Aucune réservation active");
        }
        else {
            if (reservations.length > 1){
                this.resultsLabel.setText("Réservations actives");
            }
            else {
                this.resultsLabel.setText("Réservation active");
            }
            createReservationsTable(this.panelTableauResults, reservations, true);
        }

        if (additionalReservations.length > 0){
            if (additionalReservations.length > 1){
                this.additionalResultsLabel.setText("Autres réservations");
                createReservationsTable(this.panelTableauAdditionalResults, additionalReservations, false);
            }
            else {
                this.additionalResultsLabel.setText("Autre réservation");
            }
        }
    }
    void reset(){
        this.panelTableauResults.removeAll();
        this.panelTableauAdditionalResults.removeAll();
    }

    private void createReservationsTable(JPanel panel, Reservation[] reservations, Boolean createButtons){
        //premiere ligne
        for (int i = 0; i < PanelResult.labelTitles.length; i++) {
            JLabel labelTitle = Utils.createContentJLabel(PanelResult.labelTitles[i]);
            //ajouter marge a la premiere colonne
            if (i == 0) {
                Border border = labelTitle.getBorder();
                Border margin = new EmptyBorder(0, 10, 0, 0);
                labelTitle.setBorder(new CompoundBorder(border, margin));
            }
            Utils.cellConstraints.gridx = i;
            Utils.cellConstraints.gridy = 0;
            panel.add(labelTitle, Utils.cellConstraints);
        }

        //lignes suivantes
        for (int i = 0; i < reservations.length; i++) {
            String[] content = reservations[i].getInfo();

            for (int j = 0; j < content.length; j++) {
                JLabel contentLabel = Utils.createContentJLabel(content[j]);

                //ajouter marge a la premiere colonne
                if (j == 0) {
                    Border border = contentLabel.getBorder();
                    Border margin = new EmptyBorder(0, 10, 0, 0);
                    contentLabel.setBorder(new CompoundBorder(border, margin));
                }
                //coloriser 1 ligne sur 2
                if (i % 2 == 0) {
                    contentLabel.setOpaque(true);
                    contentLabel.setBackground(Utils.THIRD_COLOR);
                }

                Utils.cellConstraints.gridx = j;
                Utils.cellConstraints.gridy = i +1;

                panel.add(contentLabel, Utils.cellConstraints);
            }
            //boutons
            if (createButtons) {
                ListenerReservationSelect selectListener = new ListenerReservationSelect(this.window, reservations[i]);
                JButton selectButton = Utils.createJButton("⇒", selectListener);
                Utils.cellConstraints.gridx = content.length;
                Utils.cellConstraints.gridy = i +1;
                panel.add(selectButton, Utils.cellConstraints);
            }
        }
    }
}
