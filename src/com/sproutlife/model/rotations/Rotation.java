/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.rotations;

/**
 * @author Alex Shapiro
 *
 */
public class Rotation {
    private int angle; // 0 = 0, 1 = 90, 2 = 180, 3 = 270
    private boolean mirror;

    public Rotation(int angle, boolean mirror) {
        this.angle = angle;
        this.mirror = mirror;
    }
    
    public int getAngle() {
        return angle;
    }
    
    public boolean isMirror() {
        return mirror;
    }
}
