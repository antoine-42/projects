class Arete {
    private int depart;
    private int arrivee;
    private int capacite;
    private int flot;

    public Arete(int d, int a, int c, int f){
        arrivee = a;
        capacite = c;
        flot = f;
    }
    public Arete(int d, int a, int c){
        arrivee = a;
        capacite = c;
        flot = 0;
    }
    public Arete(int d, int a){
        arrivee = a;
        capacite = 0;
        flot = 0;
    }

    int getDepart(){
        return depart;
    }
    int getArrivee(){
        return arrivee;
    }
    void setFlot(int f){
        flot = f;
    }
    int getFlot(){
        return flot;
    }
    int getCapacite(){
        return capacite;
    }


    public String toString(){
        return String.format("    arrivee: %d capacite: %d flot: %d", arrivee, capacite, flot);
    }
}