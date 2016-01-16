package com.sproutlife.model;

import java.util.HashSet;

import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.echosystem.Mutation;
import com.sproutlife.model.echosystem.Organism;

public class Stats {
    GameModel gameModel;
    public int c1, c2, c3, c4;
    
    public int born, die1, die2, stayed;
    public int gridSize;
    
    public int mutationCount;
    public int mutationMiss;
    
    public int avgLife;
    
    public Mutation freqMutation;
    public int freqMuteFreq =0;
    
    public int infectedCount =0;
    
    public int[] childEnergy = new int[20];
    public int[] sproutNumber = new int[20];
    
    
    public Stats(GameModel gameModel) {
        this.gameModel = gameModel;
    }
    
    public GameModel getGame() {
        return gameModel;
    }
    
    private Echosystem getEchosystem() {
        return gameModel.getEchosystem();
    }
    
    private int getClock() {
        return getEchosystem().getClock();
    }
    
    public void printInfected() {
        System.out.print(getClock() + " Org count "+getEchosystem().getOrganisms().size());
        System.out.print(" Avg Life  " + avgLife);
        System.out.println(" Infected born "+infectedCount);
        infectedCount = 0;
    }
    
    public void printChildEnergy() {
        int cellCount = 0;
        for (Organism o : getEchosystem().getOrganisms()) {
            cellCount +=o.getCells().size();
        }
        System.out.print(getClock() + " Org count "+getEchosystem().getOrganisms().size());
        System.out.print(" Cell count " + cellCount);
        System.out.print(" Avg Life " + avgLife);
        System.out.print(" CE:");
        for (int i=0;i<3;i++) {
            if (sproutNumber[i]== 0 ) {
                System.out.print(" 0");
                continue;
            }
            
            int e = childEnergy[i]*10/sproutNumber[i];
            if (i>0 && sproutNumber[i-1]>0 ) {
                e-=(childEnergy[i-1]*10/sproutNumber[i-1]);
            }
            System.out.print(" "+e);
        }

        if (sproutNumber[0]>0) {
            System.out.print(" 12R: "+sproutNumber[1]*1000/sproutNumber[0]);
        }
        if (sproutNumber[0]>0) {
            System.out.print(" 13R: "+sproutNumber[2]*1000/sproutNumber[0]);
        }
        else {
            System.out.print(" 13R: 00");
        }
        

        System.out.println();
        childEnergy = new int[20];
        sproutNumber = new int[20];
    }
    
    public void printMutations() {
        
        
        System.out.print(getClock() + " Org count "+getEchosystem().getOrganisms().size());
        System.out.print(" Avg Life  " + avgLife);
        
        System.out.print(" Mutations: " + mutationCount +" Hit: "+(mutationCount-mutationMiss) + " Percent "+(int) (mutationMiss*100/(mutationCount+0.1)));        
        if (freqMutation!=null) {
            System.out.print(" MaxFreq "+freqMuteFreq+" x "+freqMutation.getLocation().x+" y "+freqMutation.getLocation().y+" time "+freqMutation.getOrganismAge());
        }
        System.out.println();
        //else {
          
        //}
        mutationCount=0;
        mutationMiss=0;
        freqMutation = null;
        freqMuteFreq = 0;

    }
    
    public void printBDS() {
        System.out.print(getClock() + " Org count "+getEchosystem().getOrganisms().size());
        System.out.println(" Grid: "+gridSize+" Born: "+born+" Die1: "+die1+" Die2: "+die2+" Stayed: "+stayed);
    }
    
    public void printGenerations() {
        HashSet<Organism> generation = new HashSet(getEchosystem().getOrganisms());
        
        System.out.print(getClock() + " Org count "+getEchosystem().getOrganisms().size());
           
        for (int i=1;i<60;i++) {
            HashSet<Organism> parents = new HashSet<Organism>();
            HashSet<Organism> remove = new HashSet<Organism>();
            for (Organism o: generation) {

                if(getClock() - o.born < i*115 ) {
                    remove.add(o);
                    Organism p = o.getParent();
                    while (p!=null && (getClock() - p.born < i*115)) {
                        p=p.getParent();
                    }
                    
                    if (p!=null) {                            
                        parents.add(p);
                    }
                }
            }
            generation.removeAll(remove);
            generation.addAll(parents);
            System.out.print(" "+remove.size());
        }
        System.out.println(); 
    }

}
