package br.com.vinicius.mestrado.metaheuristica.utils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private static final DecimalFormat DFB = new DecimalFormat("000.000");
    private static final DecimalFormat DFS = new DecimalFormat("0.000");
    public static final double MIN_DOUBLE = 1E-16;
    public static final double MIN_SPACE_SIZE = 1E-16;
    public static final double OPINION_MAX = 1;
    public static final double OPINION_MIN = 0;
    public static final double[] EPSILONS = { 1E-1, 1E-2, 1E-4, 1E-8, 1E-16 };
    
    public static final double GOAL_VALUE = Double.NEGATIVE_INFINITY;
    
    public static boolean cec05;
    public static boolean cec15;
    public static int functionNumber;
    public static double checkpoint = 0;
    public static double bestFO = Double.POSITIVE_INFINITY;
    public static final double CHECKPOINT_INCREASER = 1E3;

    //pra 2d do CEC
//    public static final double STOP_CRITERIA = 9E4;
    //pra 30d do CEC
//    public static final double STOP_CRITERIA = 9E5;
    //pra benchmark
//    public static final double STOP_CRITERIA = 5E6;
    //outros
//    public static final double STOP_CRITERIA = 5E5;
    public static final double STOP_CRITERIA = 2E5;
//    public static final double STOP_CRITERIA = 1E9;

    public static final int TOTAL_CHECKPOINTS = (int) (Math.round(STOP_CRITERIA/CHECKPOINT_INCREASER) + 1);
    public static double[][] reportData = new double [TOTAL_CHECKPOINTS][2];
    public static int reportDataIndex = 0;
    public static boolean stopCriteriaReached;
    
    public static String formatBig (double value) {
        return DFB.format(value);
    }
    
    public static String formatSmall (double value) {
        String aux = "";
        if (value > 0) aux = " ";
        return aux + DFS.format(value);
    }
    
    public static String getDate () {
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());
        return new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSSSSS").format(date);
    }
    
    public static double[][] cloneBoundaries (double[][] boundaries) {
        double[][] boundariesClone = new double[boundaries.length][2];
        for (int k = 0; k < boundaries.length; k++) {
            boundariesClone[k][0] = boundaries[k][0];
            boundariesClone[k][1] = boundaries[k][1];
        }
        return boundariesClone;
    }
    
    public static void resetStaticData() {
//        Agent.testCounter = 0;
//        Agent.totalNotCalculated = 0;
//        Agent.zeroCounter = 0;
//        Agent.foInteractionCounter = 0;
//        Agent.foExplorationCounter = 0;
//        Agent.foGotBetterExploring = 0;
//        Agent.foGotBetterInteracting = 0;
//        Agent.stagnation = false;
        checkpoint = 0;
        bestFO = Double.POSITIVE_INFINITY;
        reportData = new double [TOTAL_CHECKPOINTS][2];
        reportDataIndex = 0;
        stopCriteriaReached = false;
    }
}
