package blchatel.polygonmap.swing;

public interface DrawSupport {

    /**
     * Register a Shape for display on draw support
     * @param s (SwingShape): swing version of the initial shape to draw
     */
    void registerShape(SwingShape s);


    /** Remove all items from the drawing list */
    void clearItems();


    /** Refresh the canvas */
    void refresh();


}
