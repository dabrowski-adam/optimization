package com.adamdabrowski.optimization;

import org.mariuszgromada.math.mxparser.*;

public class SimulatedAnnealing implements Optimizer {
    @Override
    public String Optimize(String function) {
        Function func = new Function(function);

        if (func.getArgumentsNumber() != 2) {
            return null;
        }

        // Parameters
        double coolingRate = 0.003;
        double initialTemperature = 10000.0;
        double initialX = 0.0;
        double initialY = 0.0;

        // Search
        double x = initialX;
        double y = initialY;
        double bestX = x;
        double bestY = y;
        for (double t = initialTemperature; t > 1.0; t *= (1 - coolingRate)) {
            // Select neighbour
            double neighbourX = neighbour(x, t / initialTemperature);
            double neighbourY = neighbour(y, t / initialTemperature);

            // Get energies
            double currentEnergy = func.calculate(x, y);
            double neighbourEnergy = func.calculate(neighbourX, neighbourY);

            // Decide whether to move
            if (acceptanceProbability(currentEnergy, neighbourEnergy, t) > Math.random()) {
                x = neighbourX;
                y = neighbourY;
            }

            // Track best solution
            if (func.calculate(x, y) < func.calculate(bestX, bestY)) {
                bestX = x;
                bestY = y;
            }
        }

        return "f(" + bestX + ", " + bestY + ") = " + func.calculate(bestX, bestY);
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
}
