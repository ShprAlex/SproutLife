/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Genome;
import com.sproutlife.model.echosystem.Mutation;
import com.sproutlife.model.echosystem.Organism;

public class MutationStep extends Step {
    GameModel gameModel;    
    Random random;
    
    public MutationStep(GameModel gameModel) {
        super(gameModel);
        random = new Random();
    }
    
    public void perform() {
        doExistingMutations();
        if (getGameModel().getSettings().getBoolean(Settings.MUTATION_ENABLED)) {
            addNewMutations();
        }
    } 
    
    private void doExistingMutations() {

        for (Organism o : getEchosystem().getOrganisms()) {
           
           int age = o.getAge();
                      
           ArrayList<Point> mutationPoints =  o.getMutationPoints(age);
           
           if (mutationPoints == null) {
               continue;
           }
           
           for (int pi =0; pi<mutationPoints.size();pi++) {
               Point p = mutationPoints.get(pi);
           
               getStats().mutationCount++;
               
               Cell c = getBoard().getCell(p);
               if (c!=null) {
                   if (c.getOrganism()==o) {
                       getEchosystem().removeCell(c);
                       c.getOrganism().removeFromTerritory(c);
                   }
                   else {
                       getStats().mutationMiss++;
                   }
               }        
               else {
                   getStats().mutationMiss++;
               }
               
           }
        }        
    }
    
    private void addNewMutations() {
        ArrayList<Cell> allCells = getEchosystem().getCells();
        if (allCells.size()==0) {
            return;
        }

        //int size = Math.max(2000, allCells.size());
        int repeatTimes = 1;
        int invMutationRate = 160;
        switch (getSettings().getInt(Settings.MUTATION_RATE)) {
                        
            case 1: invMutationRate = 1215; break;
            case 2: invMutationRate = 810; break;            
            case 3: invMutationRate = 540; break;
            case 4: invMutationRate = 360; break;
            case 5: invMutationRate = 240; break;
            case 6: invMutationRate = 160; break;
            case 7: invMutationRate = 100; break;
            case 8: invMutationRate = 70; break;
            case 9: invMutationRate = 50; break;            
            case 10: invMutationRate = 30; break;
            
            default: return; 
            
        }
        
        if(getEchosystem().getOrganisms().size()>invMutationRate) {
            repeatTimes = getEchosystem().getOrganisms().size()/invMutationRate;
        }
        if (getEchosystem().getOrganisms().size()<invMutationRate/3 ) {
            repeatTimes = random.nextInt(10)==0 ? 1:0;
        }
        else if (getEchosystem().getOrganisms().size()<invMutationRate ) {
            repeatTimes = random.nextInt(3)==0 ? 1:0;
        }
        
        for (int repeat=0;repeat<repeatTimes;repeat++) {
            
            int size = allCells.size();
            int mutationIndex = (new Random()).nextInt(size); 
            if (mutationIndex>=allCells.size()) {
                return;
            }
           
            Cell c = allCells.get(mutationIndex);
            Organism org = c.getOrganism();           
            
            int rand6 = random.nextInt(6);
            int decreaseOdds = 3;
            
            //if("friendly".equals(getSettings().getString(Settings.LIFE_MODE))) {
            //    decreaseOdds = 4; //50% odds of decreasing vs. 33% odds of increasing    
            //}
            
            if (rand6 <=1 && org.lifespan<getSettings().getInt(Settings.MAX_LIFESPAN)) {
                
                org.lifespan +=1;
                if (random.nextInt(2)==1) {
                    continue;
                }
            }
            else if (rand6<=decreaseOdds) {
                //2,3 33%
                org.lifespan -=1;
                if (random.nextInt(2)==1) {
                    continue;
                }
            }
            else {
              
                //do nothing
            }
            
            /*
            if (org.lifespan>160) {
                org.lifespan=160;
            }
            */
            
            
            if (org.getId()!=0) {            
                Genome g = org.getGenome();
                int age = org.getAge();
                int x = c.x - org.x;
                int y = c.y - org.y;
                
                if (age<25) {
                    //continue;
                }
                
                boolean mutationIsAdd = (random.nextInt(7)>=4);//&& (g.getMutationCount(age)==0||age>=30);
                /*
                int mutationSum = 0;
                for (int t=0;t<org.getLifespan();t++) {
                    mutationSum+=g.getMutationCount(t);
                }
                */
                if (mutationIsAdd){// &&  mutationSum<Math.sqrt(getTime()/2000.0)) {
                    //if(Math.abs(x)+Math.abs(y)+Math.abs(age)>=8) {
                        /*
                        while (g.getMutationCount(age)>5) {
                            int removeIndex = (new Random()).nextInt(g.getMutationCount(age));
                            
                            g.removeMutation(g.getMutation(age, removeIndex));                        
                        } 
                        */                   
                        Mutation m = org.addMutation(x, y);                       
                        
                        for (Organism childOrg : org.getChildren()) {
                            //Ok because, children are not yet old enough to have 
                            //encountered this mutation
                            
                            childOrg.getGenome().addMutation(m);                            
                        }
                        //experiment
                        if (org.getParent()!=null && random.nextInt(3)==0) {
                            org.getParent().getGenome().addMutation(m);
                        }
            
                        getEchosystem().removeCell(c);
                        //getBoard().removeCell(c);            
                    //}
                }
                else { //remove random mutation
                    if(g.getMutationCount(age)>0) {

                        int removeIndex = random.nextInt(g.getMutationCount(age));
                        Mutation removeM = g.getMutation(age, removeIndex);
                        g.removeMutation(removeM);
                        for (Organism childOrg : org.getChildren()) {
                            //Ok because, children are not yet old enough to have 
                            //encountered this mutation
                            //if (childOrg.getGenome().getMutationCount(age)<10) {
                                
                            childOrg.getGenome().removeMutation(removeM);
                            //}
                        }
                    }
                }
            }
        }

    }

}
