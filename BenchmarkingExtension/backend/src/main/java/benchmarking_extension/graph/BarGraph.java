package benchmarking_extension.graph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class BarGraph extends Graph {
    private double[] data;

    public BarGraph(String title, String xAxisLabel, String yAxisLabel, PlotOrientation orientation, double[] data) {
        super(title, xAxisLabel, yAxisLabel, orientation);
        this.data = data;

        setupGUI();
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(int i = 0; i < data.length; ++i){
            dataset.setValue(data[i], "Test Session", String.valueOf(i + 1));
        }

        return dataset;
    }


    @Override
    protected void setupGUI() {
        chart = createChart(createDataset());

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);
    }

    private double getAverage(){
        double total = 0;

        for(double i : data){
            total += i;
        }

        return total / data.length;
    }

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
