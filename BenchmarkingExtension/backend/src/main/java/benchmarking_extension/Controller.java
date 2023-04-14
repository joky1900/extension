package benchmarking_extension;

import benchmarking_extension.GUI.GraphicalUserInterface;
//import benchmarking_extension.data.JSONParser;
import benchmarking_extension.graph.BarGraph;
import benchmarking_extension.graph.GraphType;
import benchmarking_extension.graph.LineGraph;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.plot.PlotOrientation;

import java.awt.*;
import java.io.File;

/**
 * Controller class working as an intermediate between the GUI and ByteConverter
 *
 * @author John Kyrk
 */
public final class Controller {
    private static Model model;
    private static final String PATH = "src/main/resources/";

    public Controller(Model model){
        this.model = model;
    }


    public static void setFiles(File[] files){
        model.setFiles(files);
    }

    public static void saveToCSV(){
        model.saveToCSV();
    }

    public static void updateGraph(){
        try {
            changeGraphType(GraphType.LINE);
        } catch (Exception e){
            System.out.println("Corrupt files!");
        }
    }

    public static void loadJSON(){
        model.parseFiles();
    }

    public static void saveToImage()  {
        try {
            ChartUtils.saveChartAsPNG(new File(PATH + "line_chart.png"), GraphicalUserInterface.getChart(), 800, 800);
        } catch (Exception e){
            System.out.println("Could not save image...");
        }
    }

    public static int getSubjectTotal(){
        return model.getSubjectTotal();
    }

    public static void changeGraphType(GraphType type) {
        Color color = GraphicalUserInterface.getColor();
        switch(type){
            case BAR:
                System.out.println("Changing to bar graph!");
                GraphicalUserInterface.setGraph(new BarGraph("Average Distance Between Pixels", "", "Distance (pixel)", PlotOrientation.VERTICAL, model.getAverageDistance()));
                break;

            case LINE:
                updateLineGraph();
                break;

            default:
                System.out.println("Something went wrong when changing graph type!");
        }

        System.out.println(GraphicalUserInterface.getColor());
       GraphicalUserInterface.setColor(color);

    }

    private static void updateLineGraph(){
        Color color = GraphicalUserInterface.getColor();
        double[][] data = model.getGazeData();
        double[][] data2 = model.getBallData();
        GraphicalUserInterface.setGraph(new LineGraph("X-Position Over Time", "Time (ms)", "Pixel", PlotOrientation.VERTICAL, data, data2));
        GraphicalUserInterface.setColor(color);
    }

    public static void changeSubjectNumber(int i) {
        Color color = GraphicalUserInterface.getColor();
        model.setSubjectID(i);
        updateLineGraph();
        GraphicalUserInterface.setColor(color);
    }

    public static void changeSet(String set){
        model.setSet(set);

        if(!set.equals("A")){
            updateLineGraph();
        }else{
            updateLineGraphAverage();
        }
    }

    public static void updateLineGraphAverage(){
        Color color = GraphicalUserInterface.getColor();
        GraphicalUserInterface.setGraph(new LineGraph("X-Position Over Time", "Time (ms)", "Pixel", PlotOrientation.VERTICAL, model.getSingleAverageData()));

        GraphicalUserInterface.setColor(color);
    }
}

