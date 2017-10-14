import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Classe utilisee pour traiter les clicks sur le bouton "Resume last game" dans le menu de depart.
 * Herite de StartLoadButtonListener.
 *
 * @author Antoine Dujardin
 */
class StartLoadAutosaveButtonListener extends StartLoadButtonListener{

    /**
     * constructeur
     *
     * @param g GameManager ou se situe le bouton
     */
    StartLoadAutosaveButtonListener(GameManager g){
        super(g);
    }

    /**
     * En cas de click sur le bouton, charger la sauvegarde automatique et commencer la partie si la sauvegarde est valide
     */
    @Override
    public void actionPerformed(ActionEvent e){
        this.file = new File("autosave");

        if(this.checkFile()){
            this.load();
            gameManager.startGame();
        }
        else {
            gameManager.showFileLoadingError();
            gameManager.checkIfAutosaveExists();
        }
    }
}
