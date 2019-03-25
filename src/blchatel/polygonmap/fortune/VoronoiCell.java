package blchatel.polygonmap.fortune;

import blchatel.polygonmap.geometry2d.Edge;
import blchatel.polygonmap.geometry2d.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO not implemented yet
 */
public class VoronoiCell {

    final Vector site;
    private final List<Edge> edges;

    VoronoiCell(Vector site){
        this.site = site;
        edges = new ArrayList<>();
    }

    void addEdge(Edge e){
        edges.add(e);
    }
}
