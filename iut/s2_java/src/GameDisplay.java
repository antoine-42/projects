import javax.swing.JPanel;
import java.awt.*;
import java.util.TimerTask;

/**
 * Classe utilisee pour afficher une grille sur la fenetre
 *
 * @author Antoine Dujardin
 */
class GameDisplay extends JPanel {
    /**
     * Couleurs utilisees pour representer un block
     */
    private static Color[] colorArray = {
            new Color(201, 51, 32), //red
            new Color(89, 244, 31), //green
            new Color(10, 92, 255), //blue
            new Color(255, 229, 86), //yellow
            new Color(255, 137, 249), //pink
            new Color(52, 219, 219), //cyan
            new Color(255, 106, 0), //orange
            new Color(151, 29, 248), //magenta
            Color.BLACK
    };
    /**
     * Couleur utilisee pour indiquer le block actif
     */
    private static Color blockSelectedColor = Color.BLACK;

    /**
     * police utilisee par defaut
     */
    private static Font defaultFont = new Font(null, Font.PLAIN, 15);
    /**
     * police utilisee pour afficher le message de fin du jeu
     */
    private static Font gameOverFont = new Font(null, Font.PLAIN, 53);


    /**
     * grille qui vas etre affichee
     */
    Grid grid;


    /**
     * Initialise a partir d'une grille
     *
     * @param g grille utilisee
     */
    GameDisplay(Grid g){
        this.grid = g;

        Dimension preferredSize = new Dimension(this.grid.width * GameManager.getFullBlockSide(), this.grid.height * GameManager.getFullBlockSide() + 40);
        this.setPreferredSize(preferredSize);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        };
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(task, 0, 10);
    }

    /**
     * met a jour le GameDisplay
     *
     * @param graphics Graphics utilise
     */
    @Override
    public void paintComponent(Graphics graphics){

        Graphics g = graphics.create();

        //background
        if (this.isOpaque()) {
            g.setColor(this.getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }

        //first pass (selected group background)
        for (int i = 0; i < this.grid.width; i++) {
            for (int j = 0; j < this.grid.height; j++) {
                Block currBlock = this.grid.getBlock(i, j);

                if(currBlock != null){
                    //draw group selection
                    if(currBlock.groupSelected && !currBlock.blockSelected){
                        g.setColor(colorArray[currBlock.color]);
                        this.drawSelection(g, currBlock.getX(), currBlock.getY());
                    }
                }
            }
        }
        //second pass (block & block background)
        for (int i = 0; i < this.grid.width; i++) {
            for (int j = 0; j < this.grid.height; j++) {
                Block currBlock = this.grid.getBlock(i, j);

                if(currBlock != null){

                    //draw selection first
                    if(currBlock.blockSelected){
                        g.setColor(blockSelectedColor);
                        this.drawSelection(g, currBlock.getX(), currBlock.getY());

                        g.setColor(colorArray[currBlock.color]);
                        g.fillRect(currBlock.getX(), currBlock.getY(), GameManager.blockSide, GameManager.blockSide);
                    }
                    else if(!currBlock.groupSelected){
                        //then the block over it
                        g.setColor(colorArray[currBlock.color]);
                        g.fillRect(currBlock.getX(), currBlock.getY(), GameManager.blockSide, GameManager.blockSide);
                    }
                }
            }
        }

        //Score and stuff
        g.setColor(Color.BLACK);
        g.setFont(defaultFont);

        String newColumns = "";
        if (this.grid.generatedColumns > 0){
            newColumns = "    New columns: " + this.grid.generatedColumns;
        }
        g.drawString("Score: " + this.grid.points +
                        "    Points per click: " + this.grid.getPointsPerClick() +
                        newColumns,
                0, this.grid.height * GameManager.getFullBlockSide() + 20);

        if(!this.grid.checkFail()){
            g.setColor(Color.RED);
            g.setFont(gameOverFont);
            g.drawString("Game over", 40, 100);
        }
    }
    /**
     * dessine la selection
     *
     * @param x position horizontale de la selection
     * @param y position verticale de la selection
     * @param g Graphics utilise
     */
    private void drawSelection(Graphics g, int x, int y){
        g.fillRect(x -GameManager.margin*2, y -GameManager.margin*2, GameManager.getExtendedBlockSide(), GameManager.getExtendedBlockSide());
    }
}
