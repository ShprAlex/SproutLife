package com.sproutlife.renderer;

import java.awt.geom.Dimension2D;

public class Dimension2Double extends Dimension2D {
    private double width;
    private double height;
    
    public Dimension2Double(double width, double height) {
        setSize(width, height);
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

}
