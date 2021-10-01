package se.miun.student.evth400.eyedataanalyzer.data;

import java.util.Date;

public abstract class GazeDataSession implements Comparable<GazeDataSession>{
	protected Date conclusionTime;
	protected String sessionName;
	
	
	//dimensions
	protected Double dispHeight, dispWidth, dispX, dispY;

	@Override
	public int compareTo(GazeDataSession o) {
		return this.conclusionTime.compareTo(o.conclusionTime);
	}
	
	public Date getConclusionTime() {
		return this.conclusionTime;
	}
	
	public Double getDisplayHeight() {
		return this.dispHeight;
	}
	
	public Double getDisplayWidth() {
		return this.dispWidth;
	}
	
	public Double getDispX() {
		return this.dispX;
	}
	
	public Double getDispY() {
		return this.dispY;
	}
	
	public String getSessionName() {
		return this.sessionName;
	}
}
