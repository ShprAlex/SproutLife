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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class DisplayControlPanel extends JPanel {

    PanelController panelController;
    private JPanel panel_2;
    private JCheckBox chckbxCellLayer;
    private JCheckBox chckbxGenomeLayer;
    private JCheckBox chckbxOrgHeadLayer;
    private JCheckBox chckbxOrgTailLayer;
    private JLabel lblDrawLayers;
    private JCheckBox chckbxOutlineSeeds;
    private JLabel lblColors;
    private JPanel panel;
    private JRadioButton rdbtnBackgroundWhite;
    private JRadioButton rdbtnBackgroundBlack;
    private final ButtonGroup buttonGroupBackground = new ButtonGroup();
    private Component verticalStrut;
    private JSpinner spinnerTailLength;
    private Box horizontalBox_1;
    private JCheckBox chckbxAutoSplitColors;

    private JSpinner boardWidthSpinner;
    private JSpinner boardHeightSpinner;
    private JCheckBox autoSizeGridCheckbox;
    private JButton clipGridToViewButton;
    private JLabel lblImage;
    private JSpinner imageWidthSpinner;
    private JSpinner imageHeightSpinner;
    private Component verticalStrut_2;
    private Component horizontalStrut;
    private JLabel labelBackground;
    private JRadioButton rdbtnMultiColorMode;
    private JRadioButton rdbtnTriColorMode;
    private final ButtonGroup buttonGroupColorMode = new ButtonGroup();


    public DisplayControlPanel(PanelController panelController) {
        setMinimumSize(new Dimension(220, 0));
        setPreferredSize(new Dimension(280, 600));

        this.panelController = panelController;
        buildPanel();
    }
    /**
     * Create the panel.
     */
    public void buildPanel() {

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {10, 10, 30, 100};
        gridBagLayout.rowHeights = new int[]{20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
        setLayout(gridBagLayout);

        lblDrawLayers = new JLabel("Draw Layers");
        GridBagConstraints gbc_lblDrawLayers = new GridBagConstraints();
        gbc_lblDrawLayers.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblDrawLayers.insets = new Insets(0, 0, 5, 5);
        gbc_lblDrawLayers.gridx = 2;
        gbc_lblDrawLayers.gridy = 1;
        add(lblDrawLayers, gbc_lblDrawLayers);

        horizontalStrut = Box.createHorizontalStrut(20);
        horizontalStrut.setPreferredSize(new Dimension(5, 0));
        GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
        gbc_horizontalStrut.insets = new Insets(0, 0, 5, 0);
        gbc_horizontalStrut.gridx = 5;
        gbc_horizontalStrut.gridy = 1;
        add(horizontalStrut, gbc_horizontalStrut);

        panel_2 = new JPanel();
        GridBagConstraints gbc_panel_2 = new GridBagConstraints();
        gbc_panel_2.gridwidth = 3;
        gbc_panel_2.insets = new Insets(0, 30, 5, 5);
        gbc_panel_2.fill = GridBagConstraints.BOTH;
        gbc_panel_2.gridx = 2;
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

        horizontalBox_1 = Box.createHorizontalBox();
        horizontalBox_1.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_2.add(horizontalBox_1);

        chckbxOrgTailLayer = new JCheckBox("Tail Layer, Length");
        horizontalBox_1.add(chckbxOrgTailLayer);
        chckbxOrgTailLayer.setSelected(true);

        spinnerTailLength = new JSpinner();
        spinnerTailLength.setModel(new SpinnerNumberModel(9, 1, 20, 1));
        horizontalBox_1.add(spinnerTailLength);

        chckbxOutlineSeeds = new JCheckBox("Outline Seeds & Young");
        panel_2.add(chckbxOutlineSeeds);

        verticalStrut = Box.createVerticalStrut(20);
        verticalStrut.setPreferredSize(new Dimension(0, 10));
        GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
        gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut.gridx = 0;
        gbc_verticalStrut.gridy = 3;
        add(verticalStrut, gbc_verticalStrut);

        lblColors = new JLabel("Colors");
        GridBagConstraints gbc_lblColors = new GridBagConstraints();
        gbc_lblColors.gridwidth = 2;
        gbc_lblColors.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblColors.insets = new Insets(0, 0, 5, 5);
        gbc_lblColors.gridx = 2;
        gbc_lblColors.gridy = 4;
        add(lblColors, gbc_lblColors);

        panel = new JPanel();
        panel.setMaximumSize(new Dimension(32767, 50));
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.anchor = GridBagConstraints.NORTHWEST;
        gbc_panel.gridwidth = 4;
        gbc_panel.insets = new Insets(0, 30, 5, 0);
        gbc_panel.gridx = 2;
        gbc_panel.gridy = 5;
        add(panel, gbc_panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        labelBackground = new JLabel("Background");
        panel.add(labelBackground);

        rdbtnBackgroundBlack = new JRadioButton("Black");
        rdbtnBackgroundBlack.setMargin(new Insets(1, 5, 0, 1));
        buttonGroupBackground.add(rdbtnBackgroundBlack);
        rdbtnBackgroundBlack.setSelected(true);
        panel.add(rdbtnBackgroundBlack);

        rdbtnBackgroundWhite = new JRadioButton("White");
        buttonGroupBackground.add(rdbtnBackgroundWhite);
        panel.add(rdbtnBackgroundWhite);

        rdbtnMultiColorMode = new JRadioButton("Multi Color Mode");
        rdbtnMultiColorMode.setSelected(true);
        buttonGroupColorMode.add(rdbtnMultiColorMode);
        GridBagConstraints gbc_rdbtnMultiColorMode = new GridBagConstraints();
        gbc_rdbtnMultiColorMode.anchor = GridBagConstraints.NORTHWEST;
        gbc_rdbtnMultiColorMode.gridwidth = 2;
        gbc_rdbtnMultiColorMode.insets = new Insets(0, 30, 5, 5);
        gbc_rdbtnMultiColorMode.gridx = 2;
        gbc_rdbtnMultiColorMode.gridy = 6;
        add(rdbtnMultiColorMode, gbc_rdbtnMultiColorMode);

        rdbtnTriColorMode = new JRadioButton("Tri Color Mode");
        buttonGroupColorMode.add(rdbtnTriColorMode);
        GridBagConstraints gbc_rdbtnTriColorMode = new GridBagConstraints();
        gbc_rdbtnTriColorMode.anchor = GridBagConstraints.NORTHWEST;
        gbc_rdbtnTriColorMode.gridwidth = 2;
        gbc_rdbtnTriColorMode.insets = new Insets(0, 30, 5, 5);
        gbc_rdbtnTriColorMode.gridx = 2;
        gbc_rdbtnTriColorMode.gridy = 7;
        add(rdbtnTriColorMode, gbc_rdbtnTriColorMode);

        chckbxAutoSplitColors = new JCheckBox("Auto Split Colors");
        GridBagConstraints gbc_chckbxAutoSplitColors = new GridBagConstraints();
        gbc_chckbxAutoSplitColors.anchor = GridBagConstraints.NORTHWEST;
        gbc_chckbxAutoSplitColors.gridwidth = 3;
        gbc_chckbxAutoSplitColors.insets = new Insets(0, 60, 5, 5);
        gbc_chckbxAutoSplitColors.gridx = 2;
        gbc_chckbxAutoSplitColors.gridy = 8;
        add(chckbxAutoSplitColors, gbc_chckbxAutoSplitColors);
        chckbxAutoSplitColors.setToolTipText("<html>Split Color of Organism Group<br>when there are too many of that color.</html>");
        chckbxAutoSplitColors.setSelected(true);

        verticalStrut_2 = Box.createVerticalStrut(20);
        verticalStrut_2.setPreferredSize(new Dimension(0, 10));
        GridBagConstraints gbc_verticalStrut_2 = new GridBagConstraints();
        gbc_verticalStrut_2.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut_2.gridx = 2;
        gbc_verticalStrut_2.gridy = 9;
        add(verticalStrut_2, gbc_verticalStrut_2);


        JPanel sizePanel = new JPanel();
        GridBagConstraints gbc_sizePanel = new GridBagConstraints();
        gbc_sizePanel.insets = new Insets(0, 0, 5, 5);
        gbc_sizePanel.gridwidth = 3;
        gbc_sizePanel.fill = GridBagConstraints.BOTH;
        gbc_sizePanel.gridx = 2;
        gbc_sizePanel.gridy = 10;
        add(sizePanel, gbc_sizePanel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{30, 40, 40};
        gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        gbl_panel.columnWeights = new double[]{1.0, 1.0, 1.0};
        gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
        sizePanel.setLayout(gbl_panel);

        JLabel lblGrid = new JLabel("Grid W, H:");
        GridBagConstraints gbc_lblGrid = new GridBagConstraints();
        gbc_lblGrid.anchor = GridBagConstraints.WEST;
        gbc_lblGrid.insets = new Insets(0, 0, 5, 5);
        gbc_lblGrid.gridx = 0;
        gbc_lblGrid.gridy = 0;
        sizePanel.add(lblGrid, gbc_lblGrid);

        boardWidthSpinner = new JSpinner();
        boardWidthSpinner.setPreferredSize(new Dimension(120, 26));
        GridBagConstraints gbc_boardWidthSpinner = new GridBagConstraints();
        gbc_boardWidthSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_boardWidthSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_boardWidthSpinner.gridx = 1;
        gbc_boardWidthSpinner.gridy = 0;
        sizePanel.add(boardWidthSpinner, gbc_boardWidthSpinner);

        boardHeightSpinner = new JSpinner();
        boardHeightSpinner.setPreferredSize(new Dimension(120, 26));
        GridBagConstraints gbc_boardHeightSpinner = new GridBagConstraints();
        gbc_boardHeightSpinner.insets = new Insets(0, 0, 5, 0);
        gbc_boardHeightSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_boardHeightSpinner.gridx = 2;
        gbc_boardHeightSpinner.gridy = 0;
        sizePanel.add(boardHeightSpinner, gbc_boardHeightSpinner);

        lblImage = new JLabel("Image W, H:");
        GridBagConstraints gbc_lblImage = new GridBagConstraints();
        gbc_lblImage.anchor = GridBagConstraints.WEST;
        gbc_lblImage.insets = new Insets(0, 0, 5, 5);
        gbc_lblImage.gridx = 0;
        gbc_lblImage.gridy = 2;
        sizePanel.add(lblImage, gbc_lblImage);

        imageWidthSpinner = new JSpinner();
        GridBagConstraints gbc_imageWidthSpinner = new GridBagConstraints();
        gbc_imageWidthSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_imageWidthSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_imageWidthSpinner.gridx = 1;
        gbc_imageWidthSpinner.gridy = 2;
        sizePanel.add(imageWidthSpinner, gbc_imageWidthSpinner);
        imageWidthSpinner.setPreferredSize(new Dimension(100, 26));

        imageHeightSpinner = new JSpinner();
        GridBagConstraints gbc_imageHeightSpinner = new GridBagConstraints();
        gbc_imageHeightSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_imageHeightSpinner.insets = new Insets(0, 0, 5, 0);
        gbc_imageHeightSpinner.gridx = 2;
        gbc_imageHeightSpinner.gridy = 2;
        sizePanel.add(imageHeightSpinner, gbc_imageHeightSpinner);
        imageHeightSpinner.setPreferredSize(new Dimension(100, 26));

        autoSizeGridCheckbox = new JCheckBox("Auto Size");
        GridBagConstraints gbc_autoSizeGridCheckbox = new GridBagConstraints();
        gbc_autoSizeGridCheckbox.anchor = GridBagConstraints.WEST;
        gbc_autoSizeGridCheckbox.insets = new Insets(0, 0, 5, 5);
        gbc_autoSizeGridCheckbox.gridx = 0;
        gbc_autoSizeGridCheckbox.gridy = 4;
        sizePanel.add(autoSizeGridCheckbox, gbc_autoSizeGridCheckbox);
        autoSizeGridCheckbox.setSelected(true);

        clipGridToViewButton = new JButton("Clip to View");
        GridBagConstraints gbc_clipGridToViewButton = new GridBagConstraints();
        gbc_clipGridToViewButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_clipGridToViewButton.gridwidth = 2;
        gbc_clipGridToViewButton.insets = new Insets(0, 0, 5, 0);
        gbc_clipGridToViewButton.gridx = 1;
        gbc_clipGridToViewButton.gridy = 4;
        sizePanel.add(clipGridToViewButton, gbc_clipGridToViewButton);
        clipGridToViewButton.setPreferredSize(new Dimension(120, 29));
        clipGridToViewButton.setMargin(new Insets(2, 2, 2, 2));

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
    public JRadioButton getRdbtnBackgroundBlack() {
        return rdbtnBackgroundBlack;
    }
    public JRadioButton getRdbtnBackgroundWhite() {
        return rdbtnBackgroundWhite;
    }
    public JSpinner getSpinnerTailLength() {
        return spinnerTailLength;
    }
    public JCheckBox getChckbxAutoSplitColors() {
        return chckbxAutoSplitColors;
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
    public JSpinner getImageWidthSpinner() {
        return imageWidthSpinner;
    }
    public JSpinner getImageHeightSpinner() {
        return imageHeightSpinner;
    }
    public JRadioButton getRdbtnMultiColorMode() {
        return rdbtnMultiColorMode;
    }
    public JRadioButton getRdbtnTriColorMode() {
        return rdbtnTriColorMode;
    }
}
