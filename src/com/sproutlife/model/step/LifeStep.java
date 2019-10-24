/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step;

import java.util.ArrayList;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.step.lifemode.CompetitiveLife;
import com.sproutlife.model.step.lifemode.CooperativeLife;
import com.sproutlife.model.step.lifemode.FriendlyLife;
import com.sproutlife.model.step.lifemode.LifeMode;

public class LifeStep extends Step {     
  
    LifeMode cooperativeLife;
    LifeMode friendlyLife;
    LifeMode competitiveLife;
    LifeMode lifeMode;
                      
    public LifeStep(GameModel gameModel) {
        super(gameModel); 
        cooperativeLife = new CooperativeLife(gameModel);
        friendlyLife = new FriendlyLife(gameModel);
        competitiveLife = new CompetitiveLife(gameModel);
        
        lifeMode = friendlyLife;
    }
    
    public void perform() {
        initStats();
        updateLifeMode();

        for (Organism o : getEchosystem().getOrganisms()) {
            o.getAttributes().cellSum += o.getCells().size();
        }

        if (lifeMode == competitiveLife) {
            ((CompetitiveLife) competitiveLife).updateCells();
        }
        else {
            updateCells();
        }
    }
    
    public LifeMode getLifeMode() {
        return lifeMode;
    }
    
    private void updateLifeMode() {
        if("friendly".equals(getSettings().getString(Settings.LIFE_MODE))) {
            this.lifeMode = friendlyLife;
        }
        else if("cooperative".equals(getSettings().getString(Settings.LIFE_MODE))) {
            this.lifeMode = cooperativeLife;
        }
        else {
            // we use competitiveLife for both competitive1 and competitive2
            this.lifeMode = competitiveLife;
        }
    }
    
    public void updateCells() {        
  
        ArrayList<Cell> bornCells = new ArrayList<Cell>(); 
        ArrayList<Cell> deadCells = new ArrayList<Cell>();  

        
        for (int x=0; x<getBoard().getWidth(); x++) {
            for (int y=0; y<getBoard().getHeight(); y++) {
                
                Cell me = getBoard().getCell(x,y);
                
                if (me == null) {
                    if (getBoard().hasNeighbors(x, y)) {
                        ArrayList<Cell> neighbors = getBoard().getNeighbors(x, y);

                        Cell result = getLifeMode().getBorn(neighbors, x, y);

                        if (result != null) {

                            bornCells.add(result);
                            getStats().born++;
                        }
                    }
                }
                else {                    
                    ArrayList<Cell> neighbors = getBoard().getNeighbors(x,y);
                    Cell result = getLifeMode().keepAlive(me,neighbors,x,y);
                    if (result!=null) {
                        getStats().stayed++;                       
                    }
                    else {
                        deadCells.add(me);
                    }

                }           
            }
        }
        
        //Remove cells before adding cells to avoid Organism having duplicate cells,
        //Orgs don't do Contains checks for speed
        for (Cell c: deadCells) {
            getEchosystem().removeCell(c);
            
        }
       
        for (Cell c: bornCells) {
            getEchosystem().addCell(c);
        }

    }     
    
    private void initStats() {
        getStats().born =0;
        getStats().die1 =0;
        getStats().die2 =0;
        getStats().stayed = 0;
        getStats().gridSize = getBoard().getWidth()*getBoard().getHeight();    
    }
    
}
