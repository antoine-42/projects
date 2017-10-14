import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

/**
 * Classe utilisee pour gerer l'affichage du jeu ainsi que le passage entre le menu principal et le jeu.
 *
 * @author Antoine Dujardin
 */
class GameManager extends JComponent{
    /**
     * Taille du cote d'un block en pixel.
     */
    static final int blockSide = 30;
    /**
     * Taille de la marge sur tout les cotes des blocks en pixel.
     */
    static final int margin = 2;


    /**
     * Indique le mode. peut etre 0 ou 1.
     */
    private int mode = 0;
    /**
     * Largeur de la grille.
     */
    private int width = 15;
    /**
     * Hauteur de la grille.
     */
    private int height = 10;
    /**
     * Indique le nombre de couleurs presents sur la grille. peut etre entre 2 et 9.
     */
    private int nbColors = 3;

    /**
     * Sauvegarde selectionne.
     */
    private File selectedSave;


    /**
     * Fenetre.
     */
    private JFrame window;

    /**
     * Label utilise pour afficher une erreur pendant l'enregistrement d'une sauvegarde.
     */
    private JLabel saveErrorLabel;

    /**
     * Panneau contenant le menu de depart.
     */
    private JPanel start;
    /**
     * Panneau contenant le menu de jeu.
     */
    private JPanel gameMenu;

    /**
     * Label utilise pour afficher le nom de la sauvegarde selectionnee.
     */
    private JLabel loadLabel;
    /**
     * Label utilise pour afficher une erreur pendant la lecture d'une sauvegarde.
     */
    private JLabel loadErrorLabel;
    /**
     * Bouton utilise pour charger une sauvegarde automatique.
     */
    private JButton autosaveLoadButton;

    /**
     * Combobox utilisee pour indiquer le mode.
     */
    private JComboBox<String> modeComboBox;

    /**
     * Label utilise pour indiquer si un des champ de texte ne contiennent pas un nombre valide.
     */
    private JLabel numberErrorLabel;


    /**
     * Combobox utilisee pour indiquer la difficulte.
     */
    JComboBox<String> difficultyComboBox;

    /**
     * Checkbox utilisee pour montrer ou cacher les parametres avances.
     */
    JCheckBox showAdvanced;

    /**
     * Panneau contenant les parametres avances.
     */
    JPanel advancedSettings;
    /**
     * Champ de texte contenant la largeur de la grille.
     */
    JTextField widthTextField;
    /**
     * Champ de texte contenant la hauteur de la grille.
     */
    JTextField heightTextField;
    /**
     * Champ de texte contenant le nombre de couleur.
     */
    JTextField nbColorsTextField;


    /**
     * GameDisplay utilise pour afficher la grille.
     */
    GameDisplay display;


    /**
     * Constructeur.
     */
    GameManager(){
        this.window = new JFrame();
        this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.window.setSize(700, 700);
        this.window.setTitle("SameGame");
        this.window.setLayout(new FlowLayout());
        this.window.setResizable(false);

        generateStartMenu();
        this.window.add(this.start);
        this.window.pack();

        this.window.setVisible(true);
    }

    /**
     * Genere le menu de depart.
     */
    private void generateStartMenu(){
        //panel init
        this.start = new JPanel();
        this.start.setLayout(new BoxLayout(this.start, BoxLayout.Y_AXIS));


        //load button
        JPanel loadPanel = new JPanel();
        loadPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.X_AXIS));
        this.start.add(loadPanel);

        this.loadLabel = new JLabel();
        this.loadLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.loadLabel.setVisible(false);
        loadPanel.add(this.loadLabel);

        JButton loadButton = new JButton("Load save");
        loadPanel.add(loadButton);

        this.autosaveLoadButton = new JButton("Resume last game");
        this.checkIfAutosaveExists();
        loadPanel.add(this.autosaveLoadButton);

        this.loadErrorLabel = new JLabel();
        this.loadErrorLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.loadErrorLabel.setForeground(Color.RED);
        this.loadErrorLabel.setVisible(false);
        loadPanel.add(this.loadErrorLabel);


        //simple settings
        JPanel simpleSettings = new JPanel();
        simpleSettings.setBorder(new EmptyBorder(10, 10, 10, 10));
        simpleSettings.setLayout(new GridLayout(3, 2));
        this.start.add(simpleSettings);

        //mode
        JLabel modeLabel = new JLabel("Mode", JLabel.RIGHT);
        modeLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        simpleSettings.add(modeLabel);

        String[] modes = {"Default", "Create new columns"};
        this.modeComboBox = new JComboBox<>(modes);
        simpleSettings.add(this.modeComboBox);

        //difficulty
        JLabel difficultyLabel = new JLabel("Difficulty", JLabel.RIGHT);
        difficultyLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        simpleSettings.add(difficultyLabel);

        String[] difficulties = {"Normal", "Hard", "Very hard", "Custom"};
        this.difficultyComboBox = new JComboBox<>(difficulties);
        simpleSettings.add(this.difficultyComboBox);

        //size
        JLabel sizeLabel = new JLabel("Grid size", JLabel.RIGHT);
        sizeLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        simpleSettings.add(sizeLabel);

        String[] sizes = {"Small", "Normal", "Big", "Very big", "Custom"};
        JComboBox<String> sizeComboBox = new JComboBox<>(sizes);
        sizeComboBox.setSelectedIndex(1);
        simpleSettings.add(sizeComboBox);


        //show advanced checkbox
        this.showAdvanced = new JCheckBox("Show advanced settings");
        this.showAdvanced.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.start.add(this.showAdvanced);


        //advanced settings
        this.advancedSettings = new JPanel(new GridLayout(3, 2));
        this.advancedSettings.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.advancedSettings.setVisible(false);
        this.start.add(this.advancedSettings);

        //width
        JLabel widthLabel = new JLabel("Width", JLabel.RIGHT);
        widthLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.advancedSettings.add(widthLabel);

        this.widthTextField = new JTextField("15");
        this.advancedSettings.add(this.widthTextField);

        //height
        JLabel heightLabel = new JLabel("Height", JLabel.RIGHT);
        heightLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.advancedSettings.add(heightLabel);

        this.heightTextField = new JTextField("10");
        this.advancedSettings.add(this.heightTextField);

        //color number
        JLabel nbColorsLabel = new JLabel("Color number", JLabel.RIGHT);
        nbColorsLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.advancedSettings.add(nbColorsLabel);

        this.nbColorsTextField = new JTextField("3");
        this.advancedSettings.add(this.nbColorsTextField);


        //error display
        this.numberErrorLabel = new JLabel();
        this.numberErrorLabel.setForeground(Color.RED);
        this.start.add(this.numberErrorLabel);


        //Done button
        JButton doneButton = new JButton("Play!");
        this.start.add(doneButton);


        //event listeners
        loadButton.addActionListener(new StartLoadButtonListener(this));
        autosaveLoadButton.addActionListener(new StartLoadAutosaveButtonListener(this));

        this.difficultyComboBox.addActionListener(new StartDifficultyComboBoxListener(this));
        sizeComboBox.addActionListener(new StartSizeComboBoxListener(this));

        this.showAdvanced.addActionListener(new StartAdvancedSettingsCheckBoxListener(this));

        this.widthTextField.addActionListener(new StartAdvancedSettingsTextFieldsListener(this));
        this.heightTextField.addActionListener(new StartAdvancedSettingsTextFieldsListener(this));
        this.nbColorsTextField.addActionListener(new StartAdvancedSettingsTextFieldsListener(this));

        doneButton.addActionListener(new StartDoneButtonListener(this));
    }

    /**
     * Genere le menu de jeu.
     */
    private void generateGameMenu(){
        this.gameMenu = new JPanel();
        gameMenu.setLayout(new BoxLayout(gameMenu, BoxLayout.Y_AXIS));


        this.saveErrorLabel = new JLabel();
        this.saveErrorLabel.setForeground(Color.RED);
        this.saveErrorLabel.setVisible(false);
        this.gameMenu.add(this.saveErrorLabel);


        JPanel gameMenuButtons = new JPanel();
        gameMenuButtons.setLayout(new GridLayout(3,1));

        //add intermediary layer so that gameMenuButtons doesn't take all the available space.
        //doing is this way also adds a bit of padding which is nice
        JPanel gameMenuButtonsWrapper = new JPanel();
        gameMenuButtonsWrapper.add(gameMenuButtons);
        this.gameMenu.add(gameMenuButtonsWrapper);


        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new InGameSaveButtonListener(this));
        gameMenuButtons.add(saveButton);


        JButton restartButton = new JButton("Restart game");
        restartButton.addActionListener(new InGameRestartButtonListener(this));
        gameMenuButtons.add(restartButton);


        JButton replayButton = new JButton("Change settings");
        replayButton.addActionListener(new InGameReplayButtonListener(this));
        gameMenuButtons.add(replayButton);
    }

    /**
     * Verifie si tout les champs de texte contiennent des nombres valides superieur ou egal a 0.
     *
     * @return true si tout les champs de texte contiennent des nombres valides superieur ou egal a 0, false sinon.
     */
    private boolean checkIfAllFieldsAreValid(){
        JTextField[] fields = {this.widthTextField, this.heightTextField, this.nbColorsTextField};
        for (JTextField currField : fields) {
            if(!this.checkIfFieldIsValid(currField)){
                return false;
            }
        }
        return true;
    }
    /**
     * Verifie si le champ de texte contient un nombre valide superieur ou egal a 0.
     *
     * @param t le champ de texte
     * @return  true si le champ de texte contient un nombre valide superieur ou egal a 0, false sinon.
     */
    private boolean checkIfFieldIsValid(JTextField t){
        String text = t.getText();
        if(!isInteger(text)){
            setNumberError(text);
            return false;
        }

        return true;
    }
    /**
     * Verifie si la chaine de characteres represente un nombre valide superieur ou egal a 0.
     *
     * @param str la chaine de characteres
     * @return    true si la chaine de characteres represente un nombre valide superieur ou egal a 0, false sinon.
     */
    static boolean isInteger(String str){
        if(str == null){
            return false;
        }

        int strLength = str.length();
        if (strLength == 0){
            return false;
        }

        for (int i = 0; i < strLength; i++){
            char currChar = str.charAt(i);
            if(currChar < '0' || currChar > '9'){
                return false;
            }
        }

        return true;
    }

    /**
     * Renvoie une nouvelle grille a partir des parametres entres dans le menu de depart.
     *
     * @return une nouvelle grille a partir des parametres entres dans le menu de depart.
     */
    private Grid startNewGame(){
        //parse the strings into ints
        this.width = Integer.parseInt(this.widthTextField.getText());
        this.height = Integer.parseInt(this.heightTextField.getText());
        this.nbColors = Integer.parseInt(this.nbColorsTextField.getText());

        this.mode = this.modeComboBox.getSelectedIndex();

        //Ideally I would change the event listeners to make sure that invalid inputs can't be set but it's not that important
        if(this.nbColors < 2){
            this.nbColors = 2;
        }
        if(this.nbColors > 9){
            this.nbColors = 9;
        }
        if(this.width < 1){
            this.width = 1;
        }
        if(this.height < 1){
            this.height = 1;
        }

        return new Grid(this.width, this.height, this.nbColors, this.mode);
    }
    /**
     * Renvoie une grille chargee a partir du fichier choisi, ou null si ce fichier n'est pas valide.
     *
     * @return une grille chargee a partir du fichier choisi, ou null si ce fichier n'est pas valide.
     */
    private Grid startSavedGame(){
        GridLoader loader = new GridLoader(this.selectedSave.getPath());
        Grid grid = loader.getGrid();

        if (grid != null){
            this.width = grid.width;
            this.height = grid.height;
            this.nbColors = grid.nbColor;
            this.mode = grid.mode;

            this.widthTextField.setText(this.width + "");
            this.heightTextField.setText(this.height + "");
            this.nbColorsTextField.setText(this.nbColors + "");
            this.modeComboBox.setSelectedIndex(this.mode);

            this.selectedSave = null;
        }
        return grid;
    }
    /**
     * Lance le jeu.
     * Si les paramattres sont invalides (sauvegarde invalide, champs de texte invalides...), une erreur sera affichee
     */
    void startGame(){
        //hide the numberErrorLabel, it will be shown if needed
        this.hideNumberError();

        //all fields must be valid
        if(this.checkIfAllFieldsAreValid()){
            Grid grid;

            if(this.selectedSave != null){
                grid = this.startSavedGame();
            }
            else {
                grid = this.startNewGame();
            }

            if (grid == null){
                this.showFileLoadingError();
                return;
            }

            //remove the start menu
            this.window.remove(this.start);

            this.window.setLayout(new BorderLayout());

            this.display = new GameDisplay(grid);
            GameMouseListener mouseListener = new GameMouseListener(this.display);
            this.display.addMouseListener(mouseListener);
            this.display.addMouseMotionListener(mouseListener);
            this.window.add(this.display, BorderLayout.CENTER);

            this.generateGameMenu();
            this.window.add(this.gameMenu, BorderLayout.EAST);
            this.window.pack();

            //refresh the window
            this.window.invalidate();
            this.window.revalidate();
            this.window.repaint();
        }
    }
    /**
     * Reviens au menu de depart.
     */
    void startSettings(){
        this.window.remove(this.display);
        this.window.remove(this.gameMenu);

        this.window.setLayout(new FlowLayout());

        this.window.add(this.start);
        this.window.pack();

        this.window.invalidate();
        this.window.revalidate();
        this.window.repaint();
    }
    /**
     * Redemarre la partie avec les meme parametres.
     */
    void restartGame(){
        this.window.remove(this.display);
        this.window.remove(this.gameMenu);

        this.window.add(this.start);

        this.startGame();
    }


    /**
     * Affiche une erreur de champ de text invalide.
     *
     * @param invalidInput La chaine de charactere qui ne represente pas un nombre.
     */
    private void setNumberError(String invalidInput){
        this.numberErrorLabel.setText("Error: " + invalidInput + " is not a positive number!");
        this.numberErrorLabel.setVisible(true);
        this.window.pack();
    }
    /**
     * Masque l'erreur de champ de text invalide.
     */
    private void hideNumberError(){
        this.numberErrorLabel.setVisible(false);
        this.window.pack();
    }

    /**
     * Verifie si une sauvegarde automatique existe. Si une sauvegarde existe, montrer le bouton de chargement de la
     * sauvegarde automatique.
     */
    void checkIfAutosaveExists(){
        File autosave = new File("autosave");

        this.autosaveLoadButton.setVisible(autosave.exists());
    }

    /**
     * Definit la sauvegarde a charger.
     *
     * @param f sauvegarde a charger.
     */
    void setSelectedSave(File f){
        this.selectedSave = f;

        this.loadLabel.setText("Loaded save: " + f.getName());
        this.loadLabel.setVisible(true);
        this.window.pack();
    }
    /**
     * Affiche une erreur de chargement de sauvegarde.
     */
    void showFileLoadingError(){
        this.loadErrorLabel.setText("Error: the save file is invalid!");
        this.loadErrorLabel.setVisible(true);
        this.window.pack();
    }
    /**
     * Masque l'erreur de chargement de sauvegarde.
     */
    void hideFileLoadingError(){
        this.loadErrorLabel.setVisible(false);
        this.window.pack();
    }

    /**
     * Affiche une erreur d'enregistrement de sauvegarde.
     */
    void showFileSaveError(){
        this.saveErrorLabel.setText("Error: this location is invalid!");
        this.saveErrorLabel.setVisible(true);
        this.window.pack();
    }
    /**
     * Masque l'erreur d'enregistrement de sauvegarde.
     */
    void hideFileSaveError(){
        this.saveErrorLabel.setVisible(false);
        this.window.pack();
    }


    /**
     * Renvoie la taille complete du cote d'un block, en incluant sa marge.
     *
     * @return La taille complete du cote d'un block, en incluant sa marge.
     */
    static int getFullBlockSide(){
        return blockSide + margin*2;
    }
    /**
     * Renvoie la taille "etendue" du cote d'un block, en incluant sa marge et la marge de son voisin.
     *
     * @return La taille "etendue" du cote d'un block, en incluant sa marge et la marge de son voisin.
     */
    static int getExtendedBlockSide(){
        return blockSide + margin*4;
    }
}