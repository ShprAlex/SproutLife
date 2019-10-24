/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.echosystem;

import java.awt.Point;
import java.util.HashSet;
import java.util.Random;

/**
 * Class used for keeping track of an Organism's attributes some of which are
 * experimental. For the sake of speed we implement these as variables rather
 * than using a hashtable.
 * 
 * @author Alex Shapiro
 *
 */
public class OrgAttributes {

    // TODO: Kind is mostly used for color, should probably be refactored
    // outside of the organism class.
    public int kind = 0;

    // territory is used to track all visited points
    public HashSet<Point> territory;
    // territoryProduct is the sum of territory size at every timestep
    public int territoryProduct = 0;
    public int competitiveScore = 0;
    public int cellSum = 0;

    public int maxCells;
    public int parentAgeAtBirth;
    public int birthOrder;
    public int collisionCount=0;

    public int singleChildPathLength=0;

    public OrgAttributes(Organism o) {
        this.territory = new HashSet<Point>();        
        this.maxCells = 0;

        Organism parent = o.getParent();
        if (parent==null) {
            this.kind = (new Random()).nextInt(3); //kind = 0;
            this.birthOrder = 0;
        }
        else {
            this.kind = parent.getAttributes().kind;
            this.parentAgeAtBirth = parent.getAge();
            this.birthOrder = parent.getChildren().size();
        }
    }

    public int getTerritorySize() {
        return territory.size();
    }
}
