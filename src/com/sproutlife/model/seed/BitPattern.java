/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.seed;

import java.awt.Point;

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
    
    public int getWidth(int rotation) {
        if (rotation==0 || rotation==2) {
            return getWidth();
        }
        return getHeight();
    }
    
    public int getHeight() {
        return bitPattern[0].length;
    }
    
    public int getHeight(int rotation) {
        if (rotation==0 || rotation==2) {
            return getHeight();
        }
        return getWidth();
    }
    
    boolean getBit(int x, int y) {
        return bitPattern[x][y]==1;
    }
    
    boolean getBit(int x, int y, int rotation) {        
        return getBit(x, y, rotation, false);
    }
    
    boolean getBit(int x, int y, int rotation, boolean mirror) {
        if (mirror) {
            x = getWidth(rotation)-x-1;
        }
        switch (rotation) {
            case 1: return getBit(getHeight(rotation)-y-1,x);
            case 2: return getBit(getWidth(rotation)-x-1,getHeight(rotation)-y-1);
            case 3: return getBit(y,getWidth(rotation)-x-1);
             //case 0:
            default: return getBit(x,y);
        }          
    }
        
    public Point getCenter() {        
        return new Point((getWidth()-1)/2,(getHeight()-1)/2);
    }
    
    public Point getCenter(int rotation) {
        return getCenter(rotation, false);
    }
    
    public Point getCenter(int rotation, boolean mirror) {
        return invRotatePoint(getCenter(), this, rotation, mirror);
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
    
    public Point getOnBit(int rotation) {
        return invRotatePoint(getOnBit(), this, rotation, false);
    }
    
    public Point getOnBit(int rotation, boolean mirror) {
        return invRotatePoint(getOnBit(), this, rotation, mirror);
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
    
    public static Point rotatePoint(Point point, BitPattern p1, int rotation, boolean mirror) {
        
        if (mirror) {
            point = new Point (p1.getWidth()-point.x-1,  point.y);
        }
        switch (rotation) {

            case 1: return new Point(p1.getHeight()-point.y-1, point.x);

            case 2: return new Point(p1.getWidth()-point.x-1, p1.getHeight()-point.y-1);
            
            case 3: return new Point(point.y, p1.getWidth()-point.x-1);
             
            //Case 0:
            default: return point; 

        }
    }
    
    public static Point invRotatePoint(Point point, BitPattern p1, int rotation, boolean mirror) {
        if (!mirror) {
            switch (rotation) {
                
                case 1: return new Point(point.y, p1.getWidth()-point.x-1);
                
                case 2: return new Point(p1.getWidth()-point.x-1, p1.getHeight()-point.y-1);
                
                case 3: return new Point(p1.getHeight()-point.y-1, point.x); 
                //Case 0:
                default: return point; 
                
            }
        }
        else {
            //In case of mirror, reflect the first parameter, not x
            switch (rotation) {
                
                case 1: return new Point(p1.getHeight()-point.y-1, p1.getWidth()-point.x-1);
                
                case 2: return new Point(point.x, p1.getHeight()-point.y-1);
                
                case 3: return new Point(point.y, point.x); 
                //Case 0:
                default: return new Point(p1.getWidth()-point.x-1, point.y); 
                
            }
        }
    }
    
    public static Point invRotateOffset(Point point, BitPattern p1, BitPattern p2, int rotation) {
        return invRotateOffset(point, p1, p2, rotation, false);
    }
    
    public static Point invRotateOffset(Point point, BitPattern p1, BitPattern p2, int rotation, boolean mirror) {
        Point rp = invRotatePoint(point, p1, rotation, mirror);
        if (!mirror) {
            if (rotation == 1) {
                rp.y -= (p2.getHeight(rotation)-1);
            }
            else if (rotation == 2) {
                rp.x -= (p2.getWidth(rotation)-1);
                rp.y -= (p2.getHeight(rotation)-1);
            }
            else if (rotation ==3 ) {
                rp.x -= (p2.getWidth(rotation)-1);            
            }
        }
        else {
            if (rotation == 1) {
                rp.x -= (p2.getWidth(rotation)-1);    
                rp.y -= (p2.getHeight(rotation)-1);
                
            }
            else if (rotation == 2) {                
                rp.y -= (p2.getHeight(rotation)-1);
            }
            else if (rotation ==3 ) {
                            
            }   
            else if (rotation == 0) {
                rp.x -= (p2.getWidth(rotation)-1);
            }
        }        
        
        return rp;
    }


}
