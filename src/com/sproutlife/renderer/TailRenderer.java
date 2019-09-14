/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class TailRenderer extends Renderer {

    public TailRenderer(GameModel gameModel, BoardRenderer boardRenderer) {
        super(gameModel, boardRenderer);
    }

    public void paintTail(Graphics2D g, Organism o) {

        int BLOCK_SIZE = getBlockSize();

        Organism parent = o.getParent();

        ((Graphics2D) g).setStroke(new BasicStroke(BLOCK_SIZE * 4 / 5));

        if (parent != null) {
            Organism gParent = parent.getParent();
            if (gParent != null) {
                g.setColor(getColor(o));
                drawLine(g, parent, gParent);
            }
        }

        if (parent != null) {
            
            g.setColor(getColor(o));
            drawLine(g, o.x, o.y, parent.x, parent.y);            
        }

    }

    public void drawLine(Graphics2D g, Organism o1, Organism o2) {
        drawLine(g, o1.x, o1.y, o2.x , o2.y);
    }

    public void drawLine(Graphics2D g, int x1, int y1, int x2, int y2) {
        int BLOCK_SIZE = getBlockSize();
        g.drawLine(
                BLOCK_SIZE + BLOCK_SIZE / 2 + (BLOCK_SIZE * x1),
                BLOCK_SIZE + BLOCK_SIZE / 2 + (BLOCK_SIZE * y1),
                BLOCK_SIZE + BLOCK_SIZE / 2 + (BLOCK_SIZE * x2),
                BLOCK_SIZE + BLOCK_SIZE / 2 + (BLOCK_SIZE * y2));
    }

    private Color getColor(Organism o) {
    	return getColorModel().getTailColor(o);
    }
}
