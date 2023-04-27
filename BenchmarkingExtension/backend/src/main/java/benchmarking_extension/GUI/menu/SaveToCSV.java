package benchmarking_extension.GUI.menu;

import benchmarking_extension.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Implements the save to CSV button
 */
public class SaveToCSV extends JPanel {

    /**
     * Constructor
     */
    public SaveToCSV(){
        setPreferredSize(new Dimension(140, 50));
        setLayout(new BorderLayout());
        setBackground(new Color(22, 22, 22));

        JLabel label = new JLabel("Save to CSV");
        label.setForeground(new Color(182, 143, 0));
        label.setBackground(new Color(22, 22, 22));

        add(label, BorderLayout.CENTER);

        actionListener();
    }

    /**
     * Implements the mouse listener
     */
    private void actionListener() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                System.out.println("Saving to CSV...");
                Controller.saveToCSV();
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
