package com.sproutlife.model.echosystem;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.sproutlife.model.seed.Seed;
import com.sproutlife.model.seed.SymmetricSeed;

public class Board {
    
    private Dimension boardSize = null;    
  
    
    Cell[][] gameBoard;                  
    
    public Cell[][] getGameBoard() {
        return gameBoard;
    }
    
    public Cell getCell(int i, int j) {
        if (i<0||j<0||i>=getWidth()||j>=getHeight()) {
            return null;
        }
        return gameBoard[i][j];
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
        return boardSize.width;    
    }
    
    public int getHeight() {
        return boardSize.height;
    }   
 
    public void resetBoard() {
        gameBoard = new Cell[getWidth()][getHeight()];
    }
 
    
    public void setBoardSize(Dimension d) {
        this.boardSize = d;
    }
        
    public boolean hasNeighbors(int i, int j) {
        for (int s=-1;s<=1;s++) {
            for (int t=-1;t<=1;t++) {
                if (s==0 && t==0) {
                    continue;
                }
                if (getCell(i+s, j+t)!=null) {
                    return true;
                }    
            }
        }
        return false;
    }
    
    public ArrayList<Cell> getNeighbors(int i, int j) {
        ArrayList<Cell> surrounding = new ArrayList<Cell>(0);
        for (int s=-1;s<=1;s++) {
            for (int t=-1;t<=1;t++) {
                if (s==0 && t==0) {
                    continue;
                }
                Cell sp = this.getCell(i+s, j+t);
    
                if (sp!=null) { 
                    surrounding.add(sp); 
                }
            }
        }
        return surrounding;
    }
    
    public ArrayList<Cell> getExtra4Neighbors(int i, int j) {
        ArrayList<Cell> neighbors = new ArrayList<Cell>(0);
        Cell c;
 
        c = getCell(i+2,j);
        if (c!=null) neighbors.add(c);
        c = getCell(i-2,j);
        if (c!=null) neighbors.add(c);
        c = getCell(i,j+2);
        if (c!=null) neighbors.add(c);
        c = getCell(i,j-2);
        if (c!=null) neighbors.add(c);
        
        return neighbors;     
    }
    
    public ArrayList<Cell> getExtra12Neighbors(int i, int j) {
        ArrayList<Cell> neighbors = new ArrayList<Cell>(0);
        
        Cell c;
        
        c = getCell(i+2,j);
        if (c!=null) neighbors.add(c);
        c = getCell(i-2,j);
        if (c!=null) neighbors.add(c);
        c = getCell(i,j+2);
        if (c!=null) neighbors.add(c);
        c = getCell(i,j-2);
        if (c!=null) neighbors.add(c);        
        
        c = getCell(i+2,j+1);
        if (c!=null) neighbors.add(c);
        c = getCell(i-2,j+1);
        if (c!=null) neighbors.add(c);
        c = getCell(i+2,j-1);
        if (c!=null) neighbors.add(c);
        c = getCell(i-2,j-1);
        if (c!=null) neighbors.add(c);
        
        c = getCell(i+1,j+2);
        if (c!=null) neighbors.add(c);
        c = getCell(i-1,j+2);
        if (c!=null) neighbors.add(c);
        c = getCell(i+1,j-2);
        if (c!=null) neighbors.add(c);
        c = getCell(i-1,j-2);
        if (c!=null) neighbors.add(c);
        
        return neighbors;     
    }
    
    public ArrayList<Cell> getExtraNeighbors(int i, int j, int dist) {
        ArrayList<Cell> neighbors = new ArrayList<Cell>(0);
        for (int s=-dist;s<=dist;s++) {
            for (int t=-dist;t<=dist;t++) {
                if ((s==-1 || s==0 || s==1) && (t==-1 || t==0 || t==1)) {
                    continue;
                }
                Cell sp = this.getCell(i+s, j+t);
    
                if (sp!=null) { 
                    neighbors.add(sp); 
                }
            }
        }
        return neighbors;
    }
    
}
