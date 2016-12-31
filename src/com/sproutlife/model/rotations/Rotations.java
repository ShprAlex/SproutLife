/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.rotations;

import java.awt.Point;

import com.sproutlife.model.seed.BitPattern;

/**
 * @author Alex Shapiro
 *
 */
public class Rotations {
    private static Rotation[][] rotations = new Rotation[4][2]; 

    public static Rotation get(int angle, boolean mirror) {
        Rotation r = rotations[angle][mirror?0:1];
        if (r==null) {
            r = new Rotation(angle, mirror);
            rotations[angle][mirror?0:1]=r;
        }
        return r;
    }
    
    public static Rotation get(int angle) {
        return get(angle, false);
    }
    
    public static Rotation get() {
        return get(0, false);
    }
    
    public static Point fromBoard(Point point, BitPattern p1, Rotation r) {
        
        if (r.isMirror()) {
            point = new Point (p1.getWidth(r)-point.x-1,  point.y);
        }
        switch (r.getAngle()) {

            case 1: return new Point(p1.getHeight(r)-point.y-1, point.x);

            case 2: return new Point(p1.getWidth(r)-point.x-1, p1.getHeight(r)-point.y-1);
            
            case 3: return new Point(point.y, p1.getWidth(r)-point.x-1);
             
            //Case 0:
            default: return point; 

        }
    }
    
    public static Point toBoard(Point point, BitPattern p1, Rotation r) {
        if (!r.isMirror()) {
            switch (r.getAngle()) {
                
                case 1: return new Point(point.y, p1.getWidth()-point.x-1);
                
                case 2: return new Point(p1.getWidth()-point.x-1, p1.getHeight()-point.y-1);
                
                case 3: return new Point(p1.getHeight()-point.y-1, point.x); 
                //Case 0:
                default: return point; 
                
            }
        }
        else {
            //In case of mirror, reflect the first parameter, not x
            switch (r.getAngle()) {
                
                case 1: return new Point(p1.getHeight()-point.y-1, p1.getWidth()-point.x-1);
                
                case 2: return new Point(point.x, p1.getHeight()-point.y-1);
                
                case 3: return new Point(point.y, point.x); 
                //Case 0:
                default: return new Point(p1.getWidth()-point.x-1, point.y); 
                
            }
        }
    }
    
    public static Point offsetToBoard(Point point, BitPattern p1, BitPattern p2, Rotation r) {
        Point rp = toBoard(point, p1, r);
        if (!r.isMirror()){
            if (r.getAngle() == 1) {
                rp.y -= (p2.getHeight(r)-1);
            }
            else if (r.getAngle() == 2) {
                rp.x -= (p2.getWidth(r)-1);
                rp.y -= (p2.getHeight(r)-1);
            }
            else if (r.getAngle() ==3 ) {
                rp.x -= (p2.getWidth(r)-1);            
            }
        }
        else {
            if (r.getAngle() == 1) {
                rp.x -= (p2.getWidth(r)-1);    
                rp.y -= (p2.getHeight(r)-1);
                
            }
            else if (r.getAngle() == 2) {                
                rp.y -= (p2.getHeight(r)-1);
            }
            else if (r.getAngle() ==3 ) {
                            
            }   
            else if (r.getAngle() == 0) {
                rp.x -= (p2.getWidth(r)-1);
            }
        }        
        
        return rp;
    }
    
    public static Point fromBoard(Point point, Rotation r) {
        if (r.isMirror()) {
            point = new Point(-point.x, point.y);
        }
        switch (r.getAngle()) {
        // case 0:
            default:
                return new Point(point.x, point.y);

            case 1:
                return new Point(-point.y, point.x);
            case 2:
                return new Point(-point.x, -point.y);
            case 3:
                return new Point(point.y, -point.x);

        }
    }

    public static Point toBoard(Point point, Rotation r) {
        if (!r.isMirror()) {
            switch (r.getAngle()) {
            // case 0:
                default:
                    return new Point(point.x, point.y);
                case 1:
                    return new Point(point.y, -point.x);
                case 2:
                    return new Point(-point.x, -point.y);
                case 3:
                    return new Point(-point.y, point.x);

            }
        }
        else { // mirror == true
            switch (r.getAngle()) {
            // case 0:
                default:
                    return new Point(-point.x, point.y);

                case 1:
                    return new Point(-point.y, -point.x);
                case 2:
                    return new Point(point.x, -point.y);
                case 3:
                    return new Point(point.y, point.x);
            }
        }
    }

}
