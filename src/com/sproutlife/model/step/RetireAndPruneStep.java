/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step;

import java.util.HashSet;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Organism;

public class RetireAndPruneStep extends Step {
    GameModel gameModel;    
       
    public RetireAndPruneStep(GameModel gameModel) {
        super(gameModel);
    }
        
    public void perform() {
        //pruneInitialSpinners(); 
        retireOrganisms() ;
        pruneRetiredOrganisms();
        pruneEmptyOrganisms();
        pruneParentTree();
    }
    
    /*
     * Prune early organisms that only go in circles without reproducing
     */
    private void pruneInitialSpinners() {
        if (getTime()>10000) {
            return;
        }
        HashSet<Organism> pruneOrgs = new HashSet<Organism>();
        pruneOrgs.addAll(getEchosystem().getOrganisms());
        for (Organism org : pruneOrgs) {
            Organism parent = org.getParent();
            boolean hasCousins = false;
            for (int i=0;i<15 && parent!=null;i++) {
                if (parent.getChildren().size()>1) {
                    hasCousins = true;
                }
                parent = parent.getParent();
            }
            if (!hasCousins && parent!=null) {
                getEchosystem().removeOrganism(org);
            }
        }

    }
    
    public void retireOrganisms() {
        HashSet<Organism> retireOrgs = new HashSet<Organism>();
        retireOrgs.addAll(getOrganisms());
        for (Organism o : retireOrgs) {
            if (o.getAge()>Math.min(o.lifespan,getSettings().getInt(Settings.MAX_LIFESPAN))) {
                getEchosystem().retireOrganism(o);
            }
        }
    }
    
    public void pruneRetiredOrganisms() {
        HashSet<Organism> pruneOrgs = new HashSet<Organism>();
        pruneOrgs.addAll(getEchosystem().getRetiredOrganisms());
        for (Organism org : pruneOrgs) {
            if (org.getTimeOfDeath()<getTime()- getEchosystem().getRetirementTimeSpan()) {
                getEchosystem().removeRetired(org);
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
                if (!o.isAlive() && (getTime()-o.getTimeOfDeath()>1000)) {
                    o.setParent(null);
                }
            }
            
        }
    }
    

}
