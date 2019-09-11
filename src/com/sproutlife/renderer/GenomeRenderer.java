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

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class GenomeRenderer extends Renderer {

    public GenomeRenderer(GameModel gameModel, BoardRenderer boardRenderer) {
        super(gameModel, boardRenderer);
    }

    public void paintGenome(Graphics2D g, Organism o) {

        ArrayList<Point> filteredMutationPoints = getFilteredMutationPoints(o);

        int BLOCK_SIZE = getBlockSize();

        //Paint white background under black mutation points

        g.setColor(getColor(o));
        //g.setColor(new Color(255,255,255,120));
        if (BLOCK_SIZE>1) {
            int countP=0;
            for (Point p: filteredMutationPoints) {
                boolean oneSmaller = false;

                if (countP++>4) {
                    oneSmaller=true;
                }

                if (BLOCK_SIZE>1 || !oneSmaller) {


                    paintBlock(g,o.x,o.y,p.x,p.y,0,1,oneSmaller);
                    paintBlock(g,o.x,o.y,p.x,p.y,1,0,oneSmaller);

                }
                else {

                    paintBlock(g,o.x,o.y,p.x,p.y,1,1,oneSmaller);

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

                paintBlock(g,o.x,o.y,p.x,p.y,0,-1,oneSmaller);
                paintBlock(g,o.x,o.y,p.x,p.y,-1,0,oneSmaller);
            }
            else {
                paintBlock(g,o.x,o.y,p.x,p.y,0,0,oneSmaller);
            }
        }
    }

    public void paintBlock(Graphics2D g, int x, int y, int mx, int my, int dx, int dy, boolean oneSmaller) {

        int BLOCK_SIZE = getBlockSize();            
        double mbs = BLOCK_SIZE/3.5;
        if (BLOCK_SIZE>3) {
            mbs = BLOCK_SIZE/4.25;
        }

        int rx = BLOCK_SIZE + (BLOCK_SIZE*x)+(int)(mx*mbs)-dx;
        int ry = BLOCK_SIZE + (BLOCK_SIZE*y)+(int)(my*mbs)-dy;
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
            ArrayList<Point> mutationPoints = o.getGenome().getMutationPoints( age);
            int timeLimit = 15000;//Math.max(10000,getGameModel().getClock()/3);

            for (int i = 0;i<mutationPoints.size();i++) {
                Point p = mutationPoints.get(i);
                //May be slow
                int mutationAge = getGameModel().getEchosystem().getTime()-o.getGenome().getMutation(age, i).getGameTime();
                if(mutationAge<timeLimit) {
                    filteredMutationPoints.add(p);

                    //g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*o.x)+(BLOCK_SIZE*p.x/2), BLOCK_SIZE + (BLOCK_SIZE*o.y)+(BLOCK_SIZE*p.y/2), BLOCK_SIZE, BLOCK_SIZE);
                    if (mutationAge<500) {
                        //g.setColor(Color.gray);
                    }

                }
            }
        }
        return filteredMutationPoints;
    }

    private Color getColor(Organism o) {
        //return Color.white;

        /*int grayC = 200;
        switch (o.getAttributes().kind) {
            case 0: return new Color(255, 186, 186);
            case 1: return new Color(grayC, 255, grayC);
            case 2: return new Color(grayC+10, grayC+10,255);
        }
        return null;*/

        Color c = o.getColor();
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), 128);
        //return new Color(Math.min(255, c.getRed()+100), Math.min(255, c.getGreen()+100), Math.min(255, c.getBlue()+100));
    }
}
