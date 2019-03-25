package blchatel.polygonmap.geometry2d;


/**
 * 2D Function abstract definition
 * A function is a shape that provide a y(x), a x(y) and a integral(a, b) functions
 * While drawing, assume the function defined for x (double) between Integer.MIN_VALUE and Integer.MAX_VALUE (to simplify)
 * @see Shape
 * @see Vector
 * @see Random
 */
public abstract class Function extends Shape {

    /**
     * Compute the y-value of the function for given x
     * @param x (double): given x
     * @return (double): y value
     */
    public abstract double y(double x);


    /**
     * Compute one of the x-value (if exists) of the function for given y
     * @param y (double): given y
     * @return (double): one of the x values if exists or NaN otherwise
     */
    public abstract double x(double y);

    /**
     * Compute the mathematical integral (may be negative) of the function between a and b bound
     * @param a (double): a-bound on x
     * @param b (double): b-bound on x
     * @return (double): the area between a and b (may be negative)
     */
    public abstract double integral(double a, double b);


    /// Function extends Shape

    @Override
    public double surface() {
        return 0.0f;
    }

    @Override
    public double perimeter() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public Vector sample(Random random) {
        //To simplify the interval is big enough
        double x = random.nextInt();
        return new Vector(x, y(x));
    }

    @Override
    public boolean contains(Vector v) {
        return Math.abs(y(v.x)-v.y) < Vector.EPSILON;
    }
}
