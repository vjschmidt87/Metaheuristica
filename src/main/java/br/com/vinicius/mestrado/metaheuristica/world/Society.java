package br.com.vinicius.mestrado.metaheuristica.world;

import br.com.vinicius.mestrado.metaheuristica.agents.Agent;
import br.com.vinicius.mestrado.metaheuristica.report.ReportMaker;
import br.com.vinicius.mestrado.metaheuristica.utils.FoData;
import br.com.vinicius.mestrado.metaheuristica.utils.Utils;
import br.com.vinicius.mestrado.metaheuristica.utils.cec05.benchmark;
import br.com.vinicius.mestrado.metaheuristica.utils.cec15.testfunc;
import java.util.Arrays;

public class Society {

    private final ReportMaker report = new ReportMaker();
    private double[][] boundaries;
    private int totalAgents;
    private int totalDim;
    private double m;
    private double M;
    private double opinionAuxPartOne;
    private double opinionAuxPartTwo;
    private double size = 0;

    private Agent[] agents;
    private FoData fo;

    private boolean userMode;

    private float stdDev;
    private float attenuator;

    private String dir;

    public Society(int totalAgents, FoData fo, boolean debugMode, float attenuator, float stDev, String dir, boolean userMode) {
        this.userMode = userMode;
        this.attenuator = attenuator;
        this.stdDev = stDev;
        this.dir = dir;
        this.boundaries = Utils.cloneBoundaries(fo.getBoundaries());
        this.totalDim = boundaries.length;
        this.totalAgents = totalAgents;
        this.fo = fo;
        for (double[] boundary : boundaries) {
            size += Math.abs(boundary[0] - boundary[1]);
        }

        this.size = size / boundaries.length;
        if (debugMode) {
            System.out.println("Size: " + size);
        }
    }

    public int getTotalAgents() {
        return totalAgents;
    }

    public void setTotalAgents(int totalAgents) {
        this.totalAgents = totalAgents;
    }

    public Agent[] getAgents() {
        return agents;
    }

    public void setAgents(Agent[] agents) {
        this.agents = agents;
    }

    public void addAgent(int x, Agent agent) {
        this.agents[x] = agent;
    }

    public void createAgents() {
        int id;
        int interactStartPosition;

        int epsilonIndex = 0;
        agents = new Agent[totalAgents];
        Object testFunc = null;
        if (Utils.cec05) {
            benchmark theBenchmark = new benchmark();
            testFunc = theBenchmark.testFunctionFactory(Utils.functionNumber, totalDim);
        } else if (Utils.cec15) {
            testFunc = new testfunc();
            try {
                ((testfunc) testFunc).test_func(Utils.functionNumber, totalDim);
            } catch (Exception ex) {
                System.err.println("Problema na função do CEC15");
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }

        for (int i = 0; i < totalAgents; i++) {
            id = i + 1;
            interactStartPosition = id % totalDim;
            Agent agent = new Agent(id, interactStartPosition, boundaries, totalDim, fo.getFoKey(), testFunc, size, fo.getConstants());
            addAgent(i, agent);
            if (interactStartPosition == 0) {
                epsilonIndex++;
                if (epsilonIndex == Utils.EPSILONS.length) {
                    epsilonIndex = 0;
                }
            }
        }
    }

    private void resetFOLimits() {
        m = Double.POSITIVE_INFINITY;
        M = Double.NEGATIVE_INFINITY;
    }

    private void checkFOLimits(double fo) {
        if (fo < m) {
            m = fo;
        }
        if (fo > M) {
            M = fo;
        }
    }

    private void calculateOpinionAux() {
        if (Math.abs(m - M) < Utils.MIN_DOUBLE) {
            m -= Utils.MIN_DOUBLE;
            M += Utils.MIN_DOUBLE;
        }
        opinionAuxPartOne = (Utils.OPINION_MAX - Utils.OPINION_MIN) / (m - M);
        opinionAuxPartTwo = ((-Utils.OPINION_MAX * M) + (Utils.OPINION_MIN * m)) / (m - M);
    }

    private void cloneAgents() {
        for (Agent agent : agents) {
            agent.cloneAgent();
        }
    }

    public void simulate() {
        do {
            resetFOLimits();
            for (Agent agent : agents) {
                agent.calculateLocalFO();
                checkFOLimits(agent.getBestFO());
                Agent.foExplorationCounter++;
                if (Utils.stopCriteriaReached) {
                    break;
                }
            }
            Utils.bestFO = m;
            calculateOpinionAux();
            if (!Utils.stopCriteriaReached) {
                for (Agent agent : agents) {
                    agent.updateBestOpinion(opinionAuxPartOne, opinionAuxPartTwo, m, M);
                    agent.updateRange(attenuator, stdDev);
                }
            }
            if (!Utils.stopCriteriaReached) {
                checkInlfuence();
                interact();
            }

            if (!Utils.stopCriteriaReached) {
                move();
            }
        } while (!Utils.stopCriteriaReached && Utils.bestFO > Utils.GOAL_VALUE);
        generateReport();
    }

    public void checkInlfuence() {
        for (int i = 0; i < totalAgents; i++) {
            for (int j = 0; j < totalAgents; j++) {
                if (i == j) {
                    continue;
                }
                agents[i].checkInfluenceSphere(agents[j]);
            }
        }
    }

    public void interact() {
        cloneAgents();
        for (Agent agent : agents) {
            agent.interact(boundaries);
            if (Utils.stopCriteriaReached) {
                break;
            }
        }
    }

    public void move() {
        for (Agent agent : agents) {
            agent.move(boundaries);
        }
    }

    private void generateReport() {
        report.insertInitialParameters("Number of agents: " + totalAgents);
        report.insertInitialParameters("Total evaluations: " + Agent.testCounter);
        report.insertInitialParameters("Total evaluations: " + Agent.testCounter + ", evaluations by interaction: " + Agent.foInteractionCounter + ", evaluations by movement: " + Agent.foExplorationCounter);
        report.insertInitialParameters("Total skipped tests: " + Agent.totalNotCalculated);
        report.insertInitialParameters("Total exploration contributions: " + Agent.foGotBetterExploring + ", total interaction contributions: " + Agent.foGotBetterInteracting);
        if (userMode) {
            report.insertInitialParameters("FO: " + fo.getFo());
        } else {
            report.insertInitialParameters("FO: " + fo.getFoKey());
        }
        report.generateResultsCSV(Arrays.asList(agents), fo.getCoords(), userMode);
        report.createReport(dir + System.getProperty("file.separator") + "reports");
    }
}
