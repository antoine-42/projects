import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe utilisee pour traiter les clicks sur le bouton "Change Settings" en jeu
 *
 * @author Antoine Dujardin
 */
class InGameReplayButtonListener implements ActionListener {
    /**
     * GameManager ou se situe le bouton
     */
    private GameManager gameManager;

    /**
     * constructeur
     *
     * @param g GameManager ou se situe le bouton
     */
    InGameReplayButtonListener(GameManager g){
        this.gameManager = g;
    }

    /**
     * En cas de click sur le bouton, revenir au menu de depart
     */
    @Override
    public void actionPerformed(ActionEvent e){
        this.gameManager.startSettings();
    }
}
