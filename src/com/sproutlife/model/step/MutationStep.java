/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Genome;
import com.sproutlife.model.echosystem.Mutation;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.utils.MutationUtils;

public class MutationStep extends Step {
    GameModel gameModel;
    Random random;

    public MutationStep(GameModel gameModel) {
        super(gameModel);
        random = new Random();
    }

    public void perform() {
        doExistingMutations();
        if (getGameModel().getSettings().getBoolean(Settings.MUTATION_ENABLED)) {
            addNewMutations();
        }
    }

    /*
     * Go through the list of existing organisms and remove cells according to their
     * genome.
     */
    private void doExistingMutations() {
        for (Organism o : getEchosystem().getOrganisms()) {
            int age = o.getAge();
            List<Point> mutationPoints = MutationUtils.getOffsetMutationPointsAtAge(o, age);
            if (mutationPoints == null) {
                continue;
            }
            for (int pi = 0; pi < mutationPoints.size(); pi++) {
                Point p = mutationPoints.get(pi);
                Cell c = getBoard().getCell(p);
                if (c != null) {
                    if (c.getOrganism() == o) {
                        getEchosystem().removeCell(c);
                        c.getOrganism().removeFromTerritory(c);
                    } else {
                        getStats().mutationMiss++;
                    }
                } else {
                    getStats().mutationMiss++;
                }
            }
            getStats().mutationCount += mutationPoints.size();
        }
    }

    /*
     * Mutate the organisms self-destruct age, returns true if lifespan was mutated.
     */
    private boolean mutateLifespan(Organism org) {
        int rand3 = random.nextInt(3);

        if (rand3 == 0) {
            if (org.lifespan < getSettings().getInt(Settings.MAX_LIFESPAN)) {
                // increase lifespan
                org.lifespan += 1;
                return true;
            }
        } else if (rand3 == 1) {
            // decrease lifespan
            org.lifespan -= 1;
            return true;
        }

        // else if rand3 == 2, do nothing
        return false;
    }

    /*
     * Add a single mutation to a cell
     */
    private void addMutation(Cell c) {
        Organism org = c.getOrganism();
        int x = c.x - org.x;
        int y = c.y - org.y;

        Mutation m = MutationUtils.addMutation(org, x, y);

        for (Organism childOrg : org.getChildren()) {
            // Pretend the parent had this mutation when giving birth to it's children. It's
            // ok because the children are not yet old enough to have
            // encountered this mutation.
            childOrg.getGenome().addMutation(m);
        }
    }

    /*
     * Remove a random mutation at the organism's current age
     */
    private boolean removeMutation(Organism org) {
        Genome g = org.getGenome();
        int age = org.getAge();

        if (g.getMutationCountAtAge(age) == 0) {
            // if the organism doesn't have any mutations at the age it is now,
            // do nothing
            return false;
        }

        int indexAtAge = random.nextInt(g.getMutationCountAtAge(age));
        Mutation removeM = g.getMutation(age, indexAtAge);
        g.removeMutation(removeM);
        for (Organism childOrg : org.getChildren()) {
            // Pretend the parent didn't have this mutation when giving birth to it's
            // children. It's ok because the children are not yet old enough to have
            // encountered this
            // mutation.
            childOrg.getGenome().removeMutation(removeM);
        }

        return true;
    }

    /*
     * Add new mutations depending on the MUTATION_RATE
     */
    private void addNewMutations() {
        ArrayList<Cell> cellsFromAllOrganisms = getEchosystem().getCells();
        if (cellsFromAllOrganisms.size() == 0) {
            return;
        }

        int createMutationCount = getSettings().getInt(Settings.MUTATION_RATE);
        createMutationCount = createMutationCount * getBoard().getWidth() * getBoard().getHeight() / 10000;
        for (int repeat = 0; repeat < createMutationCount; repeat++) {
            int x = random.nextInt(getEchosystem().getBoard().getWidth());
            int y = random.nextInt(getEchosystem().getBoard().getHeight());

            Cell randomCell = getEchosystem().getBoard().getCell(x, y);
            if (randomCell == null) {
                continue;
            }

            boolean mutatedLifespan = mutateLifespan(randomCell.getOrganism());
            if (mutatedLifespan && random.nextInt(2) == 0) {
                // if we mutated the lifespan, 50% chance we're done mutating stuff this time
                continue;
            }

            // we're more likely to remove a mutation than to add one
            boolean mutationIsAdd = (random.nextInt(7) >= 4);

            if (mutationIsAdd) {
                addMutation(randomCell);
                // existing mutations remove cells, so do it for this new mutation too
                getEchosystem().removeCell(randomCell);
            } else {
                removeMutation(randomCell.getOrganism());
            }
        }

    }

}
