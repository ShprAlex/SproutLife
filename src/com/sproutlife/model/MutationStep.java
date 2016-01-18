package com.sproutlife.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.sproutlife.Settings;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Genetics;
import com.sproutlife.model.echosystem.Mutation;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.seed.Seed;
import com.sproutlife.model.seed.SeedFactory;
import com.sproutlife.model.seed.SeedFactory.SeedType;

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
        HashMap<Mutation, Integer> popularMutations = new HashMap<Mutation, Integer>();
        
        Mutation frequentMutation = null;
        int maxFreq = 0;
        
        //getStats().mutationCount=0;
        //getStats().mutationMiss=0;
                        
        for (Organism o : getEchosystem().getOrganisms()) {
           
           int age = getAge(o);
                      
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
                       //getBoard().removeCell(c);
                       Mutation m = o.getGenetics().getMutation(age, pi);
                       Integer freq = popularMutations.get(m);
                       if (freq==null) {
                           freq = 0;
                       }
                       else {
                           freq = freq;
                       }
                       popularMutations.put(m, freq+1);
                       if (freq+1>maxFreq) {
                           maxFreq = freq+1;
                           frequentMutation = m;
                       }
                       
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
        if (getStats().freqMuteFreq < maxFreq) {
            getStats().freqMutation = frequentMutation;
            getStats().freqMuteFreq = maxFreq;
        }
    }
    
    private void addNewMutations() {
        ArrayList<Cell> allCells = getEchosystem().getCells();
        if (allCells.size()==0) {
            return;
        }
        
       

        //int size = Math.max(2000, allCells.size());
        int repeatTimes = 1;
        if(getEchosystem().getOrganisms().size()>120) {
            repeatTimes = getEchosystem().getOrganisms().size()/120;
        }
        if (getEchosystem().getOrganisms().size()<120 ) {
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
            
            if (org.infectedBy!=null) {
                continue;
            }
            
            int rand6 = random.nextInt(6);
            if (rand6 <1) {
                //do nothing;
            }
            else if (rand6<=3) {
                //1,2,3 50%
                org.lifespan -=1;
                if (random.nextInt(2)==1) {
                    continue;
                }
            }
            else {
                //4,5 33%
                org.lifespan +=1;
                if (random.nextInt(2)==1) {
                    continue;
                }
            }
            
            /*
            if (org.lifespan>160) {
                org.lifespan=160;
            }
            */
            
            
            if (c.getOrganism().getId()!=0) {            
                Genetics g = org.getGenetics();
                int age = getAge(org);
                int x = c.x - org.x;
                int y = c.y - org.y;
                
                boolean mutationIsAdd = (random.nextInt(3)>0);
                
                if (mutationIsAdd) {
                    //if(Math.abs(x)+Math.abs(y)+Math.abs(age)>=8) {
                        /*
                        while (g.getMutationCount(age)>5) {
                            int removeIndex = (new Random()).nextInt(g.getMutationCount(age));
                            
                            g.removeMutation(g.getMutation(age, removeIndex));                        
                        } 
                        */                   
                        Mutation m = g.addMutation(x, y, age, getClock());                       
                        
                        for (Organism childOrg : org.getChildren()) {
                            //Ok because, children are not yet old enough to have 
                            //encountered this mutation
                            
                            childOrg.getGenetics().addMutation(m);                            
                        }
                        //experiment
                        if (org.getParent()!=null && random.nextInt(3)==0) {
                            org.getParent().getGenetics().addMutation(m);
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
                            //if (childOrg.getGenetics().getMutationCount(age)<10) {
                                
                            childOrg.getGenetics().removeMutation(removeM);
                            //}
                        }
                    }
                }
            }
        }

    }

}
