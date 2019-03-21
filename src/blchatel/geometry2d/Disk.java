/* Copyright 2019 Bastien Chatelain */

package blchatel.geometry2d;

import blchatel.random.Random;

/**
 * Disk definition and implementation with double precision
 * A disk is defined by its center vector and its radius
 */
public final class Disk extends Circle{

    /**
     * Default Disk constructor
     * @param center (Vector): center of the Disk to create
     * @param radius (double): radius of the Disk to create
     */
    public Disk(Vector center, double radius){
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
        return "Disk(c:" + c.toString() + ", r:" + r + ")";
    }


    /// Disk extends Circle

    @Override
    public boolean contains(Vector v){
        return c.subtract(v).getSqrLength() <= r*r;
    }

    @Override
    public Vector sample(Random random) {

        double e1 = random.nextDouble();
        double e2 = random.nextDouble();

        return getPoint(Math.sqrt(e1), 2*Math.PI*e2);
    }
}
