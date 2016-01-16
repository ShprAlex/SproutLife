package com.sproutlife.model.seed.patterns;

import java.awt.Point;

import com.sproutlife.model.seed.BitPattern;
import com.sproutlife.model.seed.SeedSproutPattern;

public class BoxhatRpPattern extends SeedSproutPattern {
    
    public BoxhatRpPattern() {

        this.seedPattern = new BitPattern(new int[][]                                             
                {{0,1,1},
                 {0,1,1},                          
                 {0,0,1}},
                 true);

           this.sproutPattern = new BitPattern(new int[][]  
                {{0,1,1},
                 {1,1,0},                         
                 {0,1,0}},
                 true);

           this.sproutOffset = new Point(0,0);

    }
}
