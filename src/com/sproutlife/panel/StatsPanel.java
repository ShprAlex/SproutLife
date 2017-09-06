/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.panel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class StatsPanel extends JPanel {

    PanelController panelController;
    private JTextPane statsTextPane;

    public StatsPanel(PanelController panelController) {
        setMinimumSize(new Dimension(220, 0));
        setPreferredSize(new Dimension(260, 500));

        this.panelController = panelController;
        buildPanel();
    }
    /**
     * Create the panel.
     */
    public void buildPanel() {

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {10, 100, 10};
        gridBagLayout.rowHeights = new int[]{20, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0};
        gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 1;
        gbc_scrollPane.gridy = 1;
        add(scrollPane, gbc_scrollPane);

        statsTextPane = new JTextPane();
        scrollPane.setViewportView(statsTextPane);
        statsTextPane.setText("Start game to see statistics.");

    }


    public JTextPane getStatsTextPane() {
        return statsTextPane;
    }
}
