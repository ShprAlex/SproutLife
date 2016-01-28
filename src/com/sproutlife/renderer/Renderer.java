/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.renderer;

import com.sproutlife.model.GameModel;

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
