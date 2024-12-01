package de.uni_passau.fim.se2.se.test_prioritisation.algorithms;

import de.uni_passau.fim.se2.se.test_prioritisation.encodings.Encoding;
import de.uni_passau.fim.se2.se.test_prioritisation.encodings.EncodingGenerator;
import de.uni_passau.fim.se2.se.test_prioritisation.fitness_functions.FitnessFunction;
import de.uni_passau.fim.se2.se.test_prioritisation.stopping_conditions.StoppingCondition;

import java.util.Random;

/**
 * Implements a random walk through the search space.
 *
 * @param <E> the type of encoding
 */
public final class RandomWalk<E extends Encoding<E>> implements SearchAlgorithm<E> {
    private final StoppingCondition stoppingCondition;
    private final EncodingGenerator<E> encodingGenerator;
    private final FitnessFunction<E> fitnessFunction;

    /**
     * Constructs a RandomWalk instance.
     *
     * @param stoppingCondition the condition determining when the search stops
     * @param encodingGenerator the generator to produce initial solutions
     * @param fitnessFunction   the function to evaluate the quality of solutions
     */
    public RandomWalk(
            final StoppingCondition stoppingCondition,
            final EncodingGenerator<E> encodingGenerator,
            final FitnessFunction<E> fitnessFunction) {
        if (stoppingCondition == null || encodingGenerator == null || fitnessFunction == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        this.stoppingCondition = stoppingCondition;
        this.encodingGenerator = encodingGenerator;
        this.fitnessFunction = fitnessFunction;
    }

    /**
     * Finds the best solution by conducting a random walk in the search space.
     *
     * @return the best solution found
     */
    @Override
    public E findSolution() {
        // Generate the initial solution
        E currentSolution = encodingGenerator.get();
        double bestFitness = fitnessFunction.maximise(currentSolution);
        E bestSolution = currentSolution;

        stoppingCondition.notifySearchStarted(); // Notify the stopping condition that search has started

        while (!stoppingCondition.searchMustStop()) { // Check stopping condition
            // Generate a mutated neighbor of the current solution
            E neighbor = currentSolution.mutate(new Random(), 1);
            double fitness = fitnessFunction.maximise(neighbor);

            // Update the best solution if the neighbor is better
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestSolution = neighbor;
            }

            // Move to the neighbor solution
            currentSolution = neighbor;
            stoppingCondition.notifyFitnessEvaluation(); // Notify after fitness evaluation
        }

        // Return the best solution found
        return bestSolution;
    }

    /**
     * Returns the stopping condition for this search algorithm.
     *
     * @return the stopping condition
     */
    @Override
    public StoppingCondition getStoppingCondition() {
        return stoppingCondition;
    }
}
