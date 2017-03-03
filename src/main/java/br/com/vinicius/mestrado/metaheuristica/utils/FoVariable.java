/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.vinicius.mestrado.metaheuristica.utils;

public class FoVariable implements Cloneable {
    private String name;
    private double value;
    
    public FoVariable() {
    }
    
    public FoVariable(String name, double value) { 
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    @Override
    public FoVariable clone() {
        return new FoVariable (name, value);
    }
}

