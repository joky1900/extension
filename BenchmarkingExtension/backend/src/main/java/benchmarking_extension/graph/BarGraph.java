package benchmarking_extension.graph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

/**
 * Concrete class implementing a bar graph
 *
 * @author John Kyrk
 * @version 1.0
 * @since 2023-04-23
 */
public class BarGraph extends Graph {
    private final double[] data;

    /**
     * Constructor
     * @param title Title of the graph
     * @param xAxisLabel label
     * @param yAxisLabel label
     * @param orientation vertical or horizontal
     * @param data array of doubles
     */
    public BarGraph(String title, String xAxisLabel, String yAxisLabel, PlotOrientation orientation, double[] data) {
        super(title, xAxisLabel, yAxisLabel, orientation);
        this.data = data;

        setupGUI();
    }

    /**
     * Creates the dataset used by JFreeChart
     * @return CategoryDataset
     */
    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Iterate over each value
        for(int i = 0; i < data.length; ++i){
            dataset.setValue(data[i], "Test Session", String.valueOf(i + 1));
        }

        return dataset;
    }


    /**
     * Sets up the graphical elements
     */
    @Override
    protected void setupGUI() {
        chart = createChart(createDataset());

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);
    }

    /**
     * Calculates the average value of the data array
     * @return Double indicating the average value
     */
    private double getAverage(){
        double total = 0;

        for(double i : data){
            total += i;
        }

        return total / data.length;
    }

    /**
     * Creates the {@link JFreeChart} object
     * @param dataset data to be used in the chart
     * @return {@link JFreeChart}
     */
    private JFreeChart createChart(final CategoryDataset dataset) {

        chart = ChartFactory.createBarChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ValueMarker average = new ValueMarker(getAverage());
        average.setPaint(Color.BLUE);
        average.setStroke(new BasicStroke(3.0f));


        chart.getCategoryPlot().addRangeMarker(average);

        return chart;
    }
}
