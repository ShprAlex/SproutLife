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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import com.sproutlife.model.rotations.Rotations;
import com.sproutlife.model.seed.Seed;

/**
 * A Genome is a collection of mutations Mutations are stored in an un-rotated
 * state. We un-rotate a coordinate when adding it from the board and rotate it
 * back when returning it. Taking into account that the mutation may now belong
 * to a child that's facing a different direction than its parent.
 * 
 * @author Alex Shapiro
 */

public class Genome {

    HashMap<Integer, ArrayList<Mutation>> mutations;
    Seed seed;

    public Genome() {
        this.mutations = new HashMap<Integer, ArrayList<Mutation>>();
    }

    public Genome(HashMap<Integer, ArrayList<Mutation>> mutations) {
        this.mutations = new HashMap<Integer, ArrayList<Mutation>>();

        // Clone mutations
        for (Integer i : mutations.keySet()) {
            ArrayList<Mutation> cloneList = new ArrayList<Mutation>(
                    mutations.get(i));
            this.mutations.put(i, cloneList);
        }
    }

    public Genome clone() {
        return new Genome(this.mutations);
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

    public ArrayList<Point> getMutationPoints(int organismAge) {
        ArrayList<Mutation> unRotated = mutations.get(organismAge);
        if (unRotated == null || unRotated.isEmpty()) {
            unRotated = new ArrayList<Mutation>();
            mutations.put(organismAge, unRotated);
        }
        ArrayList<Point> mutationPoints = new ArrayList<Point>(unRotated.size());
        for (Mutation m : unRotated) {
            Point rp = Rotations.toBoard(m.getLocation(), seed.getRotation());
            mutationPoints.add(rp);
        }
        return mutationPoints;
    }

    public int getMutationCount(int age) {
        if (mutations.get(age) == null) {
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
            mutations.put(m.getOrganismAge(), mutationsAtAge);
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
        ArrayList<Mutation> mutationsAtAge = mutations.get(m.getOrganismAge());
        if (mutationsAtAge != null) {
            return mutationsAtAge.remove(m);
        }
        return false;
    }

    public Collection<Mutation> getRecentMutations(int fromTime, int toTime,
            int maxAge) {
        HashSet<Mutation> recentMutations = new HashSet<Mutation>();
        for (ArrayList<Mutation> mu : mutations.values()) {
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
