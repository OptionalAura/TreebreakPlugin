/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.misc;

/**
 *
 * @author Daniel Allen
 */
public class Point2D {

    private double x, y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the distance squared from this point to (x, y). This avoids a costly
     * square root function and should be used for comparisons instead of {@link #getDistance(double x, double y)}
     *
     * @param x The X coordinate of the second point
     * @param y The Y coordinate of the second point
     * @return The distance squared, equivalent to {@link #getDistance(double x, double y)}<sup>2</sup>
     * @see #distSquared(Point2D p)
     */
    public double distSquared(double x, double y) {
        double xD = this.x - x;
        double yD = this.y - y;
        return xD * xD + yD * yD;
    }

    /**
     * Gets the distance squared from this point to the provided point. This avoids a costly
     * square root function and should be used for comparisons with other
     * lengths instead of {@link #getDistance(Point2D p)}
     *
     * @param p The second point
     * @return The distance squared, equivalent to {@link #getDistance(Point2D p)}<sup>2</sup>
     * @see #distSquared(double x, double y)
     */
    public double distSquared(Point2D p) {
        double xD = this.x - p.getX();
        double yD = this.y - p.getY();
        return xD * xD + yD * yD;
    }

    /**
     * Gets the distance squared from this point to the provided point. This uses a costly square root function and should be avoided whenever possible.
     *
     * @param p The second point
     * @return The distance, equivalent to <sup>2</sup>
     * @see #distSquared(double x, double y)
     */
    public double getDistance(Point2D p) {
        double mag = Math.sqrt((p.getX() - this.getX()) * (p.getX() - this.getX()) + (p.getY() - this.getY()) * (p.getY() - this.getY()));
        return mag;
    }

    /**
     * Gets the distance squared from this point to (x, y).This avoids a costly
 square root function and should be used for comparisons with other
 lengths instead of {@link #getDistance(Point2D)}
     *
     * @param x X-coordinate of point
     * @param y Y-coordinate of point
     * @return The distance squared, equivalent to {@link #getDistance(Point2D p)}<code><sup>2</sup></code>
     * @see #distSquared(double x, double y)
     */
    public double getDistance(double x, double y) {
        double mag = Math.sqrt((x - this.getX()) * (x - this.getX()) + (y - this.getY()) * (y - this.getY()));
        return mag;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vec2D getVectorTo(Point2D p) {
        return new Vec2D(this, p);
    }
}
