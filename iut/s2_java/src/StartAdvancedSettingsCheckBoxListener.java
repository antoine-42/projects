import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * Classe utilisee pour traiter les clicks sur la JCheckBox showAdvanced
 *
 * @author Antoine Dujardin
 */
class StartAdvancedSettingsCheckBoxListener implements ActionListener  {
    /**
     * paneau ou sont situe les paramettres avances
     */
    private JPanel advancedSettings;

    /**
     * constructeur
     *
     * @param g GameManager ou se situe la JCheckBox
     */
    StartAdvancedSettingsCheckBoxListener(GameManager g){
        this.advancedSettings = g.advancedSettings;
    }

    /**
     * En cas de click sur la JCheckBox, si elle est activer montrer les paramettres avances, sinon les cacher
     */
    @Override
    public void actionPerformed(ActionEvent e){
        //get the source, convert to checkbox
        JCheckBox advancedCheckBox = (JCheckBox)e.getSource();
        //check if checked
        boolean advancedShown = advancedCheckBox.isSelected();

        this.advancedSettings.setVisible(advancedShown);
        ((JFrame)this.advancedSettings.getTopLevelAncestor()).pack();
    }
}
