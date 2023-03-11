package benchmarking_extension;

import benchmarking_extension.menu.Menu;

public class BenchmarkingExtension {
    private final int WIDTH = 1200;
    private final int HEIGHT = 900;

    private final MainFrame mainFrame = new MainFrame(WIDTH, HEIGHT);

    public BenchmarkingExtension() {
        // Initialize main frame
        mainFrame.setJMenuBar(new Menu(this));
    }

}
