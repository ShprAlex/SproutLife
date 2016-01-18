package com.sproutlife.model.step;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Mutation;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.seed.SeedFactory.SeedType;

public class GameStep extends Step {
    
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
    
         
    int lifespan = 16;
    
    public void perform() {       
        
       // initStats();
        
        getEchosystem().incrementClock();
        
        retireAndPruneStep.perform();        
        
        colorsStep.perform();

                
        if(getEchosystem().getOrganisms().size()<40) {
            if (getClock()%200==0) {
                lifespan+=1;
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
    
        
        getEchosystem().setOrgLifespan(lifespan);
        
        lifeStep.perform();
        
        mutationStep.perform();
 
        sproutStep.setSeedType(SeedType.Bentline1m_RPentomino);
        //Set child energy
        
        sproutStep.setSeedBorder(2);
        sproutStep.perform();
                 
        
        
        int lifeSum = 0;
        if (getClock()%100==0) {
            for (Organism o : getEchosystem().getOrganisms()) {
                lifeSum +=o.lifespan;
            }
            getStats().avgLife = lifeSum*10/ getEchosystem().getOrganisms().size();
        }

        printStats();                  

    }    

    
    private void printStats() {
        if (getClock()%100==0) {
        	//getStats().printMutations();
            getStats().printChildEnergy();

        }             
    }   


}
