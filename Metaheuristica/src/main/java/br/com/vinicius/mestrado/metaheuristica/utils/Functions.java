package br.com.vinicius.mestrado.metaheuristica.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Functions {

    public static Map<String, Method> methodMap = new HashMap<>();
    public static String[] varsName;
    public static List<Double> foConstants;
    private static boolean first = true;
    
    public static void functionsStarter() {
        try {
            methodMap.put("rosenbrock", Functions.class.getMethod("rosenbrock", new Class[]{List.class, double[].class}));
            methodMap.put("rastrigin", Functions.class.getMethod("rastrigin", new Class[]{List.class, double[].class}));
            methodMap.put("easom2D", Functions.class.getMethod("easom2D", new Class[]{double[].class}));
            methodMap.put("easom", Functions.class.getMethod("easom", new Class[]{double[].class}));
            methodMap.put("soundWave", Functions.class.getMethod("soundWave", new Class[]{double[].class}));
            methodMap.put("potentialLJ", Functions.class.getMethod("potentialLJ", new Class[]{List.class, double[].class}));
            methodMap.put("sphere", Functions.class.getMethod("sphere", new Class[]{double[].class}));
            methodMap.put("spherePI", Functions.class.getMethod("spherePi", new Class[]{double[].class}));
            methodMap.put("elliptic", Functions.class.getMethod("elliptic", new Class[]{double[].class}));
        } catch (NoSuchMethodException | SecurityException e) {
            System.err.println("Erro de mapeamento " + e.getMessage());
        }
    }
    
    public static double invoker (String key, List<FoVariable> constants, double[] coords) {
        double value = 0;
        try {
            switch (key.toLowerCase()) {
                case "rosenbrock":
                    value = (double) methodMap.get("rosenbrock").invoke(null, constants, coords);
                    break;
                case "rastrigin":
                    value = (double) methodMap.get("rastrigin").invoke(null, constants, coords);
                    break;
                case "easom2d":
                    value = (double) methodMap.get("easom2D").invoke(null, coords);
                    break;
                case "easom":
                    value = (double) methodMap.get("easom").invoke(null, coords);
                    break;
                case "soundwave":
                    value = (double) methodMap.get("soundWave").invoke(null, coords);
                    break;
                case "potentiallj":
                    value = (double) methodMap.get("potentialLJ").invoke(null, constants, coords);
                    break;
                case "sphere":
                    value = (double) methodMap.get("sphere").invoke(null, coords);
                    break;
                case "spherepi":
                    value = (double) methodMap.get("spherePI").invoke(null, coords);
                    break;
                case "elliptic":
                    value = (double) methodMap.get("elliptic").invoke(null, coords);
                    break;
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            System.err.println("Erro ao chamar a função " + key + ": " + ex.getMessage());
            System.exit(1);
        }
        return value;
    }
    
    public static double rosenbrock(List<FoVariable> constants, double[] coords) {
        if (first) {
            foConstants = new ArrayList<>();
            Double a = null;
            Double b = null;
            for (FoVariable constant : constants) {
                switch (constant.getName().toLowerCase()) {
                    case "a":
                        a = constant.getValue();
                        break;
                    case "b":
                        b = constant.getValue();
                        break;
                }
            }
            if (a == null) {
                System.err.println("Constant 'a' not set. Automatic value of 1 set");
                a = Double.valueOf(1);
            } 
            if (b == null) {
                System.err.println("Constant 'b' not set. Automatic value of 100 set");
                b = Double.valueOf(100);
            }
            foConstants.add(a);
            foConstants.add(b);
            varsName = new String [coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        double value = 0;
        for (int i = 0; i < coords.length - 1; i++) {
            value += Math.pow(foConstants.get(0) - coords[i], 2) + foConstants.get(1) * Math.pow(coords[i + 1] - Math.pow(coords[i], 2), 2);
        }
        return value;
    }
    
    public static double rastrigin(List<FoVariable> constants, double[] coords) {
        if (first) {
            foConstants = new ArrayList<>();
            Double a = null;
            for (FoVariable constant : constants) {
                if ("a".equals(constant.getName().toLowerCase())) {
                    a = constant.getValue();
                }
            }
            if (a == null) {
                System.err.println("Constant 'a' not set. Automatic value of 10 set");
                a = Double.valueOf(10);
            }
            foConstants.add(a);
            varsName = new String [coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        double value = 0;
        for (int i = 0; i < coords.length; i++) {
            value += Math.pow(coords[i], 2) - foConstants.get(0) * Math.cos(2 * Math.PI * coords[i]);
        }
        return foConstants.get(0) * coords.length + value;
    }
    
    public static double easom2D(double[] coords) {
        if (first) {
            foConstants = new ArrayList<>();
            varsName = new String [coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        return -Math.cos(coords[0]) * Math.cos(coords[1]) * Math.exp(-(Math.pow(coords[0] - Math.PI, 2) + Math.pow(coords[1] - Math.PI, 2)));
    }
    
    public static double easom(double[] coords) {
        if (first) {
            foConstants = new ArrayList<>();
            varsName = new String [coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        double product = 1;
        double sum = 0;
        for (int i = 0; i < coords.length; i++) {
//            product *= Math.pow(Math.cos(x[i]), 2);
            product *= Math.cos(coords[i]);
        }
        for (int i = 0; i < coords.length; i++) {
            sum += Math.pow(coords[i] - Math.PI, 2);
        }
        return -Math.pow(-1, coords.length) * product * Math.exp(-sum);
    }
    
    /*novas funções estão na classe main para serem testadas, converter pra essa estrutura as outras além da ackley*/
    public static double ackley(List<FoVariable> constants, double[] coords) {
        if (first) {
            foConstants = new ArrayList<>();
            Double a = null;
            Double b = null;
            Double c = null;
            double d = coords.length;
            for (FoVariable constant : constants) {
                switch (constant.getName().toLowerCase()) {
                    case "a":
                        a = constant.getValue();
                        break;
                    case "b":
                        b = constant.getValue();
                        break;
                    case "c":
                        c = constant.getValue();
                        break;
                }
            }
            if (a == null) {
                System.err.println("Constant 'a' not set. Automatic value of 20 set");
                a = Double.valueOf(20);
            }
            if (b == null) {
                System.err.println("Constant 'b' not set. Automatic value of 0.2 set");
                b = Double.valueOf(0.2);
            }
            if (c == null) {
                System.err.println("Constant 'c' not set. Automatic value of 2pi set");
                c = Double.valueOf(2 * Math.PI);
            }
            foConstants.add(a);
            foConstants.add(b);
            foConstants.add(c);
            foConstants.add(d);
            varsName = new String [coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        double sum1 = 0;
        double sum2 = 0;
        for (int i = 0; i < coords.length; i++) {
            sum1 += Math.pow(coords[i], 2);
        }
        for (int i = 0; i < coords.length; i++) {
            sum2 += Math.cos(foConstants.get(2) * coords[i]);
        }
        //arredondamento do java nunca permite alcançar zero absoluto
        return -foConstants.get(0) * Math.exp(-foConstants.get(1) * Math.sqrt((1/foConstants.get(3)) * sum1)) -Math.exp((1/foConstants.get(3)) * sum2) + foConstants.get(0) + Math.exp(1);
    }
    
    public static double soundWave(double[] coords) {
        if (first) {
            foConstants = new ArrayList<>();
            String[] varsNamed = { "a1", "w1", "a2", "w2", "a3", "w3" };
            varsName = varsNamed;
            first = false;
        }
        final double theta = 2 * Math.PI/100;
        double value = 0;
        double y;
        double y0;
        for (int t = 0; t <= 100; t++) {
            y = coords[0] * Math.sin(coords[1] * t * theta + coords[2] * Math.sin(coords[3] * t * theta + coords[4] * Math.sin(coords[5] * t * theta)));
            y0 = 1 * Math.sin(5 * t * theta -1.5 * Math.sin(4.8 * t * theta + 2 * Math.sin(4.9 * t * theta)));
            value += Math.pow(y - y0, 2);
        }
        return value;
    }
    
    public static double potentialLJ (List<FoVariable> constants, double[] coords) {
        if (first) {
            foConstants = new ArrayList<>();
            Double eps = null;
            Double sigma = null;
            Double totalAtoms = null;
            for (FoVariable constant : constants) {
                switch (constant.getName().toLowerCase()) {
                    case "eps":
                        eps = constant.getValue();
                        break;
                    case "sigma":
                        sigma = constant.getValue();
                        break;
                }
            }
            if (eps == null) {
                System.err.println("Constant 'eps' not set. Automatic value of 1 set");
                eps = Double.valueOf(1);
            }
            if (sigma == null) {
                System.err.println("Constant 'sigma' not set. Automatic value of 1 set");
                sigma = Double.valueOf(1);
            }
            if (coords.length > 1 && coords.length % 3 != 0) {
                System.err.println("Lennard-Jones potential problem works with atoms in a 3d coordinate environment.");
            } else {
                totalAtoms = Double.valueOf(Math.round((coords.length) / 3) + 2);
            }
            foConstants.add(eps);
            foConstants.add(sigma);
            foConstants.add(totalAtoms);
        
            varsName = new String [coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        int indexCoord = 0;
        double rij;
        double sum = 0;
        double atoms[][] = new double[foConstants.get(2).intValue()][3];
        boolean fixed;
        for (int i = 0; i < atoms.length; i++) {
            for (int j = 0; j < 3; j++) {
                switch (i) {
                    case 0:
                        atoms[i][j] = 0;
                        fixed = true;
                        break;
                    case 1:
                        if (j == 0) {
                            atoms[i][j] = coords[indexCoord];
                            fixed = false;
                        } else {
                            atoms[i][j] = 0;
                            fixed = true;
                        }   break;
                    case 2:
                        if (j == 2) {
                            atoms[i][j] = 0;
                            fixed = true;
                        } else {
                            atoms[i][j] = coords[indexCoord];
                            fixed = false;
                        }   break;
                    default:
                        atoms[i][j] = coords[indexCoord];
                        fixed = false;
                        break;
                }
                if (!fixed) indexCoord++;
//                System.out.print(atoms[i][j] +"|");
            }
//            System.out.println("");
        }
        for (int i = 0; i < atoms.length; i++) {
            for (int j = 0; j < i; j++) {
                rij = 0;
                for (int k = 0; k < 3; k++) {
                    rij += Math.pow(atoms[j][k] - atoms[i][k], 2);
                }
                sum += (foConstants.get(1) / Math.pow(rij, 6)) - (foConstants.get(1) / Math.pow(rij, 3));
            }
        }
        //boundaries -10 a 10
        return 4 * foConstants.get(0) * sum;
    }
    
//    public static double potentialLJ (List<FoVariable> constants, double[] coords) {
//        if (first) {
//            foConstants = new ArrayList<>();
//            Double eps = null;
//            Double sigma = null;
//            Double totalAtoms = null;
//            for (FoVariable constant : constants) {
//                switch (constant.getName().toLowerCase()) {
//                    case "eps":
//                        eps = constant.getValue();
//                        break;
//                    case "sigma":
//                        sigma = constant.getValue();
//                        break;
//                }
//            }
//            if (eps == null) {
//                System.err.println("Constant 'eps' not set. Automatic value of 1 set");
//                eps = Double.valueOf(1);
//            }
//            if (sigma == null) {
//                System.err.println("Constant 'sigma' not set. Automatic value of 1 set");
//                sigma = Double.valueOf(1);
//            }
//            if (coords.length > 1 && coords.length % 3 != 0) {
//                System.err.println("Lennard-Jones potential problem works with atoms in a 3d coordinate environment.");
//            } else {
//                totalAtoms = Double.valueOf(Math.round((coords.length) / 3));
//            }
//            foConstants.add(eps);
//            foConstants.add(sigma);
//            foConstants.add(totalAtoms);
//        
//            varsName = new String [coords.length];
//            for (int i = 0; i < coords.length; i++) {
//                varsName[i] = "x" + (i + 1);
//            }
//            first = false;
//        }
//        int indexCoord = 0;
//        double rij;
//        double sum = 0;
//        double atoms[][] = new double[foConstants.get(2).intValue()][3];
//        for (double[] atom : atoms) {
//            for (int j = 0; j < 3; j++) {
//                atom[j] = coords[indexCoord];
//                indexCoord++;
//            }
//        }
//        for (int i = 0; i < atoms.length; i++) {
//            for (int j = 0; j < i; j++) {
//                rij = 0;
//                for (int k = 0; k < 3; k++) {
//                    rij += Math.pow(atoms[j][k] - atoms[i][k], 2);
//                }
//                sum += (foConstants.get(1) / Math.pow(rij, 6)) - (foConstants.get(1) / Math.pow(rij, 3));
//            }
//        }
//        //boundaries -10 a 10
//        return 4 * foConstants.get(0) * sum;
//    }
    
    public static double sphere(double[] coords) {
        double sum = 0;
        if (first) {
            foConstants = new ArrayList<>();
            varsName = new String [coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        for (int i = 0; i < coords.length; i++) {
            sum += Math.pow(coords[i], 2);
        }
        return sum;
    }
    
    public static double spherePi(double[] coords) {
        double sum = 0;
        if (first) {
            foConstants = new ArrayList<>();
            varsName = new String [coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        for (int i = 0; i < coords.length; i++) {
            sum += Math.pow(coords[i] - Math.PI, 2);
        }
        return sum;
    }
    
    public static double elliptic(double[] coords) {
        double sum = 0;
        double p;
        if (first) {
            foConstants = new ArrayList<>();
            varsName = new String [coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        for (int i = 0; i < coords.length; i++) {
                p = (double)(i/(coords.length - 1));
                sum += Math.pow(1000000, p) * Math.pow(coords[i], 2);
        }
        return sum;
    }
    
    //novas funções
}