package blchatel.polygonmap.fortune;

import blchatel.polygonmap.geometry2d.Vector;

/**
 * Event implementation.
 * Status object which is mutable and not really protected
 * An event is either a site or circle event for the sweep line to process
 */
public class Event {

    /** Type of event */
	public enum Type{
		SITE_EVENT, CIRCLE_EVENT
	}

	/// This of this event
    final Type type;
	/// Linked point of interest
	final Vector pi;
	/// Concerned arc if circle event
	BeachLine.Arc arc;

    /**
     * Default Event Constructor
     * @param pi (Vector): event point of interest: either site (SITE_EVENT) or bottom most circle point (CIRCLE_EVENT)
     * @param type (Type): type of the event
     */
	Event (Vector pi, Type type) {
		this.pi = pi;
		this.type = type;
		arc = null;
	}

    @Override
    public String toString() {
        return "Event{" +
                "pi=" + pi +
                ", type=" + type +
                '}';
    }
}
