/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.seed;

import java.awt.Point;

public class SeedSproutPattern {       
    
    protected BitPattern seedPattern;
    
    protected BitPattern sproutPattern;
    
    protected Point sproutOffset;   
    
    public BitPattern getSeedPattern() {
        return seedPattern;
    }
    
    public BitPattern getSproutPattern() {
        return sproutPattern;
    }
    
    public Point getSproutOffset() {
        return sproutOffset;
    }
    
    public Point getSproutCenter() {
        return getSproutPattern().getCenter();
    }
    
    public Point getSproutOffset(int rotation, boolean mirror) {
        return BitPattern.invRotateOffset(getSproutOffset(), getSeedPattern(), getSproutPattern(), rotation, mirror);
    }
    
    /*
    public Point getSproutOffset(int rotation) {
        Point rp = BitPattern.rotatePoint(getSproutOffset(), getSeedPattern().getWidth(), getSeedPattern().getHeight(), rotation);
        if (rotation == 1) {
            rp.y -= (getSproutPattern().getHeight(rotation)-1);
        }
        else if (rotation == 2) {
            rp.x -= (getSproutPattern().getWidth(rotation)-1);
            rp.y -= (getSproutPattern().getHeight(rotation)-1);
        }
        else if (rotation ==3 ) {
            rp.x -= (getSproutPattern().getWidth(rotation)-1);
            
        }
        return rp;
    }
    */
    
    public Point getSproutCenter(int rotation, boolean mirror) {
        return getSproutPattern().getCenter(rotation, mirror);
    }
    /*
    public static int[][] xySwitch(int[][] shape) {
        if (shape.length==0) {
            return new int[0][0];
        }
        int[][] newShape = new int[shape[0].length][shape.length];
                
        for (int i=0;i<shape.length;i++) {
            for (int j=0;j<shape.length;j++) {
                newShape[j][i] = shape[i][j];  
            }
        }        
        
        return newShape;  
        
    }
        
    public static int[][] rotate (int[][] shape, int angle) {
        if (angle==0) {
            return shape;
        }
        
        if (shape.length==0) {
            return new int[0][0];
        }
                        
        int[][] newShape = new int[0][0];
                
        if (angle==1) {
            newShape = new int[shape[0].length][shape.length];
            
        }
        else if (angle==2) {
            newShape = new int[shape.length][shape[0].length];
            
        }
        else if (angle==3) {
            newShape = new int[shape[0].length][shape.length];                        
        }
        
        for (int i=0;i<shape.length;i++) {
            for (int j=0;j<shape.length;j++) {
              if(angle==1) {                  
                  newShape[shape[0].length-j-1][i] = shape[i][j];
              }
              else if(angle==2) {
                  newShape[shape.length-i-1][shape[0].length-j-1] = shape[i][j];
              }
              else if(angle==3) {
                  newShape[j][shape.length-i-1] = shape[i][j];
              }  
            }
        }
        
        
        return newShape;                        
    }   

    public static Point rotate (Point point, int sproutW, int sproutH, int angle) {
        if (angle==0) {
            return point;
        }

        if(angle==1) {  
            return new Point(sproutH-point.y-1,point.x);
            //newShape[shape[0].length-j-1][i] = shape[i][j];
        }
        else if(angle==2) {
            return new Point(sproutW-point.x-1,sproutH-point.y-1);
            //newShape[shape.length-i-1][shape.length-j-1] = shape[i][j];
        }
        else if(angle==3) {
            return new Point(point.y,sproutW-point.x-1);
            //newShape[j][shape.length-i-1] = shape[i][j];
        }  

        return null; //shouldn't happen                         
    }
       
    public SeedSproutPattern(int[][] seedPattern, int[][] sproutPattern, Point seedOffset) {
        this.seedPattern = seedPattern;
        this.sproutPattern = sproutPattern;
        this.seedOffset = seedOffset;        
    }
    
    
    public SeedSproutPattern rotate() {
        int sproutW = this.getSproutPattern().length;
        int sproutH = this.getSproutPattern()[0].length;
        //int seedW = this.getSeedPattern().length;
        int seedH = this.getSeedPattern()[0].length;
        
        int[][] rSeedPattern = rotate(this.getSeedPattern(),1);        
        int[][] rSproutPattern = rotate(this.getSeedPattern(),1);        
        
        Point rotatePoint = rotate(this.getSeedOffset(),sproutW,sproutH,1);
        Point rSeedOffset = new Point(rotatePoint.x-seedH, rotatePoint.y);
        return new SeedSproutPattern(rSeedPattern,rSproutPattern,rSeedOffset);                 
    }
    */
}
