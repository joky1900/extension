package benchmarking_extension.GUI.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Class for the clear button
 */
public class Clear extends JPanel {

    /**
     * Constructor
     */
    public Clear() {
        setup();
    }

    /**
     * Swing settings
     */
    private void setup(){
        setPreferredSize(new Dimension(80, 50));
        setLayout(new BorderLayout());
        setBackground(new Color(22, 22, 22));

        JLabel label = new JLabel("Clear");
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
                System.out.println("Removing files....");
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
