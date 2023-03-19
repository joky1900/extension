package benchmarking_extension.GUI.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Clear extends JPanel {

    public Clear() {
        setPreferredSize(new Dimension(140, 50));
        setLayout(new BorderLayout());
        setBackground(new Color(22, 22, 22));

        JLabel label = new JLabel("Clear");
        label.setForeground(new Color(182, 143, 0));
        label.setBackground(new Color(22, 22, 22));

        add(label, BorderLayout.CENTER);

        actionListener();
    }



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
