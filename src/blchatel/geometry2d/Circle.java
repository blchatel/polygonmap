/* Copyright 2019 Bastien Chatelain */

package blchatel.geometry2d;

import blchatel.random.Random;

/**
 * Circle definition and implementation with double precision
 * A circle is defined by its center Vector and its radius
 */
public class Circle extends Shape{

    // Center of the circle
    public final Vector c;
    // Radius of the circle
    public final double r;

    /**
     * Default Circle constructor
     * @param center (Vector): center of the circle to create
     * @param radius (double): radius of the circle to create
     */
    public Circle(Vector center, double radius){
        c = center;
        r = radius;
    }

    /**
     * Extended Circle constructor
     * @param cx (double): center x-coordinate of the circle to create
     * @param cy (double): center y-coordinate of the circle to create
     * @param radius (double): radius of the circle to create
     */
    public Circle(double cx, double cy, double radius){
        c = new Vector(cx, cy);
        r = radius;
    }

    /**
     * Deep Copy Circle constructor
     * @param circle (Circle): the circle to copy
     */
    public Circle(Circle circle){
        c = circle.c;
        r = circle.r;
    }

    /**
     * Compute the (x, y) coordinates of the Vector for angle phi
     * Assume origin of phi is the x-axis parallel passing by circle center (from polar coordinate)
     * @param phi (double): angle of the Vector in radian (0 <= phi < 2pi)
     * @return (Vector): the Vector on the circle for phi angle
     */
    public Vector getPoint(double phi){
        return new Vector(c.x + r*Math.cos(phi), c.y + r*Math.sin(phi));
    }


    @Override
    public String toString() {
        return "Circle(c:" + c.toString() + ", r:" + r + ")";
    }


    /// Circle extends Shape

    @Override
    public boolean contains(Vector v){
        return c.subtract(v).getSqrLength() == r*r;
    }

    @Override
    public double perimeter() {
        return 2*Math.PI*r;
    }

    @Override
    public double surface() {
        return Math.PI *r*r;
    }

    @Override
    public Vector sample(Random random) {
        double phi = 2*Math.PI*random.nextDouble();
        return getPoint(phi);
    }
}
