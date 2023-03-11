package benchmarking_extension.menu;

import benchmarking_extension.BenchmarkingExtension;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
    private BenchmarkingExtension benchmarkingExtension;
    private final String PATH = "./src/main/resources/";

    private final JPanel colorChoice = new JPanel();

    /**
     * Default Constructor
     * @param benchmarkingExtension main class for the program
     */
    public Menu(BenchmarkingExtension benchmarkingExtension){
        this.benchmarkingExtension = this.benchmarkingExtension;
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
            this.add(getShapeMenu());
            this.add(Box.createRigidArea(new Dimension(20, 0)));
            this.add(getSlider());
            this.add(Box.createRigidArea(new Dimension(20, 0)));
            this.add(getColorMenu());
            this.add(colorChoice);
            this.add(Box.createRigidArea(new Dimension(20, 0)));
            this.add(getClearMenu());
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
        JMenu menu = new JMenu("Shape");
        menu.setForeground(new Color(182, 143, 0));

        // Free hand option
        MenuItem menuItem = new MenuItem(ImageIO.read(new File(PATH + "freehand.png")));
     //   menuItem.addActionListener(e -> changeBrushType(BrushType.DOTS));
        menu.add(menuItem);

        // Line option
        menuItem = new MenuItem(ImageIO.read(new File(PATH + "line.png")));
    //    menuItem.addActionListener(e -> changeBrushType(BrushType.LINE));
        menu.add(menuItem);

        // Square option
        menuItem = new MenuItem(ImageIO.read(new File(PATH + "square.png")));
    //    menuItem.addActionListener(e ->  changeBrushType(BrushType.SQUARE));
        menu.add(menuItem);

        // Oval option
        menuItem = new MenuItem(ImageIO.read(new File(PATH + "oval.png")));
    //    menuItem.addActionListener(e -> changeBrushType(BrushType.OVAL));
        menu.add(menuItem);

        return menu;
    }

    /**
     * Sub-menu in form of a slider for brush size within a JPanel
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
      //  slider.addChangeListener(e -> changeColor(color, slider.getValue()));

        panel.add(header);
        panel.add(slider);

        return panel;
    }

    /**
     * Clear button to clear canvas.
     * @return {@link JPanel} object
     */
    private JPanel getClearMenu(){
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(140, 50));
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(22, 22, 22));

        JLabel label = new JLabel("Clear");
        label.setForeground(new Color(182, 143, 0));
        label.setBackground(new Color(22, 22, 22));

        panel.add(label, BorderLayout.CENTER);

        // label.addActionListener(e -> clearDrawing());
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
          //      clearDrawing();
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        return panel;
    }

    //-------------------------------------------------------------------------
    // Private Mutators
    //-------------------------------------------------------------------------
    /**
    private void changeBrushType(BrushType type) {
        benchmarkingExtension.changeBrushType(type);
    }

    private void changeBrushSize(final int size) {
        benchmarkingExtension.changeBrushSize(size);
    }

    private void changeColor(String color, int value){
        benchmarkingExtension.changeColor(color, value);
        changeColorSquare(benchmarkingExtension.getColor());
    }

    private void clearDrawing(){
        benchmarkingExtension.clearDrawing();
    }
*/
    //-------------------------------------------------------------------------
    // Public Mutators
    //-------------------------------------------------------------------------
    public void changeColorSquare(Color color){
        this.colorChoice.setBackground(color);
    }
}

