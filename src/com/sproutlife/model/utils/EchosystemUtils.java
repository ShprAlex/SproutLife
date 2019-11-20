package com.sproutlife.model.utils;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;

import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.echosystem.Organism;

public class EchosystemUtils {

    public static void updateBoardBounds(Echosystem echosystem, Rectangle bounds) {
        echosystem.getBoard().setSize(new Dimension(bounds.width, bounds.height));

        echosystem.getBoard().resetBoard();

        ArrayList<Cell> removeList = new ArrayList<Cell>(0);

        for (Organism o : echosystem.getOrganisms()) {
            Point ol = o.getLocation();
            o.setLocation(ol.x-bounds.x , ol.y-bounds.y);
            for (Cell current : o.getCells()) {
                if (current.x < bounds.x || current.x >= echosystem.getBoard().getWidth()+bounds.x
                        || current.y < bounds.y || current.y >= echosystem.getBoard().getHeight()+bounds.y) {
                    removeList.add(current);
                }
                else {
                    Point cl = current.getLocation();
                    current.setLocation(new Point(cl.x-bounds.x, cl.y-bounds.y));
                    echosystem.getBoard().setCell(current);
                }
            }
        }
        for (Organism o : echosystem.getRetiredOrganisms()) {
            Point ol = o.getLocation();
            o.setLocation(ol.x-bounds.x , ol.y-bounds.y);
        }
        for (Cell r : removeList) {
            echosystem.removeCell(r, true);
        }
        pruneEmptyOrganisms(echosystem);
    }

    /*
     * Make sure the all cells on the board are contained within organisms, and
     * that all cells in organisms are on the board.
     * 
     * @return if the board isn't consistent with organisms, return false
     */
    public static boolean validateBoard(Echosystem echosystem) {
        for (Organism o : echosystem.getOrganisms()) {
            for (Cell current : o.getCells()) {
                if (echosystem.getBoard().getCell(current.x, current.y) != current) {
                    return false;
                }
            }
        }
        for (int i = 0; i < echosystem.getBoard().getWidth(); i++) {
            for (int j = 0; j < echosystem.getBoard().getHeight(); j++) {
                Cell c = echosystem.getBoard().getCell(i, j);
                if (c != null && c.getOrganism().getCell(c.x, c.y) != c) {
                    return false;
                }

            }
        }

        return true;
    }
    
    public static void pruneEmptyOrganisms(Echosystem echosystem) {
        HashSet<Organism> pruneOrgs = new HashSet<Organism>();
        pruneOrgs.addAll(echosystem.getOrganisms());
        for (Organism org : pruneOrgs) {
            if (org.getCells().size() == 0) {
                echosystem.retireOrganism(org);
            }
        }
    }
}
