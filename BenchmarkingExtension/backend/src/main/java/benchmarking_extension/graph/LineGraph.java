package benchmarking_extension.graph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class LineGraph extends Graph{
    String title, xAxisLabel, yAxisLabel;
    protected double[][] data;
    protected double[][] data2;
    private boolean average = false;

    public LineGraph(String title, String xAxisLabel, String yAxisLabel, PlotOrientation orientation, double[][] data, double[][] data2) {
        super(title, xAxisLabel, yAxisLabel, orientation);
        this.data = data;
        this.data2 = data2;
        setupGUI();
    }

    public LineGraph(String title, String xAxisLabel, String yAxisLabel, PlotOrientation orientation, double[][] data) {
        super(title, xAxisLabel, yAxisLabel, orientation);
        this.data = data;
      //  this.data2 = data2;
        average = true;
        setupGUI();

    }

    private XYDataset createDataset() {
        var series = new XYSeries("Benchmark");
        var dataset = new XYSeriesCollection();
        System.out.println("DATA: " + data.length);

        for(double[] line : data){
            series.add(line[0], line[1]);
        }

        dataset.addSeries(series);

        if(!average){
            var series2 = new XYSeries("Ball");
            for(double[] line: data2){
                series2.add(line[0], line[1]);
            }
            dataset.addSeries(series2);
        }

        return dataset;
    }

    protected void setupGUI(){
        XYDataset dataset = createDataset();
        chart = createChart(dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);
    }

    private JFreeChart createChart(final XYDataset dataset) {

       // final XYDataset dataset2 = MovingAverage.createMovingAverage(dataset, "- Moving Average", 3 * 24 * 60 * 60 * 1000L, 0L);

        XYLineAndShapeRenderer renderer = new XYSplineRenderer();

        // Renderer for the benchmark
        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, false);

        // Renderer for the moving average
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        renderer2.setSeriesPaint(0, Color.GREEN);
        renderer2.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer2.setSeriesShapesVisible(0, false);

        // Renderer for the moving average
        renderer2.setSeriesPaint(1, Color.RED);
        renderer2.setSeriesStroke(1, new BasicStroke(2.0f));
        renderer2.setSeriesShapesVisible(1, false);

        // Renderer for the ball

        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesStroke(1, new BasicStroke(4.0f));
        renderer.setSeriesShapesVisible(1, false);


        chart = ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );


        XYPlot plot = chart.getXYPlot();

        plot.setRenderer(renderer);

        //plot.setDataset(1, dataset2);
        plot.setRenderer(0, renderer);
     //   plot.setRenderer(1, renderer2);



        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle("Distance between object and interpreted gaze in pixels",
                        new Font("Serif", java.awt.Font.BOLD, 18)));

        return chart;
    }
}
