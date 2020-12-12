/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.sproutlife.gamesteplistener.DefaultGameStepListener;
import com.sproutlife.gamesteplistener.WarmupGameStepListener;
import com.sproutlife.model.GameModel;
import com.sproutlife.panel.PanelController;

public class GameController {
    private GameModel gameModel;
    private PanelController panelController;
    private Settings settings;
    protected ReentrantReadWriteLock interactionLock;
    private ArrayList<File> loadedFiles;

    public GameController() {
        settings = new Settings();
        interactionLock = new ReentrantReadWriteLock();
        gameModel = new GameModel(settings, interactionLock);
        loadedFiles = new ArrayList<>();
        panelController = new PanelController(this);
        getGameModel().getGameThread().addGameStepListener(new DefaultGameStepListener(panelController));
        getGameModel().getGameThread().addGameStepListener(new WarmupGameStepListener(panelController));
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

    public List<File> getLoadedFiles() {
        return this.loadedFiles;
    }

    public void clearLoadedFiles() {
        this.loadedFiles = new ArrayList<>();

        panelController.getGameFrame().setTitle("SproutLife - Evolving Game of Life - V " + SproutLife.getAppVersion());
    }

    public void addLoadedFile(File file) {
        this.loadedFiles.add(file);

        String title = "SproutLife - ";
        for (int i = 0; i < this.loadedFiles.size(); i++) {
            if (i > 0) {
                title += ", ";
            }
            title += this.loadedFiles.get(i).getName();
        }
        panelController.getGameFrame().setTitle(title);
    }
}
