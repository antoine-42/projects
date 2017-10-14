import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Classe utilisee pour traiter les changements de la JComboBox qui stocke la difficulte
 *
 * @author Antoine Dujardin
 */
class StartDifficultyComboBoxListener implements ActionListener  {
    /**
     * JCheckBox qui indique s'il faut afficher les parametres avances
     */
    private JCheckBox showAdvanced;

    /**
     * JTextField qui stocke le nombre de couleurs
     */
    private JTextField nbColorsTextField;

    /**
     * constructeur
     *
     * @param g GameManager ou se situe la JComboBox
     */
    StartDifficultyComboBoxListener(GameManager g){
        this.showAdvanced = g.showAdvanced;
        this.nbColorsTextField = g.nbColorsTextField;
    }

    /**
     * En cas de changement de la difficulte, changer le nombre de couleurs.
     * Plus la difficulte est elevee, plus le nombre de couleur est haut.
     */
    @Override
    public void actionPerformed(ActionEvent e){
        JComboBox source = (JComboBox)e.getSource();

        if(source.getSelectedItem() == "Custom" && !showAdvanced.isSelected()){
            //use doClick to trigger an ActionEvent
            this.showAdvanced.doClick();
        }
        else if (source.getSelectedItem() == "Normal"){
            this.nbColorsTextField.setText("3");
        }
        else if (source.getSelectedItem() == "Hard"){
            this.nbColorsTextField.setText("5");
        }
        else if (source.getSelectedItem() == "Very hard"){
            this.nbColorsTextField.setText("7");
        }
    }
}
