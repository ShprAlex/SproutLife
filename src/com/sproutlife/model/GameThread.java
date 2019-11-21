/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.sproutlife.model.step.GameStep.StepType;
import com.sproutlife.model.step.GameStepEvent;
import com.sproutlife.model.step.GameStepListener;

public class GameThread {
    private boolean playGame = false;

    Thread innerThread;

    List<GameStepListener> gameStepListeners;

    GameModel gameModel;
    ReentrantReadWriteLock interactionLock;

    int sleepDelay;
    int iterationsPerEvent;

    public GameThread(GameModel gameModel, ReentrantReadWriteLock interactionLock) {     
        this.gameModel = gameModel;
        this.gameStepListeners = new ArrayList<>();
        this.interactionLock = interactionLock;
        
        this.sleepDelay = 0;
        this.iterationsPerEvent = 1;
    }

    private GameModel getGameModel() {
        return gameModel;
    }

    public void setPlayGame(boolean playGame) {
        this.playGame = playGame;
        if (playGame) {
            new InnerGameThread().start();
        }
    }    

    public boolean isPlaying() {
        return playGame;
    }

    public int getSleepDelay() {
        return sleepDelay;
    }

    public void setSleepDelay(int sleepDelay) {
        this.sleepDelay = sleepDelay;
    }

    public int getIterationsPerEvent() {
        return iterationsPerEvent;
    }

    public void setIterationsPerEvent(int iterationsPerEvent) {
        this.iterationsPerEvent = iterationsPerEvent;
    }

    public void addGameStepListener(GameStepListener gameStepListener) {
        gameStepListeners.add(gameStepListener);
    }

    public void removeGameStepListener(GameStepListener gameStepListener) {
        gameStepListeners.remove(gameStepListener);
    }

    private void fireStepBundlePerformed() {
        for (GameStepListener gsl : gameStepListeners) {
            GameStepEvent event = new GameStepEvent(StepType.STEP_BUNDLE);
            gsl.stepPerformed(event);
        }       
    }

    private class InnerGameThread extends Thread {
        public void run() {
            while (playGame) {
                try {
                    interactionLock.writeLock().lock();
                    getGameModel().performGameStep();
                    interactionLock.writeLock().unlock();

                    int iterationsPerEvent = getIterationsPerEvent();
                    if (getGameModel().getTime() % iterationsPerEvent == 0) {
                        fireStepBundlePerformed();
                        int sleep = Math.max(1, getSleepDelay());                        
                        //Painting is glitchy if sleepDelay is less than 1;
                        Thread.sleep(sleep);
                    }
                }
                catch (InterruptedException ex) {}
            }
        }
    }
}

