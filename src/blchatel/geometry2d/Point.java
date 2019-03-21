/* Copyright 2019 Bastien Chatelain */

package blchatel.geometry2d;

/**
 * 2D Point definition and implementation with double precision
 * A point is defined by its xy-coordinates
 */
public final class Point {

    public final static Point ORIGIN = new Point(0, 0);

    // Point coordinates
    public final double x, y;

    /**
     * Default Point constructor
     * @param x (double): x-coordinate
     * @param y (double): y-coordinate
     */
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Complementary Point constructor from single precision
     * @param x (int): x-coordinate
     * @param y (int): y-coordinate
     */
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Deep Copy Point constructor
     * @param p (Point): the point to copy
     */
    public Point(Point p){
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * Compute square euclidean distance between this and given point
     * @param px (double): Other y-coordinate
     * @param py (double): Other y-coordinate
     * @return (double): the square euclidean distance
     */
    public double sqrDist(double px, double py){
        return (x-px)*(x-px) + (y-py)*(y-py);
    }

    /**
     * Compute square euclidean distance between this and given point
     * @param p (Point): Other
     * @return (double): the square euclidean distance
     */
    public double sqrDist(Point p){
        return (x-p.x)*(x-p.x) + (y-p.y)*(y-p.y);
    }


    /**
     * Compute euclidean distance between this and given point
     * @param px (double): Other y-coordinate
     * @param py (double): Other y-coordinate
     * @return (double): the euclidean distance
     */
    public double dist(double px, double py){
        return Math.sqrt((x-px)*(x-px) + (y-py)*(y-py));
    }

    /**
     * Compute euclidean distance between this and given point
     * @param p (Point): Other
     * @return (double): the euclidean distance
     */
    public double dist(Point p){
        return Math.sqrt((x-p.x)*(x-p.x) + (y-p.y)*(y-p.y));
    }



    @Override
    public String toString() {
        return "Point(x:" + x + ", y: " + y + ")";
    }
}
