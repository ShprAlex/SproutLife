/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.geometry;

/**
 * @author Alex Shapiro
 *
 */
public class Rotations {
    private static Rotation[][] rotations = new Rotation[4][2]; 

    public static Rotation getRotation(int angle, boolean mirror) {
        Rotation r = rotations[angle][mirror?0:1];
        if (r==null) {
            r = new Rotation(angle, mirror);
            rotations[angle][mirror?0:1]=r;
        }
        return r;
    }
    
    public static Rotation rotateRight(Rotation r, int angle, boolean mirror) {
        boolean newMirror = (mirror && !r.isMirror()) || (!mirror && r.isMirror());
        if (!newMirror) {
            return getRotation(r.getAngle()+angle, newMirror);
        }
        else {
            return getRotation(r.getAngle()-angle, newMirror);
        }
    
    }
}
