package de.uni_passau.fim.se2.se.test_prioritisation.stopping_conditions;

/**
 * Stopping condition that stops the search after a specified number of fitness evaluations.
 */
public class MaxFitnessEvaluations implements StoppingCondition {
    private final int maxFitnessEvaluations;
    private int currentEvaluations;

    public MaxFitnessEvaluations(final int maxFitnessEvaluations) {
        if (maxFitnessEvaluations <= 0) {
            throw new IllegalArgumentException("Max fitness evaluations must be greater than 0.");
        }
        this.maxFitnessEvaluations = maxFitnessEvaluations;
        this.currentEvaluations = 0;
    }

    @Override
    public void notifySearchStarted() {
        currentEvaluations = 0;
    }

    @Override
    public void notifyFitnessEvaluation() {
        currentEvaluations++;
    }

    @Override
    public boolean searchMustStop() {
        return currentEvaluations >= maxFitnessEvaluations;
    }

    @Override
    public double getProgress() {
        return Math.min(1.0, (double) currentEvaluations / maxFitnessEvaluations);
    }
}
