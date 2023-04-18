package benchmarking_extension;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import benchmarking_extension.data.Data;
import benchmarking_extension.data.FileData;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/**
 * it'
 */
public class Model {
    private File[] files;
    private final String PATH = "src/main/resources/";
    private String set;
    private int subjectID = 0;
    private ArrayList<FileData> filesData = new ArrayList<>();

    private final JSONParser parser = new JSONParser();

    public Model(){
        this.set = "X"; // X is default set
    }

    /**
     * Public mutator setting the array of file objects.
     * @param files array of files
     */
    public void setFiles(File[] files) {
        this.files = files;
    }

    public void setSubjectID(int subjectID){
        this. subjectID = subjectID;
    }

    public String getFileExtension(String fileName){
        String[] splitFileName = fileName.split("\\.");

        if(splitFileName.length == 1){
            return fileName;
        }else{
            return splitFileName[splitFileName.length - 1];
        }
    }

    private double[][] extractData(ArrayList<Data> subjectData){
        // Initialize array
        double[][] data = new double[subjectData.size()][2];

        // Iterate over each coordinate
        for(int i = 0; i < subjectData.size(); ++i){
            int x = subjectData.get(i).getX();
            int y = subjectData.get(i).getY();
            double timeStamp = subjectData.get(i).getTimeStamp();

            // Populate array
            data[i][0] = timeStamp;

            if(set.equals("X")) {
                data[i][1] = (long) x;
                //      data[i][1] = distance;
            }else{
                data[i][1] = (long) y;
            }
        }


        return data;
    }

    private boolean empty(){
        return filesData.size() == 0;
    }


    public double[][] getGazeData(){
        // No files are loaded
        if(empty()){
            return new double[0][0];
        }

        ArrayList<Data> gazeData = filesData.get(subjectID).getGazeData();

        return extractData(gazeData);
    }

    public double[][] getBallData(){
        // No files are loaded
        if(empty()){
            return new double[0][0];
        }

        ArrayList<Data> ballData = filesData.get(subjectID).getBallData();

        return extractData(ballData);
    }

    public double[] getAverageDistance(){
        double[] data = new double[filesData.size()];

        for(int i = 0; i < data.length; ++i){
            FileData file = filesData.get(i);
            data[i] = getAverageDistance(file.getGazeData(), file.getBallData());
        }

        return data;
    }

    public double[][] getSingleAverageData(){

        if(filesData.size() == 0){
            return new double[0][0];
        }

        FileData file = filesData.get(subjectID);
        ArrayList<Data> gazeData = file.getGazeData();
        ArrayList<Data> ballData = file.getBallData();

        int length = Math.min(ballData.size(), gazeData.size()); // get shortest

        double[][] data = new double[length][2];

        for(int i = 0; i < length; ++i){
            data[i][1] = getDistance(ballData.get(i).getX(), ballData.get(i).getY(), gazeData.get(i).getX(), gazeData.get(i).getY());
            data[i][0] = gazeData.get(i).getTimeStamp();
        }

        return data;
    }

    private double getAverageDistance(ArrayList<Data> gazeData, ArrayList<Data> ballData){
        int length = Math.min(ballData.size(), gazeData.size()); // get shortest

        int total = 0;

        for(int i = 0; i < length; ++i){
            total += getDistance(ballData.get(i).getX(), ballData.get(i).getY(), gazeData.get(i).getX(), gazeData.get(i).getY());
        }

        return (double) total / length;
    }

    private int getDistance(int xPos, int yPos, int x, int y){
        // Get delta x over delta y
        double xPow = Math.pow(xPos - x, 2);
        double yPow = Math.pow(yPos - y, 2);

        // Calculate distance
        return (int) Math.sqrt(xPow + yPow);
    }


    public void parseFiles() {
        // Reset files
        filesData = new ArrayList<>();

        ArrayList<String> fileNames = getFileNames();

        for(String subject : getSubjects(getFileNames())){
            try {
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

    /**
     * Saves data to a CSV file
     */
    public void saveToCSV() {
      //  double[][] data = GraphicalUserInterface.getGraph().getData();
        StringBuilder stringBuilder = new StringBuilder();



        for(FileData fileData : filesData){
            stringBuilder.append(fileData);
        }

       // new FileSaver(stringBuilder);
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

    public int getSubjectTotal(){
        return filesData.size();
    }

    /**
     * Gets a subject and id from a file name
     * @param fileName name of file
     * @return subject name and id
     */
    private String getSubject(String fileName){
        return fileName.split("_")[0];
    }

    public void setSet(String set){
        this.set = set;
    }

    public String getSet() {
        return set;
    }
}
