package benchmarking_extension;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import benchmarking_extension.data.CalibrationData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


public class Model {
    private File[] files;
    private final String PATH = "src/main/resources/";
    private ArrayList<JSONObject> filesJSON = new ArrayList<>();
    private JSONParser parser = new JSONParser();
    private ArrayList<CalibrationData> data = new ArrayList<>();

    /**
     * Public mutator setting the array of file objects.
     * @param files array of files
     */
    public void setFiles(File[] files) {
        this.files = files;
    }

    public String getFileExtension(String fileName){
        String[] splitFileName = fileName.split("\\.");

        if(splitFileName.length == 1){
            return fileName;
        }else{
            return splitFileName[splitFileName.length - 1];
        }
    }

    public int[][] getXYData(){
        int[][] testData = new int[0][0];
        int point = 0;

        for(int i = 0; i < data.size(); ++i){
            int[][] nextData = data.get(i).getData(point);
            point += nextData.length;
            testData = append(testData, nextData);
        }

        return testData;
    }

    /**
     * https://stackoverflow.com/questions/5820905/how-do-you-append-two-2d-array-in-java-properly
     * @param a
     * @param b
     * @return
     */
    private int[][] append(int[][] a, int[][] b){
        int[][] result = new int[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public int[][] getData(){
        // Clear data
        data.clear();

        int[][] data = new int[filesJSON.size()][2];

        for(int i = 0; i < filesJSON.size(); ++i){
            Set<String> keys = filesJSON.get(i).keySet();

            JSONArray calibrations = (JSONArray) filesJSON.get(i).get("calibrations");
            JSONArray readings = (JSONArray) ((JSONObject)calibrations.get(0)).get("readings");

            for(int j = 0; j < readings.size(); ++j){
                addCalibrationData((JSONObject)readings.get(j));
            }

            Controller.updateGraph();
        }

        return null;
    }

    private void addCalibrationData(JSONObject calibrationJSON){
        data.add(new CalibrationData(calibrationJSON));
    }

    public void parseFiles() {
        // Reset files
        filesJSON = new ArrayList<>();

        for(File file : files){
            try {
                JSONObject object = (JSONObject) parser.parse(new FileReader(file));
                this.filesJSON.add(object);
            }catch(Exception e){
                System.out.println("Could not load: " + e);
            }
        }
    }
}
