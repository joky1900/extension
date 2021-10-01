package se.miun.student.evth400.eyedataanalyzer.data_visualization;

import java.util.List;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import se.miun.student.evth400.eyedataanalyzer.data.ProcessedData;

public class FilterPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private JComboBox<Integer> window;
	private JComboBox<Integer> poly;
	private JButton filter;
	private JButton unfilter;
	private JButton optFilter;

	public FilterPanel(ProcessedData data, ActionListener updateAction) {
		setBackground(Color.WHITE);

		List<Integer> windowSizes = new ArrayList<Integer>();
		for(int i = 3; i <= data.shortestWindow(); i = i + 2) {
			windowSizes.add(i);
		}		

		window = new JComboBox<Integer>(windowSizes.toArray(new Integer[0]));

		if(data.curWindow == 0) {
			window.setSelectedIndex(-1);
		}else {
			window.setSelectedItem(data.curWindow);
		}

		poly = new JComboBox<Integer>(polySizes((Integer) window.getSelectedItem()));

		if(data.curPoly == 0) {
			poly.setSelectedIndex(-1);
		}else {
			poly.setSelectedItem(data.curPoly);
		}

		filter = new JButton("Filter");
		filter.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				data.setFiltration(getWindow(), getPoly());
				updateAction.actionPerformed(e);
			}
		});

		unfilter = new JButton("Remove filter");
		unfilter.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				data.unFilter();
				updateAction.actionPerformed(e);
			}
		});

		optFilter = new JButton("Filter by sampling frequency");
		optFilter.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				data.applyOptimalFilter();
				updateAction.actionPerformed(e);
			}
		});

		add(new JLabel("Window: "));
		add(this.window);
		add(new JLabel("Polynomial: "));
		add(this.poly);
		add(filter);
		add(unfilter);
		add(optFilter);

		window.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				poly.removeAllItems();

				for(Integer i : polySizes((Integer) window.getSelectedItem())) {
					poly.addItem(i);
				}

				repaint();
			}
		});		
	}

	public Integer getWindow(){
		return (Integer) window.getSelectedItem();
	}

	public Integer getPoly() {
		return (Integer) poly.getSelectedItem();
	}

	private Integer[] polySizes(Integer w) {
		if(w == null) {
			return new Integer[] {};
		}
		
		List<Integer> p = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15));
		p.removeIf(i -> i >= w);

		return p.toArray(new Integer[0]);
	}


}
