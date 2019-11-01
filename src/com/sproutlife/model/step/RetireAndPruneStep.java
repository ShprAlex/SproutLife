/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Organism;

public class RetireAndPruneStep extends Step {
    GameModel gameModel;    
       
    public RetireAndPruneStep(GameModel gameModel) {
        super(gameModel);
    }
        
    public void perform() {
        retireOrganisms();
        pruneEmptyOrganisms();
        pruneRetiredOrganisms();
        pruneParentTree();
    }
    
    public void retireOrganisms() {
        List<Organism> organisms = new ArrayList<>(getOrganisms());
        for (Organism o : organisms) {
            if (o.getAge()>Math.min(o.lifespan,getSettings().getInt(Settings.MAX_LIFESPAN))) {
                getEchosystem().retireOrganism(o);
            }
        }
    }
    
    public void pruneRetiredOrganisms() {
        Deque<Organism> retiredOrgs = getEchosystem().getRetiredOrganisms();
        while (retiredOrgs.size()>0) {
            Organism org = retiredOrgs.peek();
            if (org.getTimeOfDeath()<getTime()- getEchosystem().getRetirementTimeSpan()) {
                retiredOrgs.poll();
            }
            else {
                break;
            }
        }
    }       
    
    public void pruneEmptyOrganisms() {
        getEchosystem().pruneEmptyOrganisms();       
    }
    
    public void pruneParentTree() {
        for (Organism o : getEchosystem().getOrganisms()) {
            while (o.getParent()!=null) {
                o=o.getParent();
                if (!o.isAlive() && (getTime()-o.getTimeOfDeath()>getEchosystem().getRetirementTimeSpan())) {
                    o.setParent(null);
                }
            }
            
        }
    }
    

}
