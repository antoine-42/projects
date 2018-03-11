import java.util.*;


class Sommet {
    LinkedList<Arete> aretes = new LinkedList<>();

    private int id;
    LinkedList<Integer> marquagePlus = new LinkedList<>();
    LinkedList<Integer> marquageMoins = new LinkedList<>();

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

    boolean marque(){
        return !marquageMoins.isEmpty() || !marquagePlus.isEmpty();
    }


    private String marquageToString(Integer i, boolean plus){
        if (plus){
            return "+" + i + " ";
        }
        else {
            return "-" + i + " ";
        }
    }
    public String toString(){
        StringBuilder resultBuilder = new StringBuilder();
        StringBuilder marquageBuilder = new StringBuilder();

        for (Integer currMarquage : marquagePlus) {
            marquageBuilder.append(marquageToString(currMarquage, true));
        }
        for (Integer currMarquage : marquageMoins) {
            marquageBuilder.append(marquageToString(currMarquage, false));
        }

        resultBuilder.append(String.format("Sommet %d    sum: %d  marquage: %s\n",
                id, sumIn, marquageBuilder.toString()));

        if (aretes.size() > 0) {
            resultBuilder.append(String.format("%d aretes:\n", aretes.size()));
            for (Arete currAretes : aretes) {
                resultBuilder.append(currAretes);
                resultBuilder.append("\n");
            }
        }

        return resultBuilder.toString();
    }
}