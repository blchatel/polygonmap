package blchatel.polygonmap.geometry2d;

import java.awt.geom.Path2D;


/**
 * 2D Vertical definition and implementation with double precision
 * While drawing, assume the vertical defined for y (double) between Integer.MIN_VALUE and Integer.MAX_VALUE (to simplify)
 * The vertical is a shape and define its intersection handler
 * @see Shape
 * @see Function
 * @see Vector
 * @see Random
 * @see ShapeIntersection
 */
public final class Vertical extends Function {

    final double x;

    /**
     * Creates a new Vertical "function".
     * @param x (double): x position of the vertical
     */
    public Vertical(double x) {
        setIntersection(new VerticalIntersection());
        this.x = x;
    }

    /**
     * Creates a new Vertical "function".
     * @param v1 (Vector): Vertical's first point
     * @param v2 (Vector): Vertical's second point
     */
    public Vertical(Vector v1, Vector v2) {
        setIntersection(new VerticalIntersection());
        double dh = v2.x-v1.x;
        if(Math.abs(dh) >= Vector.EPSILON){
            throw new IllegalArgumentException("given Vectors have not the same x value, please create an affine instead");
        }else{
            x = (v2.x + v1.x )/2;
        }
    }

    /**
     * Creates a new Vertical "function".
     * @param x1 (double): Vertical's first point x-coordinate
     * @param y1 (double): Vertical's first point y-coordinate
     * @param x2 (double): Vertical's second point x-coordinate
     * @param y2 (double): Vertical's second point y-coordinate
     */
    public Vertical(double x1, double y1, double x2, double y2) {
        this(new Vector(x1, y1), new Vector(x2, y2));
    }



    @Override
    public String toString() {
        return "v: x = "+x;
    }

    /// Vertical extends Shape

    @Override
    public Path2D toPath() {
        Path2D path = new Path2D.Double();
        path.moveTo(x, Integer.MIN_VALUE);
        path.lineTo(x, Integer.MAX_VALUE);
		return path;
    }


    /// "Vertical extends Function"

    @Override
    public double y(double x) {
        return Double.NaN;
    }

    @Override
    public double x(double y) {
        return x;
    }

    @Override
    public double integral(double a, double b) {
        return Double.NaN;
    }

    @Override
    Vector[] acceptIntersectWith(ShapeIntersection intersection) {
        return intersection.intersectWith(this);
    }

    class VerticalIntersection implements ShapeIntersection{

        @Override
        public Vector[] intersectWith(Rectangle rectangle) {
            if(x < rectangle.x || x > rectangle.x+rectangle.w)
                return new Vector[]{};

            return new Vector[]{new Vector(x, rectangle.y), new Vector(x, rectangle.y+rectangle.h)};
        }


        @Override
        public Vector[] intersectWith(Affine affine) {
            return new Vector[]{new Vector(x, affine.y(x))};
        }

        @Override
        public Vector[] intersectWith(Vertical vertical) {
            if(vertical.x == x)
                return new Vector[]{new Vector(x, 0)};
            return new Vector[]{};
        }
    }
}
