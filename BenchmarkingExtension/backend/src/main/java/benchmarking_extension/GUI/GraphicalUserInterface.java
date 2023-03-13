package benchmarking_extension.GUI;

import benchmarking_extension.GUI.menu.Menu;
import benchmarking_extension.graph.*;

import java.awt.*;

public class GraphicalUserInterface {
    private final int WIDTH = 1200;
    private final int HEIGHT = 900;

    private final MainFrame mainFrame = new MainFrame(WIDTH, HEIGHT);
    private final Menu menu = new Menu(this);

    // Default graph is a bar graph
    private final Graph graph = new BarGraph();

    public GraphicalUserInterface(){
        // Initialize main frame
        mainFrame.setJMenuBar(menu);

        LineChartTest lineChartTest = new LineChartTest();
        mainFrame.add(lineChartTest);
    }

    public void changeGraphType(GraphType type){
        switch(type){
            case BAR -> new BarGraph();
            case LINE -> new LineGraph();
        }
    }

    public void changeColor(String color, int value){
        Color tmpColor = graph.getColor();

        switch(color){
            case "R" -> {
                graph.setColor(new Color(value, tmpColor.getGreen(), tmpColor.getBlue()));
            }
            case "G" -> {
                graph.setColor(new Color(tmpColor.getRed(), value, tmpColor.getBlue()));
            }
            case "B" -> {
                graph.setColor(new Color(tmpColor.getRed(), tmpColor.getGreen(), value));
            }
        }
    }

    public Color getColor(){
        return graph.getColor();
    }


}
