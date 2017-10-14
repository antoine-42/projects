import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe utilisee pour traiter les clicks sur le bouton "Restart" en jeu
 *
 * @author Antoine Dujardin
 */
class InGameRestartButtonListener implements ActionListener {
    /**
     * GameManager ou se situe le bouton
     */
    private GameManager gameManager;

    /**
     * constructeur
     *
     * @param g GameManager ou se situe le bouton
     */
    InGameRestartButtonListener(GameManager g){
        this.gameManager = g;
    }

    /**
     * En cas de click sur le bouton, redemarrer le jeu avec les memes parametres
     */
    @Override
    public void actionPerformed(ActionEvent e){
        this.gameManager.restartGame();
    }
}
