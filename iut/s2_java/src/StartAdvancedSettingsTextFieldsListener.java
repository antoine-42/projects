import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/**
 * Classe utilisee pour traiter les changements du JTextField nbColorsTextField
 *
 * @author Antoine Dujardin
 */
class StartAdvancedSettingsTextFieldsListener implements ActionListener {
    /**
     * JComboBox utilisee pour afficher la difficulte
     */
    private JComboBox difficultyComboBox;

    /**
     * constructeur
     *
     * @param g GameManager ou se situe le JTextField
     */
    StartAdvancedSettingsTextFieldsListener(GameManager g){
        this.difficultyComboBox = g.difficultyComboBox;
    }

    /**
     * Si on appuie sur la touche entree dans le JTextField, passer la difficulte a "custom"
     */
    @Override
    public void actionPerformed(ActionEvent e){
        this.difficultyComboBox.setSelectedIndex(3);
    }
}
