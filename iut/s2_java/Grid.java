import java.util.*;

public class Grid{
    //private vars
	//dimensions of the grid
	private int width;
	private int height;
	private int nbColor;//number of colors of the grid


    int blockSide = 30;
    int margin = 2;//margin on all sides for every block

	//list of all the groups of the same color
	List<Group> groupList;
	//list of all the blocks in a group
	List<Block> inGroup;

	//array that contains all the blocks
	Block[][] arrayy; //lmao

    //public vars
	public int points = 0;
	public int destroyedGroups = 0;

	//new Grid
	public Grid(int w, int h, int n){
		this.width = w;
		this.height = h;

		//needs between 2 and 9 colors
		if(n < 2){
			throw new IllegalArgumentException("min number of colors is 2");
		}
		else if (n > 9) {
			throw new IllegalArgumentException("max number of colors is 9");
		}
		this.nbColor = n;

		this.arrayy = new Block[width][height];

		this.generate();
		this.updateGroups();
	}

	//generates the array. each position has the same chance of being in one of the different selected colors.
	private void generate(){

		//position loops
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {

				//get a new pseudorandom number that represents the color for each position.
				this.arrayy[i][j] = new Block(i, j, -1);
			}
		}
	}

	//checks if the grid is in a failed state, returns true if failed.
	public boolean checkFail(){
		if(this.groupList.size() > 0){
			return false;
		}
		return true;
	}

	//updates groupList
	public void updateGroups(){
		this.groupList = this.getGroups();
	}
	//returns the list of group in the grid
	private List<Group> getGroups(){
		List<Group> list = new ArrayList<Group>();
		this.inGroup = new ArrayList<Block>();

		//position loops
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				//get currBlock
                Block currBlock = arrayy[i][j];

                //only continue if it's not in a group already
                if(!this.inGroup.contains(currBlock)){

	                List<Block> currGroup = this.findGroup(currBlock);

	                //only add it if big enough
	                if(currGroup.size() > 1){
	                	list.add(new Group(currGroup, this));
	                }
                }
			}
		}

		return list;
	}
	//Recursive function that returns all blocks of the color b that are ajdacent to b and its adjacent blocks
	private List<Block> findGroup(Block b){
		int[] coordinates = this.getBlockArrayCoordinates(b);
		int departX = coordinates[0];
		int departY = coordinates[1];

		//coordinates of adjacent block relative to current block
		int[][] adjacent = {
			{0, 1},
			{1, 0},
			{0, -1},
			{-1, 0},
		};

		List<Block> blocksGroup = new ArrayList<Block>();

		//for all adjacent blocks
		for (int[] currBlock : adjacent) {

			int currX = departX + currBlock[0];
			int currY = departY + currBlock[1];

			try{
				//if adjacent block exists and is same color and not in group
				if(currX >= 0 && currX < this.width
					&& currY >= 0 && currY < this.height
					&& arrayy[currX][currY] != null
					&& !this.inGroup.contains(arrayy[currX][currY])
					&& this.arrayy[currX][currY].color == b.color){

					//add block to current group
					blocksGroup.add(arrayy[currX][currY]);
					this.inGroup.add(arrayy[currX][currY]);

					//call the function on this block and add the blocks found this way to the group
					blocksGroup.addAll(this.findGroup(arrayy[currX][currY]));
				}
			} catch(Exception e){
				//TODO: fix nullPointerException on inGroup.contains(arrayy[currX][currY]
			}
		}

		return blocksGroup;
	}

	//returns the group in which b is located or null if no group
	public Group getGroup(Block b){
        //check in every group
        for (Group currGroup : this.groupList) {
			for (Block currBlock : currGroup.blockList) {

                if(currBlock == b){
					return currGroup;
				}
			}
		}
		return null;
	}

	//get the coordinates of the block in arrayy
	public int[] getBlockArrayCoordinates(Block b){
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {

                if(b == this.arrayy[i][j]){
					return new int[] {i, j};
				}
			}
		}

		return null;
	}
	//get the block located in this position on the display or null
	public Block getBlockFromDisplayCoordinates(int x, int y){
		//because of the animations, we need to look in the array for the block that currently has those coordinates
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				Block currBlock = this.arrayy[i][j];

				//if the position is within the block
				if(currBlock != null && x > currBlock.posX - 2 && x < currBlock.posX + 32
					&& y > currBlock.posY - 2 && y < currBlock.posY + 32){

					//return the block
					return currBlock;
				}
			}
		}
		return null;
	}
	//get the group located in this position on the display or null
	public Group getGroupFromDisplayCoordinates(int x, int y){
		Block currBlock = getBlockFromDisplayCoordinates(x, y);
		return getGroup(currBlock);
	}

	//replace this block in arrayy with null
	public void removeBlock(Block b){
		int[] coordinates = getBlockArrayCoordinates(b);
		arrayy[coordinates[0]][coordinates[1]] = null;
	}

	//applies gravity
	public void applyGravity(){

		//gravity
		//start on the last row
		for (int i = 0; i < this.width; i++) {
			for (int j = this.height - 1; j >= 0; j--) {

				//if we find a null
				if(this.arrayy[i][j] == null){

					//go up until we find a non null
					for (int k = j; k >= 0; k--) {
                        Block newBlock = this.arrayy[i][k];
						if(newBlock != null){

							//then put it at the place of the null
							newBlock.posX = i * (this.blockSide + this.margin * 2) + this.margin;
							newBlock.posY = j * (this.blockSide + this.margin * 2) + this.margin;

							this.arrayy[i][j] = newBlock;
							this.arrayy[i][k] = null;

							break;
						}
					}
				}
			}
		}

		//shift to the left
		//for every column
		for (int i = 0; i < this.width; i++) {

			//if there is one completely null
			if(this.arrayy[i][this.height -1] == null){

				//find the next non null column
				int nonNullN = -1;
				for (int k = i + 1; k < this.width; k++) {
					if(this.arrayy[k][this.height -1] != null){
						nonNullN = k - i;
						break;
					}
				}

				if(nonNullN != -1){
					//if there is a non null column to the right, shift it
					for (int j = 0; j < this.height; j++) {
						if(this.arrayy[i + nonNullN][j] != null){

							this.arrayy[i][j] = this.arrayy[i +nonNullN][j];
                            this.arrayy[i + nonNullN][j] = null;

							this.arrayy[i][j].posX = i * (this.blockSide + this.margin *2) + this.margin;
							this.arrayy[i][j].posY = j * (this.blockSide + this.margin *2) + this.margin;
						}
					}
				} else{
					//else create a new column
					for (int j = 0; j < this.height; j++) {
						this.arrayy[i][j] = new Block(i, j, -1);
					}
				}
			}

		}
		this.updateGroups();
	}

	public void destroyGroup(Group g){
		this.points += g.destroy();
		this.destroyedGroups++;
		this.applyGravity();
	}


	//getters/setters
	public Block getBlock(int x, int y){
		return this.arrayy[x][y];
	}

	//returns a string that represents arrayy
	public String toString(){
		String result = "";
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.width; j++) {
                Block currBlock = this.arrayy[i][j];
				if(currBlock != null){
					result += currBlock.color;
				}
				else{
					result += " ";
				}
			}
			result += "\n";
		}
		return result;
	}
}
