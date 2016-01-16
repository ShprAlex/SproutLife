package com.sproutlife.model.echosystem;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import sun.security.action.GetLongAction;

import com.sproutlife.model.seed.Seed;

public class Echosystem {
	HashSet<Organism> organisms ;
    HashSet<Organism> retiredOrganisms ;       
    
    Board board;
    
    int orgLifespan; //Organism lifespan;
    int retirementTimeSpan;
    
    int clock; 
    
    //Organism gaya;
    
    public int typeCount = 0;
    
    public Echosystem() {
    	this.organisms = new HashSet<Organism>();
    	this.retiredOrganisms = new HashSet<Organism>();
    	this.board = new Board();                
    	this.clock = 0;
    	
        this.typeCount = 0;
        
        Organism gaya = new Organism(0,0,100,100, null, null);
        
        this.organisms.add(gaya);
        
        this.orgLifespan = 30;
        this.retirementTimeSpan = 10;
        
    }  
    
    public Collection<Organism> getOrganisms() {
        return organisms;
    }
    
    public Collection<Organism> getRetiredOrganisms() {
        return retiredOrganisms;
    }
    
    public Board getBoard() {
        return board;
    };
    
    public int getClock() {
        return clock;
    }

    public void incrementClock() {
        clock++;
    }
    
    public void resetClock() {
        this.clock = 0;
    }         
    
    public ArrayList<Cell> getCells() {
        ArrayList<Cell> cellList = new ArrayList<Cell>();
        for (Organism o : getOrganisms()) {
            cellList.addAll(o.getCells());            
        }
        //System.out.println("Cellcount "+cellList.size());
        return cellList;        
    }    
    
    
    public void resetCells() {
        getBoard().resetBoard();
        organisms = new HashSet<Organism>();
        Organism gaya = new Organism(0,0,100,100, null, null);
        organisms.add(gaya);
        
    }
    public Cell addCell(int x, int y) {
        Organism gaya;
        if (getOrganisms().size()==0) {
            gaya = new Organism(0,0,100,100, null, null);
            organisms.add(gaya);
        }
        else {
            gaya = getOrganisms().iterator().next();
        }
        if (!gaya.containsCell(x,y)) {
            Cell c  = gaya.addCell(x, y);
            getBoard().setCell(c);
            return c;
            //cellList.add(c);
        } 
        return null;
    }   
    
    public boolean removeCell(Cell c) {
        boolean result = c.getOrganism().removeCell(c);
        
        getBoard().removeCell(c);
        
        return result;
    }
    
    //public boolean liftBarrier = false;
    
    public Cell addCell(int x, int y, Organism org) {
        //if (x==getBoard().getWidth()/2 && !liftBarrier) {
        //    return null;
        //}
        Cell c = org.addCell(x, y);
        if (c!=null) {
            getBoard().setCell(c);
        }
        return c;
    }
    
    /*
     * Add a cell that's been created but not yet added
     */
    public void addCell(Cell c) {
        c.getOrganism().addCell(c);
        getBoard().setCell(c);
    }
    
    /*
     * Create a cell but don't add it
     */
    public Cell createCell(int x, int y, ArrayList<Cell> parents) {
        //if (x==getBoard().getWidth()/2 && !liftBarrier) {
        //    return null;
        //}
        return parents.get(0).getOrganism().createCell(x, y, parents);
    }
    
    public void removeCell(int x, int y) {
        
        Cell c = getBoard().getCell(x, y);
        if (c!=null) {
            removeCell(c);
            //getBoard().removeCell(c);
            //cellList.remove(c);
        }        
    }
    
    public void setOrgLifespan(int orgLifeSpan) {
        this.orgLifespan = orgLifespan;
    }
    
    public int getOrgLifespan(Organism org) {
        if (org.lifespan==0) {
            org.lifespan=this.orgLifespan;
        }
        if (org.getKind()==0 ) {
            //return org.lifespan + 50;
        }
        return org.lifespan;//-org.x/100;
        //return orgLifeSpan; 
    }
    
    public int getAge(Organism org) {
        return getClock()-org.born; 
    }
    
    public int getRetirementTimeSpan() {
		return retirementTimeSpan;
	}
    
    public void setRetirementTimeSpan(int retirementTimeSpan) {
		this.retirementTimeSpan = retirementTimeSpan;
	}
    
    public Organism createOranism(int clock,int newOrgX,int newOrgY, Organism parent, Seed seed) {        
        typeCount++;
        //                                        
        Organism newOrg = new Organism(typeCount, clock,newOrgX,newOrgY, parent, seed);        
        
        organisms.add(newOrg);
        return newOrg;

    }
    
    public void removeOrganism(Organism o) {
    	organisms.remove(o);
    }
    
    public void retireOrganism(Organism o) {
    	for (Cell c: o.getCells()) {
    		getBoard().removeCell(c);
    	}
    	o.setTimeOfDeath(getClock());
    	organisms.remove(o);
    	retiredOrganisms.add(o);
    }           
    
    public void removeRetired(Organism o) {
    	retiredOrganisms.remove(o);
    }
    
    
    public void updateBoard() {

       getBoard().resetBoard();

       ArrayList<Cell> removeList = new ArrayList<Cell>(0);

        for (Organism o : getOrganisms()) {
            for (Cell current : o.getCells()) {
                if (current.x<0 || current.x >= getBoard().getWidth() || current.y <0 || current.y >= getBoard().getHeight()) {
                    removeList.add(current);
                }
                else {
                    getBoard().setCell(current);
                    //gameBoard[current.x+1][current.y+1] = current;
                }
            }            
        }
        for (Cell r : removeList) {
            removeCell(r);
        }        
    } 
    
    protected boolean validateBoard() {
        for (Organism o : getOrganisms()) {
            for (Cell current : o.getCells()) {

                if (getBoard().getCell(current.x,current.y)!= current) {
                    return false;                     
                }
            }            
        }        
        for (int i=0; i<getBoard().getWidth(); i++) {
            for (int j=0; j<getBoard().getHeight(); j++) {
                Cell c = getBoard().getCell(i,j);
                if (c!=null && c.getOrganism().getCell(c.x, c.y)!=c) {
                    return false;
                }

            }
        }
        
        return true;
    } 
    
    public void setBoardSize(Dimension d) {
        getBoard().setBoardSize(d);
        
        
        ArrayList<Cell> removeList = new ArrayList<Cell>(0);
        for (Cell current : getCells()) {
            if ((current.x > getBoard().getWidth()-1) || (current.y > getBoard().getHeight()-1)) {
                removeList.add(current);
            }
        }
        for (Cell r : removeList) {
            removeCell(r);
        }
        //cellList.removeAll(removeList);
    }
    
    
    public void randomlyFillBoard(int percent) {
        for (int i=0; i<getBoard().getWidth(); i++) {
            for (int j=0; j<getBoard().getHeight(); j++) {
                if (Math.random()*100 < percent) {
                    addCell(i,j);
                    //addCell(i+1,j+1);
                    //addCell(i+1,j);
                    //addCell(i,j+1);
                }
            }
        }
        
        for (int i=1; i<getBoard().getWidth()-2; i++) {         
            if(i%5==0) {
                addCell(i,100);
            }
        }
        for (int j=1; j<getBoard().getHeight()-2; j++) {
            if(j%5==0) {
                addCell(100,j);
            }
        }
    }       

    
}
