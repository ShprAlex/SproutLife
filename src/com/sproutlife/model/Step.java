package com.sproutlife.model;

import java.util.Collection;

import com.sproutlife.Settings;
import com.sproutlife.model.echosystem.Board;
import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.echosystem.Organism;

public abstract class Step {
    

    GameModel gameModel;
     
    public Step(GameModel gameModel) {
        this.gameModel = gameModel;     
    }
      
    public GameModel getGameModel() {
        return gameModel;
    }
    
    public Stats getStats() {
        return gameModel.getStats();
    }
    
    public Echosystem getEchosystem() {
        return gameModel.getEchosystem();
    }
    
    public Collection<Organism> getOrganisms() {
    	return getEchosystem().getOrganisms();
    }
    
    public Board getBoard() {
        return gameModel.getBoard();
    }
    
    public int getClock() {
        return getEchosystem().getClock();
    }    
    
    public int getAge(Organism o) {
    	return getEchosystem().getAge(o);
    }
    
    public Settings getSettings() {
    	return getGameModel().getSettings();
    }
    
    public abstract void perform();
     
}
