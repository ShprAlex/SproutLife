package com.sproutlife.model;

import java.util.HashMap;

import com.sproutlife.Settings;
import com.sproutlife.model.echosystem.Board;
import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.echosystem.Organism;

public class GameModel {
 
    Echosystem echosystem;  
    
    GameStep gameStep;
    
    Settings settings;
    
    Stats stats;   
          
    public GameModel(Settings settings) {
        this.settings = settings;
    	echosystem = new Echosystem();
        gameStep = new GameStep(this);                        
        stats = new Stats(this);
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
    
    public Settings getSettings() {
		return settings;
	}
    
    public void set(String s, Object o) {
    	getSettings().set(s,o);
    }

}
