/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.vinicius.mestrado.metaheuristica.utils;

import java.util.ArrayList;

public class FoData {
    private String fo;
    private String foKey;
    private ArrayList<FoVariable> coords = new ArrayList();
    private ArrayList<FoVariable> constants = new ArrayList();
    private double[][] boundaries;
    
    public FoData() {
    }

    public String getFo() {
        return fo;
    }

    public void setFo(String fo) {
        this.fo = fo;
    }

    public String getFoKey() {
        return foKey;
    }

    public void setFoKey(String foKey) {
        this.foKey = foKey;
    }

    public ArrayList<FoVariable> getCoords() {
        return coords;
    }

    public void setCoord(ArrayList<FoVariable> coords) {
        this.coords = coords;
    }

    public ArrayList<FoVariable> getConstants() {
        return constants;
    }

    public void setConstants(ArrayList<FoVariable> constants) {
        this.constants = constants;
    }
    
    public void addCoords(FoVariable v) {
        this.coords.add(v);
    }
    
    public void updateCoords (int pos, double value) {
        this.coords.get(pos).setValue(value);
    }
    
    public void addConstant(FoVariable v) {
        this.constants.add(v);
    }

    public double[][] getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(double[][] boundaries) {
        this.boundaries = boundaries;
    }
    
}

