package com.sproutlife.model.echosystem;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.sproutlife.model.seed.Seed;

public class Organism {
    int id;
    private Seed seed;
    Organism parent;
    ArrayList<Organism> children;
    Genetics genetics;
    private HashSet<Cell> cells;
    
    
    public int born;    
    public int timeOfDeath;
    public Point position;
    public int x;
    public int y;
    public int kind = 0;
    
    public int energy = 0;
    public int lifespan;
    
    public boolean bornFromInfected=false;
    public Organism infectedBy;
    public boolean infectorCanSprout=false;
    
    boolean alive = true;
    int collisionCount = 0;
        
    public Organism(int id, int born, int x, int y, Organism parent, Seed seed) {
        
        this.id = id;
        this.born = born;
        this.parent = parent;
        this.children = new ArrayList<Organism>();
        this.x = x;
        this.y = y;
        this.position = new Point(x,y);
        this.seed = seed;
        this.genetics = new Genetics();
        this.cells = new HashSet<Cell>();
  

        
        if (parent!=null) {
            if (parent.infectedBy!=null) {
                parent = parent.infectedBy;
                this.parent = parent;
                this.bornFromInfected = true;
            }
            
            parent.addChild(this);
            this.genetics = parent.getGenetics().clone();
            this.lifespan = parent.lifespan;
        }
        this.genetics.setSeed(this.seed);
        
        if (parent==null) {
            kind = (new Random()).nextInt(3); //kind = 0;
        }
        /*
        else if (id<=1000) {
            kind = (new Random()).nextInt(3); 
        }
        */
        else {
            kind = parent.getKind();
        }
        // TODO Auto-generated constructor stub
    }
    
    public int getId() {
        return id;
    }
    
    public Point getPosition() {
        return position;
    }
    
    public int getKind() {
        return kind;
    }    
    
    public Organism getParent() {
        return parent;
    }
    
    public void setParent(Organism parent) {
        this.parent = parent;
    }
    
    public ArrayList<Organism> getChildren() {
        return children;
    }
    
    public void addChild(Organism childOrg) {
        children.add(childOrg);
    }
    
    public void setSeed(Seed seed) {
        this.seed = seed;
        this.genetics.setSeed(seed);
    }
    
    public Seed getSeed() {
        return seed;
    }
    
    public Genetics getGenetics() {
        return genetics;
    }
    
    public ArrayList<Point> getMutationPoints(int time) {
        ArrayList<Point> adjustOffsets = 
                getGenetics().getMutationPoints(time);
        
        if (adjustOffsets==null) {
            return null;
        }
        for (Point p : adjustOffsets) {
            
            p.x += getPosition().x;
            p.y += getPosition().y;

        }
        return adjustOffsets;        
    } 
    
    public Set<Cell> getCells() {
        return cells;
    }
    
    public Cell addCell(int x, int y) {
        Cell c = new Cell(x, y, this);
        cells.add(c);
        return c;
    }
    
    /*
     * Add a cell that's been created
     */
    public void addCell(Cell c) {
        cells.add(c);
    }
    
    /*
     * Create cell but don't add it;
     */
    public Cell createCell(int x, int y, ArrayList<Cell> parents) {
        //Potentially check that parents are same type as organism;
        Cell c = new Cell(x, y, parents);
        //cells.add(c);
        return c;
    }
    
    public boolean containsCell(int x, int y) {
        for (Cell c: cells) {
            if (c.x ==x && c.y==y) {
                return true;
            }
        }
        return false;
    }
    
    public Cell getCell(int x, int y) {
        for (Cell c: cells) {
            if (c.x ==x && c.y==y) {
                return c;
            }
        }
        return null;
    }
    
   
    public boolean removeCell(Cell c) {        
        return cells.remove(c);            
    }
    
    public boolean removeCell(int x, int y) {
        Cell c = getCell(x,y);
        if (c!=null) {
            cells.remove(c);
            return true;
        }
        return false;           
    }
    
    public int size() {
        return cells.size();
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public int getTimeOfDeath() {
		return timeOfDeath;
	}
    
    public void setTimeOfDeath(int timeOfDeath) {
		this.timeOfDeath = timeOfDeath;
	}
    
    public boolean equals(Organism t) {
        // TODO Auto-generated method stub
        return this.id == t.id;
    }
    
}