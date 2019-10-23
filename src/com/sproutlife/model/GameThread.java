/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.SwingUtilities;

import com.sproutlife.model.step.GameStep.StepType;
import com.sproutlife.model.step.GameStepEvent;
import com.sproutlife.model.step.GameStepListener;

public class GameThread {
    private boolean playGame = false;
    
    Thread innerThread;
    
    GameStepListener gameStepListener;
    
    GameModel gameModel;
    ReentrantReadWriteLock interactionLock;
    
    int sleepDelay;
    int iterations;
    
    boolean superSlowIntro;
    boolean slowIntro;
    boolean autoAdjust;
    
    public GameThread(GameModel gameModel, ReentrantReadWriteLock interactionLock) {     
        this.gameModel = gameModel;
        this.interactionLock = interactionLock;
        
        this.autoAdjust = true;
        this.sleepDelay = 0;
        this.iterations = 1;
        
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
    
    public boolean getPlayGame() {
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
    
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    /*
     * Only expecting one gameStepListener for now, therefore a "set" method
     */
    public void setGameStepListener(GameStepListener gameStepListener) {
        this.gameStepListener = gameStepListener;
    }
    
    private void fireStepBundlePerformed() {
        if (gameStepListener!=null) {
            GameStepEvent event = new GameStepEvent(StepType.STEP_BUNDLE);
            gameStepListener.stepPerformed(event);
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

    
    public int getIterations() {
        if (!autoAdjust) {
            return this.iterations;
        }
        int iterations = 1;
        
        if(getGameModel().getEchosystem().getOrganisms().size()>120) {
            iterations =2;
        }
        
        if(getGameModel().getEchosystem().getOrganisms().size()>180) {
            iterations =4;
        }

        if (getGameModel().getEchosystem().getOrganisms().size()>240) {
            iterations = 8;
        }
                
        return iterations;
    }
    
    private class InnerGameThread extends Thread {
        public void run() {

            while (playGame) {

                try {

                    //synchronized (getGameModel().getEchosystem()) {
                        interactionLock.writeLock().lock();
                        getGameModel().performGameStep();
                        interactionLock.writeLock().unlock();
                        
                    //}
                    
                    int iterations = getIterations();

                    if (getGameModel().getTime() % iterations == 0) {

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

