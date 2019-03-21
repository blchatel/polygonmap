/* Copyright 2019 Bastien Chatelain */

package blchatel.geometry2d;

import blchatel.random.Random;

public abstract class Shape {

    /**
     * Indicate if the given point (x, y) is contained into this shape
     * @param x (double): x-coordinates of the point to test
     * @param y (double): y-coordinates of the point to test
     * @return (boolean): true if the point is contained into this shape (boundary included)
     */
    public abstract boolean contains(double x, double y);


    /**
     * Compute the perimeter of this shape
     * @return (double): the perimeter
     */
    public abstract double perimeter();


    /**
     * Compute the surface of this shape
     * @return (double): the surface
     */
    public abstract double surface();


    /**
     * Sample (assume uniformly) a point into the shape (border included)
     * @param random (Random): the random generator
     * @return (Point): the sample
     */
    public abstract Point sample(Random random);


}
