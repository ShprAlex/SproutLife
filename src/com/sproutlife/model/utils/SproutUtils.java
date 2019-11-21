package com.sproutlife.model.utils;

import java.awt.Point;
import java.util.List;
import java.util.Random;

import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.seed.BitPattern;
import com.sproutlife.model.seed.Seed;
import com.sproutlife.model.seed.SeedFactory;
import com.sproutlife.model.seed.SeedSproutPattern;
import com.sproutlife.model.seed.SeedFactory.SeedType;

public class SproutUtils {

    public static Organism sproutRandomSeed(SeedType seedType, Echosystem echosystem, Point location) {
        int x;
        int y;
        if (location!=null) {
            x = location.x;
            y = location.y;
        }
        else {
            x = (new Random()).nextInt(echosystem.getBoard().getWidth());
            y = (new Random()).nextInt(echosystem.getBoard().getHeight());
        }

        List<Seed> seedRotations = SeedFactory.getSeedRotations(seedType);
        Seed s = seedRotations.get((new Random()).nextInt(seedRotations.size()));

        SeedSproutPattern pattern = s.getSeedSproutPattern();
        final int seedWidth = pattern.getSeedPattern().getWidth();
        final int seedHeight = pattern.getSeedPattern().getWidth();
        final BitPattern currentSproutPattern = pattern.getSproutPattern();
        SeedSproutPattern newPattern = new SeedSproutPattern() {
            {
                this.seedPattern = new BitPattern(new int[seedWidth][seedHeight]);

                this.sproutPattern = currentSproutPattern;

                this.sproutOffset = new Point(0, 0);
            }
        };
        s.setPosition(x, y);
        s.setParentPosition(new Point(120, 120));
        Seed randomSeed = new Seed(newPattern, s.getRotation());

        randomSeed.setPosition(x, y);
        randomSeed.setParentPosition(new Point(x + 1, y + 1));

        return sproutSeed(randomSeed, null, echosystem);
    }

    public static Organism sproutSeed(Seed seed, Organism seedOrg, Echosystem echosystem) {
        Point sproutPosition = seed.getSproutPosition();
        Point sproutCenter = seed.getSproutCenter();

        int seedX = seed.getPosition().x;
        int seedY = seed.getPosition().y;

        int sproutX = sproutPosition.x;
        int sproutY = sproutPosition.y;

        int newOrgX = sproutCenter.x;
        int newOrgY = sproutCenter.y;

        int seedWidth = seed.getSeedWidth();
        int seedHeight = seed.getSeedHeight();
 
        int sproutWidth = seed.getSproutWidth();
        int sproutHeight = seed.getSproutHeight();

        if (seedX < 0 || seedY < 0
                || seedX + seedWidth > echosystem.getBoard().getWidth() - 1
                || seedY + seedHeight > echosystem.getBoard().getHeight() - 1
                || sproutX + sproutWidth > echosystem.getBoard().getWidth() - 1
                || sproutY + sproutHeight > echosystem.getBoard().getHeight() - 1
                || sproutX < 0 || sproutY < 0) {
            
            //Clear the seed flag from cells before returning null
            if (seedOrg!=null) {
                for (Cell c : seedOrg.getCells()) {
                    c.setMarkedAsSeed(false);
                }
            }
            return null;
        }

        Organism newOrg = echosystem.createOrganism(newOrgX, newOrgY, seedOrg, seed);

        //Remove old seed
        for (int x=0;x<seedWidth;x++) {
            for (int y=0; y<seedHeight;y++) {
                if (seed.getSeedBit(x, y)) {
                    Cell rc = echosystem.getBoard().getCell(seedX+x, seedY+y);

                    echosystem.removeCell(rc);
                    //getBoard().clearCell(seedX+x,seedY+y);              
                }
            }
        }

        for (int si = 0;si<sproutWidth;si++) {
            for (int sj = 0;sj<sproutHeight;sj++) {

                int i = sproutX+si;
                int j = sproutY+sj;

                Cell c = echosystem.getBoard().getCell(i, j);

                if (c!=null) {   
                    //Only happens when border improperly configured 
                    //and/or successively sprouted seeds overlap each other
                    echosystem.removeCell(c);
                    //getBoard().clearCell(i, j);
                }

                if (seed.getSproutBit(si, sj)) {
                    Cell newC = echosystem.addCell(i,j,newOrg);
                }
                else {
                    //Should be ok
                }
            }
        }
        return newOrg;
    }
}
