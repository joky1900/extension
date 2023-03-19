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
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 900;

    private static final MainFrame mainFrame = new MainFrame(WIDTH, HEIGHT);
    private final Menu menu = new Menu(this);

    // Default graph is a bar graph
    private static LineGraph graph = new LineGraph("Add a file...", "", "", PlotOrientation.VERTICAL, new int[0][0]);

    public GraphicalUserInterface(){;
        // Initialize main frame
        mainFrame.setJMenuBar(menu);
        mainFrame.add(graph);
    }

    public static void setGraph(LineGraph newGraph){
        graph = newGraph;
        mainFrame.getContentPane().removeAll();
        mainFrame.add(graph);
    }

    public void changeGraphType(GraphType type){
        switch(type){
          //  case BAR -> new BarGraph();
          //  case LINE -> new LineGraph();
        }
    }

    public static void changeTextSize(int size){
        graph.setTextSize(size);
    }

    /**
     * Change backgroud color of the graph
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

    public Color getColor(){
        return graph.getColor();
    }


}
