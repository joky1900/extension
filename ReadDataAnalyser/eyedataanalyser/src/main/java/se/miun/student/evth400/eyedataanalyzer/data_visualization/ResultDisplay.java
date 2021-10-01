package se.miun.student.evth400.eyedataanalyzer.data_visualization;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import se.miun.student.evth400.eyedataanalyzer.data.SubjectData;
import se.miun.student.evth400.eyedataanalyzer.tools.Util;

public class ResultDisplay extends JFrame {
	private static final long serialVersionUID = 1L;
	
	List<SubjectData> allSubjects;

	public ResultDisplay() {
		allSubjects = Util.getAllSubjectData(new File("RawDataFiles"));
		
		//Filter subjects without any calibrations
		allSubjects.removeIf(sD -> sD.getValidations().size() == 0);
		
		EventQueue.invokeLater(() -> {
			setLayout(new GridLayout(1,1));
			JTabbedPane tP = new JTabbedPane();
			
			for(Entry<String, JPanel> ent : tabContent().entrySet()) {
				JScrollPane scroll = new JScrollPane(ent.getValue());
				tP.add(ent.getKey(), scroll);
			}
			
			add(tP);
			
			setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocationRelativeTo(null);
			
			pack();
			revalidate();
			repaint();
			
			setTitle("Gaze experiment result display");
			setVisible(true);
		});		
	}
	
	public LinkedHashMap<String, JPanel> tabContent(){
		LinkedHashMap<String, JPanel> panels = new LinkedHashMap<String, JPanel>();
		
		panels.put("Subject overview", new SubjectOverview(allSubjects));
		List<SubjectData> filteredSubs = new ArrayList<SubjectData>(allSubjects);
		filteredSubs.removeIf(sub -> sub.getProcessedData() == null);
		panels.put("Data labb", new DataLabbGUI(filteredSubs));		
		panels.put("Result Summary", new ResultSummary(allSubjects));
		
		return panels;		
	}
	
	public static JPanel getDataPairTable(Map<String, ?> dataPairs) {
		JPanel panel = new JPanel(new GridLayout(0, 2));

		for(Entry<String, ?> entry : dataPairs.entrySet()) {
			panel.add(new JLabel(entry.getKey()));
			panel.add(new JLabel(entry.getValue().toString()));
		}

		panel.setBorder(new EmptyBorder(10,10,10,10));

		return panel;
	}	

	public static JLabel getHeader(String txt) {
		JLabel heading = new JLabel(txt);

		heading.setFont(new Font("serif", Font.BOLD, 20));
		heading.setBorder(new EmptyBorder(10,10,10,10));

		return heading;
	}

	public static JLabel getSubHeader(String txt) {
		JLabel heading = new JLabel(txt);

		heading.setFont(new Font("serif", Font.BOLD, 16));
		heading.setBorder(new EmptyBorder(10,10,10,10));

		return heading;
	}
}
