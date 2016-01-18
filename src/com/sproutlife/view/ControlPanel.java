package com.sproutlife.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
		gridBagLayout.columnWidths = new int[] {151, 46};
		gridBagLayout.rowHeights = new int[]{20, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel childOneEnergyLabel = new JLabel("Child 1 energy");
		GridBagConstraints gbc_childOneEnergyLabel = new GridBagConstraints();
		gbc_childOneEnergyLabel.insets = new Insets(0, 0, 5, 5);
		gbc_childOneEnergyLabel.gridx = 0;
		gbc_childOneEnergyLabel.gridy = 0;
		add(childOneEnergyLabel, gbc_childOneEnergyLabel);
		
		JSpinner childOneEnergySpinner = new JSpinner();
		childOneEnergySpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				set(Settings.CHILD_ONE_ENERGY,((JSpinner) arg0.getSource()).getValue());
			}
		});
		childOneEnergySpinner.setValue(getInt(Settings.CHILD_ONE_ENERGY));
		GridBagConstraints gbc_childOneEnergySpinner = new GridBagConstraints();
		gbc_childOneEnergySpinner.anchor = GridBagConstraints.NORTHWEST;
		gbc_childOneEnergySpinner.insets = new Insets(0, 0, 5, 5);
		gbc_childOneEnergySpinner.gridx = 1;
		gbc_childOneEnergySpinner.gridy = 0;
		add(childOneEnergySpinner, gbc_childOneEnergySpinner);
		
		JLabel childTwoEnergyLabel = new JLabel("Child 2 energy");
		GridBagConstraints gbc_childTwoEnergyLabel = new GridBagConstraints();
		gbc_childTwoEnergyLabel.insets = new Insets(0, 0, 5, 5);
		gbc_childTwoEnergyLabel.gridx = 0;
		gbc_childTwoEnergyLabel.gridy = 1;
		add(childTwoEnergyLabel, gbc_childTwoEnergyLabel);
		
		JSpinner childTwoEnergySpinner = new JSpinner();
		GridBagConstraints gbc_childTwoEnergySpinner = new GridBagConstraints();
		gbc_childTwoEnergySpinner.anchor = GridBagConstraints.NORTHWEST;
		gbc_childTwoEnergySpinner.insets = new Insets(0, 0, 5, 5);
		gbc_childTwoEnergySpinner.gridx = 1;
		gbc_childTwoEnergySpinner.gridy = 1;
		add(childTwoEnergySpinner, gbc_childTwoEnergySpinner);
		
		JLabel childThreeEnergyLabel = new JLabel("Child 3+ energy");
		GridBagConstraints gbc_childThreeEnergyLabel = new GridBagConstraints();
		gbc_childThreeEnergyLabel.insets = new Insets(0, 0, 0, 5);
		gbc_childThreeEnergyLabel.gridx = 0;
		gbc_childThreeEnergyLabel.gridy = 2;
		add(childThreeEnergyLabel, gbc_childThreeEnergyLabel);
		
		JSpinner childThreeEnergySpinner = new JSpinner();
		GridBagConstraints gbc_childThreeEnergySpinner = new GridBagConstraints();
		gbc_childThreeEnergySpinner.insets = new Insets(0, 0, 0, 5);
		gbc_childThreeEnergySpinner.anchor = GridBagConstraints.NORTHWEST;
		gbc_childThreeEnergySpinner.gridx = 1;
		gbc_childThreeEnergySpinner.gridy = 2;
		add(childThreeEnergySpinner, gbc_childThreeEnergySpinner);

	}

	public void set(String s, Object o) {
		gameController.set(s, o);
	}
	
	public int getInt(String s) {
		return gameController.getSettings().getInt(s);
	}
}
