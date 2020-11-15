/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.utils.BattleColorUtils;

public class BattleColorStep extends Step {
    GameModel gameModel;
    boolean joinMinorColors = false;

    public BattleColorStep(GameModel gameModel) {
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
        if (getEchosystem().getOrganisms().size() == 0) {
            return;
        }
        ArrayList<Entry<Integer, Integer>> colorCounts = BattleColorUtils.getColorCounts(getEchosystem());
        Entry<Integer, Integer> most_color_entry = colorCounts.get(colorCounts.size() - 1);
        Entry<Integer, Integer> least_color_entry = colorCounts.get(0);

        int largestPercentOfTotal = most_color_entry.getValue() * 100 / getEchosystem().getOrganisms().size();
        if (least_color_entry.getValue() == 0 && largestPercentOfTotal > 70) {
            // if there are only 2 colors left and largest is more than 70% of total, split it
            BattleColorUtils.splitColor(getEchosystem(), most_color_entry.getKey(), least_color_entry.getKey());
        } else if (joinMinorColors && colorCounts.get(1).getValue() > 0 && largestPercentOfTotal > 80) {
            // when the 2 smallest colors are less than 20% of total, join them together
            Entry<Integer, Integer> second_least_color_entry = colorCounts.get(1);
            BattleColorUtils.joinColor(getEchosystem(), least_color_entry.getKey(), second_least_color_entry.getKey());
        }
    }
}
