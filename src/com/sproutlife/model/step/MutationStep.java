/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step;

import java.awt.Point;
import java.awt.Color;
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
    
    /*
     * getCreateMutationCount
     * We typically add around 1 mutation at each time step of the game
     * For a high mutation rate, and a large number of organisms, we add several mutations
     * For a low mutation rate and a low number of organisms, we randomly add 0 or 1 mutations
     */
    int getCreateMutationCount() {
        int invMutationRate;
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
            default: return 1;
        }

        // lots of organisms
        if(getEchosystem().getOrganisms().size()>=invMutationRate) {
            return getEchosystem().getOrganisms().size()/invMutationRate;
        }
        // very few organism
        else if (getEchosystem().getOrganisms().size()<invMutationRate/3 ) {
            return random.nextInt(10)==0 ? 1:0;
        }
        // some organisms
        else if (getEchosystem().getOrganisms().size()<invMutationRate ) {
            return random.nextInt(3)==0 ? 1:0;
        }

        return 1;
    }

    /*
     * mutateLifespan  returns true if lifespan was mutated.
     *
     */
    private boolean mutateLifespan(Organism org) {
        int rand3 = random.nextInt(3);

        if (rand3 == 0 ) {
            if(org.lifespan<getSettings().getInt(Settings.MAX_LIFESPAN)) {
                // increase lifespan
                org.lifespan +=1;
                return true;
            }
        }
        else if (rand3 == 1) {
            // decrease lifespan
            org.lifespan -=1;
            return true;
        }

        // else if rand3 == 2, do nothing
        return false;
    }

    private boolean mutateColor(Organism org) {
        int COLOR_MUTATION_RATE = 32;
        int VISIBLE_BIAS = 16;
        Color col = org.getColor();

        switch (random.nextInt(9)) {
            case 0:
                org.setColor(new Color(Math.max(col.getRed()-COLOR_MUTATION_RATE, VISIBLE_BIAS), col.getGreen(), col.getBlue()));
                return true;
            case 1:
                org.setColor(new Color(Math.min(col.getRed()+COLOR_MUTATION_RATE, 255-VISIBLE_BIAS), col.getGreen(), col.getBlue()));
                return true;
            case 2:
                org.setColor(new Color(col.getRed(), Math.max(col.getGreen()-COLOR_MUTATION_RATE, VISIBLE_BIAS), col.getBlue()));
                return true;
            case 3:
                org.setColor(new Color(col.getRed(), Math.min(col.getGreen()+COLOR_MUTATION_RATE, 255-VISIBLE_BIAS), col.getBlue()));
                return true;
            case 4:
                org.setColor(new Color(col.getRed(), col.getGreen(), Math.max(col.getBlue()-COLOR_MUTATION_RATE, VISIBLE_BIAS)));
                return true;
            case 5:
                org.setColor(new Color(col.getRed(), col.getGreen(), Math.min(col.getBlue()+COLOR_MUTATION_RATE, 255-VISIBLE_BIAS)));
                return true;
        }
        return false;
    }

    private void addMutation(Cell c) {
        Organism org = c.getOrganism();
        Genome g = org.getGenome();
        int age = org.getAge();
        int x = c.x - org.x;
        int y = c.y - org.y;

        Mutation m = org.addMutation(x, y);

        for (Organism childOrg : org.getChildren()) {
            //Pretend the parent had this mutation when giving birth to it's children
            //It's ok because the children are not yet old enough to have
            //encountered this mutation
            childOrg.getGenome().addMutation(m);
        }
    }

    /*
     * removeMutation  remove a random mutation at the organisms current age
     */
    private boolean removeMutation(Organism org) {
        Genome g = org.getGenome();
        int age = org.getAge();

        if(g.getMutationCount(age)==0) {
            // if the organism doesn't have any mutations at the age it is now,
            // do nothing
            return false;
        }

        int indexAtAge = random.nextInt(g.getMutationCount(age));
        Mutation removeM = g.getMutation(age, indexAtAge);
        g.removeMutation(removeM);
        for (Organism childOrg : org.getChildren()) {
            //Pretend the parent didn't have this mutation when giving birth to it's children
            //It's ok because the children are not yet old enough to have
            //encountered this mutation
            childOrg.getGenome().removeMutation(removeM);
        }

        return true;
    }

    private void addNewMutations() {
        ArrayList<Cell> cellsFromAllOrganisms = getEchosystem().getCells();
        if (cellsFromAllOrganisms.size()==0) {
            return;
        }

        int createMutationCount = getCreateMutationCount();

        for (int repeat=0; repeat<createMutationCount; repeat++) {
            
            int size = cellsFromAllOrganisms.size();
            int mutationIndex = random.nextInt(size);

            Cell randomCell = cellsFromAllOrganisms.get(mutationIndex);
            
            boolean mutatedColor = mutateColor(randomCell.getOrganism());

            boolean mutatedLifespan = mutateLifespan(randomCell.getOrganism());
            if (mutatedLifespan && random.nextInt(2)==0) {
                // if we mutated the lifespan, 50% chance we're done mutating stuff this time
                continue;
            }
            
            // we're more likely to remove a mutation than to add one
            boolean mutationIsAdd = (random.nextInt(7)>=4);

            if (mutationIsAdd){
                addMutation(randomCell);
                // existing mutations remove cells, so do it for this new mutation too
                getEchosystem().removeCell(randomCell);
                
            }
            else {
                removeMutation(randomCell.getOrganism());
            }
        }

    }

}
