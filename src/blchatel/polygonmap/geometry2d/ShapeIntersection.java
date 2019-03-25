package blchatel.polygonmap.geometry2d;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Interface representing the Visitor pattern structure for Shape intersection.
 * When an intersection is asked between two shape (s1 and s2), s1 will call the "acceptIntersection(intersectionModel)" of s2
 * Hence s2 can directly select which intersection it wants from the model
 * By default the intersection are all defined below with a NotImplementedException that indicate
 * either the intersection is not defined or maybe define by symmetric case (s2.intersectWith(s1))
 * @see Shape
 * @see Vector
 * @see Function
 * @see Affine
 * @see Vertical
 * @see Edge
 * @see HalfEdge
 * @see Rectangle
 * @see Circle
 * Add any new shape here
 */
public interface ShapeIntersection {

    /**
     * Find the intersection between this Shape and the given Shape
     * Note: default case should never happen
     * @param shape (Shape): the shape to intersectWith
     * @return (Vector[]): intersection if any or an empty array
     */
    default Vector[] intersectWith(Shape shape){
        System.out.println("You forget the specific shape type");
        throw new NotImplementedException();
    }

    /**
     * Find the intersection between this Shape and the given Vector
     * @param vector (Vector): the vector to intersectWith
     * @return (Vector[]): intersection if any or an empty array
     */
    default Vector[] intersectWith(Vector vector){
        System.out.println("You probably miss the declaration of a function type here or simply implemented the other way");
        throw new NotImplementedException();
    }

    /**
     * Find the intersection between this Shape and the given Function
     * @param function (Function): the function to intersectWith
     * @return (Vector[]): intersection if any or an empty array
     */
    default Vector[] intersectWith(Function function){
        System.out.println("You probably miss the declaration of a function type here or simply implemented the other way");
        throw new NotImplementedException();
    }

    /**
     * Find the intersection between this Shape and the given Affine Function
     * @param affine (Affine): the affine function to intersectWith
     * @return (Vector[]): intersection if any or an empty array
     */
    default Vector[] intersectWith(Affine affine){
        System.out.println("You probably miss the declaration of a function type here or simply implemented the other way");
        throw new NotImplementedException();
    }

    /**
     * Find the intersection between this Shape and the given Vertical
     * @param vertical (Vertical): the vertical to intersectWith
     * @return (Vector[]): intersection if any or an empty array
     */
    default Vector[] intersectWith(Vertical vertical){
        System.out.println("You probably miss the declaration of a function type here or simply implemented the other way");
        throw new NotImplementedException();
    }

    /**
     * Find the intersection between this Shape and the given Edge
     * @param edge (Edge): the edge to intersectWith
     * @return (Vector[]): intersection if any or an empty array
     */
    default Vector[] intersectWith(Edge edge){
        System.out.println("You probably miss the declaration of a function type here or simply implemented the other way");
        throw new NotImplementedException();
    }

    /**
     * Find the intersection between this Shape and the given HalfEdge
     * @param halfedge (HalfEdge): the half-edge to intersectWith
     * @return (Vector[]): intersection if any or an empty array
     */
    default Vector[] intersectWith(HalfEdge halfedge){
        System.out.println("You probably miss the declaration of a function type here or simply implemented the other way");
        throw new NotImplementedException();
    }

    /**
     * Find the intersection between this Shape and the given Rectangle
     * @param rectangle (Rectangle): the rectangle to intersectWith
     * @return (Vector[]): intersection if any or an empty array
     */
    default Vector[] intersectWith(Rectangle rectangle){
        System.out.println("You probably miss the declaration of a function type here or simply implemented the other way");
        throw new NotImplementedException();
    }

    /**
     * Find the intersection between this Shape and the given Circle
     * @param circle (Circle): the circle to intersectWith
     * @return (Vector[]): intersection if any or an empty array
     */
    default Vector[] intersectWith(Circle circle){
        System.out.println("You probably miss the declaration of a function type here or simply implemented the other way");
        throw new NotImplementedException();
    }

}
