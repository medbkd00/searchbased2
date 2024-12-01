package de.uni_passau.fim.se2.se.test_prioritisation.fitness_functions;

import de.uni_passau.fim.se2.se.test_prioritisation.encodings.TestOrder;

/**
 * The Average Percentage of Lines Covered (APLC) fitness function.
 */
public final class APLC implements FitnessFunction<TestOrder> {

    /**
     * The coverage matrix to be used when computing the APLC metric.
     */
    private final boolean[][] coverageMatrix;

    /**
     * Creates a new APLC fitness function with the given coverage matrix.
     *
     * @param coverageMatrix the coverage matrix to be used when computing the APLC metric
     */
    public APLC(final boolean[][] coverageMatrix) {
        if (coverageMatrix == null) {
            throw new IllegalArgumentException("Coverage matrix cannot be null");
        }
        if (coverageMatrix.length == 0 || coverageMatrix[0].length == 0) {
            throw new IllegalArgumentException("Coverage matrix cannot be empty");
        }
        this.coverageMatrix = coverageMatrix;
    }

    /**
     * Computes and returns the APLC for the given order of test cases.
     * Orderings that achieve a higher rate of coverage are rewarded with higher values.
     * The APLC ranges between 0.0 and 1.0.
     *
     * @param testOrder the proposed test order for which the fitness value will be computed
     * @return the APLC value of the given test order
     * @throws NullPointerException if {@code null} is given
     */
    @Override
    public double applyAsDouble(final TestOrder testOrder) throws NullPointerException {
        if (testOrder == null) {
            throw new NullPointerException("Test order cannot be null");
        }

        int n = coverageMatrix.length;  // Number of test cases
        int m = coverageMatrix[0].length;  // Number of lines
        int[] positions = testOrder.getPositions();  // Test execution order

        // Array to store the first test that covers each line
        int[] firstCoveringTest = new int[m];

        // Initialize all entries to an invalid value (e.g., -1)
        for (int i = 0; i < m; i++) {
            firstCoveringTest[i] = -1;
        }

        // Find the first test covering each line
        for (int testIndex = 0; testIndex < positions.length; testIndex++) {
            int test = positions[testIndex];
            for (int line = 0; line < m; line++) {
                if (coverageMatrix[test][line] && firstCoveringTest[line] == -1) {
                    // Record the 1-based index of the first test that covers this line
                    firstCoveringTest[line] = testIndex + 1;
                }
            }
        }

        // Compute the sum of the first test indices covering each line
        int sumFirstCoverage = 0;
        for (int line = 0; line < m; line++) {
            if (firstCoveringTest[line] != -1) {
                sumFirstCoverage += firstCoveringTest[line];
            } else {
                // If no test covers a line, penalize it by adding (n + 1)
                sumFirstCoverage += (n + 1);
            }
        }

        // Apply the APLC formula
        return 1.0 - ((double) sumFirstCoverage / (n * m)) + (1.0 / (2 * n));
    }

    /**
     * Returns the fitness for maximisation.
     */
    @Override
    public double maximise(TestOrder encoding) throws NullPointerException {
        return applyAsDouble(encoding);
    }

    /**
     * Returns the fitness for minimisation.
     */
    @Override
    public double minimise(TestOrder encoding) throws NullPointerException {
        return -applyAsDouble(encoding);
    }
}
