import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;

/**
 * Classe utilisee pour traiter les clicks sur le bouton "Load save" dans le menu de depart.
 *
 * @author Antoine Dujardin
 */
class StartLoadButtonListener implements ActionListener  {
    /**
     * GameManager ou se situe le bouton
     */
    GameManager gameManager;

    /**
     * Nom du fichier
     */
    File file;

    StartLoadButtonListener(GameManager g){
        this.gameManager = g;
    }

    /**
     * En cas de click sur le bouton, charger la sauvegarde selectionne et commencer la partie si la sauvegarde est valide
     */
    @Override
    public void actionPerformed(ActionEvent e){
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);

        int returnVal = chooser.showOpenDialog(this.gameManager);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            this.file = chooser.getSelectedFile();

            if(this.checkFile()){
                this.load();
                gameManager.startGame();
            }
            else {
                gameManager.showFileLoadingError();
            }
        }
    }

    /**
     * verifie si la sauvegarde est valide
     *
     * @return true si la sauvegarde est valide, false sinon
     */
    boolean checkFile(){
        return !this.file.isDirectory() && this.file.exists() && this.file.canRead();
    }

    /**
     * charge la sauvegarde
     */
    void load(){
        this.gameManager.setSelectedSave(this.file);
        this.gameManager.hideFileLoadingError();
    }
}
