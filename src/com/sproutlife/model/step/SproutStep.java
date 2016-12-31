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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.seed.BitPattern;
import com.sproutlife.model.seed.Seed;
import com.sproutlife.model.seed.SeedFactory;
import com.sproutlife.model.seed.SeedFactory.SeedType;
import com.sproutlife.model.seed.SeedSproutPattern;

public class SproutStep extends Step {
    
    //LifeStep life;
    
    SeedType seedType;
    
    int seedBorder = 1;
    
    HashMap<Organism,ArrayList<Seed>> savedSeeds;
    
    public SproutStep(GameModel gameModel) {
        super(gameModel);       
    }
    
    public void setSeedType(SeedType seedType) {
        this.seedType = seedType;
    }
    
    public void setSeedType(String seedTypeString) {
        for (SeedType st : SeedType.values()) {
            if (st.toString().equals(seedTypeString)) {
                setSeedType(st);
                return;
            }
        }
    }
    
    public SeedType getSeedType() {
        return seedType;
    }    
    
    public void setSeedBorder(int seedBorder) {
        this.seedBorder = seedBorder;
    }
    
    public int getSeedBorder() {
        return seedBorder;
    }
        
    public int getChildEnergy(Organism org, int childNumber) {
    	switch (childNumber) {
    		case 1: return getSettings().getInt(Settings.CHILD_ONE_ENERGY);
    		case 2: return getSettings().getInt(Settings.CHILD_TWO_ENERGY);
    		case 3: return getSettings().getInt(Settings.CHILD_THREE_ENERGY);
    		default: return getSettings().getInt(Settings.CHILD_THREE_ENERGY);
    	}
    }      
    
    public void perform() {
        this.setSeedType(getSettings().getString(Settings.SEED_TYPE));
        
        for (Organism o : getEchosystem().getOrganisms()) {
            o.getAttributes().energy = o.getAttributes().energy +1;
        }
        
        if (getSettings().getBoolean(Settings.SPROUT_DELAYED_MODE)) {                        
            
            if (this.savedSeeds!=null) {
                sproutSeeds(this.savedSeeds);
            }
            
            savedSeeds = findSeeds();
            
        }
        else { 
            //simple way of doing things, makes it harder to display seeds;
            HashMap<Organism,ArrayList<Seed>> seeds = findSeeds();
            
            sproutSeeds(seeds);
        }
        
        if (getEchosystem().getOrganisms().size()<12) {
            sproutRandomSeed();
        }
    }
        
    private int seedDistSq(Seed s, Organism o) {
    	return (s.getSproutCenter().x-o.x)*(s.getSproutCenter().x-o.x)+(s.getSproutCenter().y-o.y)*(s.getSproutCenter().y-o.y);
    }
    
    private int innerProduct(Seed s1, Seed s2, Organism o) {
    	return (s1.getSproutCenter().x-o.x)*(s1.getSproutCenter().x-s2.getSproutCenter().x) - (s1.getSproutCenter().y-o.y)*(s1.getSproutCenter().y-s2.getSproutCenter().y);
    }
    
    private void sproutSeeds(HashMap<Organism,ArrayList<Seed>> seeds) {
        //Math.max(10,getEchosystem().getOrganisms().size()/10);        
        
        for (Organism o: seeds.keySet()) {
            if(!o.isAlive()) {
                continue;
            }
            ArrayList<Seed> seedList = seeds.get(o);
            if (seedList.size()>1) {
                final Organism fo = o;
                Collections.sort(seedList,new Comparator<Seed>() {
                    @Override
                    public int compare(Seed s1, Seed s2) {
                        
                        //int d1 = (s1.getPosition().x-fo.x)*(s1.getPosition().x-fo.x)+(s1.getPosition().y-fo.y)*(s1.getPosition().y-fo.y);
                        //int d2 = (s2.getPosition().x-fo.x)*(s2.getPosition().x-fo.x)+(s2.getPosition().y-fo.y)*(s2.getPosition().y-fo.y);
                        int d1 = seedDistSq(s1, fo);
                        int d2 = seedDistSq(s2, fo);
                        //Sprout the seeds closer first, there might not be energy for all seeds to sprout.
                        //Smaller organisms look better
                        if (d1==d2) {
                        	int ip = innerProduct(s1,s2,fo);
                        	//s1.getPosition().x-fo.x)*(s1.getPosition().x-s2.getPosition().x) - (s1.getPosition().y-fo.y)*(s1.getPosition().y-s2.getPosition().y);
                        	return ip;                     
                        }                        
                        return d1-d2;
                    }
                });
                //int q = 1;
                
                Seed s1 = seedList.get(0);
            	Seed s2 = seedList.get(1);
            	if (seedDistSq(s1, fo)==seedDistSq(s2, fo)) {
            		if (innerProduct(s1,s2,fo)==0) {
            			continue;
            		}
            	}
            }            

            for (Seed s : seedList) {
               Point seedOnPosition = s.getSeedOnPosition();
                
               Cell c = getBoard().getCell(seedOnPosition);
               
               if (c==null) {
                   //Should almost never happen, only if seeds overlapped.
                   //continue;
               }
               
               int childEnergy;
               if (o.getChildren()!=null) {
            	   childEnergy = getChildEnergy(o, o.getChildren().size()+1);
               }
               else {
            	   childEnergy = getChildEnergy(o, 1);
               }
               /*
               if (o.getKind()==0) {
                   Organism infector = o;
                   
                   for (int i =0;i<7;i++) {
                       if (infector.bornFromInfected) {
                           break;
                       }
                       else {
                           if (infector.getParent()!=null) {
                               infector= infector.getParent();
                           }
                       }
                   }
                   if (!infector.bornFromInfected && infector.getParent()!=null) { 
                       continue;
                   }
               }
               */
               
               //if (o.getParent()==null || Math.abs(Math.abs(o.getParent().x-s.getPosition().x)-Math.abs(o.getParent().y-s.getPosition().y))>4) {
                   if (o.getId()==0 || o.getAttributes().energy>=childEnergy) {
                       
                       sproutSeed(s, o);
                       
                       int childCount = o.getChildren().size()-1;

                       o.getAttributes().energy = 0;
                       
                       if(childCount>=0&&getTime()>100&&childCount<20) { //sproutSeed() above may have failed
                           getStats().childEnergy[childCount]+=o.getAge();
                           getStats().sproutNumber[childCount]++;
                       }
                   }
               //}
               
            }
        }
    }
            
    private HashMap<Organism,ArrayList<Seed>> findSeeds() {
        
        //Cell[][] gameBoard = board.getGameBoard();

        HashMap<Organism,ArrayList<Seed>> seeds = new HashMap<Organism,ArrayList<Seed>>();

        for (Organism o : getEchosystem().getOrganisms()) {
           for (Cell c : o.getCells()) {                              

                Seed s = checkAndMarkSeed(c);                              

                if (s!=null) {
                    
                    ArrayList<Seed> seedList = seeds.get(o);
                    if (seedList == null) {
                        seedList = new  ArrayList<Seed>();
                        seeds.put(o,seedList);                        
                    }
                    seedList.add(s);                    
                }                                         
            }
        }

        return seeds;

    }          

    private Seed checkAndMarkSeed(Cell topLeftCell) {
        int border = getSeedBorder();                

        for (Seed s : SeedFactory.getSeedRotations(getSeedType())) {
            
            Point seedOnBit = s.getSeedOnBit();
            
            int x = topLeftCell.x-seedOnBit.x;
            int y = topLeftCell.y-seedOnBit.y;
            
            if (x<0||y<0) {
                continue;
            }
            
            s.setPosition(x, y);                      
            s.setSeedBorder(border);
            s.setParentPosition(topLeftCell.getOrganism().getLocation());
            
            if(checkAndMarkSeed(s)) {          
                return s;
            }
        }
        return null;

    }
    

    private void sproutRandomSeed() {
        int x = (new Random()).nextInt(getBoard().getWidth());
        int y = (new Random()).nextInt(getBoard().getHeight());
        int border = getSeedBorder();                
 
        List<Seed> seedRotations = SeedFactory.getSeedRotations(getSeedType());
        Seed s = seedRotations.get((new Random()).nextInt(seedRotations.size()));
       
        SeedSproutPattern pattern = s.getSeedSproutPattern();
        final int seedWidth = pattern.getSeedPattern().getWidth();
        final int seedHeight = pattern.getSeedPattern().getWidth();
        final BitPattern currentSproutPattern = pattern.getSproutPattern();
        SeedSproutPattern newPattern = new SeedSproutPattern() {                
            {
                this.seedPattern = new BitPattern(new int[seedWidth][seedHeight]);

                this.sproutPattern = currentSproutPattern;

                this.sproutOffset = new Point(0,0);
            }        
        };  
        s.setPosition(x, y);       
        s.setParentPosition(new Point(120,120));
        Seed randomSeed = new Seed(newPattern, s.getRotation());
        
        randomSeed.setPosition(x, y);                      
        randomSeed.setSeedBorder(border);
        randomSeed.setParentPosition(new Point(x+1,y+1));
        
        sproutSeed(randomSeed,null);
        
        
    }
    
    public boolean checkAndMarkSeed(Seed seed) {

        ArrayList<Cell> seedCells = new ArrayList<Cell>();        
        Organism seedOrg = null;
        int i = seed.getPosition().x;
        int j = seed.getPosition().y;
        int seedWidth = seed.getSeedWidth();
        int seedHeight = seed.getSeedHeight();
        int border = seed.getSeedBorder();


        //Check seed bounds
        if( i+seedWidth>=getBoard().getWidth() || j+seedHeight>=getBoard().getHeight()) {            
            return false;
        }

        //Check seed;
        for (int si=0;si<seedWidth;si++) {
            for (int sj=0;sj<seedHeight;sj++) {                              
                Cell c = getBoard().getCell(i+si,j+sj);                
                if (seed.getSeedBit(si,sj)) {

                    if (c==null) {
                        return false;                        
                    }
                    //else
                    if (seedOrg==null) {
                        seedOrg = c.getOrganism();
                    }
                    if (!c.getOrganism().equals(seedOrg)) {
                        return false;
                    }
                    seedCells.add(c);                                                                
                }
                else {
                    if (c!=null) {
                        return false;
                    }
                }
            }            
        }

        //Check border
        for (int si=-border;si<seedWidth+border;si++) { 
            for (int sj=-border;sj<seedHeight+border;sj++) {                       
                if(si<=-1 || sj<=-1 || si>=(seedWidth) || sj>=seedHeight) {                    
                    if(i+si>=0 && j+sj>=0 && 
                            i+si<=getBoard().getWidth()-1 &&  
                            j+sj<=getBoard().getHeight()-1) {
                        if (getBoard().getCell(i+si,j+sj)!=null) {
                            return false;
                        }
                    }
                }
            }
        }
        for (Cell c: seedCells) {
            c.setMarkedAsSeed(true);
        }
        return true;
    }    
    
    public void sproutSeed(Seed seed, Organism seedOrg) {  

        Point sproutPosition = seed.getSproutPosition();
        Point sproutCenter = seed.getSproutCenter();
        
        int seedX = seed.getPosition().x;
        int seedY = seed.getPosition().y;
        
        int sproutX = sproutPosition.x;
        int sproutY = sproutPosition.y;

        int newOrgX = sproutCenter.x;
        int newOrgY = sproutCenter.y;
        
        int seedWidth = seed.getSeedWidth();
        int seedHeight = seed.getSeedHeight();
 
        int sproutWidth = seed.getSproutWidth();
        int sproutHeight = seed.getSproutHeight();
                
        if (seedX < 0 || seedY < 0
                || seedX + seedWidth > getBoard().getWidth() - 1
                || seedY + seedHeight > getBoard().getHeight() - 1
                || sproutX + sproutWidth > getBoard().getWidth() - 1
                || sproutY + sproutHeight > getBoard().getHeight() - 1
                || sproutX < 0 || sproutY < 0) {
            
            //Clear the seed flag from cells before returning null
            if (seedOrg!=null) {
                for (Cell c : seedOrg.getCells()) {
                    c.setMarkedAsSeed(false);
                }
            }
            return;
        }  
    
        Organism newOrg = getEchosystem().createOrganism(newOrgX, newOrgY, seedOrg, seed);
        
        //Remove old seed
        for (int x=0;x<seedWidth;x++) {
            for (int y=0; y<seedHeight;y++) {
                if (seed.getSeedBit(x, y)) {
                    Cell rc = getBoard().getCell(seedX+x, seedY+y);

                    getEchosystem().removeCell(rc);
                    //getBoard().clearCell(seedX+x,seedY+y);              
                }
            }
        }

        for (int si = 0;si<sproutWidth;si++) {
            for (int sj = 0;sj<sproutHeight;sj++) {

                int i = sproutX+si;
                int j = sproutY+sj;
                
                Cell c = getBoard().getCell(i, j);
                
                if (c!=null) {   
                    //Only happens when border improperly configured 
                    //and/or successively sprouted seeds overlap each other
                    getEchosystem().removeCell(c);
                    //getBoard().clearCell(i, j);
                }
                
                if (seed.getSproutBit(si, sj)) {
                    
                    Cell newC = getEchosystem().addCell(i,j,newOrg);                     

                }
                else {
                    //Should be ok
                }
            }
        }
    }  

}
