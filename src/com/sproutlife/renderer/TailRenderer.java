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
		

		//g.setColor(new Color(red,green,blue, 160));

		Organism parent = o.getParent();
		//if (orgs.contains(parent)) {
		((Graphics2D) g).setStroke(new BasicStroke(BLOCK_SIZE*4/5));
                
                if (parent!=null) {
                    Organism gParent=parent.getParent();
                    if (gParent!=null) {
                        g.setColor(getColor2(o));
                        drawLine(g,parent,gParent);
                    }
                }
	
		if (parent!=null) {
		    drawLine(g,o,parent);
		        g.setColor(getColor(o));
		        drawLine(g,o,parent);
		}                		
   
	}
	
	public void drawLine(Graphics2D g, Organism o1, Organism o2) {
	    g.drawLine(BLOCK_SIZE+BLOCK_SIZE/2 + (BLOCK_SIZE*(o1.x)),BLOCK_SIZE +BLOCK_SIZE/2+ (BLOCK_SIZE*(o1.y)),BLOCK_SIZE +BLOCK_SIZE/2+ (BLOCK_SIZE*(o2.x)),BLOCK_SIZE +BLOCK_SIZE/2+ (BLOCK_SIZE*(o2.y)));
	}
	
	private Color getColor(Organism o) {
	           switch (o.getKind()) {
	                    case 0: return new Color(255, 160, 160);
	                    case 1: return new Color(160, 255, 160);
	                    case 2: return new Color(160, 160, 255);
	                }
	    /*
	        switch (o.getKind()) {
	            case 0: return new Color(255, 176, 176);
	            case 1: return new Color(176, 255, 176);
	            case 2: return new Color(176, 176 ,255);
	        }
	        */
	        /*		
		switch (o.getKind()) {
			case 0: return new Color(255,0,0, 80);
			case 1: return new Color(0,255,0, 80);
			case 2: return new Color(0,60,255, 80);
		}
		*/
		return null; 
	}
	
	private Color getColor2(Organism o) {
            switch (o.getKind()) {
                     case 0: return new Color(255, 176, 176);
                     case 1: return new Color(176, 255, 176);
                     case 2: return new Color(176, 176, 255);
                 }

         return null; 
 }
}
