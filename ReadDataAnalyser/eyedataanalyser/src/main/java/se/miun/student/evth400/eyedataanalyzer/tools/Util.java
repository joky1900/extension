package se.miun.student.evth400.eyedataanalyzer.tools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.miun.student.evth400.eyedataanalyzer.data.Fixation;
import se.miun.student.evth400.eyedataanalyzer.data.Gaze;
import se.miun.student.evth400.eyedataanalyzer.data.SubjectData;

public class Util {
	/*
	 * Rounds a double to specified number of decimal points
	 */
	public static double round(double value, int decPoints) {
	    if (decPoints < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(decPoints, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	/*
	 * Parses an object when it is not known if it's a Long or a Double
	 */
	public static Double getLongOrDouble(Object obj) {
		try {
			return (Double) obj;
		}catch(ClassCastException ex) {
			Long t = (Long) obj;
			return t.doubleValue();
		}	
	}
	
	/*
	 * Date String parser
	 */
	public static Date getDateFromString(String dt) throws ParseException {
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.parse(dt);
	}
	
	/*
	 * Loads all subjects at the same time
	 */
	public static List<SubjectData> getAllSubjectData(File folder) {
		List<SubjectData> subData = new ArrayList<SubjectData>();
		
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            subData.addAll(getAllSubjectData(fileEntry));
	        } else {
	        	try {
					subData.add(new SubjectData(fileEntry.getAbsolutePath()));
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Failed to load subject file: " + fileEntry);
				}
	        }
	    }
		
		return subData;
	}
	
	/*
	 * Converts to gaze list to a CSV String (for Saccades algorithm input)
	 */
	public static String gazeToCsv(List<Gaze> gaze) {
		StringBuilder sb = new StringBuilder();
		sb.append("time,x,y,trial\n");	  

		//for every eye gaze
		for(Gaze g: gaze) {
			sb.append(g.getTimeStamp()).append(",");
			sb.append(g.getX()).append(",");
			sb.append(g.getY()).append(",");
			sb.append(g.getTrial()).append("\n");
		}

		return sb.toString();
	}
	
	/*
	 * Converts the gaze list into tab delimiter String mapped to snippet name(trial)
	 */
	public static Map<String, String> gazeToTabDelim(Map<String, List<Gaze>> gaze){
		Map<String, String> tabStrings = new HashMap<String, String>();
		
		for(Entry<String, List<Gaze>> ent : gaze.entrySet()) {
			StringBuilder sb = new StringBuilder();
			
			//heading 
			sb.append("time").append("\t");
			sb.append("x").append("\t");
			sb.append("y").append(System.lineSeparator());
			
			//do each gaze reading
			for(Gaze g: ent.getValue()) {
				sb.append(g.getTimeStamp()).append("\t");
				sb.append(g.getX()).append("\t");
				sb.append(g.getY()).append(System.lineSeparator());
			}
			
			//add to map
			tabStrings.put(ent.getKey(), sb.toString());
		}
		
		return tabStrings;
	}
	
	/*
	 * Parses the matlab result files
	 */
	public static List<Fixation> parseFixationTAB(File file){
		List<Fixation> fixes = new ArrayList<>();
		String allData = null;
		
		try {
			allData = FileUtils.readFileToString(file);
		} catch (IOException e) {			
			System.out.println(e.getMessage());
			return null;
		}
		
		String[] lines = allData.split("\n");
		
		for(int i = 1 ; i < lines.length ; i++) {
			String oneFix = lines[i].replace("\r", "");
			String[] data = oneFix.split("\t");
			
			Double st = Double.parseDouble(data[0]);
			Double ed = Double.parseDouble(data[1]);
			Double dur = Double.parseDouble(data[2]);
			
			if(dur < 150) {
				//Filter fixations less than 150 ms because could not be accurately detected
				continue;
			}
			
			fixes.add(new Fixation(data[13], 
					st.longValue(), 
					ed.longValue(),
					Double.parseDouble(data[3]), Double.parseDouble(data[4]),
					Double.parseDouble(data[10]), Double.parseDouble(data[11]),
					0D, 0D,
					dur.longValue(), "fixation"));
			
		}
		
		return fixes;
	}
	
	/*
	 * Parses the Saccades file
	 */
	public static List<Fixation> parseFixationXML(File XML){
		String allXML = null;
		try {
			allXML = FileUtils.readFileToString(XML);
		} catch (IOException e1) {
			System.out.println("Matlab data needed for: " + XML);
			return null;
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			StringBuilder xmlStringBuilder = new StringBuilder();
			xmlStringBuilder.append(allXML);
			ByteArrayInputStream input = new ByteArrayInputStream(
					xmlStringBuilder.toString().getBytes("UTF-8"));
			doc = builder.parse(input);
		} catch (Exception e) {
			e.printStackTrace();
		}

		NodeList columns = doc.getElementsByTagName("variable");

		Map<String, List<String>> stringDataset = new HashMap<String, List<String>>();
		int rows = 0;

		for(int i = 0; i < columns.getLength(); i++) {
			Node n = columns.item(i);
			Element nE = (Element) n;

			String nodeName = nE.getAttribute("name");
			List<String> values = new ArrayList<String>();

			NodeList columnValues = nE.getElementsByTagName("v");

			for(int c = 0; c < columnValues.getLength(); c++) {
				Node child = columnValues.item(c);
				values.add(child.getTextContent());

				if(c > rows) {
					rows = c;
				}
			}

			stringDataset.put(nodeName, values);
		}

		//Create fixation objects
		List<Fixation> fixations = new ArrayList<Fixation>();
		for(int i = 0; i <= rows; i++) {
			if(Long.parseLong(stringDataset.get("dur").get(i)) < 150) {
				//Do not count fixations shorter than 200ms
				continue;
			}
			
			fixations.add(new se.miun.student.evth400.eyedataanalyzer.data.Fixation(stringDataset.get("trial").get(i), 
					Long.parseLong(stringDataset.get("start").get(i)), 
					Long.parseLong(stringDataset.get("end").get(i)), 
					Double.parseDouble(stringDataset.get("x").get(i)), 
					Double.parseDouble(stringDataset.get("y").get(i)),
					Double.parseDouble(stringDataset.get("mad_x").get(i)), 
					Double.parseDouble(stringDataset.get("mad_y").get(i)), 
					Double.parseDouble(stringDataset.get("peak_vy").get(i)), 
					Double.parseDouble(stringDataset.get("peak_vx").get(i)), 
					Long.parseLong(stringDataset.get("dur").get(i)), 
					stringDataset.get("event").get(i)));
		}
		
		return fixations;
	}
	
	/*
	 * Turns string into HTML for formatting purposes
	 */
	public static String lineBreakHTML(String string){
		return "<html>" + string.replace(" ", "</br>") + "</html>";
		
	}
	
	/*
	 * Alternative way of displaying a list in String format
	 */
	public static String prettyList(ArrayList<Double> lst) {
		String ugly = lst.toString();
		
		ugly = ugly.replace("[", "").replace("]", "");
		ugly = ugly.replace(", ", System.lineSeparator());
		
		return ugly ;
	}
	
	/*
	 * Averages a list of metric maps
	 */
	public static LinkedHashMap<String, Double> averageMetrics(List<LinkedHashMap<String, Double>> metricsList){
		LinkedHashMap<String, Double> metrics = new LinkedHashMap<String, Double>();

		if(metricsList.size() == 0) {
			return metrics;
		}

		Double allVN = 0D;
		Double allVL = 0D;
		Double allHL = 0D;
		Double allRR = 0D;
		Double allLRR = 0D;

		for(Map<String, Double> oneMetricUnit : metricsList) {
			if(oneMetricUnit != null) {
				allVN = allVN + oneMetricUnit.get("Vertical Next");
				allVL = allVL + oneMetricUnit.get("Vertical Later");
				allHL = allHL + oneMetricUnit.get("Horizontal Later");
				allRR = allRR + oneMetricUnit.get("Regression Rate");
				allLRR = allLRR + oneMetricUnit.get("Line Regression Rate");
			}
		}

		metrics.put("Vertical Next", Util.round(allVN / metricsList.size(), 2));
		metrics.put("Vertical Later", Util.round(allVL / metricsList.size(), 2));
		metrics.put("Horizontal Later", Util.round(allHL / metricsList.size(), 2));
		metrics.put("Regression Rate", Util.round(allRR / metricsList.size(), 2));
		metrics.put("Line Regression Rate", Util.round(allLRR / metricsList.size(), 2));

		return metrics;
	}
}
