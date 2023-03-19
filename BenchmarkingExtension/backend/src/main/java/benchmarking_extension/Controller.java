package benchmarking_extension;

import benchmarking_extension.GUI.GraphicalUserInterface;
//import benchmarking_extension.data.JSONParser;
import benchmarking_extension.graph.LineGraph;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.json.impl.JSONArray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Controller class working as an intermediate between the GUI and ByteConverter
 *
 * @author John Kyrk
 */
public final class Controller {
    private static Model model;
    ArrayList<JSONArray> files = new ArrayList<>();
   // JSONParser parser = new JSONParser();
    public Controller(Model model){
        this.model = model;
    }

    private void loadFile(File[] files){
        for(File file : files){
        //    this.files.add(())
        }
    }

    public static void setFiles(File[] files){
        System.out.println("Setting files..." + files.length);
        model.setFiles(files);
    }

    public static void saveToCSV(){
        model.saveToCSV();
    }

    public static void updateGraph(){
        int[][] data = model.getXYData();
        GraphicalUserInterface.setGraph(new LineGraph("Eye tracking accuracy", "Point Number", "Pixel Distance", PlotOrientation.VERTICAL, data));
    }

    public static void loadJSON(){
        model.parseFiles();
        model.getData();
    }
}

