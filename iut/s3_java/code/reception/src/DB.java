import java.sql.*;


class DB {
    boolean offline = false;
    private Connection connection;


    DB(String location, String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");

        connection = DriverManager.getConnection("jdbc:mariadb://dwarves.iut-fbleau.fr/" + location, user, password);
    }

    void testDB(String table) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table);
        ResultSet set = statement.executeQuery();
        set.absolute(1);
        statement.close();
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
            System.out.println("[FATAL] could not execute query");
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
