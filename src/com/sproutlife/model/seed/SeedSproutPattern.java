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
    
    public Point getSproutCenter(int rotation, boolean mirror) {
        return getSproutPattern().getCenter(rotation, mirror);
    }
}
