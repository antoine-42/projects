import java.util.*;
import java.text.*;
import java.sql.ResultSet;
import java.sql.SQLException;


class Reservation {
	Client client;
	String reference;
	int category;
	Date start;
	Date end;
	int length;

	Reservation(Client client_, String reference_, int category_, Date start_, int length_){
		client = client_;
		reference = reference_;
		category = category_;
		start = start_;
		length = length_;

		end = new Date(this.start.getTime() + 86400*this.length);
	}


	static Reservation[] resultSetToReservations(ResultSet result){
		try{
			result.last();
			int resultNumber = result.getRow();
			Reservation[] reservations = new Reservation[resultNumber];

			result.absolute(1);
			for (int i = 0; i < resultNumber; i++) {
				String resultLastName = result.getString("nom");
				String resultName = result.getString("prenom");
				Client client = new Client(resultLastName, resultName);

				String reference = result.getString("reference");
				int category = result.getInt("categorie") -1;
				int length = result.getInt("nuits");

				Date start;
				try{
					start = Utils.DATE_FORMAT.parse(result.getString("debut"));
				}
				catch(ParseException e){
					System.out.println("[FATAL] could not parse DB output");
					e.printStackTrace();
					return null;
				}

				reservations[i] = new Reservation(client, reference, category, start, length);
				result.next();
			}

			return reservations;
		}
		catch(SQLException e){
			System.out.println("[FATAL] could not parse DB output");
			e.printStackTrace();
			return null;
		}
	}

	String[] getInfo(){
        return new String[]{
            this.client.lastName,
            this.client.name,
            this.reference,
            RoomType.TYPE[this.category],
            Utils.DATE_FORMAT_HUMAN.format(this.start),
            String.valueOf(this.length) + " Jours"
        };
    }
}