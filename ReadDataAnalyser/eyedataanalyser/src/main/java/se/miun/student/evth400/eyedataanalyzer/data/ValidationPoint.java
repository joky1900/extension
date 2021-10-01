package se.miun.student.evth400.eyedataanalyzer.data;

import java.util.Collections;
import java.util.List;

import se.miun.student.evth400.eyedataanalyzer.tools.DataLabb;
import se.miun.student.evth400.eyedataanalyzer.tools.Util;

public class ValidationPoint implements Comparable<ValidationPoint>{
	//Size of validation points used
	public static Long diameter = 20L;

	private List<Gaze> gaze;
	private Double pointX, pointY;
	private Long pointNo;

	public ValidationPoint(Double pX, Double pY, List<Gaze> gZ, Long pNo) {
		this.gaze = gZ;
		this.pointX = pX;
		this.pointY = pY;
		this.pointNo = pNo;	

		Collections.sort(this.gaze);
	}

	/*
	 * For sorting by order displayed
	 */
	@Override
	public int compareTo(ValidationPoint o) {
		return this.pointNo.compareTo(o.pointNo);
	}	

	/*
	 * The error margin calculated by averaging the coordinates over assumed fixation
	 */
	public Double getFixationError() {
		Gaze avgGaze = DataLabb.averageGazePoint(this.gaze);

		return Util.round(DataLabb.eucledianDistance(this.pointX, this.pointY, avgGaze.getX(), avgGaze.getY()), 2);
	}

	/*
	 * The error margin calculated by average Eucledian distance of each gaze sample
	 */
	public Double getGazeError() {
		Double allEuc = 0D;

		for(Gaze g : gaze) {
			allEuc = allEuc + DataLabb.eucledianDistance(getPointX(), getPointY(), g.getX(), g.getY());
		}

		return Util.round(allEuc/gaze.size(), 2);
	}	

	public Double getFixationErrorDegrees() {
		Double fixError = getFixationError();

		return Util.round(Math.toDegrees(Math.atan(fixError / 1890)), 2);
	}

	public Double getGazeErrorDegrees() {
		Double gazeError = getGazeError();

		return Util.round(Math.toDegrees(Math.atan(gazeError / 1890)), 2);
	}

	/*
	 * Get X coordinate for validation point
	 */
	public Double getPointX() {
		return this.pointX;
	}

	/*
	 * Get Y coordinate for validation point
	 */
	public Double getPointY() {
		return this.pointY;
	}

	/*
	 * Get display order number of the point
	 */
	public Long getPointNo() {
		return this.pointNo;
	}

	/*
	 * Get all recorded gaze
	 */
	public List<Gaze> getGaze() {
		return this.gaze;
	}

	/*
	 * Get the average X coordinate over the validation period
	 */
	public Double getAvgX() {
		Double sum = 0D;

		for(Gaze g : this.gaze) {
			sum = sum + g.getX();
		}

		return sum/this.gaze.size();
	}

	/*
	 * Get the average Y coordinate over the validation period
	 */
	public Double getAvgY() {
		Double sum = 0D;

		for(Gaze g : this.gaze) {
			sum = sum + g.getY();
		}

		return sum/this.gaze.size();
	}
}
