package se.miun.student.evth400.eyedataanalyzer.data_visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import se.miun.student.evth400.eyedataanalyzer.data.Fixation;
import se.miun.student.evth400.eyedataanalyzer.data.Gaze;
import se.miun.student.evth400.eyedataanalyzer.data.SnippetSession;
import se.miun.student.evth400.eyedataanalyzer.data.SubjectData;
import se.miun.student.evth400.eyedataanalyzer.data.ValidationPoint;
import se.miun.student.evth400.eyedataanalyzer.data.ValidationSession;
import se.miun.student.evth400.eyedataanalyzer.tools.CodeStimuli;
import se.miun.student.evth400.eyedataanalyzer.tools.DataLabb;
import se.miun.student.evth400.eyedataanalyzer.tools.HeatMap;

public class SnippetImgPanel extends JPanel implements Runnable, MouseInputListener{
	private static final long serialVersionUID = 1L;
	BufferedImage snippetImg;
	BufferedImage heatMap;
	CodeStimuli stimul;
	SubjectData sub;

	Thread activity;
	List<Gaze> gaze;
	int nextGaze;

	SideControlPanel side;
	JButton simulate;

	int speed = 1;

	Double startAdjX = null;
	Double startAdjY = null;
	Double adjX = 0D;
	Double adjY = 0D;

	public SnippetImgPanel(CodeStimuli stim, SubjectData sub, SideControlPanel side){
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		
		this.side = side;
		stimul = stim;
		this.sub = sub;

		this.gaze = sub.getProcessedData().getfilteredGaze(stim.getSnippetName());		

		this.simulate = side.btnSimulate();

		try {                
			snippetImg = ImageIO.read(new File("CodeImages/" + stim.getSnippetName() + ".png"));
		} catch (IOException ex) {
			System.out.print("failed to load image");
		}
		
		List<Point> points = new ArrayList<Point>();
		for(Gaze gz : gaze) {
			points.add(new Point(gz.getX().intValue(), gz.getY().intValue()));
		}
		
		final HeatMap myMap = new HeatMap(points, snippetImg);
		this.heatMap = myMap.getImage();

		side.chkShowAOI().addActionListener(new ActionListener() {				
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		side.chkShowAOI().setSelected(side.showAOI);

		if(side.chkShowFixations() != null) {
			side.chkShowFixations().addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					repaint();
				}
			});
			side.chkShowFixations().setSelected(side.showClusterFix);
		}		

		if(side.chkShowSaccFix() != null) {
			side.chkShowSaccFix().addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					repaint();
				}
			});
			side.chkShowSaccFix().setSelected(side.showSaccFix);
		}		

		if(side.chkShowGazeAreas() != null) {
			side.chkShowGazeAreas().addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					repaint();
				}
			});
			side.chkShowGazeAreas().setSelected(side.showGazeAreas);
		}	

		if(side.chkShowHeatAreas() != null) {
			side.chkShowHeatAreas().addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					repaint();
				}
			});
			side.chkShowHeatAreas().setSelected(side.showHeatAreas);
		}	

		if(simulate != null) {
			simulate.addActionListener(new ActionListener() {					
				@Override
				public void actionPerformed(ActionEvent e) {
					if(simulate.getText().equals("Simulate")) {
						simulate.setText("Stop");
						start();
					}else {
						simulate.setText("Simulate");
						stop();
					}			
				}
			});
		}

		side.btnAdjust().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(side.btnAdjust().getText() == "Adjust") {
					side.btnAdjust().setText("Remove adjustment");
					adjustByErrorMargin();
				}else {
					side.btnAdjust().setText("Adjust");
					adjX = 0D;
					adjY = 0D;
					repaint();
				}
			}
		});

		repaint();
	}		

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		BufferedImage img = snippetImg;
		
		if(side.showHeatAreas) {
			img = heatMap;
		}
		
		g2.drawImage(img, 0, 0, null);

		if(side.showAOI) {
			stimul.drawRows(g2);
			stimul.drawMethods(g2);
			stimul.drawClasses(g2);
		}	

		if(activity != null) {
			g2.setColor(Color.MAGENTA);
			g2.setStroke(new BasicStroke(3));	

			g2.fillOval(gaze.get(nextGaze).getX().intValue() - 5, 
					gaze.get(nextGaze).getY().intValue() - 5, 10, 10);	
		}	
		
		int gASize = CodeStimuli.rowHeight * 2 - 2;
		if(side.showClusterFix) {
			g2.setColor(new Color(255, 179, 179, 200));
			
			List<Fixation> fixes = new ArrayList<Fixation>(sub.getProcessedData().getClusterFixations(stimul.getSnippetName()));
			
			if(activity != null) {
				Long time = gaze.get(nextGaze).getTimeStamp();
				fixes.removeIf(fix -> fix.getStart() > time);
			}
			
			for(Fixation fix : fixes) {
				g2.fillOval(fix.getX().intValue() - gASize/2, fix.getY().intValue() - gASize/2, 
						gASize, gASize);
			}	
		}

		if(side.showSaccFix) {
			g2.setColor(new Color(255, 102, 0, 150));

			List<Fixation> fixes = new ArrayList<Fixation>(sub.getProcessedData().getSaccadesFixations(stimul.getSnippetName()));
			
			if(activity != null) {
				Long time = gaze.get(nextGaze).getTimeStamp();
				fixes.removeIf(fix -> fix.getStart() > time);
			}
			
			for(Fixation fix : fixes) {
				g2.fillOval(fix.getX().intValue() - gASize/2, fix.getY().intValue() - gASize/2, 
						gASize, gASize);
			}	
		}

		//Spatio-temporal
		if(side.showGazeAreas) {	
			Integer lastX = null, lastY= null;
			g2.setColor(new Color(120, 200, 0, 150));
			g2.setStroke(new BasicStroke(2));

			List<Fixation> fixes = new ArrayList<Fixation>(sub.getProcessedData().getSpatTempFixations(stimul.getSnippetName()));
			Collections.sort(fixes);
			
			if(activity != null) {
				Long time = gaze.get(nextGaze).getTimeStamp();
				fixes.removeIf(fix -> fix.getStart() > time);
			}
			
			for(Fixation fix : fixes) {
				int x = fix.getX().intValue() + this.adjX.intValue();
				int y = fix.getY().intValue() + this.adjY.intValue();

				if(lastX != null && lastY != null) {
					g2.drawLine(lastX, lastY, x, y);
				}
				
				g2.fillOval(x - gASize/2, y - gASize/2, 
						gASize, gASize);
				
				lastX = x;
				lastY = y;
			}	
		}

		/*if(side.showHeatAreas) {
			g2.setColor(new Color(255, 255, 153, 150));

			int stp = gaze.size() -1;
			if(activity != null) {
				stp = nextGaze;
			}
			
			for(int i = 0; i < stp ; i++) {
				g2.fillOval(gaze.get(i).getX().intValue() - 10, 
						gaze.get(i).getY().intValue() - 10, 20, 20);
			}
		}*/
	}

	@Override
	public void run() {
		long nextDelay = 0;

		for(int i = 0; i < gaze.size(); i++) {
			try {
				Thread.sleep(nextDelay);
			} catch (InterruptedException e) {
				//simulation aborted
			}

			if(i < gaze.size() -1) {
				nextDelay = gaze.get(i + 1).getTimeStamp() - gaze.get(i).getTimeStamp(); 
			}
			nextGaze = i;

			repaint();
		}	

		this.stop();
	}

	public void start() {
		if(activity == null) {
			activity = new Thread(this);
			activity.start();
		}
	}

	public void stop() {
		if(activity != null) {
			activity.interrupt();
			activity = null;
			simulate.setText("Simulate");				
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		this.startAdjX = (double) e.getX();
		this.startAdjY = (double) e.getY();
		this.adjX = 0D;
		this.adjY = 0D;		

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.startAdjX = null;
		this.startAdjY = null;
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(this.startAdjX != null) {
			this.adjX = e.getX() - this.startAdjX;
			this.adjY = e.getY() - this.startAdjY;		
			this.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	private void adjustByErrorMargin() {
		//Find validation session before snippet
		List<ValidationSession> vals = sub.getValidations();
		vals.removeIf(val -> val.getConclusionTime().after(sub.getSnippetSession(stimul.getSnippetName()).getConclusionTime()));
		ValidationSession valBefore = vals.get(vals.size() - 1);
		
		Double allErrorX = 0D, allErrorY = 0D;
		for(ValidationPoint pnt : valBefore.getNormalizedValidationPoints()) {
			allErrorX = allErrorX + (pnt.getPointX() - pnt.getAvgX());
			allErrorY = allErrorY + (pnt.getPointY() - pnt.getAvgY());
		}
		
		this.adjX = allErrorX / valBefore.getNumberOfPoints();
		this.adjY = allErrorY / valBefore.getNumberOfPoints();
		
		this.repaint();
	}

}
