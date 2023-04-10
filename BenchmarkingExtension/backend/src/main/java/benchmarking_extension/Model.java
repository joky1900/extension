package benchmarking_extension;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import benchmarking_extension.GUI.FileSaver;
import benchmarking_extension.GUI.GraphicalUserInterface;
import benchmarking_extension.data.FileData;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/**
 * it'
 */
public class Model {
    private File[] files;
    private final String PATH = "src/main/resources/";
    private ArrayList<FileData> filesData = new ArrayList<>();

    private final JSONParser parser = new JSONParser();

    /**
     * Public mutator setting the array of file objects.
     * @param files array of files
     */
    public void setFiles(File[] files) {
        this.files = files;
    }

    public String getFileExtension(String fileName){
        String[] splitFileName = fileName.split("\\.");

        if(splitFileName.length == 1){
            return fileName;
        }else{
            return splitFileName[splitFileName.length - 1];
        }
    }


    public double[][] getXData(){
        return filesData.get(0).getGazeData();
    }

    public double[][] getXData2(){
        return filesData.get(0).getBallData();
    }

    public void parseFiles() {
        // Reset files
        filesData = new ArrayList<>();

        ArrayList<String> fileNames = getFileNames();

        for(String subject : getSubjects(getFileNames())){
            try {
                System.out.println(fileNames);
                if(fileNames.contains(subject + "_ballCoordinates.json") && fileNames.contains(subject + "_ballTracing.json")){
                    JSONObject ballCoordinates = (JSONObject) parser.parse(new FileReader(PATH +subject + "_ballCoordinates.json"));
                    JSONObject ballTracing = (JSONObject) parser.parse(new FileReader(PATH + subject + "_ballTracing.json"));

                    this.filesData.add(new FileData(ballCoordinates, ballTracing));
                }else{
                    throw new Exception("Could not load: " + subject);
                }
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }

    //-------------------------------------------------------------------------
    // Helper Functions
    //-------------------------------------------------------------------------

    File getFile(String fileName){
        for(File file : files){
            if(file.getName().equals(fileName)){
                return file;
            }
        }
        return null;
    }
    /**
     * Saves data to a CSV file
     */
    public void saveToCSV() {
        double[][] data = GraphicalUserInterface.getGraph().getData();
        new FileSaver(data);
    }

    /**
     * Gets an ArrayList of file names
     * @return ArrayList of file names
     */
    private ArrayList<String> getFileNames(){
        ArrayList<String> fileNames = new ArrayList<>();

        for(File file : files){
            fileNames.add(file.getName());
        }

        return fileNames;
    }

    /**
     * Gets an ArrayList of subject name and ID
     * @param fileNames ArrayList of file names
     * @return ArrayList of subjects
     */
    private ArrayList<String> getSubjects(ArrayList<String> fileNames){
        ArrayList<String> subjects = new ArrayList<>();
        for(String fileName : fileNames){
            String subject = getSubject(fileName);
            if(!subjects.contains(subject)){
                subjects.add(subject);
            }
        }

        return subjects;
    }

    /**
     * Gets a subject and id from a file name
     * @param fileName name of file
     * @return subject name and id
     */
    private String getSubject(String fileName){
        return fileName.split("_")[0];
    }

    public void getChart() {
    }
}
