package benchmarking_extension.data;

import org.json.simple.JSONObject;

public class Data {
    private final int x;
    private final int y;
    private final Double timeStamp;

    Data(JSONObject data){
        this.x = ((Long)data.get("x")).intValue();
        this.y = ((Long)data.get("y")).intValue();


        // Parse timestamp
        if(data.get("timeStamp") instanceof Double){
            this.timeStamp= (Double)data.get("timeStamp");
        }else{
            this.timeStamp = ((Long)data.get("timeStamp")).doubleValue();
        }
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

    public Double getTimeStamp(){
        return timeStamp;
    }


}
