import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.Math;
import javax.swing.*;

public class GameDisplay extends JPanel {
    //static vars
    static Color[] colorArray = {
        Color.BLUE,
        Color.GREEN,
        Color.RED,
        Color.YELLOW,
        Color.BLACK,
        Color.MAGENTA,
        Color.ORANGE,
        Color.PINK,
        Color.CYAN
    };
    static Color groupSelectedColor = Color.BLACK;
    static Color blockSelectedColor = Color.GRAY;


    //TODO: finish that.
    int blockSide = 30;
    //margin on all sides for every block
    int margin = 2;

    int width;
    int height;
    //the grid used in this game
    Grid grid;


    //new GameDisplay with width w and height h, n colors
    public GameDisplay(int w, int h, int n){
        //grid init
        this.width = w;
        this.height = h;
        this.grid = new Grid(width, height, n);

        //events
        GameMouseMotion mouseListener = new GameMouseMotion(this, this.grid);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
    }

    @Override
    public void paintComponent(Graphics graphics){
        Graphics g = graphics.create();

        //background
        if (this.isOpaque()) {
            g.setColor(this.getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }

        //first pass (selected group background)
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Block currBlock = grid.getBlock(i, j);

                if(currBlock != null){

                    //draw group selection
                    if(currBlock.groupSelected && !currBlock.blockSelected){
                        g.setColor(groupSelectedColor);
                        g.fillRect(currBlock.posX -margin, currBlock.posY -margin, blockSide + margin*2, blockSide + margin*2);
                    }
                }
            }
        }
        //second pass (block & block background)
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Block currBlock = this.grid.getBlock(i, j);

                if(currBlock != null){

                    //draw selection first
                    if(currBlock.blockSelected){
                        g.setColor(blockSelectedColor);
                        g.fillRect(currBlock.posX -margin, currBlock.posY -margin, blockSide + margin*2, blockSide + margin*2);
                    }

                    //then the block over it
                    g.setColor(colorArray[currBlock.color]);
                    g.fillRect(currBlock.posX, currBlock.posY, blockSide, blockSide);
                }
            }
        }

        //Score and stuff
        g.setColor(Color.BLACK);
        g.drawString("Score: " + this.grid.points + "    Points per click: " + (this.grid.points / Math.max(this.grid.destroyedGroups, 1)), 0, height * (blockSide + margin *2));

        if(this.grid.checkFail()){
            g.setColor(Color.RED);
            g.drawString("Game over.", 0, height * (blockSide + margin *2) + 20);
        }
    }
}
