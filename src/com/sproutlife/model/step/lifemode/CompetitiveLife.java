/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
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

    public int getCompare(Cell c) {
        Organism o = c.getOrganism();
        //int val = o.getAge()*o.getCells().size();
        int val = o.getAttributes().cellSum;//*2-o.getAttributes().getTerritorySize();
        //*o.getCells().size()*o.getAttributes().getTerritorySize();
        if (o.getParent()!=null && o.getParent().isAlive()) {
            //val+=o.getParent().getAttributes().cellSum;
            
            //    System.out.println(o.getAttributes().cellSum+" "+ o.getParent().getCells().size()*5);
            //}
            //val+=o.getParent().getAge();
            
            
        }
        return val;
    }
    
    int counter = 0;
    public void updateCells() {
        for (Organism o : getEchosystem().getOrganisms()) {
            o.getAttributes().cellSum += o.getCells().size();
        }
        
        ArrayList<Cell> bornCells = new ArrayList<Cell>(); 
        ArrayList<Cell> deadCells = new ArrayList<Cell>();   
                              
        for (int i=0; i<getBoard().getWidth(); i++) {            
            for (int j=0; j<getBoard().getHeight(); j++) {                                   

                Cell me = getBoard().getCell(i,j);
                Cell result = null;
                boolean wasBorn = false;

                if (getBoard().hasNeighbors(i,j)) {
                    ArrayList<Cell> neighbors = getBoard().getNeighbors(i,j);

                    result = getBorn(neighbors,i,j);

                    if (result!=null) {
                        if(me==null || me.getOrganism()!=result.getOrganism()) {
                            bornCells.add(result);
                            wasBorn=true;
                            getStats().born++;
                            if (me!=null) {
                                deadCells.add(me);
                            }
                        }
                    }
                }

                if (me!=null && !wasBorn){

                    ArrayList<Cell> neighbors = getBoard().getNeighbors(i,j);
                    result = keepAlive(me,neighbors,i,j);
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
    
    public Cell keepAlive(Cell me, ArrayList<Cell> neighbors, int i, int j) {        
        
        int friendCount = 0;

        for (Cell neighbor : neighbors) {            
            //if (me.getOrganism().isFamily(neighbor.getOrganism(),1)) {
            if (me.getOrganism() == neighbor.getOrganism()) {
                friendCount++;
            }
            else {
                if (neighbor.getOrganism().getParent()==me.getOrganism()) {
                    return null;
                }
                else if (neighbor.getOrganism()!=me.getOrganism() &&getCompare(me)<getCompare(neighbor)) {
                    me.getOrganism().getAttributes().collisionCount++;
                    return null;
                }
            }
        }

        if ((friendCount == 2 || friendCount==3)) {
            //if(getBoard().hasBiggerNeighbor25(i, j, me.getOrganism())) {
            //    return null;
            //}
            //for (Cell  neighbor : getBoard().getExtraNeighbors(i, j, 2)) {
            
            for (Cell neighbor : getBoard().getExtra12Neighbors(i, j)) {
                if (neighbor.getOrganism().getParent()==me.getOrganism()) {
                    return null;
                }
                else if (neighbor.getOrganism()!=me.getOrganism() && getCompare(me)<getCompare(neighbor)) {
                    me.getOrganism().getAttributes().collisionCount++;
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
        
        if (neighbors.size()!=3 ) {
            return null;
        }
        
        //Quick check to see if all neighbors are from the same organism
        Organism checkSingleOrg = neighbors.get(0).getOrganism();

        for (Cell cell : neighbors) {
            if (cell.getOrganism() != checkSingleOrg) {
                return null;
            }
        }

        Cell bornCell = getEchosystem().createCell(i,j,neighbors);
        //for (Cell  neighbor : getBoard().getExtraNeighbors(i, j, 2)) {

        for (Cell neighbor : getBoard().getExtra12Neighbors(i, j)) {
            if (neighbor.getOrganism().getParent()==checkSingleOrg) {
                return null;
            }
            else if (neighbor.getOrganism()!=bornCell.getOrganism() && getCompare(bornCell)<getCompare(neighbor)) {
                return null;
            }
        }
        
        return bornCell;
    }
}
