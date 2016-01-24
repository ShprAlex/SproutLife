package com.sproutlife.panel;

import java.awt.Component;
import java.awt.Dimension;
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

public class ControlPanel extends JPanel {
	
	PanelController panelController;
	private JSlider zoomSlider;
	private JRadioButton rdbtnFriendly;
	private JRadioButton rdbtnCompetitive;
	
	public ControlPanel(PanelController panelController) {
		setMinimumSize(new Dimension(220, 0));	
		        
		this.panelController = panelController;
		buildPanel();
	}
	/**
	 * Create the panel.
	 */
	public void buildPanel() {		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {80, 100, 50};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 10));
		GridBagConstraints gbc_rigidArea_1 = new GridBagConstraints();
		gbc_rigidArea_1.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_1.gridx = 1;
		gbc_rigidArea_1.gridy = 0;
		add(rigidArea_1, gbc_rigidArea_1);
		
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panelController.getGameModel().setPlayGame(true);
			}
		});
		GridBagConstraints gbc_startButton = new GridBagConstraints();
		gbc_startButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_startButton.gridwidth = 2;
		gbc_startButton.insets = new Insets(0, 0, 5, 0);
		gbc_startButton.gridx = 1;
		gbc_startButton.gridy = 1;
		add(startButton, gbc_startButton);
		
		JButton pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    panelController.getGameModel().setPlayGame(false);
			}
		});
		GridBagConstraints gbc_pauseButton = new GridBagConstraints();
		gbc_pauseButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_pauseButton.gridwidth = 2;
		gbc_pauseButton.insets = new Insets(0, 0, 5, 0);
		gbc_pauseButton.gridx = 1;
		gbc_pauseButton.gridy = 2;
		add(pauseButton, gbc_pauseButton);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 10));
		GridBagConstraints gbc_rigidArea = new GridBagConstraints();
		gbc_rigidArea.gridwidth = 2;
		gbc_rigidArea.insets = new Insets(0, 0, 5, 0);
		gbc_rigidArea.gridx = 1;
		gbc_rigidArea.gridy = 3;
		add(rigidArea, gbc_rigidArea);
		
		JLabel lblZoom = new JLabel("Zoom");
		GridBagConstraints gbc_lblZoom = new GridBagConstraints();
		gbc_lblZoom.anchor = GridBagConstraints.WEST;
		gbc_lblZoom.insets = new Insets(0, 0, 5, 5);
		gbc_lblZoom.gridx = 0;
		gbc_lblZoom.gridy = 5;
		add(lblZoom, gbc_lblZoom);
		
		zoomSlider = new JSlider();
		zoomSlider.setMinimum(-5);
		zoomSlider.setValue(-2);
		zoomSlider.setMaximum(5);
		GridBagConstraints gbc_zoomSlider = new GridBagConstraints();
		gbc_zoomSlider.fill = GridBagConstraints.HORIZONTAL;
		gbc_zoomSlider.gridwidth = 2;
		gbc_zoomSlider.insets = new Insets(0, 0, 5, 0);
		gbc_zoomSlider.gridx = 1;
		gbc_zoomSlider.gridy = 5;
		add(zoomSlider, gbc_zoomSlider);
		
		JSpinner childOneEnergySpinner = new JSpinner();
		childOneEnergySpinner.setValue(getInt(Settings.CHILD_ONE_ENERGY));
		childOneEnergySpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				set(Settings.CHILD_ONE_ENERGY,((JSpinner) arg0.getSource()).getValue());
			}
		});
		
		JLabel lblLifeMode = new JLabel("Life Mode");
		GridBagConstraints gbc_lblLifeMode = new GridBagConstraints();
		gbc_lblLifeMode.anchor = GridBagConstraints.WEST;
		gbc_lblLifeMode.insets = new Insets(0, 0, 5, 5);
		gbc_lblLifeMode.gridx = 0;
		gbc_lblLifeMode.gridy = 6;
		add(lblLifeMode, gbc_lblLifeMode);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.anchor = GridBagConstraints.EAST;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.VERTICAL;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 6;
		add(panel, gbc_panel);
		
		rdbtnFriendly = new JRadioButton("Friendly");
		panel.add(rdbtnFriendly);
		
		rdbtnCompetitive = new JRadioButton("Competitive");
		panel.add(rdbtnCompetitive);
		
		ButtonGroup lifeModeButtonGroup = new ButtonGroup();
		lifeModeButtonGroup.add(rdbtnFriendly);
		lifeModeButtonGroup.add(rdbtnCompetitive);
		
		JLabel childOneEnergyLabel = new JLabel("Child 1 energy");
		GridBagConstraints gbc_childOneEnergyLabel = new GridBagConstraints();
		gbc_childOneEnergyLabel.gridwidth = 2;
		gbc_childOneEnergyLabel.anchor = GridBagConstraints.WEST;
		gbc_childOneEnergyLabel.insets = new Insets(0, 0, 5, 5);
		gbc_childOneEnergyLabel.gridx = 0;
		gbc_childOneEnergyLabel.gridy = 8;
		add(childOneEnergyLabel, gbc_childOneEnergyLabel);
		GridBagConstraints gbc_childOneEnergySpinner = new GridBagConstraints();
		gbc_childOneEnergySpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_childOneEnergySpinner.anchor = GridBagConstraints.NORTH;
		gbc_childOneEnergySpinner.insets = new Insets(0, 0, 5, 0);
		gbc_childOneEnergySpinner.gridx = 2;
		gbc_childOneEnergySpinner.gridy = 8;
		add(childOneEnergySpinner, gbc_childOneEnergySpinner);
		
		JSpinner childTwoEnergySpinner = new JSpinner();
		childTwoEnergySpinner.setValue(getInt(Settings.CHILD_TWO_ENERGY));
		childTwoEnergySpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				set(Settings.CHILD_TWO_ENERGY,((JSpinner) arg0.getSource()).getValue());
			}
		});
		
		JLabel childTwoEnergyLabel = new JLabel("Child 2 energy");
		GridBagConstraints gbc_childTwoEnergyLabel = new GridBagConstraints();
		gbc_childTwoEnergyLabel.gridwidth = 2;
		gbc_childTwoEnergyLabel.anchor = GridBagConstraints.WEST;
		gbc_childTwoEnergyLabel.insets = new Insets(0, 0, 5, 5);
		gbc_childTwoEnergyLabel.gridx = 0;
		gbc_childTwoEnergyLabel.gridy = 9;
		add(childTwoEnergyLabel, gbc_childTwoEnergyLabel);
		GridBagConstraints gbc_childTwoEnergySpinner = new GridBagConstraints();
		gbc_childTwoEnergySpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_childTwoEnergySpinner.anchor = GridBagConstraints.NORTH;
		gbc_childTwoEnergySpinner.insets = new Insets(0, 0, 5, 0);
		gbc_childTwoEnergySpinner.gridx = 2;
		gbc_childTwoEnergySpinner.gridy = 9;
		add(childTwoEnergySpinner, gbc_childTwoEnergySpinner);
		
		JSpinner childThreeEnergySpinner = new JSpinner();
		childThreeEnergySpinner.setValue(getInt(Settings.CHILD_THREE_ENERGY));
		childThreeEnergySpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				set(Settings.CHILD_THREE_ENERGY,((JSpinner) arg0.getSource()).getValue());
			}
		});
		
		JLabel childThreeEnergyLabel = new JLabel("Child 3+ energy");		
		GridBagConstraints gbc_childThreeEnergyLabel = new GridBagConstraints();
		gbc_childThreeEnergyLabel.gridwidth = 2;
		gbc_childThreeEnergyLabel.anchor = GridBagConstraints.WEST;
		gbc_childThreeEnergyLabel.insets = new Insets(0, 0, 0, 5);
		gbc_childThreeEnergyLabel.gridx = 0;
		gbc_childThreeEnergyLabel.gridy = 10;
		add(childThreeEnergyLabel, gbc_childThreeEnergyLabel);
		GridBagConstraints gbc_childThreeEnergySpinner = new GridBagConstraints();
		gbc_childThreeEnergySpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_childThreeEnergySpinner.anchor = GridBagConstraints.NORTH;
		gbc_childThreeEnergySpinner.gridx = 2;
		gbc_childThreeEnergySpinner.gridy = 10;
		add(childThreeEnergySpinner, gbc_childThreeEnergySpinner);

	}

    public void set(String s, Object o) {
        panelController.getGameController().set(s, o);
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
}
