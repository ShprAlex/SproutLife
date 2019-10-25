package com.sproutlife.io;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Genome;
import com.sproutlife.model.echosystem.Mutation;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.seed.SeedFactory.SeedType;
import com.sproutlife.model.step.SproutStep;

public class GenomeIo {
    public static int VERSION = 0;

    public static void saveGenome(File file, GameModel gameModel) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("SproutLife Genome");
        writer.println("V" + VERSION);
        writer.println();
        saveSettings(writer, gameModel);
        writer.println();
        saveOrganisms(writer, gameModel);
        writer.close();
    }

    public static void loadGenome(File file, GameModel gameModel, int kind) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader reader = new BufferedReader(isr);
        int version = loadVersion(reader);
        reader.readLine();
        loadSettings(reader, gameModel);
        loadOrganisms(reader, gameModel, kind);
        reader.close();
    }

    private static void saveSettings(PrintWriter writer, GameModel gameModel) {
        Settings s = gameModel.getSettings();
        writer.println(Settings.SEED_TYPE + " : " + s.getString(Settings.SEED_TYPE));
        writer.println(Settings.MAX_LIFESPAN + " : " + s.getInt(Settings.MAX_LIFESPAN));
        writer.println(Settings.TARGET_LIFESPAN + " : " + s.getInt(Settings.TARGET_LIFESPAN));
    }

    private static void saveOrganisms(PrintWriter writer, GameModel gameModel) throws IOException {
        int orgCount = gameModel.getEchosystem().getOrganisms().size();
        int saveOrgCount = Math.min(20, orgCount);
        writer.println("Saved Organisms" + " : " + saveOrgCount);

        ArrayList<Organism> orgList = new ArrayList<>(gameModel.getEchosystem().getOrganisms());
        Collections.shuffle(orgList);

        for (int i = 0; i < saveOrgCount; i++) {
            Organism o = orgList.get(i);
            writer.println();
            writer.println(o.lifespan);
            for (int age = 0; age <= o.lifespan; age++) {
                if (o.getGenome() == null || o.getGenome().getMutations(age) == null) {
                    continue;
                }
                for (Mutation m : o.getGenome().getMutations(age)) {
                    Point p = m.getLocation();
                    writer.println(age + ", " + p.x + ", " + p.y);
                }
            }
        }
    }

    public static int loadVersion(BufferedReader reader) throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                return -1;
            }
            if (line.startsWith("V")) {
                try {
                    return Integer.parseInt(line.substring(1));
                } catch (NumberFormatException ex) {
                    // keep reading until we get a number or end of file;
                }
                ;
            }
        }

    }

    public static void loadSettings(BufferedReader reader, GameModel gameModel) throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line == null || line.trim().isEmpty()) {
                return;
            }
            String[] kv = line.split(" : ");
            if (kv.length != 2) {
                throw new IOException("Error parsing organism settings");
            }

            String k = kv[0];
            String v = kv[1];
            gameModel.getSettings().set(k, v);
        }
    }

    public static void loadOrganisms(BufferedReader reader, GameModel gameModel, int kind) throws IOException {
        String seedTypeName = gameModel.getSettings().getString(Settings.SEED_TYPE);
        SeedType seedType = SeedType.get(seedTypeName);

        String line = reader.readLine();
        while (line != null && !line.startsWith("Saved Organisms")) {
            line = reader.readLine();
        }
        if (line == null) {
            return;
        }

        int orgCount = Integer.valueOf(line.split(" : ")[1]);
        reader.readLine();
        for (int oi = 0; oi < orgCount; oi++) {
            line = reader.readLine();
            int lifespan = Integer.valueOf(line.trim());
            line = reader.readLine();
            List<Mutation> genome = new ArrayList<>();
            while (line != null && !line.equals("")) {
                String[] tokens = line.split(",");
                Point p = new Point(Integer.valueOf(tokens[1].trim()), Integer.valueOf(tokens[2].trim()));
                int age = Integer.valueOf(tokens[0].trim());
                genome.add(new Mutation(p, age, 0));
                line = reader.readLine();
            }

            Organism o = SproutStep.sproutRandomSeed(seedType, gameModel.getEchosystem());

            if (o != null) {
                o.setLifespan(lifespan);
                o.getAttributes().kind = kind;
                Genome g = o.getGenome();
                for (Mutation m : genome) {
                    g.addMutation(m);
                }
            }
        }
    }
}
