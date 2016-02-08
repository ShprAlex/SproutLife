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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sproutlife.GameController;
import com.sproutlife.Settings;

import javax.swing.JSlider;
import javax.swing.JRadioButton;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class MainControlPanel extends JPanel {
	
	PanelController panelController;
	private JSlider zoomSlider;
	private JRadioButton rdbtnFriendly;
	private JRadioButton rdbtnCompetitive;
	private JButton startPauseButton;
	private JButton stepButton;
	private JSlider speedSlider;
	private JButton resetButton;
	private JRadioButton rdbtnCooperative;
	private JSpinner boardWidthSpinner;
	private JSpinner boardHeightSpinner;
	private JCheckBox autoSizeGridCheckbox;
	private JButton clipGridToViewButton;
	private JLabel imageWidthHeightLabel;
	private JLabel lblSeedType;
	private JComboBox seedTypeComboBox;

	
	public MainControlPanel(PanelController panelController) {
		setMinimumSize(new Dimension(220, 0));	
		setPreferredSize(new Dimension(280, 631));
		        
		this.panelController = panelController;
		buildPanel();
	}
	/**
	 * Create the panel.
	 */
	public void buildPanel() {		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {10, 0, 100, 100, 80, 10};
		gridBagLayout.rowHeights = new int[]{20, 0, 15, 0, 0, 15, 0, 0, 0, 15, 0, 0, 15, 0, 31, 15, 0, 15, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 1.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.NORTH;
		gbc_panel_1.gridwidth = 3;
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.gridx = 2;
		gbc_panel_1.gridy = 1;
		add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();		
		gbl_panel_1.columnWidths = new int[]{0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 23};
		gbl_panel_1.columnWeights = new double[]{1.0, 1.0};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0};
		panel_1.setLayout(gbl_panel_1);
		
		startPauseButton = new JButton("Start");
		startPauseButton.setMaximumSize(new Dimension(200, 23));
		startPauseButton.setPreferredSize(new Dimension(100, 23));
		GridBagConstraints gbc_startPauseButton = new GridBagConstraints();
		gbc_startPauseButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_startPauseButton.insets = new Insets(0, 0, 5, 5);
		gbc_startPauseButton.gridx = 0;
		gbc_startPauseButton.gridy = 0;
		panel_1.add(startPauseButton, gbc_startPauseButton);
		
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    }
		});
		GridBagConstraints gbc_resetButton = new GridBagConstraints();
		gbc_resetButton.insets = new Insets(0, 0, 5, 0);
		gbc_resetButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_resetButton.gridx = 1;
		gbc_resetButton.gridy = 0;
		panel_1.add(resetButton, gbc_resetButton);
		resetButton.setPreferredSize(new Dimension(100, 23));
		
		stepButton = new JButton("Step");
		GridBagConstraints gbc_stepButton = new GridBagConstraints();
		gbc_stepButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_stepButton.insets = new Insets(0, 0, 0, 5);
		gbc_stepButton.gridx = 0;
		gbc_stepButton.gridy = 1;
		panel_1.add(stepButton, gbc_stepButton);
		stepButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		    }
		});
		stepButton.setPreferredSize(new Dimension(100, 23));
		
		JLabel lblZoom = new JLabel("Zoom");
		lblZoom.setMinimumSize(new Dimension(100, 14));
		GridBagConstraints gbc_lblZoom = new GridBagConstraints();
		gbc_lblZoom.anchor = GridBagConstraints.WEST;
		gbc_lblZoom.insets = new Insets(0, 0, 5, 5);
		gbc_lblZoom.gridx = 2;
		gbc_lblZoom.gridy = 3;
		add(lblZoom, gbc_lblZoom);
		
		zoomSlider = new JSlider();
		zoomSlider.setMinorTickSpacing(1);
		zoomSlider.setMinimum(-5);
		zoomSlider.setValue(-2);
		zoomSlider.setMaximum(5);
		GridBagConstraints gbc_zoomSlider = new GridBagConstraints();
		gbc_zoomSlider.fill = GridBagConstraints.HORIZONTAL;
		gbc_zoomSlider.gridwidth = 2;
		gbc_zoomSlider.insets = new Insets(0, 0, 5, 5);
		gbc_zoomSlider.gridx = 3;
		gbc_zoomSlider.gridy = 3;
		add(zoomSlider, gbc_zoomSlider);
		
		JLabel speedLabel = new JLabel("Speed");
		GridBagConstraints gbc_speedLabel = new GridBagConstraints();
		gbc_speedLabel.anchor = GridBagConstraints.WEST;
		gbc_speedLabel.insets = new Insets(0, 0, 5, 5);
		gbc_speedLabel.gridx = 2;
		gbc_speedLabel.gridy = 4;
		add(speedLabel, gbc_speedLabel);
		
		speedSlider = new JSlider();		
		speedSlider.setMinimum(-5);
		speedSlider.setMaximum(4);
		speedSlider.setValue(-2);
		GridBagConstraints gbc_speedSlider = new GridBagConstraints();
		gbc_speedSlider.fill = GridBagConstraints.HORIZONTAL;
		gbc_speedSlider.gridwidth = 2;
		gbc_speedSlider.insets = new Insets(0, 0, 5, 5);
		gbc_speedSlider.gridx = 3;
		gbc_speedSlider.gridy = 4;
		add(speedSlider, gbc_speedSlider);
		
		JPanel lifeModePanel = new JPanel();
		ButtonGroup lifeModeButtonGroup = new ButtonGroup();
		
		lblSeedType = new JLabel("Seed Type");
		GridBagConstraints gbc_lblSeedType = new GridBagConstraints();
		gbc_lblSeedType.anchor = GridBagConstraints.WEST;
		gbc_lblSeedType.insets = new Insets(0, 0, 5, 5);
		gbc_lblSeedType.gridx = 2;
		gbc_lblSeedType.gridy = 6;
		add(lblSeedType, gbc_lblSeedType);
		
		seedTypeComboBox = new JComboBox();
		GridBagConstraints gbc_seedTypeComboBox = new GridBagConstraints();
		gbc_seedTypeComboBox.gridwidth = 2;
		gbc_seedTypeComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_seedTypeComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_seedTypeComboBox.gridx = 3;
		gbc_seedTypeComboBox.gridy = 6;
		add(seedTypeComboBox, gbc_seedTypeComboBox);		
		
		
		JLabel lblLifeMode = new JLabel("Collision Mode");
		GridBagConstraints gbc_lblLifeMode = new GridBagConstraints();
		gbc_lblLifeMode.gridwidth = 2;
		gbc_lblLifeMode.anchor = GridBagConstraints.WEST;
		gbc_lblLifeMode.insets = new Insets(0, 0, 5, 5);
		gbc_lblLifeMode.gridx = 2;
		gbc_lblLifeMode.gridy = 10;
		add(lblLifeMode, gbc_lblLifeMode);
		GridBagConstraints gbc_lifeModePanel = new GridBagConstraints();
		gbc_lifeModePanel.gridwidth = 3;
		gbc_lifeModePanel.anchor = GridBagConstraints.NORTH;
		gbc_lifeModePanel.insets = new Insets(0, 0, 5, 5);
		gbc_lifeModePanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lifeModePanel.gridx = 2;
		gbc_lifeModePanel.gridy = 11;
		add(lifeModePanel, gbc_lifeModePanel);
		GridBagLayout gbl_lifeModePanel = new GridBagLayout();
		gbl_lifeModePanel.columnWidths = new int[]{30, 40, 40};
		gbl_lifeModePanel.rowHeights = new int[]{23, 0};
		gbl_lifeModePanel.columnWeights = new double[]{1.0, 0.0, 0.0};
		gbl_lifeModePanel.rowWeights = new double[]{0.0, 0.0};
		lifeModePanel.setLayout(gbl_lifeModePanel);
		
		rdbtnCooperative = new JRadioButton("Cooperative");
		rdbtnCooperative.setToolTipText("<html>"+
		        "Tends towards order and<br>"+
		        "synchronization. Cells<br>"+
		        "don't differentiate between<br>"+
		        "their own organism and others</html>");
		GridBagConstraints gbc_rdbtnCooperative = new GridBagConstraints();
		gbc_rdbtnCooperative.anchor = GridBagConstraints.WEST;
		gbc_rdbtnCooperative.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnCooperative.gridx = 1;
		gbc_rdbtnCooperative.gridy = 0;
		lifeModePanel.add(rdbtnCooperative, gbc_rdbtnCooperative);
		lifeModeButtonGroup.add(rdbtnCooperative);
		
		rdbtnFriendly = new JRadioButton("Friendly");
		rdbtnFriendly.setToolTipText("<html>"+
		        "Cells know which one is their<br>"+
		        "organism, but tolerate some<br>"+
		        "contact from other organisms.<br>"+
		        "Maintains complexity."+
		        "</html>");
		rdbtnFriendly.setAlignmentX(Component.RIGHT_ALIGNMENT);
		GridBagConstraints gbc_rdbtnFriendly = new GridBagConstraints();
		gbc_rdbtnFriendly.anchor = GridBagConstraints.WEST;
		gbc_rdbtnFriendly.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnFriendly.gridx = 2;
		gbc_rdbtnFriendly.gridy = 0;
		lifeModePanel.add(rdbtnFriendly, gbc_rdbtnFriendly);
		lifeModeButtonGroup.add(rdbtnFriendly);
		
		rdbtnCompetitive = new JRadioButton("Competitive");
		rdbtnCompetitive.setToolTipText("<html>"+
		        "Grow bigger. Cells know which<br>"+
		        "other cells are in their organism<br>"+
		        "and its family, and will kill<br>"+
		        "adjacet cells from smaller<br>"+
		        "unrelated organisms."+		        
		        "</html>");
		rdbtnCompetitive.setAlignmentX(Component.RIGHT_ALIGNMENT);
		GridBagConstraints gbc_rdbtnCompetitive = new GridBagConstraints();
		gbc_rdbtnCompetitive.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnCompetitive.anchor = GridBagConstraints.WEST;
		gbc_rdbtnCompetitive.gridx = 1;
		gbc_rdbtnCompetitive.gridy = 1;
		lifeModePanel.add(rdbtnCompetitive, gbc_rdbtnCompetitive);
		lifeModeButtonGroup.add(rdbtnCompetitive);				
		
		JLabel lblGrid = new JLabel("Grid");
		GridBagConstraints gbc_lblGrid = new GridBagConstraints();
		gbc_lblGrid.anchor = GridBagConstraints.WEST;
		gbc_lblGrid.insets = new Insets(0, 0, 5, 5);
		gbc_lblGrid.gridx = 2;
		gbc_lblGrid.gridy = 13;
		add(lblGrid, gbc_lblGrid);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 14;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{30, 40, 40};
		gbl_panel.rowHeights = new int[]{23, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0};
		panel.setLayout(gbl_panel);
		
		JLabel lblWidth = new JLabel("Width");
		GridBagConstraints gbc_lblWidth = new GridBagConstraints();
		gbc_lblWidth.anchor = GridBagConstraints.WEST;
		gbc_lblWidth.insets = new Insets(0, 0, 5, 5);
		gbc_lblWidth.gridx = 1;
		gbc_lblWidth.gridy = 0;
		panel.add(lblWidth, gbc_lblWidth);
		
		boardWidthSpinner = new JSpinner();
		GridBagConstraints gbc_boardWidthSpinner = new GridBagConstraints();
		gbc_boardWidthSpinner.insets = new Insets(0, 0, 5, 0);
		gbc_boardWidthSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_boardWidthSpinner.gridx = 2;
		gbc_boardWidthSpinner.gridy = 0;
		panel.add(boardWidthSpinner, gbc_boardWidthSpinner);
		
		JLabel lblHeight = new JLabel("Height");
		GridBagConstraints gbc_lblHeight = new GridBagConstraints();
		gbc_lblHeight.anchor = GridBagConstraints.WEST;
		gbc_lblHeight.insets = new Insets(0, 0, 5, 5);
		gbc_lblHeight.gridx = 1;
		gbc_lblHeight.gridy = 1;
		panel.add(lblHeight, gbc_lblHeight);
		
		boardHeightSpinner = new JSpinner();
		GridBagConstraints gbc_boardHeightSpinner = new GridBagConstraints();
		gbc_boardHeightSpinner.insets = new Insets(0, 0, 5, 0);
		gbc_boardHeightSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_boardHeightSpinner.gridx = 2;
		gbc_boardHeightSpinner.gridy = 1;
		panel.add(boardHeightSpinner, gbc_boardHeightSpinner);
		
		autoSizeGridCheckbox = new JCheckBox("Auto Size");
		autoSizeGridCheckbox.setSelected(true);
		GridBagConstraints gbc_autoSizeGridCheckbox = new GridBagConstraints();
		gbc_autoSizeGridCheckbox.anchor = GridBagConstraints.WEST;
		gbc_autoSizeGridCheckbox.insets = new Insets(0, 0, 0, 5);
		gbc_autoSizeGridCheckbox.gridx = 1;
		gbc_autoSizeGridCheckbox.gridy = 2;
		panel.add(autoSizeGridCheckbox, gbc_autoSizeGridCheckbox);
		
		clipGridToViewButton = new JButton("Clip to View");
		clipGridToViewButton.setMargin(new Insets(2, 2, 2, 2));
		GridBagConstraints gbc_clipGridToViewButton = new GridBagConstraints();
		gbc_clipGridToViewButton.anchor = GridBagConstraints.EAST;
		gbc_clipGridToViewButton.gridx = 2;
		gbc_clipGridToViewButton.gridy = 2;
		panel.add(clipGridToViewButton, gbc_clipGridToViewButton);
		
		JLabel lblImage_1 = new JLabel("Image Size");
		GridBagConstraints gbc_lblImage_1 = new GridBagConstraints();
		gbc_lblImage_1.anchor = GridBagConstraints.WEST;
		gbc_lblImage_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblImage_1.gridx = 2;
		gbc_lblImage_1.gridy = 16;
		add(lblImage_1, gbc_lblImage_1);
		
		imageWidthHeightLabel = new JLabel("100, 100");
		imageWidthHeightLabel.setFont(imageWidthHeightLabel.getFont().deriveFont(Font.PLAIN));
		GridBagConstraints gbc_imageWidthHeightLabel = new GridBagConstraints();
		gbc_imageWidthHeightLabel.anchor = GridBagConstraints.EAST;
		gbc_imageWidthHeightLabel.gridwidth = 2;
		gbc_imageWidthHeightLabel.insets = new Insets(0, 0, 5, 5);
		gbc_imageWidthHeightLabel.gridx = 3;
		gbc_imageWidthHeightLabel.gridy = 16;
		add(imageWidthHeightLabel, gbc_imageWidthHeightLabel);

	}

    public int getInt(String s) {
        return panelController.getGameController().getSettings().getInt(s);
    }

    public JSlider getZoomSlider() {
        return zoomSlider;
    }
    public JRadioButton getRdbtnFriendly() {
        return rdbtnFriendly;
    }
    public JRadioButton getRdbtnCompetitive() {
        return rdbtnCompetitive;
    }
    public JButton getStartPauseButton() {
        return startPauseButton;
    }
    public JButton getStepButton() {
        return stepButton;
    }
    public JSlider getSpeedSlider() {
        return speedSlider;
    }
    public JButton getResetButton() {
        return resetButton;
    }
    public JRadioButton getRdbtnCooperative() {
        return rdbtnCooperative;
    }
    public JSpinner getBoardWidthSpinner() {
        return boardWidthSpinner;
    }
    public JSpinner getBoardHeightSpinner() {
        return boardHeightSpinner;
    }
    public JCheckBox getAutoSizeGridCheckbox() {
        return autoSizeGridCheckbox;
    }
    public JButton getClipGridToViewButton() {
        return clipGridToViewButton;
    }
    public JLabel getImageWidthHeightLabel() {
        return imageWidthHeightLabel;
    }
    public JComboBox getSeedTypeComboBox() {
        return seedTypeComboBox;
    }
}
