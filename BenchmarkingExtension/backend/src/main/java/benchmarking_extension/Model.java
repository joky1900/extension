package benchmarking_extension;

import java.io.File;

public class Model {
    private File[] files;
    private String fileName;
    private final String PATH = "src/main/resources/";



    /**
     * Public mutator setting the array of file objects.
     * @param files array of files
     */
    public void setFiles(File[] files) {
        this.files = files;
    }

    /**
     * Public mutator setting the name of the file.
     * @param fileName name of file
     */
    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getFileExtension(String fileName){
        String[] splitFileName = fileName.split("\\.");

        if(splitFileName.length == 1){
            return fileName;
        }else{
            return splitFileName[splitFileName.length - 1];
        }
    }



}
