package blchatel.polygonmap.geometry2d;

import java.awt.geom.Path2D;

/**
 * 2D Half-Edge definition and implementation with double precision
 * An half-edge is a shape defined by its head and a direction.
 * The half-edge also have a support function which is Affine or Vertical
 * The half-edge define its intersection handler
 * @see Shape
 * @see Vector
 * @see Function
 * @see Affine
 * @see Vertical
 * @see Random
 * @see ShapeIntersection
 */
public class HalfEdge extends Shape {

	public final Vector head;
    public final Vector director;
    public final Function support;

    /**
     * Half-Edge constructor
     * @param head (Vector): the head of the half-edge, a point delimiting the edge in one side
     * @param director (Vector): the support direction
     */
	public HalfEdge(Vector head, Vector director) {
	    setIntersection(new HalfEdgeIntersection());
		this.head = head;
		this.director = director;

		// Let us compute the support line
		// The vector left->right is perpendicular to it so we can easily deduct the slope
        //double slope = (right.x - left.x)/(left.y - right.y);
        if(Math.abs(director.x) < Vector.EPSILON && Math.abs(director.y) < Vector.EPSILON){
            throw new IllegalArgumentException("Vector d cannot be so small !");
        }

        if(Math.abs(director.x) < Vector.EPSILON){
            support = new Vertical(head.x);
        }
        else{
            double slope = (director.y)/(director.x);
            double ordinate = head.y - slope*head.x;
            support = new Affine(slope, ordinate);
    	}
	}

    /**
     * Reverse the Half-Edge, keep same head, and take opposite direction
     * @return (HalfEdge): the reversed half-edge
     */
	public HalfEdge reverse(){
	    return new HalfEdge(head, new Vector(-director.x,-director.y));
    }

	@Override
	public String toString() {
		return "HalfEdge{h:"+ head + ", d:" + director+"}";
	}


	/// HalfEdge extends Shape

    @Override
    public double surface() {
        return 0.0f;
    }

    @Override
    public double perimeter() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public Vector sample(Random random){
	    double lambda = random.nextPositiveDouble();
        return head.add(director.scale(lambda));
    }

    @Override
    public boolean contains(Vector v) {

	    double lambda1 = (Math.abs(director.x) > Vector.EPSILON) ? (v.x-head.x)/director.x : Double.NaN;
	    double lambda2 = (Math.abs(director.y) > Vector.EPSILON) ? (v.y-head.y)/director.y : Double.NaN;

	    if(lambda1 == Double.NaN)
	        return lambda2 >= 0;
        if(lambda2 == Double.NaN)
            return lambda1 >= 0;
        return (lambda1 >= 0 && Math.abs(lambda1-lambda2) < Vector.EPSILON);
    }

    @Override
    public Path2D toPath() {

        Path2D path = new Path2D.Double();
        path.moveTo(head.x, head.y);
        path.lineTo(head.x + Integer.MAX_VALUE*director.x, head.y + Integer.MAX_VALUE*director.y);
        return path;
    }

    @Override
    Vector[] acceptIntersectWith(ShapeIntersection intersection) {
        return intersection.intersectWith(this);
    }


    class HalfEdgeIntersection implements ShapeIntersection{

        @Override
        public Vector[] intersectWith(Rectangle rectangle) {

            Vector[] intersections = support.intersectWith(rectangle);

            if(intersections.length == 0 || !rectangle.contains(head))
                return intersections;

            Vector i = contains(intersections[0]) ? intersections[0] : intersections[1];
            return new Vector[]{i};
        }

        @Override
        public Vector[] intersectWith(HalfEdge halfEdge) {

            Vector[] intersections = support.intersectWith(halfEdge.support);

            if(intersections.length == 0)
                return intersections;

            Vector i = intersections[0];
            Vector d1 = i.subtract(head);
            Vector d2 = i.subtract(halfEdge.head);

            double lambda1 = Math.abs(director.x) > Vector.EPSILON ? d1.x/director.x : d1.y/director.y;
            double lambda2 = Math.abs(halfEdge.director.x) > Vector.EPSILON ? d2.x/halfEdge.director.x : d2.y/halfEdge.director.y;

            if(lambda1 < 0 || lambda2 < 0){
                return new Vector[]{};
            }
            return new Vector[]{i};
        }
    }

}
