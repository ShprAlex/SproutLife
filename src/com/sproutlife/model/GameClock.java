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
    private int time;

    public GameClock() {
        time = 0;
    }

    public int getTime() {
        return time;
    }

    void increment() {
        time++;
    }

    void reset() {
        this.time = 0;
    }
}
