import java.sql.ResultSet;
import java.sql.SQLException;



class Room {
	int number;
	int category;
	Boolean occupied;
	Boolean clean;
	String description;

	Room(int number_, int category_, boolean occupied_, boolean clean_, String description_){
        this.number = number_;
        this.category = category_;
		this.occupied = occupied_;
		this.clean = clean_;
		this.description = description_;
	}
    Room(int number_, int category_, boolean occupied_, boolean clean_){
        this.number = number_;
        this.category = category_;
        this.occupied = occupied_;
        this.clean = clean_;
    }


    static Room[] resultSetToRooms(ResultSet result){
        try{
            result.last();
            int resultNumber = result.getRow();
            Room[] rooms = new Room[resultNumber];

            result.absolute(1);
            for (int i = 0; i < resultNumber; i++) {
                int number = result.getInt("Numero");
                int category = result.getInt("Categorie") -1;
                boolean clean = result.getBoolean("Nettoyee");
                boolean occupied = result.getBoolean("Occupee");
                String description = result.getString("Description");

                rooms[i] = new Room(number, category, occupied, clean, description);
                result.next();
            }

            return rooms;
        }
        catch(SQLException e){
            System.out.println("[FATAL] could not parse DB output");
            e.printStackTrace();
            return null;
        }
    }

    String[] getInfo(){
        return new String[]{
                String.valueOf(this.number),
                RoomType.TYPE[this.category],
                this.description
        };
    }
}