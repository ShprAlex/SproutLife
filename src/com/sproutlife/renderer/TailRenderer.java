package com.sproutlife.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class TailRenderer extends Renderer {
	
	public TailRenderer(GameModel gameModel) {
		super(gameModel);		
	}

	public void paintTail(Graphics2D g, Organism o) {
		g.setColor(getColor(o));

		//g.setColor(new Color(red,green,blue, 160));

		Organism parent = o.getParent();
		//if (orgs.contains(parent)) {
		((Graphics2D) g).setStroke(new BasicStroke(BLOCK_SIZE*2/3));

		Organism lo = o;
		if (parent!=null) {
			g.drawLine(BLOCK_SIZE+BLOCK_SIZE/2 + (BLOCK_SIZE*(lo.x)),BLOCK_SIZE +BLOCK_SIZE/2+ (BLOCK_SIZE*(lo.y)),BLOCK_SIZE +BLOCK_SIZE/2+ (BLOCK_SIZE*(parent.x)),BLOCK_SIZE +BLOCK_SIZE/2+ (BLOCK_SIZE*(parent.y)));
			lo = parent;
			parent = parent.getParent();
		}                

		if (parent!=null) {
			g.drawLine(BLOCK_SIZE+BLOCK_SIZE/2 + (BLOCK_SIZE*(lo.x)),BLOCK_SIZE +BLOCK_SIZE/2+ (BLOCK_SIZE*(lo.y)),BLOCK_SIZE +BLOCK_SIZE/2+ (BLOCK_SIZE*(parent.x)),BLOCK_SIZE +BLOCK_SIZE/2+ (BLOCK_SIZE*(parent.y)));
			lo = parent;
			parent = parent.getParent();
		}
		/*
                if (parent!=null) {
                    g.setColor(Color.white);
                    g.drawLine(BLOCK_SIZE+BLOCK_SIZE/2 + (BLOCK_SIZE*(o.x)),BLOCK_SIZE +BLOCK_SIZE/2+ (BLOCK_SIZE*(o.y)),BLOCK_SIZE +BLOCK_SIZE/2+ (BLOCK_SIZE*(parent.x)),BLOCK_SIZE +BLOCK_SIZE/2+ (BLOCK_SIZE*(parent.y)));
                    o = parent;
                    parent = parent.getParent();
                }
		 */
		//}     
	}
	
	private Color getColor(Organism o) {
		
		switch (o.getKind()) {
			case 0: return new Color(255,0,0, 80);
			case 1: return new Color(0,255,0, 80);
			case 2: return new Color(0,60,255, 80);
		}
		return null; 
	}
}
