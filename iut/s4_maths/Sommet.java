import java.util.*;


class Sommet {
    LinkedList<Arete> aretes = new LinkedList<Arete>();

    private int id;
    LinkedList<Integer> marquagePlus = new LinkedList<Integer>();
    LinkedList<Integer> marquageMoins = new LinkedList<Integer>();

    int sumIn = 0;


    public Sommet(int i){
        id = i;
    }


    void ajouterArete(Arete a){
        aretes.add(a);
    }
    void supprimerArete(int dest){
        for (Arete currArete : aretes) {
            if (currArete.getArrivee() == dest) {
                aretes.remove(currArete);
            }
        }
    }


    int getId(){
        return id;
    }


    public String toString(){
        String result = "";

        String marquage = "";
        for (Integer currMarquage : marquagePlus) {
            marquage += "+" + currMarquage + " ";
        }
        for (Integer currMarquage : marquageMoins) {
            marquage += "-" + currMarquage + " ";
        }

        result += String.format("Sommet %d    sum: %d  marquage: %s\n", id, sumIn, marquage);

        if (aretes.size() > 0) {
            result += String.format("%d aretes:\n", aretes.size());
            for (Arete currAretes : aretes) {
                result += currAretes + "\n";
            }
        }

        return result;
    }
}