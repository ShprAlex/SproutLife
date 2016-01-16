package com.sproutlife.view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import com.sproutlife.GameController;
import com.sproutlife.model.LifeStep;

/**
 * Conway's game of life is a cellular automaton devised by the
 * mathematician John Conway.
 */
public class GameFrame extends JFrame  {
    private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(800, 600);
    private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(300, 300);

    GameController controller;
  
    private GamePanel gamePanel;
    
    public GameFrame(GameController controller) {
        
        this.controller = controller;
        
        initFrame();               
 
        JMenuBar menu = new GameMenu(controller);
        setJMenuBar(menu);
        
        gamePanel = new GamePanel(controller);
        
        add(gamePanel);       

    }
    
    public GamePanel getGamePanel() {
        return gamePanel;
    }
    
    private void initFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sprout Life - Evolving Game of Life");
        //game.setIconImage(new ImageIcon(ConwaysGameOfLife.class.getResource("/images/logo.png")).getImage());
        setSize(DEFAULT_WINDOW_SIZE);
        setMinimumSize(MINIMUM_WINDOW_SIZE);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth())/2, 
                (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight())/2);
        
    }
    
    
        
}

