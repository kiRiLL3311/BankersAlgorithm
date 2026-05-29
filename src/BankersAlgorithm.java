public class BankersAlgorithm {

    // Total number of processes
    private final int processes = 3;

    // Total number of resource types
    private final int resources = 3;

    // Max demanded matrix
    private final int[][] max = {
            {6, 4, 3},
            {2, 2, 2},
            {8, 1, 3}
    };

    // Allocation matrix
    private final int[][] allocation = {
            {1, 2, 1},
            {1, 0, 0},
            {4, 0, 2}
    };

    // Need matrix
    private final int[][] need = new int[processes][resources];

    // Available resources vector
    private final int[] available = {1, 2, 3};

    /**
     * Calculates the need matrix
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
     * Creates a deep copy of a 2D array to safely preserve the original state
     * @param matrix - [int][int]
     * @return copy of M - [int][int]
     */
    private int[][] copyMatrix(int[][] matrix) {
        int[][] copy = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                copy[i][j] = matrix[i][j];
            }
        }
        return copy;
    }

    /** * Checks the system safe state
     */
    public boolean checkSafety() {

        System.out.println("---- Checking system safety ----");

        // current = available -> work vector
        int[] current = initializeWork();

        System.out.println("Work vector:");
        for (int value : current) System.out.print(value + " ");
        System.out.println();

        // Aray init
        boolean[] finish = initializeFinish();

        System.out.println("Finish Arr:");
        for (boolean value : finish) System.out.print(value + " ");
        System.out.println();

        //---------------------------------------------------------//

        // Find a process: need <= current(work)

        int finishedCount = 0;
        int[] safeSeq = new int[processes];
        int index = 0;

        while (finishedCount < processes) {

            boolean foundProcess = false;

            for (int i = 0; i < processes; i++) {

                if (!finish[i]) {

                    if (canRun(i, current)) {

                        System.out.println("Process P" + i + " is executing");

                        // Store sequence
                        safeSeq[index++] = i;

                        // Release resources
                        exeProcess(i, current);

                        finish[i] = true;
                        finishedCount++;
                        foundProcess = true;

                        System.out.print("Updated Work: ");
                        for (int val : current) System.out.print(val + " ");
                        System.out.println();
                    }
                }
            }

            if (!foundProcess) {
                System.out.println("SYSTEM IS UNSAFE!");
                return false;
            }
        }

        System.out.println("SYSTEM IS SAFE!");
        return true;
    }

    /**
     * Initializes work array
     * @return work array[int]
     */
    private int[] initializeWork() {
        int[] work = new int[resources];
        for (int i = 0; i < resources; i++) {
            work[i] = available[i];
        }
        return work;
    }

    /**
     * Initializes finish array
     * @return initialization state - [boolean]
     */
    private boolean[] initializeFinish() {
        boolean[] finish = new boolean[processes];
        for (int i = 0; i < processes; i++) {
            finish[i] = false;
        }
        return finish;
    }

    /**
     * Checks if process can run
     * @param process process - int
     * @param work current vector - [int]
     * @return ability to run - boolean
     */
    private boolean canRun(int process, int[] work) {
        for (int j = 0; j < resources; j++) {
            if (need[process][j] > work[j]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Simulates a process finishing by releasing its allocated resources back to the available pool
     * @param process - int
     * @param work - [int]
     */
    private void exeProcess(int process, int[] work) {
        for (int j = 0; j < resources; j++) {
            work[j] += allocation[process][j];
        }
    }

    /**
     * Simulates how the OS decides whether to grant a process’s resource request
     */
    public void requestResources(int process, int[] request) {
        // Check req need
        for (int j = 0; j < resources; j++) {
            if (request[j] > need[process][j]) {
                System.out.println("Error: Request exceeds need");
                return;
            }
        }

        // 1) Check request <= available
        for (int j = 0; j < resources; j++) {
            if (request[j] > available[j]) {
                System.out.println("Not enough resources. Process must wait");
                return;
            }
        }

        // --Saving old state (for rollback)
        int[] oldAvailable = available.clone();
        int[][] oldAllocation = copyMatrix(allocation);
        int[][] oldNeed = copyMatrix(need);

        // 2) Simulate allocation
        for (int j = 0; j < resources; j++) {
            available[j] -= request[j];
            allocation[process][j] += request[j];
            need[process][j] -= request[j];
        }

        System.out.println("----Simulating to allocate----");

        // 3) Check safety
        boolean safe = checkSafety();

        // 4) Outcome
        if (safe) {
            System.out.println("Req GRANTED (safe)");
        } else {
            System.out.println("Req DENIED (unsafe)");

            // --If the system unsafe, revert all changes to maintain consistency
            // --Rollback
            rollback(oldAvailable, oldAllocation, oldNeed);
        }
    }

    /**
     * Restores the system to its previous state if a resource request leads to an unsafe condition
     * @param oldAvailable [int]
     * @param oldAllocation [int][int]
     * @param oldNeed [int][int]
     */
    private void rollback(int[] oldAvailable, int[][] oldAllocation, int[][] oldNeed) {
        for (int j = 0; j < resources; j++) {
            available[j] = oldAvailable[j];
        }
        for (int i = 0; i < processes; i++) {
            for (int j = 0; j < resources; j++) {
                allocation[i][j] = oldAllocation[i][j];
                need[i][j] = oldNeed[i][j];
            }
        }
    }
}