package de.uni_passau.fim.se2.se.test_prioritisation.mutations;

import de.uni_passau.fim.se2.se.test_prioritisation.encodings.TestOrder;

import java.util.Random;

public class ShiftToBeginningMutation implements Mutation<TestOrder> {

    /**
     * The internal source of randomness.
     */
    private final Random random;

    public ShiftToBeginningMutation(final Random random) {
        this.random = random;
    }

    /**
     * Shifts a test to the beginning of the sequence.
     *
     * @param encoding the test order to be mutated
     * @return the mutated test order
     */
    @Override
    public TestOrder apply(TestOrder encoding) {
        // Get the number of tests in the order
        int size = encoding.size();

        // Randomly select an index
        int index = random.nextInt(size);

        // Get the test at the selected index
        int testToShift = encoding.getPositions()[index];

        // Remove the test from its original position by shifting all elements to the left
        int[] newPositions = new int[size];
        System.arraycopy(encoding.getPositions(), 0, newPositions, 1, index);
        System.arraycopy(encoding.getPositions(), index + 1, newPositions, index + 1, size - index - 1);

        // Insert the test at the beginning
        newPositions[0] = testToShift;

        // Return the mutated encoding
        return new TestOrder(encoding.getMutation(), newPositions);
    }
}
