package br.com.vinicius.mestrado.metaheuristica.world;

import br.com.vinicius.mestrado.metaheuristica.report.ReportMaker;
import br.com.vinicius.mestrado.metaheuristica.utils.FileReader;
import br.com.vinicius.mestrado.metaheuristica.utils.Functions;
import br.com.vinicius.mestrado.metaheuristica.utils.Utils;

public class Main {
    public static void main(String[] args) {
        Functions.functionsStarter();
        ReportMaker report = new ReportMaker();
        int totalTests = 10;
        boolean debugMode = false;
        int[] nas = { 5, 10, 20, 50 }; 
        float[] atts = { 1E-2f, 1E-1f, 2.5E-1f, 5E-1f, 1f }; 
        float[] stDevs = { 1E-2f, 5E-2f, 1E-1f, 5E-1f, 1f }; 
        FileReader fr = new FileReader(args[0]);
        System.out.println("FO: " + fr.getFO().getFoKey());
        System.out.println("D : " + fr.getFO().getBoundaries().length);
        System.out.println("Boundaries:");
        for (double[] boundary : fr.getFO().getBoundaries()) {
            System.out.println("|" + boundary[1] + "|" + boundary[0] + "|");
        }
        System.out.println("TOTAL EVALUATIONS: " + Utils.STOP_CRITERIA);
        for (int na : nas) {
            for (float att : atts) {
                for (float stDev : stDevs) {
                    String dir = na + " x d" + System.getProperty("file.separator") + "att " + att + System.getProperty("file.separator") + "stDev " + stDev;
                    for (int i = 1; i <= totalTests; i++) {
                        System.out.println(Utils.getDate());
                        StringBuilder dataChart = new StringBuilder();
                        Society society = new Society(fr.getFO().getBoundaries().length * na, fr.getFO(), debugMode, att, stDev, dir, fr.isUserMode());
                        society.createAgents();
                        society.simulate();
                        for (double[] reportDataChart : Utils.reportData) {
                            dataChart.append(reportDataChart[0]).append(";").append(reportDataChart[1]).append("\n");
                        }
                        report.createReport(dir, "dataChart (" + Utils.getDate() + ").csv", dataChart);
                        Utils.resetStaticData();
                        System.out.println("DONE: " + i + "/" + totalTests);
                        System.out.println(Utils.getDate());
                    }
                }
            }
        }
    }
}
