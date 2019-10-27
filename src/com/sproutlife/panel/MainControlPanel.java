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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.Box;

public class MainControlPanel extends JPanel {

    PanelController panelController;
    private JRadioButton rdbtnFriendly;
    private JRadioButton rdbtnCompetitive1;
    private JSpinner boardWidthSpinner;
    private JSpinner boardHeightSpinner;
    private JCheckBox autoSizeGridCheckbox;
    private JButton clipGridToViewButton;
    private JLabel lblSeedType;
    private JComboBox seedTypeComboBox;
    private JRadioButton rdbtnCompetitive2;
    private JLabel lblImage;
    private JLabel lblWidth_1;
    private JLabel lblHeight_1;
    private JSpinner imageWidthSpinner;
    private JSpinner imageHeightSpinner;


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
        gridBagLayout.columnWidths = new int[] {10, 100, 100, 80, 10};
        gridBagLayout.rowHeights = new int[]{20, 0, 15, 0, 0, 15, 0, 31, 0, 0, 31, 15, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, 0.0};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        JPanel lifeModePanel = new JPanel();
        ButtonGroup lifeModeButtonGroup = new ButtonGroup();

        lblSeedType = new JLabel("Seed Type");
        GridBagConstraints gbc_lblSeedType = new GridBagConstraints();
        gbc_lblSeedType.anchor = GridBagConstraints.WEST;
        gbc_lblSeedType.insets = new Insets(0, 0, 5, 5);
        gbc_lblSeedType.gridx = 1;
        gbc_lblSeedType.gridy = 1;
        add(lblSeedType, gbc_lblSeedType);

        seedTypeComboBox = new JComboBox();
        GridBagConstraints gbc_seedTypeComboBox = new GridBagConstraints();
        gbc_seedTypeComboBox.gridwidth = 2;
        gbc_seedTypeComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_seedTypeComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_seedTypeComboBox.gridx = 2;
        gbc_seedTypeComboBox.gridy = 1;
        add(seedTypeComboBox, gbc_seedTypeComboBox);


        JLabel lblLifeMode = new JLabel("Collision Mode");
        GridBagConstraints gbc_lblLifeMode = new GridBagConstraints();
        gbc_lblLifeMode.gridwidth = 2;
        gbc_lblLifeMode.anchor = GridBagConstraints.WEST;
        gbc_lblLifeMode.insets = new Insets(0, 0, 5, 5);
        gbc_lblLifeMode.gridx = 1;
        gbc_lblLifeMode.gridy = 3;
        add(lblLifeMode, gbc_lblLifeMode);
        GridBagConstraints gbc_lifeModePanel = new GridBagConstraints();
        gbc_lifeModePanel.gridwidth = 3;
        gbc_lifeModePanel.anchor = GridBagConstraints.NORTH;
        gbc_lifeModePanel.insets = new Insets(0, 0, 5, 5);
        gbc_lifeModePanel.fill = GridBagConstraints.HORIZONTAL;
        gbc_lifeModePanel.gridx = 1;
        gbc_lifeModePanel.gridy = 4;
        add(lifeModePanel, gbc_lifeModePanel);
        GridBagLayout gbl_lifeModePanel = new GridBagLayout();
        gbl_lifeModePanel.columnWidths = new int[]{40};
        gbl_lifeModePanel.rowHeights = new int[]{23, 0, 0};
        gbl_lifeModePanel.columnWeights = new double[]{0.0};
        gbl_lifeModePanel.rowWeights = new double[]{0.0, 0.0, 0.0};
        lifeModePanel.setLayout(gbl_lifeModePanel);

        rdbtnFriendly = new JRadioButton("Friendly");
        rdbtnFriendly.setToolTipText("<html>"+
                "Collisions are independent of<br>"+
                "organism size.<br>"+ 
                "</html>");
        rdbtnFriendly.setAlignmentX(Component.RIGHT_ALIGNMENT);
        GridBagConstraints gbc_rdbtnFriendly = new GridBagConstraints();
        gbc_rdbtnFriendly.anchor = GridBagConstraints.WEST;
        gbc_rdbtnFriendly.insets = new Insets(0, 0, 5, 0);
        gbc_rdbtnFriendly.gridx = 0;
        gbc_rdbtnFriendly.gridy = 0;
        lifeModePanel.add(rdbtnFriendly, gbc_rdbtnFriendly);
        lifeModeButtonGroup.add(rdbtnFriendly);

        rdbtnCompetitive1 = new JRadioButton("Competitive1");
        rdbtnCompetitive1.setToolTipText("<html>"+
                "Bigger organisms destroy cells<br>"+
                "from smaller organisms based<br>"+
                "on the current size of both<br>"+
                "(weakly competitive)"+
                "</html>");
        rdbtnCompetitive1.setAlignmentX(Component.RIGHT_ALIGNMENT);
        GridBagConstraints gbc_rdbtnCompetitive = new GridBagConstraints();
        gbc_rdbtnCompetitive.insets = new Insets(0, 0, 5, 0);
        gbc_rdbtnCompetitive.anchor = GridBagConstraints.WEST;
        gbc_rdbtnCompetitive.gridx = 0;
        gbc_rdbtnCompetitive.gridy = 1;
        lifeModePanel.add(rdbtnCompetitive1, gbc_rdbtnCompetitive);
        lifeModeButtonGroup.add(rdbtnCompetitive1);

        rdbtnCompetitive2 = new JRadioButton("Competitive2");
        rdbtnCompetitive2.setToolTipText("<html>"+
                "Bigger organisms destroy cells<br>"+
                "from smaller organisms based<br>"+
                "on the size of their parents<br>"+
                "(strongly competitive)"+
                "</html>");
        rdbtnCompetitive2.setAlignmentX(1.0f);
        GridBagConstraints gbc_radioButton = new GridBagConstraints();
        gbc_radioButton.anchor = GridBagConstraints.WEST;
        gbc_radioButton.gridx = 0;
        gbc_radioButton.gridy = 2;
        lifeModePanel.add(rdbtnCompetitive2, gbc_radioButton);
        lifeModeButtonGroup.add(rdbtnCompetitive2);

        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.gridwidth = 3;
        gbc_panel.insets = new Insets(0, 0, 5, 5);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 7;
        add(panel, gbc_panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{30, 40, 40};
        gbl_panel.rowHeights = new int[]{0, 23, 0, 0, 0, 0, 0, 0};
        gbl_panel.columnWeights = new double[]{1.0, 1.0, 1.0};
        gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
        panel.setLayout(gbl_panel);
                                
                                        JLabel lblGrid = new JLabel("Grid");
                                        GridBagConstraints gbc_lblGrid = new GridBagConstraints();
                                        gbc_lblGrid.anchor = GridBagConstraints.WEST;
                                        gbc_lblGrid.insets = new Insets(0, 0, 5, 5);
                                        gbc_lblGrid.gridx = 0;
                                        gbc_lblGrid.gridy = 0;
                                        panel.add(lblGrid, gbc_lblGrid);
                        
                                JLabel lblWidth = new JLabel("Width");
                                GridBagConstraints gbc_lblWidth = new GridBagConstraints();
                                gbc_lblWidth.anchor = GridBagConstraints.WEST;
                                gbc_lblWidth.insets = new Insets(0, 0, 5, 5);
                                gbc_lblWidth.gridx = 1;
                                gbc_lblWidth.gridy = 1;
                                panel.add(lblWidth, gbc_lblWidth);
                
                        boardWidthSpinner = new JSpinner();
                        boardWidthSpinner.setPreferredSize(new Dimension(100, 26));
                        GridBagConstraints gbc_boardWidthSpinner = new GridBagConstraints();
                        gbc_boardWidthSpinner.insets = new Insets(0, 0, 5, 0);
                        gbc_boardWidthSpinner.fill = GridBagConstraints.HORIZONTAL;
                        gbc_boardWidthSpinner.gridx = 2;
                        gbc_boardWidthSpinner.gridy = 1;
                        panel.add(boardWidthSpinner, gbc_boardWidthSpinner);
                        
                                JLabel lblHeight = new JLabel("Height");
                                GridBagConstraints gbc_lblHeight = new GridBagConstraints();
                                gbc_lblHeight.anchor = GridBagConstraints.WEST;
                                gbc_lblHeight.insets = new Insets(0, 0, 5, 5);
                                gbc_lblHeight.gridx = 1;
                                gbc_lblHeight.gridy = 2;
                                panel.add(lblHeight, gbc_lblHeight);
                        
                                boardHeightSpinner = new JSpinner();
                                boardHeightSpinner.setPreferredSize(new Dimension(100, 26));
                                GridBagConstraints gbc_boardHeightSpinner = new GridBagConstraints();
                                gbc_boardHeightSpinner.insets = new Insets(0, 0, 5, 0);
                                gbc_boardHeightSpinner.fill = GridBagConstraints.HORIZONTAL;
                                gbc_boardHeightSpinner.gridx = 2;
                                gbc_boardHeightSpinner.gridy = 2;
                                panel.add(boardHeightSpinner, gbc_boardHeightSpinner);
        
        lblImage = new JLabel("Image");
        GridBagConstraints gbc_lblImage = new GridBagConstraints();
        gbc_lblImage.anchor = GridBagConstraints.WEST;
        gbc_lblImage.insets = new Insets(0, 0, 5, 5);
        gbc_lblImage.gridx = 0;
        gbc_lblImage.gridy = 3;
        panel.add(lblImage, gbc_lblImage);
        
        lblWidth_1 = new JLabel("Width");
        GridBagConstraints gbc_lblWidth_1 = new GridBagConstraints();
        gbc_lblWidth_1.anchor = GridBagConstraints.WEST;
        gbc_lblWidth_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblWidth_1.gridx = 1;
        gbc_lblWidth_1.gridy = 4;
        panel.add(lblWidth_1, gbc_lblWidth_1);
        
        imageWidthSpinner = new JSpinner();
        GridBagConstraints gbc_imageWidthSpinner = new GridBagConstraints();
        gbc_imageWidthSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_imageWidthSpinner.insets = new Insets(0, 0, 5, 0);
        gbc_imageWidthSpinner.gridx = 2;
        gbc_imageWidthSpinner.gridy = 4;
        panel.add(imageWidthSpinner, gbc_imageWidthSpinner);
        imageWidthSpinner.setPreferredSize(new Dimension(100, 26));
        
        lblHeight_1 = new JLabel("Height");
        GridBagConstraints gbc_lblHeight_1 = new GridBagConstraints();
        gbc_lblHeight_1.anchor = GridBagConstraints.WEST;
        gbc_lblHeight_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblHeight_1.gridx = 1;
        gbc_lblHeight_1.gridy = 5;
        panel.add(lblHeight_1, gbc_lblHeight_1);
        
        imageHeightSpinner = new JSpinner();
        GridBagConstraints gbc_imageHeightSpinner = new GridBagConstraints();
        gbc_imageHeightSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_imageHeightSpinner.insets = new Insets(0, 0, 5, 0);
        gbc_imageHeightSpinner.gridx = 2;
        gbc_imageHeightSpinner.gridy = 5;
        panel.add(imageHeightSpinner, gbc_imageHeightSpinner);
        imageHeightSpinner.setPreferredSize(new Dimension(100, 26));
        
                autoSizeGridCheckbox = new JCheckBox("Auto Size");
                GridBagConstraints gbc_autoSizeGridCheckbox = new GridBagConstraints();
                gbc_autoSizeGridCheckbox.anchor = GridBagConstraints.WEST;
                gbc_autoSizeGridCheckbox.insets = new Insets(0, 0, 5, 5);
                gbc_autoSizeGridCheckbox.gridx = 0;
                gbc_autoSizeGridCheckbox.gridy = 6;
                panel.add(autoSizeGridCheckbox, gbc_autoSizeGridCheckbox);
                autoSizeGridCheckbox.setSelected(true);
                
                        clipGridToViewButton = new JButton("Clip to View");
                        GridBagConstraints gbc_clipGridToViewButton = new GridBagConstraints();
                        gbc_clipGridToViewButton.fill = GridBagConstraints.HORIZONTAL;
                        gbc_clipGridToViewButton.gridwidth = 2;
                        gbc_clipGridToViewButton.insets = new Insets(0, 0, 5, 0);
                        gbc_clipGridToViewButton.gridx = 1;
                        gbc_clipGridToViewButton.gridy = 6;
                        panel.add(clipGridToViewButton, gbc_clipGridToViewButton);
                        clipGridToViewButton.setPreferredSize(new Dimension(120, 29));
                        clipGridToViewButton.setMargin(new Insets(2, 2, 2, 2));

    }

    public int getInt(String s) {
        return panelController.getGameController().getSettings().getInt(s);
    }

    public JRadioButton getRdbtnFriendly() {
        return rdbtnFriendly;
    }
    public JRadioButton getRdbtnCompetitive1() {
        return rdbtnCompetitive1;
    }
    public JRadioButton getRdbtnCompetitive2() {
        return rdbtnCompetitive2;
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
    public JComboBox getSeedTypeComboBox() {
        return seedTypeComboBox;
    }
    public JSpinner getImageWidthSpinner() {
        return imageWidthSpinner;
    }
    public JSpinner getImageHeightSpinner() {
        return imageHeightSpinner;
    }
}
