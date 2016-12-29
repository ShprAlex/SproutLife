/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.seed;

import java.awt.Point;

import com.sproutlife.geometry.Rotation;
import com.sproutlife.geometry.Rotations;
import com.sproutlife.model.echosystem.Organism;

public class Seed {
    
    protected SeedSproutPattern pattern;       
    public Point position = null;    
    public Rotation rotation;    
    public int seedBorder = 1;   
    public Point parentPosition;
    //Organism organism;   

    public Seed(SeedSproutPattern pattern, Rotation r) {    
        this.pattern = pattern;
        this.rotation = r;
    }      
    
    public Seed(SeedSproutPattern pattern) {
        this(pattern, Rotations.get());
    }
     
    public Rotation getRotation() {
        return rotation;
    }
    
    public Point getPosition() {        
        return position;        
    }    
    
    public void setPosition(int x, int y) {
        this.position = new Point(x,y);
    }                  
    
    public int getSeedBorder() {
        return seedBorder;
    }
    
    public void setSeedBorder(int b) {
        this.seedBorder = b;
    }
    
    public Point getParentPosition() {
        return parentPosition;
    }
    
    public void setParentPosition(Point parentPosition) {
        this.parentPosition = parentPosition;
    }
    /*
    public Organism getOrganism() {
        return organism;
    }
    
    public void setOrganism(Organism organism) {
        this.organism = organism;
    }
    */
    
    public SeedSproutPattern getSeedSproutPattern() {
        return pattern;
    }
    
    public BitPattern getSeedPattern() {
        return pattern.getSeedPattern();
    }
    
    public BitPattern getSproutPattern() {
        return pattern.getSproutPattern();
    }       
    
    public boolean getSeedBit(int x, int y) {       
        return getSeedPattern().getBit(x, y, getRotation());
    }
    
    public boolean getSproutBit(int x, int y) {       
        return getSproutPattern().getBit(x, y, getRotation());
    }
     
    public int getSeedWidth() {
        return getSeedPattern().getWidth(rotation);
    }
    
    public int getSeedHeight() {
        return getSeedPattern().getHeight(rotation);
    }
        
    public int getSproutWidth() {
        return getSproutPattern().getWidth(rotation);
    }
        
    public int getSproutHeight() {
        return getSproutPattern().getHeight(rotation);
    }
        
    public Point getSproutOffset() {
        return pattern.getSproutOffset(rotation);
    }      
    
    public Point getSproutCenter() {
        return pattern.getSproutCenter(rotation);
    }
    
    public Point getSeedOnBit() {
        return pattern.getSeedPattern().getOnBit(rotation);
    }
    
    public Point getSeedOnPosition() {
        Point pos = this.getPosition();
        Point onOffset = this.getSeedOnBit();        
        return new Point(pos.x+onOffset.x,pos.y+onOffset.y);
    }
}
