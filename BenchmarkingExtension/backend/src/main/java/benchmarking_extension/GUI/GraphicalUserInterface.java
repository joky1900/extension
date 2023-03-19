package benchmarking_extension.GUI;

import benchmarking_extension.GUI.menu.Menu;
import benchmarking_extension.graph.*;
import org.jfree.chart.plot.PlotOrientation;

import java.awt.*;

/**
 * Main class for the GUI
 *
 * @author John Kyrk, Sylwia Gagas
 */
public class GraphicalUserInterface {
    // Window size
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 900;

    // Main window
    private static final MainFrame mainFrame = new MainFrame(WIDTH, HEIGHT);
    private final Menu menu = new Menu(this);

    // Default graph is an empty bar graph
    private static Graph graph = new LineGraph("Add a file...", "", "", PlotOrientation.VERTICAL, new int[0][0]);

    public GraphicalUserInterface(){;
        // Add the menu and graph to the main frame
        mainFrame.setJMenuBar(menu);
        mainFrame.add(graph);
    }

    /**
     * Public mutator to change the graph to a new line graph
     * @param newGraph a LineGraph object
     */
    public static void setGraph(LineGraph newGraph){
        graph = newGraph;
        mainFrame.getContentPane().removeAll();
        mainFrame.add(graph);
    }

    /**
     * Public mutator to change the graph to a new line graph
     * @param newGraph a BarGraph object
     */
    public static void setBarGraph(BarGraph newGraph){
        graph = newGraph;
        mainFrame.getContentPane().removeAll();
        mainFrame.add(graph);
    }

    /**
     * Public mutator changing the text size of the graph
     * @param size size in integer
     */
    public static void changeTextSize(int size){
        graph.setTextSize(size);
    }

    /**
     * Change background color of the graph
     * @param color color Type (R = Red, G = Green, B = Blue)
     * @param value integer 0-255 for color intensity
     */
    public void changeColor(String color, int value){
        Color tmpColor = graph.getColor();

        switch(color){
            case "R" -> {
                graph.setColor(new Color(value, tmpColor.getGreen(), tmpColor.getBlue()));
            }
            case "G" -> {
                graph.setColor(new Color(tmpColor.getRed(), value, tmpColor.getBlue()));
            }
            case "B" -> {
                graph.setColor(new Color(tmpColor.getRed(), tmpColor.getGreen(), value));
            }
        }
    }

    /**
     * Public accessor retrieving the RGB color of the graph
     * @return Color AWT
     */
    public Color getColor(){
        return graph.getColor();
    }

    public static Graph getGraph(){
        return graph;
    }
}
