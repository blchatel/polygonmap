package blchatel.polygonmap.fortune;

import blchatel.polygonmap.geometry2d.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The beach line implementation as a binary Tree structure
 * - Each Arc is a Leaf
 * - Each BreakPoint between arcs are Node
 * - The tree is sorted by x-coordinate of the point of interest (site or breakpoint)
 * The beach line is delimited by a box.
 */
public class BeachLine {

    /// The root node: null by default
    private Node root;
    /// Used to delimiter the beach line
    private final Rectangle box;

    /**
     * BeachLine Default Constructor
     * @param box (Rectangle):
     * @param rootCell (VoronoiCell):
     */
    BeachLine(Rectangle box, VoronoiCell rootCell){
        this.root = new Arc(rootCell);
        this.box = box;
    }

    /**
     * Remove an Arc from the beach line and update the structure accordingly
     * @param alpha (Arc): the arc to remove
     */
    void remove(Arc alpha){

        if(alpha == null)
            return;
        if(alpha.equals(root))
            root=null;
        //else it has at least one ancestor

        Node grandParent = alpha.parent.parent;

        if(grandParent == null){
            // Only one ancestor level hence the parent is the root so remove the full branch and update the root
            if(alpha.equals(alpha.parent.childLeft)) root = alpha.parent.childRight;
            if(alpha.equals(alpha.parent.childRight)) root = alpha.parent.childLeft;
        }
        else {
            if (alpha.parent.childLeft.equals(alpha)) {
                if (grandParent.childLeft.equals(alpha.parent)) grandParent.setLeftChild(alpha.parent.childRight);
                if (grandParent.childRight.equals(alpha.parent)) grandParent.setRightChild(alpha.parent.childRight);
            } else {
                if (grandParent.childLeft.equals(alpha.parent)) grandParent.setLeftChild(alpha.parent.childLeft);
                if (grandParent.childRight.equals(alpha.parent)) grandParent.setRightChild(alpha.parent.childLeft);
            }
        }
        alpha.parent = null;
    }

    /**
     * From two given break point, determine which one is higher in the tree structure
     * Note: we assume bp1 and bp2 are breakpoints of the given arc which means both are ancestor of it
     * Note: if the returned value cannot be determined from the assumption, throw an exception
     * @param arc (Arc): the concerned arc
     * @param bp1 (BreakPoint): first breakpoint of given arc (an ancestor of it in the tree)
     * @param bp2 (BreakPoint): second breakpoint of given arc (an ancestor of it in the tree)
     * @return (BreakPoint): the highest breakpoint, either bp1 or bp2
     */
    BreakPoint determineHigher(Arc arc, BreakPoint bp1, BreakPoint bp2) {
        BreakPoint higher = null;
        Node alpha = arc;
        while (!alpha.equals(root)) {
            alpha = alpha.parent;
            if (alpha.equals(bp1)) higher = bp1;
            if (alpha.equals(bp2)) higher = bp2;
        }
        if(higher==null)
            throw new IllegalStateException();
        return higher;
    }


    /**
     * Find the Arc directly (vertically) above the given point (which is generally a new site)
     * This function is used to know which arc we need to split
     * @param pi (Vector): the new site
     * @return (Arc): the arc directly above (i.e. the arc to split)
     */
    Arc getArcAbove(Vector pi){
        Node bp = root;

        while(!(bp instanceof Arc)) {
            double x = getCurrentEdgeTail((BreakPoint) bp, pi.y).x;
            if(x>pi.x) bp = bp.childLeft;
            else bp = bp.childRight;
        }
        return (Arc) bp;
    }


    /**
     * Compute the current (i.e. temporary) second end (xy-coordinates)
     * of an unfinished halfEdge represented as current breakpoint position.
     * @param bp (BreakPoint): the break point to compute x-coordinate
     * @param sweepY (double): current sweep line y-coordinate to define arcs
     * @return (Vector): xy-coordinate of current edge tail
     */
    private Vector getCurrentEdgeTail(BreakPoint bp, double sweepY) {

        // Get the arc directly on left and on right of the breakpoint
        Arc left = bp.getLeftArc();
        Arc right = bp.getRightArc();

        // Get corresponding point sites
        Vector l = left.cell.site;
        Vector r = right.cell.site;

        double dp = 2*(l.y - sweepY);
        double a1 = 1/dp;
        double b1 = -2*l.x/dp;
        double c1 = (l.x*l.x + l.y*l.y - sweepY*sweepY)/dp;

        double dp2 = 2*(r.y - sweepY);
        double a2 = 1/dp2;
        double b2 = -2*r.x/dp2;
        double c2 = (r.x*r.x + r.y*r.y - sweepY*sweepY)/dp2;

        double a = a2-a1;
        double b = b2-b1;
        double c = c2-c1;

        double disc = b*b - 4*a*c;
        double x1 = (-b + Math.sqrt(disc))/(2*a);
        double x2 = (-b - Math.sqrt(disc))/(2*a);

        double x = (l.y > r.y) ? Math.min(x1, x2) : Math.max(x1, x2);
        double y = bp.halfEdge.support.y(x);
        return new Vector(x, y);
    }


    /**
     * Determine the y value from given x and parabola defined by pi site
     * @param pi (Vector): point site that define the parabola with sweepY
     * @param x (double): x-coordinate for the y we are looking for
     * @param sweepY (double): current sweep line y-coordinate to define arcs
     * @return (double) the y-coordinate on (pi, sweepY)-parabola for given x-coordinate
     */
    double getY(Vector pi, double x, double sweepY) {
        double dp = 2*(pi.y - sweepY);
        double a1 = 1/dp;
        double b1 = -2*pi.x/dp;
        double c1 = (pi.x*pi.x + pi.y*pi.y - sweepY*sweepY)/dp;

        return(a1*x*x + b1*x + c1);
    }

    /**
     * Split the arc alpha into three new leaf introduced by the new point site
     * Note: we replace alpha by a new sub tree structure
     * @param alpha (Arc): the arc to split
     * @param cell (VoronoiCell): the new point site cell
     * @return (Arc[]): new Arc left [0] and new Arc right [1] that can induce circle events
     */
    Arc[] split(Arc alpha, VoronoiCell cell){

        // - Store the tuple <pj, pi> and <pi, pj> representing the new breakpoints at the two new internal nodes.
        // - Create new half-edge records in the Voronoi diagram structure for the edge separating V(pi) and V(pj),
        //   which will be traced out by the two new breakpoints.
        Vector start = new Vector(cell.site.x, getY(alpha.cell.site, cell.site.x, cell.site.y));
        Vector alphaPi = cell.site.subtract(alpha.cell.site);

        HalfEdge el = new HalfEdge(start, new Vector(alphaPi.y, -alphaPi.x));
        HalfEdge er = new HalfEdge(start, new Vector(-alphaPi.y, alphaPi.x));

        BreakPoint bp = new BreakPoint();
        BreakPoint bpR = new BreakPoint();
        bp.set(el, alpha.cell, cell);
        bpR.set(er, cell, alpha.cell);

        // Replace the leaf of the beach line that represents alpha with a subtree having three leaves.
        // - The middle leaf stores the new site pi
        // - The other two leaves store the site pj that was originally stored with alpha.
        Arc aL = new Arc(alpha.cell);
        Arc aM = new Arc(cell);
        Arc aR = new Arc(alpha.cell);

        bp.setLeftChild(aL);
        bp.setRightChild(bpR);

        bpR.setLeftChild(aM);
        bpR.setRightChild(aR);

        // Replace alpha in the structure
        if(alpha.parent != null){
            if(alpha.equals(alpha.parent.childLeft)){
                alpha.parent.setLeftChild(bp);
            }
            else{
                alpha.parent.setRightChild(bp);
            }
        }else{
            root = bp;
        }

        return new Arc[]{aL, aR};
    }


    /**
     * Ends all the half-edges left into the beach line
     * Note: this function is the entry point of a recursive call
     * @return (List of Edge): the list of ended edges
     */
    List<Edge> endEdges(){
        List<Edge> edges = new ArrayList<>();
        root.endEdges(edges);
        return edges;
    }


    /// The binary tree structure

    /** Abstract Node of the tree, defined by parent, childLeft and childRight */
    private abstract class Node {

        /// Node pointer on tree structure
        Node parent;
        Node childLeft;
        Node childRight;

        /**
         * Set the left child of this with given node
         * @param p (Node): the new left child
         */
        void setLeftChild (Node p) {
            childLeft = p;
            if(p!=null)
                p.parent = this;
        }

        /**
         * Set the right child of this with given node
         * @param p (Node): the new right child
         */
        void setRightChild (Node p) {
            childRight = p;
            if(p!=null)
                p.parent = this;
        }

        /**
         * Ends all the half-edges left into the beach line
         * Note: this function is recursive call through the tree
         * @param edges (List of Edge): the list of ended edges (the same pointer though recursion level)
         */
        void endEdges(List<Edge> edges) {

            // recursive stop condition
            if(this instanceof Arc) {
                return;
            }
            BreakPoint bp = (BreakPoint) this;

            Vector[] intersections = bp.halfEdge.intersectWith(box);
            if(intersections.length > 0){
                if(box.contains(bp.halfEdge.head))
                    edges.add(new Edge(bp.halfEdge.head, intersections[0]));
            }
            // Recursive call
            bp.childLeft.endEdges(edges);
            bp.childRight.endEdges(edges);
        }
    }

    /** BreakPoint Node of the tree, defined with the uncompleted half-edge */
    public class BreakPoint extends Node{

        /// Half-edge linked to the node
        HalfEdge halfEdge;
        VoronoiCell left, right;

        void set(HalfEdge halfEdge, VoronoiCell left, VoronoiCell right){
            this.halfEdge = halfEdge;
            this.left = left;
            this.right = right;
        }


        /**
         * Given a break point, it compute the closest arc to the left
         * @return (Arc): the closest left Arc
         */
        Arc getLeftArc() {
            Node bp = childLeft;
            while(!(bp instanceof Arc))
                bp = bp.childRight;
            return (Arc) bp;
        }

        /**
         * Given a break point, it compute the closest arc to the right
         * @return (Arc): the closest right Arc
         */
        Arc getRightArc() {
            Node bp = childRight;
            while(!(bp instanceof Arc))
                bp = bp.childLeft;
            return (Arc) bp;
        }

        @Override
        public String toString() {
            return "BreakPoint{" +
                    "halfEdge=" + halfEdge +
                    '}';
        }
    }

    /** Arc Leaf of the tree, defined with the point of interest and the linked event if CIRCLE_EVENT*/
    public class Arc extends Node {

        final VoronoiCell cell;
        Event event;

        /**
         * Default Arc Constructor
         * @param cell (VoronoiCell): the point of interest linked to this leaf.
         */
        Arc(VoronoiCell cell){
            this.cell = cell;
        }

        /**
         * Given an Arc , it compute the closest right parent (BreakPoint)
         * @return (BreakPoint): the closest right BreakPoint
         */
        BreakPoint getRightBreakPoint() {
            Node parent = this.parent;
            if (parent == null) return null;
            Node last = this;
            while (parent.childRight == last) {
                last = parent;
                parent = parent.parent;
                if(parent == null) return null;
            }
            return (BreakPoint) parent;
        }

        /**
         * Given an Arc , it compute the closest right parent (BreakPoint)
         * @return (BreakPoint): the closest right BreakPoint
         */
        BreakPoint getLeftBreakPoint() {
            Node parent = this.parent;
            if (parent == null) return null;
            Node last = this;
            while (parent.childLeft == last) {
                last = parent;
                parent = parent.parent;
                if(parent == null) return null;
            }
            return (BreakPoint) parent;
        }


        @Override
        public String toString() {
            return "Arc{" +
                    "cell=" + cell +
                    ", event=" + event +
                    '}';
        }
    }
}
