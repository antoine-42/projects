import java.sql.*;


class DB {
    boolean offline = false;
    private Connection connection;


    DB(String location, String user, String password){
        try{
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch(ClassNotFoundException e) {
            this.offline = true;
            System.out.println("[FATAL] could not load db driver");
            e.printStackTrace();
        }

        try{
            connection = DriverManager.getConnection("jdbc:mariadb://dwarves.iut-fbleau.fr/" + location, user, password);
        }
        catch(SQLException e) {
            offline = true;
            System.out.println("[FATAL] could not connect to db");
            e.printStackTrace();
        }
    }

    ResultSet executeQuery(String query, String[] args){
        ResultSet results = null;
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < args.length; i++) {
                statement.setString(i+1, args[i]);
            }
            results = statement.executeQuery();

            try{
                statement.close();
            }
            catch(Exception e){
                System.out.println("could not close connection");
            }
        }
        catch(SQLException e) {
            offline = true;
            System.out.println("[FATAL] could not connect to db");
            e.printStackTrace();
        }

        if (offline) {
            return null;
        }
        return results;
    }

    boolean closeConnection(){
        try{
            connection.close();
        }
        catch(Exception e){
            return false;
        }
        return true;
    }
}
