package se.miun.student.evth400.eyedataanalyzer.data;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import se.miun.student.evth400.eyedataanalyzer.tools.Util;

public class SubjectData{
	private String subjectID;
	public int index;

	//Participant related
	private String origin;
	private String gender;
	private Integer age;
	private String firstLanguage;
	private Boolean hasGlasses = false;
	private Boolean hasEyeCondition = false;
	private Boolean hasBigLashes = false;
	private String javaExperience;
	private String reactiveExperience;
	private String comments;
	private String feedback;

	//System related
	private String userAgent;
	private String webCameraLabel;	
	private Long cores;	
	private String systemLanguage;

	private Double cameraFrameRate;		//These seems to all be the same, probably set by the script
	private Long videoWidth;
	private Long videoHeight;

	//Eye gaze data
	private List<String> snippetOrder;
	private List<String> snippetsLeft;
	private List<GazeDataSession> eyeGazeSessions;

	//for holding processed data
	private ProcessedData procData;

	/*
	 * ============= Getters ===================================================================
	 */
	public String getComments() { return this.comments;	}
	public Integer getSnippetsDone() {return getSnippetSessions().size();}
	public Integer getTextDone() {
		List<SnippetSession> lst= new ArrayList<SnippetSession>(getSnippetSessions());
		lst.removeIf(snip -> !snip.getSessionName().endsWith("S"));
		return lst.size();
	}
	public Integer getImpDone() {
		List<SnippetSession> lst= new ArrayList<SnippetSession>(getSnippetSessions());
		lst.removeIf(snip -> !snip.getSessionName().endsWith("I"));
		return lst.size();
	}
	public Integer getReactDone() {
		List<SnippetSession> lst= new ArrayList<SnippetSession>(getSnippetSessions());
		lst.removeIf(snip -> !snip.getSessionName().endsWith("R"));
		return lst.size();
	}
	public Integer getAge() {return this.age;}
	public String getGender() {return this.gender;}
	public String getJavaExperience() {return this.javaExperience;}
	public String getReactiveExperience() {return this.reactiveExperience;}
	public Boolean hasGlasses() {return this.hasGlasses;}
	public Boolean hasEyeCondition() {return this.hasEyeCondition;}
	public Boolean hasThickLashes() {return this.hasBigLashes;}
	public String getSubjectID() {return this.subjectID;}
	public List<String> getSnippetOrder(){return this.snippetOrder;}
	public String getFirstLanguage() {return this.firstLanguage;}
	public String getUserAgent() {return this.userAgent;}
	public String getOrigin() {return this.origin;}
	public Long getNumberOfCores() {return this.cores;}
	public String getSystemLanguage() {return this.systemLanguage;}
	public String getCameraLabel() {return this.webCameraLabel;}
	public String getFeedback() {return this.feedback;}


	//Get all snippet sessions. Returns all sessions which are snippet answers
	public List<SnippetSession> getSnippetSessions(){
		List<SnippetSession> sSessions = new ArrayList<SnippetSession>();

		for(GazeDataSession gDS : eyeGazeSessions) {
			if(gDS instanceof SnippetSession) {
				sSessions.add((SnippetSession) gDS);
			}
		}

		return sSessions;
	}

	//Get all validations. Returns all session which are validations
	public List<ValidationSession> getValidations(){
		List<ValidationSession> validations = new ArrayList<ValidationSession>();

		for(GazeDataSession gDS : eyeGazeSessions) {
			if(gDS instanceof ValidationSession) {
				validations.add((ValidationSession) gDS);
			}
		}

		return validations;
	}

	public ValidationSession getValidationSession(String sessionName) {
		for(ValidationSession val : getValidations()) {
			if(val.getSessionName().equals(sessionName)) {
				return val;
			}
		}
		return null;
	}

	public SnippetSession getSnippetSession(String snippetName) {
		for(SnippetSession snip: getSnippetSessions()) {
			if(snip.getSessionName().equals(snippetName)) {
				return snip;
			}
		}
		return null;
	}

	public ProcessedData getProcessedData() {
		return this.procData;
	}

	/*
	 * @return the average sample frequency of all snippet sessions 
	 */
	public Integer getSampleFrequenzy() {
		if(getSnippetSessions().size() == 0) {
			if(getValidations().size() != 0) {
				//get validation freq instead for comparison !missleading too short to get accurate result
				Long allHz = 0L;
				for(ValidationSession val: getValidations()) {
					allHz = allHz + val.getSampleFrequency();
				}

				return (int) (allHz / getValidations().size());
			}else {
				return 0;
			}
		}

		Long allHz = 0L;
		for(SnippetSession ses: getSnippetSessions()) {
			allHz = allHz + ses.getSampleFrequenzy();
		}

		return (int) (allHz / getSnippetSessions().size());
	}

	/*
	 * Returns number of correct answers
	 */
	public Integer getCorrectAnswers() {
		Integer correct = 0;

		for(SnippetSession snippet: this.getSnippetSessions()) {
			if(snippet.isAnswerCorrect()) {
				correct++;
			}
		}

		return correct;
	}

	public Integer getCorrectReactiveSnippets() {
		int cor = 0;
		for(SnippetSession ses: this.getSnippetSessions()) {
			if(ses.isAnswerCorrect() && ses.getSessionName().matches(".*R")
					&& ses.getResponseTime() > 15) {
				cor++;
			}
		}

		return cor;
	}

	public Integer getCorrectImperativeSnippets() {
		int cor = 0;
		for(SnippetSession ses: this.getSnippetSessions()) {
			//filter snippet response time < 20 seconds
			if(ses.isAnswerCorrect() && ses.getSessionName().matches(".*I")
					&& ses.getResponseTime() > 15) {
				cor++;
			}
		}

		return cor;
	}

	public Integer getCorrectStorySnippets() {
		int cor = 0;
		for(SnippetSession ses: this.getSnippetSessions()) {
			//filter snippet response time < 20 seconds
			if(ses.isAnswerCorrect() && ses.getSessionName().matches(".*S")
					&& ses.getResponseTime() > 15) {
				cor++;
			}
		}

		return cor;
	}

	/*
	 * Experiment completion 
	 */
	public Boolean isFinnishedTest() {
		return this.snippetsLeft.size() > 0 ? false : true;
	}

	public Boolean isStartedTest() {
		return this.getSnippetSessions().size() > 0 ? true : false;
	}

	public Long getExperimentTime() {
		if(eyeGazeSessions.size() > 0) {
			long diffInMillies = Math.abs(eyeGazeSessions.get(eyeGazeSessions.size() - 1).getConclusionTime().getTime() 
					- eyeGazeSessions.get(0).getConclusionTime().getTime());

			//Add first calibration duration
			ValidationSession firstCalib = (ValidationSession) eyeGazeSessions.get(0);
			diffInMillies = diffInMillies + firstCalib.getCalibrationTime();		

			return TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
		}
		return 0L;
	}	

	/*
	 * Error rates
	 */
	public Double getAverageFixationError() {
		Double error = 0D;
		for(ValidationSession calibration : this.getValidations()) {
			error = error + calibration.getAverageFixationError();
		}

		return error / this.getValidations().size();
	}

	public Double getAverageGazeError() {
		Double error = 0D;
		for(ValidationSession calibration : this.getValidations()) {
			error = error + calibration.getAverageGazeError();
		}

		return error / this.getValidations().size();
	}
	
	public Double getAverageFixationErrorDegrees() {
		Double error = 0D;
		for(ValidationSession calibration : this.getValidations()) {
			error = error + calibration.getAverageFixationErrorDegrees();
		}

		return error / this.getValidations().size();
	}

	public Double getAverageGazeErrorDegrees() {
		Double error = 0D;
		for(ValidationSession calibration : this.getValidations()) {
			error = error + calibration.getAverageGazeErrorDegrees();
		}

		return error / this.getValidations().size();
	}

	public Double getRelativeFixationError() {
		Double error = 0D;
		for(ValidationSession val : this.getValidations()) {
			error = error + val.getRelativeFixationError();
		}

		return Util.round(error / this.getValidations().size(), 2);
	}
	
	public Double getRelativeGazeError() {
		Double error = 0D;
		for(ValidationSession val : this.getValidations()) {
			error = error + val.getRelativeGazeError();
		}

		return Util.round(error / this.getValidations().size(), 2);
	}

	/*
	 * Return gaze data
	 */
	public Map<String, List<Gaze>> getAllNormalizedGazeData(){
		Map<String, List<Gaze>> allGaze = new HashMap<String, List<Gaze>>();

		for(SnippetSession snip : this.getSnippetSessions()) {
			allGaze.put(snip.getSessionName(), snip.getNormalizedGazeData());
		}

		return allGaze;
	}

	public Map<String, Map<Integer, List<Gaze>>> getAllValidationGazeData(){
		Map<String, Map<Integer, List<Gaze>>> allGaze = new HashMap<String, Map<Integer, List<Gaze>>>();
		
		//validation points are not normalized
		for(ValidationSession val : this.getValidations()) {
			allGaze.put(val.getSessionName(),val.getAllGazeData());
		}

		return allGaze;
	}

	public Map<String, List<Gaze>> getAllRawGazeData(){
		Map<String, List<Gaze>> allGaze = new HashMap<String, List<Gaze>>();

		for(SnippetSession snip : this.getSnippetSessions()) {
			allGaze.put(snip.getSessionName(), snip.getNormalizedGazeData());
		}

		return allGaze;
	}

	public List<ValidationPoint> getAllValidationPoints(){
		List<ValidationPoint> points = new ArrayList<ValidationPoint>();

		for(ValidationSession val : this.getValidations()) {
			points.addAll(val.getAllValidationPoints());
		}

		return points;
	}


	/*
	 * ============== Setters ==================================================================
	 */
	public void setIndex(int index) { this.index = index; }

	/*------------------------------------------------------------------------------------------
	 * Constructor
	 * Loads and parses a JSON file
	 * 
	 * @param		filePath		path to JSON file
	 -------------------------------------------------------------------------------------------*/	
	public SubjectData(String filePath) throws Exception{
		//get the objectID
		String[] f = filePath.split("\\\\");
		this.subjectID = f[f.length -1].replace(".json", "");

		this.eyeGazeSessions = new ArrayList<GazeDataSession>();

		//parsing file
		Object obj = new JSONParser().parse(new FileReader(filePath));

		//casting obj to JSONObject
		JSONObject jo = (JSONObject) obj;

		//get main objects
		JSONObject subjectDetails = (JSONObject) jo.get("subjectDetails");
		JSONArray snippetOrder =  (JSONArray) jo.get("snippetOrder");
		JSONArray snippetsLeft = (JSONArray) jo.get("snippetsLeft");
		JSONArray validations =  (JSONArray) jo.get("calibrations");
		JSONArray answers =  (JSONArray) jo.get("answers");
		JSONObject Technical = (JSONObject) jo.get("Technical");
		this.feedback = (String) jo.get("Feedback");
		if(this.feedback == null) {
			this.feedback = "";
		}

		//Get subject details
		this.origin = ((String) subjectDetails.get("origin")).trim();
		this.gender = (String) subjectDetails.get("gender");
		this.age = Integer.parseInt((String) subjectDetails.get("age"));
		String glasses = (String) subjectDetails.get("hasGlasses");
		if(glasses != null) {
			this.hasGlasses = true;
		}
		String eyeCond = (String) subjectDetails.get("hasEyeCond");
		if(eyeCond != null) {
			this.hasEyeCondition = true;
		}
		String tLash = (String) subjectDetails.get("bigLashes");
		if(tLash != null) {
			this.hasBigLashes = true;
		}
		this.javaExperience = (String) subjectDetails.get("javaExp");
		this.reactiveExperience = (String) subjectDetails.get("reactExp");
		this.comments = (String) subjectDetails.get("comments");

		//get technical details
		if(Technical != null) {
			this.userAgent = (String) Technical.get("Agent");
			this.cores = (Long) Technical.get("Cores");
			this.systemLanguage = (String) Technical.get("Language");

			JSONObject camera = (JSONObject) Technical.get("Camera");
			this.webCameraLabel = (String) camera.get("cameraLabel");
			this.cameraFrameRate = Util.getLongOrDouble(camera.get("frameRate"));
			this.videoWidth = (Long) camera.get("height");
			this.videoHeight = (Long) camera.get("width");
		}		

		//snippet order
		this.snippetOrder = new ArrayList<String>();
		Iterator<?> itr = snippetOrder.iterator();        
		while (itr.hasNext()) 
		{
			this.snippetOrder.add((String) itr.next());
		}

		this.snippetsLeft = new ArrayList<String>();
		itr = snippetsLeft.iterator();
		while(itr.hasNext()) {
			this.snippetsLeft.add((String) itr.next());
		}
		
		//Validations
		Iterator<?> valItr = validations.iterator();
		while(valItr.hasNext()) {
			JSONObject valid = (JSONObject) valItr.next();

			//time
			String dateTime = (String) valid.get("time");

			//dimensions
			JSONObject dimensions = (JSONObject) valid.get("dimensions");
			Double w = Util.getLongOrDouble(dimensions.get("w"));
			Double h = Util.getLongOrDouble(dimensions.get("h"));
			Double x = Util.getLongOrDouble(dimensions.get("x"));
			Double y = Util.getLongOrDouble(dimensions.get("y"));

			//readings
			List<ValidationPoint> rdngs = new ArrayList<ValidationPoint>();
			JSONArray readings = (JSONArray) valid.get("readings");
			Iterator<?> readItr = readings.iterator();

			while(readItr.hasNext()) {
				JSONObject reading = (JSONObject) readItr.next();

				//Point position
				Double pX = Util.getLongOrDouble(reading.get("xPos"));
				Double pY = Util.getLongOrDouble(reading.get("yPos"));

				//Point number
				Long pNo = (Long) reading.get("pointNumber");

				//Validation number
				Integer valNum = this.getValidations().size();

				//Gaze
				JSONArray gazes = (JSONArray) reading.get("gazeData");
				Iterator<?> gzeItr = gazes.iterator();
				List<Gaze> gaze = new ArrayList<Gaze>();
				while(gzeItr.hasNext()) {
					JSONObject g = (JSONObject) gzeItr.next();

					Double gX = Util.getLongOrDouble(g.get("x"));
					Double gY = Util.getLongOrDouble(g.get("y"));
					Long timestamp = Util.getLongOrDouble(g.get("timeStamp")).longValue();

					gaze.add(new Gaze(valNum + 1 + "V", gX, gY, timestamp));
				}			

				rdngs.add(new ValidationPoint(pX, pY, gaze, pNo));
			}

			this.eyeGazeSessions.add(new ValidationSession(rdngs, x, y, h, w, dateTime));
		}

		//Answers
		Iterator<?> answerItr = answers.iterator();
		while(answerItr.hasNext()) {
			JSONObject answerData = (JSONObject) answerItr.next();

			//snippet
			String snippet = (String) answerData.get("snippet").toString();

			//time
			String dateTime = (String) answerData.get("time");

			//answer
			String answer = (String) answerData.get("answer").toString();

			//dimensions
			JSONObject dimensions = (JSONObject) answerData.get("dimensions");
			Double w = Util.getLongOrDouble(dimensions.get("w"));
			Double h = Util.getLongOrDouble(dimensions.get("h"));
			Double x = Util.getLongOrDouble(dimensions.get("x"));
			Double y = Util.getLongOrDouble(dimensions.get("y"));

			//readings
			List<Gaze> rdngs = new ArrayList<Gaze>();
			JSONArray readings = (JSONArray) answerData.get("gazeData");

			Iterator<?> gazeItr = readings.iterator();
			while(gazeItr.hasNext()) {
				JSONObject gaze = (JSONObject) gazeItr.next();

				Double gX = Util.getLongOrDouble(gaze.get("x"));
				Double gY = Util.getLongOrDouble(gaze.get("y"));
				Long timestamp = Util.getLongOrDouble(gaze.get("timeStamp")).longValue();

				rdngs.add(new Gaze(snippet, gX, gY, timestamp));
			}

			this.eyeGazeSessions.add(new SnippetSession(snippet, answer, x, y, h, w, rdngs, dateTime));
		}  

		//Sort experiment recordings
		Collections.sort(this.eyeGazeSessions);

		//Add validation session names
		for(int i = 0; i < getValidations().size(); i++) {
			getValidations().get(i).setValidationName(i + 1 + "V");
		}

		//Process/load the data
		if(getSnippetsDone() > 0 && getSampleFrequenzy() >= 10) {
			this.procData = new ProcessedData(getSubjectID(), getAllNormalizedGazeData(), getAllValidationGazeData(), getSampleFrequenzy());
		}
	}

	/*
	 * Returns a map of the data summary
	 */
	public Map<String,String> getSummary(){
		Map<String, String> sum = new LinkedHashMap<String, String>();

		//Origin
		sum.put("Origin:", getOrigin());

		//Age
		sum.put("Age:", getAge() + "yo");

		//Gender
		sum.put("Gender:", getGender());

		//Experience
		sum.put("First language:", getFirstLanguage());
		sum.put("Java exp:", getJavaExperience());
		sum.put("Reactive exp:", getReactiveExperience());

		//eye spec
		if(hasBigLashes || hasGlasses || hasEyeCondition) {
			if(hasGlasses) {
				sum.put(" ", "Glasses!");
			}else {
				sum.put(" ", "");
			}
			if(hasEyeCondition) {
				sum.put("  ", "Eye Condition!");
			}else {
				sum.put("  ", "");
			}
			if(hasBigLashes) {
				sum.put("   ", "Big Lashes!");
			}else {
				sum.put("   ", "");
			}
		}

		//result
		sum.put("Snippets done:", getSnippetsDone() + "");
		sum.put("Correct:", getCorrectAnswers() + "");
		sum.put("Completion time:", getExperimentTime() + " min");
		sum.put("of story:", getCorrectStorySnippets() + "");
		sum.put("of Java:", getCorrectImperativeSnippets() + "");
		sum.put("of RxJava:", getCorrectReactiveSnippets() + "");

		//stats
		sum.put("Samlpe frq", getSampleFrequenzy() + "Hz");
		sum.put("Fixation error:", Util.round(getAverageFixationError(),2) + "px");
		sum.put("Gaze error:", Util.round(getAverageGazeError(),2) + "px");


		sum.put("Relative fix error:", Util.round(getRelativeFixationError(),2) + "%");
		sum.put("Cores", getNumberOfCores() + "");

		//Comments
		sum.put("Comments:", getComments());

		return sum;
	}


	@Override
	public String toString() {
		return getAge() + " " + getGender() + " " + getReactiveExperience().split(" ")[0] + " " + getSubjectID();
	}
}
