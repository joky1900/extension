package se.miun.student.evth400.eyedataanalyzer.data;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import se.miun.student.evth400.eyedataanalyzer.tools.DataLabb;
import se.miun.student.evth400.eyedataanalyzer.tools.Util;

public class ValidationSession extends GazeDataSession implements Serializable{
	private static final long serialVersionUID = 1L;
	List<ValidationPoint> valPoints;
	
	//for normalized comparisons
	public static int normHeight = (int) (800L * 1.04);
	public int normWidth;
	
	/*
	 * Constructor
	 * @param takes all data members
	 */
	public ValidationSession(List<ValidationPoint> readings, Double x, Double y, Double h, Double w, String dateTime) throws ParseException {
		this.conclusionTime = Util.getDateFromString(dateTime);
		
		this.dispHeight = h;
		this.dispWidth = w;
		this.dispX = x;
		this.dispY = y;
		
		this.valPoints = readings;
		
		this.normWidth = (int) (normHeight * (this.dispWidth / this.dispHeight));
		
		//sort
		Collections.sort(this.valPoints);		
	}	
	
	/*
	 * Returns the list of all validation points for the validation session
	 */
	public List<ValidationPoint> getAllValidationPoints(){
		return this.valPoints;
	}
	
	/*
	 * Returns the average fixation error for the validation session
	 */
	public Double getAverageFixationError() {		
		Double errors = 0D;
		
		for(ValidationPoint pt : valPoints) {
			errors = errors + pt.getFixationError();
		}
		
		return Util.round(errors / valPoints.size(), 2);
	}
	
	/*
	 * Returns the average gaze error for the validation session
	 */
	public Double getAverageGazeError() {		
		Double errors = 0D;
		
		for(ValidationPoint pt : valPoints) {
			errors = errors + pt.getGazeError();
		}
		
		return Util.round(errors / valPoints.size(), 2);
	}
	
	/*
	 * Returns the relative error
	 */
	public Double getRelativeFixationError() {
		Double errors = 0D;
		
		//measure one point at the time
		for(ValidationPoint pnt: getAllValidationPoints()) {
			//relative validation coordinates
			Double normPntX = pnt.getPointX() / normWidth;
			Double normPntY = pnt.getPointY() / normHeight;
			
			Gaze avgFix = DataLabb.averageGazePoint(pnt.getGaze());
			
			Double avgX = avgFix.getX() / normWidth;
			Double avgY = avgFix.getY() / normHeight;
			
			//add to total error
			errors = errors + DataLabb.eucledianDistance(normPntX, normPntY, avgX, avgY);
		}
		
		return Util.round(errors/getAllValidationPoints().size(), 2);
	}
	
	public Double getRelativeGazeError() {
		Double errors = 0D;
		
		//measure one point at the time
		for(ValidationPoint pnt: getAllValidationPoints()) {
			//relative validation coordinates
			Double normPntX = pnt.getPointX() / normWidth;
			Double normPntY = pnt.getPointY() / normHeight;
			
			Double gError = 0D;
			for(Gaze g: pnt.getGaze()) {
				Double gX = g.getX() / normWidth;
				Double gY = g.getY() / normHeight;
				
				gError = gError + DataLabb.eucledianDistance(normPntX, normPntY, gX, gY);
			}
			
			errors = errors + (gError/pnt.getGaze().size());
		}
		
		return Util.round(errors/getAllValidationPoints().size(), 2);
	}
	
	/*
	 * Returns error in degrees (50cm from screen)
	 */
	public Double getAverageFixationErrorDegrees() {		
		Double errors = 0D;
		
		for(ValidationPoint pt : valPoints) {
			errors = errors + pt.getFixationErrorDegrees();
		}
		
		return Util.round(errors / valPoints.size(), 2);
	}
	
	public Double getAverageGazeErrorDegrees() {		
		Double errors = 0D;
		
		for(ValidationPoint pt : valPoints) {
			errors = errors + pt.getGazeErrorDegrees();
		}
		
		return Util.round(errors / valPoints.size(), 2);
	}
	
	/*
	 * gets the number of validation points (should always be 7)
	 */
	public Integer getNumberOfPoints() {
		return this.valPoints.size();
	}
	
	/*
	 * Returns boolean indicating if calibration was validated successfully
	 */
	public Boolean passedThresholdHold() {
		if(this.getAverageFixationError() < (800 / 3)) {
			return true;
		}else {
			return false;
		}
	}
	
	/*
	 * Gets the total time of the session in ms
	 */
	public Long getCalibrationTime() {
		//get number of points
		Long noPoints = this.getAllValidationPoints().get(this.getAllValidationPoints().size()-1).getPointNo();
		
		//average point time (will vary depending on processor speed)
		Long totValTime = 0L;
		for(ValidationPoint pt: this.getAllValidationPoints()) {
			totValTime = totValTime + pt.getGaze().get(pt.getGaze().size() - 1).getTimeStamp()
					- pt.getGaze().get(0).getTimeStamp();
		}
		
		Long avgPointTime = totValTime / this.getAllValidationPoints().size()
				+ 1000	//measure delay
				+ 500;	//display delay
		
		return avgPointTime * noPoints + 2000;
	}
	
	/*
	 * returns normalized data
	 */
	public Map<Integer, List<Gaze>> getAllGazeData(){
		Map<Integer, List<Gaze>> gaze = new HashMap<Integer, List<Gaze>>();
		
		for(ValidationPoint pnt : this.getNormalizedValidationPoints()) {
			gaze.put(pnt.getPointNo().intValue(), pnt.getGaze());
		}
		
		return gaze;
	}
	
	public List<Gaze> getAllGaze(){
		List<Gaze> allGaze = new ArrayList<Gaze>();
		
		for(ValidationPoint pnt: this.getNormalizedValidationPoints()) {
			allGaze.addAll(pnt.getGaze());
		}
		
		return allGaze;
	}
	
	public void setValidationName(String name) {
		this.sessionName = name;
	}

	public Map<String, String> getSummary(){
		Map<String, String> sum = new LinkedHashMap<String, String>();
		
		sum.put("Validation:", getSessionName().replace("V", ""));
		sum.put("Fixation error:", getAverageFixationError() + "px");
		sum.put("Gaze error:", getAverageGazeError() + "px");
		sum.put("Relative error:", Util.round(getRelativeFixationError() * 100, 2)  + "%");
		
		return sum;
	}
	
	public List<ValidationPoint> getNormalizedValidationPoints(){
		List<ValidationPoint> normalized = new ArrayList<ValidationPoint>();
		
		//set fixed norm width
		double fixedNormWidth = 1167;
		
		double xScale = fixedNormWidth / getDisplayWidth();
		double yScale = ValidationSession.normHeight / getDisplayHeight();
		
		for(ValidationPoint rawPoint : getAllValidationPoints()) {
			//move
			Double pX = rawPoint.getPointX() + this.dispX;
			Double pY = rawPoint.getPointY() + this.dispY;
			
			//Re-scale
			pX = pX * xScale;
			pY = pY * yScale;
			
			List<Gaze> gZ = new ArrayList<Gaze>();
			for(Gaze g : rawPoint.getGaze()) {
				Double gX = (g.getX() + this.dispX) * xScale;
				Double gY = (g.getY() + this.dispY) * yScale;
				gZ.add(new Gaze(g.getTrial(), gX, gY, g.getTimeStamp()));
			}
			
			normalized.add(new ValidationPoint(pX, pY, gZ, rawPoint.getPointNo()));
		}		
		
		return normalized;
	}
	
	public Long getSampleFrequency() {
		Long allTime = 0L;
		int allSamples = 0;
		
		for(ValidationPoint pnt : getAllValidationPoints()) {
			allTime = allTime + (pnt.getGaze().get(pnt.getGaze().size() - 1).getTimeStamp() - pnt.getGaze().get(0).getTimeStamp());
			allSamples = allSamples + pnt.getGaze().size();
		}
		
		return (long) (1000D / (allTime / allSamples));
	}
}
