/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.seed;

import java.awt.Point;

import com.sproutlife.model.rotations.Rotation;
import com.sproutlife.model.rotations.Rotations;

public class BitPattern {
    
    protected int[][] bitPattern;
    
    protected Point onBit = null;
    
    public BitPattern() {
        
    }
    
    public BitPattern(int[][] bitPattern) {
        this.bitPattern = bitPattern;
    }
    
    public BitPattern(int[][] bitPattern, boolean xySwitch) {
        
        if (xySwitch) {
            this.bitPattern = xySwitch(bitPattern);
        }
        else {
            this.bitPattern = bitPattern;
        }
               
    }
    
    public int getWidth() {
        return bitPattern.length;
    }
    
    public int getWidth(Rotation r) {
        if (r.getAngle()==0 || r.getAngle()==2) {
            return getWidth();
        }
        return getHeight();
    }
    
    public int getHeight() {
        return bitPattern[0].length;
    }
    
    public int getHeight(Rotation r) {
        if (r.getAngle()==0 || r.getAngle()==2) {
            return getHeight();
        }
        return getWidth();
    }
    
    boolean getBit(int x, int y) {
        return bitPattern[x][y]==1;
    }
    
    boolean getBit(int x, int y, Rotation r) {
        Point rp = Rotations.fromBoard(new Point(x,y), this, r);
        return getBit(rp.x, rp.y);       
    }
        
    public Point getCenter() {        
        return new Point((getWidth()-1)/2,(getHeight()-1)/2);
    }
    
    public Point getCenter(Rotation r) {
        return Rotations.toBoard(getCenter(), this, r);
    }
    
    public Point getOnBit() {
        
        if (this.onBit!=null) {
            return this.onBit;
        }
        else {
            for (int x=0;x<getWidth();x++) {
                for (int y=0;y<getHeight();y++) {
                    if  (getBit(x,y)) {
                        this.onBit = new Point(x,y);
                        return onBit;
                    }
                }
            }    
        }
        return null;        
    }
    
    public Point getOnBit(Rotation r) {
        return Rotations.toBoard(getOnBit(), this, r);
    }
    
    public static int[][] xySwitch(int[][] shape) {
        if (shape.length==0) {
            return new int[0][0];
        }
        int[][] newShape = new int[shape[0].length][shape.length];
                
        for (int i=0;i<shape.length;i++) {
            for (int j=0;j<shape[0].length;j++) {
                newShape[j][i] = shape[i][j];  
            }
        }        
        
        return newShape;  
        
    }
}
