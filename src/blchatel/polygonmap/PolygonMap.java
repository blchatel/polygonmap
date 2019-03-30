package blchatel.polygonmap;

import blchatel.polygonmap.fortune.Voronoi;
import blchatel.polygonmap.fortune.VoronoiCell;
import blchatel.polygonmap.geometry2d.*;
import blchatel.polygonmap.geometry2d.Rectangle;
import blchatel.polygonmap.io.Config;
import blchatel.polygonmap.io.FileSystem;
import blchatel.polygonmap.swing.DrawSupport;
import blchatel.polygonmap.swing.SwingShape;
import blchatel.polygonmap.swing.SwingWindow;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;


public class PolygonMap {

    /// The file system
    private final FileSystem fileSystem;
    // The configuration
    private final Config c;
    // The UI window
    private final DrawSupport support;


    private PolygonMap(String configName){

        fileSystem = new FileSystem();
        c = new Config(fileSystem, configName);

        int windowW = c.get("WINDOW", "width", int.class);
        int windowH = c.get("WINDOW", "height", int.class);
        SwingWindow window = new SwingWindow("Polygon Map Generator", windowW, windowH);
        support = window.getSupport();
    }

    private void start(){

        // Init the random generator
        Random r = new Random(c.get("RANDOM", "seed1", int.class));

        // Init the bounding box
        int width = c.get("MAP", "width", int.class);
        int height = c.get("MAP", "height", int.class);
        Rectangle rec = new Rectangle(0, 0, width, height);

        // Generate the point site sample
        int samples = c.get("MAP", "samples", int.class);
        Set<Vector> points = new HashSet<>();
        for(int i = 0; i < samples; i++){
            Vector v = rec.sample(r);
            points.add(v);
        }

        // Generate the diagram with Lloyd's relaxation
        int its = c.get("MAP", "lloydIt", int.class);
        for(int i = 0; i < its; i++){
            Voronoi diagram = new Voronoi(points, rec);
            diagram.draw(support);
            points = diagram.cellCentroids();
        }

        /*
        for(Vector v : points){
            support.registerShape(new SwingShape(v.toPath(), Color.BLUE, 10));
        }
        support.refresh();
        */

    }

    ////////////////////////////////////////////////
    ///
    ///  MAIN FUNCTION CALL
    ///
    ///////////////////////////////////////////////

    /**
     * Main entry point
     * @param args (String[]): [0]-configuration file
     */
    public static void main(String[] args){

        if(args.length < 1)
            throw new IllegalArgumentException("At least one parameter is expected: \n\t[0]-configuration file");

        PolygonMap pm = new PolygonMap(args[0]);
        pm.start();
    }
}
