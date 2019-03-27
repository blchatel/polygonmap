/* Copyright 2018  Bastien Chatelain */

package blchatel.polygonmap.geometry2d;

import java.awt.geom.Path2D;


/**
 * 2D Rectangle definition and implementation with double precision
 * The rectangle is a shape defined by its bottom left corner (smallest x,y) and its dimensions
 * @see Shape
 * @see Vector
 * @see Random
 * @see Circle
 */
public final class Rectangle extends Shape{

    // xy-coordinate, width, height
    public final double x, y, w, h;
    public final Vector bl, br, tl, tr;

    /**
     * Default Rectangle Constructor
     * @param x (double): x-coordinate of the top left corner
     * @param y (double): y-coordinate of the top left corner
     * @param w (double): width
     * @param h (double): height
     */
    public Rectangle(double x, double y, double w, double h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.bl = new Vector(x, y);
        this.br = new Vector(x+w, y);
        this.tl = new Vector(x, y+h);
        this.tr = new Vector(x+w, y+h);
    }

    /**
     * Deep Copy Rectangle constructor
     * @param r (Rectangle): the rectangle to copy
     */
    public Rectangle(Rectangle r){
        x = r.x;
        y = r.y;
        w = r.w;
        h = r.h;
        this.bl = r.bl;
        this.br = r.br;
        this.tl = r.tl;
        this.tr = r.tr;
    }

    /**
     * In-circle builder
     * Create the in-circle to this rectangle
     * @return (Circle): the built circle
     */
    public Circle buildIncircle(){

        double cx = x + w/2;
        double cy = y + h/2;
        double radius = Math.min(w, h)/2;
        return new Circle(cx, cy, radius);
    }

    /**
     * Ex-circle builder
     * Create the ex-circle to this rectangle
     * @return (Circle): the built circle
     */
    public Circle buildExcircle(){

        double cx = x + w/2;
        double cy = y + h/2;
        double radius = Math.sqrt(h*h + w*w)/2;

        return new Circle(cx, cy, radius);
    }

    @Override
    public String toString() {
        return "Rectangle[x:" + x + ", y: " + y+ ", w: " + w+ ", h: " + h + "]";
    }


    /// Rectangle extends Shape

    @Override
    public boolean contains(Vector v) {
        return v.x >= this.x && v.x <= this.x+w && v.y >= this.y && v.y <= this.y+h;
    }

    @Override
    public double perimeter() {
        return 2*w + 2*h;
    }

    @Override
    public double surface() {
        return w*h;
    }

    @Override
    public Vector sample(Random random) {
        double x = random.nextDouble()*w;
        double y = random.nextDouble()*h;
        return new Vector(x, y);
    }

    @Override
    public Path2D toPath() {
        Path2D path = new Path2D.Double();
        path.moveTo(x, y);
        path.lineTo(x+w, y);
        path.lineTo(x+w, y+h);
        path.lineTo(x, y+h);
        path.closePath();
        return path;
    }

    @Override
    protected Vector[] acceptIntersectWith(ShapeIntersection intersection) {
        return intersection.intersectWith(this);
    }

}
