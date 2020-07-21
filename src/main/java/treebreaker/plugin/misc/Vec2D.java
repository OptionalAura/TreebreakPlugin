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
public class Vec2D {

    private static final double PI = 3.1415926535897932384626433832795028841971693993751058209749446;

    private double originX, originY;
    private double endX, endY;
    private double magnitude;
    private boolean magCalculated = false;

    public Vec2D(double ox, double oy, double ex, double ey) {
        this.originX = ox;
        this.originY = oy;
        this.endX = ex;
        this.endY = ey;
    }

    public Vec2D(Point2D origin, Point2D end) {
        this.originX = origin.getX();
        this.originY = origin.getY();
        this.endX = end.getX();
        this.endY = end.getY();
    }

    public double dotProduct() {
        return originX * endX + originY * endY;
    }

    public double getStuff(){
        double mag = (endX - originX) + (endY - originY);
        return mag;
    }
    
    public double getMagnitude() {
        //checks if the magnitude has already been calculated to avoid square root functions as much as possible
        if (magCalculated) {
            return magnitude;
        }
        double mag = Math.sqrt((endX - originX) * (endX - originX) + (endY - originY) * (endY - originY));
        magnitude = mag;
        magCalculated = true;
        return mag;
    }

    public void normalize() {
        this.originX /= getMagnitude();
        this.originY /= getMagnitude();
        this.endX /= getMagnitude();
        this.endY /= getMagnitude();
        this.magnitude = 1;
    }

    public double getXDisplacement() {
        return this.endX - this.originX;
    }

    public double getYDisplacement() {
        return this.endY - this.originY;
    }

    public Point2D getOrigin() {
        return new Point2D(originX, originY);
    }

    public Point2D getEnd() {
        return new Point2D(endX, endY);
    }

    public double getAngleBetweenInRadians(Vec2D other) {
        double xD = this.getXDisplacement() * other.getXDisplacement();
        double yD = this.getYDisplacement() * other.getYDisplacement();

        double dot = xD + yD;
        double div = this.getMagnitude()* other.getMagnitude();

        return Math.acos(dot / div);
    }

    public double getAngleBetweenInRadiansSigned(Vec2D other) {
        return Math.atan2(this.getYDisplacement(), this.getXDisplacement()) - Math.atan2(other.getYDisplacement(), other.getXDisplacement());
    }
    
    public double getAngleBetweenInDegrees(Vec2D other) {
        return toDegrees(getAngleBetweenInRadians(other));
    }

    public double getAngleBetweenInDegreesSigned(Vec2D other) {
        return toDegrees(getAngleBetweenInRadiansSigned(other));
    }
    
    public double toDegrees(double radians) {
        return (180d / PI) * radians;
    }
}
