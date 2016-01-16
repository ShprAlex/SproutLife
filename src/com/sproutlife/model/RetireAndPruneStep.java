package com.sproutlife.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.seed.BitPattern;
import com.sproutlife.model.seed.Seed;
import com.sproutlife.model.seed.SeedFactory;
import com.sproutlife.model.seed.SymmetricSeed;
import com.sproutlife.model.seed.SeedFactory.SeedType;
import com.sproutlife.model.seed.SeedSproutPattern;

public class RetireAndPruneStep extends Step {
    GameModel gameModel;
    
    int orgLifeSpan;
       
    public RetireAndPruneStep(GameModel gameModel) {
        super(gameModel);
    }
        
    public void perform() {
    	
    	retireOrganisms() ;    	
    	pruneRetiredOrganisms();    	
    	pruneEmptyOrganisms();
    	pruneParentTree();
    }
    
    private void retireOrganisms() {
    	HashSet<Organism> retireOrgs = new HashSet<Organism>();
    	retireOrgs.addAll(getOrganisms());
        for (Organism o : retireOrgs) {
        	if (getAge(o)>getEchosystem().getOrgLifespan(o)) {
        		getEchosystem().retireOrganism(o);
        	}
        }
    }
    
    public void pruneRetiredOrganisms() {
    	HashSet<Organism> pruneOrgs = new HashSet<Organism>();
    	pruneOrgs.addAll(getOrganisms());
    	for (Organism org : pruneOrgs) {    	
            if (getAge(org)>getEchosystem().getOrgLifespan(org)+getEchosystem().getRetirementTimeSpan()) {
                getEchosystem().removeRetired(org);
            }
        }
    }       
    
    public void pruneEmptyOrganisms() {
    	HashSet<Organism> pruneOrgs = new HashSet<Organism>();
    	pruneOrgs.addAll(getOrganisms());
    	for (Organism org : pruneOrgs) { 
        
            if (org.getCells().size()==0) {
            	getEchosystem().removeOrganism(org);
            }
        }
        
    }
    
    public void pruneParentTree() {
        for (Organism o : getEchosystem().getOrganisms()) {
            while (o.getParent()!=null) {
                o=o.getParent();
                if (getAge(o)>1000) {
                    o.setParent(null);
                }
            }
            
        }
    }
    

}
