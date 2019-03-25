package blchatel.polygonmap.geometry2d;

/**
 * Extension of util.Random
 * to propose new random extension.
 * Basically used for sampling Vector on Shape
 *
 */
public class Random extends java.util.Random {

    /**
     * Random constructor
     * @param seed (long): random seed for controlling the sequence
     */
    public Random(long seed) {
        super(seed);
    }

    /**
     * Simplified Random constructor
     */
    public Random() {
        super();
    }

    /**@return (double): a positive double value between 0 and Double.MAX_VALUE */
    public double nextPositiveDouble(){
        return (Double.MAX_VALUE) * nextDouble();
    }
}
