package benchmarking_extension.graph;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract class for representing a graph
 */
public abstract class Graph extends JPanel {
    protected JFreeChart chart;
    private Color color = new Color(255, 255, 255);
    protected String title, xAxisLabel, yAxisLabel;
    protected PlotOrientation orientation;

    /**
     * Constructor
     * @param title title of the graph
     * @param xAxisLabel label
     * @param yAxisLabel label
     * @param orientation horizontal or vertical
     */
    protected Graph(String title, String xAxisLabel, String yAxisLabel, PlotOrientation orientation){
        this.title = title;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.orientation = orientation;
    }


    //-------------------------------------------------------------------------
    // Accessors
    //-------------------------------------------------------------------------
    public Color getBackgroundColor() {
        return color;
    }


    //-------------------------------------------------------------------------
    // Mutators
    //-------------------------------------------------------------------------
    public void setColor(Color color) {
       // chart.setBackgroundPaint(color);
        chart.getPlot().setBackgroundPaint(color);
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

    protected abstract void setupGUI();

    public JFreeChart getChart() {
        return chart;
    }
}
