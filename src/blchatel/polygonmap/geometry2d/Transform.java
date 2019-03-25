package blchatel.polygonmap.geometry2d;

import java.awt.geom.AffineTransform;


/**
 * Represents an immutable 2D affine transformation.
 */
public final class Transform {
    
    /** The identity transform **/
    public static final Transform I = new Transform(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    
    // X scale
    private final double m00;
    // X shear
    private final double m01;
    // X translation
    private final double m02;
    // Y shear
    private final double m10;
    // Y scale
    private final double m11;
    // Y translation
    private final double m12;

    /**
     * Creates a new transform.
     * @param m00 (double): X scale
     * @param m01 (double): X shear
     * @param m02 (double): X translation
     * @param m10 (double): Y shear
     * @param m11 (double): Y scale
     * @param m12 (double): Y translate
     */
    public Transform(double m00, double m01, double m02, double m10, double m11, double m12) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
    }
    
    /**
     * Transforms point.
     * @param p (Vector): point, not null
     * @return (Vector): transformed point, not null
     */
    public Vector onPoint(Vector p) {
        return new Vector(
            p.x * m00 + p.y * m01 + m02,
            p.x * m10 + p.y * m11 + m12
        );
    }
    
    /**
     * Transforms vector.
     * @param v (Vector): point, not null
     * @return (Vector): transformed vector, not null
     */
    public Vector onVector(Vector v) {
        return new Vector(
            v.x * m00 + v.y * m01,
            v.x * m10 + v.y * m11
        );
    }
    
    /**
     * Appends another transform (applied after this transform).
     * @param t (Transform): transform, not null
     * @return (Transform): extended transform, not null
     */
    public Transform transformed(Transform t) {
        return new Transform(
            t.m00 * m00 + t.m01 * m10, t.m00 * m01 + t.m01 * m11, t.m00 * m02 + t.m01 * m12 + t.m02,
            t.m10 * m00 + t.m11 * m10, t.m10 * m01 + t.m11 * m11, t.m10 * m02 + t.m11 * m12 + t.m12
        );
    }
    
    /**
     * Appends translation (applied after this transform).
     * @param d (Vector): translation, not null
     * @return (Transform): extended transform, not null
     */
    public Transform translated(Vector d) {
        return new Transform(
            m00, m01, m02 + d.x,
            m10, m11, m12 + d.y
        );
    }
    
    /**
     * Appends scale (applied after this transform).
     * @param sx (double) X scale
     * @param sy (double) Y scale
     * @return (Transform): extended transform, not null
     */
    public Transform scaled(double sx, double sy) {
        return new Transform(
            m00 * sx, m01 * sx, m02 * sx,
            m10 * sy, m11 * sy, m12 * sy
        );
    }
    

    /**
     * Appends rotation around origin (applied after this transform).
     * @param a (double): angle, in radians
     * @return (Transform): extended transform, not null
     */
    public Transform rotated(double a) {
        double c = Math.cos(a);
        double s = Math.sin(a);
        return new Transform(
            c * m00 - s * m10, c * m01 - s * m11, c * m02 - s * m12,
            s * m00 + c * m10, s * m01 + c * m11, s * m02 + c * m12
        );
    }
    
    /**
     * Appends rotation around specified point (applied after this transform).
     * @param a (double): angle, in radians
     * @param center (Vector): rotation axis, not null
     * @return (Transform): extended transform, not null
     */
    public Transform rotated(double a, Vector center) {
        return
            translated(center.opposite()).
            rotated(a).
            translated(center);
    }
    

    /** @return (Transform): transform inverse, not null */
    public Transform inverted() {
        double det = 1.0f / (m00 * m11 - m01 * m10);
        double a = m11 * det;
        double b = -m01 * det;
        double c = -m10 * det;
        double d = m00 * det;
        return new Transform(
            a, b, -(a * m02 + b * m12),
            c, d, -(c * m02 + d * m12)
        );
    }

    @Override
    public String toString() {
        return String.format("[%f, %f, %f, %f, %f, %f]", m00, m01, m02, m10, m11, m12);
    }

    /** @return (AffineTransform): AWT affine transform equivalent, not null */
    public AffineTransform getAffineTransform() {
        return new AffineTransform(m00, m10, m01, m11, m02, m12);
    }

}
