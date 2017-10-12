import javax.swing.*;
import java.awt.*;

public class SameGame extends JComponent{
	public static void main(String[] args){
		//TODO LOAD IMPORTANT w/ JFileChooser
		//TODO: set this shit with a GUI or console command args
		int width = 15;
		int height = 10;
		int nbColors = 3;

        JFrame fenetre = new JFrame();
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fenetre.setSize(700, 700);

		//TODO: autosave
        //use fenetre.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		GameDisplay display = new GameDisplay(width, height, nbColors);
		fenetre.add(display, BorderLayout.CENTER);

        fenetre.setVisible(true);

        //TODO add end screen, keep final grid visible
	}
}
/*
TODO:
	improve classes
		Grid, GameDisplay
		OK classes: Block, Group, GameMouseMotion
	modes
		unlimited mode: grid always full, get most points/click or points/minute
		change gravity
*/
