package blchatel.polygonmap.fortune;

import blchatel.polygonmap.geometry2d.Vector;

import java.util.ArrayList;
import java.util.List;



/**
 * Implementation of the SITE_EVENT
 * @see Event
 */
public class VoronoiCell implements Event{

    private final Vector site;
    private final List<Vector> vertices;

    VoronoiCell(Vector site){
        this.site = site;
        vertices = new ArrayList<>();
    }

    void addVertex(Vector v){
        vertices.add(v);
    }


    /// VoronoiCell implements Event

    @Override
    public Vector getP() {
        return site;
    }
}
