/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.seed;

import java.awt.Point;

public class SymmetricSeed extends Seed {       
    
    public SymmetricSeed(SeedSproutPattern pattern) {
        super(pattern);
        // TODO Auto-generated constructor stub
    }       
    
    
        
    @Override
    public int getRotation() {
        
        int ox = getParentPosition().x;
        int oy = getParentPosition().y;
        
        Point farCorner = getFarCorner();
                        
        if(farCorner.x >= ox && farCorner.y > oy) {
            
            return 2;

        }
        if(farCorner.y >= oy && farCorner.x < ox) {
            
            return 1;

        }
        if(farCorner.x <= ox && farCorner.y < oy) {
            
            return 0;

        }

        if(farCorner.y <= oy && farCorner.x > ox) {
            
            return 3;
        }
        
        return 0;
    }
    
    @Override
    public boolean getSeedBit(int x, int y) {
        //No rotation
        return getSeedPattern().getBit(x, y);
    }
    
    private Point getFarCorner() {
    
        int ox = getParentPosition().x;
        int oy = getParentPosition().y;
        
        int x = getPosition().x;
        int y = getPosition().y;
        
        int maxX = getPosition().x+getSeedPattern().getWidth()-1;
        int maxY = getPosition().y+getSeedPattern().getHeight()-1;
         
        if (maxX+x == ox*2 && maxY-oy > oy-getPosition().y) {
            //x = maxX; 
            y = maxY;  
            return new Point(x,y);
        }
        if (maxX+x == ox*2 && maxY-oy < oy-getPosition().y) {            
            //y = maxY;  
            x = maxX;
            return new Point(x,y);
        }
        if (maxX-ox > ox-getPosition().x && maxY+y == oy*2) {
            x = maxX;
            y = maxY;
            return new Point(x,y);
        }
        if (maxX-ox < ox-getPosition().x && maxY+y == oy*2) {
            return new Point(x,y);
        }
                
        if (maxX-ox > ox-getPosition().x ) {            
            x = maxX;                
        }
        
        if (maxY-oy > oy-getPosition().y ) {            
            y = maxY;                            
        }
        
        return new Point(x,y);
        
    }
}
