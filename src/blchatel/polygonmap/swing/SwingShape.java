package blchatel.polygonmap.swing;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Shape;


/**
 * Draw a single shape.
 */
public final class SwingShape implements Comparable<SwingShape> {

	private final Shape shape;
	private final Color fillColor;
	private final Color outlineColor;
	private final float thickness;
	private final float alpha;
	private final float depth;

    /**
     * Creates a new shape item.
     * @param shape (Shape): Swing shape, not null
     * @param fillColor (Color): fill color, may be null
     * @param outlineColor (Color): outline color, may be null
     * @param thickness (double): outline thickness
     * @param alpha (double): transparency, between 0 (invisible) and 1 (opaque)
     * @param depth (double): associated depth
     */
	public SwingShape(Shape shape, Color fillColor, Color outlineColor, double thickness, double alpha, double depth) {
		super();
		this.shape = shape;
		this.fillColor = fillColor;
		this.outlineColor = outlineColor;
		this.thickness = (float)thickness;
		this.alpha = (float)alpha;
		this.depth = (float)depth;
	}

	/**
	 * Creates a new shape item.
	 * @param shape (Shape): Swing shape, not null
	 * @param fillColor (Color): fill color, may be null
	 * @param alpha (double): transparency, between 0 (invisible) and 1 (opaque)
	 * @param depth (double): associated depth
	 */
	public SwingShape(Shape shape, Color fillColor, double alpha, double depth) {
		this(shape, fillColor, null, 0.0, alpha, depth);
	}

	/**
	 * Creates a new shape item.
	 * @param shape (Shape): Swing shape, not null
	 * @param fillColor (Color): fill color, may be null
	 * @param depth (double): associated depth
	 */
	public SwingShape(Shape shape, Color fillColor, double depth) {
		this(shape, fillColor, null, 0.0, 1.0, depth);
	}

	/**
	 * Creates a new shape item.
	 * @param shape (Shape): Swing shape, not null
	 * @param fillColor (Color): fill color, may be null
	 */
	public SwingShape(Shape shape, Color fillColor) {
		this(shape, fillColor, null, 0.0, 1.0, 0);
	}


	/**
	 * Render this swing shape on the given graphics
	 * @param g (Graphics2D): where the shape is drawn
	 */
	public void render(Graphics2D g) {
        // Deal with transparency
	    if (alpha <= 0.0f)
            return;
        Composite old = null;
        if (alpha < 1.0) {
            old = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }
        // Fill the shape if needed
		if (fillColor != null) {
			g.setColor(fillColor);
			g.fill(shape);
		}
		// Draw the outline of the shape if needed
		if (outlineColor != null) {
			g.setColor(outlineColor);
			g.setStroke(new BasicStroke(thickness));
			g.draw(shape);
		}
		// restore the composite for next non-alpha
        if (old != null)
            g.setComposite(old);
	}

	/// SwingShape implements Comparable

	@Override
	public int compareTo(SwingShape o) {
		return Float.compare(depth, o.depth);
	}
}
