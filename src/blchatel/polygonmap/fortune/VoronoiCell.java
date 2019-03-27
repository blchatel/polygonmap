package blchatel.polygonmap.fortune;

import blchatel.polygonmap.geometry2d.*;
import blchatel.polygonmap.geometry2d.Random;
import blchatel.polygonmap.geometry2d.Vector;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.geom.Path2D;
import java.util.*;


/**
 * 2D Voronoi Cell definition and implementation with double precision
 * The cell is a shape defined by its site and its vertices (computed from iteratively added edges)
 * The cell is an implementation of the SITE_EVENT
 * Note: the voronoi cell provide no guaranty it is complete when using its representation
 * @see Event
 * @see Shape
 * @see Vector
 * @see Edge
 * @see Random
 */
public class VoronoiCell extends Shape implements Event{

    /// Voronoi Utilities
    private final Vector site;
    private final Set<Edge> edges;

    /// Shape utilities
    private boolean init;
    private List<Vector> points;
    private double area;
    private double perimeter;
    private Vector center;


    /**
     * Create a VoronoiCell for the given site
     * @param site (Vector): the cell site
     */
    VoronoiCell(Vector site){
        this.site = site;
        edges = new HashSet<>();
        points = new ArrayList<>();
        init = false;
    }

    /**
     * Add an edge to this cell which is under construction
     * @param e (Edge): the edge to add
     */
    void addEdge(Edge e){
        edges.add(e);
    }

    /**
     * Add a vertices to the cell
     * Used to add corner of the box that are not included while generating the voronoi diagram
     * @param v (Vector): the corner to add
     */
    void addCorner(Vector v){
        points.add(v);
    }


    /** Init the voronoi cell representation. Assume all the edge are already added*/
    private void initialize() {

        points = connectCell();

        // The cell can be decomposed into point.size() triangle
        area = 0;
        perimeter = 0;

        Vector center = Vector.ZERO;
        Vector centerSimple = Vector.ZERO;

        for(int i = 0; i < points.size(); i++){

            // create the triangle
            Vector v2 = points.get(i);
            Vector v3 = i == points.size()-1 ? points.get(0) : points.get(i+1);

            Vector c = site.add(v2).add(v3).scale(1.0/3.0);

            double a = area(site, v2, v3);
            area += a;

            center = center.add(c.scale(a));
            centerSimple = centerSimple.add(v2);

            perimeter += v2.subtract(v3).getLength();
        }

        this.center = Math.abs(area) < Vector.EPSILON ? centerSimple.scale(1.0/points.size()) : center.scale(1.0/area);
        init = true;
    }

    /**
     * Create cell vertices from added edges. Use angle from site-edgeEnd vectors to sort the vertices
     * in a counter-clock-wise order
     * @return (List of Vector): the ordered list of vertices for this cell
     */
    private List<Vector> connectCell(){

        Set<Vector> vertices = new HashSet<>(points);

        for(Edge edge : edges){
            vertices.add(edge.v1);
            vertices.add(edge.v2);
        }

        List<Vector> orderedVertices = new ArrayList<>(vertices);
        orderedVertices.sort((v1, v2) -> {
            Vector r1 = v1.subtract(site);
            Vector r2 = v2.subtract(site);
            return Double.compare(r1.getAngle(), r2.getAngle());
        });

        return orderedVertices;
    }

    /**
     * Compute the double signed area formed by the triangle a, b, c
     * @param a (Vector): first vertex of the triangle
     * @param b (Vector): second vertex of the triangle
     * @param c (Vector): third vertex of the triangle
     * @return (double): the signed area (positive if a, b, c are counter-clock-wise, negative otherwise)
     */
    private static double area(Vector a, Vector b, Vector c) {
        double abx = b.x - a.x;
        double aby = b.y - a.y;
        double acx = c.x - a.x;
        double acy = c.y - a.y;
        return abx * acy - aby * acx;
    }

    /**
     * Compute the center of the cell from Loyd's algorithm description
     * https://en.wikipedia.org/wiki/Lloyd's_algorithm
     * @return (Vector): the center of the cell
     */
    public Vector center(){
        if(!init)
            initialize();
        return center;
    }


    /// VoronoiCell implements Event

    @Override
    public Vector getP() {
        return site;
    }


    /// VoronoiCell extends Shape

    @Override
    protected Vector[] acceptIntersectWith(ShapeIntersection intersection) {
        return intersection.intersectWith(this);
    }

    @Override
    public boolean contains(Vector v) {
        throw new NotImplementedException();
    }

    @Override
    public double perimeter() {
        if(!init)
            initialize();
        return perimeter;
    }

    @Override
    public double surface() {
        if(!init)
            initialize();
        return area;
    }

    @Override
    public Vector sample(Random random) {
        throw new NotImplementedException();
    }

    @Override
    public Path2D toPath() {
        if(!init)
            initialize();

        Path2D path = new Path2D.Double();
        Vector point = points.get(0);
        path.moveTo(point.x, point.y);
        for (int i = 1; i < points.size(); ++i) {
            point = points.get(i);
            path.lineTo(point.x, point.y);
        }
        path.closePath();
        return path;
    }
}
