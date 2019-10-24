/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step;

import com.sproutlife.model.GameModel;

public class GameStep extends Step {
    
    public static enum StepType { 
        GAME_STEP, 
        LIFE_STEP, 
        SPROUT_STEP, 
        MUTATION_STEP, 
        PRUNE_STEP,
        COLOR_STEP,
        STEP_BUNDLE
    }
    
    GameStepListener gameStepListener;
    
    LifeStep lifeStep;
    SproutStep sproutStep;
    MutationStep mutationStep;
    PreReproductionStep preReproductionStep;
    RetireAndPruneStep retireAndPruneStep;
    ColorsStep colorsStep;
       
    public GameStep(GameModel gameModel) {
        super(gameModel);        
                             
        lifeStep = new LifeStep(gameModel);
        sproutStep = new SproutStep(gameModel);
        mutationStep = new MutationStep(gameModel);
        preReproductionStep = new PreReproductionStep(gameModel);
        retireAndPruneStep = new RetireAndPruneStep(gameModel);
        colorsStep = new ColorsStep(gameModel);
    }
            
    
    public void perform() {       
        retireAndPruneStep.perform();   
        fireStepPerformed(StepType.PRUNE_STEP);
        
        colorsStep.perform();
        fireStepPerformed(StepType.COLOR_STEP);
        
        lifeStep.perform();
        fireStepPerformed(StepType.LIFE_STEP);
        
        mutationStep.perform();
        fireStepPerformed(StepType.MUTATION_STEP);

        preReproductionStep.perform();
        //pre-reproduction sprouts random seeds, so we can bundle it with the sproutStep event

        sproutStep.perform();       
        fireStepPerformed(StepType.SPROUT_STEP);

        updateStats();  
        fireStepPerformed(StepType.GAME_STEP);
    }

    /*
     * Only expecting one gameStepListener for now, therefore a "set" method
     */
    public void setGameStepListener(GameStepListener gameStepListener) {
        this.gameStepListener = gameStepListener;
    }
    
    private void fireStepPerformed(StepType stepType) {
        if (gameStepListener!=null) {
            GameStepEvent event = new GameStepEvent(stepType);
            gameStepListener.stepPerformed(event);
        }
    }
    
    private void updateStats() {        
        getStats().updateSmoothedPopulation();
        if (getTime()%100==0) {
            getStats().update();
        }             
    }   


}
