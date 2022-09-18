package hrps;

import hrps.boundary.UI;

/**
 * The class that contains the UI and the only main() method that runs the app.
 */
public class HRPS {
    /**
     * Main UI of the app.
     */
    private final UI ui;

    /**
     * Creates an app by get the instance of the main UI.
     */
    private HRPS() {
        ui = UI.getInstance();
    }

    /**
     * Runs the UI.
     */
    private void run() {
        ui.run();
    }

    /**
     * Runs the app.
     *
     * @param args A String array contains arguments passed to the program when starting the program.
     */
    public static void main(String[] args) {
        new HRPS().run();
    }
}
