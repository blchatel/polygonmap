package blchatel.polygonmap.swing;

import blchatel.polygonmap.geometry2d.Transform;
import blchatel.polygonmap.geometry2d.Vector;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Our own implementation of awt canvas
 * Represent a canvas where we can draw items (SwingShape)
 * @see SwingShape
 */
public class SwingCanvas extends java.awt.Canvas{

    /// List of item to draw
    private final List<SwingShape> items;

    /// The buffer strategy
    private BufferStrategy strategy;
    /// The zoom level
    private double zoomLevel;
    /// The view center as a vector
    private Vector viewCenter;


    /** Default canvas constructor */
    SwingCanvas(){

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setIgnoreRepaint(true);
        setBackground(Color.WHITE);

        items = new LinkedList<>();

        // TODO Adapt from outside
        zoomLevel = 1500;
        viewCenter = new Vector(500, 500);
    }

    /**
     * Register a Shape for display on canvas
     * @param s (SwingShape): swing version of the initial shape to draw
     */
    public void registerShape(SwingShape s){
        items.add(s);
    }

    /** Remove all items from the drawing list */
    public void clearItems(){
        items.clear();
    }

    /** Refresh the canvas */
    public void refresh(){

        // get dimension
        final int width = getWidth();
        final int height = getHeight();

        float halfX;
        float halfY;
        if (width > height) {
            halfX = 1.0f;
            halfY = (float) height / (float) width;
        } else {
            halfX = (float) width / (float) height;
            halfY = 1.0f;
        }

        // Get the transformation to center the view correctly with correct zoomLevel
        final Transform viewToWorld = Transform.I.scaled(zoomLevel, zoomLevel).translated(viewCenter);
        final Transform worldToView = viewToWorld.inverted();
        final Transform projection = new Transform(width / halfX, 0.0f, 0.5f * width, 0.0f, -height / halfY, 0.5f * height);
        final Transform transform = worldToView.transformed(projection);

        // Setup double buffering if needed
        if (strategy == null) {
            createBufferStrategy(2);
            strategy = getBufferStrategy();
        }

        // Create graphic context
        final Graphics2D graphics = (Graphics2D) strategy.getDrawGraphics();

        // Clear background
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, width, height);

        // Enable anti-aliasing
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Set view transform
        final AffineTransform affine = transform.getAffineTransform();
        graphics.transform(affine);

        // Render ordered drawable
        Collections.sort(items);
        for (SwingShape item : items) {
            item.render(graphics);
        }

        graphics.dispose();
        strategy.show();
        Toolkit.getDefaultToolkit().sync();
    }
}
