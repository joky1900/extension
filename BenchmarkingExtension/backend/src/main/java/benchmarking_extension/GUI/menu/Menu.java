package benchmarking_extension.GUI.menu;

import benchmarking_extension.GUI.GraphicalUserInterface;
import benchmarking_extension.GUI.FileChooser;
import benchmarking_extension.graph.GraphType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;


/**
 * <h1>Menu</h1>
 * Menu object that contains sub-menus and items
 * @author John Kyrk
 * @version 1.0
 * @since 2023-03-01
 */
public class Menu extends JMenuBar {
    private static final long serialVersionUID = 1L;
    private GraphicalUserInterface graphicalUserInterface;
    private final String PATH = "./src/main/resources/";
    private final ButtonListener listener = new ButtonListener();

    private final JButton chooseFileButton = new JButton("Choose Files");
    private final ClearMenu clearMenu = new ClearMenu();
    private final SaveToCSVMenu saveToCSVMenu = new SaveToCSVMenu();

    private final JPanel colorChoice = new JPanel();

    /**
     * Default Constructor
     * @param benchmarkingExtension main class for the program
     */
    public Menu(GraphicalUserInterface benchmarkingExtension){
        this.graphicalUserInterface = benchmarkingExtension;
        chooseFileButton.addActionListener(listener);
        chooseFileButton.setActionCommand("choose");
        init();
    }


    /**
     * Set up the menu
     */
    private void init(){
        this.setPreferredSize(new Dimension(500, 75));
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(22, 22, 22));
        this.colorChoice.setPreferredSize(new Dimension(50, 50));
        this.changeColorSquare(new Color(0, 0, 0));

        // Menu items
        try {
            this.add(chooseFileButton);
            this.add(getShapeMenu());
            this.add(Box.createRigidArea(new Dimension(20, 0)));
            this.add(getSlider());
            this.add(Box.createRigidArea(new Dimension(20, 0)));
            this.add(getColorMenu());
            this.add(colorChoice);
            this.add(Box.createRigidArea(new Dimension(20, 0)));
            this.add(clearMenu);
            this.add(Box.createRigidArea(new Dimension(20, 0)));
            this.add(saveToCSVMenu);
        } catch(Exception e){
            System.out.println("Could not load images!");
        }
    }

    /**
     * Sub-menu of shapes
     * @return JMenu object
     * @throws IOException FileNotFoundException
     */
    private JMenu getShapeMenu() throws IOException {
        JMenu menu = new JMenu("Graph Type");
        menu.setForeground(new Color(182, 143, 0));

        MenuItem menuItem = new MenuItem("Bar Graph");
        menuItem.addActionListener(e -> changeGraphType(GraphType.BAR));
        menu.add(menuItem);

        menuItem = new MenuItem("Line Graph");
        menuItem.addActionListener(e -> changeGraphType(GraphType.LINE));
        menu.add(menuItem);

        return menu;
    }

    private FileChooser getFileChooser(){
        return new FileChooser();
    }

    /**
     * Sub-menu in form of a slider to change background color
     * @return JPanel containing a JSlider
     */
    private JPanel getSlider(){
        final int min = 0;
        final int max = 20;
        final int init = 5;

        // Create and set up JSlider
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, init);
        slider.setBackground(new Color(22, 22, 22));
        slider.setForeground(new Color(182, 143, 0));
        slider.setFont(new Font(slider.getFont().getName(), Font.PLAIN, 8));
        slider.setPreferredSize(new Dimension(120, 35));
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        // Add listener
   //     slider.addChangeListener(e -> changeBrushSize(slider.getValue()));

        // Container
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setPreferredSize(new Dimension(120, 35));
        panel.add(slider);

        // Header
        JLabel header = new JLabel("Size");
        header.setForeground(new Color(182, 143, 0));

        // Container
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(22, 22, 22));

        // Connect Swing objects
        mainPanel.add(header);
        mainPanel.add(panel);

        return mainPanel;
    }

    private JPanel getColorMenu(){
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(140, 50));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(22, 22, 22));


        JPanel redColorSlider = getColorSlider("R");
        JPanel greenColorSlider = getColorSlider("G");
        JPanel blueColorSlider = getColorSlider("B");

        mainPanel.add(redColorSlider);
        mainPanel.add(greenColorSlider);
        mainPanel.add(blueColorSlider);

        return mainPanel;
    }

    private JPanel getColorSlider(final String color){
        int min = 0;
        int max = 255;
        int init = 0;

        // Create and set up JSlider
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, init);
        slider.setBackground(new Color(22, 22, 22));
        slider.setFont(new Font(slider.getFont().getName(), Font.PLAIN, 8));
        slider.setPreferredSize(new Dimension(120, 35));

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(140, 35));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        // Header
        JLabel header = new JLabel(color);
        header.setForeground(new Color(182, 143, 0));
        panel.setBackground(new Color(22, 22, 22));


        // Add listener
        slider.addChangeListener(e -> changeColor(color, slider.getValue()));

        panel.add(header);
        panel.add(slider);

        return panel;
    }

    /**
     * Creates a FileChooser object and lets the user choose files
     * @return array of files
     */
    public File[] getSelectedFiles(){
        FileChooser fc = new FileChooser();
        fc.chooseFile();
        return fc.getFiles();
    }


    //-------------------------------------------------------------------------
    // Private Mutators
    //-------------------------------------------------------------------------

    private void changeGraphType(GraphType type) {
        graphicalUserInterface.changeGraphType(type);
    }
/**
    private void changeBrushSize(final int size) {
        benchmarkingExtension.changeBrushSize(size);
    }
**/
    private void changeColor(String color, int value){
        graphicalUserInterface.changeColor(color, value);
        changeColorSquare(graphicalUserInterface.getColor());
    }

    //-------------------------------------------------------------------------
    // Public Mutators
    //-------------------------------------------------------------------------
    public void changeColorSquare(Color color){
        this.colorChoice.setBackground(color);
    }

    /**
     * Inner class working as a swing action-listener.
     *
     * @author John Kyrk
     */
    public static class ButtonListener implements ActionListener {

        public ButtonListener() { }

        /**
         * Handles actions performed by the user
         *
         * @param actionEvent auto created ActionEvent object
         */
        @Override
        public void actionPerformed(final ActionEvent actionEvent) {
            // Get action identifier
            String action = actionEvent.getActionCommand();
            // Switch statement determining user input
            switch (action) {

                //------------------------------------------------------------
                // CHOOSE FILE BUTTON PRESSED
                //------------------------------------------------------------
                case "choose":
                    FileChooser fc = new FileChooser();
                    fc.chooseFile();
                    File[] files = fc.getFiles();
                    System.out.println(files);

                    /**
                    model.setFiles(files);
                    GUI.updateSelectedFilesLabel(generateFileNamesOutput(files));

                    if (model.filesAreHuffman()) {
                        GUI.disableCompressButton();
                        GUI.enableDecompressButton();
                        GUI.disableFileNameTextField();
                        GUI.setError("Click unpack to unpack the file!");
                    } else if (files.length == 1) {
                        GUI.enableCompressButton();
                        GUI.disableDecompressButton();
                        GUI.enableFileNameTextField();
                        GUI.setError("Please enter a file name:");
                    } else {
                        GUI.setError("Can only compress one file at a time!");
                    }

                    break;
                     **/
            }
        }
    }
}

