package benchmarking_extension.GUI;

import benchmarking_extension.graph.Graph;

import java.awt.*;
import javax.swing.*;

/**
 * Main Window of the program
 *
 * @author John Kyrk, Sylwia Gagas
 */
public class MainFrame extends JFrame {
    private final String header = "Benchmarking Extension";

    public MainFrame(final int width, final int height) {
        // default window-size.
        this.setSize(width, height);
        // application closes when the "x" in the upper-right corner is clicked.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setTitle(header);
        this.setVisible(true);

        // Changes layout from default to BorderLayout
        this.setLayout(new BorderLayout());
    }
}
