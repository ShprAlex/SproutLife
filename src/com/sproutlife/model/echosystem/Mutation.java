package com.sproutlife.model.echosystem;

import java.awt.Point;

public class Mutation extends Object {
        
    private Point location;
    int organismAge;
    int gameTime;
    
    public Mutation(Point location, int organismAge, int gameTime) {
        this.location = location; 
        this.organismAge = organismAge;
        this.gameTime = gameTime;
        // TODO Auto-generated constructor stub
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
