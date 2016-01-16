package com.sproutlife.model.echosystem;

import java.awt.Point;

public class Mutation {
        
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
    
}
