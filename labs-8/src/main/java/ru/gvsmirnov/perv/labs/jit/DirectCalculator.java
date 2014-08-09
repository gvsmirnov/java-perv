package ru.gvsmirnov.perv.labs.jit;

import java.util.Collection;

public class DirectCalculator implements BusinessLogic {
    @Override
    public double calculateStuff(Collection<Number> numbers) {
        Number min = null, max = null;

        for(Number number : numbers) {
            if(min == null || min.doubleValue() > number.doubleValue()) {
                min = number;
            }

            if(max == null || max.doubleValue() < number.doubleValue()) {
                max = number;
            }
        }

        return (max.doubleValue() - min.doubleValue()) / numbers.size();

    }
}
