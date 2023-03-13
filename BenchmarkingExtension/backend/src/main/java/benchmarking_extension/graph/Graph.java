package benchmarking_extension.graph;

import java.awt.*;

public abstract class Graph {
    private Color color = new Color(0, 0, 0);
    private GraphType type;
    private int size;

    protected Graph() {

    }


    //-------------------------------------------------------------------------
    // Accessors
    //-------------------------------------------------------------------------
    public Color getColor() {
        return color;
    }

    public GraphType getType() {
        return type;
    }

    public int getSize(){
        return size;
    }

    //-------------------------------------------------------------------------
    // Mutators
    //-------------------------------------------------------------------------
    public void setColor(Color color) {
        this.color = color;
    }

    public void setType(GraphType type){
        this.type = type;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
