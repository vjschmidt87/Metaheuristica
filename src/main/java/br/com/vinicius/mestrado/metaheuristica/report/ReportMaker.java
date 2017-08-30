package br.com.vinicius.mestrado.metaheuristica.report;

import br.com.vinicius.mestrado.metaheuristica.agents.Agent;
import br.com.vinicius.mestrado.metaheuristica.utils.FoVariable;
import br.com.vinicius.mestrado.metaheuristica.utils.Functions;
import br.com.vinicius.mestrado.metaheuristica.utils.Utils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ReportMaker {
    private final StringBuilder data = new StringBuilder();
    public void generateHeader(String text) {
        data.append(text).append(";\n");
    }
    
    public void insertInitialParameters(String text) {
        data.append(text).append(";\n");
    }
    
    public void generateResultsCSV(List<Agent> agents, List<FoVariable> coords, boolean userMode) {
        Collections.sort(agents);
        data.append("Agent #").append(";");
        if (userMode) {
            for (FoVariable coord : coords) {
                data.append("Best ").append(coord.getName()).append(";");
            }
        } else if (Utils.cec05 || Utils.cec15) {
            for (int i = 1; i <= coords.size(); i++) {
                data.append("Best x").append(i).append(";");
            }
        } else {
            for (String var : Functions.varsName) {
                data.append("Best ").append(var).append(";");
            }
        }
        data.append("Best Fo").append(";\n");
        for (Agent agent : agents) {
            data.append(agent.getId()).append(";");
            for (double coord : agent.getBestCoords()) {
                    data.append(coord).append(";");
            }
            data.append(agent.getBestFO()).append(";\n");
        }
    }
    
    public void createReport(String dir) {
        createReport(dir, "report (" + Utils.getDate() + ").csv", data);
    }
    
    public void createReportData(String dir, StringBuilder data, int reportNum, int dimensionSize) {
        createReport(dir, dimensionSize + "D - #" + reportNum + ".csv", data);
    }
    
    public void createReport(String dir, String name, StringBuilder data) {
        BufferedWriter writer = null;
        try {
            File directory = new File("results" + System.getProperty("file.separator") + dir);
            directory.mkdirs();
            writer = new BufferedWriter( new FileWriter(new File(directory, name)));
            writer.write(data.toString());

        }
        catch ( IOException e) {
            System.err.println(e.getMessage());
        }
        finally {
            try {
                if ( writer != null) writer.close();
            }
            catch ( IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
