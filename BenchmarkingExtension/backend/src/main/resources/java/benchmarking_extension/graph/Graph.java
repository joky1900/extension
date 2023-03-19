package benchmarking_extension.graph;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;

import javax.swing.*;
import java.awt.*;

public abstract class Graph extends JPanel {
    protected JFreeChart chart;
    private Color color = new Color(0, 0, 0);
    protected int[][] data;

    protected Graph(){
    }


    //-------------------------------------------------------------------------
    // Accessors
    //-------------------------------------------------------------------------
    public Color getColor() {
        return color;
    }


    //-------------------------------------------------------------------------
    // Mutators
    //-------------------------------------------------------------------------
    public void setColor(Color color) {
        chart.getXYPlot().setBackgroundPaint(color);
        this.color = color;
    }

    public void setTextSize(int size){
        Font font = new Font("Arial", Font.BOLD, size);

        NumberAxis rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        NumberAxis domainAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();

        rangeAxis.setTickLabelFont(font);
        domainAxis.setTickLabelFont(font);
    }

    public void setTitle(String title){

    }
}
