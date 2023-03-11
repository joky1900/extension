package benchmarking_extension;

import javax.swing.*;

/**
 * DT002G Project
 *
 * @author John Kyrk, Sylwia Gagas
 * @version 1.0
 * @since 2023-03-01
 */
public class Backend {
    public static void main(String[] args) {
        // Make sure GUI is created on the event dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BenchmarkingExtension();
                /**
                try {
                    new BenchmarkingExtension();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                 */
            }
        });
    }
}