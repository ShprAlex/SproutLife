package com.sproutlife.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import com.sproutlife.GameController;

/**
 * Conway's game of life is a cellular automaton devised by the mathematician
 * John Conway.
 */
public class GameFrame extends JFrame {
    private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(800, 600);
    private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(300, 300);

    PanelController panelController;
    JSplitPane splitPane = new JSplitPane();

    private GamePanel gamePanel;
    private ControlPanel controlPanel;

    public GameFrame(PanelController panelController) {

        this.panelController = panelController;

        initFrame();

        setLayout(new BorderLayout(0, 0));

        splitPane = new JSplitPane();
        splitPane.setResizeWeight(1);
        splitPane.setOneTouchExpandable(true);
        add(splitPane);
        
    }
    
    public JSplitPane getSplitPane() {
        return splitPane;
    }

    private void initFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sprout Life - Evolving Game of Life");
        // game.setIconImage(new
        // ImageIcon(ConwaysGameOfLife.class.getResource("/images/logo.png")).getImage());
        setSize(DEFAULT_WINDOW_SIZE);
        setMinimumSize(MINIMUM_WINDOW_SIZE);
        setLocation(
                (Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);

    }

}
