import services.ApplicationService;

/**
 * Main.java - Entry point for Strategy Pattern Payment System
 * Delegates all functionality to ApplicationService for clean separation of concerns
 */
public class Main {
    public static void main(String[] args) {
        ApplicationService app = new ApplicationService();
        app.run();
    }
}
