/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sproutlife.GameController;
import com.sproutlife.Settings;

import javax.swing.JSlider;
import javax.swing.JRadioButton;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class GameToolbar extends JPanel {

    PanelController panelController;
    private JSlider zoomSlider;
    private JButton startPauseButton;
    private JSlider speedSlider;
    private Component horizontalStrut;
    private Component horizontalStrut_1;
    private JButton stepButton;
    private JButton resetButton;


    public GameToolbar(PanelController panelController) {
        setMinimumSize(new Dimension(220, 0));

        this.panelController = panelController;
        buildPanel();
    }
    /**
     * Create the panel.
     */
    public void buildPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        horizontalStrut = Box.createHorizontalStrut(20);
        add(horizontalStrut);

        JLabel lblZoom = new JLabel("Zoom");
        add(lblZoom);
        //lblZoom.setMinimumSize(new Dimension(100, 14));

        zoomSlider = new JSlider();
        zoomSlider.setPreferredSize(new Dimension(100, 29));
        add(zoomSlider);
        zoomSlider.setMinorTickSpacing(1);
        zoomSlider.setMinimum(-5);
        zoomSlider.setValue(-2);
        zoomSlider.setMaximum(5);

        JLabel speedLabel = new JLabel("Speed");
        add(speedLabel);

        speedSlider = new JSlider();
        speedSlider.setPreferredSize(new Dimension(100, 29));
        speedSlider.setSnapToTicks(true);
        add(speedSlider);
        speedSlider.setMinimum(-5);
        speedSlider.setMaximum(4);
        speedSlider.setValue(-2);

        startPauseButton = new JButton("Start");
        add(startPauseButton);
        startPauseButton.setMaximumSize(new Dimension(200, 23));
        startPauseButton.setPreferredSize(new Dimension(80, 29));

        stepButton = new JButton("Step");
        stepButton.setPreferredSize(new Dimension(80, 29));
        add(stepButton);

        resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(80, 29));
        add(resetButton);

        horizontalStrut_1 = Box.createHorizontalStrut(20);
        add(horizontalStrut_1);

    }

    public int getInt(String s) {
        return panelController.getGameController().getSettings().getInt(s);
    }

    public JSlider getZoomSlider() {
        return zoomSlider;
    }

    public JButton getStartPauseButton() {
        return startPauseButton;
    }

    public JSlider getSpeedSlider() {
        return speedSlider;
    }
    public JButton getStepButton() {
        return stepButton;
    }
    public JButton getResetButton() {
        return resetButton;
    }
}
