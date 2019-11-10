/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Organism;

public class ColorsStep extends Step {
    GameModel gameModel;
    int COLOR_COUNT = 3;
    boolean joinMinorColors = false;

    public ColorsStep(GameModel gameModel) {
        super(gameModel);
    }

    public void perform() {
        if (getSettings().getBoolean(Settings.AUTO_SPLIT_COLORS)) {
            splitColors();
        }
    }

    public void setJoinMinorColors(boolean joinMinorColors) {
        this.joinMinorColors = joinMinorColors;
    }

    private void splitColors() {
        if (getEchosystem().getOrganisms().size() ==0) {
            return;
        }
        ArrayList<Entry<Integer,Integer>> kindCount = getColorCounts();
        int largestPercentOfTotal = kindCount.get(kindCount.size()-1).getValue()*100/getEchosystem().getOrganisms().size();

        if (kindCount.get(0).getValue()==0 && largestPercentOfTotal>70) {
            // if there are only 2 colors left and largest is more than 70% of total, split it
            splitColor(kindCount.get(kindCount.size()-1).getKey(), kindCount.get(0).getKey());
        }
        else if (joinMinorColors && kindCount.get(1).getValue()>0 && largestPercentOfTotal>80) {
            // when the 2 smallest colors are less than 20% of total, join them together
            joinColor(kindCount.get(0).getKey(), kindCount.get(1).getKey());
        }
    }

    private ArrayList<Entry<Integer,Integer>> getColorCounts() {
        HashMap<Integer, Integer> kindCountMap = new HashMap<>();
        for (int k=0;k<COLOR_COUNT;k++) {
            kindCountMap.put(k,0);
        }
        for (Organism o : getEchosystem().getOrganisms()) {
            Integer v = kindCountMap.get(o.getAttributes().colorKind);
            kindCountMap.put(o.getAttributes().colorKind, v == null ? 1 : v+1);
        }
        ArrayList<Entry<Integer,Integer>> kindCount = new ArrayList<>(kindCountMap.entrySet());
        Collections.sort(kindCount, (e1, e2)->(e1.getValue()-e2.getValue()));
        return kindCount;
    }

    private void joinColor(int fromKind, int toKind) {
        for (Organism o : getEchosystem().getOrganisms()) {
            if (o.getAttributes().colorKind==fromKind) {
                o.getAttributes().colorKind=toKind;
            }
        }
    }

    private void splitColor(int splitKind, int emptyKind) {
        int xSum = 0;
        int sSize = 0;
        for (Organism o : getEchosystem().getOrganisms()) {
            if (o.getAttributes().colorKind == splitKind) {
                xSum += o.x;
                sSize +=1;
            }
        }

        int avgX = xSum / sSize;
        for (Organism o : getEchosystem().getOrganisms()) {
            if (o.getAttributes().colorKind==splitKind) {
                if ((avgX > getBoard().getWidth()/2 && o.x > avgX) ||
                        (avgX <= getBoard().getWidth()/2 && o.x < avgX)) {
                    o.getAttributes().colorKind = emptyKind;
                }
            }
        }
    }
}
