package br.com.vinicius.mestrado.metaheuristica.utils;

import java.io.BufferedReader;
import java.io.IOException;

public class FileReader {
    private BufferedReader br = null;
    private final FoData fo = new FoData();
    private int totalDimensions;
    double[][] boundaries;
    private boolean userMode;
    public FileReader() {
    }
    
    public FileReader(String name) {
        try {
            String sCurrentLine;
            br = new BufferedReader(new java.io.FileReader(name));

            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.startsWith("dimensions")) {
                    totalDimensions = Integer.parseInt(sCurrentLine.substring(sCurrentLine.indexOf("=") + 1));
                    boundaries = new double[totalDimensions][2];
                } else if (sCurrentLine.startsWith("variable")) {
                    FoVariable v = new FoVariable(sCurrentLine.substring(sCurrentLine.indexOf("=") + 1), 0);
                    fo.addCoords(v);
                } else if (sCurrentLine.startsWith("boundaries")) {
                    double bottom;
                    double top;
                    top = boundaryCheck(sCurrentLine.substring(sCurrentLine.indexOf("=") + 1, sCurrentLine.indexOf(",")));
                    bottom = boundaryCheck(sCurrentLine.substring(sCurrentLine.indexOf(",") + 1));
                    for (int i = 0; i < totalDimensions; i++) {
                        boundaries[i][0] = top;
                        boundaries[i][1] = bottom;
                    }
                    fo.setBoundaries(boundaries);
                } else if (sCurrentLine.startsWith("boundary")) {
                    int i = Integer.parseInt(sCurrentLine.substring(sCurrentLine.indexOf("[") + 1, sCurrentLine.indexOf("]"))) - 1;
                    boundaries[i][0] = boundaryCheck(sCurrentLine.substring(sCurrentLine.indexOf("=") + 1, sCurrentLine.indexOf(",")));
                    boundaries[i][1] = boundaryCheck(sCurrentLine.substring(sCurrentLine.indexOf(",") + 1));
                } else if (sCurrentLine.startsWith("readyFormula")) {
                    userMode = false;
                    try {
                        if ("cec05".equals(sCurrentLine.substring(sCurrentLine.indexOf("=") + 1, sCurrentLine.indexOf(",")).toLowerCase())) {
                            Utils.cec05 = true;
                            Utils.functionNumber = Integer.parseInt(sCurrentLine.substring(sCurrentLine.indexOf(",") + 1));
                        } else if ("cec15".equals(sCurrentLine.substring(sCurrentLine.indexOf("=") + 1, sCurrentLine.indexOf(",")).toLowerCase())) {
                            Utils.cec15 = true;
                            Utils.functionNumber = Integer.parseInt(sCurrentLine.substring(sCurrentLine.indexOf(",") + 1));
                        }
                    } catch (Exception e) {}
                    fo.setFoKey(sCurrentLine.substring(sCurrentLine.indexOf("=")+1));
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    private double boundaryCheck(String val) throws NumberFormatException {
        double top;
        if("pi".equals(val)) {
            top = Math.PI;
        } else {
            top = Double.parseDouble(val);
        }
        return top;
    }

    public FoData getFO() {
        return fo;
    }

    public boolean isUserMode() {
        return userMode;
    }

}

