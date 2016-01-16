package com.sproutlife.model;

import com.sproutlife.model.echosystem.Board;
import com.sproutlife.model.echosystem.Echosystem;

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
    
    public Board getBoard() {
        return gameModel.getBoard();
    }
    
    public int getClock() {
        return gameModel.getClock();
    }    
    
    public abstract void perform();
     
}
