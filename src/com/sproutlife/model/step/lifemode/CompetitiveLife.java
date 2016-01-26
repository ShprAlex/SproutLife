package com.sproutlife.model.step.lifemode;

import java.util.ArrayList;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class CompetitiveLife extends LifeMode {
    
    public CompetitiveLife(GameModel gameModel) {
        super(gameModel);
        // TODO Auto-generated constructor stub
    }
    
    public int getCompare(Organism o) {
        int val = o.maxCells;
        if (o.getParent()!=null) {
            val = Math.max(val,o.getParent().maxCells);
            o = o.getParent();
        }
        if (o.getParent()!=null) {
            val = Math.max(val,o.getParent().maxCells);
            o = o.getParent();
        }
        return val;
    }
    
    public Cell keepAlive(Cell me, ArrayList<Cell> neighbors, int i, int j) {        
        
        int friendCount = 0;

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
            for (Cell neighbor : getBoard().getExtra4Neighbors(i, j)) {            
                if (!me.getOrganism().isFamily(neighbor.getOrganism(),2) && 
                        getCompare(me.getOrganism())<getCompare(neighbor.getOrganism())) {
                    return null;

                }
            }
            me.age+=1;                
            return me;

        }



        return null;
        
    }

    public Cell getBorn(ArrayList<Cell> neighbors, int i, int j) {
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

     
        
        Organism biggestOrg = null;
        boolean tieForBiggest = false;
        for (Cell c : neighbors) {
            Organism o = c.getOrganism();
            if (biggestOrg==null ||getCompare(biggestOrg)>getCompare(o)) {
                biggestOrg = o;
                tieForBiggest=false;
            }
            else {
                if (o!=biggestOrg && getCompare(o)==getCompare(biggestOrg)) {
                    tieForBiggest=true;
                }
            }
        }
        for (Cell c : getBoard().getExtra4Neighbors(i, j)) { 
            Organism o = c.getOrganism();
            if (biggestOrg==null ||getCompare(biggestOrg)>getCompare(o)) {
                biggestOrg = o;
                tieForBiggest=false;
            }
            else {
                if (o!=biggestOrg && getCompare(o)==getCompare(biggestOrg)) {
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
        
        
        return null;            

    }
}
