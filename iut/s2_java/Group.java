import java.util.*;
import java.lang.Math;

public class Group{
	//static vars
	static Grid grid;


	//public vars
	public List<Block> blockList;
	public int nBlocks;
	public int points;

	//true if whole group is selected
	public boolean selected = false;


	//creates new Group with blocks b on grid g
	public Group(List<Block> b, Grid g){
		grid = g;

		this.blockList = b;
		this.nBlocks = b.size();
		this.points = (int)Math.pow(this.nBlocks - 2, 2);
	}

	//returns true if block is in this.blockList
	public boolean contains(Block block){
		return this.blockList.contains(block);
	}

	//selects or unselects all block in this
	public void selectGroup(boolean b){
		this.selected = b;
		for (Block currBlock : this.blockList) {
			currBlock.groupSelected = b;
		}
	}

	//turns all the blocks in group to none, returns the number of points
	public int destroy(){
		for (Block currBlock : this.blockList) {
			grid.removeBlock(currBlock);
		}
		return this.points;
	}
}
