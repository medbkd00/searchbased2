package de.uni_passau.fim.se2.se.test_prioritisation.crossover;

import de.uni_passau.fim.se2.se.test_prioritisation.encodings.TestOrder;

import java.util.*;

public class OrderCrossover implements Crossover<TestOrder> {

    /**
     * The internal source of randomness.
     */
    private final Random random;

    /**
     * Creates a new order crossover operator.
     *
     * @param random the internal source of randomness
     */
    public OrderCrossover(final Random random) {
        this.random = random;
    }

    /**
     * Combines two parent encodings to create a new offspring encoding using the order crossover operation.
     * The order crossover corresponds to a two-point crossover where the section between two random indices is copied
     * from the first parent and the remaining alleles are added in the order they appear in the second parent.
     * The resulting children must correspond to a valid test order encoding of size n that represents a permutation of tests
     * where each test value in the range [0, n-1] appears exactly once.
     *
     * @param parent1 the first parent encoding
     * @param parent2 the second parent encoding
     * @return the offspring encoding
     */
    @Override
    public TestOrder apply(TestOrder parent1, TestOrder parent2) {
        int size = parent1.size();

        // Randomly select two crossover points
        int crossoverPoint1 = random.nextInt(size);
        int crossoverPoint2 = random.nextInt(size);

        // Ensure crossoverPoint1 < crossoverPoint2 for consistency
        if (crossoverPoint1 > crossoverPoint2) {
            int temp = crossoverPoint1;
            crossoverPoint1 = crossoverPoint2;
            crossoverPoint2 = temp;
        }

        // Create an offspring array to store the new test order
        int[] offspringPositions = new int[size];
        Arrays.fill(offspringPositions, -1); // Fill with invalid values initially

        // Copy the section between crossover points from the first parent
        for (int i = crossoverPoint1; i <= crossoverPoint2; i++) {
            offspringPositions[i] = parent1.getPositions()[i];
        }

        // Fill the remaining spots from the second parent, preserving their order
        int offspringIndex = 0;
        for (int i = 0; i < size; i++) {
            int test = parent2.getPositions()[i];
            // If the test is already present, skip it
            if (Arrays.stream(offspringPositions).anyMatch(x -> x == test)) {
                continue;
            }
            // Find the next available spot in the offspring
            while (offspringPositions[offspringIndex] != -1) {
                offspringIndex++;
            }
            offspringPositions[offspringIndex] = test;
        }

        // Return the new offspring encoding
        return new TestOrder(parent1.getMutation(), offspringPositions);
    }
}
