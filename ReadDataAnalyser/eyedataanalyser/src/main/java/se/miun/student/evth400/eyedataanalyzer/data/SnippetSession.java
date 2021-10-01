package se.miun.student.evth400.eyedataanalyzer.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import se.miun.student.evth400.eyedataanalyzer.tools.Util;

public class SnippetSession extends GazeDataSession {
	private String answer;
	
	private List<Gaze> rawGazeData;
	private List<Gaze> normalizedGazeData;
	
	private Double adjustmentX;
	private Double adjustmentY;
	
	public static int normHeight = 800;
	public static int normWidth = 1000;
	
	/*
	 * Constructor, takes all data members
	 */
	public SnippetSession(String snip, String ans, Double x, Double y, Double h, Double w, List<Gaze> reads,  String dateTime) throws ParseException {
		this.sessionName = snip;
		this.answer = ans;
		
		this.dispHeight = h;
		this.dispWidth = w;
		this.dispX = x;
		this.dispY = y;
		
		//Sort the gaze data is in chronological order
		Collections.sort(reads);
		
		this.rawGazeData = reads;
		this.normalizedGazeData = this.normalize(reads);
		
		this.conclusionTime = Util.getDateFromString(dateTime);
	}
	
	public Double adjustX(Double adj) {
		this.adjustmentX = this.adjustmentX + adj;
		return this.adjustmentX;
	}
	
	public Double adjustY(Double adj) {
		this.adjustmentY = this.adjustmentY + adj;
		return this.adjustmentY;
	}
	
	/*
	 * Get response time
	 * @return 	milliseconds
	 */
	public Long getResponseTime() {	
		//whole duration
		Long dur = this.rawGazeData.get(this.rawGazeData.size() -1).getTimeStamp()
				- this.rawGazeData.get(0).getTimeStamp();
		
		//Look for pause in the data (Assumed paused to display help button prompt)
		Long startGap = this.rawGazeData.get(0).getTimeStamp();
		for(Gaze g : this.rawGazeData) {
			if(g.getTimeStamp() - startGap > 999) {		//anything over  a second
				dur = dur - (g.getTimeStamp() - startGap);
			}
			startGap = g.getTimeStamp();
		}
		
		return dur;
	}
	
	/*
	 * Calculates the sample frequency
	 */
	public Long getSampleFrequenzy() {
		return (long) (1000D / (this.getResponseTime() / this.rawGazeData.size()));
	}
	
	/*
	 * Calculates the signal precision STD
	 */
	
	/*
	 * Normalized the gaze data to the resolution of the original images 1000x800
	 */
	private List<Gaze> normalize(List<Gaze> readings){
		//make new list
		List<Gaze> normalized = new ArrayList<Gaze>();

		Long firstTimeStamp = readings.get(0).getTimeStamp() - 1;
		
		for(Gaze g : readings) {
			Double normX = g.getX();
			Double normY = g.getY();

			//move
			normX = normX - this.dispX;
			normY = normY - this.dispY;

			//make to percentage
			Double relativeX = normX / this.dispWidth;
			Double relativeY = normY / this.dispHeight;

			//normalize to 1000x800 for snippet comparison
			normX = 1000 * relativeX;
			normY = 800 * relativeY;
			
			//normalize time stamp to start from 0
			Long nTmSp = g.getTimeStamp() - firstTimeStamp;

			//add to list
			normalized.add(new Gaze(this.getSessionName() ,normX, normY, nTmSp));			
		}

		return normalized;
	}	
	
	/*
	 * Check if given answer is correct
	 * @returns		Boolean
	 */
	public Boolean isAnswerCorrect() {
		String stripped = this.answer.toLowerCase().replaceAll("\\s", "");
		
		 switch (this.sessionName){
	        case "1I":
	            //Printer = "aborted"
	        	if(Arrays.asList("aborted").contains(stripped)){
	               return true;
	            }
	            return false;
	        case "2I":
	            //IntStatistics	 = 5
	            return stripped.equals("5") ? true: false;
	        case "3I":
	            //CheckIfLettersOnly = No
	            return stripped.equals("no") ? true: false;
	        case "4I":
	            //InsertSort = 7543
	            return stripped.equals("7543") ? true: false;
	        case "5I":
	            //ReverseString = dlrow olleH
	            return stripped.equals("dlrowolleh") ? true: false;
	        case "6I":
	            //Count
	            return stripped.equals("3") ? true: false;
	        case "7I":
	            //InheritAnimal = RuffRuff
	            return stripped.equals("ruffruff") ? true: false;
	        case "8I":
	            //Triangle
	            return stripped.equals("4.5") ? true: false;
	        case "9I":
	            //SumArray = 23
	            return stripped.equals("23") ? true: false;
	        case "10I":
	            //Transform
	            return stripped.equals("5343") ? true: false;
	        case "11I":
	            //Sort
	            return stripped.equals("1459") ? true: false;
	        case "12I":
	            //Remove
	            return stripped.equals("1102345") ? true: false;
	        case "13I":
	            //Box
	           return stripped.equals("284125") ? true: false;
	        case "14I":
	            //Numbers
	            return stripped.equals("67") ? true: false;
	        case "15I":
	            //ListValue
	            return stripped.equals("752") ? true: false;
	        case "1R":
	            //Ambiguous
	            return stripped.equals("catcatcatcat") ? true: false;
	        case "2R":
	            //ColorSub
	            return stripped.equals("345") ? true: false;
	        case "3R":
	            //DistNum
	            return stripped.equals("onethreefour") ? true: false;
	        case "4R":
	            //TwiceScheduled
	            return stripped.equals("sum:691") ? true: false;
	        case "5R":
	            //ThreeDiv
	            return stripped.equals("3691215") ? true: false;
	        case "6R":
	            //StopAndUnsubscribe
	            return stripped.equals("123") ? true: false;
	        case "7R":
	            //Groceries
	            return stripped.equals("milktuna") ? true: false;
	        case "8R":
	            //Group
	            return stripped.equals("3") ? true: false;
	        case "9R":
	            //Stop
	            return stripped.equals("readyset") ? true: false;
	        case "10R":
	            //SameTime
	            return stripped.equals("0123344") ? true: false;
	        case "11R":
	            //Rectangle
	            return stripped.equals("100100") ? true: false;
	        case "12R":
	            //Range
	            return stripped.equals("21") ? true: false;
	        case "13R":
	            //PrimeNumbers
	            return stripped.equals("11131719") ? true: false;
	        case "14R":
	            //ObserverNumbers
	            return stripped.equals("3359fail") ? true: false;
	        case "15R":
	            //ParallellColor
	            return stripped.equals("1red2blue3orange") ? true: false;
	        case "1S":
	            //What did the frog offer to offer for the princess?
	        	if(Arrays.asList("goldenball", "goldball", "ball", "plaything", "toy",
	        			"thegoldenball","agoldeball", "hergoldenball").contains(stripped)){
	               return true;
	            }
	            return false;
	        case "2S":
	            //How does the mother describe the wolfs voice?
	            if (Arrays.asList("rough", "roughvoice","roughvoiceandblackfeet").contains(stripped)) {
	                return true;
	            }
	            return false;
	        case "3S":
	            //What did Rapunzel do to get the king's sons attention?
	            if (Arrays.asList("shesang", "sang", "song","sing").contains(stripped)) {
	                return true;
	            }
	            return false;
	        case "4S":
	            //What did the brother turn into when he drank the water?
	            if (Arrays.asList("aroe", "roe", "deer", "animal").contains(stripped)) {
	                return true;
	            }
	            return false;
	        case "5S":
	            //What two items did the girl give away?
	            if (Arrays.asList("breadhood", "hoodbread", "breadandhood", "hoodandbread",
	            		"herpieceofbreadandherhood", "pieceofbreadhood",
	            		"pieceofbreadandherhood").contains(stripped)) {
	                return true;
	            }
	            return false;
	        case "6S":
	            //How many ravens flew down to him?
	            if (Arrays.asList("3", "three").contains(stripped)) {
	                return true;
	            }
	            return false;
	        case "7S":
	            //What were the windows made out of?
	            if (Arrays.asList("sugar", "clearsugar").contains(stripped)) {
	                return true;
	            }
	            return false;
	        case "8S":
	            //What did the evil sisters empty into the ashes?
	            if (Arrays.asList("peaslentils", "peasandlentils", "lentilspeas", "lentilsandpeas"
	            		,"herpeasandlentils").contains(stripped)) {
	                return true;
	            }
	            return false;
	        case "9S":
	            //What three trades did the sons decide to learn?
	            return stripped.equals("barber") ? true : false;
	        case "10S":
	            //To what town did the animals set out to become musicians?
	            if (Arrays.asList("bremen", "beemen").contains(stripped)) {
	                return true;
	            }
	            return false;
	        case "11S":
	            //What did Little Red-Cap bring to her grandmother?
	            if (Arrays.asList("cakewine", "cakeandwine", "winecake", "wineandcake",
	            		"cakeandbottleofwine", "apieceofcakeandabottleofwine",
	            		"cakeandabottleofwine").contains(stripped)) {
	                return true;
	            }
	            return false;
	        case "12S":
	            //What was the old woman doing on the ground?
	            if (Arrays.asList("cuttinggrass", "cutgrass", "kneeling",
	            		"cuttinggrasswithasickle").contains(stripped)) {
	                return true;
	            }
	            return false;
	        case "13S":
	            //What did the Little man turn the straw into?
	            return stripped.equals("gold") ? true : false;
	        case "14S":
	            //What was the fish made entirely out of?
	            return stripped.equals("gold") ? true : false;
	        case "15S":
	            //What was the Ash-Maidens own shoes made out of?
	            if (Arrays.asList("wood", "heavywood", "wooden").contains(stripped)) {
	                return true;
	            }
	            return false;
	    }

		return false;
	}
	
	/*
	 * Get submitted answer
	 */
	public String getAnswer() {
		return this.answer;
	}
	
	/*
	 * @return the raw gaze data
	 */
	public List<Gaze> getRawGazeData(){
		return this.rawGazeData;
	}
	
	/*
	 * @return the normalized gaze data (for all further data processing)
	 */
	public List<Gaze> getNormalizedGazeData(){
		return this.normalizedGazeData;
	}
	
	public Map<String, String> getSummary(){
		Map<String, String> sum = new LinkedHashMap<String, String>();
		
		sum.put("Snippet: ", getSessionName());
		sum.put("Answer", getAnswer() + " (" + isAnswerCorrect() + ")");
		
		return sum;
	}
}
