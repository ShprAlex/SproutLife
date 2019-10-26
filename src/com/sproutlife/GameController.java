/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.sproutlife.model.GameModel;
import com.sproutlife.panel.PanelController;

public class GameController {
    private GameModel gameModel;
    private PanelController panelController;
    private Settings settings;
    protected ReentrantReadWriteLock interactionLock;

    public GameController() {
        settings = new Settings();
        interactionLock = new ReentrantReadWriteLock();
        gameModel = new GameModel(settings, interactionLock);
        panelController = new PanelController(this);
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public ReentrantReadWriteLock getInteractionLock() {
        return interactionLock;
    }

    public Settings getSettings() {
        return settings;
    }
}
