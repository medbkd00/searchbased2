package de.uni_passau.fim.se2.se.test_prioritisation.algorithms;

import de.uni_passau.fim.se2.se.test_prioritisation.encodings.Encoding;
import de.uni_passau.fim.se2.se.test_prioritisation.encodings.EncodingGenerator;
import de.uni_passau.fim.se2.se.test_prioritisation.fitness_functions.FitnessFunction;
import de.uni_passau.fim.se2.se.test_prioritisation.stopping_conditions.StoppingCondition;

/**
 * Implements a random search space exploration. To this end, a number of solutions are sampled in a
 * random fashion and the best encountered solution is returned.
 *
 * @param <E> the type of encoding
 */
public final class RandomSearch<E extends Encoding<E>> implements SearchAlgorithm<E> {
    private final StoppingCondition stoppingCondition;
    private final EncodingGenerator<E> encodingGenerator;
    private final FitnessFunction<E> fitnessFunction;

    /**
     * Constructs a RandomSearch instance.
     *
     * @param stoppingCondition the condition determining when the search stops
     * @param encodingGenerator the generator to produce random solutions
     * @param fitnessFunction   the function to evaluate the quality of solutions
     */
    public RandomSearch(
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
     * Finds the best solution by sampling random solutions and evaluating their fitness.
     *
     * @return the best solution found
     */
    @Override
    public E findSolution() {
        E bestSolution = null;
        double bestFitness = Double.NEGATIVE_INFINITY;

        stoppingCondition.notifySearchStarted(); // Notify the stopping condition that search has started

        while (!stoppingCondition.searchMustStop()) {
            // Generate a random candidate solution
            E candidate = encodingGenerator.get();

            // Evaluate the fitness of the candidate solution
            double fitness = fitnessFunction.maximise(candidate);

            // Check if the candidate is better than the current best solution
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestSolution = candidate;
            }

            // Notify the stopping condition after each fitness evaluation
            stoppingCondition.notifyFitnessEvaluation();
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
