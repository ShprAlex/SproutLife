package com.sproutlife.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class GenomeRenderer extends Renderer {
	
	public GenomeRenderer(GameModel gameModel) {
		super(gameModel);		
	}

	public void paintGenome(Graphics2D g, Organism o) {
		
		ArrayList<Point> filteredMutationPoints = getFilteredMutationPoints(o);
		
		int mbs = Math.min(1, BLOCK_SIZE/3);
	    
		//Paint white background under black mutation points
		 
		g.setColor(new Color(255,255,255,120));
		if (BLOCK_SIZE>3) {                  
			for (Point p: filteredMutationPoints) { 
				g.fillOval(BLOCK_SIZE + (BLOCK_SIZE*o.x)+(p.x*mbs)-2, BLOCK_SIZE + (BLOCK_SIZE*o.y)+(p.y*mbs)-2, BLOCK_SIZE+4, BLOCK_SIZE+4);
			}
		}

		//Paint mutation points on top of background
		
		g.setColor(Color.black);		
		for (Point p: filteredMutationPoints) {
			if (BLOCK_SIZE<=3) {
				g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*o.x)+(p.x*mbs), BLOCK_SIZE + (BLOCK_SIZE*o.y)+(p.y*mbs), BLOCK_SIZE, BLOCK_SIZE);    
			}
			else {
				g.fillOval(BLOCK_SIZE + (BLOCK_SIZE*o.x)+(p.x*mbs), BLOCK_SIZE + (BLOCK_SIZE*o.y)+(p.y*mbs), BLOCK_SIZE, BLOCK_SIZE);
			}
		}
	}
		
	
	ArrayList<Point> getFilteredMutationPoints(Organism o) {
		ArrayList<Point> filteredMutationPoints = new ArrayList<Point>();
		for (int age = 0;age<50;age++) {
			ArrayList<Point> mutationPoints = o.getGenome().getMutationPoints( age);
			int clockLimit = 15000;//Math.max(10000,getGameModel().getClock()/3);

			for (int i = 0;i<mutationPoints.size();i++) {
				Point p = mutationPoints.get(i);
				//May be slow
				int mutationAge = getGameModel().getEchosystem().getClock()-o.getGenome().getMutation(age, i).getGameTime();
				if(mutationAge<clockLimit) {
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
