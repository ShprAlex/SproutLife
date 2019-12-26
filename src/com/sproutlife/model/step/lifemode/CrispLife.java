/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step.lifemode;

import java.util.Collection;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class CrispLife extends CompetitiveLife {
    public CrispLife(GameModel gameModel) {
        super(gameModel);
    }

    protected void updateCompetitiveScore(Organism o) {
        Organism p = o.getParent();
        if (p==null) {
            super.updateCompetitiveScore(o);
            return;
        }

        o.getAttributes().competitiveScore = (int) p.getAttributes().territoryProduct;
    }

    public boolean keepAlive(Cell c, Collection<Cell> neighbors, int x, int y) {
        if (!super.keepAlive(c, neighbors, x, y)) {
            return false;
        }

        for (Cell neighbor : getBoard().getExtra12Neighbors(x, y)) {
            if (neighbor.getOrganism() != c.getOrganism() && neighbor.getOrganism() != c.getOrganism().getParent()
                    && getCompare(c, neighbor) < 0) {
                c.getOrganism().getAttributes().collisionCount++;
                return false;
            }
        }

        return true;
    }

    public Cell getBorn(Collection<Cell> neighbors, int x, int y) {
        Cell bornCell = super.getBorn(neighbors, x, y);
        if (bornCell == null) {
            return null;
        }

        for (Cell neighbor : getBoard().getExtra12Neighbors(x, y)) {
            if (neighbor.getOrganism() != bornCell.getOrganism() && neighbor.getOrganism() != bornCell.getOrganism().getParent()
                    && getCompare(bornCell, neighbor) < 0) {
                return null;
            }
        }

        return bornCell;
    }
}
