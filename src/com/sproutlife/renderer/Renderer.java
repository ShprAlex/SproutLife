package com.sproutlife.renderer;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.sproutlife.model.GameModel;
import com.sproutlife.panel.GamePanel;

public abstract class Renderer {
    
    protected GameModel gameModel;

    public Renderer(GameModel gameModel) {
        this.gameModel = gameModel;	        
    }

    public int getBlockSize() {
        return BoardRenderer.BLOCK_SIZE;
    }
    
    public GameModel getGameModel() {
        return gameModel;
    }	
}
