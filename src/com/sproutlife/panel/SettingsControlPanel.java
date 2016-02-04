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

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sproutlife.Settings;

public class SettingsControlPanel extends JPanel {
	
	PanelController panelController;
	private JSpinner maxLifespanSpinner;
	private JSpinner childOneEnergySpinner;
	private JSpinner childTwoEnergySpinner;
	private JSpinner childThreeEnergySpinner;
	
	public SettingsControlPanel(PanelController panelController) {
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
		gridBagLayout.columnWidths = new int[] {10, 0, 100, 100, 80, 10};
		gridBagLayout.rowHeights = new int[]{20, 0, 15, 20, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		childOneEnergySpinner = new JSpinner();
		childOneEnergySpinner.setPreferredSize(new Dimension(50, 20));

		ButtonGroup lifeModeButtonGroup = new ButtonGroup();
		
		JLabel lblMaxLifespan = new JLabel("Max lifespan");
		GridBagConstraints gbc_lblMaxLifespan = new GridBagConstraints();
		gbc_lblMaxLifespan.anchor = GridBagConstraints.WEST;
		gbc_lblMaxLifespan.gridwidth = 2;
		gbc_lblMaxLifespan.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaxLifespan.gridx = 2;
		gbc_lblMaxLifespan.gridy = 1;
		add(lblMaxLifespan, gbc_lblMaxLifespan);
		
		maxLifespanSpinner = new JSpinner();
		maxLifespanSpinner.setPreferredSize(new Dimension(50, 20));
		GridBagConstraints gbc_maxLifespanSpinner = new GridBagConstraints();
		gbc_maxLifespanSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_maxLifespanSpinner.anchor = GridBagConstraints.NORTHEAST;
		gbc_maxLifespanSpinner.insets = new Insets(0, 0, 5, 5);
		gbc_maxLifespanSpinner.gridx = 4;
		gbc_maxLifespanSpinner.gridy = 1;
		add(maxLifespanSpinner, gbc_maxLifespanSpinner);
		
		JLabel childOneEnergyLabel = new JLabel("Min childbearing age");
		GridBagConstraints gbc_childOneEnergyLabel = new GridBagConstraints();
		gbc_childOneEnergyLabel.gridwidth = 2;
		gbc_childOneEnergyLabel.anchor = GridBagConstraints.WEST;
		gbc_childOneEnergyLabel.insets = new Insets(0, 0, 5, 5);
		gbc_childOneEnergyLabel.gridx = 2;
		gbc_childOneEnergyLabel.gridy = 3;
		add(childOneEnergyLabel, gbc_childOneEnergyLabel);
		GridBagConstraints gbc_childOneEnergySpinner = new GridBagConstraints();
		gbc_childOneEnergySpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_childOneEnergySpinner.anchor = GridBagConstraints.NORTHEAST;
		gbc_childOneEnergySpinner.insets = new Insets(0, 0, 5, 5);
		gbc_childOneEnergySpinner.gridx = 4;
		gbc_childOneEnergySpinner.gridy = 3;
		add(childOneEnergySpinner, gbc_childOneEnergySpinner);
		
		childTwoEnergySpinner = new JSpinner();
		childTwoEnergySpinner.setPreferredSize(new Dimension(50, 20));
		
		JLabel childTwoEnergyLabel = new JLabel("Min 1st - 2nd child age");
		GridBagConstraints gbc_childTwoEnergyLabel = new GridBagConstraints();
		gbc_childTwoEnergyLabel.gridwidth = 2;
		gbc_childTwoEnergyLabel.anchor = GridBagConstraints.WEST;
		gbc_childTwoEnergyLabel.insets = new Insets(0, 0, 5, 5);
		gbc_childTwoEnergyLabel.gridx = 2;
		gbc_childTwoEnergyLabel.gridy = 4;
		add(childTwoEnergyLabel, gbc_childTwoEnergyLabel);
		GridBagConstraints gbc_childTwoEnergySpinner = new GridBagConstraints();
		gbc_childTwoEnergySpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_childTwoEnergySpinner.anchor = GridBagConstraints.NORTHEAST;
		gbc_childTwoEnergySpinner.insets = new Insets(0, 0, 5, 5);
		gbc_childTwoEnergySpinner.gridx = 4;
		gbc_childTwoEnergySpinner.gridy = 4;
		add(childTwoEnergySpinner, gbc_childTwoEnergySpinner);
		
		childThreeEnergySpinner = new JSpinner();
		childThreeEnergySpinner.setPreferredSize(new Dimension(50, 20));
		
		JLabel childThreeEnergyLabel = new JLabel("Min age btw ++ children");		
		GridBagConstraints gbc_childThreeEnergyLabel = new GridBagConstraints();
		gbc_childThreeEnergyLabel.gridwidth = 2;
		gbc_childThreeEnergyLabel.anchor = GridBagConstraints.WEST;
		gbc_childThreeEnergyLabel.insets = new Insets(0, 0, 0, 5);
		gbc_childThreeEnergyLabel.gridx = 2;
		gbc_childThreeEnergyLabel.gridy = 5;
		add(childThreeEnergyLabel, gbc_childThreeEnergyLabel);
		GridBagConstraints gbc_childThreeEnergySpinner = new GridBagConstraints();
		gbc_childThreeEnergySpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_childThreeEnergySpinner.insets = new Insets(0, 0, 0, 5);
		gbc_childThreeEnergySpinner.anchor = GridBagConstraints.NORTHEAST;
		gbc_childThreeEnergySpinner.gridx = 4;
		gbc_childThreeEnergySpinner.gridy = 5;
		add(childThreeEnergySpinner, gbc_childThreeEnergySpinner);

	}

    public JSpinner getMaxLifespanSpinner() {
        return maxLifespanSpinner;
    }

    public JSpinner getChildOneEnergySpinner() {
        return childOneEnergySpinner;
    }
    public JSpinner getChildTwoEnergySpinner() {
        return childTwoEnergySpinner;
    }
    public JSpinner getChildThreeEnergySpinner() {
        return childThreeEnergySpinner;
    }
}
