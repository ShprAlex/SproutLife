/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step.lifemode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class SimpleLife extends LifeMode {

    public SimpleLife(GameModel gameModel) {
        super(gameModel);
    }

    public void perform() {
        updateMetrics();
        updateCells();
    }

    public void updateMetrics() {
        for (Organism o: getEchosystem().getOrganisms()) {
            o.getAttributes().maxCells = Math.max(o.getCells().size(), o.getAttributes().maxCells);
            o.getAttributes().cellSum += o.getCells().size();
            o.getAttributes().competitiveScore = o.getAttributes().cellSum;
        }
    }

    public void updateCells() {
        ArrayList<Cell> bornCells = new ArrayList<Cell>();
        ArrayList<Cell> deadCells = new ArrayList<Cell>();

        for (int x = 0; x < getBoard().getWidth(); x++) {
            for (int y = 0; y < getBoard().getHeight(); y++) {
                updateCell(x, y, bornCells, deadCells);
            }
        }

        // Remove cells before adding cells to avoid Organism having duplicate cells,
        // Orgs don't do contains() checks for speed
        for (Cell c : deadCells) {
            getEchosystem().removeCell(c);
        }
        for (Cell c : bornCells) {
            getEchosystem().addCell(c);
        }
    }

    protected void updateCell(int x, int y, List<Cell> bornCells, List<Cell> deadCells) {
        Cell c = getBoard().getCell(x,y);
        ArrayList<Cell> neighbors = getBoard().getNeighbors(x,y);

        if (c == null) {
            if (neighbors.size()>=3) {
                Cell bc = getBorn(neighbors, x, y);
                if (bc != null) {
                    bornCells.add(bc);
                    getStats().born++;
                }
            }
        } else {
            if (!keepAlive(c, neighbors, x, y)) {
                deadCells.add(c);
            } else {
                getStats().stayed++;
            }
        }
    }

    public boolean keepAlive(Cell c, Collection<Cell> neighbors, int x, int y) {
        if ((neighbors.size() == 2 || neighbors.size() == 3)) {
            c.age += 1;
            return true;
        }
        return false;
    }

    public Cell getBorn(Collection<Cell> neighbors, int x, int y) {
        if (x < 0 || x > getBoard().getWidth() - 1 || y < 0 || y > getBoard().getHeight() - 1) {
            return null;
        }
        if (neighbors.size() != 3) {
            return null;
        }
        // Quick check to see if all neighbors are from the same organism
        Organism checkSingleOrg = neighbors.iterator().next().getOrganism();
        boolean singleOrg = true;
        for (Cell cell : neighbors) {
            if (cell.getOrganism() != checkSingleOrg) {
                singleOrg = false;
                break;
            }
        }
        if (singleOrg) {
            Cell bornCell = getEchosystem().createCell(x, y, checkSingleOrg);
            return bornCell;
        }
        return null;
    }
}
