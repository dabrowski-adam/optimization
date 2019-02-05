package com.adamdabrowski.optimization;

import org.mariuszgromada.math.mxparser.*;

public class SimulatedAnnealing implements Optimizer {
    @Override
    public String Optimize(String function) {
        Function func = new Function(function);
        int argumentsNumber = func.getArgumentsNumber();

        if (argumentsNumber < 1) {
            return null;
        }

        // Parameters
        double coolingRate = 0.003;
        double initialTemperature = 10000.0;

        // Search
        double[] current = initialSolution(argumentsNumber);
        double[] best = current.clone();

        for (double t = initialTemperature; t > 1.0; t *= (1 - coolingRate)) {
            // Select neighbour
            double[] neighbour = neighbourSolution(current, t / initialTemperature);

            // Get energies
            double currentEnergy = func.calculate(current);
            double neighbourEnergy = func.calculate(neighbour);

            // Decide whether to move
            if (acceptanceProbability(currentEnergy, neighbourEnergy, t) > Math.random()) {
                current = neighbour.clone();
            }

            // Track best solution
            if (func.calculate(current) < func.calculate(best)) {
                best = current.clone();
            }
        }

        return printSolution(func, best);
    }

    String printSolution(Function func, double[] solution) {
        StringBuilder sb = new StringBuilder("f(");
        sb.append(solution[0]);
        for (int i = 1; i < func.getArgumentsNumber(); i++) {
            sb.append(",");
            sb.append(solution[i]);
        }
        sb.append(") = ");
        sb.append(func.calculate(solution));

        return sb.toString();
    }

    double acceptanceProbability(double solutionEnergy, double neighbourEnergy, double temperature) {
        if (neighbourEnergy > solutionEnergy) {
            return 1.0;
        }
        return Math.exp((solutionEnergy - neighbourEnergy) / temperature);
    }

    double neighbour(double p, double coolingProgress) {
        double maxDistance = 0.1;
        double sign = Math.random() > 0.5 ? 1 : -1;

        return p + sign * maxDistance * Math.random() * coolingProgress;
    }

    double[] neighbourSolution(double[] solution, double coolingProgress) {
        double[] neighbourSolution = solution.clone();
        for (int i = 0; i < neighbourSolution.length; i++) {
            neighbourSolution[i] = neighbour(neighbourSolution[i], coolingProgress);
        }
        return  neighbourSolution;
    }

    double[] initialSolution(int arguments) {
        double[] solution = new double[arguments];
        for (int i = 0; i < arguments; i++) {
            solution[i] = 0;
        }
        return solution;
    }
}
