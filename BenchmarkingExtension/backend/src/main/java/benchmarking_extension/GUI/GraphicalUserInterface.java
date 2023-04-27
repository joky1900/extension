package benchmarking_extension.GUI;

import benchmarking_extension.GUI.menu.Menu;
import benchmarking_extension.graph.*;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import java.awt.*;
import java.io.IOException;

/**
 * Main class for the GUI
 *
 * @author John Kyrk, Sylwia Gagas
 * @version 1.0
 * @since 2023-04-23
 */
public class GraphicalUserInterface {
    // Window size
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 900;

    // Main window
    private static final Menu menu = new Menu();

    // Default graph is an empty line graph
    private static Graph graph = new LineGraph("Add a file...", "", "", PlotOrientation.VERTICAL, new double[0][0], new double[0][0]);
    private static final MainFrame mainFrame = new MainFrame(WIDTH, HEIGHT);
    public GraphicalUserInterface(){;
        // Add the menu and graph to the main frame
        mainFrame.setJMenuBar(menu);
        mainFrame.add(graph);
        initColor();
    }

    /**
     * Public mutator to change the graph
     * @param newGraph a LineGraph or BarGraph object
     */
    public static void setGraph(Graph newGraph){
        graph = newGraph;
        mainFrame.setContentPane(graph);
        mainFrame.revalidate();
    }

    /**
     * Public mutator changing the text size of the graph
     * @param size size in integer
     */
    public static void changeTextSize(int size){
        graph.setTextSize(size);
    }


    public static void initColor(){
        graph.setColor(new Color(255, 255 ,255));
    }
    /**
     * Change background color of the graph
     * @param color color Type (R = Red, G = Green, B = Blue)
     * @param value integer 0-255 for color intensity
     */
    public static void changeColor(String color, int value){
        Color tmpColor = graph.getBackgroundColor();

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

    public static void setColor(Color color){
        graph.setColor(color);
    }

    /**
     * Public accessor retrieving the RGB color of the graph
     * @return Color AWT
     */
    public static Color getBackgroundColor(){
        return graph.getBackgroundColor();
    }

    public static JFreeChart getChart(){
        return graph.getChart();
    }

    public static void updateSubjects(){
        try {
            menu.updateSubjectNumberMenu();
            System.out.println("Updated subject number menu");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
