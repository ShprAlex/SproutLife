/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.echosystem;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.sproutlife.model.rotations.Rotations;
import com.sproutlife.model.seed.Seed;

/**
 * A Genome is a collection of Mutations.
 * 
 * @author Alex Shapiro
 */

public class Genome {
    
    // map of age -> list of mutations at age
    List<List<Mutation>> genome;
    Seed seed;

    public Genome() {
        this.genome = new ArrayList<>();
    }

    public Genome(List<List<Mutation>> sourceMutations) {
        this.genome = new ArrayList<>();
        // Clone mutations
        for (int age = 0; age < sourceMutations.size(); age++) {
            List<Mutation> sourceMutationsAtAge = sourceMutations.get(age);
            ArrayList<Mutation> cloneList = sourceMutationsAtAge != null
                    ? new ArrayList<Mutation>(sourceMutations.get(age))
                    : null;
            setMutationsAtAge(age, cloneList);
        }
    }

    public Genome clone() {
        return new Genome(this.genome);
    }

    /*
     * A seed has information about how the parent organism was rotated and
     * oriented
     */
    public void setSeed(Seed seed) {
        this.seed = seed;
    }

    public Seed getSeed() {
        return seed;
    }

    public int getMutationCount(int age) {
        if (age >= genome.size() || genome.get(age) == null) {
            return 0;
        }
        return genome.get(age).size();
    }

    public List<Mutation> getMutationsAtAge(int age) {
        if (age >= genome.size()) {
            return null;
        }
        return genome.get(age);
    }

    public void setMutationsAtAge(int age, List<Mutation> mutationsAtAge) {
        while (this.genome.size()<=age) {
            this.genome.add(null);
        }
        this.genome.set(age, mutationsAtAge);
    }

    public ArrayList<Point> getMutationPoints(int organismAge) {
        if (organismAge >= genome.size()) {
            return new ArrayList<Point>();
        }
        List<Mutation> unRotated = getMutationsAtAge(organismAge);
        if (unRotated == null || unRotated.isEmpty()) {
            unRotated = new ArrayList<Mutation>();
            //setMutationsAtAge(organismAge, unRotated);
        }
        ArrayList<Point> mutationPoints = new ArrayList<Point>(unRotated.size());
        for (Mutation m : unRotated) {
            Point rp = Rotations.toBoard(m.getLocation(), seed.getRotation());
            mutationPoints.add(rp);
        }
        return mutationPoints;
    }

    public Mutation getMutation(int age, int mutationIndex) {
        List<Mutation> mutationsAtAge = getMutationsAtAge(age);
        if (mutationsAtAge == null) {
            return null;
        }
        return genome.get(age).get(mutationIndex);
    }

    public void addMutation(Mutation m) {
        List<Mutation> mutationsAtAge = getMutationsAtAge(m.getOrganismAge());
        if (mutationsAtAge == null) {
            mutationsAtAge = new ArrayList<Mutation>();
            setMutationsAtAge(m.getOrganismAge(), mutationsAtAge);
        }
        mutationsAtAge.add(m);
    }

    public Mutation addMutation(int x, int y, int organismAge, int systemTime) {
        Point location = Rotations.fromBoard(new Point(x, y), seed.getRotation());
        Mutation m = new Mutation(location, organismAge, systemTime);
        addMutation(m);
        return m;
    }

    public boolean removeMutation(Mutation m) {
        List<Mutation> mutationsAtAge = getMutationsAtAge(m.getOrganismAge());
        if (mutationsAtAge != null) {
            return mutationsAtAge.remove(m);
        }
        return false;
    }

    public Collection<Mutation> getRecentMutations(int fromTime, int toTime, int maxAge) {
        HashSet<Mutation> recentMutations = new HashSet<Mutation>();
        for (List<Mutation> mu : genome) {
            if (mu == null) {
                continue;
            }
            for (Mutation m : mu) {
                if (m.getGameTime() >= fromTime && m.getGameTime() <= toTime
                        && m.getOrganismAge() <= maxAge) {
                    recentMutations.add(m);
                }
            }
        }
        return recentMutations;
    }
}
