/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.echosystem;

import java.awt.Point;
import java.util.ArrayList;


public class Cell extends Point {            
    
    private Organism organism = null;

    public int age; //experimental
    
    boolean markedAsSeed;     
    
    public Cell(int x, int y, Organism o) {
        super(x,y);
        this.organism = o;   
        this.age = 1;
        this.markedAsSeed = false;  
    }
    
    public Cell(int x, int y, ArrayList<Cell> surrounding) {
        this(x, y, surrounding.get(0).getOrganism());        
    }
    
    public boolean isMarkedAsSeed() {
        return markedAsSeed;
    }

    public void setMarkedAsSeed(boolean markedAsSeed) {
        this.markedAsSeed = markedAsSeed;
    }
    
    public Organism getOrganism() {
        return organism;
    }
    
    public boolean sameOrganism(Cell c2) {
        return this.getOrganism().equals(c2.getOrganism());
    }
                                                           
}
