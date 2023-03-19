package benchmarking_extension.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Class handling calibration data
 */
public class CalibrationData {
    int posX, posY; // Position of object
    ArrayList<GazeData> gazeData = new ArrayList<>();

    public CalibrationData(JSONObject data){

        // Parse x coordinate
        if(data.get("xPos") instanceof Double){
            this.posX = ((Double)data.get("xPos")).intValue();
        }else{
            this.posX = ((Long)data.get("xPos")).intValue();
        }

        // Parse y coordinate
        if(data.get("yPos") instanceof Double){
            this.posY = ((Double)data.get("yPos")).intValue();
        }else{
            this.posY = ((Long)data.get("yPos")).intValue();
        }

        extractGazeData((JSONArray) data.get("gazeData"));
    }

    /**
     * Extracts all coordinates from gazeJsonData
     * @param gazeJsonData JSONArray containing coordinates
     */
    private void extractGazeData(JSONArray gazeJsonData){
        for (Object gazeJsonDatum : gazeJsonData) {
            gazeData.add(new GazeData((JSONObject) gazeJsonDatum));
        }
    }

    /**
     * Returns a two-dimensional array of type integer where column 1 is distance to object and column 2 is index number
     * @param point Initial index number to start at
     * @return Two dimensional array
     */
    public int[][] getData(int point){
        // Initialize array
        int[][] data = new int[gazeData.size()][2];

        // Iterate over each coordinate
        for(int i = 0; i < gazeData.size(); ++i){
            int x = gazeData.get(i).x;
            int y = gazeData.get(i).y;

            // Get delta x over delta y
            double xPow = Math.pow(posX - x, 2);
            double yPow = Math.pow(posY - y, 2);

            // Calculate distance
            int distance = (int) Math.sqrt(xPow + yPow);

            // Populate array
            data[i][0] = i + point;
            data[i][1] = distance;
        }

        return data;
    }
}
