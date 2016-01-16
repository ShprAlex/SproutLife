package com.sproutlife.model.seed.patterns;

import java.awt.Point;

import com.sproutlife.model.seed.BitPattern;
import com.sproutlife.model.seed.SeedSproutPattern;
import com.sproutlife.model.seed.SeedFactory.SeedType;

public class Bentline1mRpPattern extends SeedSproutPattern {
    
    public Bentline1mRpPattern() {
        
        this.seedPattern = new BitPattern(new int[][]                                             
                {{1,0,0},
                {0,1,1},                          
                {0,0,0}},
                true);

        this.sproutPattern = new BitPattern(new int[][]  
                {{0,1,1},
                {1,1,0},                         
                {0,1,0}},
                true);

        this.sproutOffset = new Point(0,0);

    }
}
