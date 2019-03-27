package blchatel.polygonmap.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * The UI for polygonmap application composed of a frame with menu and two main components:
 * a canvas for drawing, and an option panel
 * @see SwingCanvas
 * @see SwingOptionPanel
 */
public class SwingWindow {

    /// The frame
    private final JFrame frame;
    /// The option panel (EAST)
    private SwingOptionPanel optionPanel;
    /// The canvas (CENTER)
    private DrawSupport canvas;


    /**
     * Create the UI with menu, canvas and option panel
     * @param title (String): title of the window
     * @param width (int): width dimension of the window
     * @param height (int): height dimension of the window
     */
    public SwingWindow(String title, int width, int height){

        // Create Swing frame
        frame = new JFrame(title);
        frame.add(buildContent());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Menu bar
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu();
        menu.setText("Mon Menu");
        mb.add(menu);
        frame.setJMenuBar(mb);

        // Show frame
        frame.pack();
        frame.setSize(width, height);
        frame.setVisible(true);
    }

    /** Build window content (Canvas + Options)*/
    private Component buildContent(){
        final JPanel component = new JPanel();
        component.setLayout(new BorderLayout());

        canvas = new SwingCanvas();
        component.add((SwingCanvas)canvas, BorderLayout.CENTER);

        optionPanel = new SwingOptionPanel();
        component.add(optionPanel, BorderLayout.EAST);

        /*
        SwingUtilities.invokeLater(() -> component.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent evt) {
                refresh();
            }
        }));
        */

        return component;
    }

    public DrawSupport getSupport() {
        return canvas;
    }

    /** Refresh all the component when called */
    public void refresh() {
        canvas.refresh();
    }
}
