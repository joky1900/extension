package se.miun.student.evth400.eyedataanalyzer.data_visualization;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;

import se.miun.student.evth400.eyedataanalyzer.data.Gaze;
import se.miun.student.evth400.eyedataanalyzer.data.ProcessedData;
import se.miun.student.evth400.eyedataanalyzer.data.SnippetSession;
import se.miun.student.evth400.eyedataanalyzer.data.SubjectData;
import se.miun.student.evth400.eyedataanalyzer.data.ValidationPoint;
import se.miun.student.evth400.eyedataanalyzer.data.ValidationSession;
import se.miun.student.evth400.eyedataanalyzer.tools.CodeStimuli;
import se.miun.student.evth400.eyedataanalyzer.tools.DataLabb;
import se.miun.student.evth400.eyedataanalyzer.tools.LineChart;
import se.miun.student.evth400.eyedataanalyzer.tools.ScrollablePanel;

/*
 * Panel class where a subject can be selected and gazedata visualised 
 */
public class DataLabbGUI extends ScrollablePanel {
	private static final long serialVersionUID = 1L;

	private JComboBox<SubjectData> cboSubjectSelection;
	private JPanel subSum;
	private JPanel filterPan;
	private JPanel panValidations;
	private JPanel panSnippets;

	private SubjectData selectedSubject;


	public DataLabbGUI(List<SubjectData> allSubs){
		selectedSubject = allSubs.get(0);

		setLayout(new BorderLayout());
		setScrollableWidth(ScrollablePanel.ScrollableSizeHint.FIT);
		setScrollableHeight(ScrollablePanel.ScrollableSizeHint.NONE);
		setBackground(Color.WHITE);

		JPanel cont = new JPanel();
		cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
		JPanel subSel = new JPanel(new BorderLayout());
		cont.add(subSel);

		//Combobox to select subject
		cboSubjectSelection = new JComboBox<SubjectData>(allSubs.toArray(new SubjectData[allSubs.size()]));
		cboSubjectSelection.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedSubject = (SubjectData) cboSubjectSelection.getSelectedItem();
				updateSubject();
			}
		});

		subSel.add(cboSubjectSelection, BorderLayout.NORTH);
		//cont.add(cboSubjectSelection, new GridBagConstraints(1, 1, 1, 1, 1, 0.05, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		//Subject summary
		subSum = new JPanel(new GridLayout(1,1));
		subSum.add(getSubjectSumPanel(selectedSubject));
		//cont.add(subSum, new GridBagConstraints(1, 2, 1, 1, 1, 0.2, GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
		subSel.add(subSum, BorderLayout.CENTER);

		//Filter panel
		filterPan = new JPanel(new BorderLayout());
		subSel.add(filterPan, BorderLayout.SOUTH);
		filterPan.add(new FilterPanel(selectedSubject.getProcessedData(), new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateSubject();				
			}
		}), BorderLayout.CENTER);
		


		//Create two tabs, snippetData and validation data
		JTabbedPane dataTab = new JTabbedPane(JTabbedPane.TOP);
		panSnippets = new JPanel();
		panSnippets.setLayout(new BoxLayout(panSnippets, BoxLayout.Y_AXIS));
		panValidations = new JPanel();
		panValidations.setLayout(new BoxLayout(panValidations, BoxLayout.Y_AXIS));
		dataTab.add("Validations", panValidations);
		dataTab.add("Snippets", panSnippets);

		//cont.add(dataTab, new GridBagConstraints(1, 3, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5,5),0,0));
		cont.add(dataTab);

		//add(allScroll, BorderLayout.CENTER);
		add(cont, BorderLayout.CENTER);

		//update
		this.updateSubject();
	}

	private void updateSubject() {
		//Summary
		this.subSum.removeAll();
		this.subSum.add(getSubjectSumPanel(selectedSubject));
		
		//Filter panel
		filterPan.removeAll();
		filterPan.add(new FilterPanel(selectedSubject.getProcessedData(), new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateSubject();				
			}
		}), BorderLayout.CENTER);

		//Validations
		this.panValidations.removeAll();
		for(ValidationSession val: selectedSubject.getValidations()) {
			ValidationLabb lab = new ValidationLabb(val, selectedSubject.getProcessedData());
			lab.setMinimumSize(lab.getPreferredSize());
			lab.setMaximumSize(lab.getPreferredSize());
			panValidations.add(lab);
		}

		//snippets
		this.panSnippets.removeAll();
		for(SnippetSession snip : selectedSubject.getSnippetSessions()) {
			SnippetLabb lab = new SnippetLabb(snip, selectedSubject);
			lab.setMinimumSize(lab.getPreferredSize());
			lab.setMaximumSize(lab.getPreferredSize());
			panSnippets.add(lab);
		}

		this.revalidate();
		this.repaint();
	}

	private JPanel getSubjectSumPanel(SubjectData sub) {
		JPanel p = new JPanel(new GridLayout(0, 6));

		for(Entry<String, String> ent : sub.getSummary().entrySet()) {
			JLabel lbl = new JLabel(ent.getKey());
			lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
			p.add(lbl);

			JLabel val = new JLabel(ent.getValue());
			val.setFont(new Font("SansSerif", Font.PLAIN, 12));
			p.add(val);
		}

		return p;
	}

	/*
	 * Class for visualizing validation
	 */
	private class ValidationLabb extends JPanel{
		private static final long serialVersionUID = 1L;

		private ValidationSession session;

		private JPanel gazeChart;
		private JPanel cont;

		public ValidationLabb(ValidationSession val, ProcessedData data) {
			session = val;

			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			//Summary
			JPanel sum = ResultDisplay.getDataPairTable(val.getSummary());
			add(sum);

			//Visual Scroll
			cont = new ScrollablePanel();
			cont.setLayout(new FlowLayout());
			cont.setBorder(new EmptyBorder(0, 0, 10, 0));
			JScrollPane scroll = new JScrollPane(cont, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
			add(scroll);

			//Gaze chart
			this.gazeChart = getGazeChart(data.getfilteredGaze(val.getSessionName()));
			this.gazeChart.setPreferredSize(new Dimension(session.normWidth, ValidationSession.normHeight));
			cont.add(this.gazeChart);

			//point display
			ValidationImgPanel img = new ValidationImgPanel(session, data.getfilteredGaze(session.getSessionName()));
			cont.add(img);

		}

		private JPanel getGazeChart(List<Gaze> gazeData) {
			Map<Long, Double> gazeY = new HashMap<Long, Double>();
			Map<Long, Double> gazeX = new HashMap<Long, Double>();
			Map<Long, Double> pointY = new HashMap<Long, Double>();
			Map<Long, Double> pointX = new HashMap<Long, Double>();

			long sampleNo = 1;
			for(ValidationPoint pnt : session.getAllValidationPoints()) {
				Long samples = (long) pnt.getGaze().size();
				pointY.put(sampleNo, pnt.getPointY());
				pointY.put(sampleNo + samples -1, pnt.getPointY());
				pointY.put(sampleNo + samples + 1, null);
				pointX.put(sampleNo, pnt.getPointX());
				pointX.put(sampleNo + samples -1, pnt.getPointX());
				pointX.put(sampleNo + samples +1, null);

				Long startStamp = pnt.getGaze().get(0).getTimeStamp();
				Long endStamp = pnt.getGaze().get((int) (samples - 1)).getTimeStamp();

				List<Gaze> pointGaze = new ArrayList<Gaze>(gazeData);
				pointGaze.removeIf(g -> g.getTimeStamp() < startStamp);
				pointGaze.removeIf(g -> g.getTimeStamp() > endStamp);

				for(int idx = 0; idx < pointGaze.size(); idx++) {
					gazeY.put(sampleNo + idx, pointGaze.get(idx).getY());
					gazeX.put(sampleNo + idx, pointGaze.get(idx).getX());
				}

				gazeY.put(sampleNo + samples +1, null);
				gazeX.put(sampleNo + samples +1, null);

				sampleNo = sampleNo + samples + 2;
			}

			List<XYSeries> series = new ArrayList<XYSeries>();
			series.add(LineChart.generateSeries(gazeX, "x-gaze"));
			series.add(LineChart.generateSeries(gazeY, "y-gaze"));
			series.add(LineChart.generateSeries(pointX, "Validation x"));
			series.add(LineChart.generateSeries(pointY, "validation y"));

			return new LineChart(series, session.getSessionName(), "sample no", "px");
		}


	}

	public class SnippetLabb extends JPanel {
		private static final long serialVersionUID = 1L;

		JPanel cont;
		SideControlPanel side;
		SnippetImgPanel snipImg;
		JPanel gazeChart;

		SnippetSession session;

		public SnippetLabb(SnippetSession snip, SubjectData sub) {
			session = snip;

			setLayout(new BorderLayout());

			//Summary
			JPanel sum = ResultDisplay.getDataPairTable(snip.getSummary());
			add(sum, BorderLayout.NORTH);

			//Visual
			cont = new JPanel();
			cont.setBorder(new EmptyBorder(0, 0, 10, 0));
			JScrollPane scroll = new JScrollPane(cont, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			add(scroll, BorderLayout.CENTER);

			side = new SideControlPanel(sub);
			snipImg = new SnippetImgPanel(new CodeStimuli(snip.getSessionName()), sub, side);
			snipImg.setPreferredSize(new Dimension(1000, 800));
			cont.add(snipImg);
			cont.add(side);

			//Line charts
			JPanel charts= new JPanel(new GridLayout(2, 1));
			cont.add(charts);
			
			List<Gaze> inRange = DataLabb.removeSnippetOutOfBounds(sub.getProcessedData().getfilteredGaze(snip.getSessionName()));
			Map<Long,Double> xGaze = new HashMap<Long, Double>();
			Map<Long,Double> yGaze = new HashMap<Long, Double>();
			
			for(Gaze g: inRange) {
				xGaze.put(g.getTimeStamp(), g.getX());
				yGaze.put(g.getTimeStamp(), g.getY());
			}
			
			charts.add(this.getGazeChart(xGaze, "x-coordinates"));
			charts.add(this.getGazeChart(yGaze, "y-coordinates"));
			
			//invert the y-axle
			((ChartPanel) charts.getComponent(1)).getChart().getXYPlot().getRangeAxis().setInverted(true);
		}

		private JPanel getGazeChart(Map<Long, Double> gazeData, String dataName) {
			List<XYSeries> series = new ArrayList<XYSeries>();
			series.add(LineChart.generateSeries(gazeData, dataName));

			LineChart chart = new LineChart(series, dataName, "timestamp", "px");

			NumberAxis xAxis = new NumberAxis();
			xAxis.setTickUnit(new NumberTickUnit(1000));
			XYPlot plot = chart.getChart().getXYPlot();
			plot.setDomainAxis(xAxis);
						
			chart.setPreferredSize(new Dimension((int)((session.getResponseTime().intValue()) / 20 - chart.getChart().getPadding().getLeft() * 2), 400));
			
			XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
			renderer.setDefaultStroke(new BasicStroke(3f));
			renderer.setAutoPopulateSeriesStroke(false);
			
			plot.setRenderer(renderer);
			
			return chart;
		}

	}
}
