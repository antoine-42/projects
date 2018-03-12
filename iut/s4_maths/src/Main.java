public class Main {
    public static void main(String[] args) throws Exception {
        test1();
    }

    private static void test0() throws Exception{
        Graphe g = new Graphe();

        g.ajouterSommet();
        g.ajouterSommet();
        g.ajouterSommet();
        g.ajouterSommet();

        g.ajouterArete(0, 1, 1, 1);
        g.ajouterArete(0, 2, 3, 2);

        g.ajouterArete(1, 2, 2, 1);
        g.ajouterArete(2, 1, 2, 1);

        g.ajouterArete(1, 3, 2, 1);
        g.ajouterArete(2, 3, 2, 2);

        g.setEntree(0);
        g.setSortie(3);

        g.marquer();
        System.out.println(g);
    }
    private static void test1() throws Exception{
        Graphe g = new Graphe();

        g.ajouterSommet();
        g.ajouterSommet();
        g.ajouterSommet();
        g.ajouterSommet();

        g.ajouterArete(0, 1, 3, 3);
        g.ajouterArete(0, 2, 6, 2);

        g.ajouterArete(2, 1, 4, 1);

        g.ajouterArete(1, 3, 6, 4);
        g.ajouterArete(2, 3, 1, 1);

        g.setEntree(0);
        g.setSortie(3);

        g.marquer();
        System.out.println(g);
    }
    private static void test2() throws Exception{
        Graphe g = new Graphe();

        g.ajouterSommet();
        g.ajouterSommet();
        g.ajouterSommet();
        g.ajouterSommet();
        g.ajouterSommet();
        g.ajouterSommet();

        g.ajouterArete(0, 1, 2, 2);
        g.ajouterArete(0, 2, 6, 0);
        g.ajouterArete(0, 3, 4, 2);

        g.ajouterArete(1, 4, 2, 2);
        g.ajouterArete(1, 5, 4, 0);

        g.ajouterArete(2, 1, 8, 0);

        g.ajouterArete(3, 5, 2, 2);

        g.ajouterArete(4, 5, 4, 2);

        g.setEntree(0);
        g.setSortie(5);

        g.marquer();
        System.out.println(g);
    }
    private static void test3() throws Exception{
        Graphe g = new Graphe();

        g.ajouterSommet();
        g.ajouterSommet();
        g.ajouterSommet();
        g.ajouterSommet();

        g.ajouterArete(0, 1, 100, 0);
        g.ajouterArete(0, 2, 100, 1);

        g.ajouterArete(2, 1, 1, 1);

        g.ajouterArete(1, 3, 100, 1);
        g.ajouterArete(2, 3, 100, 0);

        g.setEntree(0);
        g.setSortie(3);

        g.marquer();
        System.out.println(g);
    }
}
