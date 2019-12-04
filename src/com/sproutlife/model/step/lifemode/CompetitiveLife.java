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

public class CompetitiveLife extends LifeMode {
    private boolean isCompetitive2 = false;
    
    public CompetitiveLife(GameModel gameModel) {
        super(gameModel);
    }

    public void perform() {
        isCompetitive2 = "competitive2".equals(getSettings().getString(Settings.LIFE_MODE));
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

        int cs = (int) p.getAttributes().territoryProduct;
        if(isCompetitive2) {
            while (p.getChildren().size()==1) {
                p=p.getParent();
                if (p!=null) {
                    // include the first ancestor with child count > 1
                    cs = (int) Math.max(cs, p.getAttributes().territoryProduct);
                }
                else {
                    break;
                }
            }
        }
        o.getAttributes().competitiveScore = cs;
    }

    public double getCompare(Cell c1, Cell c2) {
        Organism o1 = c1.getOrganism();
        Organism o2 = c2.getOrganism();
        return o1.getAttributes().competitiveScore - o2.getAttributes().competitiveScore;
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
        //Orgs don't do Contains checks for speed
        for (Cell c: deadCells) {
            getEchosystem().removeCell(c);            
        }
        for (Cell c: bornCells) {
            getEchosystem().addCell(c);
        }
    }

    private void updateCell(int x, int y, List<Cell> bornCells, List<Cell> deadCells) {
        Cell c = getBoard().getCell(x,y);
        Cell bc = null;
        boolean wasBorn = false;
        ArrayList<Cell> neighbors = getBoard().getNeighbors(x,y);

        if (neighbors.size()>=3) {
            bc = getBorn(neighbors,x,y);
            if (bc!=null && (c==null || getCompare(bc, c)>0)) {
                bornCells.add(bc);
                wasBorn=true;
                getStats().born++;
                if (c!=null) {
                    deadCells.add(c);
                }
            }
        }
        if (c!=null && !wasBorn){
            if (!keepAlive(c,neighbors,x,y)) {
                deadCells.add(c);
            }
            else {
                getStats().stayed++;
            }
        }
    }

    public boolean keepAlive(Cell c, Collection<Cell> neighbors, int x, int y) {
        int sameOrgCount = 0;

        for (Cell neighbor : neighbors) {            
            if (c.getOrganism() == neighbor.getOrganism()) {
                sameOrgCount++;
            }
            else if (getCompare(c, neighbor)<0) {
                c.getOrganism().getAttributes().collisionCount++;
                return false;
            }
        }

        if ((sameOrgCount == 2 || sameOrgCount==3)) {
            if(!isCompetitive2) {
                for (Cell neighbor : getBoard().getExtra12Neighbors(x, y)) {
                    if (neighbor.getOrganism()!=c.getOrganism() && neighbor.getOrganism()!=c.getOrganism().getParent()
                            && getCompare(c, neighbor)<0) {
                        c.getOrganism().getAttributes().collisionCount++;
                        return false;
                    }
                }
            }
            c.age+=1;
            return true;
        }
        return false;
    }

    public Cell getBorn(Collection<Cell> neighbors, int x, int y) {
        if (x < 0 || x > getBoard().getWidth()-1 || y < 0 || y > getBoard().getHeight()-1) {
            return null;
        }

        Cell maxCell = Collections.max(neighbors, (c1,c2)->(int) getCompare(c1, c2));
        int count = 0;
        for (Cell c : neighbors) {
            if (c.getOrganism() == maxCell.getOrganism()) {
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
        if(!isCompetitive2) {
            for (Cell neighbor : getBoard().getExtra12Neighbors(x, y)) {
                if (neighbor.getOrganism()!=bornCell.getOrganism() && neighbor.getOrganism()!=bornCell.getOrganism().getParent()
                        && getCompare(bornCell, neighbor)<0) {
                    return null;
                }
            }
        }
        return bornCell;
    }
}
