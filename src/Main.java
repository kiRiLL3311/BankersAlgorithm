public class Main {

    public static void main(String[] args) {

        int processes = 5;
        int resources = 3;

        int[] available = {3, 3, 2};

        int[][] max = {
                {6, 4, 3},
                {2, 2, 2},
                {8, 1, 3}
        };

        int[][] allocation = {
                {1, 2, 1},
                {1, 0, 0},
                {4, 0, 2}
        };

        System.out.println("Data loaded successfully.");

    }
}