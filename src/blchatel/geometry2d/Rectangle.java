/* Copyright 2018  Bastien Chatelain */

package blchatel.geometry2d;


import blchatel.random.Random;

/**
 * Rectangle definition and implementation with double precision
 * A rectangle is defined by its top left corner xy-coordinates, its width and its height
 */
public final class Rectangle extends Shape{

    // xy-coordinate, width, height
    public final double x, y, w, h;

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
        return "Rectangle(x:" + x + ", y: " + y+ ", w: " + w+ ", h: " + h + ")";
    }


    /// Rectangle extends Shape

    @Override
    public boolean contains(double x, double y) {
        return x >= this.x && x <= this.x+w && y >= this.y && y <= this.y+h;
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
    public Point sample(Random random) {
        double x = random.nextDouble()*w;
        double y = random.nextDouble()*h;
        return new Point(x, y);
    }
}
