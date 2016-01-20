package com.sproutlife.renderer;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.sproutlife.model.GameModel;
import com.sproutlife.panel.gamepanel.GamePanel;

public abstract class Renderer {
    
    protected GameModel gameModel;

    public int BLOCK_SIZE; //will refactor this

    public Renderer(GameModel gameModel) {
        this.gameModel = gameModel;	
        BLOCK_SIZE = GamePanel.BLOCK_SIZE;
    }

    public GameModel getGameModel() {
        return gameModel;
    }	
}
