import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Classe utilisee pour traiter les changements de la JComboBox qui stocke la taille de la grille
 *
 * @author Antoine Dujardin
 */
class StartSizeComboBoxListener implements ActionListener {
    /**
     * JCheckBox qui indique s'il faut afficher les parametres avances
     */
    private JCheckBox showAdvanced;

    /**
     * JTextField qui stocke la largeur de la grille
     */
    private JTextField widthTextField;
    /**
     * JTextField qui stocke la hauteur de la grille
     */
    private JTextField heightTextField;

    /**
     * constructeur
     *
     * @param g GameManager ou se situe la JComboBox
     */
    StartSizeComboBoxListener(GameManager g){
        this.showAdvanced = g.showAdvanced;

        this.widthTextField = g.widthTextField;
        this.heightTextField = g.heightTextField;
    }

    /**
     * En cas de changement de la taille, changer les dimensions.
     */
    public void actionPerformed(ActionEvent e){
        JComboBox source = (JComboBox)e.getSource();

        if(source.getSelectedItem() == "Custom" && !showAdvanced.isSelected()){
            //use doClick to trigger an ActionEvent
            this.showAdvanced.doClick();
        }
        else if (source.getSelectedItem() == "Small"){
            this.widthTextField.setText("10");
            this.heightTextField.setText("10");
        }
        else if (source.getSelectedItem() == "Normal"){
            this.widthTextField.setText("15");
            this.heightTextField.setText("10");
        }
        else if (source.getSelectedItem() == "Big"){
            this.widthTextField.setText("20");
            this.heightTextField.setText("15");
        }
        else if (source.getSelectedItem() == "Very big"){
            this.widthTextField.setText("30");
            this.heightTextField.setText("20");
        }
    }
}
