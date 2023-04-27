package benchmarking_extension;

import benchmarking_extension.GUI.GraphicalUserInterface;
import javax.swing.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * DT002G Project
 *
 * @author John Kyrk, Sylwia Gagas
 * @version 1.0
 * @since 2023-04-23
 */
public class Backend {
    private static final Logger logger = Logger.getLogger("backend");

    /**
     * Main class of the program
     * @param args not used
     */
    public static void main(final String[] args) {
        // Make sure GUI is created on the event dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                logger.log(Level.WARNING, "Logging...");
                Model model = new Model();
                Controller controller = new Controller(model);
                GraphicalUserInterface GUI = new GraphicalUserInterface();
            }
        });
    }


}