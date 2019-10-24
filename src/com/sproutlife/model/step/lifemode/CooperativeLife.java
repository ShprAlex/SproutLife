/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step.lifemode;

import java.util.ArrayList;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class CooperativeLife extends LifeMode {
    
    public CooperativeLife(GameModel gameModel) {
        super(gameModel);
        // TODO Auto-generated constructor stub
    }
    
    public Cell keepAlive(Cell me, ArrayList<Cell> neighbors, int x, int y) {
        
        if ((neighbors.size() == 2 || neighbors.size()==3)) { 
            me.age+=1;                
            return me;
        }
        
        return null;
        
    }

    public Cell getBorn(ArrayList<Cell> neighbors, int x, int y) {
        if (x<0||x>getBoard().getWidth()-1||y<0||y>getBoard().getHeight()-1) {
            return null;
        }
        
        if (neighbors.size() != 3) {
            return null;
        }

        //Quick check to see if all neighbors are from the same organism
        Organism checkSingleOrg = neighbors.get(0).getOrganism();
        boolean singleOrg = true;
        for (Cell cell : neighbors) {
            if (cell.getOrganism() != checkSingleOrg) {
                singleOrg = false;
                break;
            }
        }
        if (singleOrg) {
            Cell bornCell = getEchosystem().createCell(x,y,neighbors);
            return bornCell;
        }        
        
        return null;
    }
}
