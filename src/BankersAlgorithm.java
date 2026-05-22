public class BankersAlgorithm {
    /**
     * Total number of processes
     */
    private final int processes = 3;

    /**
     * Total number of resource types
     */
    private final int resources = 3;

    /**
     * Maxi demanded matrix
     */
    private final int[][] max = {
            {6, 4, 3},
            {2, 2, 2},
            {8, 1, 3}
    };

    /**
     * Allocation matrix
     */
    private final int[][] allocation = {
            {1, 2, 1},
            {1, 0, 0},
            {4, 0, 2}
    };

    /**
     * Need matrix
     */
    private final int[][] need = new int[processes][resources];

    /**
     * Calculates the need matrix
     *
     * Formula:
     * Need = Max - Allocation
     */
    public void calculateNeedMatrix() {

        for (int i = 0; i < processes; i++) {

            for (int j = 0; j < resources; j++) {

                assert allocation[i][j] <= max[i][j]
                        : "Allocation cannot reach maximum demand";

                need[i][j] = max[i][j] - allocation[i][j];
            }
        }
    }

    /**
     * Prints the need matrix
     */
    public void printNeedMatrix() {

        System.out.println("Need Matrix:");

        for (int i = 0; i < processes; i++) {

            System.out.print("P" + i + ": ");

            for (int j = 0; j < resources; j++) {

                System.out.print(need[i][j] + " ");
            }

            System.out.println();
        }
    }

    /**
     * Available resources vector
     */
    private final int[] available = {1, 2, 3};


    /**
     * Checks the system safe state
     */
    public void checkSafety() {

        System.out.println("---- Checking system safety ----");

        // Simulate allocations on current wit no changing original data
        // Temp copy of available -> work vector
        int[] current = new int[resources];

        for (int i = 0; i < resources; i++) {

            current[i] = available[i];
        }

        // Show c.vector
        System.out.println("Work vector:");

        for (int value : current) {

            System.out.print(value + " ");
        }

        System.out.println();
    }
}
