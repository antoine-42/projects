import java.util.*;

//Blocks that make up the grid in GameDisplay
public class Block{
	//static vars
    static int blockSide = 30;
    static int margin = 2; //margin on all sides for every block


	//public vars
	public int color;

	//position on the display
	public int posX;
	public int posY;

	//states
	public boolean groupSelected = false;
	public boolean blockSelected = false;


	//creates Block at arrayy position x, y with color c
	public Block(int x, int y, int c){
		if(c > -1){
			this.color = c;
		}
		else {
			Random rand = new Random();
			this.color = rand.nextInt(3);
		}
		//this leaves a border between all the blocks
		this.posX = x * (blockSide + margin) + margin;
		this.posY = y * (blockSide + margin) + margin;
	}

}
