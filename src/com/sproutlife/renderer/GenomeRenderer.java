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
		
		double mbs = BLOCK_SIZE/3.5;
	    
		//Paint white background under black mutation points
		
		g.setColor(new Color(240,240,240));
		//g.setColor(new Color(255,255,255,120));
		if (BLOCK_SIZE>3) {       		    
		    for (Point p: filteredMutationPoints) {
		        if (BLOCK_SIZE>3) {
		            g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*o.x)+(int)(p.x*mbs), BLOCK_SIZE + (BLOCK_SIZE*o.y)+(int)(p.y*mbs)-1, BLOCK_SIZE, BLOCK_SIZE+2);
		            g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*o.x)+(int)(p.x*mbs)-1, BLOCK_SIZE + (BLOCK_SIZE*o.y)+(int)(p.y*mbs), BLOCK_SIZE+2, BLOCK_SIZE);
		        }
		        else if(false){
		            g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*o.x)+(int)(p.x*mbs)-1, BLOCK_SIZE + (BLOCK_SIZE*o.y)+(int)(p.y*mbs)-1, BLOCK_SIZE+2, BLOCK_SIZE+2);
		        
		            //g.fillOval(BLOCK_SIZE + (BLOCK_SIZE*o.x)+(int)(p.x*mbs)-2, BLOCK_SIZE + (BLOCK_SIZE*o.y)+(int)(p.y*mbs)-2, BLOCK_SIZE+4, BLOCK_SIZE+4);
		        }
		    }
		}

		//Paint mutation points on top of background

		g.setColor(Color.black);		
		for (Point p: filteredMutationPoints) {
		    if (BLOCK_SIZE>3) {
		        g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*o.x)+(int)(p.x*mbs), BLOCK_SIZE + (BLOCK_SIZE*o.y)+(int)(p.y*mbs)+1, BLOCK_SIZE, BLOCK_SIZE-2);
		        g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*o.x)+(int)(p.x*mbs)+1, BLOCK_SIZE + (BLOCK_SIZE*o.y)+(int)(p.y*mbs), BLOCK_SIZE-2, BLOCK_SIZE);    
		    }
		    else {
		        g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*o.x)+(int)(p.x*mbs), BLOCK_SIZE + (BLOCK_SIZE*o.y)+(int)(p.y*mbs), BLOCK_SIZE, BLOCK_SIZE);
		        //g.fillOval(BLOCK_SIZE + (BLOCK_SIZE*o.x)+(int)(p.x*mbs), BLOCK_SIZE + (BLOCK_SIZE*o.y)+(int)(p.y*mbs), BLOCK_SIZE, BLOCK_SIZE);
		    }
		}
	}
		
	
	ArrayList<Point> getFilteredMutationPoints(Organism o) {
		ArrayList<Point> filteredMutationPoints = new ArrayList<Point>();
		for (int age = 0;age<50;age++) {
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
}
