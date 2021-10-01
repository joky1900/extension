package se.miun.student.evth400.eyedataanalyzer.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.*;

import javax.script.ScriptException;

import se.miun.student.evth400.eyedataanalyzer.tools.DataLabb;

/*
 * Class used to retrieve and hold processed data
 * data is saved to reduce loading time
 */
public class ProcessedData implements Serializable{
	private static final long serialVersionUID = 1L;

	public String subjectID;
	private Map<String, List<Gaze>> rawSnippetGaze;
	private Map<String, Map<Integer, List<Gaze>>> rawValidationGaze;

	private Map<Integer, Map<Integer, Map<String,List<Gaze>>>> altFilteredData;	//fst order = window length, snd = polynomial
	private Map<String, List<Fixation>> fixations;
	private Map<String, List<Gaze>> filteredGaze;

	//check for existing file
	File filtFile; 
	private int optWindow, optPoly = 3; 
	public int curWindow = 0, curPoly = 0;

	public ProcessedData(String subId, Map<String, List<Gaze>> normGaze,  Map<String, Map<Integer, List<Gaze>>> valGaze, int sampFq) {
		this.subjectID = subId;
		this.rawSnippetGaze = normGaze; 
		this.rawValidationGaze = valGaze; 

		//alternative gaze
		this.filtFile = new File("filteredData/" + subId);
		if(filtFile.exists()) {
			try {
				FileInputStream fileIn = new FileInputStream(filtFile);
				ObjectInputStream objectIn = new ObjectInputStream(fileIn);

				this.altFilteredData = (Map<Integer, Map<Integer, Map<String, List<Gaze>>>>) objectIn.readObject();
				objectIn.close();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}else {
			this.altFilteredData = new HashMap<Integer, Map<Integer, Map<String,List<Gaze>>>>();
		}
		
		if(sampFq % 2 == 0) {
			optWindow = sampFq -1;
		}else {
			optWindow = sampFq;
		}
		
		this.applyOptimalFilter();

		//generate fixations
		this.fixations = new HashMap<String, List<Fixation>>();
		this.fixations.put("Saccades", DataLabb.getSaccadesFixations(this, this.subjectID, false));
		this.fixations.put("Cluster", DataLabb.getMatLabFixations(this.subjectID, this));
		this.fixations.put("SpatialTemp", DataLabb.getSpatioTempFixations(this));
	}

	public void applyOptimalFilter() {
		this.setFiltration(optWindow, optPoly);
	}

	public Integer shortestWindow() {
		Integer sht = 100;

		for(Entry<String, List<Gaze>> ent : this.rawSnippetGaze.entrySet()) {
			if(ent.getValue().size() < sht) {
				sht = ent.getValue().size();
			}
		}

		for(Entry<String, Map<Integer, List<Gaze>>> ent : this.rawValidationGaze.entrySet()) {
			for(Entry<Integer, List<Gaze>> pnt : ent.getValue().entrySet()) {
				if(pnt.getValue().size() < sht) {
					sht = pnt.getValue().size();
				}
			}
		}

		if(sht % 2 == 0) {
			//window length is even, sav-golay needs uneven
			sht--;
		}

		return sht;
	}

	public Map<String, List<Gaze>> getFiltered() {
		if(this.filteredGaze == null) {
			unFilter();
		}

		return this.filteredGaze;
	}

	public void unFilter() {
		this.filteredGaze = this.rawSnippetGaze;
		this.curPoly = 0; this.curWindow = 0;

		for(Entry<String, Map<Integer, List<Gaze>>> val : rawValidationGaze.entrySet()) {
			List<Gaze> filt = new ArrayList<Gaze>();
			for(Entry<Integer, List<Gaze>> pnt : val.getValue().entrySet()) {
				filt.addAll(pnt.getValue());
			}

			this.filteredGaze.put(val.getKey(), filt);
		}
	}

	public Map<String, List<Gaze>> setFiltration(int window, int poly){		
		try {
			if(!altFilteredData.containsKey(window) || !altFilteredData.get(window).containsKey(poly)) {
				//has not been cached
				Map<String, List<Gaze>> filtGaze = DataLabb.applySavGolay(rawSnippetGaze, window, poly);

				for(Entry<String, Map<Integer, List<Gaze>>> val : rawValidationGaze.entrySet()) {
					List<Gaze> filt = new ArrayList<Gaze>();
					for(Entry<Integer, List<Gaze>> pnt : val.getValue().entrySet()) {
						int w = pnt.getValue().size() % 2 == 0? pnt.getValue().size() - 1 : pnt.getValue().size();
						filt.addAll(DataLabb.applySavGolay(pnt.getValue(), w, poly));
					}

					filtGaze.put(val.getKey(), filt);
				}

				if(this.altFilteredData.containsKey(window)) {
					this.altFilteredData.get(window)
					.put(poly, filtGaze);
				}else {
					this.altFilteredData.put(window, new HashMap<Integer, Map<String, List<Gaze>>>());
					this.altFilteredData.get(window)
					.put(poly, filtGaze);
				}
				
				//save to file
				try {
					FileOutputStream fileOut = new FileOutputStream(filtFile);
					ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
					objectOut.writeObject(this.altFilteredData);
					objectOut.close();
				}catch(Exception ex) {
					System.out.print(ex.getMessage());
				}
			}
			this.filteredGaze = altFilteredData.get(window).get(poly);
		}catch(ScriptException ex) {
			ex.printStackTrace();
			System.out.println("Sav golay failed: " + ex.getMessage());
		}		

		curWindow = window; curPoly = poly;

		return getFiltered();
	}

	public List<Fixation> getClusterFixations(String snippet){
		return getSnippetFixations(fixations.get("Cluster"), snippet);
	}

	public List<Fixation> getSaccadesFixations(String snippet){
		return getSnippetFixations(fixations.get("Saccades"), snippet);
	}

	public List<Fixation> getSpatTempFixations(String snippet){
		return getSnippetFixations(fixations.get("SpatialTemp"), snippet);
	}

	public List<Gaze> getfilteredGaze(String snipName){
		return getFiltered().get(snipName);		
	}

	/*
	 * methods for filtering out data specified to particular session
	 */
	public List<Gaze> getAllFilteredGaze(){
		List<Gaze> allGaze = new ArrayList<Gaze>();

		for(Entry<String, List<Gaze>> ent : filteredGaze.entrySet()) {
			allGaze.addAll(ent.getValue());
		}

		return allGaze;
	}	

	public static List<Gaze> getSnippetGaze(String snippetName, List<Gaze> gaze){
		List<Gaze> filtered = new ArrayList<Gaze>(gaze);
		filtered.removeIf(g -> !g.getTrial().equals(snippetName));

		return filtered;
	}

	public static List<Fixation> getSnippetFixations(List<Fixation> fix, String snippetName){
		List<Fixation> filtered = new ArrayList<Fixation>(fix);
		filtered.removeIf(f -> !f.getTrial().equals(snippetName));

		return filtered;
	}

}
