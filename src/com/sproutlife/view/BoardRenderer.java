package com.sproutlife.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class BoardRenderer {
	private GameModel gameModel;
	private GamePanel gamePanel;
	private int BLOCK_SIZE; //will refactor this

	public BoardRenderer(GameModel gameModel, GamePanel gamePanel) {
		this.gameModel = gameModel;
		this.gamePanel = gamePanel;
		BLOCK_SIZE = GamePanel.BLOCK_SIZE;
	}

	private GamePanel getGamePanel() {
		return gamePanel;
	}

	public GameModel getGameModel() {
		return gameModel;
	}

	public void paint(Graphics g) {		
		/*
	        AffineTransform t = new AffineTransform();
	        t.setToScale(2, 2);
	        ((Graphics2D) g).setTransform(t);
		 */

        paintBackground(g);
        
		paintCells(g);
		
		//paintRetiredCells(g);

        paintOrgHeads(g);
        
        paintOrgLines(g);

        paintMutations(g);
	}
	
	private void paintBackground(Graphics g) {
		g.setColor(new Color(248,248,248));
		g.fillRect(0,0,getGamePanel().getWidth(),getGamePanel().getHeight());

	}
	
	
	private void paintCells(Graphics g) {
		ArrayList<Cell> cells = getGameModel().getEchosystem().getCells();
		for (Cell newCell : cells) {
			// Draw new point

			int red = 155;
			int green = 0;
			int blue = 0;

			int age = getGameModel().getEchosystem().getClock()-newCell.getOrganism().born;
			if(newCell.getOrganism()!=null) {
				red = newCell.getOrganism().getKind()%3==0?255:120;
				green = newCell.getOrganism().getKind()%3==1?255:120;
				blue = newCell.getOrganism().getKind()%3==2?255:120;
			}

			g.setColor(new Color(red,green,blue));
			g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*newCell.x), BLOCK_SIZE + (BLOCK_SIZE*newCell.y), BLOCK_SIZE, BLOCK_SIZE);
		} 

	}
	
	private void paintRetiredCells(Graphics g) {
		Collection<Organism> retiredOrgs = getGameModel().getEchosystem().getRetiredOrganisms();
		g.setColor(Color.lightGray);

		
		for (Organism o: retiredOrgs) {			
			for (Cell c: o.getCells()) {
				
				int age = getGameModel().getEchosystem().getClock()-c.getOrganism().getTimeOfDeath();
				
				int red = 0;
				int green = 0;
				int blue = 0;

				if(c.getOrganism()!=null) {
					red = Math.min(255,c.getOrganism().getKind()%3==0?255:120+age*10);
					green = Math.min(255,c.getOrganism().getKind()%3==1?255:120+age*10);
					blue = Math.min(255,c.getOrganism().getKind()%3==2?255:120+age*10);
				}

				g.setColor(new Color(red,green,blue));
				
				g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*c.x), BLOCK_SIZE + (BLOCK_SIZE*c.y), BLOCK_SIZE, BLOCK_SIZE);
			}
		}
	}
	
	public void paintOrgHeads(Graphics g) {

		Collection<Organism> orgs = getGameModel().getEchosystem().getOrganisms();

		for (Organism o : orgs) {

			//int age = getGameModel().getAge(o);                


			//int red =  o.getKind()%3==0?255:0;
			//int green = o.getKind()%3==1?255:0;
			//int blue = o.getKind()%3==2?255:0;
			switch (o.getKind()) {
			case 0:g.setColor(new Color(255,0,0, 80)); break;
			case 1:g.setColor(new Color(0,255,0, 80)); break;
			case 2:g.setColor(new Color(0,60,255, 80)); break;

			}


			/*
	                Point op = new Point(o.getPosition());
	                if(o.getParent()!=null) {
	                    int age = getGameModel().getAge(o);
	                    if (age<10) {
	                         op.x=o.getParent().x+(op.x-o.getParent().x)*age/10;
	                         op.y=o.getParent().y+(op.y-o.getParent().y)*age/10;
	                    }
	                }
			 */
			if (o.getCells().size()>0) {
				int rectSize = BLOCK_SIZE*3;//(int) Math.sqrt(o.getCells().size()*BLOCK_SIZE*BLOCK_SIZE);
				//g.fillOval(BLOCK_SIZE + (BLOCK_SIZE*o.x)-rectSize/2, BLOCK_SIZE + (BLOCK_SIZE*o.y)-rectSize/2, rectSize, rectSize);
				g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*(o.x-1)), BLOCK_SIZE + (BLOCK_SIZE*(o.y-1)), rectSize, rectSize);
			}
		}
	}
	
	public void paintOrgLines(Graphics g) {
		
		Collection<Organism> orgs = getGameModel().getEchosystem().getOrganisms();

		for (Organism o : orgs) {

			switch (o.getKind()) {
			case 0:g.setColor(new Color(255,0,0, 80)); break;
			case 1:g.setColor(new Color(0,255,0, 80)); break;
			case 2:g.setColor(new Color(0,60,255, 80)); break;

			}
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
	}
	
	private void paintMutations(Graphics g) {
		Collection<Organism> orgs = getGameModel().getEchosystem().getOrganisms();
		/*
	            Collection<Organism> orgs = new ArrayList<Organism>();            
	            Organism firstOrg = getGameModel().getEchosystem().getOrganisms().iterator().next();
	            orgs.add(firstOrg);
	            orgs.addAll(firstOrg.getChildren());
		 */

		for (Organism o : orgs) {
			ArrayList<Point> filteredMutationPoints = new ArrayList<Point>();
			for (int age = 0;age<50;age++) {
				ArrayList<Point> mutationPoints = o.getGenetics().getMutationPoints( age);
				int clockLimit = 15000;//Math.max(10000,getGameModel().getClock()/3);

				for (int i = 0;i<mutationPoints.size();i++) {
					Point p = mutationPoints.get(i);
					//May be slow
					int mutationAge = getGameModel().getEchosystem().getClock()-o.getGenetics().getMutation(age, i).getGameTime();
					if(mutationAge<clockLimit) {
						filteredMutationPoints.add(p);

						//g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*o.x)+(BLOCK_SIZE*p.x/2), BLOCK_SIZE + (BLOCK_SIZE*o.y)+(BLOCK_SIZE*p.y/2), BLOCK_SIZE, BLOCK_SIZE);
						if (mutationAge<500) {
							//g.setColor(Color.gray);
						}

					}
				}
			}
			int mbs = Math.min(1, BLOCK_SIZE/3);
			g.setColor(new Color(255,255,255,120));

			if (BLOCK_SIZE>3) {                  
				for (Point p: filteredMutationPoints) { 
					g.fillOval(BLOCK_SIZE + (BLOCK_SIZE*o.x)+(p.x*mbs)-2, BLOCK_SIZE + (BLOCK_SIZE*o.y)+(p.y*mbs)-2, BLOCK_SIZE+4, BLOCK_SIZE+4);
				}
			}
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
	}
}
