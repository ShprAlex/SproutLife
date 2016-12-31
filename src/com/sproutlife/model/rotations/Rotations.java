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
    
    public static Point fromBoard(Point point, BitPattern bp1, Rotation r) {
        
        if (r.isMirror()) {
            point = new Point (bp1.getWidth(r)-point.x-1,  point.y);
        }
        switch (r.getAngle()) {

            case 1: return new Point(bp1.getHeight(r)-point.y-1, point.x);

            case 2: return new Point(bp1.getWidth(r)-point.x-1, bp1.getHeight(r)-point.y-1);
            
            case 3: return new Point(point.y, bp1.getWidth(r)-point.x-1);
             
            //Case 0:
            default: return point; 

        }
    }
    
    public static Point toBoard(Point point, BitPattern bp1, Rotation r) {
        if (!r.isMirror()) {
            switch (r.getAngle()) {
                
                case 1: return new Point(point.y, bp1.getWidth()-point.x-1);
                
                case 2: return new Point(bp1.getWidth()-point.x-1, bp1.getHeight()-point.y-1);
                
                case 3: return new Point(bp1.getHeight()-point.y-1, point.x); 
                //Case 0:
                default: return point; 
                
            }
        }
        else {
            //In case of mirror, reflect the first parameter, not x
            switch (r.getAngle()) {
                
                case 1: return new Point(bp1.getHeight()-point.y-1, bp1.getWidth()-point.x-1);
                
                case 2: return new Point(point.x, bp1.getHeight()-point.y-1);
                
                case 3: return new Point(point.y, point.x); 
                //Case 0:
                default: return new Point(bp1.getWidth()-point.x-1, point.y); 
                
            }
        }
    }
    
    public static Point translate(Point p, int tx, int ty) {
        return new Point(p.x+tx,p.y+ty);
    }
    
    public static Point translate(Point p, Point t) {
        return translate(p, t.x, t.y);
    }
    
    public static Point offsetToBoard(Point offset, BitPattern bp1, BitPattern bp2, Rotation r) {
        Point p2a = offset;
        Point p2b = translate(offset, bp2.getWidth() - 1, 0);
        Point p2c = translate(offset, 0, bp2.getHeight() - 1);
        Point p2d = translate(offset, bp2.getWidth() - 1, bp2.getHeight() - 1);
        
        Point rp2a = toBoard(p2a, bp1, r);
        Point rp2b = toBoard(p2b, bp1, r);
        Point rp2c = toBoard(p2c, bp1, r);
        Point rp2d = toBoard(p2d, bp1, r);

        int minx = Math.min(Math.min(rp2a.x, rp2b.x), Math.min(rp2c.x, rp2d.x));
        int miny = Math.min(Math.min(rp2a.y, rp2b.y), Math.min(rp2c.y, rp2d.y));
        return new Point(minx, miny);        
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
