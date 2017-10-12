import java.awt.event.*;

public class GameMouseMotion implements MouseMotionListener, MouseListener{
	public GameDisplay gameDisplay;
	public Grid grid;

	public Group activeGroup;
	public Block activeBlock;

	public GameMouseMotion(GameDisplay d, Grid g){
		this.gameDisplay = d;
		this.grid = g;
	}
	
	//methods inherited from MouseListener
	public void mouseEntered(MouseEvent e){
		this.updateActive(e);
	}
	public void mouseExited(MouseEvent e){
		this.updateActive(e);
	}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseClicked(MouseEvent e){
		this.updateActive(e);
		if(activeGroup != null){

			this.grid.destroyGroup(this.activeGroup);
		}
	}

	//methods inherited from MouseMotionListener
	public void mouseDragged(MouseEvent e){}
	public void mouseMoved(MouseEvent e){
		this.updateActive(e);
	}

	//sets the group and block at the coordinates of the event active
	public void updateActive(MouseEvent event){
		activate(false);

		int x = event.getX();
		int y = event.getY();

		this.activeGroup = this.grid.getGroupFromDisplayCoordinates(x, y);
		this.activeBlock = this.grid.getBlockFromDisplayCoordinates(x, y);

		activate(true);
		gameDisplay.repaint(0, 0, 1000, 1000);
	}

	//activates or deactivates activeGroup and activeBlock
	public void activate(boolean b){
		if(activeGroup != null && activeBlock != null){

			this.activeGroup.selectGroup(b);
			this.activeBlock.blockSelected = b;
		}
	}
}
