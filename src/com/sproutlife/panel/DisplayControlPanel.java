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

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class DisplayControlPanel extends JPanel {
	
	PanelController panelController;
	private JPanel panel_2;
	private JCheckBox chckbxCellLayer;
	private JCheckBox chckbxGenomeLayer;
	private JCheckBox chckbxOrgHeadLayer;
	private JCheckBox chckbxOrgTailLayer;
	private JLabel lblDrawLayers;
	private JCheckBox chckbxOutlineSeeds;
	
	private JLabel lblSproutMode;
	private JPanel panel;
	private JRadioButton rdbtnFunctional;
	private JRadioButton rdbtnVisual;
	
	public DisplayControlPanel(PanelController panelController) {
		setMinimumSize(new Dimension(220, 0));	
		setPreferredSize(new Dimension(260, 631));
		        
		this.panelController = panelController;
		buildPanel();
	}
	/**
	 * Create the panel.
	 */
	public void buildPanel() {		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {10, 30, 100, 10};
		gridBagLayout.rowHeights = new int[]{20, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		setLayout(gridBagLayout);
		
		lblDrawLayers = new JLabel("Draw Layers");
		GridBagConstraints gbc_lblDrawLayers = new GridBagConstraints();
		gbc_lblDrawLayers.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblDrawLayers.insets = new Insets(0, 0, 5, 5);
		gbc_lblDrawLayers.gridx = 1;
		gbc_lblDrawLayers.gridy = 1;
		add(lblDrawLayers, gbc_lblDrawLayers);
		
		panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.gridwidth = 2;
		gbc_panel_2.anchor = GridBagConstraints.WEST;
		gbc_panel_2.insets = new Insets(0, 30, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.VERTICAL;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 2;
		add(panel_2, gbc_panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		
		chckbxCellLayer = new JCheckBox("Cell Layer");
		chckbxCellLayer.setSelected(true);
		panel_2.add(chckbxCellLayer);
		
		chckbxGenomeLayer = new JCheckBox("Genome Layer");
		chckbxGenomeLayer.setSelected(true);
		panel_2.add(chckbxGenomeLayer);
		
		chckbxOrgHeadLayer = new JCheckBox("Org Head Layer");
		chckbxOrgHeadLayer.setSelected(true);
		panel_2.add(chckbxOrgHeadLayer);
		
		chckbxOrgTailLayer = new JCheckBox("Org Tail Layer");
		chckbxOrgTailLayer.setSelected(true);
		panel_2.add(chckbxOrgTailLayer);
		
		chckbxOutlineSeeds = new JCheckBox("Outline Seeds & Young");
		panel_2.add(chckbxOutlineSeeds);
		
		
		lblSproutMode = new JLabel("Sprout Mode");
		GridBagConstraints gbc_lblSproutMode = new GridBagConstraints();
		gbc_lblSproutMode.anchor = GridBagConstraints.WEST;
		gbc_lblSproutMode.insets = new Insets(0, 0, 5, 5);
		gbc_lblSproutMode.gridx = 1;
		gbc_lblSproutMode.gridy = 4;
		add(lblSproutMode, gbc_lblSproutMode);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 5;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{30, 40, 40, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		ButtonGroup sproutModeButtonGroup = new ButtonGroup();
		rdbtnFunctional = new JRadioButton("Functional");
		sproutModeButtonGroup.add(rdbtnFunctional);
		rdbtnFunctional.setToolTipText("<html>Seeds are immediately replaced<br>by sprouts before being shown.<br>More efficient for reproduction.</html>");
		rdbtnFunctional.setSelected(true);
		rdbtnFunctional.setMargin(new Insets(2, 2, 2, 13));
		rdbtnFunctional.setAlignmentX(1.0f);
		GridBagConstraints gbc_radioButton = new GridBagConstraints();
		gbc_radioButton.anchor = GridBagConstraints.WEST;
		gbc_radioButton.insets = new Insets(0, 0, 0, 5);
		gbc_radioButton.gridx = 1;
		gbc_radioButton.gridy = 0;
		panel.add(rdbtnFunctional, gbc_radioButton);
		
		rdbtnVisual = new JRadioButton("Visual");
		sproutModeButtonGroup.add(rdbtnVisual);
		rdbtnVisual.setToolTipText("<html>Seeds are displayed but an<br>extra step is added to show them.</html>");
		rdbtnVisual.setMargin(new Insets(2, 2, 2, 11));
		GridBagConstraints gbc_rdbtnVisual = new GridBagConstraints();
		gbc_rdbtnVisual.anchor = GridBagConstraints.WEST;
		gbc_rdbtnVisual.gridx = 2;
		gbc_rdbtnVisual.gridy = 0;
		panel.add(rdbtnVisual, gbc_rdbtnVisual);

		

	}

    public JCheckBox getChckbxCellLayer() {
        return chckbxCellLayer;
    }
    public JCheckBox getChckbxGenomeLayer() {
        return chckbxGenomeLayer;
    }
    public JCheckBox getChckbxOrgHeadLayer() {
        return chckbxOrgHeadLayer;
    }
    public JCheckBox getChckbxOrgTailLayer() {
        return chckbxOrgTailLayer;
    }
    public JCheckBox getChckbxOutlineSeeds() {
        return chckbxOutlineSeeds;
    }
    
    public JRadioButton getRdbtnVisual() {
        return rdbtnVisual;       
    }
    public JRadioButton getRdbtnFunctional() {
        return rdbtnFunctional;
    }
}
