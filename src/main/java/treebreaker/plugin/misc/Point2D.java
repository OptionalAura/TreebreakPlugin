/*
 * Copyright (C) 2021 Daniel Allen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
