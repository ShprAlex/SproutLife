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
import java.util.List;

/**
 * A Cell keeps track its coordinates and which organism it belongs to.
 * 
 * For now cells extend Point, which gives us hash code and equals methods, this
 * dependency should probably be removed later.
 * 
 * @author Alex Shapiro
 */
public class Cell extends Point {

    private Organism organism = null;

    public int age; // experimental

    boolean markedAsSeed;

    public Cell(int x, int y, Organism o) {
        super(x, y);
        this.organism = o;
        this.age = 1;
        this.markedAsSeed = false;
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

    public boolean isSameOrganism(Cell c2) {
        return this.getOrganism().equals(c2.getOrganism());
    }

}
