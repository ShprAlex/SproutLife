package com.sproutlife;

import com.sproutlife.model.GameModel;
import com.sproutlife.view.GameFrame;

public class GameController {
    GameFrame frame;
    
    private GameModel gameModel;    
    private GameThread game;
    
    public GameController() {
        gameModel = new GameModel();        
     
        frame = new GameFrame(this);
        
        //game = new GameThread(frame.getGamePanel());
    }
    
    public void start() {
        frame.setVisible(true);  
        
        getGameModel().getEchosystem().resetCells();
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
}
