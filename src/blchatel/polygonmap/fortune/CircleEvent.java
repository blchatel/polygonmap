package blchatel.polygonmap.fortune;


import blchatel.polygonmap.geometry2d.Vector;


/**
 * Implementation of the CIRCLE_EVENT
 * @see Event
 */
public class CircleEvent implements Event{

    /// Linked point of interest
    private final Vector p;
    /// Concerned arc if circle event
    BeachLine.Arc arc;

    /**
     * Default CircleEvent Constructor
     * @param p (Vector): event point of interest: bottom most circle point (CIRCLE_EVENT)
     * @param arc (Arc): event concerned arc
     */
    CircleEvent (Vector p, BeachLine.Arc arc) {
        this.p = p;
        this.arc = arc;
    }


    @Override
    public String toString() {
        return "CircleEvent[p = " + p +']';
    }


    /// CircleEvent implements Event

    @Override
    public Vector getP() {
        return p;
    }
}
