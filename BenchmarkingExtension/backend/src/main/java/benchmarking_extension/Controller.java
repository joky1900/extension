package benchmarking_extension;

import benchmarking_extension.GUI.GraphicalUserInterface;
//import benchmarking_extension.data.JSONParser;
import benchmarking_extension.graph.LineGraph;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.json.impl.JSONArray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

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
        System.out.println("Setting files..." + files.length);
        model.setFiles(files);
    }

    public static void saveToCSV(){
        model.saveToCSV();
    }

    public static void updateGraph(){
        double[][] data = model.getXData();
        double[][] data2 = model.getXData2();
        GraphicalUserInterface.setGraph(new LineGraph("Eye tracking accuracy", "Point Number", "Pixel Distance", PlotOrientation.VERTICAL, data, data2));
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
}

