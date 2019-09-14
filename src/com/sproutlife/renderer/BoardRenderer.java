/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.renderer.colors.ColorModel;
import com.sproutlife.renderer.colors.DefaultColorModel;

public class BoardRenderer {
    
    private int BLOCK_SIZE;  
    private int defaultBlockSize = 3;  

    private GameModel gameModel;
    private ColorModel colorModel;
    
    private CellRenderer cellRenderer;
    private HeadRenderer headRenderer;
    private TailRenderer tailRenderer;
    private GenomeRenderer genomeRenderer;
    
    private boolean paintCellLayer;
    private boolean paintHeadLayer;
    private boolean paintTailLayer;
    private boolean paintGenomeLayer;
    private boolean outlineSeeds;
    
    private AffineTransform transform;
    
    Rectangle2D.Double bounds;
    Rectangle2D.Double visibleBounds;
        
    private double zoom = 1;      

    public BoardRenderer(GameModel gameModel) {
        
        this.BLOCK_SIZE = this.defaultBlockSize;
        this.gameModel = gameModel;

        this.colorModel = new DefaultColorModel();

        this.cellRenderer = new CellRenderer(gameModel, this);
        this.headRenderer = new HeadRenderer(gameModel, this);
        this.tailRenderer = new TailRenderer(gameModel, this);
        this.genomeRenderer = new GenomeRenderer(gameModel, this);

        this.paintCellLayer = true;
        this.paintHeadLayer = true;
        this.paintTailLayer = true;
        this.paintGenomeLayer = true;
        this.outlineSeeds = false;

        this.transform = new AffineTransform();        
    } 
    
    public void paint(Graphics2D g) {               
        
        if (getZoom()!=1) {
            g.setTransform(transform);
        }

        paintBackground(g);             
        
        if (getPaintHeadLayer()) {
            paintOrgHeads(g);
        }
        if (getPaintTailLayer()) {
            paintOrgTails(g);
        }
        
        if (getPaintCellLayer()) {
            paintCells(g);
        }
        
        if (getPaintGenomeLayer()) {
            paintGenomes(g);
        }
    }   
    
    public GameModel getGameModel() {
        return gameModel;
    }       
    
    public ColorModel getColorModel() {
		return colorModel;
	}

    public void setPaintCellLayer(boolean paint) {
        this.paintCellLayer = paint;
    }
    
    public boolean getPaintCellLayer() {
        return paintCellLayer;
    }
    
    public void setPaintHeadLayer(boolean paint) {
        this.paintHeadLayer = paint;
    }
    
    public boolean getPaintHeadLayer() {
        return paintHeadLayer;
    }
    
    public void setPaintTailLayer(boolean paint) {
        this.paintTailLayer = paint;
    }
    
    public boolean getPaintTailLayer() {
        return paintTailLayer;
    }
    
    public void setPaintGenomeLayer(boolean paint) {
        this.paintGenomeLayer = paint;
    }
    
    public boolean getPaintGenomeLayer() {
        return paintGenomeLayer;
    }    
    
    public void setOutlineSeeds(boolean outlineSeeds) {
        this.outlineSeeds = outlineSeeds;
    }
    
    public boolean getOutlineSeeds() {
        return outlineSeeds;
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
        this.visibleBounds = visibleBounds;
    }
    
    public void setBounds(Dimension d) {
        this.bounds = new Rectangle2D.Double(0,0,d.getWidth(),d.getHeight());
    }
    
    public Rectangle2D.Double getRendererBounds() {
        if (bounds==null) {
            return null;
        }
        
        double x = bounds.x - 20 * BLOCK_SIZE / getDefaultBlockSize();
        double y = bounds.y - 20 * BLOCK_SIZE / getDefaultBlockSize();
        double w = bounds.width * BLOCK_SIZE / getDefaultBlockSize();
        double h = bounds.height * BLOCK_SIZE / getDefaultBlockSize();
        
        return new Rectangle2D.Double(x,y,w,h);        
    }
      
    public double getZoom() {
        return zoom;
    }
    
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    private void paintBackground(Graphics g) {      
        g.setColor(new Color(255,255,255));
        int x = (int) getRendererBounds().x;
        int y = (int) getRendererBounds().y;
        int w = (int) getRendererBounds().width;
        int h = (int) getRendererBounds().height;
        g.fillRect(x, y, w, h);
    }

    private void paintCells(Graphics2D g) {
        Collection<Organism> orgs = getGameModel().getEchosystem().getOrganisms();
        for (Organism o : orgs) {
            cellRenderer.paintCells(g, o);
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
            genomeRenderer.paintGenome(g,o);
        }
    }
}
