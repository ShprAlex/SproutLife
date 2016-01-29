/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.seed.patterns;

import java.awt.Point;

import com.sproutlife.model.seed.BitPattern;
import com.sproutlife.model.seed.SeedSproutPattern;

public class OnebitB1RpPattern extends SeedSproutPattern {
    
    public OnebitB1RpPattern() {

        this.seedPattern = new BitPattern(new int[][]                                             
               {{0,0,0},
                {0,1,0},                          
                {0,0,0}},               
                true);

        this.sproutPattern = new BitPattern(new int[][]  
               {{0,1,1},
                {1,1,0},                         
                {0,1,0}},
                true);

        this.sproutOffset = new Point(-1,-1);

    }
}
