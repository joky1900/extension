package benchmarking_extension.GUI.menu;

import benchmarking_extension.GUI.GraphicalUserInterface;
import benchmarking_extension.GUI.FileChooser;
import benchmarking_extension.graph.GraphType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


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

    private final ChooseFile chooseFileButton = new ChooseFile();
    private final Clear clear = new Clear();
    private final SaveImage saveImage = new SaveImage();
    private final SaveToCSV saveToCSVMenu = new SaveToCSV();

    private final JPanel colorChoice = new JPanel();

    /**
     * Default Constructor
     * @param benchmarkingExtension main class for the program
     */
    public Menu(GraphicalUserInterface benchmarkingExtension){
        this.graphicalUserInterface = benchmarkingExtension;
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
            this.add(clear);
            this.add(saveToCSVMenu);
            this.add(saveImage);
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
        final int min = 5;
        final int max = 25;
        final int init = 15;

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
        slider.addChangeListener(e -> GraphicalUserInterface.changeTextSize(slider.getValue()));

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
        int init = 255;

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
      //  graphicalUserInterface.changeGraphType(type);
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
}

