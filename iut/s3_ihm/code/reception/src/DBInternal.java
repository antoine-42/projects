import java.sql.*;


class DBInternal {
    private DB dataBase;


    DBInternal() throws ClassNotFoundException, SQLException{
        this.dataBase = new DB("bohl", "bohl", "bohl");
        this.dataBase.testDB("Chambre");
    }


    Room[] searchRoom(int roomType){
        String query = "SELECT * FROM Chambre WHERE Categorie = ? AND Nettoyee AND !Occupee";
        String[] args = {String.valueOf(roomType +1)};

        return Room.resultSetToRooms(dataBase.executeQuery(query, args));
    }
    void affectRoom(int roomNumber){
        String query = "UPDATE Chambre SET Occupee=1 WHERE Numero = ?";
        String[] args = {String.valueOf(roomNumber)};

        dataBase.executeQuery(query, args);
    }

    boolean checkReservationValidated(String reference){
        String query = "SELECT * FROM Presentations WHERE Reservation = ?";
        String[] args = {String.valueOf(reference)};

        ResultSet set = dataBase.executeQuery(query, args);
        try{
            return set.next();
        }
        catch (Exception e) {
            System.out.println("Exception");
            return false;
        }
    }
    void validReservation(String reference, String room){
        String query = "INSERT INTO Presentations(Jour, Reservation, Chambre) VALUES (CURDATE(), ?, ?)";
        String[] args = {reference, room};

        dataBase.executeQuery(query, args);
    }


    boolean closeConnection(){
        return this.dataBase.closeConnection();
    }
}
