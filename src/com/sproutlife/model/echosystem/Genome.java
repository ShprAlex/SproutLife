package com.sproutlife.model.echosystem;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import com.sproutlife.model.seed.Seed;

public class Genome {
    
    HashMap <Integer, ArrayList<Mutation>> mutations;    
    Seed seed;
    public Genome() {
        mutations = new HashMap<Integer, ArrayList<Mutation>>();

    }
    
    public Genome(HashMap <Integer, ArrayList<Mutation>> mutations) {
        this.mutations = new HashMap<Integer, ArrayList<Mutation>>();
        for (Integer i : mutations.keySet()) {
            ArrayList<Mutation> cloneList 
                = new ArrayList<Mutation>(mutations.get(i));
            this.mutations.put(i, cloneList);
        }
        //this.mutations = 
        //        new HashMap<Integer, ArrayList<Mutation>>(mutations);
    }
    
    public Genome clone() {        
        return new Genome(this.mutations);        
    }
    
    public void setSeed(Seed seed) {
        this.seed = seed;
    }
    public Seed getSeed() {
        return seed;
    }        
    
    public ArrayList<Point> getMutationPoints(int organismAge) {
        ArrayList<Mutation> unRotated = mutations.get(organismAge);
        if (unRotated==null || unRotated.isEmpty()) {
            unRotated = new ArrayList<Mutation>();
            mutations.put(organismAge, unRotated);            
        }
        ArrayList<Point> mutationPoints = new ArrayList<Point>(unRotated.size());
        for (Mutation m : unRotated) {
            Point rp = invRotatePoint(m.getLocation(), seed.getRotation(), seed.isMirror());
            mutationPoints.add(rp);
        }
        return mutationPoints;
        
    }
    
    public int getMutationCount(int age) {
        if (mutations.get(age)==null) {
            return 0;
        }
        return mutations.get(age).size();
    }
    
    public Mutation getMutation(int age, int mutationIndex) {
        return mutations.get(age).get(mutationIndex);
    }
    
    public void addMutation(Mutation m) {
        ArrayList<Mutation> mutationsAtAge = mutations.get(m.getOrganismAge());
        if (mutationsAtAge == null) {
            mutationsAtAge = new ArrayList<Mutation>();
            mutations.put(m.getOrganismAge(),mutationsAtAge);
        }
        mutationsAtAge.add(m);
    }
    
    public Mutation addMutation(int x, int y, int organismAge, int systemTime) {        
        Point location = rotatePoint(new Point(x, y), seed.getRotation(), seed.isMirror());
        Mutation m = new Mutation(location,organismAge, systemTime);
        addMutation(m);    
        return m;
    }    
    
    public boolean removeMutation(Mutation m) {
        ArrayList<Mutation> mutationsAtAge = mutations.get(m.getOrganismAge());
        if (mutationsAtAge!=null) {
            return mutationsAtAge.remove(m);
        }
        return false;
    }
    
    public Collection<Mutation> getRecentMutations(int fromTime, int toTime, int maxAge) {
    	HashSet<Mutation> recentMutations = new HashSet<Mutation>();
    	for (ArrayList<Mutation> mu : mutations.values()) {
    		for (Mutation m : mu) {
    			if (m.getGameTime()>=fromTime && 
    			        m.getGameTime()<=toTime &&
    			        m.getOrganismAge()<=maxAge) {
    				recentMutations.add(m);
    			}
    		}
    	}
    	return recentMutations;
    }
        
    
    public static Point rotatePoint(Point point, int rotation, boolean mirror) {
        if (mirror) {
            point = new Point(-point.x, point.y);
        }
        switch (rotation) {                
            //case 0:
            default: return new Point(point.x, point.y); 
            
            case 1: return new Point(-point.y, point.x);                            
            case 2: return new Point(-point.x, -point.y);
            case 3: return new Point(point.y, -point.x); 
            
        }
    }
    
    public static Point invRotatePoint(Point point, int rotation, boolean mirror) {
        if (!mirror) {
            switch (rotation) {                
                //case 0:
                default: return new Point(point.x, point.y); 
                
                case 1: return new Point(point.y, -point.x);                
                case 2: return new Point(-point.x, -point.y);                
                case 3: return new Point(-point.y, point.x); 
   
            }
        }
        else {
            switch (rotation) {   
                //case 0:
                default: return new Point(-point.x, point.y); 
                
                case 1: return new Point(-point.y, -point.x);                
                case 2: return new Point(point.x, -point.y);                
                case 3: return new Point(point.y, point.x);     
            }
        }
    }
    
    

}
