package com.sproutlife.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sproutlife.GameController;
import com.sproutlife.Settings;

public class ControlPanel extends JPanel {
	
	GameController gameController;
	
	public ControlPanel(GameController gameController) {		   
		this.gameController = gameController;
		buildPanel();
	}
	/**
	 * Create the panel.
	 */
	public void buildPanel() {		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {130, 50};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 20, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gameController.setGameBeingPlayed(true);
			}
		});
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 10));
		GridBagConstraints gbc_rigidArea_1 = new GridBagConstraints();
		gbc_rigidArea_1.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_1.gridx = 0;
		gbc_rigidArea_1.gridy = 0;
		add(rigidArea_1, gbc_rigidArea_1);
		GridBagConstraints gbc_startButton = new GridBagConstraints();
		gbc_startButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_startButton.insets = new Insets(0, 0, 5, 5);
		gbc_startButton.gridx = 0;
		gbc_startButton.gridy = 1;
		add(startButton, gbc_startButton);
		
		JButton pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gameController.setGameBeingPlayed(false);
			}
		});
		GridBagConstraints gbc_pauseButton = new GridBagConstraints();
		gbc_pauseButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_pauseButton.insets = new Insets(0, 0, 5, 5);
		gbc_pauseButton.gridx = 0;
		gbc_pauseButton.gridy = 2;
		add(pauseButton, gbc_pauseButton);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 10));
		GridBagConstraints gbc_rigidArea = new GridBagConstraints();
		gbc_rigidArea.gridwidth = 2;
		gbc_rigidArea.insets = new Insets(0, 0, 5, 0);
		gbc_rigidArea.gridx = 0;
		gbc_rigidArea.gridy = 3;
		add(rigidArea, gbc_rigidArea);
		
		JLabel childOneEnergyLabel = new JLabel("Child 1 energy");
		GridBagConstraints gbc_childOneEnergyLabel = new GridBagConstraints();
		gbc_childOneEnergyLabel.anchor = GridBagConstraints.WEST;
		gbc_childOneEnergyLabel.insets = new Insets(0, 0, 5, 5);
		gbc_childOneEnergyLabel.gridx = 0;
		gbc_childOneEnergyLabel.gridy = 4;
		add(childOneEnergyLabel, gbc_childOneEnergyLabel);
		
		JSpinner childOneEnergySpinner = new JSpinner();
		childOneEnergySpinner.setValue(getInt(Settings.CHILD_ONE_ENERGY));
		childOneEnergySpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				set(Settings.CHILD_ONE_ENERGY,((JSpinner) arg0.getSource()).getValue());
			}
		});		
		GridBagConstraints gbc_childOneEnergySpinner = new GridBagConstraints();
		gbc_childOneEnergySpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_childOneEnergySpinner.anchor = GridBagConstraints.NORTH;
		gbc_childOneEnergySpinner.insets = new Insets(0, 0, 5, 0);
		gbc_childOneEnergySpinner.gridx = 1;
		gbc_childOneEnergySpinner.gridy = 4;
		add(childOneEnergySpinner, gbc_childOneEnergySpinner);
		
		JLabel childTwoEnergyLabel = new JLabel("Child 2 energy");
		GridBagConstraints gbc_childTwoEnergyLabel = new GridBagConstraints();
		gbc_childTwoEnergyLabel.anchor = GridBagConstraints.WEST;
		gbc_childTwoEnergyLabel.insets = new Insets(0, 0, 5, 5);
		gbc_childTwoEnergyLabel.gridx = 0;
		gbc_childTwoEnergyLabel.gridy = 5;
		add(childTwoEnergyLabel, gbc_childTwoEnergyLabel);
		
		JSpinner childTwoEnergySpinner = new JSpinner();
		childTwoEnergySpinner.setValue(getInt(Settings.CHILD_TWO_ENERGY));
		childTwoEnergySpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				set(Settings.CHILD_TWO_ENERGY,((JSpinner) arg0.getSource()).getValue());
			}
		});
		GridBagConstraints gbc_childTwoEnergySpinner = new GridBagConstraints();
		gbc_childTwoEnergySpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_childTwoEnergySpinner.anchor = GridBagConstraints.NORTH;
		gbc_childTwoEnergySpinner.insets = new Insets(0, 0, 5, 0);
		gbc_childTwoEnergySpinner.gridx = 1;
		gbc_childTwoEnergySpinner.gridy = 5;
		add(childTwoEnergySpinner, gbc_childTwoEnergySpinner);
		
		JLabel childThreeEnergyLabel = new JLabel("Child 3+ energy");		
		GridBagConstraints gbc_childThreeEnergyLabel = new GridBagConstraints();
		gbc_childThreeEnergyLabel.anchor = GridBagConstraints.WEST;
		gbc_childThreeEnergyLabel.insets = new Insets(0, 0, 0, 5);
		gbc_childThreeEnergyLabel.gridx = 0;
		gbc_childThreeEnergyLabel.gridy = 6;
		add(childThreeEnergyLabel, gbc_childThreeEnergyLabel);
		
		JSpinner childThreeEnergySpinner = new JSpinner();
		childThreeEnergySpinner.setValue(getInt(Settings.CHILD_THREE_ENERGY));
		childThreeEnergySpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				set(Settings.CHILD_THREE_ENERGY,((JSpinner) arg0.getSource()).getValue());
			}
		});
		GridBagConstraints gbc_childThreeEnergySpinner = new GridBagConstraints();
		gbc_childThreeEnergySpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_childThreeEnergySpinner.anchor = GridBagConstraints.NORTH;
		gbc_childThreeEnergySpinner.gridx = 1;
		gbc_childThreeEnergySpinner.gridy = 6;
		add(childThreeEnergySpinner, gbc_childThreeEnergySpinner);

	}

	public void set(String s, Object o) {
		gameController.set(s, o);
	}
	
	public int getInt(String s) {
		return gameController.getSettings().getInt(s);
	}
}
