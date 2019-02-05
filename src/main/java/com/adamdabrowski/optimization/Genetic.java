package com.adamdabrowski.optimization;

import org.mariuszgromada.math.mxparser.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Genetic implements Optimizer {
    @Override
    public String Optimize(String function) {
        Function func = new Function(function);
        int argumentsNumber = func.getArgumentsNumber();

        if (argumentsNumber < 1) {
            return null;
        }

        // Parameters
        int populationSize = 100;
        int best = 20;
//        int lucky = 5;
        int generations = 1000;

        // Population
        double[][] population = new double[populationSize][]; // 0..99
        for (int i = 0; i < populationSize; i++) {
            population[i] = randomSolution(argumentsNumber);
        }

        // Evolve
        for (int i = 0; i < generations; i++) {
            // Fitness
            Arrays.sort(population, Comparator.comparingDouble(func::calculate)); // Ascending

            // Select best
            double[][] breeders = new double[best][]; // 0..19
            for (int j = 0; j < best; j++) {
                breeders[j] = population[j].clone();
            }

            // Crossover
            double[][] children = new double[populationSize][]; // 0..99
            for (int j = 0; j < best / 2; j++) { // Parents // 0..9
                int childCount = populationSize / (best / 2); // 5

                for (int x = 0; x < childCount; x++) { //  // 0..4
                double[] child = new double[argumentsNumber];

                for (int k = 0; k < argumentsNumber; k++) { // Genes
                    String binaryA = Long.toBinaryString(Double.doubleToRawLongBits(breeders[0][k]));
                    String binaryB = Long.toBinaryString(Double.doubleToRawLongBits(breeders[best - 1][k]));

                    StringBuilder crossover = new StringBuilder();
                    for (int l = 0; l < binaryA.length() && l < binaryB.length(); l++) {
                        if (l % 2 == 0) {
                            crossover.append(binaryA.charAt(l));
                        } else {
                            crossover.append(binaryB.charAt(l));
                        }
                    }

                    child[k] = Double.longBitsToDouble(new BigInteger(crossover.toString(), 2).longValue());
                }

                Mutate(child);

                children[j * childCount + x] = child;
                }
            }

            population = children;
        }

        // Best
        Arrays.sort(population, Comparator.comparingDouble(func::calculate)); // Ascending
        return printSolution(func, population[0]);
    }

    void Mutate(double[] genes) {
        int geneCount = genes.length;

        Random r = new Random();
        if (r.nextDouble() * 100 > 98) {
            int randomGene = r.nextInt(geneCount);
            String binary = Long.toBinaryString(Double.doubleToRawLongBits(genes[randomGene]));
            int randomBit = r.nextInt(binary.length());
            char bitValue = binary.charAt(randomBit);
            char flippedBit = bitValue == '1' ? '0' : '1';

            StringBuilder mutatedBinary = new StringBuilder(binary);
            mutatedBinary.setCharAt(randomBit, flippedBit);
            genes[randomGene] = Double.longBitsToDouble(new BigInteger(mutatedBinary.toString(), 2).longValue());
        }
    }

    double[] randomSolution(int arguments) {
        Random r = new Random();
        double[] solution = new double[arguments];
        for (int i = 0; i < arguments; i++) {
            solution[i] = r.nextDouble();
        }
        return solution;
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
}
