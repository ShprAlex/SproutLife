package com.sproutlife.renderer;

import java.awt.geom.Point2D;

public class Point2Double extends Point2D {
    private double x;
    private double y;
    
    public Point2Double(double x, double y) {
        setLocation(x, y);
    }
    
    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public double getX() {
        return this.x;
    }
 
    @Override
    public double getY() {
        return this.y;
    }

}
