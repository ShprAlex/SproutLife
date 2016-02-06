/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.echosystem;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import com.sproutlife.model.GameClock;
import com.sproutlife.model.seed.Seed;

/**
 * The Echosystem keeps track of all our organisms. It manages the Board,
 * which knows which cells are neighbors. It has a reference to a clock which
 * lets us know how old rganisms are and when they were born.
 * 
 * @author Alex Shapiro
 */

public class Echosystem {
    HashSet<Organism> organisms;
    HashSet<Organism> retiredOrganisms;

    Board board;

    int defaultOrgLifespan;
    int retirementTimeSpan;

    GameClock clock;

    public int typeCount = 0;

    public Echosystem(GameClock clock) {
        this.organisms = new HashSet<Organism>();
        this.retiredOrganisms = new HashSet<Organism>();
        this.board = new Board();
        this.clock = clock;

        this.defaultOrgLifespan = 30;
        this.retirementTimeSpan = 1000;
    }

    public Collection<Organism> getOrganisms() {
        return organisms;
    }

    public Collection<Organism> getRetiredOrganisms() {
        return retiredOrganisms;
    }

    public Board getBoard() {
        return board;
    }

    public GameClock getClock() {
        return clock;
    }

    public int getTime() {
        return clock.getTime();
    }

    /*
     * Convenience method. Maybe slow, and shouldn't be used often. Should be
     * replaced by custom iterator.
     * 
     * @Return all the cells
     */
    public ArrayList<Cell> getCells() {
        ArrayList<Cell> cellList = new ArrayList<Cell>();
        for (Organism o : getOrganisms()) {
            cellList.addAll(o.getCells());
        }

        return cellList;
    }

    public void resetCells() {
        ArrayList<Cell> cells = new ArrayList<Cell>(getCells());
        for (Cell c : cells) {
            removeCell(c);
        }

        this.setDefaultOrgLifespan(15);
    }

    public boolean removeCell(Cell c) {
        return removeCell(c, true);
    }

    private boolean removeCell(Cell c, boolean updateBoard) {
        if (c == null) {
            return false;
        }

        boolean result = c.getOrganism().removeCell(c);

        if (updateBoard) {
            // We don't want to update the board if we just reset it.
            getBoard().removeCell(c);
        }

        return result;
    }

    public Cell addCell(int x, int y, Organism org) {
        // if (x==getBoard().getWidth()/2 && !liftBarrier) {
        // return null;
        // }
        Cell c = org.addCell(x, y);
        if (c != null) {
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
        return parents.get(0).getOrganism().createCell(x, y, parents);
    }

    public void removeCell(int x, int y) {

        Cell c = getBoard().getCell(x, y);
        if (c != null) {
            removeCell(c);
        }
    }

    public void setDefaultOrgLifespan(int orgLifespan) {
        this.defaultOrgLifespan = orgLifespan;
    }

    public int getDefaultOrgLifespan() {
        return defaultOrgLifespan;
    }

    public int getRetirementTimeSpan() {
        return retirementTimeSpan;
    }

    public void setRetirementTimeSpan(int retirementTimeSpan) {
        this.retirementTimeSpan = retirementTimeSpan;
    }

    public Organism createOrganism(int x, int y, Organism parent, Seed seed) {
        typeCount++;
        //
        Organism newOrg = new Organism(typeCount, getClock(), x, y, parent,
                seed);
        if (parent == null) {
            newOrg.setLifespan(getDefaultOrgLifespan());// +(new
                                                        // Random()).nextInt(3));
        }

        organisms.add(newOrg);
        return newOrg;

    }

    public void removeOrganism(Organism o) {
        for (Cell c : o.getCells()) {
            getBoard().removeCell(c);
        }
        organisms.remove(o);
    }

    /*
     * A retired organims is really dead, maybe the function should be renamed.
     * Retired organisms hang around in a separate list for statistics purposes.
     */
    public void retireOrganism(Organism o) {

        for (Cell c : o.getCells()) {

            getBoard().removeCell(c);
        }
        o.setAlive(false);
        o.setTimeOfDeath(getTime());

        organisms.remove(o);
        retiredOrganisms.add(o);
    }

    public void removeRetired(Organism o) {
        retiredOrganisms.remove(o);
    }

    public void updateBoard() {

        getBoard().resetBoard();

        ArrayList<Cell> removeList = new ArrayList<Cell>(0);

        for (Organism o : getOrganisms()) {
            for (Cell current : o.getCells()) {
                if (current.x < 0 || current.x >= getBoard().getWidth()
                        || current.y < 0 || current.y >= getBoard().getHeight()) {
                    removeList.add(current);
                }
                else {
                    getBoard().setCell(current);
                    // gameBoard[current.x+1][current.y+1] = current;
                }
            }
        }
        for (Cell r : removeList) {
            removeCell(r, false);
        }
        pruneEmptyOrganisms();
    }

    /*
     * Make sure the all cells on the board are contained within organisms, and
     * that all cells in organisms are on the board.
     * 
     * @return if the board isn't consistent with organisms, return false
     */
    public boolean validateBoard() {
        for (Organism o : getOrganisms()) {
            for (Cell current : o.getCells()) {
                if (getBoard().getCell(current.x, current.y) != current) {
                    return false;
                }
            }
        }
        for (int i = 0; i < getBoard().getWidth(); i++) {
            for (int j = 0; j < getBoard().getHeight(); j++) {
                Cell c = getBoard().getCell(i, j);
                if (c != null && c.getOrganism().getCell(c.x, c.y) != c) {
                    return false;
                }

            }
        }

        return true;
    }

    public void pruneEmptyOrganisms() {
        HashSet<Organism> pruneOrgs = new HashSet<Organism>();
        pruneOrgs.addAll(getOrganisms());
        for (Organism org : pruneOrgs) {

            if (org.getCells().size() == 0) {
                retireOrganism(org);
                //org.setAlive(false);
                //removeOrganism(org);
            }
        }
    }

    public void clearRetiredOrgs() {
        getRetiredOrganisms().clear();
    }
    
    public void setBoardSize(Dimension d) {
        getBoard().setSize(d);
        updateBoard();

    }
}
