/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.echosystem;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.sproutlife.model.seed.Seed;
import com.sproutlife.model.seed.SymmetricSeed;

/**
 * The board allows us to quickly check for neighbors
 * 
 * Adding and removing cells to the board should be done through the Echosystem
 * class. We mostly avoid error checking for the sake of speed.
 * 
 * @author Alex Shapiro
 */

public class Board {

    private Dimension size = null;

    Cell[][] gameBoard;

    public Cell getCell(int x, int y) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
            return null;
        }
        return gameBoard[x][y];
    }

    public Cell getCell(Point p) {
        return getCell(p.x, p.y);
    }

    public void setCell(Cell c) {
        gameBoard[c.x][c.y] = c;
    }

    public void removeCell(Cell c) {
        clearCell(c.x, c.y);
    }

    public void clearCell(int x, int y) {
        gameBoard[x][y] = null;
    }

    public int getWidth() {
        return size.width;
    }

    public int getHeight() {
        return size.height;
    }

    public void resetBoard() {
        gameBoard = new Cell[getWidth()][getHeight()];
    }

    public void setSize(Dimension d) {
        this.size = d;
    }

    public boolean hasNeighbors(int x, int y) {
        for (int s = -1; s <= 1; s++) {
            for (int t = -1; t <= 1; t++) {
                if (s == 0 && t == 0) {
                    continue;
                }
                if (getCell(x + s, y + t) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Cell> getNeighbors(int x, int y) {
        ArrayList<Cell> surrounding = new ArrayList<Cell>(0);
        for (int s = -1; s <= 1; s++) {
            for (int t = -1; t <= 1; t++) {
                if (s == 0 && t == 0) {
                    continue;
                }
                Cell sp = this.getCell(x + s, y + t);

                if (sp != null) {
                    surrounding.add(sp);
                }
            }
        }
        return surrounding;
    }

    public ArrayList<Cell> getExtra4Neighbors(int x, int y) {
        ArrayList<Cell> neighbors = new ArrayList<Cell>(0);
        Cell c;

        c = getCell(x + 2, y);
        if (c != null)
            neighbors.add(c);
        c = getCell(x - 2, y);
        if (c != null)
            neighbors.add(c);
        c = getCell(x, y + 2);
        if (c != null)
            neighbors.add(c);
        c = getCell(x, y - 2);
        if (c != null)
            neighbors.add(c);

        return neighbors;
    }

    public ArrayList<Cell> getExtra12Neighbors(int x, int y) {
        ArrayList<Cell> neighbors = new ArrayList<Cell>(0);

        Cell c;

        c = getCell(x + 2, y);
        if (c != null)
            neighbors.add(c);
        c = getCell(x - 2, y);
        if (c != null)
            neighbors.add(c);
        c = getCell(x, y + 2);
        if (c != null)
            neighbors.add(c);
        c = getCell(x, y - 2);
        if (c != null)
            neighbors.add(c);

        c = getCell(x + 2, y + 1);
        if (c != null)
            neighbors.add(c);
        c = getCell(x - 2, y + 1);
        if (c != null)
            neighbors.add(c);
        c = getCell(x + 2, y - 1);
        if (c != null)
            neighbors.add(c);
        c = getCell(x - 2, y - 1);
        if (c != null)
            neighbors.add(c);

        c = getCell(x + 1, y + 2);
        if (c != null)
            neighbors.add(c);
        c = getCell(x - 1, y + 2);
        if (c != null)
            neighbors.add(c);
        c = getCell(x + 1, y - 2);
        if (c != null)
            neighbors.add(c);
        c = getCell(x - 1, y - 2);
        if (c != null)
            neighbors.add(c);

        return neighbors;
    }

    public ArrayList<Cell> getExtraNeighbors(int x, int y, int dist) {
        ArrayList<Cell> neighbors = new ArrayList<Cell>(0);
        for (int s = -dist; s <= dist; s++) {
            for (int t = -dist; t <= dist; t++) {
                if ((s == -1 || s == 0 || s == 1)
                        && (t == -1 || t == 0 || t == 1)) {
                    continue;
                }
                Cell sp = this.getCell(x + s, y + t);

                if (sp != null) {
                    neighbors.add(sp);
                }
            }
        }
        return neighbors;
    }

}
