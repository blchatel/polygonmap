package blchatel.polygonmap.geometry2d;

import java.awt.geom.Path2D;

/**
 * 2D Edge definition and implementation with double precision
 * An edge is a shape defined by its two end vectors
 * @see Shape
 * @see Vector
 * @see Random
 */
public final class Edge extends Shape {

    public final Vector v1, v2;
    public final double length;

    /**
     * Creates a new Edge.
     * @param v1 (Vector): edge's first end
     * @param v2 (Vector): edge's second end
     */
    public Edge(Vector v1, Vector v2) {
        this.v1 = v1;
        this.v2 = v2;
        this.length = v2.subtract(v1).getLength();
    }

    /**
     * Creates a new Edge.
     * @param x1 (double): edge's first end x-coordinate
     * @param y1 (double): edge's first end y-coordinate
     * @param x2 (double): edge's second end x-coordinate
     * @param y2 (double): edge's second end y-coordinate
     */
    public Edge(double x1, double y1, double x2, double y2) {
        this(new Vector(x1, y1), new Vector(x2, y2));
    }


    @Override
    public String toString() {
        return "Edge["+v1 + ", " + v2+"]";
    }


    /// Edge extends Shape

    @Override
    public double surface() {
        return 0.0f;
    }

    @Override
    public double perimeter() {
        return length;
    }

    @Override
    public Vector sample(Random random) {
        return v1.mixed(v2, random.nextDouble());
    }

    @Override
    public boolean contains(Vector v) {
        Vector director = new Vector(v2.x-v1.x, v2.y-v1.y).normalized();
        double lambda1 = (Math.abs(director.x) > Vector.EPSILON) ? (v.x-v1.x)/director.x : Double.NaN;
        double lambda2 = (Math.abs(director.y) > Vector.EPSILON) ? (v.y-v1.y)/director.y : Double.NaN;

        if(lambda1 == Double.NaN)
            return lambda2 >= 0 && lambda2 <= length;
        if(lambda2 == Double.NaN)
            return lambda1 >= 0 && lambda1 <= length;
        return (lambda1 >= 0 && lambda1 <= length && Math.abs(lambda1-lambda2) < Vector.EPSILON);
    }

    @Override
    public Path2D toPath() {
        Path2D path = new Path2D.Double();
		path.moveTo(v1.x, v1.y);
        path.lineTo(v2.x, v2.y);
		return path;
    }

    @Override
    protected Vector[] acceptIntersectWith(ShapeIntersection intersection) {
        return intersection.intersectWith(this);
    }
}
