package blchatel.polygonmap.fortune;// the voronoi diagram(a set of halfEdges) for a set of Vectors(sites)

import blchatel.polygonmap.geometry2d.Edge;
import blchatel.polygonmap.geometry2d.HalfEdge;
import blchatel.polygonmap.geometry2d.Rectangle;
import blchatel.polygonmap.geometry2d.Vector;
import blchatel.polygonmap.swing.DrawSupport;
import blchatel.polygonmap.swing.Drawable;
import blchatel.polygonmap.swing.SwingShape;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * Implementation of Voronoi Diagram following
 * algorithm of section 7 from :
 * De Berg, Mark, et al. "Computational geometry." Computational geometry. Springer, Berlin, Heidelberg, 1997. 1-17.
 * @see Event
 * @see BeachLine
 * @see VoronoiCell
 * @see Drawable
 * @see DrawSupport
 */
public class Voronoi implements Drawable{

	/// Box of voronoi
	private final Rectangle box;
    /// Cells of the diagram
    private Set<VoronoiCell> cells;
	/// Edges of the diagram
	private List<Edge> edges;
	/// Priority queue representing the sweep line
	private PriorityQueue<Event> events;
	/// Binary search tree representing the beach line
    private BeachLine beachLine;
	/// Current y-coordinate of sweep line
	private double sweepY;

	/**
	 * Create and generate a Voronoi Diagram for given site points
	 * @param sites (Set of Vector): the site points
	 */
	public Voronoi(Set<Vector> sites, Rectangle box) {

		// generate the box vertices and edges
		this.box = box;
		// Init list for edges and event
		voronoiDiagram(sites);
	}


    /** Event comparator sorting event by decreasing y*/
	private static final Comparator<Event> priority = (event1, event2) -> {
	    Vector p1 = event1.getP();
	    Vector p2 = event2.getP();

	    if (p1.y == p2.y) return Double.compare(p1.x, p2.x);
        else if (p1.y > p2.y) return -1;
        return 1;
    };


    /**
     * Compute the voronoi diagram for given set point O(n log n)
     * @param sites (Set of Vector): the site points
     */
    private void voronoiDiagram(Set<Vector> sites) {

        VoronoiCell tl=null, tr=null, bl=null, br = null;
        double tlBestD = Double.MAX_VALUE, trBestD = Double.MAX_VALUE, blBestD = Double.MAX_VALUE, brBestD = Double.MAX_VALUE;

        // Initialize the events queue with all site events, initialize an empty Beach Line status
        // structure and an empty doubly-connected edges list.
        events = new PriorityQueue<>(priority);
        cells = new HashSet<>(sites.size());
        for(Vector p : sites) {
            VoronoiCell voronoiCell = new VoronoiCell(p);
            events.add(voronoiCell);
            cells.add(voronoiCell);

            // Deal with the four corner that need to be added to correct cell
            double tlD = p.subtract(box.tl).getSqrLength();
            double trD = p.subtract(box.tr).getSqrLength();
            double blD = p.subtract(box.bl).getSqrLength();
            double brD = p.subtract(box.br).getSqrLength();

            if(br == null){
                tl = voronoiCell; tlBestD = tlD;
                tr = voronoiCell; trBestD = trD;
                bl = voronoiCell; blBestD = blD;
                br = voronoiCell; brBestD = brD;
            }
            else if(tlD < tlBestD){
                tl = voronoiCell; tlBestD = tlD;
            }
            else if(trD < trBestD){
                tr = voronoiCell; trBestD = trD;
            }
            else if(blD < blBestD){
                bl = voronoiCell; blBestD = blD;
            }
            else if(brD < brBestD){
                br = voronoiCell; brBestD = brD;
            }
        }
        assert tl != null;
        tl.addCorner(new Vector(box.x, box.y+box.h));
        tr.addCorner(new Vector(box.x+box.w, box.y+box.h));
        bl.addCorner(new Vector(box.x, box.y));
        br.addCorner(new Vector(box.x+box.w, box.y));

        beachLine = null;
        edges = new ArrayList<>();

        while(!events.isEmpty()) {
            // Remove the event with largest y-coordinate from events.
            Event e = events.remove();
            sweepY = e.getP().y;
            if(e instanceof VoronoiCell) {
                handleSite((VoronoiCell) e);
            }else{
                handleCircle((CircleEvent)e);
            }
        }

        // The internal nodes still present in the beach line correspond to the half-infinite edges of the Voronoi diagram.
        // Compute a bounding box that contains all vertices of the Voronoi diagram in its interior,
        // and attach the half-infinite edges to the bounding box by updating the doubly-connected edge list appropriately.
        assert beachLine != null;
        edges.addAll(beachLine.endEdges());

        // Traverse the half-edges of the doubly-connected edge list to add the cell
        // records and the pointers to and from them.
        // TODO if necessary
	}


    /**
     * Handle site event: new half-edge are initialized
     * @param cell (Vector): cell corresponding to event to handle
     */
	private void handleSite(VoronoiCell cell) {

        // If the beach line is empty, insert pi into it (the beach line consists of a single leaf storing pi) and return.
        if(beachLine == null) {
            beachLine = new BeachLine(box, cell);
            return;
        }
        // Otherwise, continue

        // Search in the beach line for the arc alpha vertically above pi.
        BeachLine.Arc alpha = beachLine.getArcAbove(cell.getP());

        // If the leaf representing alpha has a pointer to a circle event in events, then this circle event is a
        // false alarm and it must be deleted from events.
        if(alpha.event != null) {
            events.remove(alpha.event);
            alpha.event = null;
        }

        // - Replace the leaf of the beach line that represents alpha with a subtree having three leaves.
        // - Store the tuple <pj, pi> and <pi, pj> representing the new breakpoints at the two new internal nodes.
        // - Create new half-edge records in the Voronoi diagram structure for th edge separating V(pi) and V(pj),
        //   which will be traced out by the two new breakpoints.
        BeachLine.Arc[] arcs = beachLine.split(alpha, cell);

        // Perform re-balancing operations on the beach line if necessary.
        // TODO

        // Check the triple of consecutive arcs where the new arc for pi is the left arc
        // to see if the breakpoints converge. If so, insert the circle event into events and
        // add pointers between the node in the beach line and the node in events. Do the same for the
        // triple where the new arc is the right arc.
		checkCircleEvent(arcs[0]);
		checkCircleEvent(arcs[1]);
	}


    /**
     * Handle circle event: leaf gamma and corresponding arc (alpha) is drop out
     * @param event (CircleEvent): the circle event
     */
	private void handleCircle(CircleEvent event) {

        BeachLine.Arc gamma = event.arc;
        Vector cT = event.getP();

        // - Delete all circle events involving alpha from events;
        //   these can be found using the pointers from the predecessor and the successor of gama in the beach line.
        //   (The circle event where alpha is the middle arc is currently being handled, and has already been deleted from events.)
        BeachLine.BreakPoint xl = gamma.getLeftBreakPoint();
        BeachLine.BreakPoint xr = gamma.getRightBreakPoint();
        BeachLine.Arc predecessor = xl.getLeftArc();
        BeachLine.Arc successor = xr.getRightArc();

        if (predecessor.event != null) {
            events.remove(predecessor.event);
            predecessor.event = null;
        }
        if (successor.event != null) {
            events.remove(successor.event);
            successor.event = null;
        }

        // - Add the center of the circle causing the event as a vertex record to the doubly-connected edge list.
        Vector center = new Vector(cT.x, beachLine.getY(gamma.cell.getP(), cT.x, sweepY));
        //System.out.println(predecessor.cell.getP()+", "+gamma.cell.getP()+", "+successor.cell.getP());

        Edge el = boxedEdge(xl.halfEdgeHead(), center);
        Edge er = boxedEdge(xr.halfEdgeHead(), center);

        if(el != null) {
            edges.add(el);
            xl.left.addEdge(el);
            xl.right.addEdge(el);
        }
        if(er != null) {
            edges.add(er);
            xr.left.addEdge(er);
            xr.right.addEdge(er);
        }


        // - Create two half-edge records corresponding to the new breakpoint of the beach line.
        //   Gamma is bounded by two break points: xl and xr
        //   When gamma is removed from the beach line, one break point (the lowest in the tree) is also removed
        //   and we reuse the other (the highest) as new break point
        BeachLine.BreakPoint higher = beachLine.determineHigher(gamma, xl, xr);
        Vector predSucc = successor.cell.getP().subtract(predecessor.cell.getP());
        HalfEdge edge = new HalfEdge(center, new Vector(predSucc.y, -predSucc.x));
        higher.set(edge, predecessor.cell, successor.cell);

        // Set the pointers between them appropriately.
        // Attach the three new records to the half-edge records that end at the vertex.

        // - Delete the leaf gamma that represents the disappearing arc alpha from the beach line.
        // - Update the tuple representing the breakpoints at the internal nodes.
        beachLine.remove(gamma);

        // - Perform re-balancing operations on the beach line if necessary.
        // TODO

        // Check the new triple of consecutive arcs that has the former left neighbor of alpha as its middle arc
        // to see if the two breakpoints of the triple converge.
        // If so, insert the corresponding circle event into events
        checkCircleEvent(predecessor);
        checkCircleEvent(successor);
	}


    /**
     * Detects and adds circle event if sites a, b, c lie on the same circle
     * @param b (Arc): b-arc between a and c
     */
    private void checkCircleEvent(BeachLine.Arc b) {

	    // Find in the beach line the break point bounding the arc b
		BeachLine.BreakPoint lbp = b.getLeftBreakPoint();
		BeachLine.BreakPoint rbp = b.getRightBreakPoint();
		if(lbp == null || rbp == null) return;

		// Find in the beach line the left and right arc of b
		BeachLine.Arc a = lbp.getLeftArc();
		BeachLine.Arc c = rbp.getRightArc();

		// If one arc is null or if left and right are from same site, circle event cannot exists
		if(a == null || c == null || a.cell.equals(c.cell)) return;

		Vector bP = b.cell.getP();
        // The same if the point are not ccw
		if(ccw(a.cell.getP(), bP, c.cell.getP()) > 0) return;
		
		// halfEdges will intersect to form a vertex for a circle event
		Vector[] intersections = lbp.intersectWith(rbp);
        if(intersections.length == 0 ) return;
		Vector start = intersections[0];

		// compute radius
		double dx = bP.x - start.x;
		double dy = bP.y - start.y;
		double d = Math.sqrt((dx*dx) +(dy*dy));
		if(start.y - d > sweepY) return; // must be after sweep line, new event in y

		Vector ep = new Vector(start.x, start.y - d);

		// add circle event
		CircleEvent e = new CircleEvent(ep, b);
		b.event = e;
		events.add(e);
	}


    /**
     * Determine if three points make a counter-clockwise turn
     * @param a (Vector): first point
     * @param b (Vector): second point
     * @param c (Vector): third
     * @return (int): > 0 if three points make a counter-clockwise turn, < 0 if clockwise and 0 if collinear
     */
	private static double ccw(Vector a, Vector b, Vector c) {

	    double det = b.x*c.y + a.x*b.y + a.y*c.x - b.x*a.y - c.x*b.y - c.y*a.x;
	    if(Math.abs(det)<Vector.EPSILON) return 0;
	    return det;
    }


    /**
     * Compute the boxed version of an edge (i.e the sub-edge that fit the box)
     * Note: if an edge end is not contained in the box, then this end is replaced by
     * intersection between the edge and the box boundary
     * @param head (Vector): optimal head of the edge
     * @param tail (Vector): optimal tail of the edge
     * @return (Edge): the boxed edge
     */
    private Edge boxedEdge(Vector head, Vector tail){

	    boolean h = box.contains(head);
	    boolean t = box.contains(tail);

	    if(!h && !t) return null;
	    if(h && t) return new Edge(head, tail);

	    Vector inside = h ? head : tail;
	    Vector outside = h ? tail : head;

        HalfEdge half = new HalfEdge(inside, outside.subtract(inside));
        Vector[] intersections = half.intersectWith(box);

        if(intersections.length == 0){

            System.out.println(head +"->"+ box.contains(head));
            System.out.println(tail +"->"+ box.contains(tail));
            System.out.println(half);

        }

        return new Edge(inside, intersections[0]);
    }


    /**
     * Getter for the cell centroids
     * @return (Set of Vector): the centroids (one for each cell)
     */
    public Set<Vector> cellCentroids() {

        Set<Vector> centroids = new HashSet<>(cells.size());

        for(VoronoiCell cell : cells)
            centroids.add(cell.center());

        return centroids;
    }


    /// Voronoi implements Drawable

    @Override
    public void draw(DrawSupport support) {

        System.out.println("Draw Diagram");

        support.clearItems();

        for(VoronoiCell cell : cells){
            support.registerShape(new SwingShape(cell.toPath(), Color.GREEN, 0));
           // support.registerShape(new SwingShape(cell.getP().toPath(), Color.BLACK, 10));
        }

        for(Edge edge : edges){
            support.registerShape(new SwingShape(edge.toPath(), null, Color.BLACK, 1, 1, 10));
        }

        support.registerShape(new SwingShape(box.toPath(), null, Color.BLACK, 1, 1, 20));

        support.refresh();
    }
}
