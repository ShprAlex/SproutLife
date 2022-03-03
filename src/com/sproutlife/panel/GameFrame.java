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

import com.sproutlife.SproutLife;


public class GameFrame extends JFrame {
    private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(1292, 641);
    private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(300, 300);

    PanelController panelController;

    public GameFrame(PanelController panelController) {
        this.panelController = panelController;
        initFrame();
        setLayout(new BorderLayout(0, 0));
    }

    private void initFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SproutLife - Evolving Game of Life - V " + SproutLife.getAppVersion());

        setSize(DEFAULT_WINDOW_SIZE);
        setMinimumSize(MINIMUM_WINDOW_SIZE);
        setLocation(
                (Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);

    }

}
