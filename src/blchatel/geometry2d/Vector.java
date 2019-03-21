/* Copyright 2019 Bastien Chatelain */

package blchatel.geometry2d;

/**
 * 2D Vector definition and implementation with double precision
 * A vector is defined by its xy-value
 */
public final class Vector{

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
     * Create the vector between the two given points
     * @param origin (Point): origin
     * @param ordinate (Point): ordinate
     */
    public Vector(Point origin, Point ordinate){
        this.x = ordinate.x -origin.x;
        this.y = ordinate.y -origin.y;
    }


    /**
     * Deep Copy Vector constructor
     * @param v (Vector): the vector to copy
     */
    public Vector(Vector v) {
        this.x = v.x;
        this.y = v.y;
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
     * Rotate this vector around its origin by an angle anticlockwise
     * @param angle (double): rotation angle in radian
     * @return (Vector): the resulting Vector
     */
    public Vector rotate(double angle){
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector(cos*x - sin*y, sin*x + cos*y);
    }


    @Override
    public String toString() {
        return "Vector(x:" + x + ", y: " + y + ")";
    }

}
