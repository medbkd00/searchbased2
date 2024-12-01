package de.uni_passau.fim.se2.se.test_prioritisation.encodings;

import de.uni_passau.fim.se2.se.test_prioritisation.mutations.Mutation;

import java.util.Random;

public class TestOrderGenerator implements EncodingGenerator<TestOrder> {

    private final Random random;
    private final Mutation<TestOrder> mutation;
    private final int testCases;

    /**
     * Creates a new test order generator with the given mutation and number of test cases.
     *
     * @param random    the source of randomness
     * @param mutation  the elementary transformation that the generated orderings will use
     * @param testCases the number of test cases in the ordering
     */
    public TestOrderGenerator(final Random random, final Mutation<TestOrder> mutation, final int testCases) {
        this.random = random;
        this.mutation = mutation;
        this.testCases = testCases;
    }

    /**
     * Creates and returns a random permutation of test cases.
     *
     * @return random test case ordering
     */
    @Override
    public TestOrder get() {
        int[] randomOrder = new int[testCases];
        for (int i = 0; i < testCases; i++) {
            randomOrder[i] = i;
        }

        // Randomly shuffle the test order
        for (int i = testCases - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = randomOrder[i];
            randomOrder[i] = randomOrder[j];
            randomOrder[j] = temp;
        }

        return new TestOrder(mutation, randomOrder);
    }
}
