package com.sproutlife.model.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.echosystem.Organism;

public class BattleColorUtils {
    public static final int COLOR_COUNT = 3;

    public static ArrayList<Entry<Integer, Integer>> getColorCounts(Echosystem echosystem) {
        HashMap<Integer, Integer> colorCountsMap = new HashMap<>();
        for (int k = 0; k < COLOR_COUNT; k++) {
            colorCountsMap.put(k, 0);
        }
        for (Organism o : echosystem.getOrganisms()) {
            Integer v = colorCountsMap.get(o.getAttributes().colorKind);
            colorCountsMap.put(o.getAttributes().colorKind, v == null ? 1 : v + 1);
        }
        ArrayList<Entry<Integer, Integer>> colorCounts = new ArrayList<>(colorCountsMap.entrySet());
        Collections.sort(colorCounts, (e1, e2) -> (e1.getValue() - e2.getValue()));
        return colorCounts;
    }

    public static void joinColor(Echosystem echosystem, int fromKind, int toKind) {
        for (Organism o : echosystem.getOrganisms()) {
            if (o.getAttributes().colorKind == fromKind) {
                updateOrganismColor(o, toKind);
            }
        }
    }

    public static void splitColor(Echosystem echosystem, int splitKind, int emptyKind) {
        int xSum = 0;
        int sSize = 0;
        for (Organism o : echosystem.getOrganisms()) {
            if (o.getAttributes().colorKind == splitKind) {
                xSum += o.x;
                sSize += 1;
            }
        }

        int avgX = xSum / sSize;
        for (Organism o : echosystem.getOrganisms()) {
            if (o.getAttributes().colorKind == splitKind) {
                if ((avgX > echosystem.getBoard().getWidth() / 2 && o.x > avgX)
                        || (avgX <= echosystem.getBoard().getWidth() / 2 && o.x < avgX)) {
                    updateOrganismColor(o, emptyKind);
                }
            }
        }
    }

    public static void updateOrganismColor(Organism o, int colorKind) {
        // we update the colorKind for all ancestors, because some ancestors get
        // rendered as tail
        o.getAttributes().colorKind = colorKind;
        Organism p = o.getParent();
        while (p != null) {
            p.getAttributes().colorKind = colorKind;
            p = p.getParent();
        }
    }
}
