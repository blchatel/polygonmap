/* Copyright 2019 Bastien Chatelain */

package blchatel.geometry2d;

import java.io.Serializable;

/**
 * 2D Vector definition and implementation with double precision
 * A vector is defined by its xy-value
 */
public final class Vector implements Serializable{

    /** Small value for double precision in vector comparison */
    public static final double EPSILON = 10E-6;

    /** The zero vector (0, 0) */
    public static final Vector ZERO = new Vector(0.0f, 0.0f);

    public final static Vector X = new Vector(1, 0);
    public final static Vector Y = new Vector(0, 1);


    // Point value
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


    /** @return (double): euclidian length */
    public double getLength() {
        return Math.sqrt(x*x + y*y);
    }

    /** @return (double): angle in standard trigonometrical system, in radians */
    public double getAngle() {
        return Math.atan2(y, x);
    }

    /** @return (Vector): negated vector */
    public Vector opposite() {
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
        return "Vector".hashCode() ^ Double.hashCode(x) ^ Double.hashCode(y);
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
        return "Vector(x:" + x + ", y: " + y + ")";
    }

}
