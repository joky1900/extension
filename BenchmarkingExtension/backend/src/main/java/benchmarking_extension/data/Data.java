package benchmarking_extension.data;

import org.json.simple.JSONObject;

/**
 * Class for holding individual polling data
 *
 * @author John Kyrk
 * @version 1.0
 * @since 2023-04-20
 */
public class Data {
    private final int x;
    private final int y;
    private final Double timeStamp;

    Data(JSONObject data){
        // Parse coordinate values from json
        this.x = ((Long)data.get("x")).intValue();
        this.y = ((Long)data.get("y")).intValue();


        // Parse timestamp depending on data type
        if(data.get("timeStamp") instanceof Double){
            this.timeStamp= (Double)data.get("timeStamp");
        }else{
            this.timeStamp = ((Long)data.get("timeStamp")).doubleValue();
        }
    }

    //-----------------------------------------------------------------
    // Public Accessors
    //-----------------------------------------------------------------
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Double getTimeStamp(){
        return timeStamp;
    }
}
