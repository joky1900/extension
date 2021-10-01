package se.miun.student.evth400.eyedataanalyzer.data_visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import se.miun.student.evth400.eyedataanalyzer.data.Gaze;
import se.miun.student.evth400.eyedataanalyzer.data.ValidationPoint;
import se.miun.student.evth400.eyedataanalyzer.data.ValidationSession;
import se.miun.student.evth400.eyedataanalyzer.tools.DataLabb;

public class ValidationImgPanel extends JPanel {
	private static final long serialVersionUID = 1L;	
	
	ValidationSession ses;
	List<Gaze> gaze;
	
	public ValidationImgPanel(ValidationSession val, List<Gaze> gaze) {
		this.ses = val;
		this.gaze = gaze;
		
		setBackground(Color.white);
		setPreferredSize(new Dimension(1167, ValidationSession.normHeight));
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
				
		//paint validation points
		g2.setColor(Color.BLUE);
		for(ValidationPoint pnt : ses.getNormalizedValidationPoints()) {
			g2.setColor(Color.BLUE);
			g2.fillOval((int)(pnt.getPointX() - 10), 
					(int)(pnt.getPointY() - 10), 
					20, 20);
			g2.setColor(Color.BLACK);
			g2.drawOval((int)(pnt.getPointX() - 10), 
					(int)(pnt.getPointY() - 10), 
					20, 20);
		}
		
		//paint gaze
		g2.setColor(new Color(255, 255, 153, 150));
		for(int i = 0; i < gaze.size() -1 ; i++) {
			g2.fillOval((int)(gaze.get(i).getX().intValue() - 10), 
					(int)(gaze.get(i).getY().intValue() - 10),
					20, 20);
		}
		
		
		//paint fixation
		for(ValidationPoint pnt : ses.getAllValidationPoints()) {
			List<Gaze> pointGaze = new ArrayList<Gaze>();
			
			long startTime = pnt.getGaze().get(0).getTimeStamp();
			long endTime = pnt.getGaze().get(pnt.getGaze().size() -1).getTimeStamp();
			
			for(Gaze gz : gaze) {
				if(gz.getTimeStamp() >= startTime && gz.getTimeStamp() <= endTime) {
					pointGaze.add(gz);
				}
			}
			
			Gaze fixGaze = DataLabb.averageGazePoint(pointGaze);
			
			//paint
			g2.setColor(Color.GREEN);
			for(int i = 0; i < gaze.size() -1 ; i++) {
				g2.fillOval((int)(fixGaze.getX() - 10), 
						(int)(fixGaze.getY() - 10),
						20, 20);
			}
		}
		
	}	
}
