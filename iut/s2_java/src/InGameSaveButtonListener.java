import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;

/**
 * Classe utilisee pour traiter les clicks sur le bouton "Save" en jeu
 *
 * @author Antoine Dujardin
 */
class InGameSaveButtonListener implements ActionListener {
    /**
     * GameManager ou se situe le bouton
     */
    private GameManager gameManager;
    /**
     * Emplacement de la sauvegarde
     */
    private File file;

    /**
     * constructeur
     *
     * @param g GameManager ou se situe le bouton
     */
    InGameSaveButtonListener(GameManager g){
        this.gameManager = g;
    }

    /**
     * En cas de click sur le bouton, si le fichier choisi est valide, sauvegarder.
     */
    @Override
    public void actionPerformed(ActionEvent e){
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);

        int returnVal = chooser.showOpenDialog(this.gameManager);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            this.file = chooser.getSelectedFile();

            if (this.checkFile()){
                this.save();
            }
            else {
                this.gameManager.showFileSaveError();
            }

        }
    }

    /**
     * Verifie si le fichier est valide.
     *
     * @return true si le fichier est valide, false sinon.
     */
    private boolean checkFile(){
        if (!this.file.isDirectory()){
            if (this.file.exists()){
                if (this.file.canWrite()){
                    return true;
                }
            }
            else {
                return true;
            }
        }

        return false;
    }

    /**
     * effectue la sauvegarde
     */
    private void save(){
        GridSaver saver = new GridSaver(this.file.getPath(), this.gameManager.display.grid);
        saver.save();
        this.gameManager.hideFileSaveError();
    }
}
