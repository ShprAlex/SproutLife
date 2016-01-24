package com.sproutlife.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.panel.gamepanel.GamePanel;

public class BoardRenderer {
    
    private int BLOCK_SIZE;  
    private int defaultBlockSize = 3;  

    private GameModel gameModel;
    
    private CellRenderer cellRenderer;
    private HeadRenderer headRenderer;
    private TailRenderer tailRenderer;
    private GenomeRenderer genomeRenderer;
    
    private AffineTransform transform;
    
    Rectangle2D.Double bounds;
    Rectangle2D.Double visibleBounds;
        
    private double zoom = 1;

    public BoardRenderer(GameModel gameModel) {
        
        this.BLOCK_SIZE = this.defaultBlockSize;
        this.gameModel = gameModel;
   
        this.cellRenderer = new CellRenderer(gameModel, this);
        this.headRenderer = new HeadRenderer(gameModel, this);
        this.tailRenderer = new TailRenderer(gameModel, this);
        this.genomeRenderer = new GenomeRenderer(gameModel, this);
        
        transform = new AffineTransform();
    } 
    
    public GameModel getGameModel() {
        return gameModel;
    }       
    
    public AffineTransform getTransform() {
        return transform;
    }
    
    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }
    
    public int getBlockSize() {
        return BLOCK_SIZE;
    }
    
    public void setBlockSize(int blockSize) {
        this.BLOCK_SIZE = blockSize;   
    }
    
    public int getDefaultBlockSize() {
        return defaultBlockSize;
    }
    
    public void setDefaultBlockSize(int defaultBlockSize) {
        this.defaultBlockSize = defaultBlockSize;
    }
    
    public void updateVisibleRenderers(Rectangle2D.Double visibleBounds) {
        //synchronized(graph) {
            this.visibleBounds = visibleBounds;
    }
    
    public void setBounds(Dimension d) {
        this.bounds = new Rectangle2D.Double(0,0,d.getWidth(),d.getHeight());
    }
    
    public Rectangle2D.Double getRendererBounds() {
        if (bounds==null) {
            return null;
        }
        
        double x = bounds.x;
        double y = bounds.y;
        double w = bounds.width * BLOCK_SIZE/getDefaultBlockSize();
        double h = bounds.height * BLOCK_SIZE/getDefaultBlockSize();
        
        //Can dynamically compute bounds
        //return new Rectangle2D.Double(0,0,gamePanel.getWidth(),gamePanel.getHeight());
        return new Rectangle2D.Double(x,y,w,h);        
    }
      
    public void paint(Graphics2D g) {		
        
        if (getZoom()!=1) {
            /*            
            AffineTransform t = new AffineTransform();
            int width = getGamePanel().getWidth();
            int height = getGamePanel().getHeight();
            double zoom = getZoom();
            
            double xoffset = (width - width * zoom)/2;
            double yoffset = (height - height * zoom)/2;
           
            t.translate(xoffset, yoffset);
            t.scale(getZoom(), getZoom());
            */
            
            g.setTransform(transform);
        }

        //paintBackground(g);  		

        paintOrgHeads(g);

        paintOrgTails(g);
        
        paintCells(g);

        paintGenomes(g);
    }
    
    public double getZoom() {
        return zoom;
    }
    
    public void setZoom(double zoom) {
        //transform = new AffineTransform();
        //int width = getGamePanel().getWidth();
        //int height = getGamePanel().getHeight();
        //double zoom = getZoom();
        
        //double xoffset = (width - width * zoom)/2;
        //double yoffset = (height - height * zoom)/2;
       
        //t.translate(xoffset, yoffset);
        //transform.scale(getZoom(), getZoom());
        this.zoom = zoom;
    }

    private void paintBackground(Graphics g) {
        
        g.setColor(new Color(248,248,248));
        int x = (int) getRendererBounds().x;
        int y = (int) getRendererBounds().y;
        int w = (int) getRendererBounds().width;
        int h = (int) getRendererBounds().height;
        g.fillRect(x, y, w, h);
        
        g.setColor(Color.black);
        g.drawRect(x, y, w, h);

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
