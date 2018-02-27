import javax.swing.* ;
import java.awt.* ;


public class Fenetre extends JFrame
{
	public Fenetre()
	{
		super("Statistiques") ;

		Dimension taille = new Dimension(750, 75) ;
		Dimension tailleMini = new Dimension(250, 75) ;
		Dimension tailleMaxi = new Dimension(1500, 1500) ;
		//Dimension position = new Dimension(20, 20) ;

		this.setMinimumSize(tailleMini) ;
		this.setMaximumSize(tailleMaxi) ;
		this.setSize(taille) ;
		this.setLocation(20, 20) ;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		MenuDeroulant debut = new MenuDeroulant() ;
		this.setContentPane(debut.createConteneur()) ;
	}
}