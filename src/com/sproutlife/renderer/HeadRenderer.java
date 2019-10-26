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
import com.sproutlife.model.echosystem.Organism;

public class HeadRenderer extends OrganismRenderer {

    public HeadRenderer(GameModel gameModel, BoardRenderer boardRenderer) {
        super(gameModel, boardRenderer);
    }

    public void render(Graphics2D g, Organism o) {
        int BLOCK_SIZE = getBlockSize();
        int rectSize = BLOCK_SIZE*3;
        g.setColor(getColor(o));
        double ox = o.x;
        double oy = o.y;
        for (Organism ch : o.getChildren()) {
            if (ch.isAlive()) {
                return;
            }
        }

        Organism parent = o.getParent();
        if (parent!=null) {
            int paab = o.getAttributes().parentAgeAtBirth;
            if (parent.getParent()!=null) {
                // average of parent and grandparent age of having child.
                paab = (paab + o.getParent().getAttributes().parentAgeAtBirth)/2;
            }
            double scale = 1.0*Math.min(o.getAge(),paab)/paab;
            if (o.getChildren().size()>0) {
                scale = 1;
            }
            ox = parent.x+((o.x-parent.x)*scale);
            oy = parent.y+((o.y-parent.y)*scale);
        }

        g.fillRect((int) (BLOCK_SIZE*(ox-1)), (int) (BLOCK_SIZE*(oy-1)), rectSize, rectSize);
    }

    private Color getColor(Organism o) {
        return getColorModel().getHeadColor(o);
    }
}
