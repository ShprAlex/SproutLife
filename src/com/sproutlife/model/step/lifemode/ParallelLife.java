/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step.lifemode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class ParallelLife extends SimpleLife {
    
    public ParallelLife(GameModel gameModel) {
        super(gameModel);
    }

    public void updateCells() {
        List<Cell> bornCells = Collections.synchronizedList(new ArrayList<>());
        List<Cell> deadCells = Collections.synchronizedList(new ArrayList<>());

        // Split updating cells into multiple threads for multi-core CPU processing
        int PARTITION_WIDTH = 50;
        List<Thread> threads = new ArrayList<>();
        for (int partitionStart=0; partitionStart < getBoard().getWidth(); partitionStart+=PARTITION_WIDTH) {
            final int partitionStartFinal = partitionStart;
            Thread t = new Thread(new Runnable() {
                public void run() {
                    for (int x=partitionStartFinal; x < getBoard().getWidth() && x < partitionStartFinal+PARTITION_WIDTH; x++) {
                        for (int y=0; y<getBoard().getHeight(); y++) {
                            updateCell(x, y, bornCells, deadCells);
                        }
                    }
                }
            });
            t.start();
            threads.add(t);
        }
        try {
            for (Thread t : threads) {
                t.join();
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        //Remove cells before adding cells to avoid Organism having duplicate cells,
        //Orgs don't do contains() checks for speed
        for (Cell c: deadCells) {
            getEchosystem().removeCell(c);            
        }
        for (Cell c: bornCells) {
            getEchosystem().addCell(c);
        }
    }
}
