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
    //int kind = 0;
    public int age= 1;
         
    public Cell(int x, int y) {
        super(x,y);
        /*
        for (GenePoint p2 : point) {
            if (Math.abs(p2.x - this.x)<=1&&Math.abs(p2.y - this.y)<=1) {
                this.type = p2.type;
            }
            else {
                //this.type = typeCount++;
            }
        }
        */
    
    }
    
    public Cell(int x, int y, Organism o) {
        super(x,y);
        this.organism = o;   
    }
    
    public Cell(int x, int y, ArrayList<Cell> surrounding) {
        super(x,y);
        this.organism=surrounding.get(0).getOrganism();
        //this.kind = typeMap.get(type).getKind();
    }
    
    public void addToOrganism() {
        organism.addCell(this.x, this.y);
    }      
    
    public Organism getOrganism() {
        return organism;
    }
    
    private void setOrganism(Organism o) {
        this.organism = o;
    }
    
    public boolean sameOrganism(Cell p2) {
        return this.getOrganism().equals(p2.getOrganism());
    }
    
    public void mutate() {
        
    }
                                                           
}
