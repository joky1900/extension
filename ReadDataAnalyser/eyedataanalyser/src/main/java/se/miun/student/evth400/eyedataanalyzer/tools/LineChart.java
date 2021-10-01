package se.miun.student.evth400.eyedataanalyzer.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LineChart extends ChartPanel{
	private static final long serialVersionUID = 1L;
	private static Paint[] plotColors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN, Color.PINK, Color.ORANGE, Color.DARK_GRAY};

	public LineChart(List<XYSeries> series, String heading, String xLabel, String yLabel) {
		super(createChart(createDataSet(series), heading, xLabel, yLabel));

        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.setBackground(Color.white);
    }
	
	private static XYDataset createDataSet(List<XYSeries> series) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		for(XYSeries s : series) {
			dataset.addSeries(s);
		}
		return dataset;
	}


    private static JFreeChart createChart(XYDataset dataset, String heading, String xLabel, String yLabel) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                heading,
                xLabel,
                yLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
       
        //set different colors to series
        for(int i = 0; i < dataset.getSeriesCount(); i++) {
        	renderer.setSeriesPaint(i, plotColors[i]);
            renderer.setSeriesStroke(i, new BasicStroke(2.0f));
        }       

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle(heading,
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );
        
        return chart;
    }
    
    public static XYSeries generateSeries(Map<?,Double> values, String serieName) {
    	XYSeries series = new XYSeries(serieName);

    	for(Entry<?,Double> ent: values.entrySet()) {
    		if(ent.getKey() instanceof Long){
    			series.add(((Long) ent.getKey()).doubleValue(), ent.getValue());
    		}else {    		
    			series.add((double) ent.getKey(), ent.getValue());
    		}
    	}

    	return series;
    }

	
}
