package se.miun.student.evth400.eyedataanalyzer.data_visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import se.miun.student.evth400.eyedataanalyzer.data.Gaze;
import se.miun.student.evth400.eyedataanalyzer.data.SnippetSession;
import se.miun.student.evth400.eyedataanalyzer.data.SubjectData;
import se.miun.student.evth400.eyedataanalyzer.data.ValidationPoint;
import se.miun.student.evth400.eyedataanalyzer.data.ValidationSession;
import se.miun.student.evth400.eyedataanalyzer.tools.CodeStimuli;
import se.miun.student.evth400.eyedataanalyzer.tools.DataLabb;
import se.miun.student.evth400.eyedataanalyzer.tools.HeatMap;
import se.miun.student.evth400.eyedataanalyzer.tools.Results;
import se.miun.student.evth400.eyedataanalyzer.tools.ScrollablePanel;

public class ResultSummary extends ScrollablePanel {
	private static final long serialVersionUID = 1L;

	private List<SubjectData> subjects;

	public ResultSummary(List<SubjectData> allSubs) {
		this.subjects = allSubs;

		setScrollableWidth(ScrollablePanel.ScrollableSizeHint.FIT);
		setScrollableHeight(ScrollablePanel.ScrollableSizeHint.NONE);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		//Data quality
		SumDisplay dataQual = new SumDisplay("Data quality");
		JScrollPane scroll = new JScrollPane(dataQual);
		add(scroll);

		for(Entry<String, JPanel> pan : Results.getDataQuality(allSubs).entrySet()) {
			dataQual.addPan(pan.getValue(), pan.getKey());
		}


		//Fixation Algorithms
		SumDisplay fixAlgo = new SumDisplay("Fixation extraction algorithms");
		scroll = new JScrollPane(fixAlgo);
		add(scroll);

		for(Entry<String, JPanel> pan : Results.getFixtionProcessing(allSubs).entrySet()) {
			fixAlgo.addPan(pan.getValue(), pan.getKey());
		}

		//Competent programmer distribution
		SumDisplay compDemo = new SumDisplay("Distributions");
		scroll = new JScrollPane(compDemo);
		add(scroll);

		for(Entry<String, JPanel> pan : Results.getDistributions(allSubs).entrySet()) {
			compDemo.addPan(pan.getValue(), pan.getKey());
		}

		//Fixation matches
		SumDisplay fixToLinesNT = new SumDisplay("Reading patterns");
		scroll = new JScrollPane(fixToLinesNT);
		add(scroll);

		for(Entry<String, JPanel> pan : Results.getNTImperative(allSubs).entrySet()) {
			fixToLinesNT.addPan(pan.getValue(), pan.getKey());
		}
		
		SumDisplay fixToLinesIR = new SumDisplay("Reading patterns");
		scroll = new JScrollPane(fixToLinesIR);
		add(scroll);

		for(Entry<String, JPanel> pan : Results.getImperativeReactive(allSubs).entrySet()) {
			fixToLinesIR.addPan(pan.getValue(), pan.getKey());
		}
		
		/*SumDisplay fixToLinesComp = new SumDisplay("Reading patterns");
		scroll = new JScrollPane(fixToLinesComp);
		add(scroll);

		for(Entry<String, JPanel> pan : Results.getComprehension(allSubs).entrySet()) {
			fixToLinesComp.addPan(pan.getValue(), pan.getKey());
		}*/

		//Snippet heat maps
		SumDisplay snipHeat = new SumDisplay("Snippet heat maps");
		scroll = new JScrollPane(snipHeat);
		add(scroll);

		for(Entry<String, JPanel> pan : getSnippetHeatMaps().entrySet()) {
			snipHeat.addPan(pan.getValue(), pan.getKey());
		}

		//Validation heat maps
		SumDisplay valHeat = new SumDisplay("Validation Heat");
		scroll = new JScrollPane(valHeat);
		add(scroll);

		for(Entry<String, JPanel> pan : getValidationHeatMaps().entrySet()) {
			valHeat.addPan(pan.getValue(), pan.getKey());
		}

		repaint();
		revalidate();
	}

	public class SumDisplay extends ScrollablePanel{
		private static final long serialVersionUID = 1L;

		JPanel holder;

		public SumDisplay(String heading) {
			setScrollableWidth(ScrollablePanel.ScrollableSizeHint.NONE);
			setScrollableHeight(ScrollablePanel.ScrollableSizeHint.FIT);

			setLayout(new BorderLayout());			

			JLabel head = new JLabel(heading, JLabel.CENTER);
			add(head, BorderLayout.NORTH);

			holder  = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
			this.add(holder, BorderLayout.CENTER);			
		}

		public void addPan(JPanel pan, String heading){
			JPanel subPan = new JPanel(new BorderLayout());
			subPan.add(new JLabel(heading, JLabel.CENTER), BorderLayout.NORTH);
			subPan.setBackground(Color.LIGHT_GRAY);
			pan.setBackground(Color.WHITE);
			subPan.add(pan, BorderLayout.CENTER);
			pan.setBorder(new EmptyBorder(10, 10, 10, 10));

			this.holder.add(subPan);

			revalidate();
			repaint();
		}
	}

	

	private Map<String, JPanel> getSnippetHeatMaps() {
		Map<String, JPanel> allMaps = new HashMap<String, JPanel>();

		for(Entry<String, CodeStimuli> stim : DataLabb.codeDisplays.entrySet()) {
			JPanel stimPan = new JPanel(new BorderLayout());

			int timesShown  = 0, correct = 0;

			//Generate all point data
			List<Point> points = new ArrayList<Point>();
			List<SubjectData> competentData = DataLabb.competentSubjectsOnly(subjects);
			for(SubjectData sub : competentData) {
				for(SnippetSession snip : sub.getSnippetSessions()) {
					if(snip.getSessionName().equals(stim.getKey())) {
						timesShown = timesShown + 1;
						if(snip.isAnswerCorrect())
							correct = correct + 1;
						//data for snippet
						for(Gaze g : sub.getProcessedData().getfilteredGaze(snip.getSessionName())) {
							points.add(new Point(g.getX().intValue(), g.getY().intValue()));
						}
					}
				}
			}

			BufferedImage input = null;
			try { 
				input = ImageIO.read(new File("CodeImages/" + stim.getKey() + ".png"));
			} catch (IOException ex) {
				System.out.print("failed to load image");
			}

			//Create a heatmap from the data and add it 
			final HeatMap myMap = new HeatMap(points, input);
			myMap.setPreferredSize(new Dimension(1000, 800)); 

			stimPan.add(myMap, BorderLayout.CENTER);

			//intensity lever
			JSlider inten = new JSlider(JSlider.HORIZONTAL, 0, 10, 3);
			inten.addChangeListener(new ChangeListener() {				
				@Override
				public void stateChanged(ChangeEvent e) {
					float i = inten.getValue();
					myMap.changeIntensity(i / 10);
				}
			});
			stimPan.add(inten, BorderLayout.SOUTH);

			//Table
			JPanel stats = new JPanel(new GridLayout(2, 2));
			stats.add(new JLabel("Times shown: "));
			stats.add(new JLabel("Times correct: "));
			stats.add(new JLabel(timesShown + ""));
			stats.add(new JLabel(correct + ""));
			stimPan.add(stats, BorderLayout.NORTH);

			allMaps.put(stim.getKey(), stimPan);
		}


		return allMaps;
	}

	private Map<String, JPanel> getValidationHeatMaps() {
		Map<String, JPanel> allMaps = new HashMap<String, JPanel>();

		//Generate all point data
		List<Point> points = new ArrayList<Point>();
		List<SubjectData> proccesableData = DataLabb.processableSubjectsOnly(subjects);
		for(SubjectData sub : proccesableData) {
			for(ValidationSession val : sub.getValidations()) {
				//data for snippet
				for(Gaze g : sub.getProcessedData().getfilteredGaze(val.getSessionName())) {
					points.add(new Point(g.getX().intValue() + 50, g.getY().intValue() + 50));
				}
			}
		}

		BufferedImage input = null;
		try { 
			input = ImageIO.read(new File("CodeImages/ValidationExtraMargin.png"));
		} catch (IOException ex) {
			System.out.print("failed to load image");
		}

		//Create a heat map from the data and add it 
		JPanel stimPan = new JPanel(new BorderLayout());
		final HeatMap myMap = new HeatMap(points, input);
		myMap.setPreferredSize(new Dimension(1267, 932)); 
		stimPan.add(myMap, BorderLayout.CENTER);

		//intensity lever
		final JSlider inten = new JSlider(JSlider.HORIZONTAL, 0, 10, 3);
		inten.addChangeListener(new ChangeListener() {				
			@Override
			public void stateChanged(ChangeEvent e) {
				float i = inten.getValue();
				myMap.changeIntensity(i / 10);
			}
		});
		stimPan.add(inten, BorderLayout.SOUTH);

		allMaps.put("Filtered Gaze", stimPan);

		//From unfiltered gaze----------------------------------------------------------
		//Generate all point data
		points = new ArrayList<Point>();
		for(SubjectData sub : proccesableData) {
			for(ValidationSession val : sub.getValidations()) {
				//data for snippet
				for(Gaze g : val.getAllGaze()) {
					points.add(new Point(g.getX().intValue() + 50, g.getY().intValue() + 50));
				}
			}
		}

		input = null;
		try { 
			input = ImageIO.read(new File("CodeImages/ValidationExtraMargin.png"));
		} catch (IOException ex) {
			System.out.print("failed to load image");
		}

		//Create a heat map from the data and add it 
		stimPan = new JPanel(new BorderLayout());
		HeatMap fixMap = new HeatMap(points, input);
		fixMap.setPreferredSize(new Dimension(1267, 932));
		stimPan.add(fixMap, BorderLayout.CENTER);

		//intensity lever
		final JSlider in = new JSlider(JSlider.HORIZONTAL, 0, 10, 3);
		in.addChangeListener(new ChangeListener() {				
			@Override
			public void stateChanged(ChangeEvent e) {
				float i = in.getValue();
				fixMap.changeIntensity(i / 10);
			}
		});
		stimPan.add(in, BorderLayout.SOUTH);

		allMaps.put("Unfiltered Gaze", stimPan);

		//From fixations--------------------------------------
		points = new ArrayList<Point>();
		for(SubjectData sub : proccesableData) {
			for(ValidationSession val : sub.getValidations()) {
				for(ValidationPoint pnt: val.getNormalizedValidationPoints()) {
					Gaze avg = DataLabb.averageGazePoint(pnt.getGaze());
					for(Gaze g : pnt.getGaze()) {
						points.add(new Point(avg.getX().intValue() + 50, avg.getY().intValue() + 50));
					}
				}
			}
		}

		input = null;
		try { 
			input = ImageIO.read(new File("CodeImages/ValidationExtraMargin.png"));
		} catch (IOException ex) {
			System.out.print("failed to load image");
		}

		//Create a heat map from the data and add it 
		stimPan = new JPanel(new BorderLayout());
		HeatMap fxMap = new HeatMap(points, input);
		fxMap.setPreferredSize(new Dimension(1267, 932));
		stimPan.add(fxMap, BorderLayout.CENTER);

		//intensity lever
		final JSlider fix = new JSlider(JSlider.HORIZONTAL, 0, 10, 3);
		fix.addChangeListener(new ChangeListener() {				
			@Override
			public void stateChanged(ChangeEvent e) {
				float i = fix.getValue();
				fxMap.changeIntensity(i / 10);
			}
		});
		stimPan.add(fix, BorderLayout.SOUTH);

		allMaps.put("Fixations", stimPan);

		return allMaps;
	}

	
}
