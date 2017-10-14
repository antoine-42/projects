import java.util.*;

/**
 * Classe utilisee pour representer une grille.
 *
 * @author Antoine Dujardin
 */
class Grid{
    /**
     * Liste contenant tout les groupes.
     */
	private List<Group> groupList;
    /**
     * Liste contenant tout les blocks appartenant a un groupe.
     */
	private List<Block> inGroup;

    /**
     * Tableau contenant tout les blocks.
     */
	private Block[][] arrayy;

    /**
     * Indique si des nouvelles colonnes doivent etre creees pour remplacer l'espace vide a la droite du tableau.
     */
	private boolean createNewColumns = false;

    /**
     * GridSaver utilise pour les sauvegardes automatiques.
     */
    private GridSaver saver;


    /**
     * Indique le mode. peut etre 0 ou 1.
     */
    int mode;

    /**
     * Indique le nombre de couleurs presents sur la grille. peut etre entre 2 et 9.
     */
    int nbColor;
    /**
     * Largeur de la grille.
     */
    int width;
    /**
     * Hauteur de la grille.
     */
    int height;

    /**
     * Somme des points obtenus en detruisant des groupes.
     */
	int points = 0;
    /**
     * Nombre de groupes detruits.
     */
	int destroyedGroups = 0;
    /**
     * Nombre de colonnes generees.
     */
    int generatedColumns = 0;


    /**
     * constructeur pour generer une nouvelle grille.
     *
     * @param w Largeur de la grille.
     * @param h Hauteur de la grille.
     * @param n nombre de couleurs.
     * @param m mode.
     */
	Grid(int w, int h, int n, int m){
		this.width = w;
		this.height = h;
        this.mode = m;
        this.createNewColumns = this.mode == 1;

		//needs between 2 and 9 colors
		if(n < 2){
			throw new IllegalArgumentException("min number of colors is 2");
		}
		else if (n > 9) {
			throw new IllegalArgumentException("max number of colors is 9");
		}
		this.nbColor = n;

		this.generate();

		this.initialise();
	}
    /**
     * constructeur pour obtenir une grille dont tout les champs sont pre-definit.
     *
     * @param w Largeur de la grille.
     * @param h Hauteur de la grille.
     * @param n nombre de couleurs.
     * @param m Mode.
     * @param a Tableau contenant tout les blocks.
     * @param p Points.
     * @param d Nombre de groupes detruits.
     * @param g Nombre de colonnes generees.
     */
    Grid(int w, int h, int n, int m, Block[][] a, int p, int d, int g){
        this.width = w;
        this.height = h;
        this.nbColor = n;

        this.mode = m;
        this.createNewColumns = this.mode == 1;

	    this.arrayy = a;
	    this.points = p;
	    this.destroyedGroups = d;
	    this.generatedColumns = g;

        this.initialise();
    }
    /**
     * Initialise la Grid.
     * Utilise par les deux constructeurs
     */
    private void initialise(){
        this.updateGroups();

        this.saver = new GridSaver("autosave", this);
        this.saver.save();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        };
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(task, 0, 10);
    }


    /**
     * Initialise tout les Block d'arrayy.
     */
	private void generate(){
        this.arrayy = new Block[width][height];
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {

				this.arrayy[i][j] = new Block(i, j, -1, nbColor);
			}
		}
	}

    /**
     * Verifie s'il est possible d'effectuer une action sur la grille.
     *
     * @return true si aucune action peut etre effectuee sur la grille, false sinon.
     */
	boolean checkFail(){
        return this.groupList.size() > 0;
	}

    /**
     * Met a jour la liste des groupes.
     */
	private void updateGroups(){
		this.groupList = this.getGroups();
	}
    /**
     * Renvoie une liste de groupe a jour.
     *
     * @return liste de groupe a jour.
     */
	private List<Group> getGroups(){
		List<Group> list = new ArrayList<>();
		this.inGroup = new ArrayList<>();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				//get currBlock
                Block currBlock = arrayy[i][j];

                //only continue if it's not null and not in a group already
                if(currBlock != null
                        && !this.inGroup.contains(currBlock)){

	                List<Block> currGroup = this.findGroup(currBlock);

	                //only add it if big enough
	                if(currGroup.size() > 1){
	                	list.add(new Group(currGroup));
	                }
                }
			}
		}

		return list;
	}
    /**
     * Fonction recursive qui renvoie une liste de tout les blocks adjacents et de meme couleur qui ne sont pas deja
     * dans un groupe.
     *
     * @param b block de depart
     * @return  liste de blocks adjacents et de meme couleur qui ne sont pas deja dans un groupe.
     */
	private List<Block> findGroup(Block b){
		int[] coordinates = this.getBlockArrayCoordinates(b);
		if (coordinates == null){
		    throw new NullPointerException();
        }
		int departX = coordinates[0];
		int departY = coordinates[1];

		//coordinates of adjacent block relative to current block
		int[][] adjacent = {
			{0, 1},
			{1, 0},
			{0, -1},
			{-1, 0},
		};

		List<Block> blocksGroup = new ArrayList<>();

		//for all adjacent blocks
		for (int[] currBlock : adjacent) {

			int currX = departX + currBlock[0];
			int currY = departY + currBlock[1];

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
		}

		return blocksGroup;
	}

    /**
     * Renvoie le groupe qui contient le block envoye en parametre.
     *
     * @param b block dont on souhaite connaitre le groupe.
     * @return  groupe ou se situe le block, ou null si le block n'est dans aucun groupe.
     */
    private Group getGroup(Block b){
        //check if the block is in a group
        if(!inGroup.contains(b)){
            return null;
        }

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

    /**
     * Renvoie les coordonnees du block dans arrayy ou null si le block n'est pas trouve.
     *
     * @param b block dont on souhaite connaitre les coordonnees.
     * @return  tableau qui contient les coordonnees ou null si le block n'est pas trouve. l'abscisse est en position 0 et l'ordonnee en position 1.
     */
    private int[] getBlockArrayCoordinates(Block b){
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {

                if(b == this.arrayy[i][j]){
					return new int[] {i, j};
				}
			}
		}

		return null;
	}

    /**
     * Renvoie le block situe a cette position sur l'affichage.
     *
     * @param x Abscisse.
     * @param y Ordonnee.
     * @return  Block situe a cette position.
     */
	Block getBlockFromDisplayCoordinates(int x, int y){
		//because of the animations, we need to look in the array for the block that currently has those coordinates
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				Block currBlock = this.arrayy[i][j];

				//if the position is within the block
				if(currBlock != null
                        && x >= currBlock.getX() - GameManager.margin //>= means that there isn't any gap between 2 blocks
                        && x < currBlock.getX() + GameManager.blockSide + GameManager.margin
                        && y >= currBlock.getY() - 2
                        && y < currBlock.getY() + 32){

					//return the block
					return currBlock;
				}
			}
		}
		return null;
	}
    /**
     * Renvoie le groupe situe a cette position sur l'affichage.
     *
     * @param x Abscisse.
     * @param y Ordonnee.
     * @return  Groupe situe a cette position.
     */
	Group getGroupFromDisplayCoordinates(int x, int y){
		Block currBlock = getBlockFromDisplayCoordinates(x, y);
		return getGroup(currBlock);
	}

    /**
     * Met a jour la grille.
     * Les blocks tombent, puis sont decalles vers la gauche. Si besoin, cree des nouvelles colonnes.
     */
    private void applyGravity() {
        //start on the last row
        for (int i = 0; i < this.width; i++) {
            for (int j = this.height - 1; j >= 0; j--) {

                //if we find a null
                if (this.arrayy[i][j] == null) {

                    //go up until we find a non null
                    for (int k = j; k >= 0; k--) {
                        Block newBlock = this.arrayy[i][k];
                        if (newBlock != null) {

                            //then put it at the place of the null
                            newBlock.setDestination(i * GameManager.getFullBlockSide() + GameManager.margin, j * GameManager.getFullBlockSide() + GameManager.margin);

                            this.arrayy[i][j] = newBlock;
                            this.arrayy[i][k] = null;

                            break;
                        }
                    }
                }
            }
        }
    }
    private void shiftLeft(){
        int currGeneratedColumns = 0;

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

							this.arrayy[i][j].setPosition(i * GameManager.getFullBlockSide() + GameManager.margin, j * GameManager.getFullBlockSide() + GameManager.margin);
						}
					}
				} else if (this.createNewColumns){
					//else create a new column
					for (int j = 0; j < this.height; j++) {
						this.arrayy[i][j] = new Block(this.width + 1, j, -1, nbColor);
                        this.arrayy[i][j].setDestination(i * GameManager.getFullBlockSide() + GameManager.margin, j * GameManager.getFullBlockSide() + GameManager.margin);
					}
                    currGeneratedColumns++;
				}
			}

		}
        generatedColumns += currGeneratedColumns;
		this.updateGroups();
	}

    /**
     * Detruit un block.
     *
     * @param b Block a detruire.
     */
    private void removeBlock(Block b){
        int[] coordinates = getBlockArrayCoordinates(b);
        if (coordinates == null){
            throw new NullPointerException();
        }
        arrayy[coordinates[0]][coordinates[1]] = null;
    }
    /**
     * Detruit un groupe.
     *
     * @param g Groupe a detruire.
     */
	void destroyGroup(Group g){
        for (Block currBlock : g.blockList) {
            this.removeBlock(currBlock);
        }
        this.points += g.points;

		this.destroyedGroups++;
		this.applyGravity();
		this.shiftLeft();

        this.saver.save();
	}

    /**
     * Bouge tous les blocks de la grille vers leur position.
     */
	private void tick(){
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if(this.arrayy[i][j] != null){
                    this.arrayy[i][j].tick();
                }
            }
        }
    }


    /**
     * Renvoie le block situe a la position demandee.
     *
     * @param x Abscisse
     * @param y Ordonee
     * @return  Block situe a la position demandee
     */
	Block getBlock(int x, int y){
		return this.arrayy[x][y];
	}

    /**
     * Renvoie le nombre de points par click.
     *
     * @return Nombre de points par click.
     */
    int getPointsPerClick(){
        return this.points / Math.max(this.destroyedGroups, 1);
    }

    /**
     * Renvoie une chaine de charactere representant la grille.
     *
     * @return Chaine de charactere representant la grille.
     */
	public String toString(){
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {

                Block currBlock = this.arrayy[i][j];
				if(currBlock != null){
					result.append(currBlock.color);
				}
				else{
					result.append(" ");
				}
			}
			result.append("\n");
		}
		return result.toString();
	}
}
