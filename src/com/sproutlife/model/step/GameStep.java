package com.sproutlife.model.step;

import java.util.Random;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.seed.SeedFactory.SeedType;

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
    RetireAndPruneStep retireAndPruneStep;
    ColorsStep colorsStep;
       
    public GameStep(GameModel gameModel) {
        super(gameModel);        
                             
        lifeStep = new LifeStep(gameModel);
        sproutStep = new SproutStep(gameModel);
        mutationStep = new MutationStep(gameModel);
        retireAndPruneStep = new RetireAndPruneStep(gameModel);
        colorsStep = new ColorsStep(gameModel);
        
        //Mutation m = new Mutation(new Point(1,-2),5);
        //gameModel.getEchosystem().getOrganisms().iterator().next().getGenetics().addMutation(m);
    }
            
    
    public void perform() {       
        
       // initStats();       
        
        retireAndPruneStep.perform();   
        fireStepPerformed(StepType.PRUNE_STEP);
        
        colorsStep.perform();
        fireStepPerformed(StepType.COLOR_STEP);
                
        
        if(getEchosystem().getOrganisms().size()<40) {
            if (getTime()%200==0) {
                int lifespan = Math.max(16,getEchosystem().getDefaultOrgLifespan());                          
                getEchosystem().setDefaultOrgLifespan(lifespan+1);
            }            
        }
        /*
        if ((getClock()%5000)<1000) {
            getEchosystem().liftBarrier =true;
        }
        else {
            getEchosystem().liftBarrier =false;
        }
        */                   
        
        lifeStep.perform();
        fireStepPerformed(StepType.LIFE_STEP);
        
        mutationStep.perform();
        fireStepPerformed(StepType.MUTATION_STEP);
 

        sproutStep.setSeedType(SeedType.Bentline1m_RPentomino);        
        sproutStep.setSeedBorder(1);
        sproutStep.perform();       
        fireStepPerformed(StepType.SPROUT_STEP);

        printStats();  
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
    
    private void printStats() {        
        
        int lifeSum = 0;
        if (getTime()%100==0) {
            for (Organism o : getEchosystem().getOrganisms()) {
                lifeSum +=o.lifespan;
            }
            getStats().avgLife = lifeSum*10/ getEchosystem().getOrganisms().size();
        }
        
        if (getTime()%100==0) {
        	//getStats().printMutations();
            //getStats().printChildEnergy();

        }             
    }   


}
