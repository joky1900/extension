package se.miun.student.evth400.eyedataanalyzer.data_visualization;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import se.miun.student.evth400.eyedataanalyzer.data.SubjectData;

public class SideControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public Boolean showAOI = false;
	public Boolean showClusterFix = false;
	public Boolean showSaccFix = false;
	public Boolean showGazeAreas = false;
	public Boolean showHeatAreas = false;

	public SideControlPanel(SubjectData sub) {
		init(sub);
	}

	private void init(SubjectData sub) {
		setLayout(new GridLayout(0, 1));

		JCheckBox AOI = new JCheckBox("Show AOI");
		AOI.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(AOI.isSelected()) {
					showAOI = true;
				}else {
					showAOI = false;
				}
			}
		});		
		add(AOI);

		JCheckBox fix = new JCheckBox("Show cluster fixations");
		fix.addActionListener(new ActionListener() {				
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fix.isSelected()) {
					showClusterFix = true;
				}else {
					showClusterFix = false;
				}
			}
		});
		add(fix);


		JCheckBox fixSacc = new JCheckBox("Show \"saccades\" fixations");
		fixSacc.addActionListener(new ActionListener() {				
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fixSacc.isSelected()) {
					showSaccFix = true;
				}else {
					showSaccFix = false;
				}
			}
		});
		add(fixSacc);

		JCheckBox chkGaze = new JCheckBox("Show temp-spatial fixations");
		chkGaze.addActionListener(new ActionListener() {				
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chkGaze.isSelected()) {
					showGazeAreas = true;
				}else {
					showGazeAreas = false;
				}
			}
		});
		add(chkGaze);
		
		JButton btnAdj = new JButton("Adjust");
		add(btnAdj);

		JCheckBox chkHeat = new JCheckBox("Heat map");
		chkHeat.addActionListener(new ActionListener() {				
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chkHeat.isSelected()) {
					showHeatAreas = true;
				}else {
					showHeatAreas = false;
				}
			}
		});
		add(chkHeat);

		//simulation button
		JButton simulation = new JButton("Simulate");
		add(simulation);
	}

	public JCheckBox chkShowAOI() {
		return (JCheckBox)  this.getComponent(0);
	}

	public JCheckBox chkShowFixations() {
		return (JCheckBox) (this.getComponentCount() > 1 ? this.getComponent(1) : null);
	}

	public JCheckBox chkShowSaccFix() {
		return (JCheckBox) (this.getComponentCount() > 1 ? this.getComponent(2) : null);
	}

	public JCheckBox chkShowGazeAreas() {
		return (JCheckBox) (this.getComponentCount() > 1 ? this.getComponent(3) : null);
	}
	
	public JButton btnAdjust() {
		return (JButton) (this.getComponentCount() > 1 ? this.getComponent(4) : null);
	}

	public JCheckBox chkShowHeatAreas() {
		return (JCheckBox) (this.getComponentCount() > 1 ? this.getComponent(5) : null);
	}

	public JButton btnSimulate() {
		return (JButton) (this.getComponentCount() > 1 ? this.getComponent(6) : null);
	}
}
