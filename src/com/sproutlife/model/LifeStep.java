package com.sproutlife.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.sproutlife.model.echosystem.Board;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.seed.SeedFactory.SeedType;


public class LifeStep extends Step {   
        
    public LifeStep(GameModel gameModel) {
        super(gameModel);                    
    }
    
    public void perform() {
        initStats();
        
        updateCells();
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
    
    private Cell keepAlive(Cell me, ArrayList<Cell> neighbors, int i, int j) {

        //int friendCount = 0;

        /*
        for (Cell neighbor : neighbors) {
            if (me.sameOrganism(neighbor) ) {
                friendCount++;
            }
        }                              
        */ 
        /*
        if(getEchosystem().getAge(me.getOrganism())>getEchosystem().getOrgLifespan(me.getOrganism())) {
            //getEchosystem().removeCell(me);
            return null;
        }
        */
        if (neighbors.size() == 2) { 
            me.age+=1;
            
            //return me;
            /*
            if (me.age!=25) {
                return me;
             }
             else {                
                 getStats().die1++;
                 return null;
             }
            */
            return me;
        }
        
        if (neighbors.size()==3) {
            /*
            if (neighbors.get(0).x +neighbors.get(1).x +neighbors.get(2).x==me.x*3 &&
                    neighbors.get(0).y +neighbors.get(1).y +neighbors.get(2).y==me.y*3) {
                //removeCell(me);
                return null;

            }
            */
            
            
            me.age+=1;
            /*
            if (me.age!=9 ) {
               return me;
            }
            else {                
                getStats().die1++;
                return null;
            }
            */
            return me;
            
        }         
        
        if(neighbors.size()<=1) {
            //getStats().die1++;
        }
        else {
            getStats().die2++;
        }
         
       // getEchosystem().removeCell(me);

        return null;
        
    }

    private Cell getBorn(ArrayList<Cell> neighbors, int i, int j) {
        if (i<0||i>getBoard().getWidth()-1||j<0||j>getBoard().getHeight()-1) {
            return null;
        }
        
        if (neighbors.size()<3) {
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

        //Do something more complicated for mixed neighbors 
 
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

            if (parentMap.get(org).size()<3) {
                //Ignore neighbors if less than 3 cells from the same organism               
                
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

        if(parentMap.size()==2) {
            Iterator <Organism >parentOrgsIterator = parentMap.keySet().iterator();
            Organism po1 = parentOrgsIterator.next();
            Organism po2 = parentOrgsIterator.next();
            Cell bornCell;
            Organism winner = chooseBornWinner(po1, po2);

            if (winner!=null) {
                bornCell = getEchosystem().createCell(i,j,parentMap.get(winner));            
                return bornCell;   
            }
        }

        return null;            

    }

    private Organism chooseBornWinner(Organism parentOrg1, Organism parentOrg2) {
       
        // if (parentOrg1.energy>parentOrg2.energy) {
        if (parentOrg1.size()>parentOrg2.size()) {
        //if (parentOrg1.size()<parentOrg2.size()) {
            return parentOrg1;              
        }
         
        //if (parentOrg2.energy>parentOrg1.energy) {
      if (parentOrg2.size()>parentOrg1.size()) {
        //if (parentOrg2.size()<parentOrg1.size()) {

            return parentOrg2;               
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
