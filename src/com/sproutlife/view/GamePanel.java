package com.sproutlife.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;

import com.sproutlife.GameController;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.GameStep;
import com.sproutlife.model.LifeStep;
import com.sproutlife.model.echosystem.Board;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;


public class GamePanel extends JPanel implements ComponentListener, MouseListener, MouseMotionListener {
    private static final int BLOCK_SIZE = 3;
  
    GameController controller;
    
    public GamePanel(GameController controller) {
        // Add resizing listener
        this.controller = controller;
        
        addComponentListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }
     
    private GameModel getGameModel() {
        return controller.getGameModel();
    }
    
    private GameStep getGameStep() {
        return getGameModel().getGameStep();
    }
    
    private Board getBoard() {
        return getGameModel().getBoard();
    }

    /*
    private void updateArraySize() {
        gameBoard.updateArraySize();

        repaint();
    }
    */
    
    public void addCell(int x, int y) {
        /*
        if (x>getSproutLife().getBoard().getWidth() || y>getSproutLife().getBoard().getHeight()) {
            getSproutLife().getEchosystem().resetCells();
        }
        */
        if (getGameModel().getBoard().getCell(x, y)==null) {
            Cell c = getGameModel().getEchosystem().addCell(x, y);
            //getGameModel().getBoard().setCell(c);
        }
        else {
            getGameModel().getEchosystem().removeCell(x, y);
            //getGameModel().getBoard().clearCell(x, y);
        }
        
        repaint();
    }
    
    public void addCell(MouseEvent me) {
        
        int x = me.getPoint().x/BLOCK_SIZE-1;
        int y = me.getPoint().y/BLOCK_SIZE-1;
        
        if ((x >= 0) && (x < getBoard().getWidth()) && (y >= 0) && (y < getBoard().getHeight())) {
            addCell(x,y);
        }
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        /*
        AffineTransform t = new AffineTransform();
        t.setToScale(2, 2);
        ((Graphics2D) g).setTransform(t);
        */
        g.setColor(new Color(248,248,248));
        g.fillRect(0,0,getWidth(),getHeight());
        
        synchronized (getGameModel().getEchosystem()) {
        try {
            ArrayList<Cell> cells = getGameModel().getEchosystem().getCells();
            for (Cell newCell : cells) {
                // Draw new point
                
                int red = 155;
                int green = 0;
                int blue = 0;

                int age = getGameModel().getClock()-newCell.getOrganism().born;
                if(newCell.getOrganism()!=null) {
                    red = newCell.getOrganism().getKind()%3==0?255:120;
                    green = newCell.getOrganism().getKind()%3==1?255:120;
                    blue = newCell.getOrganism().getKind()%3==2?255:120;
                }
              
                g.setColor(new Color(red,green,blue));
                g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*newCell.x), BLOCK_SIZE + (BLOCK_SIZE*newCell.y), BLOCK_SIZE, BLOCK_SIZE);
            } 
            
            
            Collection<Organism> orgs = getGameModel().getEchosystem().getOrganisms();
            /*
            Collection<Organism> orgs = new ArrayList<Organism>();            
            Organism firstOrg = getGameModel().getEchosystem().getOrganisms().iterator().next();
            orgs.add(firstOrg);
            orgs.addAll(firstOrg.getChildren());
            */
            
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
                

                
                /*
                if (o.bornFromInfected) {
                    g.setColor(Color.black);
                    g.drawRect(BLOCK_SIZE + (BLOCK_SIZE*o.x)-rectSize/2, BLOCK_SIZE + (BLOCK_SIZE*o.y)-rectSize/2, rectSize, rectSize);
                }
                */
               
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
                ArrayList<Point> filteredMutationPoints = new ArrayList<Point>();
                for (int age = 0;age<50;age++) {
                    ArrayList<Point> mutationPoints = o.getGenetics().getMutationPoints( age);
                    int clockLimit = 15000;//Math.max(10000,getGameModel().getClock()/3);

                    for (int i = 0;i<mutationPoints.size();i++) {
                        Point p = mutationPoints.get(i);
                        //May be slow
                        int mutationAge = getGameModel().getClock()-o.getGenetics().getMutation(age, i).getGameTime();
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
            
            /*
            for (Organism o : orgs) {
                
                g.setColor(Color.black);
                int rectSize = BLOCK_SIZE;
                g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*o.x), BLOCK_SIZE + (BLOCK_SIZE*o.y), rectSize, rectSize);               
            }
            */
            
        } catch (ConcurrentModificationException cme) {}
        }
        // Setup grid
        /*
        g.setColor(Color.BLACK);
        for (int i=0; i<=d_gameBoardSize.width; i++) {
            g.drawLine(((i*BLOCK_SIZE)+BLOCK_SIZE), BLOCK_SIZE, (i*BLOCK_SIZE)+BLOCK_SIZE, BLOCK_SIZE + (BLOCK_SIZE*d_gameBoardSize.height));
        }
        for (int i=0; i<=d_gameBoardSize.height; i++) {
            g.drawLine(BLOCK_SIZE, ((i*BLOCK_SIZE)+BLOCK_SIZE), BLOCK_SIZE*(d_gameBoardSize.width+1), ((i*BLOCK_SIZE)+BLOCK_SIZE));
        }
        */
    }

    @Override
    public void componentResized(ComponentEvent e) {
        // Setup the game board size with proper boundries
        getBoard().setBoardSize(new Dimension(getWidth()/BLOCK_SIZE-2, getHeight()/BLOCK_SIZE-2));
        
        //updateArraySize();
        
        repaint();
    }
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {
        // Mouse was released (user clicked)
        addCell(e);
    }
    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        // Mouse is being dragged, user wants multiple selections
        addCell(e);
    }
    @Override
    public void mouseMoved(MouseEvent e) {}
    
}
