/* Copyright 2019 Bastien Chatelain */

package blchatel.geometry2d;

import blchatel.random.Random;

public abstract class Shape {

    private static final Random RANDOM = new Random();

    /**
     * Indicate if the given vector (x, y) is contained into this shape
     * @param v (Vector): vector to test
     * @return (boolean): true if the vector is contained into this shape (boundary included)
     */
    public abstract boolean contains(Vector v);


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
     * Sample (assume uniformly) a vector into the shape (border included)
     * @param random (Random): the random generator
     * @return (Vector): the sample
     */
    public abstract Vector sample(Random random);

    /**
     * Sample uniform vector inside shape, including border.
     * @return (Vector): a uniform sample, not null
     */
    public Vector sample() {
        return sample(RANDOM);
    }

}
