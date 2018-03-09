import java.util.*;


class Graphe{
    private LinkedList<Sommet> sommets = new LinkedList<Sommet>();
    
    private int nb_sommets = 0;
    private Integer entree_id;
    private Integer sortie_id;


    public Graphe(){

    }

    void ajouterSommet(){
        sommets.add(new Sommet(nb_sommets));
        nb_sommets++;
    }
    void supprimerSommet(int id){
        sommets.remove(getSommet(id));
    }

    void ajouterArete(int startId, int endId, int capacite, int flot) {
        if (flot > capacite) {
            System.out.println("Flot > capacite");
        }
        else {
            getSommet(startId).ajouterArete(new Arete(startId, endId, capacite, flot));
        }
    }
    void ajouterArete(int startId, int endId, int capacite){
        getSommet(startId).ajouterArete(new Arete(startId, endId, capacite));
    }
    void ajouterArete(int startId, int endId){
        getSommet(startId).ajouterArete(new Arete(startId, endId));
    }
    void supprimerArete(int startId, int endId){
        getSommet(startId).supprimerArete(endId);
    }

    void setEntree(int id){
        entree_id = id;
    }
    void setSortie(int id){
        sortie_id = id;
    }


    boolean flotValide(){
        if (entree_id == null || sortie_id == null) {
            return false;
        }

        for (Sommet currSommet : sommets) {
            LinkedList<Arete> incoming = getIncomingAretes(currSommet.getId());
            LinkedList<Arete> outcoming = currSommet.aretes;
            int incomingSum = getArreteFlotSum(incoming);
            int outcomingSum = getArreteFlotSum(outcoming);

            currSommet.sumIn = incomingSum;
            if (incomingSum != outcomingSum) {
                if (currSommet.getId() == entree_id) {
                    if (incomingSum > 0) {
                        System.out.println("flow entering entry");
                        return false;
                    }
                }
                else if (currSommet.getId() == sortie_id) {
                    if (outcomingSum > 0) {
                        System.out.println("flow leaving exit");
                        return false;
                    }
                }
                else {
                    System.out.println(String.format("Sommet %d: incoming %d != outgoing %d", currSommet.getId(), incomingSum, outcomingSum));
                    return false;
                }
            }
        }
        return true;
    }

    void marquer(){
        if (!flotValide()) {
            System.out.println("Flot invalide");
            return;
        }
        else {
            System.out.println("Flot valide");
        }

        LinkedList<Sommet> file = new LinkedList<Sommet>();
        marquerIteratif(file);
    }
    private void marquerIteratif(LinkedList<Sommet> file){
        Sommet entree = getSommet(entree_id);

        for (Arete outcomingArete : entree.aretes) {
            file.add(getSommet(outcomingArete.getArrivee()));
        }

        while (!file.isEmpty()) {
            Sommet currSommet = file.pop();

            currSommet.marquagePlus = new LinkedList<Integer>();
            currSommet.marquageMoins = new LinkedList<Integer>();

            marquerSommetPlus(currSommet);
            //marquerSommetMoins(currSommet);

            if (!currSommet.marquagePlus.isEmpty() || !currSommet.marquageMoins.isEmpty()) {
                for (Arete outcomingArete : currSommet.aretes) {
                    file.add(getSommet(outcomingArete.getArrivee()));
                }
            }
        }
    }
    private void marquerSommetPlus(Sommet sommet){
        LinkedList<Arete> incoming = getIncomingAretes(sommet.getId());
        for (Arete incomingArete : incoming) {
            Sommet incomingSommetID = incomingArete.getDepart().getId();
            if (incomingArete.getFlot() < incomingArete.getCapacite()) {
                sommet.marquagePlus.add(incomingSommetID);
            }
        }
    }
    private void marquerSommetMoins(Sommet sommet){
        LinkedList<Arete> outcoming = sommet.aretes;
        for (Arete outcomingArete : outcoming) {
            if (outcomingArete.getFlot() < outcomingArete.getCapacite()) {
                sommet.marquageMoins.add(outcomingArete.getArrivee());
            }
        }
    }


    private Sommet getSommet(int id){
        for (Sommet currSommet : sommets) {
            if (currSommet.getId() == id) {
                return currSommet;
            }
        }
        return null;
    }

    private LinkedList<Arete> getIncomingAretes(int id){
        LinkedList<Arete> results = new LinkedList<Arete>();
        for (Sommet currSommet : sommets) {
            for (Arete currArete : currSommet.aretes) {
                if (currArete.getArrivee() == id) {
                    results.add(currArete);
                }
            }
        }
        return results;
    }

    private int getArreteFlotSum(LinkedList<Arete> areteList){
        int sum = 0;
        for (Arete arete : areteList) {
            sum += arete.getFlot();
        }
        return sum;
    }
    private int getArreteCapaciteSum(LinkedList<Arete> areteList){
        int sum = 0;
        for (Arete arete : areteList) {
            sum += arete.getCapacite();
        }
        return sum;
    }


    public String toString(){
        String result = "";
        result += String.format("Graphe: %d sommets\n", nb_sommets);

        for (Sommet currSommet : sommets) {
            result += currSommet + "\n";
        }

        return result;
    }
}