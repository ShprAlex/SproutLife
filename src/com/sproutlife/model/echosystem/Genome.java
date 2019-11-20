/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.echosystem;

import java.util.ArrayList;
import java.util.List;

/**
 * A Genome is a collection of Mutations.
 * 
 * @author Alex Shapiro
 */

public class Genome {
    
    // map of age -> list of mutations at age
    List<List<Mutation>> genome;

    public Genome() {
        this.genome = new ArrayList<>();
    }

    public Genome(Genome sourceGenome) {
        this.genome = new ArrayList<>();
        // Clone mutations
        for (int age = 0; age < sourceGenome.getAgeRange(); age++) {
            List<Mutation> sourceMutationsAtAge = sourceGenome.getMutationsAtAge(age);
            ArrayList<Mutation> clonedMutationsAtAge = sourceMutationsAtAge != null
                    ? new ArrayList<Mutation>(sourceMutationsAtAge)
                    : null;
            setMutationsAtAge(age, clonedMutationsAtAge);
        }
    }

    public Genome clone() {
        return new Genome(this);
    }

    public int getAgeRange() {
        return genome.size();
    }

    public int getMutationCountAtAge(int age) {
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
        while (genome.size()<=age) {
            genome.add(null);
        }
        genome.set(age, mutationsAtAge);
    }

    public Mutation getMutation(int age, int mutationIndex) {
        List<Mutation> mutationsAtAge = getMutationsAtAge(age);
        if (mutationsAtAge == null) {
            return null;
        }
        return mutationsAtAge.get(mutationIndex);
    }

    public void addMutation(Mutation m) {
        List<Mutation> mutationsAtAge = getMutationsAtAge(m.getOrganismAge());
        if (mutationsAtAge == null) {
            mutationsAtAge = new ArrayList<Mutation>();
            setMutationsAtAge(m.getOrganismAge(), mutationsAtAge);
        }
        mutationsAtAge.add(m);
    }

    public boolean removeMutation(Mutation m) {
        List<Mutation> mutationsAtAge = getMutationsAtAge(m.getOrganismAge());
        if (mutationsAtAge != null) {
            return mutationsAtAge.remove(m);
        }
        return false;
    }
}
