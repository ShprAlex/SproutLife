package com.sproutlife.model.seed;

import java.awt.Point;

import com.sproutlife.model.echosystem.Organism;

public class Seed {
    
    protected SeedSproutPattern pattern;       
    public Point position = null;    
    public int rotation = 0;    
    public int seedBorder = 1;   
    public boolean mirror;
    public Point parentPosition;
    //Organism organism;   
           
    public Seed(SeedSproutPattern pattern, int rotation, boolean mirror) {    
        this.pattern = pattern;
        this.rotation = rotation;
        this.mirror = mirror;
    } 
    
    public Seed(SeedSproutPattern pattern, int rotation) {    
        this(pattern, rotation , false);
    }     
    
    public Seed(SeedSproutPattern pattern) {
        this(pattern, 0 , false);
    }
     
    public int getRotation() {
        return rotation;
    }
    
    public boolean isMirror() {
        return mirror;
    }
    
    public Point getPosition() {        
        return position;        
    }    
    
    public void setPosition(int x, int y) {
        this.position = new Point(x,y);
    }             
    
    public void setMirror(boolean mirror) {
        this.mirror = mirror;
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
        return getSeedPattern().getBit(x, y, getRotation(), isMirror());
    }
    
    public boolean getSproutBit(int x, int y) {       
        return getSproutPattern().getBit(x, y, getRotation(), isMirror());
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
        return pattern.getSproutOffset(getRotation(), isMirror());
    }      
    
    public Point getSproutCenter() {
        return pattern.getSproutCenter(rotation, mirror);
    }
    
    public Point getSeedOnBit() {
        return pattern.getSeedPattern().getOnBit(rotation, mirror);
    }
    
    public Point getSeedOnPosition() {
        Point pos = this.getPosition();
        Point onOffset = this.getSeedOnBit();        
        return new Point(pos.x+onOffset.x,pos.y+onOffset.y);
    }
    
     

}
