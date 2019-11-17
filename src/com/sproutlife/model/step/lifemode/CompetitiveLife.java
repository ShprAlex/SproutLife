/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step.lifemode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class CompetitiveLife extends LifeMode {
    private boolean isCompetitive2 = false;
    
    public CompetitiveLife(GameModel gameModel) {
        super(gameModel);
    }

    public void perform() {
        updateMetrics();
        updateCells();
    }

    public void updateMetrics() {
        int targetAge = getSettings().getInt(Settings.TARGET_LIFESPAN);
        for (Organism o: getEchosystem().getOrganisms()) {
            o.getAttributes().maxCells = Math.max(o.getCells().size(), o.getAttributes().maxCells);
            o.getAttributes().cellSum += o.getCells().size();
            o.getAttributes().territory.addAll(o.getCells());
            if (o.getAge()<=targetAge) {
                o.getAttributes().territoryProduct += o.getAttributes().getTerritorySize();
            }
            else {
                double decreaseByFraction = (o.getAge()-targetAge)*(o.getAge()-targetAge)/400.0;
                o.getAttributes().territoryProduct -= o.getAttributes().getTerritorySize()*decreaseByFraction;
            }
            updateCompetitiveScore(o);
        }
    }

    private void updateCompetitiveScore(Organism o) {
        Organism p = o.getParent();
        if (p==null) {
            o.getAttributes().competitiveScore = (int) o.getAttributes().territoryProduct;
            return;
        }

        if (isCompetitive2 ) {
            o.getAttributes().competitiveScore = (int) (p.getAttributes().territoryProduct / p.getChildren().size());
        }
        else {
            o.getAttributes().competitiveScore = (int) p.getAttributes().territoryProduct;
        }
    }

    public double getCompare(Cell c1, Cell c2) {
        Organism o1 = c1.getOrganism();
        Organism o2 = c2.getOrganism();
        return o1.getAttributes().competitiveScore - o2.getAttributes().competitiveScore;
    }

    public void updateCells() {
        isCompetitive2 = "competitive2".equals(getSettings().getString(Settings.LIFE_MODE));

        List<Cell> bornCells = Collections.synchronizedList(new ArrayList<>());
        List<Cell> deadCells = Collections.synchronizedList(new ArrayList<>());

        // Split updating cells into multiple threads for multi-core CPU processing
        int PARTITION_SIZE = 50;
        List<Thread> threads = new ArrayList<>();
        for (int widthPartition=0; widthPartition<getBoard().getWidth();widthPartition+=PARTITION_SIZE) {
            final int widthPartitionFinal = widthPartition;
            Thread t = new Thread(new Runnable() {
                public void run() {
                    for (int x=widthPartitionFinal; x<getBoard().getWidth()&&x<widthPartitionFinal+PARTITION_SIZE; x++) {
                        for (int y=0; y<getBoard().getHeight(); y++) {
                            Cell me = getBoard().getCell(x,y);
                            Cell result = null;
                            boolean wasBorn = false;
                            ArrayList<Cell> neighbors = getBoard().getNeighbors(x,y);

                            if (getBoard().hasNeighbors(x,y)) {
                                result = getBorn(neighbors,x,y);
                                if (result!=null) {
                                    if(me==null || getCompare(result, me)>0) {
                                        bornCells.add(result);
                                        wasBorn=true;
                                        getStats().born++;
                                        if (me!=null) {
                                            deadCells.add(me);
                                        }
                                    }
                                }
                            }
                            if (me!=null && !wasBorn){
                                result = keepAlive(me,neighbors,x,y);
                                if (result!=null) {
                                    getStats().stayed++;
                                }
                                else {
                                    deadCells.add(me);
                                }
                            }
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
        //Orgs don't do Contains checks for speed
        for (Cell c: deadCells) {
            getEchosystem().removeCell(c);            
        }
        for (Cell c: bornCells) {
            getEchosystem().addCell(c);
        }
    } 

    public Cell keepAlive(Cell me, ArrayList<Cell> neighbors, int x, int y) {
        int friendCount = 0;

        for (Cell neighbor : neighbors) {            
            if (me.getOrganism() == neighbor.getOrganism()) {
                friendCount++;
            }
            else if (getCompare(me, neighbor)<0) {
                me.getOrganism().getAttributes().collisionCount++;
                return null;
            }
        }

        if ((friendCount == 2 || friendCount==3)) {
            for (Cell neighbor : getBoard().getExtra12Neighbors(x, y)) {
                if (neighbor.getOrganism()!=me.getOrganism() && neighbor.getOrganism()!=me.getOrganism().getParent()
                        && getCompare(me, neighbor)<0) {
                    me.getOrganism().getAttributes().collisionCount++;
                    return null;
                }
            }
            me.age+=1;
            return me;
        }

        return null;
    }

    public Cell getBorn(ArrayList<Cell> neighbors, int x, int y) {
        if (x<0||x>getBoard().getWidth()-1||y<0||y>getBoard().getHeight()-1) {
            return null;
        }

        Cell maxCell = Collections.max(neighbors, (c1,c2)->(int) getCompare(c1, c2));
        int count = 0;
        for (Cell c : neighbors) {
            if (c.getOrganism()==maxCell.getOrganism()) {
                count+=1;
            }
            else if (getCompare(maxCell, c) == 0) {
                // different organisms with same competitive score
                return null;
            }
        }
        if (count!=3) {
            return null;
        }

        Cell bornCell = getEchosystem().createCell(x, y, maxCell.getOrganism());

        for (Cell neighbor : getBoard().getExtra12Neighbors(x, y)) {
            if (neighbor.getOrganism()!=bornCell.getOrganism() && neighbor.getOrganism()!=bornCell.getOrganism().getParent()
                    && getCompare(bornCell, neighbor)<0) {
                return null;
            }
        }

        return bornCell;
    }
}
