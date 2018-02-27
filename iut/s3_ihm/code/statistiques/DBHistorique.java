import java.sql.* ;


class DBHistorique
{
    int taux_occupation;
    int taux_non_presentation;

    double moy_occupation ;
    double moy_presentation ;

    DB database;
    
    //Constructeur préfourni
    DBHistorique(int taux_occupation_, int taux_non_presentation_) {
        this.database = new DB("bohl", "bohl", "bohl");

        this.taux_occupation = taux_occupation_;
        this.taux_non_presentation = taux_non_presentation_;
    }

    //Constructeur via la date
    DBHistorique(String date){
        this.database = new DB("bohl", "bohl", "bohl");

        String query = "SELECT * FROM Historique WHERE jour = ?";
        String[] args = {date};

        ResultSet result = this.database.executeQuery(query, args);
        try{
            if (result.next()){
                result.absolute(1) ;
                this.taux_occupation = result.getInt("taux_occupation");
                this.taux_non_presentation = result.getInt("taux_non_presentation");
            }
        }
        catch(SQLException e){
            System.out.println("[FATAL] could not parse DB output");
            e.printStackTrace();
        }
    }

    //Constructeur via une plage de dates
    DBHistorique(String date_min, String date_max)
    {
        this.database = new DB("bohl", "bohl", "bohl");

        //Récupère l'id de la date de début de période
        String query1 = "SELECT id FROM Historique WHERE jour = ?";
        String[] args1 = {date_min};
        ResultSet result1 = this.database.executeQuery(query1, args1);

        //Récupère l'id de la date de fin de période
        String query2 = "SELECT id FROM Historique WHERE jour = ?";
        String[] args2 = {date_max};
        ResultSet result2 = this.database.executeQuery(query2, args2);

        //Prépare la requete de balayage
        String query3 = "SELECT * FROM Historique WHERE id = ?";
        ResultSet result3 ;


        int id_min, id_max ; //Stocke les id
        int current_occ, current_pres ; //Valeur du taux pour la date en cours

        try{
            //Place la valeur d'id_min
            if (result1.next())
            {
                result1.absolute(1) ;
                id_min = result1.getInt("id");

                //Place la valeur d'id_max
                if (result2.next())
                {
                    result2.absolute(1) ;
                    id_max = result2.getInt("id");

                    while(id_min >= id_max)
                    {
                        String[] args3 = {date_max} ;
                        result3 = this.database.executeQuery(query3, args3) ;

                        //Occupation
                        if (result3.next())
                        {
                            result3.absolute(1) ;
                            current_occ = result3.getInt("taux_occupation") ;
                            this.moy_occupation = (this.moy_occupation + current_occ)/2 ;
                        }

                        //Presentation
                        if (result3.next())
                        {
                            result3.absolute(1) ;
                            current_pres = result3.getInt("taux_non_presentation") ;
                            this.moy_presentation = (this.moy_presentation + current_pres)/2 ;
                        }

                    }
                }
            }
        }
        catch(SQLException e){
            System.out.println("[FATAL] could not parse DB output");
            e.printStackTrace();
        }
    }
}