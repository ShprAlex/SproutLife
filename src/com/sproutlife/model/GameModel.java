package com.sproutlife.model;

import com.sproutlife.model.echosystem.Board;
import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.echosystem.Organism;

public class GameModel {
 
    Echosystem echosystem;  
    
    GameStep gameStep;
    
    boolean mutationEnabled;   
    
    Stats stats;   
          
    public GameModel() {
        echosystem = new Echosystem();
        gameStep = new GameStep(this);                        
        stats = new Stats(this);      
        mutationEnabled = true;
    }
    
    public GameStep getGameStep() {
        return gameStep;
    }
    
    public Echosystem getEchosystem() {
        return echosystem;
    }
    
    public Board getBoard() {
        return echosystem.getBoard();
    }          
    
    public int getClock() {
    	return echosystem.getClock();
    }
    
    public Stats getStats() {
        return stats;
    }
    
    public void setMutationEnabled(boolean mutationEnabled) {
        this.mutationEnabled = mutationEnabled;
    }
    
    public boolean isMutationEnabled() {
        return mutationEnabled;
    }
    

}
