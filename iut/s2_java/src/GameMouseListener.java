import java.awt.event.*;

/**
 * Classe utilisee pour attraper et interpreter les clicks et mouvements du curseur sur un GameDisplay
 *
 * @author Antoine Dujardin
 */
class GameMouseListener implements MouseMotionListener, MouseListener{
	private GameDisplay gameDisplay;
	private Grid grid;

    /**
     * Group actif.
     */
	private Group activeGroup;
    /**
     * Block actif.
     */
	private Block activeBlock;

    /**
     * Constructeur.
     *
     * @param d Gamedisplay sur lequel GameMouseMotion s'applique.
     */
	GameMouseListener(GameDisplay d){
		this.gameDisplay = d;
		this.grid = d.grid;
	}

    /**
     * Herite depuis MouseListener.
     * Met a jour le block et groupe actif quand le curseur entre la fenetre.
     */
    @Override
	public void mouseEntered(MouseEvent e){
		this.updateActive(e);
	}
    /**
     * Herite depuis MouseListener.
     * Met a jour le block et groupe actif quand le curseur sort de la fenetre.
     */
    @Override
	public void mouseExited(MouseEvent e){
		this.updateActive(e);
	}
    /**
     * Herite depuis MouseListener.
     * Non utilise.
     */
    @Override
	public void mousePressed(MouseEvent e){}
    /**
     * Herite depuis MouseListener.
     * Non utilise.
     */
    @Override
	public void mouseReleased(MouseEvent e){}
    /**
     * Herite depuis MouseListener.
     * Met a jour le block et groupe actif quand un click est detecte, et detruit le groupe selectionne.
     */
    @Override
	public void mouseClicked(MouseEvent e){
		this.updateActive(e);
		if(activeGroup != null){
			this.grid.destroyGroup(this.activeGroup);
		}
	}

    /**
     * Herite depuis MouseMotionListener.
     * Non utilise.
     */
    @Override
	public void mouseDragged(MouseEvent e){}
    /**
     * Herite depuis MouseMotionListener.
     * Met a jour le block et groupe actif quand le curseur bouge.
     */
    @Override
	public void mouseMoved(MouseEvent e){
		this.updateActive(e);
	}

    /**
     * Met a jour le block et le curseur actif a partir de la position du MouseEvent.
     *
     * @param event la position est extraite de cet evenement pour trouver le block et group actif.
     */
	private void updateActive(MouseEvent event){
		activate(false);

		int x = event.getX();
		int y = event.getY();

		this.activeGroup = this.grid.getGroupFromDisplayCoordinates(x, y);
		this.activeBlock = this.grid.getBlockFromDisplayCoordinates(x, y);

		activate(true);
		this.gameDisplay.repaint();
	}

    /**
     * active ou desactive le block et groupe selectionne.
     *
     * @param b active si true, sinon desactive.
     */
	private void activate(boolean b){
		if(activeGroup != null && activeBlock != null){

			this.activeGroup.selectGroup(b);
			this.activeBlock.blockSelected = b;
		}
	}
}
