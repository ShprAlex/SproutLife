/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step.lifemode;

import java.util.Collection;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.Stats;
import com.sproutlife.model.echosystem.Board;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.echosystem.Organism;

public abstract class LifeMode {
    GameModel gameModel;

    public LifeMode(GameModel gameModel) {
        this.gameModel = gameModel;     
    }

    public abstract void perform();

    protected abstract boolean keepAlive(Cell c, Collection<Cell> neighbors, int x, int y);

    protected abstract Cell getBorn(Collection<Cell> neighbors, int x, int y);

    protected GameModel getGameModel() {
        return gameModel;
    }

    protected Stats getStats() {
        return gameModel.getStats();
    }

    protected Echosystem getEchosystem() {
        return gameModel.getEchosystem();
    }

    protected Collection<Organism> getOrganisms() {
        return getEchosystem().getOrganisms();
    }

    protected Board getBoard() {
        return gameModel.getBoard();
    }

    protected int getTime() {
        return getEchosystem().getTime();
    }        

    protected Settings getSettings() {
        return getGameModel().getSettings();
    }    
}
