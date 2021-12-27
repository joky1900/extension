package se.miun.student.evth400.eyedataanalyzer.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;


import se.miun.student.evth400.eyedataanalyzer.data.Fixation;

public final class CodeStimuli {
	private int topX = 7;
	private int topY = 10;
	private int height, width;
	
	public static int rowHeight = 27;
	public static int fontWidth = 13;

	private String snippetName;
	private List<Class> classes;	

	public CodeStimuli(String snippet) {
		this.snippetName = snippet;

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
		List<String> c = Arrays.asList(text.split("\n"));
		this.height = c.size() * rowHeight;

		classes.add(new Class(c, 0));		
	}

	private void parseCode(String code){
		List<String> c = Arrays.asList(code.split("\n"));
		this.height = c.size() * rowHeight;
		int startIdx = 0;

		List<String> cls = new ArrayList<String>();
		for(int l = 0; l < c.size(); l++) {
			if(c.get(l).matches("public class .*\r") || c.get(l).matches("public abstract .*\r") || c.get(l).matches("public interface .*\r")) {
				//New class
				cls.clear();
				cls.add(c.get(l));
				startIdx = l;
			}else {
				//Add row
				cls.add(c.get(l));
			}

			if(c.get(l).matches("}\r")) {
				//this is the end of the class
				classes.add(new Class(cls, startIdx));
			}
		}
	}

	public String getSnippetName() {
		return this.snippetName;
	}

	public void drawClasses(Graphics2D g) {
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.BLUE);

		for(Class c: classes) {
			g.drawRect(c.getTopX(), c.getTopY(), c.getWidth(), c.getHeight());
		}
	}

	public void drawMethods(Graphics2D g) {
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.RED);

		for(Class c: classes) {
			for(Method m : c.getMethods()) {
				g.drawRect(m.getTopX(), m.getTopY(), m.getWidth(), m.getHeight());
			}
		}
	}

	public void drawRows(Graphics2D g) {
		g.setStroke(new BasicStroke(1));
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

	public void drawWords(Graphics2D g) {
		g.setColor(new Color(120, 200, 0, 30));
		g.setStroke(new BasicStroke(1));

		for(Class c: classes) {
			for(Row r: c.standAloneRows) {
				for(Word w: r.words) {
					g.fillRect(w.getTopX(), w.getTopY(), w.getWidth(), w.getHeight());
				}
			}
		}

		for(Class c: classes) {
			for(Method m : c.getMethods()) {
				for(Row r : m.getRows()) {
					for(Word w: r.words) {
						g.fillRect(w.getTopX(), w.getTopY(), w.getWidth(), w.getHeight());
					}
				}
			}
		}
	}

	public Fixation matchFix(Fixation fix) {
		fix.setRowIdx(null);
		fix.setWordIdx(null);

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
		double closestRow = 1000;
		for(Row r : nearRows) {
			if(Math.abs((r.getTopY() + r.getHeight() / 2) - fix.getY()) < closestRow) {
				//Set row index
				closestRow = Math.abs((r.getTopY() + r.getHeight() / 2) - fix.getY());
				fix.setRowIdx(r.getRowIdx());

				//Set word index
				double closestWord = 1000;
				for(Word w : r.words) {
					//Check if within word bounds
					if(fix.getX() >= w.getTopX() && fix.getX() <= (w.getTopX() + w.getWidth())) {
						fix.setWordIdx(w.getWordIdx());
						break;
					}

					//Not within bounds but within row, find closest word
					if(w.distFromBorder(fix.getX()) < closestWord) {
						closestWord = w.distFromBorder(fix.getX());
						fix.setWordIdx(w.getWordIdx());
					}
				}
			}
		}

		return fix;
	}

	public Integer getWordCount() {
		Integer count = 0;

		for(Class c: classes) {
			for(Row r: c.standAloneRows) {
				count = count + r.words.size();
			}
		}

		for(Class c: classes) {
			for(Method m : c.getMethods()) {
				for(Row r : m.getRows()) {
					count = count + r.words.size();
				}
			}
		}

		return count;
	}

	public abstract class CodeSection{
		protected List<String> code;
		protected int RowIdx;
		protected int topX = 0, topY = 0;
		protected int width = 0, height = 0;

		public CodeSection(List<String> code, int startRowIdx) {
			this.code = code;
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
		public String getCode() {return code.toString();}

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
					if(!code.get(r).matches("\r")) {
						standAloneRows.add(new Row(code.get(r), r));
					}
				}				
			}else {
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
							if(!code.get(l).matches("\r")) {
								standAloneRows.add(new Row(code.get(l), l + this.RowIdx));
							}						
						}else {
							//in method
							met.add(code.get(l));
						}					
					}
				}
			}			

			//Calculate the width of class			
			for(Row r: this.standAloneRows) {
				int w = r.getWidth() + r.getTopX() - CodeStimuli.this.topX;

				if(this.width < w) {
					this.width = w;
				}
			}
			for(Method m : this.methods) {
				int w = m.getTopX() + m.getWidth() - CodeStimuli.this.topX;

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
				if(!code.get(l).matches("\r")) {
					methodRows.add(new Row(code.get(l), RowIdx + l));
				}
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
		List<Word> words;

		public Row(String code, int rowIdx) {
			super(Arrays.asList(code), rowIdx);
		}

		protected void parseCode(List<String> code) {
			words = new ArrayList<Word>();
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

			if(!row.equals("")) {
				//parse words
				String[] w = row.split("[\s.:,(){};“”]");

				int posX = this.topX;
				for(int wIdx = 0; wIdx < w.length; wIdx++) {	
					if(w[wIdx] == "") {
						posX = posX + CodeStimuli.fontWidth;
						continue;
					}
					words.add(new Word(Arrays.asList(w[wIdx]), this.RowIdx, wIdx, posX));
					posX = posX + CodeStimuli.fontWidth;
					posX = posX + w[wIdx].length() * CodeStimuli.fontWidth;
				}
			}
		}
	}

	public class Word extends CodeSection{
		private Integer wordIdx;

		public Word(List<String> code, int startRowIdx, int wIdx, int posX) {
			super(code, startRowIdx);
			this.wordIdx = wIdx;
			this.topX = posX;
		}

		//Code only has one item
		@Override
		protected void parseCode(List<String> code) {
			this.width = code.get(0).length() * CodeStimuli.fontWidth;
		}

		public Integer getWordIdx() {
			return wordIdx;
		}
		
		public double distFromBorder(double x) {
			double left = Math.abs(x - getTopX());
			double right = Math.abs(x - (getTopX() + getWidth()));
			return Math.min(left, right);
		}
	}
}
