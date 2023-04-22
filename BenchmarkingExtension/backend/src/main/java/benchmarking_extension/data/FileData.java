package benchmarking_extension.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Class for holding test session data from ballTracing.json and ballCoordinates.json
 *
 * @author John Kyrk
 * @version 1.0
 * @since 2023-04-20
 */
public class FileData {
    private final ArrayList<Data> ballData = new ArrayList<>();
    private final ArrayList<Data> gazeData = new ArrayList<>();

    /**
     * Constructor
     * @param ballData data from ballCoordinates.json
     * @param gazeData data from ballTracing.json
     */
    public FileData(final JSONObject ballData, final JSONObject gazeData){
        extractBallData(ballData);
        extractGazeData(gazeData);
    }

    /**
     * Helper method converting json data to an ArrayList of Data
     * @param ballData data from ballCoordinates.json
     */
    private void extractBallData(final JSONObject ballData){
        JSONArray data = (JSONArray) ballData.get("ballCoordinates");
        JSONObject ballPosition = (JSONObject) ((JSONObject) data.get(0)).get("ballPosition");
        JSONArray readings = (JSONArray) ballPosition.get("readings");

        for (Object reading : readings) {
            this.ballData.add(new Data((JSONObject) reading));
        }
    }

    /**
     * Helper method converting json data to an ArrayList of Data
     * @param gazeData data from ballTracing.json
     */
    private void extractGazeData(final JSONObject gazeData){
        JSONArray data = (JSONArray) gazeData.get("eyeOnBall");
        JSONArray readings = (JSONArray) ((JSONObject)data.get(0)).get("gazeData");

        for (Object reading : readings) {
            this.gazeData.add(new Data((JSONObject) reading));
        }
    }

    /**
     * Method for appending a two-dimensional array to another
     * @see "https://stackoverflow.com/questions/5820905/how-do-you-append-two-2d-array-in-java-properly"
     * @param a two-dimensional array
     * @param b two-dimensional array
     * @return appended two-dimensional array
     */
    private double[][] append(final double[][] a, final double[][] b){
        double[][] result = new double[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    //-----------------------------------------------------------------------------
    // Public Accessors
    //-----------------------------------------------------------------------------
    public ArrayList<Data> getGazeData(){
        return gazeData;
    }

    public ArrayList<Data> getBallData(){
        return ballData;
    }

    public String toString(){
        return ballData + "\n" + gazeData;
    }
}
