import javax.swing.* ;
import java.awt.* ;


public class MenuDeroulant extends JComponent
{
	private final char pourcent = (char) 37 ;
	private final String message1 = "Pas de résultat pour la date souhaitée" ;

	private JComboBox<String> listeJour ;
	private JComboBox<String> listeMois ;
	private JComboBox<String> listeAnnee ;
	private String currentDate ;
	private String currentDateHuman ;
	private JLabel occupationTexte = new JLabel(message1) ;

	public MenuDeroulant()
	{
	} 

	public JPanel createConteneur()
	{

		JPanel panneau = new JPanel() ;

		JLabel labelDebut = new JLabel("Debut") ;

		/*Liste déroulante*/
		final int NBR_JOUR_MOIS = 31 ;
		final int NBR_MOIS_AN = 12 ;
		final int ANNEE_EN_COURS = 2017 ; //MODIFIER POUR PLUS TARD --> AUTOMATISATION

		this.listeJour = new JComboBox<String>() ;
		this.listeMois = new JComboBox<String>() ;
		this.listeAnnee = new JComboBox<String>() ;
		
		int i ; //Compteur

		//Rempli une liste de jours
		for(i=1 ; i<=NBR_JOUR_MOIS ; i++)
			this.listeJour.addItem(String.valueOf(i)) ;

		//Rempli une liste de mois
		for(i=1 ; i<=NBR_MOIS_AN ; i++)
			this.listeMois.addItem(String.valueOf(i)) ;

		//Rempli une liste d'années
		for(i=-1 ; i<=1 ; i++)
			this.listeAnnee.addItem(String.valueOf(ANNEE_EN_COURS + i)) ;

		/*Contrôleur*/
		MenuActionListener controleur = new MenuActionListener(this, listeJour, listeMois, listeAnnee) ;

		/*Bouton de validation*/
		JButton valider = new JButton("Valider") ;
		valider.addActionListener(controleur) ;


		/*Ajouts au panneau*/
		panneau.add(labelDebut) ;
		panneau.add(listeJour) ;
		panneau.add(listeMois) ;
		panneau.add(listeAnnee) ;
		panneau.add(valider) ;
		panneau.add(occupationTexte) ;

		return panneau ;
	}

	/*Getters composants date*/
	public JComboBox<String> getListeJour()
	{
		return listeJour ;
	}

	public JComboBox getListeMois()
	{
		return listeMois ;
	}

	public JComboBox<String> getListeAnnee()
	{
		return listeAnnee ;
	}


	/*Set Date of the selected day*/
	public void setDateSelected(Object jour, Object mois, Object annee)
	{
		 this.currentDate = annee+"-"+mois+"-"+jour ;
		 this.currentDateHuman = jour + "/" + mois + "/" + annee ;
	}

	/*Get Date of the selected day*/
	public String getDateSelected()
	{
		return currentDate ;
	}

	public void getStats()
	{
		Statistiques stats = new Statistiques() ;
		String occupation = stats.getOccupation1Day(this.currentDate) ;
		String nonOccupation = stats.getOccupation1Day(this.currentDate) ;
		final String message1 = String.format("<html>L\'occupation pour le %s est de %s%c<br>", currentDateHuman, occupation, this.pourcent) ;
		final String message2 = String.format("Les non-présentations représentent, pour le %s, %s%c</html>", currentDateHuman, nonOccupation, this.pourcent) ;

		this.occupationTexte.setText(message1+message2) ;
	}


}


/*
	String[] mois = new String[]{"Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre"} ;
*/