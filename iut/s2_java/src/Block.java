import java.util.Random;

/**
 * Un <code>Block</code> represente un block sur la grille de jeu.
 *
 * @author Antoine Dujardin
 */
class Block{
    /**
     * couleur du block
     */
	int color;

    /**
     * position horizontale sur la fenetre en pixel du block
     */
    private int posX;
    /**
     * position verticale sur la fenetre en pixel du block
     */
    private int posY;
    /**
     * destination horizontale sur la fenetre en pixel du block
     */
    private int destX;
    /**
     * destination verticale sur la fenetre en pixel du block
     */
    private int destY;

    /**
     * definit si ce block fait partie d'un groupe selectionne
     */
	boolean groupSelected = false;
    /**
     * definit si ce block est directement selectionne
     */
	boolean blockSelected = false;


    /**
     * constructeur du Block dans le cas ou la couleur a deja ete selectionee
     *
     * @param x position horizontale sur arrayy
     * @param y position verticale sur arrayy
     * @param c couleur du block
     */
	Block(int x, int y, int c){
        this.color = c;

		this.posX = x * GameManager.getFullBlockSide() + GameManager.margin;
		this.posY = y * GameManager.getFullBlockSide() + GameManager.margin;

		this.destX = this.posX;
		this.destY = this.posY;
	}
    /**
     * constructeur du Block dans le cas ou la couleur sera selectionnee aleatoirement si c n'est pas un code de
     * couleur valide
     *
     * @param x position horizontale sur arrayy
     * @param y position verticale sur arrayy
     * @param c couleur du block
     * @param n code couleur maximum
     */
    Block(int x, int y, int c, int n){
        this(x, y, c);

        if(c < 0 || c >= n){
            Random rand = new Random();
            this.color = rand.nextInt(n);
        }
    }

    /**
     * renvoie la position verticale du block
     *
     * @return position verticale du block
     */
    int getX(){
        return this.posX;
    }
    /**
     * renvoie la position horizontale du block
     *
     * @return position horizontale du block
     */
    int getY(){
        return this.posY;
    }

    /**
     * force un changement de position immediat
     *
     * @param x nouvelle position horizontale du block
     * @param y nouvelle position verticale du block
     */
    void setPosition(int x, int y){
        this.setDestination(x, y);
        this.destX = x;
        this.destY = y;
    }
    /**
     * change la destination du block
     *
     * @param x nouvelle destination horizontale du block
     * @param y nouvelle destination verticale du block
     */
    void setDestination(int x, int y){
        this.destX = x;
        this.destY = y;
    }

    /**
     * indique si le block est arrive a sa destination
     *
     * @return true si le block est arrive a sa destination, false sinon.
     */
    private boolean isArrived(){
        return this.destX == this.posX && this.destY == this.posY;
    }
    /**
     * bouge le block d'un maximum de 3 pixels vers sa position
     */
    void tick(){
        if(!this.isArrived()){
            this.posX = translate(this.posX, this.destX);
            this.posY = translate(this.posY, this.destY);
        }
    }
    /**
     * renvoie la nouvelle position dans une dimension
     *
     * @param currPos position actuelle
     * @param dest    destination
     * @return        nouvelle position
     */
    private int translate(int currPos, int dest){
        int distance = dest - currPos;
        int mod = 1;
        if (distance < 0){
            mod = -1;
        }
        return currPos + mod*Math.min(mod*distance, 3);
    }
}
