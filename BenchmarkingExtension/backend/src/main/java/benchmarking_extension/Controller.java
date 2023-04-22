package benchmarking_extension;

import benchmarking_extension.GUI.GraphicalUserInterface;
import benchmarking_extension.graph.BarGraph;
import benchmarking_extension.graph.GraphType;
import benchmarking_extension.graph.LineGraph;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.plot.PlotOrientation;

import java.awt.*;
import java.io.File;

/**
 * Controller class working as an intermediate between the GUI and the Model
 *
 * @author John Kyrk
 * @since 2023-04-20
 * @version 1.0
 */
public final class Controller {
    private static Model model;
    private static final String PATH = "src/main/resources/";

    /**
     * Default constructor
     * @param model model to connect with the controller
     */
    public Controller(final Model model){
        Controller.model = model;
    }

    /**
     *
     */
    public static void saveToCSV(){
        model.saveToCSV();
    }

    /**
     *
     */
    public static void updateGraph(){
        try {
            changeGraphType(GraphType.LINE);
        } catch (Exception e){
            System.out.println("Corrupt files!");
        }
    }

    /**
     * Loads the data from the selected files
     */
    public static void loadJSON(){
        model.parseFiles();
    }

    /**
     * Changes the graph based on the type of graph
     * @param type
     */
    public static void changeGraphType(final GraphType type) {
        // Get background color
        Color color = GraphicalUserInterface.getBackgroundColor();
        switch (type) {
            case BAR -> {
                GraphicalUserInterface.setGraph(new BarGraph("Average Distance Between Pixels",
                        "", "Distance (pixel)", PlotOrientation.VERTICAL, model.getAverageDistance()));
            }
            case LINE -> updateLineGraph();
            default -> System.out.println("Something went wrong when changing graph type!");
        }

        // Set same color for the new graph
       GraphicalUserInterface.setColor(color);

    }

    /**
     *
     */
    private static void updateLineGraph(){
        Color color = GraphicalUserInterface.getBackgroundColor();
        double[][] data = model.getGazeData();
        double[][] data2 = model.getBallData();
        GraphicalUserInterface.setGraph(new LineGraph("X-Position Over Time",
                "Time (ms)", "Pixel", PlotOrientation.VERTICAL, data, data2));
        GraphicalUserInterface.setColor(color);
    }

    /**
     *
     */
    public static void updateLineGraphAverage(){
        Color color = GraphicalUserInterface.getBackgroundColor();
        GraphicalUserInterface.setGraph(new LineGraph("X-Position Over Time",
                "Time (ms)", "Pixel", PlotOrientation.VERTICAL, model.getSingleAverageData()));

        GraphicalUserInterface.setColor(color);
    }

    /**
     * Saves the currently displayed graph to an image
     */
    public static void saveToImage()  {
        try {
            ChartUtils.saveChartAsPNG(new File(PATH + "line_chart.png"),
                    GraphicalUserInterface.getChart(), 800, 800);
        } catch (Exception e){
            System.out.println("Could not save image...");
        }
    }

    //-----------------------------------------------------------------------------------
    // Public Accessors
    //-----------------------------------------------------------------------------------

    /**
     * Sets the array of json files in model
     * @param files array of files
     */
    public static void setFiles(final File[] files){
        model.setFiles(files);
    }

    /**
     * Returns the currently selected set
     * @return currently selected set
     */
    public static String getSet() {
        return model.getSet();
    }

    /**
     * Gets the total number of subjects
     * @return integer indicating the total amount of subjects
     */
    public static int getSubjectTotal(){
        return model.getSubjectTotal();
    }

    /**
     * Changes the subject to be selected
     * @param i subject id
     */
    public static void changeSubjectID(final int i) {
        // Get current background color
        Color color = GraphicalUserInterface.getBackgroundColor();
        model.setSubjectID(i);  // Update the subject ID
        updateLineGraph();

        // Set same color for the new graph
        GraphicalUserInterface.setColor(color);
    }

    /**
     * Changes the set to be viewed
     * @param set set identifier
     */
    public static void changeSet(final String set){
        // Set the set in model
        model.setSet(set);

        // If x or y is selected, update line graph normally
        if(!set.equals("A")){
            updateLineGraph();
        }else{
            // Update line graph for average value
            updateLineGraphAverage();
        }
    }
}

