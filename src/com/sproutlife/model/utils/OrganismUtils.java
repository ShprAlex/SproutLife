package com.sproutlife.model.utils;

import java.util.HashSet;

import com.sproutlife.model.echosystem.Organism;

public class OrganismUtils {
    private static HashSet<Organism> getAncestorsAndMe(Organism o, int dist) {
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

    public static boolean isFamily(Organism o1, Organism o2, int dist) {
        HashSet<Organism> o1Ancestors = getAncestorsAndMe(o1, dist);
        HashSet<Organism> o2Ancestors = getAncestorsAndMe(o2, dist); 
        for (Organism o :o1Ancestors) {
            if (o2Ancestors.contains(o)) {
                return true;
            }
        }
        return false;        
    }
}
