/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;


public class GameFrame extends JFrame {
    private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(1080, 675);
    private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(300, 300);

    PanelController panelController;
    JSplitPane splitPane = new JSplitPane();

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
        setTitle("Sprout Life - Evolving Game of Life - V "+panelController.getGameController().getAppVersion());
        // game.setIconImage(new
        // ImageIcon(ConwaysGameOfLife.class.getResource("/images/logo.png")).getImage());
        setSize(DEFAULT_WINDOW_SIZE);
        setMinimumSize(MINIMUM_WINDOW_SIZE);
        setLocation(
                (Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);

    }

}
