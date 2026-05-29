public class Main {

    public static void main(String[] args) {

        BankersAlgorithm bankersAlgorithm = new BankersAlgorithm();

        bankersAlgorithm.calculateNeedMatrix();

        bankersAlgorithm.printNeedMatrix();

        bankersAlgorithm.checkSafety();

        System.out.println();

        // test a request
        int[] request = {1,2,3};
        bankersAlgorithm.requestResources(1, request);
    }
}