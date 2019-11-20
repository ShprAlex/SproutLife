package com.sproutlife.model.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.sproutlife.model.echosystem.Genome;
import com.sproutlife.model.echosystem.Mutation;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.rotations.Rotations;

public class MutationUtils {

    public static List<Point> getMutationPointsAtAge(Organism o, int age) {
        Genome genome = o.getGenome();
        if (age >= genome.getAgeRange()) {
            return new ArrayList<>();
        }
        List<Mutation> unRotated = genome.getMutationsAtAge(age);
        if (unRotated == null || unRotated.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<Point> mutationPoints = new ArrayList<Point>(unRotated.size());
        for (Mutation m : unRotated) {
            Point rp = Rotations.toBoard(m.getLocation(), o.getSeed().getRotation());
            mutationPoints.add(rp);
        }
        return mutationPoints;
    }
    
    public static List<Point> getOffsetMutationPointsAtAge(Organism o, int age) {
        List<Point> mutationPoints = getMutationPointsAtAge(o, age);
        for (Point mp : mutationPoints) {
            mp.x += o.x;
            mp.y += o.y;
        }
        return mutationPoints;
    }

    public static Mutation addMutation(Organism o, int x, int y) {
        Genome genome = o.getGenome();
        Point location = Rotations.fromBoard(new Point(x, y), o.getSeed().getRotation());
        Mutation m = new Mutation(location, o.getAge(), o.getClock().getTime());
        genome.addMutation(m);
        return m;
    }

    public static Collection<Mutation> getRecentMutations(Organism o, int fromTime, int toTime, int maxAge) {
        Genome genome = o.getGenome();
        HashSet<Mutation> recentMutations = new HashSet<Mutation>();
        for (int age = 0; age<genome.getAgeRange(); age++) {
            List<Mutation> mu = genome.getMutationsAtAge(age);
            if (mu == null) {
                continue;
            }
            for (Mutation m : mu) {
                if (m.getGameTime() >= fromTime && m.getGameTime() <= toTime
                        && m.getOrganismAge() <= maxAge) {
                    recentMutations.add(m);
                }
            }
        }
        return recentMutations;
    }
}
