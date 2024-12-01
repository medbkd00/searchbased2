package de.uni_passau.fim.se2.se.test_prioritisation.encodings;

import de.uni_passau.fim.se2.se.test_prioritisation.mutations.Mutation;

import java.util.Arrays;

public class TestOrder extends Encoding<TestOrder> {

    /**
     * Internal backing array that stores the actual ordering of the tests.
     */
    private final int[] positions;

    /**
     * Creates a new test order with the given mutation and test case ordering.
     *
     * @param mutation  the mutation to be used with this encoding
     * @param positions the test case ordering
     */
    public TestOrder(Mutation<TestOrder> mutation, int[] positions) {
        super(mutation);
        if (!isValid(positions)) {
            throw new IllegalArgumentException("Invalid test order.");
        }
        this.positions = positions.clone();
    }

    /**
     * Tells whether the given array represents a valid regression test case prioritization encoding.
     *
     * @param tests the test suite prioritization array to check
     * @return {@code true} if the given prioritization is valid, {@code false} otherwise
     */
    public static boolean isValid(final int[] tests) {
        if (tests == null) return false;

        boolean[] seen = new boolean[tests.length];
        for (int test : tests) {
            if (test < 0 || test >= tests.length || seen[test]) {
                return false;
            }
            seen[test] = true;
        }
        return true;
    }

    /**
     * Creates a deep copy of this TestOrder object.
     *
     * @return a new TestOrder with a copy of the positions array
     */
    @Override
    public TestOrder deepCopy() {
        return new TestOrder(getMutation(), positions.clone());
    }

    /**
     * Returns the number of test cases in this test case ordering.
     *
     * @return the number of test cases
     */
    public int size() {
        return positions.length;
    }

    /**
     * Returns a reference to the underlying internal backing array.
     *
     * @return the orderings array
     */
    public int[] getPositions() {
        return positions;
    }

    @Override
    public TestOrder self() {
        return this;
    }
}
