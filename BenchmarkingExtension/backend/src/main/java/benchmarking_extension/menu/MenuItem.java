package benchmarking_extension.menu;

import javax.swing.*;
import java.awt.*;

/**
 * <h1>MenuItem</h1>
 * <p>
 *     Menu item object extending Swing's JMenuItem.
 *     Includes a background image.
 * </p>
 * @author John Kyrk
 * @version 1.0
 * @since 2023-03-01
 */
public class MenuItem extends JMenuItem {
    Image image;

    /**
     * Constructor with text as visual representation
     * @param text text for menu item
     */
    public MenuItem(String text){
        super(text);
        image = null;
        initialize();

    }

    /**
     * Constructor with an image as visual representation
     * @param image image to be displayed
     */
    public MenuItem(Image image){
        this.image = image;
        initialize();
    }

    /**
     * Initialize menu item by setting it's preferred size
     */
    private void initialize(){
        setPreferredSize(new Dimension(100, 25));
    }

    /**
     * Override Swing paintComponent and draw the background image
     * @param g AWT Graphics Object
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if(image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }
}