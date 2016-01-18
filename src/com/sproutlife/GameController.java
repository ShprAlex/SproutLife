package com.sproutlife;

import com.sproutlife.model.GameModel;
import com.sproutlife.panel.GameFrame;

public class GameController {
    GameFrame frame;
    
    private GameModel gameModel;    
    private GameThread game;
    
    private Settings settings;
    
    
    public GameController() {
    	settings = new Settings();
    	
    	gameModel = new GameModel(settings);        
     
        frame = new GameFrame(this);
                        
        //game = new GameThread(frame.getGamePanel());
    }
    
    public void start() {
        frame.setVisible(true);          
    }        
    
    public GameModel getGameModel() {
        return gameModel;
    }
    
    public GameFrame getFrame() {
        return frame;
    }
    
    public void setGameBeingPlayed(boolean isBeingPlayed) {
        if (isBeingPlayed) {
           
            game = new GameThread(this);
            game.setPlayGame(true);
            game.start();
            
        } else {
            game.setPlayGame(false);
            
        }
    }    
    
    public Settings getSettings() {
		return settings;
	}
    
    public void set(String s, Object o) {
    	getSettings().set(s,o);
    }    
    
}
