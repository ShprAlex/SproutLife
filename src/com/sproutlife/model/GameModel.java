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

	/**
	 * Calls Echosystem default constructor.
	 */
	private Echosystem echosystem;

	/**
	 * Calls GameClock default constructor.
	 */
	private GameClock clock;

	/**
	 * Calls GameStep default constructor.
	 */
	private GameStep gameStep;

	/**
	 * Calls GameThread default constructor.
	 */
	private GameThread gameThread;

	/**
	 * Calls Settings default constructor.
	 */
	private Settings settings;

	/**
	 * Calls Stats default constructor.
	 */
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

	/**
	 * Performs a single game step.
	 */
	public void performGameStep() {
		incrementTime();
		gameStep.perform();
	}

	/*
	 * public GameStep getGameStep() { return gameStep; }
	 */

	/**
	 * Echosystem object getter.
	 * 
	 * @return Echosystem object.
	 */
	public Echosystem getEchosystem() {
		return echosystem;
	}

	/**
	 * Board object getter.
	 * 
	 * @return Board object.
	 */
	public Board getBoard() {
		return echosystem.getBoard();
	}

	/**
	 * Time getter.
	 * 
	 * @return Time value.
	 */
	public int getTime() {
		return clock.getTime();
	}

	/**
	 * GamClock object getter.
	 * 
	 * @return GameClock object.
	 */
	public GameClock getClock() {
		return clock;
	}

	/**
	 * Increments time by 1.
	 */
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

	/**
	 * Stats object getter.
	 * 
	 * @return Stats object.
	 */
	public Stats getStats() {
		return stats;
	}

	/**
	 * Settings object getter.
	 * 
	 * @return Settings object.
	 */
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

	/**
	 * GameThread object getter.
	 * 
	 * @return Reference to internal game thread object..
	 */
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

	/**
	 * Game step listener setter.
	 * 
	 * @param l
	 *            Listener reference.
	 */
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
	public void set(String s, Object o) {
		getSettings().set(s, o);
	}

}
