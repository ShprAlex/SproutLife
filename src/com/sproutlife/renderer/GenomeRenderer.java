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
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.utils.MutationUtils;

public class GenomeRenderer extends OrganismRenderer {

    public GenomeRenderer(GameModel gameModel, BoardRenderer boardRenderer) {
        super(gameModel, boardRenderer);
    }

    public void render(Graphics2D g, Organism o) {
        for (Organism ch : o.getChildren()) {
            // if we hide the parent when a child is born, it looks like the parent became the child.
            if (ch.isAlive()) {
                return;
            }
        }

        ArrayList<Point> filteredMutationPoints = getFilteredMutationPoints(o);
        int BLOCK_SIZE = getBlockSize();

        double adjx = 0;
        double adjy = 0;
        Organism parent = o.getParent();
        if (parent!=null) {
            Point2Double scaledLocation = RendererUtils.getScaleTowardsBirthLocation(o);
            adjx = scaledLocation.getX()-o.x;
            adjy = scaledLocation.getY()-o.y;
        }

        //Paint light background under black mutation points
        g.setColor(getGenomeBackgroundColor(o));
        if (BLOCK_SIZE>1) {
            int countP=0;
            for (Point p: filteredMutationPoints) {
                boolean oneSmaller = false;

                if (countP++>4) {
                    oneSmaller=true;
                }

                if (BLOCK_SIZE>1 || !oneSmaller) {
                    paintBlock(g,o.x,o.y,p.x,p.y,adjx, adjy, 0,1,oneSmaller);
                    paintBlock(g,o.x,o.y,p.x,p.y,adjx, adjy, 1,0,oneSmaller);
                }
                else {
                    paintBlock(g,o.x,o.y,p.x,p.y,adjx, adjy, 1,1,oneSmaller);
                }
            }
        }

        //Paint mutation points on top of background
        g.setColor(Color.black);
        int countP=0;
        for (Point p: filteredMutationPoints) {
            boolean oneSmaller = false;

            if (countP++>4) {
                oneSmaller=true;
            }

            if (BLOCK_SIZE>3&&!oneSmaller || BLOCK_SIZE>4) {

                paintBlock(g,o.x,o.y,p.x,p.y,adjx, adjy,0,-1, oneSmaller);
                paintBlock(g,o.x,o.y,p.x,p.y,adjx, adjy,-1,0, oneSmaller);
            }
            else {
                paintBlock(g,o.x,o.y,p.x,p.y,adjx, adjy,0,0, oneSmaller);
            }
        }
    }

    public void paintBlock(Graphics2D g, int x, int y, int mx, int my, double adjx, double adjy, int dx, int dy, boolean oneSmaller) {

        int BLOCK_SIZE = getBlockSize();            
        double mbs = BLOCK_SIZE/3.5;
        if (BLOCK_SIZE>3) {
            mbs = BLOCK_SIZE/4.25;
        }

        int rx = (int) (BLOCK_SIZE*(x+adjx))+(int)(mx*mbs)-dx;
        int ry = (int) (BLOCK_SIZE*(y+adjy))+(int)(my*mbs)-dy;
        int rw = BLOCK_SIZE+dx*2;
        int rh = BLOCK_SIZE+dy*2;

        if (oneSmaller && rw>1) {
            rw-=1;
            rh-=1;
        }

        g.fillRect(rx, ry, rw, rh);            
    }


    ArrayList<Point> getFilteredMutationPoints(Organism o) {
        ArrayList<Point> filteredMutationPoints = new ArrayList<Point>();
        for (int age = 0;age<40;age++) {
            List<Point> mutationPoints = MutationUtils.getMutationPointsAtAge(o, age);
            int timeLimit = 15000;

            for (int i = 0;i<mutationPoints.size();i++) {
                Point p = mutationPoints.get(i);
                //May be slow
                int mutationAge = getGameModel().getEchosystem().getTime()-o.getGenome().getMutation(age, i).getGameTime();
                if(mutationAge<timeLimit) {
                    filteredMutationPoints.add(p);
                }
            }
        }
        return filteredMutationPoints;
    }

    private Color getGenomeBackgroundColor(Organism o) {
    	return getColorModel().getGenomeBackgroundColor(o);
    }
}
