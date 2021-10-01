package se.miun.student.evth400.eyedataanalyzer.data_visualization;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import se.miun.student.evth400.eyedataanalyzer.data.SubjectData;
import se.miun.student.evth400.eyedataanalyzer.tools.DataLabb;
import se.miun.student.evth400.eyedataanalyzer.tools.ScrollablePanel;

public class SubjectOverview extends ScrollablePanel {
	private static final long serialVersionUID = 1L;
	List<SubjectData> allSubs;

	public SubjectOverview(List<SubjectData> allSubs) {
		this.allSubs = allSubs;
		init();
	}

	protected void init() {
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		add(content);

		setScrollableWidth(ScrollablePanel.ScrollableSizeHint.FIT);
		setScrollableHeight(ScrollablePanel.ScrollableSizeHint.NONE);

		//Accepted results ----
		content.add(ResultDisplay.getHeader("Competent programmers"));

		TableModel passModel = new SubjectTableModel(DataLabb.competentSubjectsOnly(allSubs));
		JTable  pass = new JTable(passModel);
		pass.setPreferredScrollableViewportSize(pass.getPreferredSize());
		pass.setFillsViewportHeight(true);
		TableColumnModel passColumnModel = pass.getColumnModel();
		pass.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for(int c = 0 ; c < passModel.getColumnCount(); c++) {
			passColumnModel.getColumn(c).setPreferredWidth(150);
		}
		pass.repaint();

		// code for sorting here.
		TableRowSorter<TableModel> passSorter = new TableRowSorter<>(pass.getModel());
		pass.setRowSorter(passSorter);
		List<RowSorter.SortKey> passSortKeys = new ArrayList<>();

		int passColumnIndexToSort = 1;
		passSortKeys.add(new RowSorter.SortKey(passColumnIndexToSort, SortOrder.ASCENDING));

		passSorter.setSortKeys(passSortKeys);

		//add to panel
		content.add(new JScrollPane(pass, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);


		//all results
		content.add(ResultDisplay.getHeader("Test subjects"));

		TableModel tableModel = new SubjectTableModel(allSubs);
		JTable  table = new JTable(tableModel);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFillsViewportHeight(true);
		TableColumnModel columnModel = table.getColumnModel();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for(int c = 0 ; c < tableModel.getColumnCount(); c++) {
			columnModel.getColumn(c).setPreferredWidth(150);
		}
		table.repaint();

		// code for sorting here.
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();

		int columnIndexToSort = 1;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));

		sorter.setSortKeys(sortKeys);
		sorter.sort();


		//add to panel
		content.add(new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

	}

	public class SubjectTableModel extends AbstractTableModel{
		private static final long serialVersionUID = 1L;

		private static final int COLUMN_NO			= 0;
		private static final int COLUMN_SUBID		= 1;
		private static final int COLUMN_ORIGIN		= 2;
		private static final int COLUMN_AGE			= 3;
		private static final int COLUMN_GLASSES		= 4;
		private static final int COLUMN_GENDER		= 5;
		private static final int COLUMN_JAVAEXP		= 6;
		private static final int COLUMN_REACTEXP    = 7;
		private static final int COLUMN_NOTES		= 8;
		private static final int COLUMN_SNIPANSW	= 9;
		private static final int COLUMN_TOTCORRECT	= 10;
		private static final int COLUMN_RCOR		= 11;
		private static final int COLUMN_JCOR		= 12;
		private static final int COLUMN_FRQ			= 13;
		private static final int COLUMN_ERROR		= 14;
		private static final int COLUMN_REL_ERR		= 15;
		private static final int COLUMN_BROWSER		= 16;
		private static final int COLUMN_FEEDBACK	= 17;

		private String[] columnNames = {"No #", "Subject id", "Origin", "Age", "Glasses",
				"Gender", "Java exp", "React exp", "Notes", "Snippets done", "Snippets correct",
				"of RxJava", "of Java", "Sample frequency Hz", "Average error px", "Relative error",
				"System", "Feedback"/*,
				"Fixations per snippet", "Test duration"*/};
		private List<SubjectData> allSubs;

		public SubjectTableModel(List<SubjectData> listSubs) {
			this.allSubs = listSubs;

			int indexCount = 1;
			for (SubjectData sub : allSubs) {
				sub.setIndex(indexCount++);
			}

		}

		@Override
		public int getRowCount() {
			return allSubs.size();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columnNames[columnIndex];
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (allSubs.isEmpty()) {
				return Object.class;
			}
			return getValueAt(0, columnIndex).getClass();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			SubjectData sub = allSubs.get(rowIndex);
			Object returnValue = null;

			switch (columnIndex) {
			case COLUMN_NO:
				returnValue = sub.index;
				break;
			case COLUMN_SUBID:
				returnValue = sub.getSubjectID();
				break;
			case COLUMN_ORIGIN:
				returnValue = sub.getOrigin();
				break;
			case COLUMN_AGE:
				returnValue = sub.getAge();
				break;
			case COLUMN_GLASSES:
				returnValue = sub.hasGlasses() ? "yes" : "";
				break;
			case COLUMN_SNIPANSW:
				returnValue = sub.getSnippetsDone();
				break;
			case COLUMN_GENDER:
				returnValue = sub.getGender();
				break;
			case COLUMN_JAVAEXP:
				returnValue = sub.getJavaExperience();
				break;
			case COLUMN_REACTEXP:
				returnValue = sub.getReactiveExperience();
				break;
			case COLUMN_TOTCORRECT:
				returnValue = sub.getCorrectAnswers();
				break;
			case COLUMN_NOTES:
				returnValue = sub.getComments();
				break;
			case COLUMN_RCOR:
				returnValue = sub.getCorrectReactiveSnippets();
				break;
			case COLUMN_JCOR:
				returnValue = sub.getCorrectImperativeSnippets();
				break;
			case COLUMN_FRQ:
				returnValue = sub.getSampleFrequenzy();
				break;
			case COLUMN_ERROR:
				returnValue = sub.getAverageFixationError();
				break;
			case COLUMN_REL_ERR:
				returnValue = sub.getRelativeFixationError();
				break;
			case COLUMN_BROWSER:
				returnValue = sub.getUserAgent();
				break;
			case COLUMN_FEEDBACK:
				returnValue = sub.getFeedback();
				break;
				/*case COLUMN_RESPT:
				returnValue = sub.getExperimentTime();
				break;
			case COLUMN_FIX:
				if(DataLabb.isAccepted(sub)) {
					returnValue = sub.getAnalysis().getFixations().size() / sub.getSnippetsAnswered();
				}

				break;*/
				/*case COLUMN_:
				returnValue = ;
				break;*/				
			default:
				throw new IllegalArgumentException("Invalid column index");
			}

			return returnValue;
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			SubjectData sub = allSubs.get(rowIndex);
			if (columnIndex == COLUMN_NO) {
				sub.setIndex((int) value);
			}      
		}

	}
}
