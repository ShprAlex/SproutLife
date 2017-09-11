/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.echosystem;

import java.awt.Point;


/**
 * A Mutation is a location + time relative to the organism's birth coordinates. 
 * If an organism has a cell under the mutation point, the cell dies.
 * 
 * @author Alex Shapiro
 */

public class Mutation extends Object {
        
    private Point location; //Un-rotated mutation location
    int organismAge; //How old is the organism when this mutation is affective
    int gameTime; //When was this mutation created, used for statistics.
    
    public Mutation(Point location, int organismAge, int gameTime) {
        this.location = location; 
        this.organismAge = organismAge;
        this.gameTime = gameTime;
    }
    
    public Point getLocation() {
        return location;
    }
    
    public int getOrganismAge() {
        return organismAge;
    }
    
    public int getGameTime() {
        return gameTime;
    }
    
    @Override
    public int hashCode() {
        return location.hashCode();
    }
    
    @Override
    public boolean equals(Object arg2) {
        Mutation m2 = (Mutation) arg2;
        return getLocation().equals(m2.getLocation()) && getOrganismAge()==m2.getOrganismAge();
    }
}
