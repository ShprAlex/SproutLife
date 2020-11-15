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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.echosystem.Genome;
import com.sproutlife.model.echosystem.Mutation;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.seed.SeedFactory.SeedType;
import com.sproutlife.model.step.SproutStep;
import com.sproutlife.model.utils.BattleColorUtils;
import com.sproutlife.model.utils.SproutUtils;

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

    public static void loadGenome(File file, GameModel gameModel) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader reader = new BufferedReader(isr);
        int version = loadVersion(reader);
        loadSettings(reader, gameModel);

        int least_color_kind = getAndClearLeastPrevalentColor(gameModel);
        loadOrganisms(reader, gameModel, least_color_kind);
        reader.close();
    }

    private static void saveSettings(PrintWriter writer, GameModel gameModel) {
        Settings s = gameModel.getSettings();
        writer.println(Settings.SEED_TYPE + " : " + s.getString(Settings.SEED_TYPE));
        writer.println(Settings.LIFE_MODE + " : " + s.getString(Settings.LIFE_MODE));
        writer.println(Settings.MAX_LIFESPAN + " : " + s.getInt(Settings.MAX_LIFESPAN));
        writer.println(Settings.TARGET_LIFESPAN + " : " + s.getInt(Settings.TARGET_LIFESPAN));
        writer.println(Settings.CHILD_ONE_PARENT_AGE + " : " + s.getInt(Settings.CHILD_ONE_PARENT_AGE));
        writer.println(Settings.MUTATION_RATE + " : " + s.getInt(Settings.MUTATION_RATE));
        writer.println(Settings.SPROUT_DELAYED_MODE + " : " + s.getBoolean(Settings.SPROUT_DELAYED_MODE));
        writer.println(Settings.PRIMARY_HUE_DEGREES + " : " + s.getInt(Settings.PRIMARY_HUE_DEGREES));
        writer.println(Settings.HUE_RANGE + " : " + s.getInt(Settings.HUE_RANGE));
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
            writer.println("lifespan : "+o.lifespan);
            for (int age = 0; age <= o.lifespan; age++) {
                if (o.getGenome() == null || o.getGenome().getMutationsAtAge(age) == null) {
                    continue;
                }
                for (Mutation m : o.getGenome().getMutationsAtAge(age)) {
                    Point p = m.getLocation();
                    writer.println(age + ", " + p.x + ", " + p.y);
                }
            }
        }
    }

    private static int loadVersion(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        while (line != null && !line.startsWith("V")) {
            line = reader.readLine();
        }
        if (line == null || !line.startsWith("V")) {
            return -1;
        }
        try {
            return Integer.parseInt(line.substring(1));
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private static void loadSettings(BufferedReader reader, GameModel gameModel) throws IOException {
        String line = reader.readLine();
        while (line != null && (!line.contains(":") || line.trim().equalsIgnoreCase("settings"))) {
            line = reader.readLine();
        }
        if (line == null) {
            return;
        }
        if (line.trim().equalsIgnoreCase("settings")) {
            line = reader.readLine();
        }
        while (true) {
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
            line = reader.readLine();
        }
    }

    private static int getAndClearLeastPrevalentColor(GameModel gameModel) {
        Echosystem echosystem = gameModel.getEchosystem();
        ArrayList<Entry<Integer, Integer>> colorCounts = BattleColorUtils.getColorCounts(gameModel.getEchosystem());
        Entry<Integer, Integer> least_color_entry = colorCounts.get(0);
        Entry<Integer, Integer> second_least_color_entry = colorCounts.get(1);
        BattleColorUtils.joinColor(echosystem, least_color_entry.getKey(), second_least_color_entry.getKey());
        int most_color_kind = colorCounts.get(BattleColorUtils.COLOR_COUNT-1).getKey();
        int least_color_kind = least_color_entry.getKey();
        if (second_least_color_entry.getValue()==0) {
            least_color_kind = most_color_kind == 0 ? 2 : 0;
        }
        clearColumn(gameModel, least_color_kind);
        return least_color_kind;
    }

    private static void clearColumn(GameModel gameModel, int colorKind) {
        Collection<Organism> organisms = new ArrayList<>(gameModel.getEchosystem().getOrganisms());
        int boardWidth = gameModel.getEchosystem().getBoard().getWidth();

        int minX = colorKind*boardWidth/3;
        int maxX = (colorKind+1)*boardWidth/3;
        for (Organism o: organisms) {
            if (o.x>=minX && o.x<=maxX) {
                gameModel.getEchosystem().retireOrganism(o);
            }
        }
    }

    private static void loadOrganisms(BufferedReader reader, GameModel gameModel, int colorKind) throws IOException {
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
            Map<String, String> attributes = new HashMap<>();
            String followingLine = loadOrganismAttributes(reader, attributes);

            List<Mutation> genome = loadOrganismGenome(reader, followingLine);
            Organism o = addOrganismToBoard(gameModel, colorKind);

            if (o != null) {
                setOrganismAttributes(o, attributes);
                o.getAttributes().colorKind = colorKind;
                Genome g = o.getGenome();
                for (Mutation m : genome) {
                    g.addMutation(m);
                }
            }
        }
    }

    private static String loadOrganismAttributes(BufferedReader reader, Map<String, String> attributes) throws IOException {
        String line = reader.readLine();
        if (line == null || line.trim().equals("") || (line.contains(",") && !line.contains(":"))) {
            return line;
        }
        if (!line.contains(":")) { // for backward compatibility
            String lifespan = line.trim();
            attributes.put("lifespan", lifespan);
            return reader.readLine();
        }
        while (line.contains(":")) {
            String [] kv = line.split(":");
            attributes.put(kv[0].trim(), kv[1].trim());
            line = reader.readLine();
        }
        // return the last line we read
        return line;
    }

    private static void setOrganismAttributes(Organism o, Map<String, String> attributes) {
        for (String key: attributes.keySet()) {
            String value = attributes.get(key);
            try {
                switch (key) {
                    case "lifespan": o.setLifespan(Integer.parseInt(value)); break;
                }
            }
            catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static List<Mutation> loadOrganismGenome(BufferedReader reader, String firstLine) throws IOException {
        List<Mutation> genome = new ArrayList<>();
        String line = firstLine;
        while (line != null && !line.equals("")) {
            String[] tokens = line.split(",");
            Point p = new Point(Integer.valueOf(tokens[1].trim()), Integer.valueOf(tokens[2].trim()));
            int age = Integer.valueOf(tokens[0].trim());
            genome.add(new Mutation(p, age, 0));
            line = reader.readLine();
        }
        return genome;
    }

    private static Organism addOrganismToBoard(GameModel gameModel, int colorKind) {
        String seedTypeName = gameModel.getSettings().getString(Settings.SEED_TYPE);
        SeedType seedType = SeedType.get(seedTypeName);

        int boardWidth = gameModel.getEchosystem().getBoard().getWidth();
        int x = (new Random()).nextInt(boardWidth/3);
        int y = (new Random()).nextInt(gameModel.getEchosystem().getBoard().getHeight());
        x+= colorKind*boardWidth/3;

        Organism o = SproutUtils.sproutRandomSeed(seedType, gameModel.getEchosystem(), new Point(x,y));
        return o;
    }
}
