import java.io.*;
import java.util.Arrays;

/**
 * Classe utilisee pour charger une grille depuis le disque
 *
 * @author Antoine Dujardin
 */
class GridLoader {
    /**
     * Lettres utilisees pour representer des blocks dans un fichier
     */
    static final char[] charArray  = {'R', 'V', 'B', 'J', 'P', 'C', 'O', 'M', 'N'};

    /**
     * Nom du fichier
     */
    private String fileName;
    /**
     * Blocks charges depuis le disque dur
     */
    private Block[][] blockArray;

    /**
     * Indique si le fichier utilise le nouveau format
     */
    private boolean isNewFormat;
    /**
     * Indique si le fichier est invalide
     */
    private boolean invalid = false;

    /**
     * Nombre de couleurs dans cette grille
     */
    private int nbColors = 0;
    /**
     * mode de jeu utilise dans cette grille
     */
    private int mode = 0;
    /**
     * Taille horizontale de cette grille
     */
    private int width = -1;
    /**
     * Taille verticale de cette grille
     */
    private int height = 0;

    /**
     * Points de cette grille
     */
    private int points = 0;
    /**
     * Nombre de groupes detruits dans cette grille
     */
    private int destroyedGroups = 0;
    /**
     * Nombre de colonnes generees dans cette grille
     */
    private int generatedColumns = 0;


    /**
     * Initialise un nouveau GridLoader
     *
     * @param f fichier qui vas etre charge
     */
    GridLoader(String f){
        this.fileName = f;

        this.checkFormat();

        if(!this.isNewFormat){
            this.initDimensions();
        }

        if (!invalid){
            this.load();
        }
    }

    /**
     * Verifie si le fichier a ete sauvegarde avec le nouveau format.
     * Si c'est le cas, charge les informations de taille et score.
     */
    private void checkFormat(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            try {
                String firstLine = reader.readLine();

                int[] numberArray = new int[6];
                Arrays.fill(numberArray, -1);

                int currNumber = 0;

                for (char currChar : firstLine.toCharArray()){
                    String currString = currChar + "";

                    //fill the number array
                    if (GameManager.isInteger(currString)){
                        //init the array at current position if it has not been done already
                        if (numberArray[currNumber] == -1){
                            numberArray[currNumber] = 0;
                        }
                        numberArray[currNumber] = numberArray[currNumber]*10 + Integer.parseInt(currString);
                    }
                    else {
                        currNumber++;
                    }

                    //if the pointer gets too high, abort. this does not necessarily mean that the file is invalid.
                    if (currNumber >= numberArray.length){
                        break;
                    }
                }

                //if a value has not been initialised, it means that the file is invalid
                boolean allInit = true;
                for (int currInt : numberArray) {
                    if(currInt == -1){
                        allInit = false;
                    }
                }
                if(allInit){
                    this.isNewFormat = true;

                    this.width = numberArray[0];
                    this.height = numberArray[1];
                    this.mode = numberArray[2];
                    this.points = numberArray[3];
                    this.destroyedGroups = numberArray[4];
                    this.generatedColumns = numberArray[5];
                }


            } catch (IOException e){
                System.out.println("Error while reading " + fileName + ".");
            }

            try {
                reader.close();
            } catch (IOException e){
                System.out.println("Error while closing " + fileName + ".");
            }

        } catch (FileNotFoundException e){
            System.out.println("Can't open " + fileName + ".");
            this.invalid = true;
        }
    }

    /**
     * Cherche et met a jour les dimensions en lisant le fichier.
     */
    private void initDimensions(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            try {
                while (reader.ready()){
                    String currLine = reader.readLine();
                    this.height++;
                    int currWidth = 0;

                    for (char c : currLine.toCharArray()) {

                        int currInt = charToInt(c);
                        if (currInt != -1){
                            currWidth++;
                        }
                    }

                    if (this.width == -1){
                        this.width = currWidth;
                    }
                    if (this.width != currWidth){
                        System.out.println("Error: " + fileName + " doesn't uses the correct format.");
                    }
                }
            } catch (IOException e){
                System.out.println("Error while reading " + fileName + ".");
            }

            try {
                reader.close();
            } catch (IOException e){
                System.out.println("Error while closing " + fileName + ".");
            }

        } catch (FileNotFoundException e){
            System.out.println("Can't open " + fileName + ".");
            this.invalid = true;
        }

        //the grid has to be big enough to have a group on it
        if (this.height < 2 && this.width < 2){
            this.invalid = true;
        }
    }

    /**
     * Charge le fichier a partir des dimensions chargees precedemment.
     */
    private void load(){
        blockArray = new Block[width][height];

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            try {
                //if using new format, throw away the first line
                if(this.isNewFormat){
                    reader.readLine();
                }

                int posX = 0;
                int posY = 0;

                while (reader.ready()){

                    if(posY >= this.height){
                        this.invalid = true;
                        return;
                    }

                    String currLine = reader.readLine();
                    for (char c : currLine.toCharArray()) {

                        if(posX >= this.width){
                            this.invalid = true;
                            return;
                        }

                        int currInt = charToInt(c);
                        if (currInt != -1){
                            if(currInt == -2){
                                blockArray[posX][posY] = null;
                            }
                            else {
                                blockArray[posX][posY] = new Block(posX, posY, currInt);
                            }
                            posX++;
                        }
                        else {
                            System.out.println("Error: " + fileName + " doesn't uses the correct format.");
                        }
                    }

                    posX = 0;
                    posY++;
                }
            } catch (IOException e){
                System.out.println("Error while reading " + fileName);
            }

            try {
                reader.close();
            } catch (IOException e){
                System.out.println("Error while closing " + fileName);
            }

        } catch (FileNotFoundException e){
            System.out.println("Can't open " + fileName);
            this.invalid = true;
        }
    }

    /**
     * Renvoie le chiffre assigne au charactere passe en parametre.
     *
     * @param c charactere dont on souhaite obtenir le chiffre.
     * @return  chiffre assigne au charactere passe en parametre
     */
    private int charToInt(char c){
        for (int i = 0; i < charArray.length; i++){
            if (c == charArray[i]){
                if(i > this.nbColors){
                    this.nbColors = i +1;
                }
                return i;
            } else if(c == '_'){
                return -2;
            }
        }
        return -1;
    }

    /**
     * Renvoie la grille chargee
     *
     * @return la grille chargee si elle n'est pas invalide, null sinon.
     */
    Grid getGrid(){
        if(this.invalid){
            return null;
        }
        if (this.nbColors < 2){
            this.nbColors = 2;
        }
        return new Grid(this.width, this.height, this.nbColors, this.mode, this.blockArray,
                this.points,this.destroyedGroups, this.generatedColumns);
    }
}