package se.miun.student.evth400.eyedataanalyzer.data;

import java.io.Serializable;

public class Fixation implements Serializable, Comparable<Fixation>{
	private static final long serialVersionUID = 1L;
	
	String trial;
	Long start, end;
	Double x, y;
	Double madX, madY;
	Double peakVY, peakVX;
	Long duration;
	String event;
	Integer rowMatchIdx;
	
	public Fixation(String trial, Long start, Long end, Double x, Double y, Double mX, Double mY, Double pY,
			Double pX, Long duration, String event) {
		super();
		this.trial = trial;
		this.start = start;
		this.end = end;
		this.x = x;
		this.y = y;
		this.madX = mX;
		this.madY = mY;
		this.peakVY = pY;
		this.peakVX = pX;
		this.duration = duration;
		this.event = event;
	}

	public String getTrial() {return trial;}
	public Long getStart() {return start;}
	public Long getEnd() {return end;}
	public Double getX() {return x;}
	public Double getY() {return y;}
	public Double getMadX() {return madX;}
	public Double getMadY() {return madY;}
	public Double getPeakVY() {return peakVY;}
	public Double getPeakVX() {return peakVX;}
	public Long getDuration() {return duration;}
	public String getEvent() {return event;}	
	
	public Double getRangeStartX() {
		return this.getX() - this.getMadX(); 
	}
	
	public Double getRangeStartY() {
		return this.getY() - this.getMadY();
	}
	
	public Double getRangeW() {
		return this.madX * 2;
	}
	
	public Double getRangeH() {
		return this.madY * 2;
	}
	
	public Fixation setRowIdx(Integer idx) {
		this.rowMatchIdx = idx;
		
		return this;
	}
	
	public Integer getRowIdx() {
		return this.rowMatchIdx;
	}

	@Override
	public int compareTo(Fixation o) {
		return Long.compare(this.getStart(), o.getStart());
	}
}
