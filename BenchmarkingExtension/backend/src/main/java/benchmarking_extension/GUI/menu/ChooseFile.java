package benchmarking_extension.GUI.menu;

import benchmarking_extension.Controller;
import benchmarking_extension.GUI.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Class for importing files into the program. Utilizes FileChooser
 */
public class ChooseFile extends JPanel{
    private final int width = 140;
    private final int height = 50;

    public ChooseFile(){
        swingSetup();
    }

    private void swingSetup(){
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());
        setBackground(new Color(22, 22, 22));

        JLabel label = new JLabel("Choose Files");
        label.setForeground(new Color(182, 143, 0));    // Goldenrod
        label.setBackground(new Color(22, 22, 22));     // Dark gray

        add(label, BorderLayout.CENTER);

        actionListener();
    }


    private void actionListener() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                FileChooser fc = new FileChooser();
                fc.chooseFile();
                Controller.setFiles(fc.getFiles());
                Controller.loadJSON();
                Controller.updateGraph();
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
    }
}