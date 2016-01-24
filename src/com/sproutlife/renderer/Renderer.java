package com.sproutlife.renderer;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.sproutlife.model.GameModel;
import com.sproutlife.panel.gamepanel.GamePanel;

public abstract class Renderer {
    
    protected GameModel gameModel;
    protected BoardRenderer boardRenderer;

    public Renderer(GameModel gameModel, BoardRenderer boardRenderer) {
        this.gameModel = gameModel;	  
        this.boardRenderer = boardRenderer;
    }

    public int getBlockSize() {
        return boardRenderer.getBlockSize();
    }      
    
    public GameModel getGameModel() {
        return gameModel;
    }
    
    public BoardRenderer getBoardRenderer() {
        return boardRenderer;
    }
}
