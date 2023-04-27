package benchmarking_extension.GUI.menu;

import benchmarking_extension.GUI.GraphicalUserInterface;
import benchmarking_extension.graph.GraphType;
import benchmarking_extension.Controller;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


/**
 * <h1>Menu</h1>
 * Menu object that contains sub-menus and items
 * @author John Kyrk
 * @version 1.0
 * @since 2023-04-23
 */
public class Menu extends JMenuBar {
    private static final long serialVersionUID = 1L;
    private final String PATH = "./src/main/resources/";

    private final ChooseFile chooseFileButton = new ChooseFile();
    private static final JMenu subjectMenu = new JMenu("Subject");
    private static final JMenu setMenu = new JMenu("Set");
    private final Clear clear = new Clear();
    private final SaveImage saveImage = new SaveImage();
    private final SaveToCSV saveToCSVMenu = new SaveToCSV();

    private final JPanel colorChoice = new JPanel();

    /**
     * Default Constructor
    */
    public Menu(){
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
        this.changeColorSquare(new Color(255, 255, 255));

        // Menu items
        try {
            updateSubjectNumberMenu();
            updateSetNumberMenu();
            this.add(chooseFileButton);
            this.add(subjectMenu);
            this.add(setMenu);
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
     * Sub-menu graph types
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

    /**
     * Sub menu for updating subject number
     * @throws IOException FileNotFoundException
     */
    public void updateSubjectNumberMenu() throws IOException {
        subjectMenu.removeAll();
        subjectMenu.setForeground(new Color(182, 143, 0));

        int total = Controller.getSubjectTotal();

        for(int i = 0; i < total; ++i) {
            MenuItem menuItem = new MenuItem(String.valueOf(i));
            int finalI = i;
            menuItem.addActionListener(e -> changeSubjectNumber(finalI));
            subjectMenu.add(menuItem);
        }
    }

    /**
     * Sub menu for updating the subject set number
     */
    public void updateSetNumberMenu(){
        setMenu.setForeground(new Color(182,143,0));

        MenuItem menuItem = new MenuItem("X");
        menuItem.addActionListener(e -> changeSet("X"));
        setMenu.add(menuItem);

        menuItem = new MenuItem("Y");
        menuItem.addActionListener(e -> changeSet("Y"));
        setMenu.add(menuItem);

        menuItem = new MenuItem("Avg");
        menuItem.addActionListener(e -> Controller.changeSet("A"));
        setMenu.add(menuItem);

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

    /**
     * Creates the color menu
     * @return {@link JPanel}
     */
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

    /**
     * Creates the color slider
     * @param color default color
     * @return {@link JPanel}
     */
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

    //-------------------------------------------------------------------------
    // Private Mutators
    //-------------------------------------------------------------------------

    /**
     * Changes the type of graph being displayed
     * @param type {@link GraphType}
     */
    private void changeGraphType(GraphType type) {
        try {
            Controller.changeGraphType(type);
        } catch (Exception e){
            System.out.println("Could not change graph type!");
        }
    }

    /**
     * Changes the subject number
     * @param i index
     */
    private void changeSubjectNumber(int i){
        Controller.changeSubjectID(i);
    }

    /**
     * Changes the set type
     * @param set x or y
     */
    private void changeSet(String set){
        Controller.changeSet(set);
    }

    /**
     * Changes the background color
     * @param color new color
     * @param value rgb value (0-255)
     */
    private void changeColor(String color, int value){
        GraphicalUserInterface.changeColor(color, value);
        changeColorSquare(GraphicalUserInterface.getBackgroundColor());
    }

    //-------------------------------------------------------------------------
    // Public Mutators
    //-------------------------------------------------------------------------

    /**
     * Changes the square representing the current background color
     * @param color new color
     */
    public void changeColorSquare(Color color){
        this.colorChoice.setBackground(color);
    }
}

