package se.miun.student.evth400.eyedataanalyzer.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import se.miun.student.evth400.eyedataanalyzer.data.Fixation;

public final class CodeStimuli {
	int topX = 7;
	int topY = 10;
	public static int rowHeight = 27;
	static int fontWidth = 13;

	String snippet;
	List<Class> classes;
	int height, width;

	public CodeStimuli(String snippet) {
		this.snippet = snippet;

		classes = new ArrayList<Class>();

		//Load the code file
		try {
			String allCode = FileUtils.readFileToString(new File("SnippetCodeForMeasuring/" + snippet + ".txt")) + "\r";

			if(snippet.endsWith("S")) {
				parseText(allCode);
			}else {
				parseCode(allCode);				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}			

	}

	private void parseText(String text) {
		//everything is rows
		String[] c = text.split("\n");
		this.height = c.length * rowHeight;

		List<String> cls = Arrays.asList(c);
		classes.add(new Class(cls, 0));		
	}

	private void parseCode(String code){
		String[] c = code.split("\n");
		this.height = c.length * rowHeight;
		int startIdx = 0;

		List<String> cls = new ArrayList<String>();
		for(int l = 0; l < c.length; l++) {
			if(c[l].matches("public class .*\r")) {
				//New class
				cls.clear();
				cls.add(c[l]);
				startIdx = l;
			}else {
				//Add row
				cls.add(c[l]);
			}

			if(c[l].matches("}\r")) {
				//this is the end of the class
				classes.add(new Class(cls, startIdx));
			}
		}
	}

	public String getSnippetName() {
		return this.snippet;
	}

	public void drawClasses(Graphics2D g) {
		g.setColor(Color.BLUE);

		for(Class c: classes) {
			g.drawRect(c.getTopX(), c.getTopY(), c.getWidth(), c.getHeight());
		}
	}

	public void drawMethods(Graphics2D g) {
		g.setColor(Color.RED);

		for(Class c: classes) {
			for(Method m : c.getMethods()) {
				g.drawRect(m.getTopX(), m.getTopY(), m.getWidth(), m.getHeight());
			}
		}

		for(Class c: classes) {
			for(int r = 1; r < c.getRows().size() - 2; r++) {
				Row row = c.getRows().get(r);
				g.drawRect(row.getTopX(), row.getTopY(), row.getWidth(), row.getHeight());
			}
		}
	}

	public void drawRows(Graphics2D g) {
		g.setColor(Color.GREEN);

		for(Class c: classes) {
			for(Row r: c.standAloneRows) {
				g.drawRect(r.getTopX(), r.getTopY(), r.getWidth(), r.getHeight());
			}
		}

		for(Class c: classes) {
			for(Method m : c.getMethods()) {
				for(Row r : m.getRows()) {
					g.drawRect(r.getTopX(), r.getTopY(), r.getWidth(), r.getHeight());
				}
			}
		}
	}

	public Fixation matchRow(Fixation fix) {
		fix.setRowIdx(null);

		//Extract all rows (not identifying code sections)
		List<Row> allRows = new ArrayList<CodeStimuli.Row>();
		for(Class c : classes) {
			allRows.addAll(c.getRows());

			for(Method m : c.getMethods()) {
				allRows.addAll(m.getRows());
			}
		}

		List<Row> nearRows = new ArrayList<Row>();
		for(Row r : allRows) {
			if(fix.getX() > (r.getTopX() - rowHeight + 1) && fix.getX() < (r.getTopX() + r.getWidth() + rowHeight - 1) &&
					fix.getY() > (r.getTopY() - rowHeight + 1) && fix.getY() < (r.getTopY() + r.getHeight() + rowHeight - 1)) {
				//fixation within range of row
				nearRows.add(r);
			}
		}

		//Get closest row if more than one within range
		int closest = 100;
		for(Row r : nearRows) {
			if(Math.abs((r.getTopY() + r.getHeight() / 2) - fix.getY()) < closest) {
				fix.setRowIdx(r.getRowIdx());
			}
		}


		return fix;
	}

	public Double getViewPortCoverage() {
		//Extract all rows
		List<Row> allRows = new ArrayList<CodeStimuli.Row>();
		for(Class c : classes) {
			allRows.addAll(c.getRows());

			for(Method m : c.getMethods()) {
				allRows.addAll(m.getRows());
			}
		}
		
		Double fullArea = 800D * 1000D;
		Double rowArea = 0D;
		for(Row r: allRows) {
			rowArea = rowArea + (r.getHeight() * r.getWidth());
		}
		
		return rowArea/fullArea;
	}


	public abstract class CodeSection{
		int RowIdx;
		int topX = 0, topY = 0;
		int width = 0, height = 0;

		public CodeSection(List<String> code, int startRowIdx) {
			this.RowIdx = startRowIdx;
			this.topY = CodeStimuli.this.topY + startRowIdx * CodeStimuli.rowHeight;
			this.height = code.size() * CodeStimuli.rowHeight;
			parseCode(code);
		}

		public int getRowIdx() {return RowIdx;}
		public int getTopX() {return topX;}
		public int getTopY() {return topY;}
		public int getWidth() {return width;}
		public int getHeight() {return height;}

		protected abstract void parseCode(List<String> code);
	}

	public class Class extends CodeSection{
		private List<Method> methods;
		private List<Row> standAloneRows;

		public Class(List<String> code, int startRowIdx) {
			super(code, startRowIdx);		
			this.topX = CodeStimuli.this.topX;
		}

		protected void parseCode(List<String> code) {
			methods = new ArrayList<Method>();
			standAloneRows = new ArrayList<Row>();

			//check if story snippet
			if(CodeStimuli.this.getSnippetName().endsWith("S")) {
				for(int r = 0; r < code.size(); r++) {
					standAloneRows.add(new Row(code.get(r), r));
				}
				return;				
			}

			//top and bottom row is naturally part of the class
			standAloneRows.add(new Row(code.get(0), this.RowIdx));
			standAloneRows.add(new Row(code.get(code.size()-1), this.RowIdx + code.size() -1));

			List<String> met = new ArrayList<String>();
			int startIdx = 0;
			for(int l = 1; l < code.size() - 1; l++) {

				if(code.get(l).matches("\t@Override.*\r")) {
					//Override notation = take two lines
					met.add(code.get(l));
					startIdx = l + this.RowIdx;
					l++;
					met.add(code.get(l));

					//one line method?
					if(code.get(l).matches("\tp.*[)]\\s*\\{.*\\}\r")) {
						methods.add(new Method(met, startIdx));
						met.clear();
					}
				}else if(code.get(l).matches("\tp.*[)]\\s*\\{\r")) {
					//method start
					met.add(code.get(l));
					startIdx = l + this.RowIdx;

					//is it a one line method?
					if(code.get(l).matches(".*\\}\r")) {
						methods.add(new Method(met, startIdx));
						met.clear();
					}
				}else if(code.get(l).matches("\t\\}\r")) {
					//method end
					met.add(code.get(l));
					methods.add(new Method(met, startIdx));
					met.clear();
				}else if(code.get(l).matches("\t\t\t*.*\r")) {
					//method content
					met.add(code.get(l));
				}else{
					//empty row
					if(met.size() == 0) {
						//in class
						standAloneRows.add(new Row(code.get(l), l + this.RowIdx));
					}else {
						//in method
						met.add(code.get(l));
					}					
				}
			}

			//Calculate the width of class
			for(Method m : this.methods) {
				int w = m.getTopX() + m.getWidth() - CodeStimuli.this.topX;

				if(this.width < w) {
					this.width = w;
				}
			}
			for(Row r: this.standAloneRows) {
				int w = r.getTopX() - this.getTopX() + r.getWidth();

				if(this.width < w) {
					this.width = w;
				}
			}
		}

		public List<Method> getMethods(){
			return this.methods;
		}

		public List<Row> getRows(){
			return this.standAloneRows;
		}
	}

	public class Method extends CodeSection{
		private List<Row> methodRows;

		public Method(List<String> code, int startRowIdx) {
			super(code, startRowIdx);
		}

		protected void parseCode(List<String> code) {
			this.topX = CodeStimuli.this.topX + CodeStimuli.fontWidth * 4;
			methodRows = new ArrayList<Row>();
			for(int l = 0; l < code.size(); l++) {
				methodRows.add(new Row(code.get(l), RowIdx + l));
			}

			for(Row r : this.methodRows) {
				int w = r.getTopX() + r.getWidth() - this.topX;

				if(this.width < w) {
					this.width = w;
				}
			}
		}

		public List<Row> getRows(){
			return this.methodRows;
		}
	}

	public class Row extends CodeSection{
		Boolean isEmpty = false;

		public Row(String code, int rowIdx) {
			super(Arrays.asList(code), rowIdx);
		}

		protected void parseCode(List<String> code) {
			String row = code.get(0);

			//Calculate topX
			int offSet = 0;
			this.topX = CodeStimuli.this.topX;
			while(row.startsWith("\t", offSet++)) {
				this.topX = this.topX + CodeStimuli.fontWidth * 4; 
			}

			row = row.replaceAll("\t*", "");
			row = row.replaceAll("\r", "");

			//calculate row width;
			this.width = row.trim().length() * CodeStimuli.fontWidth;	

			if(row.equals("")) {
				isEmpty = true;
			}
		}
	}
	
	public class Word extends CodeSection{

		public Word(List<String> code, int startRowIdx) {
			super(code, startRowIdx);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void parseCode(List<String> code) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
