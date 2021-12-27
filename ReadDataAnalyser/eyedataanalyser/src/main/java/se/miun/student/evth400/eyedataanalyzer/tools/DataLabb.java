package se.miun.student.evth400.eyedataanalyzer.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.script.ScriptException;

import org.apache.commons.io.FileUtils;
import org.renjin.script.RenjinScriptEngine;
import org.renjin.sexp.DoubleArrayVector;
import org.renjin.sexp.DoubleVector;
import org.renjin.sexp.ListVector;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCallerOptions;
import com.github.rcaller.rstuff.RCode;

import se.miun.student.evth400.eyedataanalyzer.data.Fixation;
import se.miun.student.evth400.eyedataanalyzer.data.Gaze;
import se.miun.student.evth400.eyedataanalyzer.data.ProcessedData;
import se.miun.student.evth400.eyedataanalyzer.data.SubjectData;

public class DataLabb {
	public static Map<String, CodeStimuli> codeDisplays;

	/*
	 * Static constructor
	 * Loads all the different stimuli
	 */
	static {
		codeDisplays = new HashMap<String, CodeStimuli>();

		for(int i = 1 ; i < 16; i++) {
			String react = i + "R";
			String imp = i + "I";
			String story = i + "S";

			codeDisplays.put(react, new CodeStimuli(react));
			codeDisplays.put(imp, new CodeStimuli(imp));
			codeDisplays.put(story, new CodeStimuli(story));
		}
	}

	/*
	 * Removes any gaze sample outside snippet boundary by setting them to null.
	 * Should be done after filtration to avoid data loss.
	 */
	public static List<Gaze> removeSnippetOutOfBounds(List<Gaze> readings){
		//Remove any point that is outside the code display
		Double minX = 0D;
		Double maxX = 1000D;
		Double minY = 0D;
		Double maxY = 800D;

		List<Gaze> newList = new ArrayList<Gaze>();

		for(Gaze g: readings) {
			if(g.getX() > maxX || g.getY() > maxY || g.getX() < minX || g.getY() < minY) {
				g = new Gaze(g.getTrial(), null, null, g.getTimeStamp());
			}
			newList.add(g);
		}

		return newList;
	}

	/*
	 * Savinzky-Golay filter run with the Renjin-Engine
	 * @param List of gaze, window size, polynomial
	 */
	public static List<Gaze> applySavGolay(List<Gaze> gaze, int window, int poly) throws ScriptException {
		//R engine and function code
		RenjinScriptEngine engine = new RenjinScriptEngine();
		String code = "library(signal)\n"
				+ "savGol <- function(vector){\n"
				+ "sgolayfilt(vector, p = " + poly + ", n = " + window + ")\n"
				+ "}";

		//Extract x and  y
		double[] x = new double[gaze.size()], y = new double[gaze.size()];
		for(int i = 0; i < gaze.size(); i++) {
			x[i] = gaze.get(i).getX();
			y[i] = gaze.get(i).getY();
		}

		engine.put("input", x);
		engine.eval(code);

		DoubleVector result = (DoubleVector) engine.eval("savGol(input)");

		x = result.toDoubleArray();

		engine.put("input", y);
		engine.eval(code);
		result = (DoubleVector) engine.eval("savGol(input)");
		y = result.toDoubleArray();

		//Replace original values
		List<Gaze> filtered = new ArrayList<Gaze>();
		for(int i = 0; i < gaze.size(); i++) {
			filtered.add(new Gaze(gaze.get(i).getTrial(), x[i], y[i], gaze.get(i).getTimeStamp()));
		}

		return filtered;
	}

	/*
	 * Savitzky-Golay taking all the snippets and running them through the filter one at the time
	 * @Map of Gaze lists tied to session name
	 */
	public static Map<String, List<Gaze>> applySavGolay(Map<String, List<Gaze>> gaze, int window, int poly) throws ScriptException {
		Map<String, List<Gaze>> filtered = new HashMap<String, List<Gaze>>();

		//Filter each gaze session individually and each validation point
		for(Entry<String, List<Gaze>> ent : gaze.entrySet()) {
			filtered.put(ent.getKey(), applySavGolay(ent.getValue(), window, poly));
		}

		return filtered;
	}

	/*
	 * returns only competent subjects with sufficient sampling frequency
	 */
	public static List<SubjectData> competentSubjectsOnly(List<SubjectData> subs){
		List<SubjectData> filtered = new ArrayList<SubjectData>(subs);

		filtered.removeIf(sub -> sub.getSnippetsDone() < 6);
		filtered.removeIf(sub -> (sub.getCorrectImperativeSnippets() + sub.getCorrectReactiveSnippets()) < 3);
		filtered.removeIf(sub -> sub.getSampleFrequenzy() < 10);

		return filtered;
	}

	/*
	 * returns only reactive competent subjects
	 */
	public static List<SubjectData> reactiveSubjectsOnly(List<SubjectData> subs){
		List<SubjectData> filtered = competentSubjectsOnly(subs);

		filtered.removeIf(sub -> sub.getReactiveExperience().equals("None"));
		filtered.removeIf(sub -> sub.getCorrectReactiveSnippets() < 1);

		return filtered;
	}

	/*
	 * returns all subjects valid for data quality analysis
	 * At least one calibration and 3 snippets has to have been completeted
	 */
	public static List<SubjectData> startedSubjectsOnly(List<SubjectData> subs){
		List<SubjectData> filtered = new ArrayList<SubjectData>(subs);

		filtered.removeIf(sub -> sub.getSnippetsDone() < 3);

		return filtered;
	}

	/*
	 * returns subjects valid for filter and fixation processing
	 */
	public static List<SubjectData> processableSubjectsOnly(List<SubjectData> subs){
		List<SubjectData> filtered = new ArrayList<SubjectData>(subs);

		filtered.removeIf(sub -> sub.getSnippetsDone() < 3);
		filtered.removeIf(sub -> sub.getSampleFrequenzy() < 10);

		return filtered;
	}

	/*
	 * Calculates the velocity between gaze samples
	 * Not used atm but could be implemented into custom velocity based fixation algorithm
	 */	
	public static List<Gaze> calculateVelocity(List<Gaze> gaze){
		//make sure the gaze data is in chronological order
		Collections.sort(gaze);

		for(int i = 1; i < gaze.size(); i++) {
			Gaze thisGaze = gaze.get(i);
			Gaze lastGaze = gaze.get(i-1);

			//new trial started
			if(!thisGaze.getTrial().equals(lastGaze.getTrial())) {
				continue;
			}

			//null values
			if(thisGaze.getX() == null || lastGaze.getX() == null) {
				gaze.get(i).setVelocity(null);
				continue;
			}


			//Get distance
			Double d = eucledianDistance(thisGaze.getX(), thisGaze.getY(), lastGaze.getX(), lastGaze.getY());

			//get time
			Long t = thisGaze.getTimeStamp() - lastGaze.getTimeStamp();

			//Define v as px/s
			Double v = d / t * 1000;

			//set
			gaze.get(i).setVelocity(v);
		}

		return gaze;
	}

	/*
	 * "Saccades" fixation algorithm run with the R engine
	 * Takes time, save results
	 */	
	public static List<Fixation> getSaccadesFixations(ProcessedData data, String subjectID, Boolean regenerate) {
		File tmpFile = new File("SaccadesFixations/" + subjectID);		

		if(!regenerate) {
			//check to see if exists			
			if(tmpFile.exists()) {
				//load the file
				return Util.parseFixationXML(tmpFile);
			}
		}				

		data.applyOptimalFilter();		
		List<Gaze> gaze = data.getAllFilteredGaze();
		Collections.sort(gaze);


		RCode code = RCode.create();

		//select library
		code.addRCode("library(zoom)");
		code.addRCode("library(saccades)");

		//load the data
		code.addRCode("data <- read.csv(text=\"" + Util.gazeToCsv(gaze) + "\")");

		//do not smooth saccades
		code.addRCode("smooth.saccades <- FALSE");

		//As done by: Comparing webcam-based eyetracking with normal eyetracking in a value-based decision-making task
		code.addRCode("lambda <- 3.25");

		//detect fixations
		code.addRCode("fixations <- detect.fixations(data)");

		//Call script
		RCaller caller = RCaller.create(code, RCallerOptions.create());
		caller.runAndReturnResult("fixations");

		//Parse result		
		try {
			FileUtils.writeStringToFile(tmpFile, caller.getParser().getXMLFileAsString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Saccades fixations generated for " + subjectID);

		return Util.parseFixationXML(tmpFile);
	}

	/*
	 * Matlab have to be generated separately from the directories in the OutData folder
	 * and put into the MathLabResults folder. The M12 algorithm is to be run unfiltered in matlab
	 */
	public static List<Fixation> getMatLabFixations(String subID, ProcessedData data){
		File fixFile = new File("MatLabResults/" + subID + ".txt");

		if(fixFile.exists()) {
			return Util.parseFixationTAB(fixFile);
		}else {
			File outFile = new File("OutData/" + subID);
			if(outFile.exists())
				return null;

			//data.applyOptimalFilter();
			data.unFilter();

			//create output file
			for(Entry<String, String> ent : Util.gazeToTabDelim(data.getFiltered()).entrySet()) {
				String fileName = "OutData/" + subID + "/" + ent.getKey() + ".txt";
				File newFile = new File(fileName);
				newFile.getParentFile().mkdirs();

				try(PrintWriter writer = new PrintWriter(newFile)){
					writer.write(ent.getValue());
				}catch(FileNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}	

			System.out.println("output for file "+ subID + " done! Needs matlab processing");
		}

		return null;
	}

	/*
	 * Mean shift spato-temporal algorithm
	 * TODO: implement, not working properly
	 */
	public static List<Fixation> getMeanShiftClusteringFixations(ProcessedData data) throws ScriptException {
		//filter
		data.applyOptimalFilter();

		//List for fixations
		List<Fixation> fixes = new ArrayList<Fixation>();

		//space window
		double dia = 7;//CodeStimuli.rowHeight - 1;

		//R engine and code
		RenjinScriptEngine engine = new RenjinScriptEngine();
		String code = "library(meanShiftR)\n"
				//bandwidth
				+ "h <- c(" + dia + "," + dia + ",15)\n"
				//max iterations
				+ "iter <- 1000\n"
				//define function
				+ "mShift <- function(t,x,y){\n" 
				+ "m <- matrix(c(x, y, t), ncol = 3)\n" +
				"  meanShift(m,m,\n" + 
				"  bandwidth=h,\n" + 
				"  alpha=0,\n" +
				"  iterations = iter" +
				")\n"
				+ "}";

		//one trial at the time
		for(Entry<String, List<Gaze>> ent : data.getFiltered().entrySet()) {
			//Extract x and  y
			double[] x = new double[ent.getValue().size()], 
					y = new double[ent.getValue().size()];
			long[]	t = new long[ent.getValue().size()];

			for(int i = 0; i < ent.getValue().size(); i++) {
				x[i] = ent.getValue().get(i).getX();
				y[i] = ent.getValue().get(i).getY();
				t[i] = ent.getValue().get(i).getTimeStamp();
			}

			engine.put("x", x);
			engine.put("y", y);
			engine.put("t", t);
			engine.eval(code);

			ListVector result = (ListVector) engine.eval("mShift(t,x,y)");

			//convert to gaze clusters
			DoubleArrayVector assign = (DoubleArrayVector) result.get(0);
			//DoubleArrayVector value = (DoubleArrayVector) result.get(1);

			//For testing TODO: remove
			Map<Double, List<Gaze>> clusters = new HashMap<Double, List<Gaze>>();

			for(int idx = 0; idx < assign.length(); idx++) {
				if(!clusters.containsKey(assign.get(idx))) {
					clusters.put(assign.get(idx), new ArrayList<Gaze>());
				}

				clusters.get(assign.get(idx)).add(ent.getValue().get(idx));
			}


			Double largestRange = 0D;
			Long longestTime = 0L;

			for(Entry<Double, List<Gaze>> clust : clusters.entrySet()) {
				if(clust.getValue().size() > 1) {
					for(int g = 1; g < clust.getValue().size(); g++) {
						Double range = eucledianDistance(clust.getValue().get(g-1).getX(),
								clust.getValue().get(g-1).getY(), 
								clust.getValue().get(g).getX(),
								clust.getValue().get(g).getY());

						if(range > largestRange) {
							largestRange = range;
						}
					}

					Long time = clust.getValue().get(clust.getValue().size()-1).getTimeStamp() 
							- clust.getValue().get(0).getTimeStamp();

					if(time > longestTime) {
						longestTime = time;
					}
				}
			}


			System.out.println("Trial: " + ent.getKey());
			System.out.println("Range: " + Util.round(largestRange,2));
			System.out.println("Time: " + longestTime);
			System.out.println();
		}

		//discard the first fixation as it can be unrealiable TODO: implement
		return fixes;
	}

	@SuppressWarnings("unchecked")
	public static List<Fixation> getSpatioTempFixations(ProcessedData data){
		List<Fixation> fixes = new ArrayList<Fixation>();

		//check for existing file
		File fixFile = new File("SpatioTemporalFixations/" + data.subjectID);
		if(fixFile.exists()) {
			try {
				FileInputStream fileIn = new FileInputStream(fixFile);
				ObjectInputStream objectIn = new ObjectInputStream(fileIn);

				fixes = (List<Fixation>) objectIn.readObject();
				objectIn.close();

				return fixes;
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}

		//apply filter
		data.applyOptimalFilter();

		//go thorough each trial
		for(Entry<String, List<Gaze>> trial : data.getFiltered().entrySet()) {
			List<List<Gaze>> clusters = longestFix(trial.getValue());

			//create fixations
			for(List<Gaze> cluster : clusters) {
				Gaze avg = averageGazePoint(cluster);

				fixes.add(new Fixation(avg.getTrial(), cluster.get(0).getTimeStamp(),
						cluster.get(cluster.size() - 1).getTimeStamp(), avg.getX(), avg.getY(), null, null, null, null, 
						cluster.get(cluster.size() - 1).getTimeStamp() - cluster.get(0).getTimeStamp(), "fixation"));
			}
		}

		//Save to file
		try {
			FileOutputStream fileOut = new FileOutputStream(fixFile);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(fixes);
			objectOut.close();
			System.out.println(data.subjectID + " spatio-temp has been generated");
		}catch(Exception ex) {
			System.out.print(ex.getMessage());
		}

		return fixes;
	}

	/*
	 * Single sliding time window identifying the longest fixation area within a range
	 * Defining minimum fixation time as 200ms and gaze area within 2 text row diameter
	 * Recurses to find the largest fixations within remaining gaze
	 */
	private static List<List<Gaze>> longestFix(List<Gaze> searchArea){
		List<List<Gaze>> fixations = new ArrayList<List<Gaze>>();
		List<Gaze> largest = new ArrayList<Gaze>();
		int wStart, wEnd;

		//check the search area is long enough
		if(searchArea.size() == 0) {
			return fixations;
		}else if(searchArea.get(searchArea.size() -1).getTimeStamp() - searchArea.get(0).getTimeStamp() < 150) {
			return fixations;
		}

		//Slide
		for(wStart = 0, wEnd = 1; wEnd < searchArea.size(); wStart++, wEnd++) {
			//new window
			List<Gaze> currentWindow = new ArrayList<Gaze>();
			for(int i = wStart; i <= wEnd; i++) {
				currentWindow.add(searchArea.get(i));
			}

			//grow			
			Boolean withinBounds = true;
			while(withinBounds && wEnd < searchArea.size()-1) {
				//add end to window
				wEnd++;
				currentWindow.add(searchArea.get(wEnd));

				//test if adding another gaze point knocks any previous gazepoints out of the cluster
				Gaze centerGaze = averageGazePoint(currentWindow);

				for(Gaze gz : currentWindow) {
					if(eucledianDistance(gz.getX(), gz.getY(), centerGaze.getX(), centerGaze.getY()) > (CodeStimuli.rowHeight - 1)) {
						withinBounds = false;
					}
				}

				if(!withinBounds) {
					//remove it again
					wEnd--;
					currentWindow.remove(currentWindow.size()-1);					
				}
			}

			if(currentWindow.size() > largest.size()) {
				largest = currentWindow;
			}
		}	

		//Is it long enough >= 200 ms
		if(largest.get(largest.size() - 1).getTimeStamp() - largest.get(0).getTimeStamp() >= 150) {
			fixations.add(largest);

			//keep recursing
			final Long startTime = largest.get(0).getTimeStamp();
			final Long endTime = largest.get(largest.size() - 1).getTimeStamp();
			List<Gaze> after = new ArrayList<Gaze>(searchArea);
			List<Gaze> before = new ArrayList<Gaze>(searchArea);
			after.removeIf(g -> g.getTimeStamp() <= endTime);
			before.removeIf(g -> g.getTimeStamp() >= startTime);

			fixations.addAll(longestFix(before));
			fixations.addAll(longestFix(after));
		}

		return fixations;
	}

	/*
	 * Takes a list of Gaze and returns the center point of the distribution
	 */
	public static Gaze averageGazePoint(List<Gaze> gaze) {
		Double x = 0D, y = 0D;
		Long t = 0L;

		for(Gaze g: gaze) {
			x = x + g.getX();
			y = y + g.getY();
			t = t + g.getTimeStamp();
		}

		x = x / gaze.size();
		y = y / gaze.size();
		t = t / gaze.size();

		return new Gaze(gaze.get(0).getTrial(), x, y, t);
	}

	/*
	 * Matches fixations to corresponding row
	 */
	public static List<Fixation> matchFixationsToRows(List<Fixation> fixes) {
		List<Fixation> onStimuli = new ArrayList<Fixation>(fixes);

		//remove fixations not in viewport
		onStimuli.removeIf(fix -> fix.getX() < (0 - CodeStimuli.rowHeight));
		onStimuli.removeIf(fix -> fix.getX() > (1000 + CodeStimuli.rowHeight));
		onStimuli.removeIf(fix -> fix.getY() < (0 - CodeStimuli.rowHeight));
		onStimuli.removeIf(fix -> fix.getY() > (800 + CodeStimuli.rowHeight));
		onStimuli.removeIf(fix -> fix.getTrial().endsWith("V"));

		for(Fixation fix: onStimuli) {
			if(fix.getRowIdx() == null) {				
				//try to match to row
				codeDisplays.get(fix.getTrial()).matchFix(fix);
			}			
		}

		return onStimuli;
	}

	public static LinkedHashMap<String, Double> getLineMetrics(List<Fixation> fixes){
		List<Fixation> matched = new ArrayList<Fixation>(matchFixationsToRows(fixes));
		List<Fixation> matches = new ArrayList<Fixation>(matched) ;	
		matches.removeIf(f -> f.getRowIdx() == null);	//filter non matches

		//Return null if not enough fixations to form a saccade
		if(matches.size() < 2) {
			return null;
		}

		Double m = (double) matches.size();
		Double f = (double) matched.size();

		//exclude records with less than 50% unmatched fixations
		if(m/f < 0.5) {
			return null;
		}

		Map<String, AtomicInteger> counter = new LinkedHashMap<String, AtomicInteger>();
		counter.put("Vertical Next", new AtomicInteger(0));
		counter.put("Vertical Later", new AtomicInteger(0));
		counter.put("Horizontal Later", new AtomicInteger(0));
		counter.put("Regression Rate", new AtomicInteger(0));
		counter.put("Line Regression Rate", new AtomicInteger(0));

		Integer validMoves = 0;

		//For each fix
		Collections.sort(matches);
		Fixation lastFix = null;
		for(Fixation fix : matches) {
			if(lastFix != null) {
				validMoves = validMoves + 1;

				//check for next row
				if(fix.getRowIdx() >= lastFix.getRowIdx()) {
					//stayed on the same line or gone down
					counter.get("Vertical Later").incrementAndGet();

					if(fix.getRowIdx() == lastFix.getRowIdx() + 1 || fix.getRowIdx() == lastFix.getRowIdx()) {
						//stayed on the same line or went down 1 line
						counter.get("Vertical Next").incrementAndGet();
					}

					if(fix.getRowIdx() == lastFix.getRowIdx()) {
						if(fix.getX() >= lastFix.getX()) {
							//later on the same line
							counter.get("Horizontal Later").incrementAndGet();
						}else if(fix.getX() < lastFix.getX()){
							//regression on the same line
							counter.get("Regression Rate").incrementAndGet();
							counter.get("Line Regression Rate").incrementAndGet();
							
							//not forward reading
							counter.get("Vertical Later").decrementAndGet();
							counter.get("Vertical Next").decrementAndGet();
						}
					}
				}else{
					//vertical regression
					counter.get("Regression Rate").incrementAndGet();
				}				
			}			

			lastFix = fix;
		}

		//convert to percentage
		LinkedHashMap<String, Double> metrics = new LinkedHashMap<String, Double>();

		metrics.put("Vertical Next", Double.valueOf(counter.get("Vertical Next").get()) / validMoves * 100);
		metrics.put("Vertical Later",  Double.valueOf(counter.get("Vertical Later").get()) / validMoves * 100);
		metrics.put("Horizontal Later",  Double.valueOf(counter.get("Horizontal Later").get()) / validMoves * 100);
		metrics.put("Regression Rate",  Double.valueOf(counter.get("Regression Rate").get()) / validMoves * 100);
		metrics.put("Line Regression Rate",  Double.valueOf(counter.get("Line Regression Rate").get()) / validMoves * 100);

		return metrics;
	}
	
	/*
	 * Eucledian distance algorithm
	 */
	public static Double eucledianDistance(Double x1, Double y1, Double x2, Double y2) {
		return Math.sqrt(Math.pow(Math.abs(x2-x1), 2) + Math.pow(Math.abs(y2-y1), 2));
	}
	
	public static Map<String,Double> wilcoxon(List<Double> A, List<Double> B) throws Exception{
		Double[] s1 = A.toArray(new Double[A.size()]);
		Double[] s2 = B.toArray(new Double[B.size()]);
		
		//Load R script
		RenjinScriptEngine engine = new RenjinScriptEngine();
		String code = FileUtils.readFileToString(new File("WilcoxonR.txt"));
		
		engine.put("s1", s1);
		engine.put("s2", s2);
		engine.eval(code);
		
		ListVector result = (ListVector) engine.eval("wilcoxon(s1, s2, paired=TRUE, exact = FALSE)");
				
		Map<String,Double> results = new HashMap<String, Double>();
		results.put("p", Util.round(result.getElementAsDouble(2), 2));
		results.put("z", Util.round(result.getElementAsDouble(6), 2));
		results.put("V", Util.round(result.getElementAsDouble(0), 2));		
		
		return results; 
	}
	
	/*
	 * Calculate the mean and sample standard deviation of a list of  doubles
	 */
	public static Map<String, Double> getDoubleMeanAndSD(List<Double> data){
		Map<String, Double> result = new HashMap<String, Double>();
		
		if(data.size() == 0) {
			result.put("Mean", 0D);
			result.put("SD", 0D);
			return result;
		}
		
		Double mean = 0D;		
		for(Double d : data) {
			mean = mean + d;
		}
		mean = mean / data.size();
		result.put("Mean", mean);
		
		Double SD = 0D;
		for(Double d : data) {
			SD = SD + Math.pow(d - mean, 2);
		}
		SD = Math.sqrt(SD / (data.size() > 1 ? data.size()-1 : data.size()));
		result.put("SD", SD);
		
		return result;
	}
}
