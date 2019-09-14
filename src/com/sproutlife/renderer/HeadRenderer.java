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

public class HeadRenderer extends Renderer {

    public HeadRenderer(GameModel gameModel, BoardRenderer boardRenderer) {
        super(gameModel, boardRenderer);
    }

    public void paintHead(Graphics2D g, Organism o) {
        int BLOCK_SIZE = getBlockSize();

        g.setColor(getColor(o));
        if (o.getCells().size()>0) {
            int rectSize = BLOCK_SIZE*3;
            g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*(o.x-1)), BLOCK_SIZE + (BLOCK_SIZE*(o.y-1)), rectSize, rectSize);
        }

    }

    private Color getColor(Organism o) {
        return getColorModel().getHeadColor(o);
    }
}
