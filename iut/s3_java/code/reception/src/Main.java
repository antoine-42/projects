public class Main {
    public static void main(String[] args) {
        //enable antialiasing
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");

        Reception reception = new Reception();
    }
}
