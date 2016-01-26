package com.sproutlife.model.step.lifemode;

import java.util.ArrayList;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;

public class FriendlyLife extends CooperativeLife {
    
    public FriendlyLife(GameModel gameModel) {
        super(gameModel);        
    }
    
    public Cell keepAlive(Cell me, ArrayList<Cell> neighbors, int i, int j) {

        int friendCount = 0;

        for (Cell neighbor : neighbors) {            
            if (me.getOrganism()==neighbor.getOrganism()) {
                friendCount++;
            }                
        }
        if (friendCount>=1&&(neighbors.size() == 2 || neighbors.size()==3)) { 
            me.age+=1;                
            return me;

        }

        return null;

    }

}
