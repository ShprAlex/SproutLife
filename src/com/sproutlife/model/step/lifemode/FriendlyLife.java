/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step.lifemode;

import java.util.Collection;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;

public class FriendlyLife extends ParallelLife {

    public FriendlyLife(GameModel gameModel) {
        super(gameModel);        
    }

    public boolean keepAlive(Cell c, Collection<Cell> neighbors, int x, int y) {
        int sameOrgCount = 0;
        for (Cell neighbor : neighbors) {            
            if (c.getOrganism()==neighbor.getOrganism()) {
                sameOrgCount++;
            }
        }
        if (sameOrgCount >=1 && (neighbors.size() == 2 || neighbors.size()==3)) {
            c.age+=1;
            return true;
        }
        return false;
    }
}
