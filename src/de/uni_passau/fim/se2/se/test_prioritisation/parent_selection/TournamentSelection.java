package de.uni_passau.fim.se2.se.test_prioritisation.parent_selection;

import de.uni_passau.fim.se2.se.test_prioritisation.encodings.TestOrder;
import de.uni_passau.fim.se2.se.test_prioritisation.fitness_functions.APLC;

import java.util.*;

public class TournamentSelection implements ParentSelection<TestOrder> {
    private final int tournamentSize;
    private final APLC fitnessFunction;
    private final Random random;
    private final static int DEFAULT_TOURNAMENT_SIZE = 5;

    public TournamentSelection(int tournamentSize, APLC fitnessFunction, Random random) {
        if (fitnessFunction == null || random == null) {
            throw new NullPointerException("Fitness function and random must not be null.");
        }
        if (tournamentSize <= 0) {
            throw new IllegalArgumentException("Tournament size must be greater than 0.");
        }
        this.tournamentSize = tournamentSize;
        this.fitnessFunction = fitnessFunction;
        this.random = random;
    }

    public TournamentSelection(APLC fitnessFunction, Random random) {
        this(DEFAULT_TOURNAMENT_SIZE, fitnessFunction, random);
    }

    @Override
    public TestOrder selectParent(List<TestOrder> population) {
        if (population == null || population.isEmpty()) {
            throw new IllegalArgumentException("Population must not be null or empty.");
        }

        // Ensure tournament size is not greater than population size
        if (tournamentSize > population.size()) {
            throw new IllegalArgumentException("Tournament size must be smaller than population size.");
        }

        // Create a tournament from the population
        List<TestOrder> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            TestOrder randomIndividual = population.get(random.nextInt(population.size()));
            tournament.add(randomIndividual);
        }

        // Select the best individual based on fitness
        return Collections.max(tournament, Comparator.comparingDouble(fitnessFunction::applyAsDouble));
    }


}
