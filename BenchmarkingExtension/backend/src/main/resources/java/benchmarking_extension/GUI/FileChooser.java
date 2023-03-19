package benchmarking_extension.GUI;

import javax.swing.*;
import java.io.File;

/**
 * Wraps a JFileChooser object within a panel and provides public accessors
 * @author John Kyrk
 */
public class FileChooser extends JPanel {
    private final String DEFAULT_DIRECTORY = "src/main/resources/";
    private final JFileChooser jFileChooser = new JFileChooser(DEFAULT_DIRECTORY);
    private final File[] NO_FILE = {};
    private File[] files = {};

    /**
     * Default Constructor
     */
    public FileChooser(){
        jFileChooser.setMultiSelectionEnabled(true);
    }

    /**
     * Opens the file choosing window and lets the user select files
     */
    public void chooseFile(){
        // Show window and store action
        int rVal = jFileChooser.showOpenDialog(this);

        // Load files
        loadFiles(rVal);
    }

    /**
     * Loads files into the local member depending on user choice
     * @param rVal user choice
     */
    private void loadFiles(final int rVal){
        // User pressed open
        if(rVal == JFileChooser.APPROVE_OPTION){
            setFiles(jFileChooser.getSelectedFiles());
            // User pressed Cancel
        }else if(rVal == JFileChooser.CANCEL_OPTION){
            setFiles(NO_FILE);
            // User did something else
        }else{
            setFiles(null);
        }
    }

    /**
     * Set local member files
     * @param files array of files
     */
    private void setFiles(File[] files){
        this.files = files;
    }

    /**
     * public accessor retrieving files
     * @return array of files
     */
    public File[] getFiles(){
        return files;
    }
}
