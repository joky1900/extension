package se.miun.student.evth400.eyedataanalyzer.data;

import java.io.Serializable;
import java.util.Comparator;

public class Gaze implements Comparable<Gaze>, Serializable{
	private static final long serialVersionUID = 1L;
	
	private Double x, y;
	private Long timestamp;
	private String trial;
	private Double velocity = 0D;
	
	public Gaze(String trial, Double gX, Double gY, Long tStamp) {
		this.x = gX;
		this.y = gY;
		this.timestamp = tStamp;
		this.trial = trial;
	}
	
	/*
	 * Getters
	 */
	public Long getTimeStamp() {return this.timestamp;}
	public Double getX() {return this.x;}	
	public Double getY() {return this.y;}	
	public String getTrial() {return this.trial;}	
	public Double getVelocity() {return this.velocity;}

	/*
	 * Set velocity value after calculation
	 */
	public void setVelocity(Double vel) {
		this.velocity = vel;
	}
	
	/*
	 * Order gaze data in chronological order
	 */
	@Override
	public int compareTo(Gaze o) {		
		return Comparator.comparing(Gaze::getTrial)
	              .thenComparing(Gaze::getTimeStamp)
	              .compare(this, o);
	}
}
