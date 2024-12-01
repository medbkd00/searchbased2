package de.uni_passau.fim.se2.se.test_prioritisation.algorithms;

import de.uni_passau.fim.se2.se.test_prioritisation.encodings.Encoding;
import de.uni_passau.fim.se2.se.test_prioritisation.encodings.EncodingGenerator;
import de.uni_passau.fim.se2.se.test_prioritisation.fitness_functions.FitnessFunction;
import de.uni_passau.fim.se2.se.test_prioritisation.stopping_conditions.StoppingCondition;
import de.uni_passau.fim.se2.se.test_prioritisation.utils.Randomness;  // Import the Randomness class

import java.util.Random;

/**
 * Implements the Simulated Annealing algorithm for test order prioritisation based on
 * -----------------------------------------------------------------------------------------
 * Flow chart of the algorithm:
 * Bastien Chopard, Marco Tomassini, "An Introduction to Metaheuristics for Optimization",
 * (Springer), Ch. 4.3, Page 63
 * -----------------------------------------------------------------------------------------
 * Note we've applied a few modifications to add elitism.
 *
 * @param <E> the type of encoding
 */
public final class SimulatedAnnealing<E extends Encoding<E>> implements SearchAlgorithm<E> {
    private final StoppingCondition stoppingCondition;
    private final EncodingGenerator<E> encodingGenerator;
    private final FitnessFunction<E> energy;
    private final int degreesOfFreedom;

    public SimulatedAnnealing(
            final StoppingCondition stoppingCondition,
            final EncodingGenerator<E> encodingGenerator,
            final FitnessFunction<E> energy,
            final int degreesOfFreedom, Random random) {
        if (stoppingCondition == null || encodingGenerator == null || energy == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        this.stoppingCondition = stoppingCondition;
        this.encodingGenerator = encodingGenerator;
        this.energy = energy;
        this.degreesOfFreedom = degreesOfFreedom;
    }

    @Override
    public E findSolution() {
        E currentSolution = encodingGenerator.get();  // Generate initial solution
        double currentEnergy = energy.minimise(currentSolution);
        E bestSolution = currentSolution;
        double bestEnergy = currentEnergy;

        double temperature = 1000.0; // Initial temperature
        double coolingRate = 0.95;  // Cooling factor
        int iterationCount = 0;

        stoppingCondition.notifySearchStarted();  // Notify search start

        while (!stoppingCondition.searchMustStop()) {
            // Generate a neighbor solution
            E neighbor = currentSolution.mutate(Randomness.random(), degreesOfFreedom);
            double neighborEnergy = energy.minimise(neighbor);

            // Acceptance probability
            if (neighborEnergy < currentEnergy || Math.exp((currentEnergy - neighborEnergy) / temperature) > Randomness.random().nextDouble()) {
                currentSolution = neighbor;
                currentEnergy = neighborEnergy;

                // Update the best solution
                if (currentEnergy < bestEnergy) {
                    bestSolution = currentSolution;
                    bestEnergy = currentEnergy;
                }
            }

            // Cooling schedule
            temperature *= coolingRate;
            iterationCount++;

            // Debugging logs (optional)
            System.out.printf("Iteration: %d, Temperature: %.2f, Current Energy: %.4f, Best Energy: %.4f%n",
                    iterationCount, temperature, currentEnergy, bestEnergy);

            stoppingCondition.notifyFitnessEvaluation(); // Notify fitness evaluation
        }

        return bestSolution;
    }

    @Override
    public StoppingCondition getStoppingCondition() {
        return stoppingCondition;
    }
}
