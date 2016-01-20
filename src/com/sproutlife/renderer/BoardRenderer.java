package com.sproutlife.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.panel.GamePanel;

public class BoardRenderer extends Renderer {
    
    public static final int BLOCK_SIZE = 4;      

    private GamePanel gamePanel;
    
    private CellRenderer cellRenderer;
    private HeadRenderer headRenderer;
    private TailRenderer tailRenderer;
    private GenomeRenderer genomeRenderer;
    
    private double zoom = 1;

    public BoardRenderer(GameModel gameModel, GamePanel gamePanel) {
        super(gameModel);

        this.gamePanel = gamePanel;
        this.cellRenderer = new CellRenderer(gameModel);
        this.headRenderer = new HeadRenderer(gameModel);
        this.tailRenderer = new TailRenderer(gameModel);
        this.genomeRenderer = new GenomeRenderer(gameModel);
    }

    private GamePanel getGamePanel() {
        return gamePanel;
    }

    public void paint(Graphics2D g) {		
        
        if (getZoom()!=1) {
                        
            AffineTransform t = new AffineTransform();
            int width = getGamePanel().getWidth();
            int height = getGamePanel().getHeight();
            double zoom = getZoom();
            
            double xoffset = (width - width * zoom)/2;
            double yoffset = (height - height * zoom)/2;
           
            t.translate(xoffset, yoffset);
            t.scale(getZoom(), getZoom());
            
            
            g.setTransform(t);
        }

        paintBackground(g);  		

        paintOrgHeads(g);

        paintOrgTails(g);
        
        paintCells(g);

        paintGenomes(g);
    }
    
    public double getZoom() {
        return zoom;
    }
    
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    private void paintBackground(Graphics g) {

        g.setColor(new Color(248,248,248));
        g.fillRect(0,0,getGamePanel().getWidth(),getGamePanel().getHeight());
    }


    private void paintCells(Graphics2D g) {	

        ArrayList<Cell> cells = getGameModel().getEchosystem().getCells();
        for (Cell c : cells) {
            cellRenderer.paintCell(g, c);			
        } 
    }

    public void paintOrgHeads(Graphics2D g) {

        Collection<Organism> orgs = getGameModel().getEchosystem().getOrganisms();

        for (Organism o : orgs) {
            headRenderer.paintHead(g, o);
        }
    }

    public void paintOrgTails(Graphics2D g) {

        Collection<Organism> orgs = getGameModel().getEchosystem().getOrganisms();

        for (Organism o : orgs) {
            tailRenderer.paintTail(g, o);
        }		
    }

    private void paintGenomes(Graphics2D g) {

        Collection<Organism> orgs = getGameModel().getEchosystem().getOrganisms();

        for (Organism o : orgs) {
            genomeRenderer.paintGenome(g, o);
        }
    }
}
