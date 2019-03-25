/* Copyright 2019 Bastien Chatelain */

package blchatel.polygonmap.geometry2d;

import java.awt.geom.Path2D;


/**
 * 2D Shape representation. This class is abstract but propose some utilities function
 * (perimeter, surface, etc.)
 * All the Shape implements the visitor pattern for Intersection
 * @see ShapeIntersection has the knowledge of all existing Shape implementation.
 */
public abstract class Shape {

    private ShapeIntersection intersection;

    /**
     * Intersection setter
     * Note: its use is optional as its reimplementation.
     * @param intersection (ShapeIntersection): the intersection pattern of the function
     */
    final void setIntersection(ShapeIntersection intersection) {
        this.intersection = intersection;
    }

    /**
     * Compute the intersection (if exists) between this and given shape
     * Note if the Shape are the same, we return a single point arbitrarily selected
     * Note if the Shape do not intersect, we return an empty array
     * @param other (Shape): the other shape
     * @return (Vector[]): the intersection points if exists, empty array otherwise
     */
    public final Vector[] intersectWith(Shape other){
        return other.acceptIntersectWith(intersection);
    }

    /**
     * Solve the intersection ambiguity through the given handler
     * @param intersection (Intersection): the intersection handler
     * @return (Vector[]): the intersection points if exists, empty array otherwise
     */
    abstract Vector[] acceptIntersectWith(ShapeIntersection intersection);

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

    /** @return (Path2D): AWT path used for drawing */
    public abstract Path2D toPath();

}
