package com.sproutlife.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.sproutlife.model.echosystem.Mutation;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.seed.SeedFactory.SeedType;

public class GameStep extends Step {
    
    LifeStep lifeStep;
    
    SproutStep sproutStep;
    
    MutationStep mutationStep;
    
    RetireAndPruneStep retireAndPruneStep;
       
    public GameStep(GameModel gameModel) {
        super(gameModel);
             
        lifeStep = new LifeStep(gameModel);
        sproutStep = new SproutStep(gameModel);
        mutationStep = new MutationStep(gameModel);
        retireAndPruneStep = new RetireAndPruneStep(gameModel);
        
        //Mutation m = new Mutation(new Point(1,-2),5);
        //gameModel.getEchosystem().getOrganisms().iterator().next().getGenetics().addMutation(m);
    }
    
         
    int lifespan = 16;
    
    public void perform() {       
        
       // initStats();
        
        getEchosystem().incrementClock();
        
        retireAndPruneStep.perform();        
        
        splitColors();

                
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
        
        
        
        sproutStep.setSeedType(SeedType.Bentline1_RPentomino);
        sproutStep.setSproutEnergy(15);
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

    private void splitColors() {
        if (getEchosystem().getOrganisms().size() ==0) {
            return;
        }
        int kindCount[] = new int[3];
        for (Organism o : getEchosystem().getOrganisms()) {
            kindCount[o.getKind()]++;
            /*
            if (o.getKind()!=oneKind) {
                isOneKind=false;
            } 
            */           
        }

        if (kindCount[0]==0 || kindCount[1]==0 || kindCount[2]==0) {     
            int splitKind = -1;
            int emptyKind = -1;
            
            for (int i=0;i<3;i++) {
                if (kindCount[i]==0) {
                    emptyKind = i;
                }
                if (kindCount[i]*100/getEchosystem().getOrganisms().size()>85) {
                    splitKind = i;                
                }                
            }              
            
            if (splitKind!=-1) {
                ArrayList<Integer> xCoords = new ArrayList<Integer>();
                for (Organism o : getEchosystem().getOrganisms()) {                    
                    if (o.kind==splitKind) {
                        xCoords.add(o.x);
                    }                                        
                }
                Collections.sort( xCoords);
                
                int middleX = 0;
                if (xCoords.size()>1) {                       
                    middleX = xCoords.get(xCoords.size()/2);
                }
                for (Organism o : getEchosystem().getOrganisms()) {
                    if (o.kind==splitKind) {
                        
                        o.kind = o.x<middleX?splitKind : emptyKind ;
                    }
                }
            }        
        }
    }
    
    private void printStats() {
        if (getClock()%100==0) {
            getStats().printChildEnergy();

        }             
    }   


}
