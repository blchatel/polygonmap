/* Copyright 2019 Bastien Chatelain */

package blchatel.geometry2d;

import blchatel.random.Random;

/**
 * Circle definition and implementation with double precision
 * A circle is defined by its center point and its radius
 */
public class Circle extends Shape{

    // Center of the circle
    public final Point c;
    // Radius of the circle
    public final double r;

    /**
     * Default Circle constructor
     * @param center (Point): center of the circle to create
     * @param radius (double): radius of the circle to create
     */
    public Circle(Point center, double radius){
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
        c = new Point(cx, cy);
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
     * Compute the (x, y) coordinates of the point for angle phi
     * Assume origin of phi is the x-axis parallel passing by circle center (from polar coordinate)
     * @param phi (double): angle of the point in radian (0 <= phi < 2pi)
     * @return (Point): the point on the circle for phi angle
     */
    public Point getPoint(double phi){
        return new Point(c.x + r*Math.cos(phi), c.y + r*Math.sin(phi));
    }


    @Override
    public String toString() {
        return "Circle(c:" + c.toString() + ", r:" + r + ")";
    }


    /// Circle extends Shape

    @Override
    public boolean contains(double x, double y){
        return c.sqrDist(x, y) == r*r;
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
    public Point sample(Random random) {
        double phi = 2*Math.PI*random.nextDouble();
        return getPoint(phi);
    }
}
