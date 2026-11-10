package main;

/** A view-only demo. This is not the finished office-hours application. */
public class ConsoleViewDemo {
    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        view.showMenu("View demo: type a name, or type quit to stop.");
        String name = view.readLine("Name: ");
        while (name != null && !name.equals("quit")) {
            view.showMessage("You typed: " + name);
            name = view.readLine("Name: ");
        }
        view.showMessage("Demo ended.");
    }
}
