package com.sproutlife.model.echosystem;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.sproutlife.model.GameClock;
import com.sproutlife.model.seed.Seed;

public class Organism {
    int id;
    private Seed seed;
    Organism parent;
    ArrayList<Organism> children;
    Genome genome;
    private HashSet<Cell> cells;
    // territory is used to track all visited points
    private HashSet<Point> territory; 
    public GameClock clock;
    
    
    public int born;    
    public int timeOfDeath;
    public Point position;
    public int x;
    public int y;
    public int kind = 0;
    
    public int energy = 0;
    public int lifespan;
    public int maxCells;
    public int paab;
    
    public boolean bornFromInfected=false;
    public Organism infectedBy;
    public boolean infectorCanSprout=false;
    
    boolean alive = true;
    int collisionCount = 0;
    int territoryLength = 0;
        
    public Organism(int id, GameClock clock, int x, int y, Organism parent, Seed seed) {
        
        this.id = id;
        this.clock = clock;
        this.born = clock.getTime();
        this.parent = parent;
        this.children = new ArrayList<Organism>();
        this.x = x;
        this.y = y;
        this.position = new Point(x,y);
        this.seed = seed;
        this.genome = new Genome();
        this.cells = new HashSet<Cell>();
        this.territory = new HashSet<Point>();
        this.timeOfDeath = -1;  
        this.maxCells = 0;
        
        if (parent!=null) {
            if (parent.infectedBy!=null) {
                parent = parent.infectedBy;
                this.parent = parent;
                this.bornFromInfected = true;
            }
            this.paab = parent.getAge();
            parent.addChild(this);
            this.genome = parent.getGenome().clone();
            this.lifespan = parent.lifespan;
        }
        this.genome.setSeed(this.seed);
        
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
        this.genome.setSeed(seed);
    }
    
    public Seed getSeed() {
        return seed;
    }
    
    public GameClock getClock() {
        return clock;
    }
    
    public int getLifespan() {
        return lifespan;
    }
    
    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }
    
    public int getAge() {
        return getClock().getTime() - this.born;
    }
    
    public Genome getGenome() {
        return genome;
    }
    
    public Mutation addMutation(int x, int y) {      
        return getGenome().addMutation(x, y, getAge(), clock.getTime());        
    }   
    
    public ArrayList<Point> getMutationPoints(int time) {
        ArrayList<Point> adjustOffsets = 
                getGenome().getMutationPoints(time);
        
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
        addCell(c);
        return c;
    }
    
    /*
     * Add a cell that's been created
     */
    public void addCell(Cell c) {
        cells.add(c);
        territory.add(c);
        int dist = (c.x-this.x)*(c.x-this.x)+(c.y-this.y)*(c.y-this.y);
        if (territoryLength<dist) {
            territoryLength = dist;
        }
        this.maxCells = Math.max(size(), maxCells);
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
            return removeCell(c);
        }
        return false;           
    }
    
    public int size() {
        return cells.size();
    }
    
    public int getTerritorySize() {
        return territory.size();
    }
    
    public int getTerritoryLength() {
        return territoryLength;
    }
    
    public boolean removeFromTerritory(Cell c) {
        boolean result = territory.remove(c);
        if(!result) {
            int x=4;
            System.out.println("Remove from territory failed?");
        }
        return result;
    }
    
    public boolean isAlive() {
        return alive && !(getTimeOfDeath()>0);
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
    
    private HashSet<Organism> getAncestorsAndMe(Organism o, int dist) {
        HashSet<Organism> ancestors = new HashSet<Organism>();        
        for (int d=0;d<=dist;d++) {
            if (o!=null) {
                ancestors.add(o);
                o = o.getParent();
            }
            else {
                break;
            }
        }
        return ancestors;
    }
    
    public boolean isFamily(Organism o2, int dist) {
        
        HashSet<Organism> myAncestors = getAncestorsAndMe(this, dist);
        HashSet<Organism> otherAncestors = getAncestorsAndMe(o2, dist);
        for (Organism o :myAncestors) {
            if (otherAncestors.contains(o)) {
                return true;
            }
        }
        return false;        
    }
    
}