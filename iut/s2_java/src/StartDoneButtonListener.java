import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe utilisee pour traiter les clicks sur le bouton "Done" dans le menu de depart
 *
 * @author Antoine Dujardin
 */
class StartDoneButtonListener implements ActionListener  {
    /**
     * GameManager ou se situe le bouton
     */
    private GameManager game;

    /**
     * constructeur
     *
     * @param g GameManager ou se situe le bouton
     */
    StartDoneButtonListener(GameManager g){
        game = g;
    }

    /**
     * En cas de click sur le bouton, commencer la partie
     */
    @Override
    public void actionPerformed(ActionEvent e){
        game.startGame();
    }
}
