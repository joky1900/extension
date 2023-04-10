package benchmarking_extension.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class FileData {
    private final ArrayList<Data> ballData = new ArrayList<>();
    private final ArrayList<Data> gazeData = new ArrayList<>();

    public FileData(JSONObject ballData, JSONObject gazeData){
        extractBallData(ballData);
        extractGazeData(gazeData);
    }

    private void extractBallData(JSONObject ballData){
        JSONArray data = (JSONArray) ballData.get("ballCoordinates");
        JSONObject ballPosition = (JSONObject) ((JSONObject) data.get(0)).get("ballPosition");
        JSONArray readings = (JSONArray) ballPosition.get("readings");

        for(int i = 0; i < readings.size(); ++i){
            this.ballData.add(new Data((JSONObject) readings.get(i)));
        }
    }

    private void extractGazeData(JSONObject gazeData){
        JSONArray data = (JSONArray) gazeData.get("eyeOnBall");
        JSONArray readings = (JSONArray) ((JSONObject)data.get(0)).get("gazeData");

        for(int i = 0; i < readings.size(); ++i){
            this.gazeData.add(new Data((JSONObject) readings.get(i)));
        }
    }

    /**
    public double[][] getGazeData(){
        double[][] testData = new double[0][0];
        int point = 0;

        for(int i = 0; i < gazeData.size(); ++i){
            double[][] nextData = gazeData.get(i).getData(point);
            point += nextData.length;
            testData = append(testData, nextData);
        }

        return testData;
    }
     **/

    public double[][] getGazeData(){
        // Initialize array
        double[][] data = new double[gazeData.size()][2];

        // Iterate over each coordinate
        for(int i = 0; i < gazeData.size(); ++i){
            int x = gazeData.get(i).getX();
            int y = gazeData.get(i).getY();
            double timeStamp = gazeData.get(i).getTimeStamp();

            // Get delta x over delta y
            //      double xPow = Math.pow(posX - x, 2);
            //      double yPow = Math.pow(posY - y, 2);

            // Calculate distance
            //        int distance = (int) Math.sqrt(xPow + yPow);

            // Populate array
            data[i][0] = timeStamp;
            data[i][1] = (long) x;
            //      data[i][1] = distance;
        }

        return data;
    }

    public double[][] getBallData(){
        // Initialize array
        double[][] data = new double[ballData.size()][2];

        // Iterate over each coordinate
        for(int i = 0; i < ballData.size(); ++i){
            int x = ballData.get(i).getX();
            int y = ballData.get(i).getY();
            double timeStamp = ballData.get(i).getTimeStamp();

            // Get delta x over delta y
            //      double xPow = Math.pow(posX - x, 2);
            //      double yPow = Math.pow(posY - y, 2);

            // Calculate distance
            //        int distance = (int) Math.sqrt(xPow + yPow);

            // Populate array
            data[i][0] = timeStamp;
            data[i][1] = (long) x;
            //      data[i][1] = distance;
        }

        return data;
    }

    /**
     * https://stackoverflow.com/questions/5820905/how-do-you-append-two-2d-array-in-java-properly
     * @param a
     * @param b
     * @return
     */
    private double[][] append(double[][] a, double[][] b){
        double[][] result = new double[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
