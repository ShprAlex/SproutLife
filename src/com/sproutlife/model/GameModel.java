package com.sproutlife.model;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.sproutlife.Settings;
import com.sproutlife.model.echosystem.Board;
import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.step.GameStep;
import com.sproutlife.model.step.GameStepListener;

public class GameModel {

    Echosystem echosystem;  

    GameClock clock;

    GameStep gameStep;

    GameThread gameThread;

    Settings settings;

    Stats stats;   

    public GameModel(Settings settings, ReentrantReadWriteLock interactionLock) {
        this.settings = settings;
        this.clock = new GameClock();
        echosystem = new Echosystem(clock);
        gameStep = new GameStep(this);       
        gameThread = new GameThread(this, interactionLock);
        stats = new Stats(this);
    }

    public void performGameStep() {
        incrementTime();
        gameStep.perform();
    }
    /*
    public GameStep getGameStep() {
        return gameStep;
    }
    */
    public Echosystem getEchosystem() {
        return echosystem;
    }

    public Board getBoard() {
        return echosystem.getBoard();
    }          

    public int getTime() {
        return clock.getTime();
    }

    public GameClock getClock() {
        return clock;
    }

    private void incrementTime() {
        clock.increment();
    }

    public Stats getStats() {
        return stats;
    }   

    public Settings getSettings() {
        return settings;
    }
    

    public void setPlayGame(boolean playGame) {
        if (playGame) {          
            gameThread.setPlayGame(true);

        } else {
            gameThread.setPlayGame(false);            
        }
    }    

    public void setGameStepListener(GameStepListener l) {
        if (gameThread!=null) {
            gameThread.setGameStepListener(l);
            gameStep.setGameStepListener(l);
        }
    }

    public void set(String s, Object o) {
        getSettings().set(s,o);
    }

}
