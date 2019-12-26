/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.sproutlife.Settings;
import com.sproutlife.model.echosystem.Board;
import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.step.GameStep;
import com.sproutlife.model.step.GameStepListener;
import com.sproutlife.model.utils.EchosystemUtils;

/**
 * GameModel contains the game state
 *
 * @author Alex Shapiro
 */
public class GameModel {
    private Echosystem echosystem;
    private GameClock clock;
    private GameStep gameStep;
    private GameThread gameThread;
    private Settings settings;
    private Stats stats;

    public GameModel(Settings settings, ReentrantReadWriteLock interactionLock) {
        this.settings = settings;
        this.clock = new GameClock();
        echosystem = new Echosystem(clock);
        gameStep = new GameStep(this);
        gameThread = new GameThread(this, interactionLock);
        stats = new Stats(this);
    }

    public Echosystem getEchosystem() {
        return echosystem;
    }

    public Board getBoard() {
        return echosystem.getBoard();
    }

    public int getTime() {
        return clock.getTime();
    }

    public Stats getStats() {
        return stats;
    }

    public Settings getSettings() {
        return settings;
    }

    public GameThread getGameThread() {
        return gameThread;
    }

    private GameClock getClock() {
        return clock;
    }

    private void incrementTime() {
        clock.increment();
    }

    public void performGameStep() {
        incrementTime();
        gameStep.perform();
    }

    public void resetGame() {
        getEchosystem().resetCells();
        EchosystemUtils.pruneEmptyOrganisms(getEchosystem());
        getEchosystem().clearRetiredOrgs();
        getStats().reset();
        getClock().reset();
    }

    public void setPlayGame(boolean playGame) {
        gameThread.setPlayGame(playGame);
    }

    public boolean isPlaying() {
        return gameThread.isPlaying();
    }

    public void setGameStepListener(GameStepListener l) {
        if (gameThread == null) {
            return;
        }
        gameThread.addGameStepListener(l);
    }
}
