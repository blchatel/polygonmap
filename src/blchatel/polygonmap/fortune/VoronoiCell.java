package blchatel.polygonmap.fortune;

import blchatel.polygonmap.geometry2d.Vector;

import java.util.ArrayList;
import java.util.List;



/**
 * TODO not implemented yet
 */
public class VoronoiCell {

    final Vector site;
    private final List<Vector> vertices;

    VoronoiCell(Vector site){
        this.site = site;
        vertices = new ArrayList<>();
    }

    void addVertex(Vector v){
        vertices.add(v);
    }
}
