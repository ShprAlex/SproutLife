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
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.renderer.colors.AngleColorModel;

public class TailRenderer extends OrganismRenderer {
    int tailLength = 9;

    public TailRenderer(GameModel gameModel, BoardRenderer boardRenderer) {
        super(gameModel, boardRenderer);
    }

    public void setTailLength(int tailLength) {
        this.tailLength = tailLength;
    }

    public void render(Graphics2D g, Organism o) {
        int BLOCK_SIZE = getBlockSize();
        Organism parent = o.getParent();
        int strokeWidth = BLOCK_SIZE;

        ((Graphics2D) g).setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        if (parent==null) {
            return;
        }

        for (Organism ch : o.getChildren()) {
            if(ch.isAlive()) {
                return;
            }
        }

        g.setColor(getColor(o));
        int tl=0;
        if (o.isAlive()) {
            int paab = o.getAttributes().parentAgeAtBirth;
            if (parent.getParent()!=null) {
                // average of parent and grandparent age of having child.
                paab = (paab + o.getParent().getAttributes().parentAgeAtBirth)/2;
            }
            double scale = 1.0*Math.min(o.getAge(),paab)/paab;
            if (o.getChildren().size()>0) {
                scale = 1;
            }
            double ox = parent.x+((o.x-parent.x)*scale);
            double oy = parent.y+((o.y-parent.y)*scale);
            drawLine(g, ox, oy, parent.x, parent.y);
            o = parent;
            parent = parent.getParent();
            tl=1;
        }

        while (tl<tailLength && parent!=null) {
            g.setColor(getColor(o));
            drawLine(g, o.x, o.y, parent.x, parent.y);
            o = parent;
            parent = parent.getParent();
            tl++;
        }
    }

    public void drawLine(Graphics2D g, Organism o1, Organism o2) {
        drawLine(g, o1.x, o1.y, o2.x , o2.y);
    }

    public void drawLine(Graphics2D g, double x1, double y1, double x2, double y2) {
        int BLOCK_SIZE = getBlockSize();
        g.drawLine(
                BLOCK_SIZE / 2 + (int) (BLOCK_SIZE * x1),
                BLOCK_SIZE / 2 + (int) (BLOCK_SIZE * y1),
                BLOCK_SIZE / 2 + (int) (BLOCK_SIZE * x2),
                BLOCK_SIZE / 2 + (int) (BLOCK_SIZE * y2));
    }

    private Color getColor(Organism o) {
    	return getColorModel().getTailColor(o);
    }
}
