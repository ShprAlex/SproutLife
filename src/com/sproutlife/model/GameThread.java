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

    boolean superSlowIntro;
    boolean slowIntro;
    boolean autoAdjust;

    public GameThread(GameModel gameModel, ReentrantReadWriteLock interactionLock) {     
        this.gameModel = gameModel;
        this.gameStepListeners = new ArrayList<>();
        this.interactionLock = interactionLock;
        
        this.autoAdjust = true;
        this.sleepDelay = 0;
        this.iterationsPerEvent = 1;
        
        superSlowIntro = false;
        slowIntro = true;
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

    public void setAutoAdjust(boolean autoAdjust) {
        this.autoAdjust = autoAdjust;
    }

    public boolean getAutoAdjust() {
        return autoAdjust;
    }

    public void setSleepDelay(int sleepDelay) {
        this.sleepDelay = sleepDelay;
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

    private int getSleepDelay() {
        if (!autoAdjust) {
            return sleepDelay;
        }
        int sleep = 1;
       
        if (superSlowIntro) {
            if (getGameModel().getTime()<100 ) {
                sleep = 800 - (int) (Math.log10(getGameModel().getTime()/13.0+1)*800) ;
            }
            else {
                sleep = Math.max(1, 40-(int) Math.sqrt(getGameModel().getTime()/4));
            }
        }       

        if (slowIntro && !superSlowIntro) {                    
            if (getGameModel().getTime()<2000 ) {
                sleep = 10;
            }
            else if (getGameModel().getTime()<4000 ) {
                sleep = 8;
            }
        }
        return sleep;
        
    }

    public int getIterationsPerEvent() {
        if (!autoAdjust) {
            return this.iterationsPerEvent;
        }
        int autoIterations = 1;

        if (getGameModel().getEchosystem().getOrganisms().size() > 120) {
            autoIterations = 2;
        }
        if (getGameModel().getEchosystem().getOrganisms().size() > 180) {
            autoIterations = 4;
        }
        if (getGameModel().getEchosystem().getOrganisms().size() > 240) {
            autoIterations = 8;
        }
        return autoIterations;
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

