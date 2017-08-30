package br.com.vinicius.mestrado.metaheuristica.agents;

import br.com.vinicius.mestrado.metaheuristica.utils.FoVariable;
import br.com.vinicius.mestrado.metaheuristica.utils.Utils;
import br.com.vinicius.mestrado.metaheuristica.utils.Functions;
import br.com.vinicius.mestrado.metaheuristica.utils.cec05.test_func;
import br.com.vinicius.mestrado.metaheuristica.utils.cec15.testfunc;
import java.util.ArrayList;
import java.util.List;

public class Agent implements Comparable<Agent> {

    public static double testCounter = 0;
    public static double totalNotCalculated = 0;
    public static double zeroCounter = 0;

    public static double foInteractionCounter = 0;
    public static double foExplorationCounter = 0;
    public static double foGotBetterExploring = 0;
    public static double foGotBetterInteracting = 0;

    private int id;
    private int interactStartPosition;

    private double[] coords;
    private double[] bestCoords;
    private double[] bestCoordsCloned;
    private double[] coordsTest;
    private double fo;
    private double foLast = Double.POSITIVE_INFINITY;
    private double bestFO = Double.POSITIVE_INFINITY;
    private double bestFOCloned;
    private double[] angle;
    private double[] distances;
    private double bestOpinion = Double.NEGATIVE_INFINITY;
    private double statusRange;
    //influenciado por esses agentes
    private ArrayList<Agent> agentsJ = new ArrayList<>();
    private String foKey;
    private Object function;

    private double angleAbs;
    private double size = 0;
    private int totalDim;

    private int influencer = 0;
    private int lastUpdatedPosition;

    private final int shift = 1;
    private final int rotate = 0;

    public Agent(int id, int interactStartPosition, double[][] boundaries, int totalDim, String foKey, Object function, double size, List<FoVariable> constants) {
        this.id = id;
        this.interactStartPosition = interactStartPosition;
        double pos;
        double dimSize;
        this.function = function;
        this.foKey = foKey;
        this.totalDim = totalDim;

        this.size = size;
        this.distances = new double[totalDim];
        this.coords = new double[totalDim];
        this.bestCoords = new double[totalDim];
        this.bestCoordsCloned = new double[totalDim];
        this.coordsTest = new double[totalDim];
        this.angle = new double[totalDim];
        for (int k = 0; k < totalDim; k++) {
            dimSize = Math.abs(boundaries[k][0] - boundaries[k][1]);
            pos = boundaries[k][1] + (Math.random() * dimSize);
            this.coords[k] = pos;
            this.bestCoords[k] = pos;
            this.bestCoordsCloned[k] = pos;
        }
        calculateDistances(boundaries);
    }

    public int getId() {
        return id;
    }

    public double[] getCoords() {
        return coords;
    }

    public double[] getBestCoords() {
        return bestCoords;
    }

    public double[] getBestCoordsCloned() {
        return bestCoordsCloned;
    }

    public double getBestFO() {
        return bestFO;
    }

    public double getBestFOCloned() {
        return bestFOCloned;
    }

    public double getBestOpinion() {
        return bestOpinion;
    }

    public double getStatusRange() {
        return statusRange;
    }

    public void addAgentJ(Agent agentJ) {
        this.agentsJ.add(agentJ);
    }

    public void incrementInfluencer() {
        this.influencer++;
    }

    public void cloneAgent() {
        bestFOCloned = bestFO;
        for (int i = 0; i < totalDim; i++) {
            bestCoordsCloned[i] = bestCoords[i];
        }
    }

    private void calculateDistances(double[][] boundaries) {
        double randomStep = Math.random();
        for (int k = 0; k < coords.length; k++) {
            distances[k] = Utils.MIN_DOUBLE + (randomStep
                    * (Math.abs(boundaries[k][0] - boundaries[k][1])));
        }
    }

    public void calculateLocalFO() {
        fo = calculateFO(coords);
        if (fo < bestFO) {
            for (int x = 0; x < totalDim; x++) {
                bestCoords[x] = coords[x];
            }
            bestFO = fo;
            foGotBetterExploring++;
        }
    }

    public double calculateFO(double[] coords) {
        double evaluation;
        if (function != null && Utils.cec05) {
            evaluation = ((test_func) function).f(coords);
        } else if (function != null && Utils.cec15) {
            evaluation = ((testfunc) function).callFunction(coords, totalDim, Utils.functionNumber, shift, rotate);
        } else {
            evaluation = Functions.invoker(foKey, coords);
        }
        testCounter++;
        if (evaluation < Utils.bestFO) {
            Utils.bestFO = evaluation;
        }

        if (testCounter >= Utils.STOP_CRITERIA) {
            Utils.stopCriteriaReached = true;
        }

        reportDataUpdate();
        return evaluation;
    }

    public double updateBestOpinion(double opinionPOne, double opinionPTwo, double m, double M) {
        bestOpinion = (opinionPOne * bestFO) + opinionPTwo;
        if (bestOpinion > Utils.OPINION_MAX || bestOpinion == m) {
            bestOpinion = Utils.OPINION_MAX;
        } else if (bestOpinion < Utils.OPINION_MIN || bestOpinion == M) {
            bestOpinion = Utils.OPINION_MIN;
        }
        return bestOpinion;
    }

    public void updateRange(double attenuator, double stdDev) {
        double exp = -Math.pow(bestOpinion - Utils.OPINION_MAX, 2) / (2 * Math.pow(stdDev, 2));
        statusRange = attenuator * size * Math.pow(totalDim, 0.5) * Math.exp(exp);
    }

    public void checkInfluenceSphere(Agent agentJ) {
        double dist = 0;
        if (agentJ.getBestOpinion() > bestOpinion
                || (agentJ.getBestOpinion() == bestOpinion && agentJ.getBestFO() < bestFO)) {
            for (int k = 0; k < totalDim; k++) {
                dist += Math.pow(coords[k] - agentJ.getCoords()[k], 2);
            }
            if (dist <= Math.pow(agentJ.getStatusRange(), 2)) {
                addAgentJ(agentJ);
                agentJ.incrementInfluencer();
            }
        }
    }

    public void interact(double[][] boundaries) {
        double ownOpinionValue = 0;
        double dislocationAux = 0;
        double normCoords = 0;
        boolean warning = false;
        if (agentsJ.size() > 0) {
            if (bestOpinion - Utils.OPINION_MAX == 0) {
                warning = true;
                dislocationAux = Utils.EPSILON;
            } else {
                ownOpinionValue = bestOpinion - Utils.OPINION_MAX;
            }
            for (Agent agentJ : agentsJ) {
                if (bestFO <= agentJ.getBestFOCloned()) {
                    break;
                }
                lastUpdatedPosition = -1;
                for (int i = 0; i < totalDim; i++) {
                    normCoords += Math.pow(agentJ.getBestCoordsCloned()[i] - bestCoordsCloned[i], 2);
                    coordsTest[i] = bestCoordsCloned[i];
                }

                normCoords = Math.sqrt(normCoords);
                if (normCoords == 0) {
                    normCoords = Utils.MIN_DOUBLE;
                }
                if (!warning) {
                    dislocationAux = ((agentJ.getBestOpinion() - Utils.OPINION_MAX) / ownOpinionValue) + Utils.EPSILON;
                }
                interactFormula(agentJ.getBestCoordsCloned(), boundaries, dislocationAux, normCoords, interactStartPosition, coords.length);
                if (Utils.stopCriteriaReached) {
                    break;
                }
                if (interactStartPosition > 0) {
                    interactFormula(agentJ.getBestCoordsCloned(), boundaries, dislocationAux, normCoords, 0, interactStartPosition);

                    if (Utils.stopCriteriaReached) {
                        break;
                    }
                }
            }
        }
    }

    private void interactFormula(double[] bestCoordsJ, double[][] boundaries, double dislocationAux, double normCoords, int begin, int end) {
        double foTest;
        double I;
        double dislocation;
        for (int k = begin; k < end; k++) {
            I = distances[k];

            dislocation = dislocationAux
                    * ((bestCoordsJ[k] - bestCoordsCloned[k]) / normCoords)
                    * I;

            coordsTest[k] = fixPosition(bestCoordsJ[k] + dislocation, boundaries[k]);

            if (coordsTest[k] != bestCoordsCloned[k]) {
                foInteractionCounter++;
                foTest = calculateFO(coordsTest);
                if (foTest < bestFO) {
                    copyCoords(end, k);
                    bestFO = foTest;
                    foGotBetterInteracting++;
                    lastUpdatedPosition = k;
                }

                if (Utils.stopCriteriaReached) {
                    break;
                }
            } else {
                totalNotCalculated++;
            }
        }
    }

    private void copyCoords(int end, int current) {
        if (lastUpdatedPosition == -1) {
            if (interactStartPosition == 0 || (interactStartPosition > 0 && end == interactStartPosition)) {
                if (interactStartPosition > 0 && end == interactStartPosition) {
                    for (int k = interactStartPosition; k < totalDim; k++) {
                        bestCoords[k] = coordsTest[k];
                    }
                }
                for (int k = 0; k <= current; k++) {
                    bestCoords[k] = coordsTest[k];
                }
            } else {
                for (int k = interactStartPosition; k <= current; k++) {
                    bestCoords[k] = coordsTest[k];
                }
            }
        } else {
            if ((lastUpdatedPosition + 1) > current) {
                for (int k = (lastUpdatedPosition + 1); k < totalDim; k++) {
                    bestCoords[k] = coordsTest[k];
                }
                lastUpdatedPosition = -1;
            }
            for (int k = (lastUpdatedPosition + 1); k <= current; k++) {
                bestCoords[k] = coordsTest[k];
            }
        }
        lastUpdatedPosition = current;
    }

    public void move(double[][] boundaries) {
        agentsJ = new ArrayList<>();
        influencer = 0;
        calculateDistances(boundaries);
        if (fo > foLast || foLast == Double.POSITIVE_INFINITY) {
            angleAbs = 0;
            for (int x = 0; x < totalDim; x++) {
                angle[x] = -1 + (Math.random() * 2);
                angleAbs += Math.pow(angle[x], 2);
            }
            angleAbs = Math.sqrt(angleAbs);
            if (angleAbs == 0) {
                angleAbs = Utils.MIN_DOUBLE;
            }
        }
        foLast = fo;
        for (int k = 0; k < totalDim; k++) {
            coords[k] = fixPosition(coords[k] + ((distances[k]) * (angle[k] / angleAbs)), boundaries[k]);
        }
    }

    private double fixPosition(double coord, double[] boundary) {
        double dimSize = Math.abs(boundary[0] - boundary[1]);
        if (boundary[0] - boundary[1] < Utils.MIN_DOUBLE) {
            coord = boundary[0];
        } else if (coord > boundary[0]) {
            coord = coord - (Math.ceil(Math.abs((coord - boundary[0]) / dimSize)) * dimSize);
        } else if (coord < boundary[1]) {
            coord = coord + (Math.ceil(Math.abs((coord - boundary[1]) / dimSize)) * dimSize);
        }
        if (coord > boundary[0] || coord < boundary[1]) {
            System.err.println("Still out of bounds");
            System.exit(1);
        }
        return coord;
    }

    private void reportDataUpdate() {
        if (Utils.reportDataIndex == 0) {
            Utils.reportData[Utils.reportDataIndex][0] = 1;
            Utils.reportData[Utils.reportDataIndex][1] = Utils.bestFO;
        }
        if (Utils.reportDataIndex > 0 && testCounter == Utils.checkpoint) {
            Utils.reportData[Utils.reportDataIndex][0] = Utils.checkpoint;
            Utils.reportData[Utils.reportDataIndex][1] = Utils.bestFO;
        }
        if (testCounter == Utils.checkpoint || Utils.reportDataIndex == 0) {
            Utils.checkpoint += Utils.CHECKPOINT_INCREASER;
            Utils.reportDataIndex++;
        }
    }

    @Override
    public int compareTo(Agent agent) {
        return Double.compare(bestFO, agent.getBestFO());
    }
}
