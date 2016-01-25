package com.sproutlife.model.echosystem;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import com.sproutlife.model.GameClock;
import com.sproutlife.model.seed.Seed;

public class Echosystem {
    HashSet<Organism> organisms ;
    HashSet<Organism> retiredOrganisms ;  
        
    Board board;      

    int defaultOrgLifespan; 
    int retirementTimeSpan;

    GameClock clock; 

    public int typeCount = 0;

    public Echosystem(GameClock clock) {
        this.organisms = new HashSet<Organism>();
        this.retiredOrganisms = new HashSet<Organism>();
        this.board = new Board();                
        this.clock = clock;

        //this.typeCount = 0;

        //Organism gaya = new Organism(0,getClock(),100,100, null, null);

        //this.organisms.add(gaya);

        this.defaultOrgLifespan = 30;
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

    public GameClock getClock() {
        return clock;
    }
    
    public int getTime() {
        return clock.getTime();
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
        this.setDefaultOrgLifespan(15);
        /*
        organisms = new HashSet<Organism>();
        Organism gaya = new Organism(0,getClock(),100,100, null, null);
        organisms.add(gaya);
        */
        

    }
    /*
    public Cell addCell(int x, int y) {
        Organism gaya;
        if (getOrganisms().size()==0) {
            gaya = new Organism(0,getClock(),100,100, null, null);
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
    */
    public boolean removeCell(Cell c) {
        return removeCell(c, true);
    }

    private boolean removeCell(Cell c, boolean updateBoard) {
        if (c==null) {
            return false;
        }
        
        boolean result = c.getOrganism().removeCell(c);

        if (updateBoard) {
            //We don't want to update the board if we just reset it.
            getBoard().removeCell(c);
        }

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

    public void setDefaultOrgLifespan(int orgLifespan) {
        this.defaultOrgLifespan = orgLifespan;
    }
   
    public int getDefaultOrgLifespan() {
        return defaultOrgLifespan;
    }
    
    public int getRetirementTimeSpan() {
        return retirementTimeSpan;
    }

    public void setRetirementTimeSpan(int retirementTimeSpan) {
        this.retirementTimeSpan = retirementTimeSpan;
    }

    public Organism createOrganism(int x,int y, Organism parent, Seed seed) {        
        typeCount++;
        //                                        
        Organism newOrg = new Organism(typeCount, getClock(), x, y, parent, seed);   
        if (parent==null) {
            newOrg.setLifespan(getDefaultOrgLifespan()+(new Random()).nextInt(3));
        }

        organisms.add(newOrg);
        return newOrg;

    }

    public void removeOrganism(Organism o) {
        for (Cell c: o.getCells()) {
            getBoard().removeCell(c);
        }
        organisms.remove(o);
    }

    public void retireOrganism(Organism o) {

        for (Cell c: o.getCells()) {

            getBoard().removeCell(c);
        }


        o.setTimeOfDeath(getTime());

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
            removeCell(r, false);
        }
        pruneEmptyOrganisms();
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
    
    public void pruneEmptyOrganisms() {     
        HashSet<Organism> pruneOrgs = new HashSet<Organism>();
        pruneOrgs.addAll(getOrganisms());
        for (Organism org : pruneOrgs) { 
        
            if (org.getCells().size()==0) {
                removeOrganism(org);
            }
        }        
    }

    public void setBoardSize(Dimension d) {

        getBoard().setBoardSize(d);
        updateBoard();
 
    }

/*
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
    */        

}
