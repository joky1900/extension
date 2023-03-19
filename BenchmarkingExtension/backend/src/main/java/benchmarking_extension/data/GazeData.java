package benchmarking_extension.data;

import org.json.simple.JSONObject;

/**
 * Class representing an x and y coordinate value with a timestamp
 */
public class GazeData {
    int x, y;
    String timeStamp;

    public GazeData(JSONObject data){
        this.x = ((Long)data.get("x")).intValue();
        this.y = ((Long)data.get("y")).intValue();
        this.timeStamp = data.get("timeStamp").toString();
    }

    //-----------------------------------------------------------------
    // Accessors
    //-----------------------------------------------------------------
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
