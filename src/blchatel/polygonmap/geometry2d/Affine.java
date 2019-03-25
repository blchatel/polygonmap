package blchatel.polygonmap.geometry2d;

import java.awt.geom.Path2D;

/**
 * 2D Affine Function definition and implementation with double precision
 * While drawing, assume the function defined for x (double) between Integer.MIN_VALUE and Integer.MAX_VALUE (to simplify)
 * The affine function is a shape and define its intersection handler
 * @see Shape
 * @see Function
 * @see Vector
 * @see Random
 * @see ShapeIntersection
 */
public final class Affine extends Function {

    private final double ordinate;
    private final double slope;

    /**
     * Creates a new Affine.
     * @param slope (double): slope of the linear
     */
    public Affine(double slope, double ordinate) {
        setIntersection(new AffineIntersection());
        this.slope = slope;
        this.ordinate = ordinate;
    }

    /**
     * Creates a new Affine.
     * @param v1 (Vector): affine's first point
     * @param v2 (Vector): affine's second point
     */
    public Affine(Vector v1, Vector v2) {
        double dh = v2.x-v1.x;
        if(Math.abs(dh) < Vector.EPSILON){
            throw new IllegalArgumentException("Invalid Affine function, please init a Vertical instead");
        }else{
            this.slope = (v2.y-v1.y)/dh;
            this.ordinate = v1.y - this.slope*v1.x;
        }
    }

    /**
     * Creates a new Affine.
     * @param x1 (double): affine's first point x-coordinate
     * @param y1 (double): affine's first point y-coordinate
     * @param x2 (double): affine's second point x-coordinate
     * @param y2 (double): affine's second point y-coordinate
     */
    public Affine(double x1, double y1, double x2, double y2) {
        this(new Vector(x1, y1), new Vector(x2, y2));
    }


    /// Affine extends Shape

    @Override
    public Path2D toPath() {
        Path2D path = new Path2D.Double();
        path.moveTo(Integer.MIN_VALUE, y(Integer.MIN_VALUE));
        path.lineTo(Integer.MAX_VALUE, y(Integer.MAX_VALUE));
		return path;
    }

    @Override
    public String toString() {
        return "a: y=" + slope +"x + "+ordinate;
    }

    /// Affine extends Function

    @Override
    public double y(double x) {
        return slope*x+ordinate;
    }

    @Override
    public double x(double y) {
        if(slope==0)
            return 0;
        return (y-ordinate)/slope;
    }

    @Override
    public double integral(double a, double b) {
        return -0.5*(a-b)*(slope*(a+b) + 2*ordinate);
    }

    @Override
    Vector[] acceptIntersectWith(ShapeIntersection intersection) {
        return intersection.intersectWith(this);
    }


    class AffineIntersection implements ShapeIntersection{


        @Override
        public Vector[] intersectWith(Rectangle rectangle) {

            double H = rectangle.y+rectangle.h;
            double W = rectangle.x+rectangle.w;

            Vector i1 = null;
            Vector i2 = null;

            double y0 = y(rectangle.x);
            if(y0 >= rectangle.y && y0 <= H)
                i1 = new Vector(rectangle.x, y0);

            double yW = y(W);
            if(yW >= rectangle.y && yW <= H)
                if(i1 == null)
                    i1 = new Vector(W, yW);
                else {
                    i2 = new Vector(W, yW);
                    return new Vector[]{i1, i2};
                }

            double x0 = x(rectangle.y);
            if(x0 >= rectangle.x && x0 <= W)
                if(i1 == null)
                    i1 = new Vector(x0, rectangle.y);
                else {
                    i2 = new Vector(x0, rectangle.y);
                    return new Vector[]{i1, i2};
                }


            if(i1 == null)
                return new Vector[]{};

            double xH = x(H);
            if(xH >= rectangle.x && xH <= W)
                i2 = new Vector(xH, H);

            return new Vector[]{i1, i2};
        }

        @Override
        public Vector[] intersectWith(Affine affine) {

            if(affine.slope == slope && affine.ordinate != ordinate) return new Vector[]{};
            if(affine.slope == slope) return new Vector[]{new Vector(0, ordinate)};
            double x = (affine.ordinate - ordinate)/(slope - affine.slope);
            double y = slope*x + ordinate;

            return new Vector[]{new Vector(x, y)};
        }

        @Override
        public Vector[] intersectWith(Vertical vertical) {
            return new Vector[]{new Vector(vertical.x, y(vertical.x))};
        }
    }

}
