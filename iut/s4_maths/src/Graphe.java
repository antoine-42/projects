import java.util.*;


class Graphe{
    // Stocke les sommets
    private LinkedList<Sommet> sommets = new LinkedList<>();
    
    private int nb_sommets = 0;
    private Integer entree_id;
    private Integer sortie_id;


    Graphe(){

    }

    // Ajoute un Sommet au graphe
    void ajouterSommet(){
        sommets.add(new Sommet(nb_sommets));
        nb_sommets++;
    }
    // Supprime le sommet n. id du graphe
    void supprimerSommet(int id){
        sommets.remove(getSommet(id));
    }

    // Ajoute une arete au graphe.
    void ajouterArete(int startId, int endId, int capacite, int flot) throws Exception {
        if (flot > capacite) {
            throw new Exception("Flot > capacite");
        }
        else {
            getSommet(startId).ajouterArete(new Arete(startId, endId, capacite, flot));
        }
    }
    // Ajoute une arete au graphe.
    void ajouterArete(int startId, int endId, int capacite){
        getSommet(startId).ajouterArete(new Arete(startId, endId, capacite));
    }
    // Ajoute une arete au graphe.
    void ajouterArete(int startId, int endId){
        getSommet(startId).ajouterArete(new Arete(startId, endId));
    }
    // Ajoute une arete au graphe.
    void supprimerArete(int startId, int endId){
        getSommet(startId).supprimerArete(endId);
    }

    // Choisir l'entree du graphe
    void setEntree(int id){
        entree_id = id;
    }
    // Choisir la sortie du graphe
    void setSortie(int id){
        sortie_id = id;
    }


    // Verifie si le flot est valide
    boolean flotValide(){
        // Il faut une entree et une sortie pour avoir un flot valide
        if (entree_id == null || sortie_id == null) {
            System.out.println("Entree ou sortie non definie");
            return false;
        }
        // Verifie la somme de flot entrant et sortant pour tout les sommets
        for (Sommet currSommet : sommets) {
            LinkedList<Arete> incoming = getIncomingAretes(currSommet.getId());
            LinkedList<Arete> outcoming = currSommet.aretes;
            int incomingSum = getArreteFlotSum(incoming);
            int outcomingSum = getArreteFlotSum(outcoming);

            currSommet.sumIn = incomingSum;

            if (currSommet.getId() == entree_id && incomingSum > 0) {
                System.out.println("flow entering entry");
                return false;
            }
            else if (currSommet.getId() == sortie_id && outcomingSum > 0) {
                System.out.println("flow leaving exit");
                return false;
            }
            if (incomingSum != outcomingSum && currSommet.getId() != entree_id && currSommet.getId() != sortie_id) {
                System.out.println(String.format("Sommet %d: incoming %d != outgoing %d", currSommet.getId(), incomingSum, outcomingSum));
                return false;
            }
        }
        return true;
    }

    // Commence le marquage
    void marquer(){
        if (!flotValide()) {
            System.out.println("Flot invalide");
            return;
        }
        else {
            System.out.println("Flot valide");
        }

        LinkedList<Sommet> file = new LinkedList<>();
        Sommet entree = getSommet(entree_id);

        for (Arete outcomingArete : entree.aretes) {
            file.add(getSommet(outcomingArete.getArrivee()));
        }

        marquerIteratif(file);
    }
    // Effectue le marquage interativement avec les sommets contenus dans file
    private void marquerIteratif(LinkedList<Sommet> file){
        LinkedList<Sommet> passed = new LinkedList<>();

        while (!file.isEmpty()) {
            //Prendre un sommet dans la file
            Sommet currSommet = file.pop();

            // TODO check if sommet was marqued during this round of marquing
            // Marquer le sommet
            currSommet.marquagePlus = new LinkedList<>();
            currSommet.marquageMoins = new LinkedList<>();

            marquerSommetPlus(currSommet);
            marquerSommetMoins(currSommet);

            // S'il a ete marque, ajouter ses fils dans la file
            if (currSommet.marque()) {
                passed.add(currSommet);
                for (Arete outcomingArete : currSommet.aretes) {
                    if (!passed.contains(outcomingArete)) {
                        file.add(getSommet(outcomingArete.getArrivee()));
                    }
                }
            }
        }
    }
    // Marquage positif d'un sommet
    private void marquerSommetPlus(Sommet sommet){
        LinkedList<Arete> incoming = getIncomingAretes(sommet.getId());
        for (Arete incomingArete : incoming) {
            int incomingSommetID = incomingArete.getDepart();
            if (incomingArete.getFlot() < incomingArete.getCapacite()) {
                sommet.marquagePlus.add(incomingSommetID);
            }
        }
    }
    // Marquage negatif d'un sommet
    private void marquerSommetMoins(Sommet sommet){
        LinkedList<Arete> incoming = getIncomingAretes(sommet.getId());
        for (Arete incomingArete : incoming) {
            int incomingSommetID = incomingArete.getDepart();
            LinkedList<Arete> currIncoming = (LinkedList<Arete>) incoming.clone();
            currIncoming.remove(incomingArete);
            if (incomingArete.getFlot() > 0 && getArreteCapaciteSum(currIncoming) > 0 ) {
                sommet.marquageMoins.add(incomingSommetID);
            }
        }
    }

    // Cherche a augmenter le flot d'un graphe
    void augmenterFlot(){
        LinkedList<Sommet> chemin = getChemin();
        while (chemin != null) {
            // augmenter le flot a partir du chemin obtenu

            // Ca marche pas :/
            
            // marquer a nouveau et obtenir un nouveau chemin 
            marquer();
            LinkedList<Sommet> chemin = getChemin();
        }
    }
    // Donne un chemin du debut a la fin avec que des sommets marques
    private LinkedList<Sommet> getChemin(){
        LinkedList<Sommet> passed = new LinkedList<>();
        LinkedList<Sommet> chemin = new LinkedList<>();
        chemin.add(getSommet(entree_id));

        while (chemin.getLast().getId() != sortie_id){
            LinkedList<Sommet> successeurs = getSuccesseursMarques(chemin.getLast());

            if (successeurs.size() == 0){
                // Aller a une autre branche
                while (true){
                    chemin.pop();
                    LinkedList<Sommet> backtrackSuccesseurs = getSuccesseursMarques(chemin.getLast());

                    if (backtrackSuccesseurs.size() > 0){
                        chemin.add(backtrackSuccesseurs.getFirst());
                        break;
                    }
                    else if (chemin.size() == 0){
                        // Fail, aucun chemin valide
                        return null;
                    }
                }
            }
            else {
                Sommet currSommet = successeurs.pop();
                chemin.add(currSommet);
                passed.add(currSommet);

                successeurs.removeAll(passed);
            }
        }
        return chemin;
    }
    // Donne tout les successeurs d'un sommets qui sont marques
    LinkedList<Sommet> getSuccesseursMarques(Sommet sommet){
        LinkedList<Sommet> successeursMarques = new LinkedList<>();

        for (Arete arete : sommet.aretes){
            Sommet currSommet = getSommet(arete.getArrivee());
            if (currSommet.marque()){
                successeursMarques.add(currSommet);
            }
        }

        return successeursMarques;
    }
    // Donne le flot maximal entre les arretes
    static int maxFlot(LinkedList<Arete> aretes) throws Exception{
        int minFlot = Integer.MAX_VALUE;
        int lastArrivee = aretes.get(0).getDepart();

        for(Arete arete : aretes){
            if (lastArrivee != arete.getDepart()){ // On doit partir du dernier sommet d'arrivee.
                throw new Exception("Les Aretes ne sont pas liees!");
            }
            if (arete.getFlot() < minFlot){
                minFlot = arete.getFlot();
            }

            lastArrivee = arete.getArrivee();
        }
        return minFlot;
    }


    // Obtenir le sommet avec id
    private Sommet getSommet(int id){
        for (Sommet currSommet : sommets) {
            if (currSommet.getId() == id) {
                return currSommet;
            }
        }
        return null;
    }

    // Obtenir les aretes allant dans id
    private LinkedList<Arete> getIncomingAretes(int id){
        LinkedList<Arete> results = new LinkedList<>();
        for (Sommet currSommet : sommets) {
            for (Arete currArete : currSommet.aretes) {
                if (currArete.getArrivee() == id) {
                    results.add(currArete);
                }
            }
        }
        return results;
    }

    // Obtenir la somme des flots des arretes de la liste
    private int getArreteFlotSum(LinkedList<Arete> areteList){
        int sum = 0;
        for (Arete arete : areteList) {
            sum += arete.getFlot();
        }
        return sum;
    }
    // Obtenir la somme des capacites des arretes de la liste
    private int getArreteCapaciteSum(LinkedList<Arete> areteList){
        int sum = 0;
        for (Arete arete : areteList) {
            sum += arete.getCapacite();
        }
        return sum;
    }


    // Generer une representation en string du graphe
    public String toString(){
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(String.format("Graphe: %d sommets\n", nb_sommets));

        for (Sommet currSommet : sommets) {
            resultBuilder.append(currSommet);
            resultBuilder.append("\n");
        }

        return resultBuilder.toString();
    }
}