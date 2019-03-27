/* Copyright 2019 Bastien Chatelain */

package blchatel.polygonmap.geometry2d;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.io.Serializable;

/**
 * 2D Vector definition and implementation with double precision
 * A vector is a shape defined by its xy-value
 * @see Shape
 * @see Random
 */
public final class Vector extends Shape implements Serializable{

    /** Small value for double precision in vector comparison */
    public static final double EPSILON = 10E-6;
    /** Radius size in pixel while drawing the vector*/
    private static final int VIZ_RADIUS_PIXEL = 3;
    /** The zero vector (0, 0) */
    public static final Vector ZERO = new Vector(0.0f, 0.0f);
    /** X unit vector */
    public final static Vector X = new Vector(1, 0);
    /** Y unit vector*/
    public final static Vector Y = new Vector(0, 1);

    /// Vector coordinates
    public final double x, y;

    /**
     * Default Vector constructor
     * @param x (double): x-coordinate
     * @param y (double): y-coordinate
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Complementary Vector constructor from single precision
     * @param x (int): x-coordinate
     * @param y (int): y-coordinate
     */
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Deep Copy Vector constructor
     * @param v (Vector): the vector to copy
     */
    public Vector(Vector v) {
        this.x = v.x;
        this.y = v.y;
    }

    /** @return (double): euclidian length */
    public double getSqrLength() {
        return x*x + y*y;
    }

    /**
     * Compute the squared distance between this and another vector
     * @param other (Vector): the other vector
     * @return (double): the squared distance
     */
    public double sqrDstTo(Vector other){
        return (x-other.x)*(x-other.x) + (y-other.y)*(y-other.y);
    }

    /** @return (double): euclidian length */
    public double getLength() {
        return Math.sqrt(x*x + y*y);
    }

    /** @return (double): angle in standard trigonometrical system, in radians */
    public double getAngle() {
        return Math.atan2(y, x);
    }

    /** @return (Vector): the opposite vector (-x, -y) */
    public Vector opposite(){
        return new Vector(-x, -y);
    }

    /**
     * Add a vector to this vector
     * @param v (Vector): the vector to add
     * @return (Vector): the resulting Vector
     */
    public Vector add(Vector v){
        return new Vector(x + v.x, y + v.y);
    }

    /**
     * Subtract a vector to this vector
     * @param v (Vector): the vector to subtract
     * @return (Vector): the resulting Vector
     */
    public Vector subtract(Vector v){
        return new Vector(x - v.x, y - v.y);
    }

    /**
     * Scale this vector by a factor
     * @param factor (double): the division factor
     * @return (Vector): the resulting Vector
     */
    public Vector scale(double factor){
        return new Vector(x*factor, y*factor);
    }

    /**
     * @param other (Vector): right-hand operand, not null
     * @return (Vector): dot product
     */
    public double dot(Vector other) {
        return x * other.x + y * other.y;
    }

    /**
     * Rotate this vector around its origin by an angle anticlockwise
     * @param angle (double): rotation angle in radian
     * @return (Vector): the resulting Vector
     */
    public Vector rotate(double angle){
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector(cos*x - sin*y, sin*x + cos*y);
    }

    /**
     * Computes linear interpolation between two vectors.
     * @param other (Vector): second vector, not null
     * @param factor (double) weight of the second vector
     * @return (Vector): interpolated vector, not null
     */
    public Vector mixed(Vector other, double factor) {
        return new Vector(x * (1.0f - factor) + other.x * factor, y * (1.0f - factor) + other.y * factor);
    }

    /**
     * Computes unit vector of same direction, or (1, 0) if zero.
     * @return (Vector): rescaled vector, not null
     */
    public Vector normalized() {
        double length = getLength();
        if (length > 1e-6)
            return scale(1.0/length);
        return Vector.X;
    }

    /// Vector implements Serializable

    @Override
    public int hashCode() {
        return Double.hashCode(x) ^ Double.hashCode(y);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Vector))
            return false;
        Vector other = (Vector)object;
        return Math.abs((x-other.x) + (y-other.y)) < EPSILON;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /// Vector extends Shape

    @Override
    public boolean contains(Vector v) {
        return equals(v);
    }

    @Override
    public double perimeter() {
        return 0;
    }

    @Override
    public double surface() {
        return 0;
    }

    @Override
    public Vector sample(Random random) {
        return this;
    }

    @Override
    public Path2D toPath() {

        Ellipse2D ellipse = new Ellipse2D.Double(
                x - VIZ_RADIUS_PIXEL,
                y - VIZ_RADIUS_PIXEL,
                VIZ_RADIUS_PIXEL * 2,
                VIZ_RADIUS_PIXEL * 2
        );
        return new Path2D.Double(ellipse);
    }

    @Override
    protected Vector[] acceptIntersectWith(ShapeIntersection intersection) {
        return intersection.intersectWith(this);
    }
}
