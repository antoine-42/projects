import java.awt.* ;
import java.sql.* ;
import java.lang.* ;

public class Statistiques
{

	Statistiques()
	{
	}

	public String getOccupation1Day(String day)
	{
		String occupation ;

		occupation = String.valueOf(new DBHistorique(day).taux_occupation) ;

		return occupation ;
	}

}