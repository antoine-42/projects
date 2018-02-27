import java.awt.event.* ;
import javax.swing.* ;
import java.awt.* ;

public class MenuActionListener implements ActionListener
{
	MenuDeroulant menu ;

	JComboBox<String> jour ;
	JComboBox<String> mois ;
	JComboBox<String> annee ;

	public MenuActionListener(MenuDeroulant menu_, JComboBox<String> jour_, JComboBox<String> mois_, JComboBox<String> annee_)
	{
		menu = menu_ ;

		jour = jour_ ;
		mois = mois_ ;
		annee = annee_ ;

	}

	public void actionPerformed(ActionEvent e)
	{
		String action =e.getActionCommand() ;

		if(action.equals("Valider"))
		{
			menu.setDateSelected(jour.getSelectedItem(), mois.getSelectedItem(), annee.getSelectedItem()) ;
			menu.getStats() ;
		}
	}
}