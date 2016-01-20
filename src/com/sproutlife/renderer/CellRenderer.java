package com.sproutlife.renderer;

import java.awt.Color;
import java.awt.Graphics2D;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;

public class CellRenderer extends Renderer {
    boolean paintRetiredCells;
	
	public CellRenderer(GameModel gameModel) {
		super(gameModel);
		paintRetiredCells = false;
	}

	public void setPaintRetiredCells(boolean paintRetired) {
		this.paintRetiredCells = paintRetired;
	}
	
	public boolean getPaintRetiredCells() {
		return paintRetiredCells;
	}
	
	public void paintCell(Graphics2D g, Cell c) {
		g.setColor(getColor(c));
		int BLOCK_SIZE = getBlockSize();
		g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*c.x), BLOCK_SIZE + (BLOCK_SIZE*c.y), BLOCK_SIZE, BLOCK_SIZE);
	}

	private Color getColor(Cell c) {	
		int red = 0;
		int green = 0;
		int blue = 0;

		int age;
		
		if (c.getOrganism().isAlive()) {
			age = c.getOrganism().getAge();
			
			red = c.getOrganism().getKind()%3==0?255:120;
			green = c.getOrganism().getKind()%3==1?255:120;
			blue = c.getOrganism().getKind()%3==2?255:120;
		}
		else if (getPaintRetiredCells()) {
			age = getGameModel().getTime()-c.getOrganism().getTimeOfDeath();
			
			red = Math.min(255,c.getOrganism().getKind()%3==0?255:120+age*10);
			green = Math.min(255,c.getOrganism().getKind()%3==1?255:120+age*10);
			blue = Math.min(255,c.getOrganism().getKind()%3==2?255:120+age*10);
		}	
		
		return new Color(red,green,blue);
	}

}
