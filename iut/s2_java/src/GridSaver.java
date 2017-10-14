import java.io.*;

/**
 * Classe utilisee pour sauvegarder une grille.
 *
 * @author Antoine Dujardin
 */
class GridSaver {
    /**
     * Emplacement ou la grille vas etre sauvegardee.
     */
    private String fileName;
    /**
     * Grille qui doit etre sauvegardee.
     */
    private Grid grid;

    /**
     * Constructeur
     *
     * @param f Emplacement ou la grille vas etre sauvegardee.
     * @param g Grille qui doit etre sauvegardee.
     */
    GridSaver(String f, Grid g){
        this.fileName = f;
        this.grid = g;
    }

    /**
     * Sauvegarde la grille a l'emplacement indique.
     */
    void save(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.fileName));

            //If the grid includes information that can't be saved with the old format, use the new one.
            if(this.grid.mode != 0 || this.grid.destroyedGroups != 0){
                writer.write(this.grid.width + "," + this.grid.height + "," + this.grid.mode + ","
                        + this.grid.points + "," + this.grid.destroyedGroups + "," + this.grid.generatedColumns + "\n");
            }

            try {
                for (int j = 0; j < this.grid.height; j++){
                    for (int i = 0; i < this.grid.width; i++){
                        if (this.grid.getBlock(i, j) != null){
                            writer.write(GridLoader.charArray[this.grid.getBlock(i, j).color]);
                        }
                        else {
                            writer.write('_');
                        }
                    }
                    writer.write('\n');
                }
            } catch (IOException e){
                System.out.println("Error while writing in " + this.fileName);
            }

            try {
                writer.close();
            } catch (IOException e){
                System.out.println("Error while closing " + this.fileName);
            }
        } catch (IOException e){
            System.out.println("Error while creating " + this.fileName);
        }
    }
}
