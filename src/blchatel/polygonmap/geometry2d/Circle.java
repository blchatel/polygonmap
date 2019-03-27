/* Copyright 2019 Bastien Chatelain */

package blchatel.polygonmap.geometry2d;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

/**
 * 2D Circle definition and implementation with double precision
 * The circle is a shape defined by its center and its radius
 * @see Shape
 * @see Vector
 * @see Random
 * @see Circle
 */
public final class Circle extends Shape{

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
     * Compute the (x, y) coordinates of the disk Vector for radius r and angle phi
     * Assume origin of phi is the x-axis parallel passing by Disk center (from polar coordinate)
     * @param r (double): radius of the Vector  (0 <= r < R)
     * @param phi (double): angle of the Vector in radian (0 <= phi < 2pi)
     * @return (Vector): the Vector on the Disk for radius r and phi angle
     */
    public Vector getPoint(double r, double phi){
        if(r > this.r)
            throw new IllegalArgumentException("r is bigger than Disk radius");
        return new Vector(c.x + r*Math.cos(phi), c.y + r*Math.sin(phi));
    }

    @Override
    public String toString() {
        return "Circle[c:" + c.toString() + ", r:" + r + "]";
    }


    /// Circle extends Shape

    @Override
    public boolean contains(Vector v){
        return c.subtract(v).getSqrLength() <= r*r;
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
        double e1 = random.nextDouble();
        double e2 = random.nextDouble();
        return getPoint(Math.sqrt(e1), 2*Math.PI*e2);
    }

    @Override
    public Path2D toPath() {
        Ellipse2D ellipse = new Ellipse2D.Double(
                c.x - r,
                c.y - r,
                r * 2,
                r * 2
        );
        return new Path2D.Double(ellipse);
    }

    @Override
    protected Vector[] acceptIntersectWith(ShapeIntersection intersection) {
        return intersection.intersectWith(this);
    }
}
