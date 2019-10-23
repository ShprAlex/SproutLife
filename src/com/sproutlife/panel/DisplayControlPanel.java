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
import java.awt.FlowLayout;
import java.awt.Component;
import javax.swing.Box;
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
    private JLabel lblBackground;
    private JPanel panel;
    private JRadioButton rdbtnBackgroundWhite;
    private JRadioButton rdbtnBackgroundBlack;
    private final ButtonGroup buttonGroupBackground = new ButtonGroup();
    private Component verticalStrut;
    private JSpinner spinnerTailLength;
    private Box horizontalBox_1;




    public DisplayControlPanel(PanelController panelController) {
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
        gridBagLayout.columnWidths = new int[] {10, 30, 100};
        gridBagLayout.rowHeights = new int[]{20, 0, 0, 0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0};
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
        gbc_panel_2.insets = new Insets(0, 30, 5, 0);
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
        
        horizontalBox_1 = Box.createHorizontalBox();
        horizontalBox_1.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_2.add(horizontalBox_1);

        chckbxOrgTailLayer = new JCheckBox("Org Tail Layer, Length");
        horizontalBox_1.add(chckbxOrgTailLayer);
        chckbxOrgTailLayer.setSelected(true);
        
        spinnerTailLength = new JSpinner();
        spinnerTailLength.setModel(new SpinnerNumberModel(9, 1, 20, 1));
        horizontalBox_1.add(spinnerTailLength);
        
                chckbxOutlineSeeds = new JCheckBox("Outline Seeds & Young");
                panel_2.add(chckbxOutlineSeeds);
        
        verticalStrut = Box.createVerticalStrut(20);
        GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
        gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut.gridx = 0;
        gbc_verticalStrut.gridy = 3;
        add(verticalStrut, gbc_verticalStrut);
        
        lblBackground = new JLabel("Background Theme");
        GridBagConstraints gbc_lblBackground = new GridBagConstraints();
        gbc_lblBackground.gridwidth = 2;
        gbc_lblBackground.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblBackground.insets = new Insets(0, 0, 5, 0);
        gbc_lblBackground.gridx = 1;
        gbc_lblBackground.gridy = 4;
        add(lblBackground, gbc_lblBackground);
        
        panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.fill = GridBagConstraints.VERTICAL;
        gbc_panel.anchor = GridBagConstraints.WEST;
        gbc_panel.gridwidth = 2;
        gbc_panel.insets = new Insets(0, 30, 5, 0);
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 5;
        add(panel, gbc_panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        rdbtnBackgroundBlack = new JRadioButton("Black");
        buttonGroupBackground.add(rdbtnBackgroundBlack);
        rdbtnBackgroundBlack.setSelected(true);
        panel.add(rdbtnBackgroundBlack);
        
        rdbtnBackgroundWhite = new JRadioButton("White");
        buttonGroupBackground.add(rdbtnBackgroundWhite);
        panel.add(rdbtnBackgroundWhite);

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
}
