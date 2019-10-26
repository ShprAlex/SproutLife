/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.renderer;

import java.awt.Color;
import java.awt.Graphics2D;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class CellRenderer extends OrganismRenderer {
    public CellRenderer(GameModel gameModel, BoardRenderer boardRenderer) {
        super(gameModel, boardRenderer);
    }

    public void render(Graphics2D g, Organism o) {
        int BLOCK_SIZE = getBlockSize();
        if (getBoardRenderer().getOutlineSeeds()) {
            for (Cell c: o.getCells()) {
                if (c.isMarkedAsSeed() || c.getOrganism().getAge()<5) {
                    g.setColor(Color.black);
                    g.fillRect(BLOCK_SIZE*c.x-2, BLOCK_SIZE*c.y-2, BLOCK_SIZE+4, BLOCK_SIZE+4);
                }
            }
            for (Cell c: o.getCells()) {
                if (c.isMarkedAsSeed() || c.getOrganism().getAge()<5) {
                    g.setColor(Color.white);
                    g.fillRect(BLOCK_SIZE*c.x-1, BLOCK_SIZE*c.y-1, BLOCK_SIZE+2, BLOCK_SIZE+2);
                }
            }
        }

        for (Cell c: o.getCells()) { 
            g.setColor(getColor(o));
            g.fillRect(BLOCK_SIZE*c.x, BLOCK_SIZE*c.y, BLOCK_SIZE, BLOCK_SIZE);
        }
    }

    private Color getColor(Organism o) {
    	return getColorModel().getCellColor(o);
    }
}
