/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.echosystem.Mutation;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.utils.MutationUtils;

public class Stats {
    GameModel gameModel;

    public double smoothedPopulation=0;
    public double smoothedPopDensity=0;
    public int smoothedResetTimer = 0;

    public double avgAge;
    public double avgLifespan;
    public int avgMaxLifespan;
    public int gameTime = 0;
    public double boardSizeDivPopulation;
    double lifespanHistogram[] = new double[300];

    double avgSize = 0;
    double avgMaxSize = 0;
    double avgCellSum = 0;
    public double avgCompetitiveScore = 0;
    double[] avgCellsAtAge = new double[300];

    double avgChildNumber = 0;
    double[] parentAgeAtBirthHist = new double[300];
    double[] childNumberPercent = new double[10];
    double childlessPercent = 0;

    double avgTotalMutations = 0;
    double avgMutationAge = 0;
    //visualize all mutations on a 30x30 grid
    double mutationGridtXY[][] = new double[30][30];
    double mutationGridtXYold[][][] = new double[20][30][30];

    double mutationAgeHistogram[] = new double[300];

    double mutationSpeed = 0;
    double mutationDiversity = 0;
    double mutationDiversityAge = 0;

    public int c1, c2, c3, c4;

    public int born, die1, die2, stayed;
    public int gridSize;

    public int mutationCount;
    public int mutationMiss;

    public Stats(GameModel gameModel) {
        this.gameModel = gameModel;        
    }

    public void reset() {
        smoothedPopulation = 0;
        avgCompetitiveScore = 0;
        avgTotalMutations = 0;
      
    }
    
    private Echosystem getEchosystem() {
        return gameModel.getEchosystem();
    }
    
    private int getTime() {
        return gameModel.getTime();
    }
    
    public String getDisplayText() {
        final String HEADER_ROW = "<html><body style='font-family:ariel;'>";
        final String START_TABLE = "<table width='100%' cellspacing='0' cellpadding='0'>";
        final String END_TABLE = "</table>";
        final String BLANK_ROW = "<tr style='height:5px;'></tr>";
        final String FOOTER_ROW = "</body></html>";
        String text = "";
        
        text += HEADER_ROW;                
        text += START_TABLE;
        text += buildDisplayRow("Game Time: ",gameTime);
        text += buildDisplayRow("Population (Organism #): ",getEchosystem().getOrganisms().size());
        text += buildDisplayRow("Smoothed Population: ",(int) (smoothedPopulation+0.5)); //+0.5 to do average vs. floor
        text += buildDisplayRowBold("Smoothed Pop Density: ",String.format("%.1f", smoothedPopDensity)); //+0.5 to do average vs. floor
        text += BLANK_ROW;        
        text += buildDisplayRow("Average Age: ",String.format("%.1f", avgAge));
        text += buildDisplayRow("Average Lifespan: ",String.format("%.1f", avgLifespan));
        text += buildDisplayRowBold("Average Self-Destruct Age: ",String.format("%.1f", avgMaxLifespan/10.0+1));
        text += BLANK_ROW;
        text += buildDisplayRow("Average Child #: ",String.format("%.3f", avgChildNumber));
        text += buildDisplayRow("Died Childless %: ",String.format("%.1f", childlessPercent));
        text += buildDisplayRow("1 child %\t: ",String.format("%.1f", (childNumberPercent[0]-childNumberPercent[1])));
        text += buildDisplayRow("2 chilren %\t: ",String.format("%.1f", (childNumberPercent[1]-childNumberPercent[2])));
        text += buildDisplayRow("3 chilren %\t: ",String.format("%.1f", (childNumberPercent[2]-childNumberPercent[3])));
        text += buildDisplayRow("4+ chilren %\t: ",String.format("%.1f", (childNumberPercent[4])));
        text += BLANK_ROW;
        text += buildDisplayRow("Average Cell #: ",String.format("%.1f", avgSize));       
        text += buildDisplayRow("Avg Current Life Cell Sum: ",String.format("%.0f", avgCellSum));        
        text += buildDisplayRowBold("Avg Competitive Score: ",String.format("%.0f", avgCompetitiveScore));
        text += buildDisplayRow("% Child born at parent age histogram: ");
        text += buildParentAgeAtBirthHistogram();
        text += BLANK_ROW;        
        text += buildDisplayRowBold("Average Mutation #: ",String.format("%.1f", avgTotalMutations)); 
        text += buildDisplayRowBold("Mutation Diversity: ",String.format("%.1f", mutationDiversity*10));        
        text += buildDisplayRowBold("Evolution speed (avg genome vector delta): ",String.format("%.1f", mutationSpeed));        
        
        text += buildDisplayRow("Average Mutation Age: ",String.format("%.1f", avgMutationAge));
        text += buildDisplayRow("Mutation Diversity Age: ",String.format("%.1f", mutationDiversityAge));
        
        text += buildDisplayRow("Mutation age histogram: ");
        text += buildMutationAgeHistogram();
        text += buildDisplayRow("Avg Genome, mutat prevalence: ");
        text += buildDisplayRow("2=20%, $=95%, #=99% ");        
        text += buildGenomeGrid();
        
        
        text += END_TABLE;
        text += FOOTER_ROW;
        
        return text;
    }
    
    public void update() {
        gameTime = getTime();
        updateSizeStats();
        updateLifespanStats();
        updateChildStats();
        updateMutationStats();
        logStats();
    }
    
    private String buildDisplayRow(String label, Object value) {
        return "<tr><td>"+label+"</td><td align='right'>"+value+"</td></tr>";
    }
    
    private String buildDisplayRow(String label) {
        return "<tr><td colspan='2'>"+label+"</td></tr>";
    }
    
    private String buildDisplayRowBold(String label, Object value) {
        return buildDisplayRow(label, "<b>"+value+"</b>");
    }
    
    private String buildGenomeGrid() {
        String text = "";
        //This is a temporary ascii based visualizatoin
        //we're switching the x,y axis because the r-pentomino
        //causes the organism to grow and mutate horizontally, and we
        //have more room vertically.
        for (int i=0;i<14;i++) {
            String mutationHist = "";
            for (int j=0;j<18;j++) {
                if (mutationGridtXY[i][j]<1) {
                    mutationHist+=" "+String.format("_");
                }
                else if (mutationGridtXY[i][j]>=9.9) {
                    mutationHist+=" "+String.format("#");
                }
                else if (mutationGridtXY[i][j]>=9.5) {
                    mutationHist+=" "+String.format("$");
                }
                else {
                    //if (Math.abs(mutationGridtXYold[12][i][j]-mutationGridtXY[i][j])>2) {
                    //    mutationHist+=" <b>"+String.format("%.0f",mutationGridtXY[i][j])+"</b>";                        
                    //}
                    //else {
                        mutationHist+=" "+String.format("%.0f",mutationGridtXY[i][j]);
                    //}
                }
            }
            text += buildDisplayRow(mutationHist);
        }
        return text;

    }
    
    private String buildMutationAgeHistogram() {
        String text = "";

        for (int r=0;r<4;r++) {
            String mutationHist = "";
            for (int t=0;t<10;t++) {
                if (mutationAgeHistogram[r*10+t]<0.1) {
                    mutationHist+=" "+"__";
                }
                else {
                    mutationHist+=" ";
                    if (mutationAgeHistogram[r*10+t]+0.05<1) {
                        mutationHist+="_";
                    }
                    
                    mutationHist+=String.format("%.0f",mutationAgeHistogram[r*10+t]*10);
                    
                }
            }
            text += buildDisplayRow(mutationHist);
        }
        return text;

    }

    private String buildParentAgeAtBirthHistogram() {
        String mutationHist = "";
        for (int t = 1; t <= 80; t++) {
            double v = parentAgeAtBirthHist[t];
            if (v < 1) {
                mutationHist += " " + "__";
            } else {
                mutationHist += " ";
                if (v + 0.5 < 10) {
                    mutationHist += "_"; // prepend underscore to single digit values
                }
                mutationHist += String.format("%.0f", v);
            }
            if (t % 10 == 0) {
                mutationHist += "<br/>";
            }
        }
        return buildDisplayRow("<span style='font-family:monospace; font-size:10px'>" + mutationHist + "</span>");
    }

    private String buildParentAgeAtBirthHistogramForLog() {
        String mutationHist = "";
        for (int t = 0; t < 75; t++) {
            double v = parentAgeAtBirthHist[t];

            if (v + 0.5 < 10) {
                mutationHist += " ";
            }
            mutationHist += String.format(" %.0f", v);
        }
        return mutationHist;
    }

    public void updateSmoothedPopulation() {
        int population = getEchosystem().getOrganisms().size();
        if (population<100) {
            smoothedPopulation = (smoothedPopulation*99+population)/100;
        }
        else {
            // extra smooth
            smoothedPopulation = (smoothedPopulation*999+population)/1000;
        }

        smoothedPopDensity = smoothedPopulation*10000/gameModel.getBoard().getHeight()/gameModel.getBoard().getWidth();
        
    }
    
    public void printHistogram() {
        int cellCount = 0;

        
        for (Organism o : getEchosystem().getOrganisms()) {
            int oc = o.size();
            cellCount += oc;
        }
        
        int[] countAtAge = new int[100];
        int[] countAtSize = new int[100];
        int[] countAtLifespan = new int[200];
        int[] maxSizeAtLifespan = new int[200];
        int[] maxAgeAtLifespan = new int[200];
        int[] ageHalfLifespan = new int[200];
        int[] sizeAtAge = new int[100];
        int[] mutationCount = new int[100];
        
        

        for (Organism o : getEchosystem().getOrganisms()) {
            int oc = o.size();
            cellCount += oc;

            countAtSize[o.size()/5]+=1;            
            countAtAge[o.getAge()/5]+=1;
            countAtLifespan[o.lifespan/2]+=1;
            //int mc = o.getGenome().getRecentMutations(0,getEchosystem().getTime(),o.lifespan).size();
            //mutationCount[mc/3]++;
            if (oc>maxSizeAtLifespan[o.lifespan/2]) {
                maxSizeAtLifespan[o.lifespan/2]=oc;
            }
            if (o.getAge()>maxAgeAtLifespan[o.lifespan/2]) {
                maxAgeAtLifespan[o.lifespan/2]=oc;
            }
            if (o.getAge()>=o.lifespan/2) {
                ageHalfLifespan[o.lifespan/2]++;;
            }
            
            
            sizeAtAge[o.getAge()/5]+=o.size();
        }               
        
        System.out.print(getTime() + " Org count "+getEchosystem().getOrganisms().size());
        System.out.print(" Cell count " + cellCount);
        System.out.print(" Avg cells " + cellCount*10/getEchosystem().getOrganisms().size());
        /*
        System.out.print(" Count at age: ");
        for (int i=0;i<10;i++) {
            System.out.print(countAtAge[i]+" ");
        }
        */
        /*
        System.out.print(" Count at size: ");
        for (int i=0;i<10;i++) {
            System.out.print(countAtSize[i]+" ");
        }
        */
        
        System.out.print(" Count at lifespan: ");
        for (int i=6;i<50;i++) {
            System.out.print(countAtLifespan[i]+" ");
        }
        
        System.out.print(" ACS: "+(int) avgCompetitiveScore);
        /*
        System.out.print(" Max cells: ");               
        for (int i=0;i<50;i++) {
            System.out.print(maxCells[i]+" ");
        }
        */
        /*
        System.out.print(" MC: ");
        for (int i=0;i<50;i++) {
            System.out.print(mutationCount[i]+" ");
        }
        */
        /*
        System.out.print(" Size at age: ");
        for (int i=0;i<10;i++) {
            if (countAtAge[i]>0) {
                System.out.print(sizeAtAge[i]*10/countAtAge[i]+" ");
            }
            else {
                System.out.print("0 ");
            }
        }
        */
    }

    public void printMutations() {
        System.out.print(getTime() + " Org count "+getEchosystem().getOrganisms().size());
        System.out.print(" Avg Life  " + avgMaxLifespan);
        System.out.print(" RM count: "+getRecentMutationCount(10000,1000));
        System.out.print(" Mutations: " + mutationCount +" Hit: "+(mutationCount-mutationMiss) + " Percent "+(int) (mutationMiss*100/(mutationCount+0.1)));
        System.out.println();
        //else {
          
        //}
        mutationCount=0;
        mutationMiss=0;
    }

    private int getRecentMutationCount(int fromAge, int toAge) {
        HashSet<Mutation> recentMutations = new HashSet<Mutation>();
        HashMap<Mutation,Integer> totalRM = new HashMap<Mutation,Integer>();

        for (Organism o: getEchosystem().getOrganisms()) {
            int fromTime = getEchosystem().getTime()-fromAge;
            int toTime = getEchosystem().getTime()-toAge;
            for(Mutation m: MutationUtils.getRecentMutations(o, fromTime, toTime, o.lifespan)) {
                recentMutations.add(m);
                Integer mCount = totalRM.get(m);
                if (mCount==null) {
                    mCount = 0;
                }
                mCount++;
                totalRM.put(m, mCount);
            }
        }
        int totalCount = 0;
        for (Integer mc : totalRM.values()) {
            totalCount +=mc;
        }
        return totalCount;//recentMutations.size();
    }
    
    
    public void printBDS() {
        System.out.print(getTime() + " Org count "+getEchosystem().getOrganisms().size());
        System.out.println(" Grid: "+gridSize+" Born: "+born+" Die1: "+die1+" Die2: "+die2+" Stayed: "+stayed);
    }
    
    public int getOldestCommonAncestorAge() {
        HashSet<Organism> generation = new HashSet(getEchosystem().getOrganisms());
        HashMap<Organism,Integer> descendantMap = new HashMap<Organism,Integer>();    
        
        //System.out.print(getTime() + " Org count "+getEchosystem().getOrganisms().size());
        
        for (int i=1;i<200;i++) {
            HashSet<Organism> parents = new HashSet<Organism>();
            HashSet<Organism> remove = new HashSet<Organism>();
            for (Organism o: generation) {

                if(o.getAge() < i*115 ) {
                    remove.add(o);
                    Organism p = o.getParent();
                    while (p!=null && (p.getTimeSinceBorn() < i*115)) {
                        
                        Integer dCount = descendantMap.get(p);
                        if (dCount==null) {
                            dCount =0;
                        }
                        descendantMap.remove(p);
                        
                        p=p.getParent();
                        
                        if(p!=null){
                            Integer pdCount = descendantMap.get(p);
                            if (pdCount==null) {
                                pdCount =0;
                            }
                            descendantMap.put(p, dCount+pdCount);
                        }
                        
                    }
                    
                    if (p!=null) {                     
                        Integer dCount = descendantMap.get(p);
                        if (dCount==null) {
                            dCount =0;
                        }
                        Integer odCount = descendantMap.get(o);
                        if (odCount==null) {
                            odCount =1;
                        }
                        descendantMap.put(p, dCount+odCount);
                        parents.add(p);
                    }
                }
            }
            
            generation.removeAll(remove);
            generation.addAll(parents);

            for (Organism r:remove) {
                descendantMap.remove(r);
            }
            if (descendantMap.size()>10) {
                int x=5;
            }
            for (Organism ancestor:generation) {
                if (descendantMap.get(ancestor)>getEchosystem().getOrganisms().size()/3) {
                    return ancestor.getTimeSinceBorn();
                }
            }
            //if (remove.size()==1) {
            //    return remove.iterator().next().getTimeSinceBorn();
                
            //}
            //System.out.print(" "+remove.size());
        }
       
        return 10000;
        //System.out.println(); 
    }
    
    private void updateSizeStats() {
        int maxSizeSum = 0;
        int sizeSum = 0;
        int combinedCellSum = 0;
        int competitiveScoreSum = 0;
        for (Organism o : getEchosystem().getOrganisms()) {
            sizeSum += o.size();
            combinedCellSum +=o.getAttributes().cellSum;
            if(o.getParent()!=null) {
                int ms = o.getParent().getAttributes().maxCells;
                if (o.getParent().getParent()!=null) {
                    ms = Math.max(ms, o.getParent().getAttributes().maxCells);
                }
                maxSizeSum+=ms;
            }
            competitiveScoreSum+=o.getAttributes().competitiveScore;
        }
        for (Organism o : getEchosystem().getRetiredOrganisms()) {
            competitiveScoreSum+=o.getAttributes().competitiveScore;
        }

        int maxCellsAtAge[] = new int[1000]; 
        for (Organism o : getEchosystem().getOrganisms()) {
            maxCellsAtAge[o.getAge()] = Math.max(maxCellsAtAge[o.getAge()], o.getCells().size());
        }
        for (int i=0;i<100;i++) {
            if (maxCellsAtAge[i]>0) {
                avgCellsAtAge[i]=(avgCellsAtAge[i]*9+maxCellsAtAge[i])/10;
            }
            else {
                avgCellsAtAge[i]=(avgCellsAtAge[i]*99+maxCellsAtAge[i])/10;
            }
        }
        double population = (double) getEchosystem().getOrganisms().size();
        double historicalPopulation = population + getEchosystem().getRetiredOrganisms().size();
        if (population>0) {
            this.avgSize = sizeSum / population;
            this.avgCellSum = combinedCellSum / population;
            this.avgMaxSize = maxSizeSum/ population;
            this.avgCompetitiveScore = competitiveScoreSum/ historicalPopulation;
            this.boardSizeDivPopulation = gameModel.getBoard().getHeight()*gameModel.getBoard().getWidth()/smoothedPopulation;
        }
    }
    
    private void updateLifespanStats() {
        int ageSum = 0;
        for (Organism o : getEchosystem().getOrganisms()) {            
            ageSum +=o.getAge();                           
        }
        if (getEchosystem().getOrganisms().size()>0) {
            this.avgAge = ageSum / (double) getEchosystem().getOrganisms().size();
        }
        else {
            this.avgAge = 0;
        }
        
        int lifespanSum = 0;
        for (Organism o : getEchosystem().getRetiredOrganisms()) {
            lifespanSum +=o.getAge();
        }
        if (getEchosystem().getRetiredOrganisms().size()>0) {
            this.avgLifespan = lifespanSum / (double) getEchosystem().getRetiredOrganisms().size();
        }
        else {
            this.avgLifespan = 0;
        }
        
        int maxLifespanSum = 0;

        for (Organism o : getEchosystem().getOrganisms()) {
            maxLifespanSum +=o.lifespan;
        }
        this.avgMaxLifespan = maxLifespanSum*10/ getEchosystem().getOrganisms().size();

    }
    
    public void updateChildStats() {
        int sumChildNumber = 0;

        int[] childNumberHistogram = new int[5]; //keep track of number number organisms having x number of childern
        parentAgeAtBirthHist = new double[100]; 
        for (Organism o : getEchosystem().getRetiredOrganisms()) {
            sumChildNumber += o.getChildren().size();
            int parentAgeAtBirth =  o.getAttributes().parentAgeAtBirth;
            parentAgeAtBirth = Math.min(parentAgeAtBirth, 74);
            parentAgeAtBirthHist[parentAgeAtBirth]++;

            for (int ci = 0; ci<o.getChildren().size(); ci++) {
                if (ci<4) {
                    childNumberHistogram[ci]+=1;
                }
                else {
                    childNumberHistogram[4]+=1;
                }
            }
        }

        avgChildNumber = 0;
        childNumberPercent = new double[5];
        int retiredOrgSize = getEchosystem().getRetiredOrganisms().size();
        
        if (getEchosystem().getRetiredOrganisms().size()>0) {
            if (sumChildNumber>0) {
                for (int i=0;i<100;i++) {
                    parentAgeAtBirthHist[i] *= (100.0/retiredOrgSize);
                }
            }
            avgChildNumber = sumChildNumber / (double) getEchosystem().getRetiredOrganisms().size();
            childlessPercent = (getEchosystem().getRetiredOrganisms().size()-childNumberHistogram[0])*100.0/getEchosystem().getRetiredOrganisms().size();

            for (int ci = 0; ci<5;ci++) {
                if (sumChildNumber>0) {
                    childNumberPercent[ci] = childNumberHistogram[ci]*100.0 / getEchosystem().getRetiredOrganisms().size();
                }
            }                

        }
    }
    
    public void updateMutationStats() {
        int mutationSum=0;
        int mutationAgeSum=0;
        int mutationSumX=0;
        int mutationSumY=0;
        int mutationSumLate=0;
        int mutationSumLateX=0;
        int mutationSumLateY=0;
        double population = getEchosystem().getOrganisms().size();
        
        double mutationDistSum = 0 ;
        mutationDiversity = 0;
        mutationDiversityAge = 0;
        HashMap<Mutation,Integer> mutationFreq = new HashMap<Mutation,Integer>();
        
        for (int i=0;i<30;i++) {
            for (int j=0;j<30;j++) {
                mutationGridtXY[i][j]=0;               
            }
        }  
        for (int t=0;t<mutationAgeHistogram.length;t++) {
            mutationAgeHistogram[t]=0;
        }               
        
        for (Organism o : getEchosystem().getOrganisms()) {

            int toTime = getEchosystem().getTime();

            Collection<Mutation> mutations = MutationUtils.getRecentMutations(o, 0, toTime, 100);
            mutationSum +=mutations.size();
            for (Mutation m : mutations) { 
                Integer freq = mutationFreq.get(m);
                if (freq==null) {
                    freq = 0;
                }
                mutationFreq.put(m, freq+1);

                int mx = Math.min(13,Math.max(0, m.getLocation().x+10));
                int my = Math.min(20,Math.max(0,m.getLocation().y+10));
                mutationGridtXY[mx][my]+=1.0/population;

                mutationAgeSum+=m.getOrganismAge();
                if (m.getOrganismAge()<mutationAgeHistogram.length) {
                    mutationAgeHistogram[m.getOrganismAge()]+=1.0/population;
                }
                mutationSumX+=m.getLocation().x;
                mutationSumY+=m.getLocation().y;
                if (m.getOrganismAge()>25) {
                    mutationSumLateX+=m.getLocation().x;
                    mutationSumLateY+=m.getLocation().y;
                    mutationSumLate++;
                }
            }
        }
        for (int i=0;i<20;i++) {
            for (int j=0;j<20;j++) {                
                mutationGridtXY[i][j]/=(0.1);//*(double) getEchosystem().getOrganisms().size());
                if(getEchosystem().getTime()%100==0) {
                    mutationDistSum+=(mutationGridtXY[i][j]-mutationGridtXYold[12][i][j])*(mutationGridtXY[i][j]-mutationGridtXYold[12][i][j]);                  
                     
                    for (int t=18;t>=0;t--) {
                        mutationGridtXYold[t+1][i][j] = (mutationGridtXYold[t+1][i][j] + mutationGridtXYold[t][i][j])/2;                        
                    }
                    mutationGridtXYold[0][i][j] =(mutationGridtXYold[0][i][j]+mutationGridtXY[i][j])/2;
                     
                }
            }
        }       

        avgMutationAge = mutationAgeSum/(double) mutationSum;
        
        avgTotalMutations = mutationSum / (double) getEchosystem().getOrganisms().size();                
        mutationSpeed = mutationDistSum;
        
        for (Mutation m: mutationFreq.keySet()) {
            double divers = 0.5-mutationFreq.get(m)/population;
            if (0.5-Math.abs(divers)<0) {
                System.out.println("Diversity less than 0, mf, caa "+mutationFreq.get(m)+" "+population);
            }

            mutationDiversity+=(0.5-Math.abs(divers));
            mutationDiversityAge+=m.getOrganismAge()*(0.5-Math.abs(divers));
        }
        if( mutationDiversity>0) {
            mutationDiversityAge/= mutationDiversity;
        }
    }

    public String getStatsSummary() {
        String summary = "";
        summary += "Game Time: " + String.format("%-8s", getTime()/100*100);
        summary += " Population: " + String.format("%-5.0f", smoothedPopulation);
        summary += " Avg Comp Score: " + String.format("%-5s", (int) avgCompetitiveScore);
        summary += " Avg Genome Size: " + String.format("%-5.1f", avgTotalMutations);
        return summary;
    }

    public void logStats() {
        if (getEchosystem().getTime() % 2000 == 0) {
            String logStr = "";
            logStr += "Time: " + String.format("%1$8s", getTime());
            logStr += " MSpeed: " + String.format("%1$4s", (int) mutationSpeed);
            logStr += " MDiversity: " + String.format("%1$3s", (int) (mutationDiversity * 10));
            logStr += " OneChildPct: " +  String.format("%4.1f", (childNumberPercent[0]-childNumberPercent[1]));
            logStr += " TwoChildrenPct: " + String.format("%4.1f", (childNumberPercent[1]-childNumberPercent[2]));
            logStr += " ThreeChildrenPct: " + String.format("%4.1f", (childNumberPercent[2]-childNumberPercent[3]+childNumberPercent[4]));
            logStr += " PopDensity: " + String.format("%5.1f", smoothedPopDensity);
            logStr += " AvgLife: " + String.format("%1$3s", (int) avgMaxLifespan);
            logStr += " CompScore: " + String.format("%1$5s", (int) avgCompetitiveScore);
            logStr += " MutationCount: " + String.format("%5.1f", avgTotalMutations);
            logStr += " ChildbirthAgeHg: " + buildParentAgeAtBirthHistogramForLog();
            System.out.println(logStr);
        }
    }

}
