/* Copyright 2019 Bastien Chatelain */

package blchatel.geometry2d;

import blchatel.random.Random;

/**
 * Disk definition and implementation with double precision
 * A disk is defined by its center point and its radius
 */
public final class Disk extends Circle{

    /**
     * Default Disk constructor
     * @param center (Point): center of the Disk to create
     * @param radius (double): radius of the Disk to create
     */
    public Disk(Point center, double radius){
        super(center, radius);
    }

    /**
     * Extended Disk constructor
     * @param cx (double): center x-coordinate of the Disk to create
     * @param cy (double): center y-coordinate of the Disk to create
     * @param radius (double): radius of the Disk to create
     */
    public Disk(double cx, double cy, double radius){
        super(cx, cy, radius);
    }

    /**
     * Deep Copy Disk constructor
     * @param disk (Disk): the Disk to copy
     */
    public Disk(Disk disk){
        super(disk);
    }

    /**
     * Compute the (x, y) coordinates of the disk point for radius r and angle phi
     * Assume origin of phi is the x-axis parallel passing by Disk center (from polar coordinate)
     * @param r (double): radius of the point  (0 <= r < R)
     * @param phi (double): angle of the point in radian (0 <= phi < 2pi)
     * @return (Point): the point on the Disk for radius r and phi angle
     */
    public Point getPoint(double r, double phi){
        if(r > this.r)
            throw new IllegalArgumentException("r is bigger than Disk radius");
        return new Point(c.x + r*Math.cos(phi), c.y + r*Math.sin(phi));
    }


    @Override
    public String toString() {
        return "Disk(c:" + c.toString() + ", r:" + r + ")";
    }


    /// Disk extends Circle

    @Override
    public boolean contains(double x, double y){
        return c.sqrDist(x, y) <= r*r;
    }

    @Override
    public Point sample(Random random) {

        double e1 = random.nextDouble();
        double e2 = random.nextDouble();

        return getPoint(Math.sqrt(e1), 2*Math.PI*e2);
    }
}
