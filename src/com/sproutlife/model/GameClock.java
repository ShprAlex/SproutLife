/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model;

/**
 * This class is used to manage the time in the game.
 * 
 * @author Alex Shapiro
 */
public class GameClock {
	/**
	 * Discrete time internal counter.
	 */
	private int time;

	/**
	 * Construction without parameters.
	 */
	public GameClock() {
		time = 0;
	}

	/**
	 * Time getter.
	 * 
	 * @return Discrete time value.
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Increments discrete time value.
	 */
	void increment() {
		time++;
	}

	/**
	 * Resets discrete time value.
	 */
	void reset() {
		this.time = 0;
	}
}
