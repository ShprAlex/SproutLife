package com.sproutlife.model;

import com.sproutlife.model.step.GameStep;
import com.sproutlife.model.step.GameStep.StepType;
import com.sproutlife.model.step.GameStepEvent;
import com.sproutlife.model.step.GameStepListener;

public class GameThread {
    boolean playGame = false;
    
    Thread innerThread;
    
    GameStepListener gameStepListener;
    
    GameModel gameModel;
    
    public GameThread(GameModel gameModel) {     
        this.gameModel = gameModel;
        // TODO Auto-generated constructor stub
    }

    private GameModel getGameModel() {
        return gameModel;
    }
    
    private GameStep getGameStep() {
        return getGameModel().getGameStep();
    }
    
    public void setPlayGame(boolean playGame) {
        this.playGame = playGame;
        if (playGame) {
            new InnerGameThread().start();
        }
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
 
    private class InnerGameThread extends Thread {
        public void run() {

            while(playGame) {

                try {
                    //Thread.sleep(1000/i_movesPerSecond);
                    //
                    synchronized (getGameModel().getEchosystem()) {
                        getGameStep().perform();
                    }

                    int sleep = 1;

                    boolean superSlowIntro = false;

                    if (superSlowIntro) {
                        if (getGameModel().getTime()<100 ) {
                            sleep = 800 - (int) (Math.log10(getGameModel().getTime()/13.0+1)*800) ;
                        }
                        else {
                            sleep = Math.max(1, 40-(int) Math.sqrt(getGameModel().getTime()/4));
                        }
                    }
                    boolean slowIntro = true;


                    if (slowIntro && !superSlowIntro) {                    
                        if (getGameModel().getTime()<2000 ) {
                            sleep = 10;
                        }
                        else if (getGameModel().getTime()<4000 ) {
                            sleep = 8;
                        }
                    }
                    int iterations = 1;

                    if(getGameModel().getEchosystem().getOrganisms().size()>120) {
                        iterations =2;
                    }
                    if (getGameModel().getEchosystem().getOrganisms().size()>180) {
                        iterations = 4;
                    }
                    if (getGameModel().getEchosystem().getOrganisms().size()>240) {
                        iterations = 8;
                    }
                    //sleep = 1;
                    //iterations = 32;
                    if (getGameModel().getTime()%iterations==0) {
                        fireStepBundlePerformed();                    
                        Thread.sleep(sleep);
                    }
                } catch (InterruptedException ex) {}
            }
        }
    }
}

