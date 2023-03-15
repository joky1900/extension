package benchmarking_extension;

//import benchmarking_extension.data.JSONParser;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
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
        return data.get(0).getData();
    }

    public int[][] getData(){
        // Clear data
        data.clear();

        int[][] data = new int[filesJSON.size()][2];

        for(int i = 0; i < filesJSON.size(); ++i){
            Set<String> keys = filesJSON.get(i).keySet();

     //       filesJSON.get(i).get
            JSONArray calibrations = (JSONArray) filesJSON.get(i).get("calibrations");
            JSONArray readings = (JSONArray) ((JSONObject)calibrations.get(0)).get("readings");
          //  System.out.println(((JSONObject)readings.get(0)).get("xPos"));
            addCalibrationData((JSONObject)readings.get(0));
         //   JSONArray readings = (JSONArray) ((JSONObject) calibrations.get(0)).get("readings");
            Controller.updateGraph();

      //      filesJSON.get(i).getJSONObject
           // data[i][0] = ;
          //  data[i][1] = ;
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

      //  System.out.println(filesJSON);
    }
}
