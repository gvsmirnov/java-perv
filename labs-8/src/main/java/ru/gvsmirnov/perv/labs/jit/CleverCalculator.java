package ru.gvsmirnov.perv.labs.jit;

import java.util.Collection;
import java.util.DoubleSummaryStatistics;

import static java.util.stream.Collectors.summarizingDouble;

public class CleverCalculator implements BusinessLogic {

    @Override
    public double calculateStuff(Collection<Number> numbers) {
        DoubleSummaryStatistics stats = numbers.stream()
                .parallel()
                .collect(summarizingDouble(Number::doubleValue));

        return (stats.getMax() - stats.getMin()) / stats.getCount();
    }
}
