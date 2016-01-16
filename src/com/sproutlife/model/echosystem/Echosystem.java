package com.sproutlife.model.echosystem;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.sproutlife.model.seed.Seed;

public class Echosystem {
    HashMap<Integer, Organism> organisms ; 
    
    Board board;
    
    //Organism gaya;
    
    public int typeCount = 0;
    
    public Echosystem() {
        board = new Board();
        typeCount = 0;
        Organism gaya = new Organism(0,0,100,100, null, null);
        organisms = new HashMap<Integer,Organism>();
        organisms.put(typeCount, gaya);
        
    }  
    
    public Collection<Organism> getOrganisms() {
        return organisms.values();
    }

    public Board getBoard() {
        return board;
    };
    
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
        organisms = new HashMap<Integer,Organism>();
        Organism gaya = new Organism(0,0,100,100, null, null);
        organisms.put(0, gaya);
        
    }
    public Cell addCell(int x, int y) {
        Organism gaya;
        if (getOrganisms().size()==0) {
            gaya = new Organism(0,0,100,100, null, null);
            organisms.put(0, gaya);
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
    
    public Organism createOranism(int clock,int newOrgX,int newOrgY, Organism parent, Seed seed) {        
        typeCount++;
        //                                        
        Organism newOrg = new Organism(typeCount, clock,newOrgX,newOrgY, parent, seed);        
        
        organisms.put(typeCount,newOrg);
        return newOrg;

    }
    
    public void pruneOrganisms() {
        Iterator<Integer> oi = organisms.keySet().iterator();
        while(oi.hasNext()) {
            if (organisms.get(oi.next()).getCells().size()==0) {
                oi.remove();
            }
        }
        
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
