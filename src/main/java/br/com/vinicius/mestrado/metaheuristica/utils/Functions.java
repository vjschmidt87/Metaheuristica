package br.com.vinicius.mestrado.metaheuristica.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Functions {

    public static Map<String, Method> methodMap = new HashMap<>();
    public static String[] varsName;
    private static boolean first = true;
    
    public static void functionsStarter() {
        try {
            methodMap.put("rosenbrock", Functions.class.getMethod("rosenbrock", new Class[]{double[].class}));
            methodMap.put("rastrigin", Functions.class.getMethod("rastrigin", new Class[]{double[].class}));
            methodMap.put("easom2D", Functions.class.getMethod("easom2D", new Class[]{double[].class}));
            methodMap.put("easom", Functions.class.getMethod("easom", new Class[]{double[].class}));
            methodMap.put("soundWave", Functions.class.getMethod("soundWave", new Class[]{double[].class}));
            methodMap.put("potentialLJ", Functions.class.getMethod("potentialLJ", new Class[]{double[].class}));
            methodMap.put("sphere", Functions.class.getMethod("sphere", new Class[]{double[].class}));
            methodMap.put("spherePI", Functions.class.getMethod("spherePi", new Class[]{double[].class}));
            methodMap.put("elliptic", Functions.class.getMethod("elliptic", new Class[]{double[].class}));
        } catch (NoSuchMethodException | SecurityException e) {
            System.err.println("Erro de mapeamento " + e.getMessage());
        }
    }
    
    public static double invoker (String key, double[] coords) {
        try {
            switch (key.toLowerCase()) {
                case "rosenbrock":
                    return (double) methodMap.get("rosenbrock").invoke(null, coords);
                case "rastrigin":
                    return (double) methodMap.get("rastrigin").invoke(null, coords);
                case "easom2d":
                    return (double) methodMap.get("easom2D").invoke(null, coords);
                case "easom":
                    return (double) methodMap.get("easom").invoke(null, coords);
                case "soundwave":
                    return (double) methodMap.get("soundWave").invoke(null, coords);
                case "potentiallj":
                    return (double) methodMap.get("potentialLJ").invoke(null, coords);
                case "sphere":
                    return (double) methodMap.get("sphere").invoke(null, coords);
                case "spherepi":
                    return (double) methodMap.get("spherePI").invoke(null, coords);
                case "elliptic":
                    return (double) methodMap.get("elliptic").invoke(null, coords);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            System.err.println("Erro ao chamar a função " + key + ": " + ex.getMessage());
            System.exit(1);
        }
        return Double.MAX_VALUE;
    }
    
    public static double rosenbrock(double[] coords) {
        double value = 0;
        if (first) {
            varsName = new String[coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        for (int i = 0; i < coords.length - 1; i++) {
            value += Math.pow(1 - coords[i], 2) + 100 * Math.pow(coords[i + 1] - Math.pow(coords[i], 2), 2);
        }
        return value;
    }
    
    public static double rastrigin(double[] coords) {
        double value = 0;
        if (first) {
            varsName = new String[coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        for (int i = 0; i < coords.length; i++) {
            value += Math.pow(coords[i], 2) - 10 * Math.cos(2 * Math.PI * coords[i]);
        }
        return 10 * coords.length + value;
    }
    
    public static double easom2D(double[] coords) {
        if (first) {
            varsName = new String[coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        return -Math.cos(coords[0]) * Math.cos(coords[1]) * Math.exp(-(Math.pow(coords[0] - Math.PI, 2) + Math.pow(coords[1] - Math.PI, 2)));
    }
    
    public static double easom(double[] coords) {
        if (first) {
            varsName = new String[coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        double product = 1;
        double sum = 0;
        for (int i = 0; i < coords.length; i++) {
            product *= Math.cos(coords[i]);
        }
        for (int i = 0; i < coords.length; i++) {
            sum += Math.pow(coords[i] - Math.PI, 2);
        }
        return -Math.pow(-1, coords.length) * product * Math.exp(-sum);
    }
    
    /**
     * novas funções estão na classe main para serem testadas, 
     * converter pra essa estrutura as outras além da ackley
    **/
    public static double ackley(double[] coords) {
        double sum1 = 0;
        double sum2 = 0;
        if (first) {
            varsName = new String[coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        for (int i = 0; i < coords.length; i++) {
            sum1 += Math.pow(coords[i], 2);
        }
        for (int i = 0; i < coords.length; i++) {
            sum2 += Math.cos(2 * Math.PI * coords[i]);
        }
        //arredondamento do java nunca permite alcançar zero absoluto
        return -20 * Math.exp(-0.2 * Math.sqrt((1/coords.length) * sum1)) 
                -Math.exp((1/(2 * Math.PI)) * sum2) + 20 + Math.exp(1);
    }
    
    public static double soundWave(double[] coords) {
        if (first) {
            String[] varsNamed = { "a1", "w1", "a2", "w2", "a3", "w3" };
            varsName = varsNamed;
            first = false;
        }
        final double theta = 2 * Math.PI/100;
        double value = 0;
        double y;
        double y0;
        for (int t = 0; t <= 100; t++) {
            y = coords[0] * Math.sin(coords[1] * t * theta + coords[2] 
                    * Math.sin(coords[3] * t * theta + coords[4] 
                            * Math.sin(coords[5] * t * theta)));
            y0 = 1 * Math.sin(5 * t * theta -1.5 
                    * Math.sin(4.8 * t * theta + 2 * Math.sin(4.9 * t * theta)));
            value += Math.pow(y - y0, 2);
        }
        return value;
    }
    
    public static double potentialLJ (double[] coords) {
        int totalAtoms = 0;
        if (first) {
            totalAtoms = Math.round((coords.length) / 3) + 2;    
            varsName = new String[coords.length];
            for (int i = 0; i < coords.length; i++) {
                varsName[i] = "x" + (i + 1);
            }
            first = false;
        }
        int indexCoord = 0;
        double rij;
        double sum = 0;
        double atoms[][] = new double[totalAtoms][3];
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
            }
        }
        for (int i = 0; i < atoms.length; i++) {
            for (int j = 0; j < i; j++) {
                rij = 0;
                for (int k = 0; k < 3; k++) {
                    rij += Math.pow(atoms[j][k] - atoms[i][k], 2);
                }
                sum += (1/Math.pow(rij, 6)) - (1/Math.pow(rij, 3));
            }
        }
        return 4 * 1 * sum;
    }
    
    public static double sphere(double[] coords) {
        double sum = 0;
        if (first) {
            varsName = new String[coords.length];
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
            varsName = new String[coords.length];
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
            varsName = new String[coords.length];
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