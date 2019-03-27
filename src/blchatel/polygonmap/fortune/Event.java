package blchatel.polygonmap.fortune;


import blchatel.polygonmap.geometry2d.Vector;

/**
 * Event interface. Should be represented by SITE_EVENT and CIRCLE_EVENT
 */
public interface Event {

    /** @return (Vector): the point of interest of the event. Either the site for (SITE_EVENT) or the bottom most circle point for CIRCLE_EVENT*/
    Vector getP();

}
