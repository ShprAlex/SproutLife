package com.sproutlife.model;

import java.util.HashMap;

import com.sproutlife.Settings;
import com.sproutlife.model.echosystem.Board;
import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.step.GameStep;

public class GameModel {
 
    Echosystem echosystem;  
    
    GameClock clock;
    
    GameStep gameStep;
    
    Settings settings;
    
    Stats stats;   
          
    public GameModel(Settings settings) {
        this.settings = settings;
        this.clock = new GameClock();
    	echosystem = new Echosystem(clock);
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
    
    public int getTime() {
    	return clock.getTime();
    }
    
    public GameClock getClock() {
        return clock;
    }
    
    public void incrementTime() {
        clock.increment();
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
