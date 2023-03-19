package benchmarking_extension.graph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
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
    XYDataset dataset;
    PlotOrientation orientation;

    public LineGraph(String title, String xAxisLabel, String yAxisLabel, PlotOrientation orientation, int[][] data) {
        this.title = title;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.orientation = orientation;
        this.data = data;
        setupGUI();
    }

    private XYDataset createDataset() {
        var series = new XYSeries("Benchmark");

        for(int[] line : data){
            series.add(line[0], line[1]);
        }


        var dataset = new XYSeriesCollection();
        dataset.addSeries(series);

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

    private JFreeChart createChart(XYDataset dataset) {

        final XYDataset dataset2 = MovingAverage.createMovingAverage(dataset, "- Moving Average", 3 * 24 * 60 * 60 * 1000L, 0L);


        // Renderer for the benchmark
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, false);

        // Renderer for the moving average
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        renderer2.setSeriesPaint(0, Color.RED);
        renderer2.setSeriesStroke(0, new BasicStroke(5.0f));
        renderer2.setSeriesShapesVisible(0, false);


        JFreeChart chart = ChartFactory.createXYLineChart(
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

        plot.setDataset(0, dataset);
        plot.setRenderer(0, renderer);

        plot.setDataset(1, dataset2);
        plot.setRenderer(1, renderer2);

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
