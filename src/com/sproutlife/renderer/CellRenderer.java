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

public class CellRenderer extends Renderer {
    boolean paintRetiredCells;
	
	public CellRenderer(GameModel gameModel, BoardRenderer boardRenderer) {
		super(gameModel, boardRenderer);
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
	

		int age;
		
		if (c.getOrganism().isAlive()) {
		        int grayC = 120;
		        switch (c.getOrganism().getKind()) {
		            case 0: return new Color(255, grayC, grayC);
		            case 1: return new Color(grayC-10, 255, grayC-10);
		            case 2: return new Color(grayC, grayC ,255);
		        }
		}
		else if (getPaintRetiredCells()) {
			age = getGameModel().getTime()-c.getOrganism().getTimeOfDeath();
			int ageC = Math.min(255,120+age*10);
		        switch (c.getOrganism().getKind()) {		            
		            case 0: return new Color(255, ageC, ageC);
		            case 1: return new Color(ageC, 255, ageC);
		            case 2: return new Color(ageC, ageC ,255);
		        }	
		}	
		
		return null;
	}

}
