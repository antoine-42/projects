import java.util.List;
import java.lang.Math;

/**
 * Un <code>Group</code> represente un groupe de <code>Block</code> de meme couleur et adjacents.
 *
 * @author Antoine Dujardin
 */
class Group{

	/**
	 * Liste des blocks contenus dans ce groupe.
	 */
	List<Block> blockList;
	/**
	 * Points que vas rapporter ce Group quand il sera detruit.
	 */
	int points;


	/**
	 * Construit un groupe avec les blocks contenus dans la liste b.
     *
     * @param b liste de blocks.
	 */
	Group(List<Block> b){
		this.blockList = b;
		this.points = (int)Math.pow(this.blockList.size() - 2, 2);
	}

    /**
     * selectionne ou deselectionne le groupe.
     *
     * @param b si vrai, on selectionne le groupe, sinon on deselectionne.
     */
	void selectGroup(boolean b){
		for (Block currBlock : this.blockList) {
			currBlock.groupSelected = b;
		}
	}
}
