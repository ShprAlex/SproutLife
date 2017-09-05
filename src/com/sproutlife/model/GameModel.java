/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.sproutlife.Settings;
import com.sproutlife.model.echosystem.Board;
import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.step.GameStep;
import com.sproutlife.model.step.GameStepListener;

/**
 * This is a class used to manage the game model essentials.
 *
 * @author Alex Shapiro
 */
public class GameModel {

	private Echosystem echosystem;

	private GameClock clock;

	private GameStep gameStep;

	private GameThread gameThread;

	private Settings settings;

	private Stats stats;

	/**
	 * GameModel Constructor
	 */
	public GameModel(Settings settings, ReentrantReadWriteLock interactionLock) {
		this.settings = settings;
		this.clock = new GameClock();
		echosystem = new Echosystem(clock);
		gameStep = new GameStep(this);
		gameThread = new GameThread(this, interactionLock);
		stats = new Stats(this);
	}

	
	public void performGameStep() {
		incrementTime();
		gameStep.perform();
	}
	public Echosystem getEchosystem() {
		return echosystem;
	}

	public Board getBoard() {
		return echosystem.getBoard();
	}

	public int getTime() {
		return clock.getTime();
	}

	public GameClock getClock() {
		return clock;
	}

	private void incrementTime() {
		clock.increment();
	}

	/**
	 * Resets current game.
	 */
	public void resetGame() {
		getEchosystem().resetCells();
		getEchosystem().pruneEmptyOrganisms();
		getEchosystem().clearRetiredOrgs();
		getStats().reset();
		getClock().reset();
	}

	public Stats getStats() {
		return stats;
	}
	
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Game play mode flag setter.
	 *
	 * @param playGame
	 *            True if the play mode is on, false otherwise.
	 */
	public void setPlayGame(boolean playGame) {
		gameThread.setPlayGame(playGame);
	}

	public GameThread getGameThread() {
		return gameThread;
	}

	/**
	 * Checks whether or not a game is played.
	 *
	 * @return boolean value.
	 */
	public boolean getPlayGame() {
		return gameThread.getPlayGame();
	}
	
	public void setGameStepListener(GameStepListener l) {
		if (gameThread == null) {
			return;
		}

		gameThread.setGameStepListener(l);
		gameStep.setGameStepListener(l);
	}

	/**
	 * Sets settings with the following parameters.
	 *
	 * @param s
	 * @param o
	 */
	public void set(String key, Object value) {
		getSettings().set(key, value);
	}

}
