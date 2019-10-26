/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.renderer;

import java.awt.Graphics2D;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.renderer.colors.ColorModel;

public abstract class OrganismRenderer {

    protected GameModel gameModel;
    protected BoardRenderer boardRenderer;

    public OrganismRenderer(GameModel gameModel, BoardRenderer boardRenderer) {
        this.gameModel = gameModel;
        this.boardRenderer = boardRenderer;
    }

    public abstract void render(Graphics2D g, Organism o);

    public int getBlockSize() {
        return boardRenderer.getBlockSize();
    }      

    public GameModel getGameModel() {
        return gameModel;
    }

    public BoardRenderer getBoardRenderer() {
        return boardRenderer;
    }

    public ColorModel getColorModel() {
        return boardRenderer.getColorModel();
    }
}
