package se.miun.student.evth400.eyedataanalyzer.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;

import se.miun.student.evth400.eyedataanalyzer.data.Fixation;
import se.miun.student.evth400.eyedataanalyzer.data.SnippetSession;
import se.miun.student.evth400.eyedataanalyzer.data.SubjectData;
import se.miun.student.evth400.eyedataanalyzer.data.ValidationPoint;
import se.miun.student.evth400.eyedataanalyzer.data.ValidationSession;
/*
 * Calculates and displays data processing results with swing components
 * 
 */
public class Results {
	/*
	 * Calculates gaze and fixation accuracy in pixels and relative
	 */
	public static LinkedHashMap<String, JPanel> getDataQuality(List<SubjectData> subjects) {
		LinkedHashMap<String, JPanel> panels = new LinkedHashMap<String, JPanel>();
		
		final Map<Long, Map<String, List<Double>>> dataQuality = new HashMap<Long, Map<String,List<Double>>>();
		
		JPanel pan = new JPanel(new GridLayout(0,  3, 10, 10));

		pan.add(new JLabel());
		pan.add(new JLabel("Mean"));
		pan.add(new JLabel("SD"));		

		List<SubjectData> parttakeSubs = DataLabb.startedSubjectsOnly(subjects);
		for(SubjectData sub: parttakeSubs) {
			Map<String, List<Double>> map = new HashMap<String, List<Double>>();
			map.put("fixErr", new ArrayList<Double>());
			map.put("gzeErr", new ArrayList<Double>());
			map.put("prc", new ArrayList<Double>());
			
			dataQuality.put(sub.getSampleFrequenzy().longValue(), map);
		}
		
		final List<Double> data = new ArrayList<Double>();

		pan.add(new JLabel("Fixation error (px)"));				
		parttakeSubs.forEach(sub -> data.add(sub.getAverageFixationError()));
		Map<String, Double >dataPxF = DataLabb.getDoubleMeanAndSD(data);
		pan.add(new JLabel(Util.round(dataPxF.get("Mean"), 2) + ""));
		pan.add(new JLabel(Util.round(dataPxF.get("SD"), 2) + ""));

		pan.add(new JLabel("Fixtion error (◦)"));
		data.removeAll(data);
		parttakeSubs.forEach(sub -> data.add(sub.getAverageFixationErrorDegrees()));
		Map<String, Double>dataDegF = DataLabb.getDoubleMeanAndSD(data);
		pan.add(new JLabel(Util.round(dataDegF.get("Mean"), 2) + ""));
		pan.add(new JLabel(Util.round(dataDegF.get("SD"), 2) + ""));

		pan.add(new JLabel("Relative fixation error"));
		data.removeAll(data);
		parttakeSubs.forEach(sub -> data.add(sub.getRelativeFixationError()));
		parttakeSubs.forEach(sub -> dataQuality.get(sub.getSampleFrequenzy().longValue()).get("fixErr").add(sub.getRelativeFixationError()));
		Map<String, Double > dataRel = DataLabb.getDoubleMeanAndSD(data);
		pan.add(new JLabel(Util.round(dataRel.get("Mean"), 2) + ""));
		pan.add(new JLabel(Util.round(dataRel.get("SD"), 2) + ""));

		pan.add(new JLabel("Gaze error (px)"));
		data.removeAll(data);
		parttakeSubs.forEach(sub -> data.add(sub.getAverageGazeError()));
		Map<String, Double > dataPxG = DataLabb.getDoubleMeanAndSD(data);
		pan.add(new JLabel(Util.round(dataPxG.get("Mean"), 2) + ""));
		pan.add(new JLabel(Util.round(dataPxG.get("SD"), 2) + ""));

		pan.add(new JLabel("Gaze error (◦)"));
		data.removeAll(data);
		parttakeSubs.forEach(sub -> data.add(sub.getAverageGazeErrorDegrees()));
		Map<String, Double>dataDegG = DataLabb.getDoubleMeanAndSD(data);
		pan.add(new JLabel(Util.round(dataDegG.get("Mean"), 2) + ""));
		pan.add(new JLabel(Util.round(dataDegG.get("SD"), 2) + ""));

		pan.add(new JLabel("Relative gaze Error"));
		data.removeAll(data);
		parttakeSubs.forEach(sub -> data.add(sub.getRelativeGazeError()));
		parttakeSubs.forEach(sub -> dataQuality.get(sub.getSampleFrequenzy().longValue()).get("gzeErr").add(sub.getRelativeGazeError()));
		Map<String, Double> dataGzeRel = DataLabb.getDoubleMeanAndSD(data);
		pan.add(new JLabel(Util.round(dataGzeRel.get("Mean"), 2) + ""));
		pan.add(new JLabel(Util.round(dataGzeRel.get("SD"), 2) + ""));

		panels.put("Accuracy", pan);

		/*
		 * Calculates sample frequency
		 */
		pan = new JPanel(new GridLayout(0,  4, 10, 10));

		pan.add(new JLabel());
		pan.add(new JLabel("No."));
		pan.add(new JLabel("Mean"));
		pan.add(new JLabel("SD"));		

		data.removeAll(data);

		pan.add(new JLabel("All systems"));
		parttakeSubs.forEach(sub -> data.add(sub.getSampleFrequenzy().doubleValue()));
		Map<String, Double> dataAll = DataLabb.getDoubleMeanAndSD(data);
		pan.add(new JLabel(data.size() + ""));
		pan.add(new JLabel(Util.round(dataAll.get("Mean"), 2) + ""));
		pan.add(new JLabel(Util.round(dataAll.get("SD"), 2) + ""));

		//by core count
		LinkedHashMap<String, List<Double>> byCoreCount = new LinkedHashMap<String, List<Double>>();
		for(SubjectData sub : parttakeSubs) {
			long core = sub.getNumberOfCores();
			String coreKey = "";

			if(core <= 4) {
				coreKey = "2-4";
			}else if (core > 4 && core <= 8) {
				coreKey = "6-8";
			}else if (core > 8 && core <= 12) {
				coreKey = "10-12";
			}else {
				coreKey = "14+";
			}

			if(!byCoreCount.containsKey(coreKey)) {
				byCoreCount.put(coreKey, new ArrayList<Double>());
			}
			byCoreCount.get(coreKey).add(sub.getSampleFrequenzy().doubleValue());
		}

		for(Entry<String, List<Double>> ent : byCoreCount.entrySet()) {
			pan.add(new JLabel(ent.getKey() + " cores"));
			Map<String, Double> dataFq = DataLabb.getDoubleMeanAndSD(ent.getValue());
			pan.add(new JLabel(ent.getValue().size() + ""));
			pan.add(new JLabel(Util.round(dataFq.get("Mean"), 2) + ""));
			pan.add(new JLabel(Util.round(dataFq.get("SD"), 2) + ""));
		}


		panels.put("Sample frequency",pan);

		/*
		 * Precision
		 */
		List<Double> pxPrec = new ArrayList<Double>();
		List<Double> relPrec = new ArrayList<Double>();
		List<Double> degPrec = new ArrayList<Double>();

		for(SubjectData sub : parttakeSubs) {
			List<Double> pxSub = new ArrayList<Double>();
			List<Double> relSub = new ArrayList<Double>();
			List<Double> degSub = new ArrayList<Double>();

			for(ValidationSession ses: sub.getValidations()) {
				for(ValidationPoint pnt : ses.getAllValidationPoints()) {
					Double rmsPx = 0D;
					Double rmsRel = 0D;
					Double rmsDeg = 0D;

					for(int i = 1 ; i < pnt.getGaze().size(); i++) {
						rmsPx = rmsPx + Math.pow(pnt.getGaze().get(i).getX() - pnt.getGaze().get(i-1).getX(), 2)
						+ Math.pow(pnt.getGaze().get(i).getY() - pnt.getGaze().get(i-1).getY(), 2);

						rmsRel = rmsRel + Math.pow(pnt.getGaze().get(i).getX()/ses.getDisplayWidth()
								- pnt.getGaze().get(i-1).getX()/ ses.getDisplayWidth(), 2)
						+ Math.pow(pnt.getGaze().get(i).getY()/ses.getDisplayHeight()
								- pnt.getGaze().get(i-1).getY()/ ses.getDisplayHeight(), 2);

						//in degrees (50cm from screen)
						double xDeg = Math.abs(pnt.getGaze().get(i).getX() - pnt.getGaze().get(i-1).getX()); 
						xDeg = Math.toDegrees(Math.atan(xDeg / 1890));
						double yDeg = Math.abs(pnt.getGaze().get(i).getY() - pnt.getGaze().get(i-1).getY());
						yDeg = Math.toDegrees(Math.atan(yDeg / 1890));

						rmsDeg = rmsDeg + Math.pow(xDeg, 2) + Math.pow(yDeg, 2);						
					}

					rmsPx = Math.sqrt(rmsPx / pnt.getGaze().size() - 1);
					rmsRel = Math.sqrt(rmsRel / (double)(pnt.getGaze().size() - 1)); 
					rmsDeg = Math.sqrt(rmsDeg / (double)(pnt.getGaze().size() - 1));
					pxSub.add(rmsPx);
					relSub.add(rmsRel);
					degSub.add(rmsDeg);
					
					dataQuality.get(sub.getSampleFrequenzy().longValue()).get("prc").add(rmsRel);
				}
			}

			//Add subject mean
			pxPrec.add(DataLabb.getDoubleMeanAndSD(pxSub).get("Mean"));
			relPrec.add(DataLabb.getDoubleMeanAndSD(relSub).get("Mean"));
			degPrec.add(DataLabb.getDoubleMeanAndSD(degSub).get("Mean"));
		}

		//Overall mean
		Map<String, Double> px = DataLabb.getDoubleMeanAndSD(pxPrec);
		Map<String, Double> rel = DataLabb.getDoubleMeanAndSD(relPrec);
		Map<String, Double> deg = DataLabb.getDoubleMeanAndSD(degPrec);

		pan = new JPanel(new GridLayout(4, 3,10 ,10));

		pan.add(new JLabel());
		pan.add(new JLabel("Mean"));
		pan.add(new JLabel("SD"));

		pan.add(new JLabel("Precision (px)"));
		pan.add(new JLabel(Util.round(px.get("Mean"),2) + ""));
		pan.add(new JLabel(Util.round(px.get("SD"),2) + ""));

		pan.add(new JLabel("Precision (◦)"));
		pan.add(new JLabel(Util.round(deg.get("Mean"), 2) + ""));
		pan.add(new JLabel(Util.round(deg.get("SD"), 2) + ""));

		pan.add(new JLabel("Relative precision"));
		pan.add(new JLabel(Util.round(rel.get("Mean"),2) + ""));
		pan.add(new JLabel(Util.round(rel.get("SD"),2) + ""));

		panels.put("Precision", pan);
		
		/*Compare*/
		Map<Long, Double> allFixErr = new HashMap<Long, Double>();
		Map<Long, Double> allGzeErr = new HashMap<Long, Double>();
		Map<Long, Double> allPrc = new HashMap<Long, Double>();
		for(Entry<Long, Map<String, List<Double>>> ent : dataQuality.entrySet()) {
			allFixErr.put(ent.getKey(), DataLabb.getDoubleMeanAndSD(ent.getValue().get("fixErr")).get("Mean") * 100);
			allGzeErr.put(ent.getKey(), DataLabb.getDoubleMeanAndSD(ent.getValue().get("gzeErr")).get("Mean") * 100);
			allPrc.put(ent.getKey(), DataLabb.getDoubleMeanAndSD(ent.getValue().get("prc")).get("Mean") * 100);
		}
		
		List<XYSeries> series = new ArrayList<XYSeries>();
		series.add(LineChart.generateSeries(allFixErr, "FIX-ACC"));
		series.add(LineChart.generateSeries(allGzeErr, "GZE-ACC"));
		series.add(LineChart.generateSeries(allPrc, "RMS-S2S"));

		LineChart chart = new LineChart(series, "Data quality to sample rate", "Sample rate", "%");
		chart.setPreferredSize(new Dimension(500, 350));
		panels.put("Data quality", chart);	
		
		
		return panels;
	}

	/*
	 * Fixation processing
	 */
	public static Map<String, JPanel> getFixtionProcessing(List<SubjectData> subjects){
		Map<String, JPanel> pans = new HashMap<String, JPanel>();

		//Data keepers
		Map<Integer, List<Double>> fixesForSaccades = new HashMap<Integer, List<Double>>();
		fixesForSaccades.put(1, new ArrayList<Double>());
		fixesForSaccades.put(2, new ArrayList<Double>());
		fixesForSaccades.put(3, new ArrayList<Double>());
		fixesForSaccades.put(0, new ArrayList<Double>());

		Map<Integer, List<Double>> fixesForM12 = new HashMap<Integer, List<Double>>();
		fixesForM12.put(1, new ArrayList<Double>());
		fixesForM12.put(2, new ArrayList<Double>());
		fixesForM12.put(3, new ArrayList<Double>());
		fixesForM12.put(0, new ArrayList<Double>());

		Map<Integer, List<Double>> fixesForCluster = new HashMap<Integer, List<Double>>();
		fixesForCluster.put(1, new ArrayList<Double>());
		fixesForCluster.put(2, new ArrayList<Double>());
		fixesForCluster.put(3, new ArrayList<Double>());
		fixesForCluster.put(0, new ArrayList<Double>());

		//Split into frequency rates and convert to fixations / 10 seconds
		List<SubjectData> okData = DataLabb.processableSubjectsOnly(subjects);
		for(SubjectData sub : okData) {
			Double saccFixes = 0D;
			Double M12Fixes = 0D;
			Double clusterFixes = 0D;

			for(SnippetSession snip : sub.getSnippetSessions()) {
				Double tenSecWindows = snip.getResponseTime() / 10000D;
				saccFixes = saccFixes + (sub.getProcessedData().getSaccadesFixations(snip.getSessionName()).size() / tenSecWindows);
				M12Fixes = M12Fixes + (sub.getProcessedData().getClusterFixations(snip.getSessionName()).size() / tenSecWindows);
				clusterFixes = clusterFixes + (sub.getProcessedData().getSpatTempFixations(snip.getSessionName()).size() / tenSecWindows);
			}

			//per trial
			saccFixes = saccFixes / sub.getSnippetsDone();
			M12Fixes = M12Fixes / sub.getSnippetsDone();
			clusterFixes = clusterFixes / sub.getSnippetsDone();

			int sampGroup = sub.getSampleFrequenzy() / 10 > 3 ? 3 : sub.getSampleFrequenzy() / 10;

			fixesForSaccades.get(sampGroup).add(saccFixes);
			fixesForM12.get(sampGroup).add(M12Fixes);
			fixesForCluster.get(sampGroup).add(clusterFixes);
			fixesForSaccades.get(0).add(saccFixes);
			fixesForM12.get(0).add(M12Fixes);
			fixesForCluster.get(0).add(clusterFixes);
		}

		//Chart 
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixesForSaccades.get(1)).get("Mean") , "Saccades", "10-19Hz" );
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixesForSaccades.get(2)).get("Mean") , "Saccades", "20-29Hz" );      
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixesForSaccades.get(3)).get("Mean") , "Saccades", "30+ Hz"); 
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixesForSaccades.get(0)).get("Mean") , "Saccades", "Overall"); 

		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixesForM12.get(1)).get("Mean") , "M12", "10-19Hz" );        
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixesForM12.get(2)).get("Mean") , "M12", "20-29Hz" );       
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixesForM12.get(3)).get("Mean") , "M12", "30+ Hz" );   
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixesForM12.get(0)).get("Mean") , "M12", "Overall" ); 

		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixesForCluster.get(1)).get("Mean") , "Spatio-temporal", "10-19Hz"  );        
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixesForCluster.get(2)).get("Mean") , "Spatio-temporal", "20-29Hz" );        
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixesForCluster.get(3)).get("Mean"), "Spatio-temporal", "30+ Hz" );  
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixesForCluster.get(0)).get("Mean"), "Spatio-temporal", "Overall" );  

		JFreeChart barChart = ChartFactory.createBarChart(
				"Fixations detected",           
				"Algorithm",            
				"Per 10 seconds",            
				dataset,          
				PlotOrientation.VERTICAL,           
				true, true, false);

		ChartPanel chartPanel = new ChartPanel( barChart );        
		chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );

		pans.put("Fixation algorithms", chartPanel);

		//mappable to line--------------------
		//Get all line metrics
		List<Double> saccLst = new ArrayList<Double>();
		List<Double> M12Lst = new ArrayList<Double>();
		List<Double> clusterLst = new ArrayList<Double>();

		for(SubjectData sub : okData) {
			if(sub.getSampleFrequenzy() < 20) {
				continue;
			}
			for(SnippetSession ses : sub.getSnippetSessions()) {
				//Saccades
				List<Fixation> fixes = new ArrayList<Fixation>(DataLabb.matchFixationsToRows(sub.getProcessedData().getSaccadesFixations(ses.getSessionName())));
				int all = fixes.size();
				fixes.removeIf(f -> f.getRowIdx() == null);
				Double matched = Double.valueOf(fixes.size());

				saccLst.add(matched / all);

				//M12
				fixes = new ArrayList<Fixation>(DataLabb.matchFixationsToRows(sub.getProcessedData().getClusterFixations(ses.getSessionName())));
				all = fixes.size();
				fixes.removeIf(f -> f.getRowIdx() == null);
				matched = Double.valueOf(fixes.size());

				if(all > 0 ) {
					M12Lst.add(matched / all);
				}else {
					M12Lst.add(0D);
				}

				//cluster
				fixes = new ArrayList<Fixation>(DataLabb.matchFixationsToRows(sub.getProcessedData().getSpatTempFixations(ses.getSessionName())));
				all = fixes.size();
				fixes.removeIf(f -> f.getRowIdx() == null);
				matched = Double.valueOf(fixes.size());

				if(all > 0) {
					clusterLst.add(matched / all);
				}else {
					clusterLst.add(0D);
				}
			}
		}

		//Average
		Map<String, Double> saccStat = DataLabb.getDoubleMeanAndSD(saccLst);
		Map<String, Double> M12Stat = DataLabb.getDoubleMeanAndSD(M12Lst);
		Map<String, Double> clusterStat = DataLabb.getDoubleMeanAndSD(clusterLst);

		//create table
		JPanel tbl = new JPanel(new GridLayout(0, 3, 10, 10));
		tbl.add(new JLabel());
		tbl.add(new JLabel("Mean"));
		tbl.add(new JLabel("SD"));

		tbl.add(new JLabel("Saccades"));
		tbl.add(new JLabel(Util.round(saccStat.get("Mean") * 100,2) + ""));
		tbl.add(new JLabel(Util.round(saccStat.get("SD") * 100,2) + ""));

		tbl.add(new JLabel("M12"));
		tbl.add(new JLabel(Util.round(M12Stat.get("Mean") * 100,2) + ""));
		tbl.add(new JLabel(Util.round(M12Stat.get("SD") * 100,2) + ""));

		tbl.add(new JLabel("Temp cluster"));
		tbl.add(new JLabel(Util.round(clusterStat.get("Mean") * 100,2) + ""));
		tbl.add(new JLabel(Util.round(clusterStat.get("SD") * 100,2) + ""));

		pans.put("Fixations mapped to rows", tbl);


		//table of viable snippets
		JPanel viaData = new JPanel(new GridLayout(0, 4));
		viaData.add(new JLabel());
		viaData.add(new JLabel("Story"));
		viaData.add(new JLabel("Impertive"));
		viaData.add(new JLabel("Reactive"));

		int totImp = 0, totTxt = 0, totRect = 0, corViaImp = 0, corViaTxt = 0, corViaReact = 0;
		List<SubjectData> competentData = DataLabb.competentSubjectsOnly(subjects);
		for(SubjectData sub : competentData) {
			viaData.add(new JLabel(sub.getSubjectID()));

			int viableImp = 0;
			int viableTxt = 0;
			int viableReact = 0;
			for(SnippetSession ses : sub.getSnippetSessions()) {
				List<Fixation> fixes = sub.getProcessedData().getSpatTempFixations(ses.getSessionName());
				fixes = DataLabb.matchFixationsToRows(fixes);

				Double f = (double) fixes.size();
				fixes.removeIf(fix -> fix.getRowIdx() == null);
				Double m = (double) fixes.size();

				if(m/f >= 0.5) {
					if(ses.getSessionName().endsWith("S")) {
						viableTxt = viableTxt + 1;
						if(ses.isAnswerCorrect()) {corViaTxt = corViaTxt + 1;}
					}else if(ses.getSessionName().endsWith("I")) {
						viableImp = viableImp + 1;
						if(ses.isAnswerCorrect()) {corViaImp = corViaImp + 1;}
					}else if(ses.getSessionName().endsWith("R")) {
						viableReact = viableReact + 1;
						if(ses.isAnswerCorrect()) {corViaReact = corViaReact + 1;}
					}
				}	
			}

			totTxt = totTxt + viableTxt;
			totImp = totImp + viableImp;
			totRect = totRect + viableReact;

			viaData.add(new JLabel(viableTxt + "/" + sub.getTextDone(), JLabel.CENTER));
			if(viableTxt < 2) {
				viaData.getComponent(viaData.getComponentCount() - 1).setForeground(Color.RED);
			}
			viaData.add(new JLabel(viableImp + "/" + sub.getImpDone(), JLabel.CENTER));
			if(viableImp < 2) {
				viaData.getComponent(viaData.getComponentCount() - 1).setForeground(Color.RED);
			}
			if(sub.getReactDone() > 0) {
				viaData.add(new JLabel(viableReact + "/" + sub.getReactDone(), JLabel.CENTER));
				if(viableReact < 2) {
					viaData.getComponent(viaData.getComponentCount() - 1).setForeground(Color.RED);
				}
			}else {
				viaData.add(new JLabel(""));
			}
		}
		viaData.add(new JLabel("Total correct:   "));
		viaData.add(new JLabel(corViaTxt + "/" + totTxt));
		viaData.add(new JLabel(corViaImp + "/" + totImp));
		viaData.add(new JLabel(corViaReact + "/" + totRect));

		pans.put("Usable data", viaData);

		//Line chart of fixations in accordance with individual sampling rates
		Map<Long, Double> M12 = new HashMap<Long, Double>();
		Map<Long, Double> Saccades = new HashMap<Long, Double>();
		Map<Long, Double> SpatioTemp = new HashMap<Long, Double>();

		Map<Long, List<Double>> allM12 = new HashMap<Long, List<Double>>();
		Map<Long, List<Double>> allSaccades = new HashMap<Long, List<Double>>();
		Map<Long, List<Double>> allSpatTemp = new HashMap<Long, List<Double>>();

		List<Fixation> allSpatFixes = new ArrayList<Fixation>();
		List<Fixation> allM12Fixes = new ArrayList<Fixation>();

		for(SubjectData sub : okData) {
			Long frq = sub.getSampleFrequenzy().longValue();
			Double subM12 = 0D, subSaccades = 0D, subSpatTemp = 0D;

			for(SnippetSession ses : sub.getSnippetSessions()) {
				Double tenSecWindows = ses.getResponseTime() / 10000D;

				subM12 = subM12 + (double) sub.getProcessedData().getClusterFixations(ses.getSessionName()).size() / tenSecWindows;
				subSaccades = subSaccades + (double) sub.getProcessedData().getSaccadesFixations(ses.getSessionName()).size() / tenSecWindows;
				subSpatTemp = subSpatTemp + (double) sub.getProcessedData().getSpatTempFixations(ses.getSessionName()).size() / tenSecWindows;		
				allSpatFixes.addAll(sub.getProcessedData().getSpatTempFixations(ses.getSessionName()));
				allM12Fixes.addAll(sub.getProcessedData().getClusterFixations(ses.getSessionName()));
			}	

			if(!allM12.containsKey(frq))
				allM12.put(frq, new ArrayList<Double>());
			allM12.get(frq).add(subM12 / sub.getSnippetSessions().size());

			if(!allSaccades.containsKey(frq))
				allSaccades.put(frq, new ArrayList<Double>());
			allSaccades.get(frq).add(subSaccades / sub.getSnippetSessions().size());

			if(!allSpatTemp.containsKey(frq))
				allSpatTemp.put(frq, new ArrayList<Double>());
			allSpatTemp.get(frq).add(subSpatTemp / sub.getSnippetSessions().size());
		}

		for(Entry<Long, List<Double>> ent : allM12.entrySet()) {
			M12.put(ent.getKey(), DataLabb.getDoubleMeanAndSD(ent.getValue()).get("Mean"));
		}
		for(Entry<Long, List<Double>> ent : allSaccades.entrySet()) {
			Saccades.put(ent.getKey(), DataLabb.getDoubleMeanAndSD(ent.getValue()).get("Mean"));
		}
		for(Entry<Long, List<Double>> ent : allSpatTemp.entrySet()) {
			SpatioTemp.put(ent.getKey(), DataLabb.getDoubleMeanAndSD(ent.getValue()).get("Mean"));
		}

		List<XYSeries> series = new ArrayList<XYSeries>();
		series.add(LineChart.generateSeries(M12, "M12"));
		series.add(LineChart.generateSeries(Saccades, "Saccades"));
		series.add(LineChart.generateSeries(SpatioTemp, "SpatioTemp"));

		LineChart chart = new LineChart(series, "Fixations to sample rate", "Sample rate", "fixations per 10 sec");
		chart.setPreferredSize(new Dimension(500, 350));
		pans.put("Fixations to sample rate", chart);	

		return pans;
	}

	/*
	 * Reading patterns
	 */
	public static LinkedHashMap<String, JPanel> getNTImperative(List<SubjectData> subjects){
		LinkedHashMap<String, JPanel> pans = new LinkedHashMap<String, JPanel>();

		//Compare Java vs. Natural Text
		List<LinkedHashMap<String, Double>> allStory = new ArrayList<LinkedHashMap<String,Double>>();
		List<LinkedHashMap<String, Double>> allImperative = new ArrayList<LinkedHashMap<String,Double>>();

		LinkedHashMap<String, ArrayList<Double>> storyDataSet = new LinkedHashMap<String, ArrayList<Double>>();
		storyDataSet.put("Vertical Next", new ArrayList<Double>());
		storyDataSet.put("Vertical Later", new ArrayList<Double>());
		storyDataSet.put("Horizontal Later", new ArrayList<Double>());
		storyDataSet.put("Regression Rate", new ArrayList<Double>());
		storyDataSet.put("Line Regression Rate", new ArrayList<Double>());
		LinkedHashMap<String, ArrayList<Double>> imperativeDataSet = new LinkedHashMap<String, ArrayList<Double>>();
		imperativeDataSet.put("Vertical Next", new ArrayList<Double>());
		imperativeDataSet.put("Vertical Later", new ArrayList<Double>());
		imperativeDataSet.put("Horizontal Later", new ArrayList<Double>());
		imperativeDataSet.put("Regression Rate", new ArrayList<Double>());
		imperativeDataSet.put("Line Regression Rate", new ArrayList<Double>());

		List<Double> fixCountNT = new ArrayList<Double>();
		List<Double> fixCountImp = new ArrayList<Double>();
		List<Double> fixDurNT = new ArrayList<Double>();
		List<Double> fixDurImp = new ArrayList<Double>();

		List<SubjectData> competentData = DataLabb.competentSubjectsOnly(subjects);
		for(SubjectData compSub : competentData) { //Loop competent data only
			//subject data
			List<LinkedHashMap<String, Double>> subStory = new ArrayList<LinkedHashMap<String,Double>>();
			List<LinkedHashMap<String, Double>> subImperative = new ArrayList<LinkedHashMap<String,Double>>();
			List<Double> subFixNT = new ArrayList<Double>();
			List<Double> subFixImp = new ArrayList<Double>();
			List<Double> subDurNT = new ArrayList<Double>();
			List<Double> subDurImp = new ArrayList<Double>();

			LinkedHashMap<String, Double> metrics;
			for(SnippetSession ses : compSub.getSnippetSessions()) {
				Integer wordCount = DataLabb.codeDisplays.get(ses.getSessionName()).getWordCount();
				List<Fixation> fixes = compSub.getProcessedData().getSpatTempFixations(ses.getSessionName());
				List<Double> durs = new ArrayList<Double>();
				fixes.forEach(fix -> durs.add(fix.getDuration().doubleValue()));

				if(ses.getSessionName().endsWith("S")) {
					//this is a story snippet
					metrics = DataLabb.getLineMetrics(fixes);
					if(metrics != null) {subStory.add(metrics);}

					subFixNT.add((double) DataLabb.matchFixationsToRows(fixes).size() / wordCount);
					subDurNT.add(DataLabb.getDoubleMeanAndSD(durs).get("Mean"));
				}else if(ses.getSessionName().endsWith("I")) {
					//This is an imperative
					metrics = DataLabb.getLineMetrics(fixes);
					if(metrics != null) {subImperative.add(metrics);};	

					subFixImp.add((double) DataLabb.matchFixationsToRows(fixes).size() / wordCount);
					subDurImp.add(DataLabb.getDoubleMeanAndSD(durs).get("Mean"));
				}		
			}

			//check if less than 2 valid
			if (subStory.size() > 1 && subImperative.size() > 1) {
				//average metrics over subject
				LinkedHashMap<String, Double> story = Util.averageMetrics(subStory);
				LinkedHashMap<String, Double> imperative = Util.averageMetrics(subImperative);

				//Add to all metrics
				allImperative.add(imperative);
				allStory.add(story);

				//Add to dataset
				for(Entry<String, Double> ent : story.entrySet()) {
					storyDataSet.get(ent.getKey()).add(ent.getValue());					
				}
				for(Entry<String, Double> ent : imperative.entrySet()) {
					imperativeDataSet.get(ent.getKey()).add(ent.getValue());
				}

				//Add to fix count
				fixCountImp.add(DataLabb.getDoubleMeanAndSD(subFixImp).get("Mean"));
				fixCountNT.add(DataLabb.getDoubleMeanAndSD(subFixNT).get("Mean"));

				fixDurImp.add(DataLabb.getDoubleMeanAndSD(subDurImp).get("Mean"));
				fixDurNT.add(DataLabb.getDoubleMeanAndSD(subDurNT).get("Mean"));
			}else {
				System.out.println(compSub + "     Story: " + subStory.size() + "     Imp: " + subImperative.size());
			}
		}

		Map<String, Map<String, Double>> wilcox = new LinkedHashMap<String, Map<String,Double>>();

		try{
			wilcox.put("Vertical Next", DataLabb.wilcoxon(storyDataSet.get("Vertical Next"), imperativeDataSet.get("Vertical Next")));
			wilcox.put("Vertical Later", DataLabb.wilcoxon(storyDataSet.get("Vertical Later"), imperativeDataSet.get("Vertical Later")));
			wilcox.put("Horizontal Later", DataLabb.wilcoxon(storyDataSet.get("Horizontal Later"), imperativeDataSet.get("Horizontal Later")));
			wilcox.put("Regression Rate", DataLabb.wilcoxon(storyDataSet.get("Regression Rate"), imperativeDataSet.get("Regression Rate")));
			wilcox.put("Line Regression Rate", DataLabb.wilcoxon(storyDataSet.get("Line Regression Rate"), imperativeDataSet.get("Line Regression Rate")));
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		//Average
		LinkedHashMap<String, Double> story = Util.averageMetrics(allStory);
		LinkedHashMap<String, Double> imperative = Util.averageMetrics(allImperative);

		//----------Story vs. Imperative
		//Chart 
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(Entry<String, Double> storyMet : story.entrySet()) {
			dataset.addValue(storyMet.getValue(), "Natural text", storyMet.getKey());      
		}
		for(Entry<String, Double> impMet : imperative.entrySet()) {
			dataset.addValue(impMet.getValue(), "Imperative Java" , impMet.getKey());     
		}

		JFreeChart impStoryChart = ChartFactory.createBarChart(
				"Story vs. imperative Java",           
				"Metric",            
				"% of fixations",            
				dataset,          
				PlotOrientation.VERTICAL,           
				true, true, false);

		ChartPanel chartPanel = new ChartPanel( impStoryChart );        
		chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );

		pans.put("Scan path", chartPanel);

		//Wilcoxon
		JPanel wil = new JPanel(new GridLayout(0, 4, 10, 0));
		for(Entry<String, Map<String,Double>> ent : wilcox.entrySet()) {
			wil.add(new JLabel(ent.getKey()));
			wil.add(new JLabel("V = " + ent.getValue().get("V")));
			wil.add(new JLabel("Z = " + ent.getValue().get("z")));
			wil.add(new JLabel(ent.getValue().get("p") < 0.05D ? "p = " + ent.getValue().get("p") + "*": "p = " + ent.getValue().get("p")));
		}

		pans.put("Wilcoxon NT vs. Imperative", wil);

		//Datasets
		JPanel impStoryDataset = new JPanel(new GridLayout(1, 2, 10, 10));
		JPanel impDta = new JPanel(new BorderLayout());
		impDta.add(new JLabel("Imperative"), BorderLayout.NORTH);
		JPanel metrics = new JPanel(new GridLayout(1,5));
		impDta.add(metrics, BorderLayout.CENTER);
		for(String met : imperativeDataSet.keySet()) {
			JPanel metBox = new JPanel(new BorderLayout()); 
			metBox.add(new JLabel(met), BorderLayout.NORTH);
			metBox.add(new JTextArea(Util.prettyList(imperativeDataSet.get(met))), BorderLayout.CENTER);
			metrics.add(metBox);
		}
		JPanel stDta = new JPanel(new BorderLayout());
		stDta.add(new JLabel("Natural Text"), BorderLayout.NORTH);
		metrics = new JPanel(new GridLayout(1,5));		
		stDta.add(metrics, BorderLayout.CENTER);
		for(String met : imperativeDataSet.keySet()) {
			JPanel metBox = new JPanel(new BorderLayout()); 
			metBox.add(new JLabel(met), BorderLayout.NORTH);
			metBox.add(new JTextArea(Util.prettyList(storyDataSet.get(met))), BorderLayout.CENTER);
			metrics.add(metBox);
		}
		impStoryDataset.add(impDta);
		impStoryDataset.add(stDta);
		pans.put("Subject paired data", impStoryDataset);

		//--------Compare fixation metrics
		dataset = new DefaultCategoryDataset();
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixCountImp).get("Mean"), "Imperative fixations","Imperative fixations");
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixCountNT).get("Mean"), "NT fixations", "NT fixations");

		impStoryChart = ChartFactory.createBarChart(
				"Story vs. imperative Java",           
				"Snippet type",            
				"Fixations",            
				dataset,          
				PlotOrientation.VERTICAL,           
				true, true, false);

		chartPanel = new ChartPanel( impStoryChart );        
		chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );

		pans.put("Fixation count", chartPanel);

		wil = new JPanel(new GridLayout(0, 4, 10, 0));
		Map<String, Double> wilcoMap = null;
		try {
			wilcoMap = DataLabb.wilcoxon(fixCountNT, fixCountImp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wil.add(new JLabel("V = " + wilcoMap.get("V")));
		wil.add(new JLabel("Z = " + wilcoMap.get("z")));
		wil.add(new JLabel(wilcoMap.get("p") < 0.05D ? "p = " + wilcoMap.get("p") + "*": "p = " + wilcoMap.get("p")));

		pans.put("Wilcoxon fixation count", wil);

		dataset = new DefaultCategoryDataset();
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixDurImp).get("Mean"), "Imperative","Imperative");
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixDurNT).get("Mean"), "NT", "NT");

		impStoryChart = ChartFactory.createBarChart(
				"Story vs. imperative Java",           
				"Snippet type",            
				"Mean fixation duration",            
				dataset,          
				PlotOrientation.VERTICAL,           
				true, true, false);

		chartPanel = new ChartPanel( impStoryChart );        
		chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );

		pans.put("Mean fixation duration",chartPanel);


		wil = new JPanel(new GridLayout(0, 4, 10, 0));
		wilcoMap = null;
		try {
			wilcoMap = DataLabb.wilcoxon(fixDurNT, fixDurImp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wil.add(new JLabel("V = " + wilcoMap.get("V")));
		wil.add(new JLabel("Z = " + wilcoMap.get("z")));
		wil.add(new JLabel(wilcoMap.get("p") < 0.05D ? "p = " + wilcoMap.get("p") + "*": "p = " + wilcoMap.get("p")));

		pans.put("Wilcoxon fixation duration", wil);

		return pans;
	}

	public static LinkedHashMap<String, JPanel> getImperativeReactive(List<SubjectData> subjects){
		LinkedHashMap<String, JPanel> pans = new LinkedHashMap<String, JPanel>();

		//Compare imp vs react
		List<LinkedHashMap<String, Double>> allReactive = new ArrayList<LinkedHashMap<String,Double>>();
		List<LinkedHashMap<String, Double>> allReactImperative = new ArrayList<LinkedHashMap<String,Double>>();

		LinkedHashMap<String, ArrayList<Double>> reactDataSet = new LinkedHashMap<String, ArrayList<Double>>();
		reactDataSet.put("Vertical Next", new ArrayList<Double>());
		reactDataSet.put("Vertical Later", new ArrayList<Double>());
		reactDataSet.put("Horizontal Later", new ArrayList<Double>());
		reactDataSet.put("Regression Rate", new ArrayList<Double>());
		reactDataSet.put("Line Regression Rate", new ArrayList<Double>());
		LinkedHashMap<String, ArrayList<Double>> reactImpDataSet = new LinkedHashMap<String, ArrayList<Double>>();
		reactImpDataSet.put("Vertical Next", new ArrayList<Double>());
		reactImpDataSet.put("Vertical Later", new ArrayList<Double>());
		reactImpDataSet.put("Horizontal Later", new ArrayList<Double>());
		reactImpDataSet.put("Regression Rate", new ArrayList<Double>());
		reactImpDataSet.put("Line Regression Rate", new ArrayList<Double>());		

		List<Double> fixCountReact = new ArrayList<Double>();
		List<Double> fixCountImp = new ArrayList<Double>();
		List<Double> fixDurReact = new ArrayList<Double>();
		List<Double> fixDurImp = new ArrayList<Double>();

		List<SubjectData> reactParticipants = new ArrayList<SubjectData>(DataLabb.reactiveSubjectsOnly(subjects));
		for(SubjectData reactSub : reactParticipants) {
			//subject data
			List<LinkedHashMap<String, Double>> subReactive = new ArrayList<LinkedHashMap<String,Double>>();
			List<LinkedHashMap<String, Double>> subImperative = new ArrayList<LinkedHashMap<String,Double>>();
			List<Double> subFixImp = new ArrayList<Double>();
			List<Double> subFixReact = new ArrayList<Double>();
			List<Double> subDurReact = new ArrayList<Double>();
			List<Double> subDurImp = new ArrayList<Double>();

			LinkedHashMap<String, Double> metrics;
			for(SnippetSession ses : reactSub.getSnippetSessions()) {
				Integer wordCount = DataLabb.codeDisplays.get(ses.getSessionName()).getWordCount();
				List<Fixation> fixes = reactSub.getProcessedData().getSpatTempFixations(ses.getSessionName());
				List<Double> durs = new ArrayList<Double>();
				fixes.forEach(fix -> durs.add(fix.getDuration().doubleValue()));

				if(ses.getSessionName().endsWith("I")) {
					//This is an imperative
					metrics = DataLabb.getLineMetrics(fixes);
					if(metrics != null) {subImperative.add(metrics);}

					subFixImp.add((double) DataLabb.matchFixationsToRows(fixes).size() / wordCount);
					subDurImp.add(DataLabb.getDoubleMeanAndSD(durs).get("Mean"));
				}else if(ses.getSessionName().endsWith("R")) {
					//this is a reactive snippet
					metrics = DataLabb.getLineMetrics(fixes);
					if(metrics != null) {subReactive.add(metrics);}

					subFixReact.add((double) DataLabb.matchFixationsToRows(fixes).size() / wordCount);
					subDurReact.add(DataLabb.getDoubleMeanAndSD(durs).get("Mean"));
				}			
			}

			//check if less than 2 valid
			if (subImperative.size() < 2 || subReactive.size() < 2) {
				continue;
			}

			//average metrics over subject
			LinkedHashMap<String, Double> reactive = Util.averageMetrics(subReactive);
			LinkedHashMap<String, Double> imperative = Util.averageMetrics(subImperative);

			//Add to all metrics
			allReactImperative.add(imperative);
			allReactive.add(reactive);

			fixCountImp.add(DataLabb.getDoubleMeanAndSD(subFixImp).get("Mean"));
			fixCountReact.add(DataLabb.getDoubleMeanAndSD(subFixReact).get("Mean"));
			fixDurImp.add(DataLabb.getDoubleMeanAndSD(subDurImp).get("Mean"));
			fixDurReact.add(DataLabb.getDoubleMeanAndSD(subDurReact).get("Mean"));

			//Add to dataset
			for(String metric : reactive.keySet()) {
				reactDataSet.get(metric).add(reactive.get(metric));
			}
			for(String metric : imperative.keySet()) {
				reactImpDataSet.get(metric).add(imperative.get(metric));
			}

		}

		Map<String, Map<String, Double>> wilcox = new LinkedHashMap<String, Map<String,Double>>();

		try{
			wilcox.put("Vertical Next", DataLabb.wilcoxon(reactImpDataSet.get("Vertical Next"), reactDataSet.get("Vertical Next")));
			wilcox.put("Vertical Later", DataLabb.wilcoxon(reactImpDataSet.get("Vertical Later"), reactDataSet.get("Vertical Later")));
			wilcox.put("Horizontal Later", DataLabb.wilcoxon(reactImpDataSet.get("Horizontal Later"), reactDataSet.get("Horizontal Later")));
			wilcox.put("Regression Rate", DataLabb.wilcoxon(reactImpDataSet.get("Regression Rate"), reactDataSet.get("Regression Rate")));
			wilcox.put("Line Regression Rate", DataLabb.wilcoxon(reactImpDataSet.get("Line Regression Rate"), reactDataSet.get("Line Regression Rate")));
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		//Average
		LinkedHashMap<String, Double> reactive = Util.averageMetrics(allReactive);
		LinkedHashMap<String, Double> reactImp = Util.averageMetrics(allReactImperative);

		//Chart
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(Entry<String, Double> reactMet : reactive.entrySet()) {
			dataset.addValue(reactMet.getValue(), "Reactive Java", reactMet.getKey());      
		}
		for(Entry<String, Double> impMet : reactImp.entrySet()) {
			dataset.addValue(impMet.getValue(), "Imperative Java" , impMet.getKey());     
		}

		JFreeChart reactStoryChart = ChartFactory.createBarChart(
				"Imperative Java vs. reactive",           
				"Metric",            
				"% of fixations",            
				dataset,          
				PlotOrientation.VERTICAL,           
				true, true, false);

		ChartPanel reactPanel = new ChartPanel( reactStoryChart );        
		reactPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );

		pans.put("Imperative Java vs. Reactive", reactPanel);

		//Wilcoxon
		JPanel wil = new JPanel(new GridLayout(0, 4, 10, 0));
		for(Entry<String, Map<String,Double>> ent : wilcox.entrySet()) {
			wil.add(new JLabel(ent.getKey()));
			wil.add(new JLabel("V = " + ent.getValue().get("V")));
			wil.add(new JLabel("Z = " + ent.getValue().get("z")));
			wil.add(new JLabel(ent.getValue().get("p") < 0.05D ? "p = " + ent.getValue().get("p") + "*": "p = " + ent.getValue().get("p")));
		}

		pans.put("Wilcoxon Imperative vs. Reactive", wil);

		//Datasets
		JPanel impStoryDataset = new JPanel(new GridLayout(1, 2, 10, 10));
		JPanel impDta = new JPanel(new BorderLayout());
		impDta.add(new JLabel("Imperative code"), BorderLayout.NORTH);
		JPanel metrics = new JPanel(new GridLayout(1,5));
		impDta.add(metrics, BorderLayout.CENTER);
		for(String met : reactImpDataSet.keySet()) {
			JPanel metBox = new JPanel(new BorderLayout()); 
			metBox.add(new JLabel(met), BorderLayout.NORTH);
			metBox.add(new JTextArea(Util.prettyList(reactImpDataSet.get(met))), BorderLayout.CENTER);
			metrics.add(metBox);
		}
		JPanel stDta = new JPanel(new BorderLayout());
		stDta.add(new JLabel("Reactive code"), BorderLayout.NORTH);
		metrics = new JPanel(new GridLayout(1,5));		
		stDta.add(metrics, BorderLayout.CENTER);
		for(String met : reactDataSet.keySet()) {
			JPanel metBox = new JPanel(new BorderLayout()); 
			metBox.add(new JLabel(met), BorderLayout.NORTH);
			metBox.add(new JTextArea(Util.prettyList(reactDataSet.get(met))), BorderLayout.CENTER);
			metrics.add(metBox);
		}
		impStoryDataset.add(impDta);
		impStoryDataset.add(stDta);
		pans.put("Subject paired data", impStoryDataset);

		//--------Compare fixation metrics
		dataset = new DefaultCategoryDataset();
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixCountImp).get("Mean"), "Imperative fixations","Imperative fixations");
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixCountReact).get("Mean"), "Reactive fixations", "Reactive fixations");

		reactStoryChart = ChartFactory.createBarChart(
				"Imperative Java vs. reactive",           
				"Snippet type",            
				"Fixations",            
				dataset,          
				PlotOrientation.VERTICAL,           
				true, true, false);

		reactPanel = new ChartPanel( reactStoryChart );        
		reactPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );

		pans.put("Fixation count", reactPanel);

		wil = new JPanel(new GridLayout(0, 4, 10, 0));
		Map<String, Double> wilcoMap = null;
		try {
			wilcoMap = DataLabb.wilcoxon(fixCountImp, fixCountReact);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wil.add(new JLabel("V = " + wilcoMap.get("V")));
		wil.add(new JLabel("Z = " + wilcoMap.get("z")));
		wil.add(new JLabel(wilcoMap.get("p") < 0.05D ? "p = " + wilcoMap.get("p") + "*": "p = " + wilcoMap.get("p")));

		pans.put("Wilcoxon fixation count", wil);

		dataset = new DefaultCategoryDataset();
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixDurImp).get("Mean"), "Imperative","Imperative");
		dataset.addValue(DataLabb.getDoubleMeanAndSD(fixDurReact).get("Mean"), "Reactive", "Reactive");

		reactStoryChart = ChartFactory.createBarChart(
				"Imperative Java vs. reactive",           
				"Snippet type",            
				"Mean fixation duration",            
				dataset,          
				PlotOrientation.VERTICAL,           
				true, true, false);

		reactPanel = new ChartPanel( reactStoryChart );        
		reactPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );

		pans.put("Mean fixation duration",reactPanel);


		wil = new JPanel(new GridLayout(0, 4, 10, 0));
		wilcoMap = null;
		try {
			wilcoMap = DataLabb.wilcoxon(fixDurImp, fixDurReact);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wil.add(new JLabel("V = " + wilcoMap.get("V")));
		wil.add(new JLabel("Z = " + wilcoMap.get("z")));
		wil.add(new JLabel(wilcoMap.get("p") < 0.05D ? "p = " + wilcoMap.get("p") + "*": "p = " + wilcoMap.get("p")));

		pans.put("Wilcoxon fixation duration", wil);

		return pans;
	}

	public static LinkedHashMap<String, JPanel> getComprehension(List<SubjectData> subjects){
		LinkedHashMap<String, JPanel> pans = new LinkedHashMap<String, JPanel>();

		//Check all imperative data for correctness
		LinkedHashMap<String, List<LinkedHashMap<String, Double>>> allCorrect = new LinkedHashMap<String, List<LinkedHashMap<String,Double>>>();
		LinkedHashMap<String, List<LinkedHashMap<String, Double>>> allIncorrect = new LinkedHashMap<String, List<LinkedHashMap<String,Double>>>();

		LinkedHashMap<String, ArrayList<Double>> correctDataSet = new LinkedHashMap<String, ArrayList<Double>>();
		correctDataSet.put("Vertical Next", new ArrayList<Double>());
		correctDataSet.put("Vertical Later", new ArrayList<Double>());
		correctDataSet.put("Horizontal Later", new ArrayList<Double>());
		correctDataSet.put("Regression Rate", new ArrayList<Double>());
		correctDataSet.put("Line Regression Rate", new ArrayList<Double>());
		LinkedHashMap<String, ArrayList<Double>> wrongDataSet = new LinkedHashMap<String, ArrayList<Double>>();
		wrongDataSet.put("Vertical Next", new ArrayList<Double>());
		wrongDataSet.put("Vertical Later", new ArrayList<Double>());
		wrongDataSet.put("Horizontal Later", new ArrayList<Double>());
		wrongDataSet.put("Regression Rate", new ArrayList<Double>());
		wrongDataSet.put("Line Regression Rate", new ArrayList<Double>());	

		List<SubjectData> impData = DataLabb.processableSubjectsOnly(subjects);
		for(SubjectData sub : impData) {			
			LinkedHashMap<String, Double> metrics;
			for(SnippetSession ses : sub.getSnippetSessions()) {
				if(ses.getSessionName().endsWith("I")) {
					if(ses.isAnswerCorrect()) {
						//this is a correctly answered imperative snippet
						metrics = DataLabb.getLineMetrics(sub.getProcessedData().getSpatTempFixations(ses.getSessionName()));
						if(metrics != null) {
							if(!allCorrect.containsKey(ses.getSessionName()))
								allCorrect.put(ses.getSessionName(), new ArrayList<LinkedHashMap<String,Double>>());
							allCorrect.get(ses.getSessionName()).add(metrics);
						}
					}else {
						metrics = DataLabb.getLineMetrics(sub.getProcessedData().getSpatTempFixations(ses.getSessionName()));
						if(metrics != null) {
							if(!allIncorrect.containsKey(ses.getSessionName()))
								allIncorrect.put(ses.getSessionName(), new ArrayList<LinkedHashMap<String,Double>>());
							allIncorrect.get(ses.getSessionName()).add(metrics);
						}
					}
				}
			}
		}

		//Remove snippets not having pair
		List<String> snips = new ArrayList<String>(allIncorrect.keySet());
		for(String s : snips) {
			if(!allCorrect.containsKey(s)) {
				allIncorrect.remove(s);
			}
		}
		snips = new ArrayList<String>(allCorrect.keySet());
		for(String s : snips) {
			if(!allIncorrect.containsKey(s)) {
				allCorrect.remove(s);
			}
		}
		snips = new ArrayList<String>(allCorrect.keySet());

		//Average
		List<LinkedHashMap<String, Double>> correct = new ArrayList<LinkedHashMap<String,Double>>();
		for(String snip : snips) {
			LinkedHashMap<String, Double> avg = Util.averageMetrics(allCorrect.get(snip));
			correct.add(avg);
			for(String met : correctDataSet.keySet()) {
				correctDataSet.get(met).add(avg.get(met));
			}
		}		

		List<LinkedHashMap<String, Double>> incorrect = new ArrayList<LinkedHashMap<String,Double>>();
		for(String snip : snips) {
			LinkedHashMap<String, Double> avg = Util.averageMetrics(allIncorrect.get(snip));
			incorrect.add(avg);
			for(String met : wrongDataSet.keySet()) {
				wrongDataSet.get(met).add(avg.get(met));
			}
		}	

		LinkedHashMap<String, Double> overallCorrect = Util.averageMetrics(correct);
		LinkedHashMap<String, Double> overallWrong = Util.averageMetrics(incorrect);

		//Wilcoxon
		Map<String, Map<String, Double>> wilcox = new LinkedHashMap<String, Map<String,Double>>();

		try{
			wilcox.put("Vertical Next", DataLabb.wilcoxon(correctDataSet.get("Vertical Next"), wrongDataSet.get("Vertical Next")));
			wilcox.put("Vertical Later", DataLabb.wilcoxon(correctDataSet.get("Vertical Later"), wrongDataSet.get("Vertical Later")));
			wilcox.put("Horizontal Later", DataLabb.wilcoxon(correctDataSet.get("Horizontal Later"), wrongDataSet.get("Horizontal Later")));
			wilcox.put("Regression Rate", DataLabb.wilcoxon(correctDataSet.get("Regression Rate"), wrongDataSet.get("Regression Rate")));
			wilcox.put("Line Regression Rate", DataLabb.wilcoxon(correctDataSet.get("Line Regression Rate"), wrongDataSet.get("Line Regression Rate")));
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		//------------Correct vs incorrect
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(Entry<String, Double> corrMet : overallCorrect.entrySet()) {
			dataset.addValue(corrMet.getValue(), "Correct", corrMet.getKey() );      
		}
		for(Entry<String, Double> wrongMet : overallWrong.entrySet()) {
			dataset.addValue(wrongMet.getValue(), "Incorrect" , wrongMet.getKey());     
		}

		JFreeChart correctChart = ChartFactory.createBarChart(
				"Correct vs. incorrect Java",           
				"Metric",            
				"% of fixations",            
				dataset,          
				PlotOrientation.VERTICAL,           
				true, true, false);

		ChartPanel corrPanel = new ChartPanel( correctChart );        
		corrPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );

		pans.put("Correct vs. incorrect answers", corrPanel);

		//Wilcoxon
		JPanel wil = new JPanel(new GridLayout(0, 4, 10, 0));
		for(Entry<String, Map<String,Double>> ent : wilcox.entrySet()) {
			wil.add(new JLabel(ent.getKey()));
			wil.add(new JLabel("V = " + ent.getValue().get("V")));
			wil.add(new JLabel("Z = " + ent.getValue().get("z")));
			wil.add(new JLabel(ent.getValue().get("p") < 0.05D ? "p = " + ent.getValue().get("p") + "*": "p = " + ent.getValue().get("p")));
		}

		pans.put("Wilcoxon Correct vs. Incorrect", wil);

		//Datasets
		JPanel impStoryDataset = new JPanel(new GridLayout(1, 2, 10, 10));
		JPanel impDta = new JPanel(new BorderLayout());
		impDta.add(new JLabel("Correct code"), BorderLayout.NORTH);
		JPanel metrics = new JPanel(new GridLayout(1,5));
		impDta.add(metrics, BorderLayout.CENTER);
		for(String met : correctDataSet.keySet()) {
			JPanel metBox = new JPanel(new BorderLayout()); 
			metBox.add(new JLabel(met), BorderLayout.NORTH);
			metBox.add(new JTextArea(Util.prettyList(correctDataSet.get(met))), BorderLayout.CENTER);
			metrics.add(metBox);
		}
		JPanel stDta = new JPanel(new BorderLayout());
		stDta.add(new JLabel("Incorrect code"), BorderLayout.NORTH);
		metrics = new JPanel(new GridLayout(1,5));		
		stDta.add(metrics, BorderLayout.CENTER);
		for(String met : wrongDataSet.keySet()) {
			JPanel metBox = new JPanel(new BorderLayout()); 
			metBox.add(new JLabel(met), BorderLayout.NORTH);
			metBox.add(new JTextArea(Util.prettyList(wrongDataSet.get(met))), BorderLayout.CENTER);
			metrics.add(metBox);
		}
		impStoryDataset.add(impDta);
		impStoryDataset.add(stDta);
		pans.put("Subject paired data", impStoryDataset);

		//--------Compare fixation metrics
		dataset = new DefaultCategoryDataset();
		//dataset.addValue(DataLabb.getDoubleMeanAndSD(fixCountCorr).get("Mean"), "Imperative fixations","Imperative fixations");
		//dataset.addValue(DataLabb.getDoubleMeanAndSD(fixCountWrong).get("Mean"), "Reactive fixations", "Reactive fixations");

		correctChart = ChartFactory.createBarChart(
				"Imperative Java vs. reactive",           
				"Snippet type",            
				"Fixations",            
				dataset,          
				PlotOrientation.VERTICAL,           
				true, true, false);

		corrPanel = new ChartPanel( correctChart );        
		corrPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );

		pans.put("Fixation count", corrPanel);

		wil = new JPanel(new GridLayout(0, 4, 10, 0));
		Map<String, Double> wilcoMap = null;
		try {
			//wilcoMap = DataLabb.wilcoxon(fixCountCorr, fixCountWrong);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//wil.add(new JLabel("V = " + wilcoMap.get("V")));
		//wil.add(new JLabel("Z = " + wilcoMap.get("z")));
		//wil.add(new JLabel(wilcoMap.get("p") < 0.05D ? "p = " + wilcoMap.get("p") + "*": "p = " + wilcoMap.get("p")));

		pans.put("Wilcoxon fixation count", wil);

		return pans;
	}

	public static Map<String, JPanel> getDistributions(List<SubjectData> subjects){
		Map<String, JPanel> pans = new HashMap<String, JPanel>();

		//Competent programmers
		JPanel comp = new JPanel(new GridLayout(4, 2,10,10));
		List<SubjectData> competentData = DataLabb.competentSubjectsOnly(subjects);

		List<SubjectData> males = new ArrayList<SubjectData>(competentData);
		males.removeIf(sub -> !sub.getGender().equals("male"));
		comp.add(new JLabel("Male"));
		comp.add(new JLabel(males.size() + " (" + Util.round((double)(males.size())/competentData.size() * 100, 2) + "%)"));

		List<SubjectData> females = new ArrayList<SubjectData>(competentData);
		females.removeIf(sub -> !sub.getGender().equals("female"));
		comp.add(new JLabel("Female"));
		comp.add(new JLabel(females.size() + " (" + Util.round((double)(females.size())/competentData.size() * 100, 2) + "%)"));

		List<SubjectData> other = new ArrayList<SubjectData>(competentData);
		other.removeIf(sub -> !sub.getGender().equals("other"));
		comp.add(new JLabel("Other"));
		comp.add(new JLabel(other.size() + " (" + Util.round((double)(other.size())/competentData.size() * 100, 2) + "%)"));

		comp.add(new JLabel("Age in years (sd)"));
		List<Double> ages = new ArrayList<Double>();
		for(SubjectData sub : competentData) {
			ages.add(sub.getAge().doubleValue());
		};
		Map<String, Double> age = DataLabb.getDoubleMeanAndSD(ages);
		comp.add(new JLabel(Util.round(age.get("Mean"), 0) + " (" + Util.round(age.get("SD"), 0) + ")"));

		pans.put("Competent demographics", comp);

		//---usable data
		JPanel use = new JPanel(new GridLayout(0, 2, 10, 10));

		use.add(new JLabel("Total participants"));
		use.add(new JLabel(subjects.size() + " (100%)"));
		use.add(new JLabel("Started experiments"));
		use.add(new JLabel(DataLabb.startedSubjectsOnly(subjects).size() + " (" + Util.round((double)(DataLabb.startedSubjectsOnly(subjects).size()) / subjects.size() * 100, 0) + "%)"));
		use.add(new JLabel("Of which passed data treshhold"));
		use.add(new JLabel(DataLabb.processableSubjectsOnly(subjects).size() + " (" + Util.round((double)(DataLabb.processableSubjectsOnly(subjects).size()) / subjects.size() * 100, 0) + "%)"));
		use.add(new JLabel("Of which competent programmers"));
		use.add(new JLabel(DataLabb.competentSubjectsOnly(subjects).size() + " (" + Util.round((double)(DataLabb.competentSubjectsOnly(subjects).size()) / subjects.size() * 100, 0) + "%)"));

		pans.put("Participant filtration", use);


		//Origins
		int unis = 0, compa = 0, socMed = 0;		
		for(SubjectData sub : subjects) {
			if(sub.getOrigin().equals("1") || sub.getOrigin().equals("2")) {
				unis = unis+1;
			}else if(sub.getOrigin().equals("3") || sub.getOrigin().equals("6")) {
				socMed = socMed + 1;
			}else if(sub.getOrigin().equals("5")){
				compa = compa + 1;
			}
		}

		JPanel orig = new JPanel(new GridLayout(3,2, 10, 10));
		orig.add(new JLabel("Universities"));
		orig.add(new JLabel(unis + " (" + Util.round((double)(unis) / subjects.size() * 100, 0) + "%)"));
		orig.add(new JLabel("Companies"));
		orig.add(new JLabel(compa + " (" + Util.round((double)(compa) / subjects.size() * 100, 0) + "%)"));
		orig.add(new JLabel("Social media"));
		orig.add(new JLabel(socMed + " (" + Util.round((double)(socMed) / subjects.size() * 100, 0) + "%)"));
		pans.put("Subject origins", orig);


		return pans;
	}
}
