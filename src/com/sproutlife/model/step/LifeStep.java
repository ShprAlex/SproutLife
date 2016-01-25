package com.sproutlife.model.step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class LifeStep extends Step { 
    
    public static enum LifeMode { 
        FRIENDLY, 
        COMPETITIVE 
    }
    LifeMode lifeMode;
        
    public LifeStep(GameModel gameModel) {
        super(gameModel);   
        lifeMode = LifeMode.FRIENDLY;
    }
    
    public void perform() {
        initStats();
        updateLifeMode();
        updateCells();                
    }
    
    public LifeMode getLifeMode() {
        return lifeMode;
    }
    
    private void updateLifeMode() {
        if("friendly".equals(getSettings().getString(Settings.LIFE_MODE))) {
            this.lifeMode = LifeMode.FRIENDLY;
        }
        else {
            this.lifeMode = LifeMode.COMPETITIVE;
        }
    }
    
    public void updateCells() {
        
        
                
        ArrayList<Cell> bornCells = new ArrayList<Cell>(); 
        ArrayList<Cell> deadCells = new ArrayList<Cell>();         
        
        for (int i=0; i<getBoard().getWidth(); i++) {
            
            for (int j=0; j<getBoard().getHeight(); j++) {                                   
                
                Cell me = getBoard().getCell(i,j);

                if (me!=null) {
                    ArrayList<Cell> neighbors = getBoard().getNeighbors(i,j);
                    Cell result = keepAlive(me,neighbors,i,j);
                    if (result!=null) {
                        getStats().stayed++;
                    }
                    else {
                        deadCells.add(me);
                    }

                } 
                else {                        
                    if (getBoard().hasNeighbors(i,j)) {
                        ArrayList<Cell> neighbors = getBoard().getNeighbors(i,j);

                        Cell result = getBorn(neighbors,i,j);

                        if (result!=null) {
                            bornCells.add(result);
                            getStats().born++;
                        }

                    }
                }           
            }
        }
        
        for (Cell c: bornCells) {
            getEchosystem().addCell(c);
            //getBoard().setCell(c);            
        }
        for (Cell c: deadCells) {
            getEchosystem().removeCell(c);
        }
    }     
    
    public int getCompare(Organism o) {
        return o.size();
    }
    
    private Cell keepAlive(Cell me, ArrayList<Cell> neighbors, int i, int j) {

        int friendCount = 0;

        
        if (getLifeMode()==null) {
            for (Cell neighbor : neighbors) {            
                if (me.getOrganism()==neighbor.getOrganism()) {
                    friendCount++;
                }                
            }
            if ((neighbors.size() == 2 || neighbors.size()==3)) { 
                me.age+=1;                
                return me;
                         
            }
        }
        else if (getLifeMode()==LifeMode.FRIENDLY) {
            for (Cell neighbor : neighbors) {            
                if (me.getOrganism()==neighbor.getOrganism()) {
                    friendCount++;
                }                
            }
            if (friendCount>=1&&(neighbors.size() == 2 || neighbors.size()==3)) { 
                me.age+=1;                
                return me;
                         
            }
        }
        else if (getLifeMode()==LifeMode.COMPETITIVE) {
            for (Cell neighbor : neighbors) {            
                if (me.getOrganism().isFamily(neighbor.getOrganism(),2)) {
                    friendCount++;
                }
                else {
                    if (getCompare(me.getOrganism())<getCompare(neighbor.getOrganism())) {
                        return null;
                    }
                }
            }
            


            if ((friendCount == 2 || friendCount==3)) {
                //if(getBoard().hasBiggerNeighbor25(i, j, me.getOrganism())) {
                //    return null;
                //}
                me.age+=1;                
                return me;

            }
        }
         
/*
        if (neighbors.size() == 2) { 
            me.age+=1;
            
            return me;
        }
        
        if (neighbors.size()==3) {
            
            me.age+=1;

            return me;
            
        }         
        
        if(neighbors.size()<=1) {
            getStats().die1++;
        }
        else {
            getStats().die2++;
        }        
*/
        return null;
        
    }

    private Cell getBorn(ArrayList<Cell> neighbors, int i, int j) {
        if (i<0||i>getBoard().getWidth()-1||j<0||j>getBoard().getHeight()-1) {
            return null;
        }
        
        if (neighbors.size()<3 || neighbors.size()>6) {
            return null;
        }

        //Quick check to see if all neighbors are from the same organism
        Organism checkSingleOrg = neighbors.get(0).getOrganism();
        boolean singleOrg = true;
        for (Cell cell : neighbors) {
            if (cell.getOrganism() != checkSingleOrg) {
                singleOrg = false;
                break;
            }
        }
        if (singleOrg) {
            if (neighbors.size()==3) {
                Cell bornCell = getEchosystem().createCell(i,j,neighbors);
                return bornCell;
            }
            else {
                return null;
            }
        }
        if (getLifeMode()==LifeMode.FRIENDLY) {
            return null;
        }

        /*
         * getLifeMode()==LifeMode.COMPETITIVE
         * Do something more complicated for > 4 mixed neighbors 
         */               
        
        Organism biggestOrg = null;
        boolean tieForBiggest = false;
        for (Cell c : neighbors) {
            Organism o = c.getOrganism();
            if (biggestOrg==null ||getCompare(biggestOrg)>getCompare(o)) {
                biggestOrg = o;
                tieForBiggest=false;
            }
            else {
                if (getCompare(o)==getCompare(biggestOrg)) {
                    tieForBiggest=true;
                }
            }
        }
        if (tieForBiggest) {
            return null;
        }
        ArrayList<Cell> biggestOrgCells = new ArrayList<Cell>();
        for (Cell c : neighbors) {
            if (c.getOrganism()==biggestOrg) {
                biggestOrgCells.add(c);
            }
        }
        if (biggestOrgCells.size()==3) {
            Cell bornCell = getEchosystem().createCell(i,j,biggestOrgCells);
            return bornCell;
        }
        
        
        /*
                 
        HashMap <Organism, ArrayList<Cell>> parentMap = new HashMap<Organism, ArrayList<Cell>>();

        //Group parent cells by organism
        for (Cell cell : neighbors) {
            Organism org = cell.getOrganism();
            ArrayList<Cell> parentList = parentMap.get(org);

            if (parentList == null) {
                parentList = new ArrayList<Cell>(8);
                parentMap.put(org, parentList);            
            }

            parentList.add(cell);
        }

        Iterator <Organism> iterator = parentMap.keySet().iterator();
        while (iterator.hasNext()) {
            Organism org = iterator.next();

            if (parentMap.get(org).size()<=3) {
                iterator.remove(); 
            }            
            else if (parentMap.get(org).size()>3) {
                //If there are 4 neighbor cells from the same Org, it's too crowded to be born
                return null; 
            }
        }
        
        if(parentMap.size()==1) {
            Cell bornCell = getEchosystem().createCell(i,j,parentMap.values().iterator().next());

            return bornCell;          
        }
        if (getLifeMode()==LifeMode.COMPETITIVE) {
            if(parentMap.size()==2) {
                Iterator <Organism >parentOrgsIterator = parentMap.keySet().iterator();
                Organism po1 = parentOrgsIterator.next();
                Organism po2 = parentOrgsIterator.next();
                Cell bornCell;
                Organism winner = chooseWinner(po1, po2);
    
                if (winner!=null) {
                    bornCell = getEchosystem().createCell(i,j,parentMap.get(winner));            
                    return bornCell;   
                }
            }
        }
        */
        return null;            

    }

    private Organism chooseWinner(Organism org1, Organism org2) {
       
        // if (parentOrg1.energy>parentOrg2.energy) {
        if (org1.size()>org2.size()) {
        //if (parentOrg1.size()<parentOrg2.size()) {
            return org1;              
        }
         
        //if (parentOrg2.energy>parentOrg1.energy) {
      if (org2.size()>org1.size()) {
        //if (parentOrg2.size()<parentOrg1.size()) {

            return org2;               
        }
        
        return null; 
    } 
    
    
    private void initStats() {                
        getStats().born =0;
        getStats().die1 =0;
        getStats().die2 =0;
        getStats().stayed = 0;
        getStats().gridSize = getBoard().getWidth()*getBoard().getHeight();    
    }
    /*  
    private void printStats() {
        if (getClock()%100==0) {
            getStats().printBDS();

        }             
    }
    */    
}
