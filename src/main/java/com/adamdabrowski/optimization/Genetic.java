package com.adamdabrowski.optimization;

import org.mariuszgromada.math.mxparser.*;

public class Genetic implements Optimizer {
    @Override
    public String Optimize(String function) {
        Function func = new Function(function);

        if (func.getArgumentsNumber() != 2) {
            return null;
        }

        return "f(0, 0) = " + func.calculate(0, 0);
    }
}
