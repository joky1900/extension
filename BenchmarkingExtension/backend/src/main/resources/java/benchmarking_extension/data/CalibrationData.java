package benchmarking_extension.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CalibrationData {
    int posX, posY;
    ArrayList<GazeData> gazeData = new ArrayList<>();

    public CalibrationData(JSONObject data){
        if(data.get("xPos") instanceof Double){
            this.posX = ((Double)data.get("xPos")).intValue();
        }else{
            this.posX = ((Long)data.get("xPos")).intValue();
        }

        if(data.get("yPos") instanceof Double){
            this.posY = ((Double)data.get("yPos")).intValue();
        }else{
            this.posY = ((Long)data.get("yPos")).intValue();
        }

        extractGazeData((JSONArray) data.get("gazeData"));
    }

    private void extractGazeData(JSONArray gazeJsonData){
        for(int i = 0; i < gazeJsonData.size(); ++i){
            gazeData.add(new GazeData((JSONObject) gazeJsonData.get(i)));
        }
    }

    public int[][] getData(){
        int[][] data = new int[gazeData.size()][2];
        System.out.println(gazeData.size());
        for(int i = 0; i < gazeData.size(); ++i){
            int x = gazeData.get(i).x;
            int y = gazeData.get(i).y;

            System.out.println("posX " + posX + " | " + "posY " + posY + " | " + x + "," + y);
            double xPow = Math.pow(posX - x, 2);
            double yPow = Math.pow(posY - y, 2);

            int distance = (int) Math.sqrt(xPow + yPow);

            System.out.println("Distance: " + distance);

            data[i][0] = i;
            data[i][1] = distance;
        }

        return data;
    }
}
