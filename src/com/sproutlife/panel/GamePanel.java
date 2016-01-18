package com.sproutlife.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;

import com.sproutlife.GameController;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Board;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.step.GameStep;
import com.sproutlife.renderer.BoardRenderer;


public class GamePanel extends JPanel implements ComponentListener, MouseListener, MouseMotionListener {
    public static final int BLOCK_SIZE = 3;
  
    GameController controller;
    BoardRenderer boardRenderer;
    
    public GamePanel(GameController controller) {
        // Add resizing listener
        this.controller = controller;
        this.boardRenderer = new BoardRenderer(getGameModel(), this);
        
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
    
    private BoardRenderer getBoardRenderer() {
    	return boardRenderer;
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
       
        
        synchronized (getGameModel().getEchosystem()) {
        	try {
        		getBoardRenderer().paint((Graphics2D) g);

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
    	synchronized (getGameModel().getEchosystem()) {
    		getGameModel().getEchosystem().setBoardSize(new Dimension(getWidth()/BLOCK_SIZE-2, getHeight()/BLOCK_SIZE-2));
    	}
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
