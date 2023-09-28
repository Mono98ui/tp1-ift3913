public class TestComment {
    public class FactorialCalculator {

        /**
         * Calculates the factorial of a non-negative integer.
         *
         * @param n The non-negative integer for which factorial is calculated.
         * @return The factorial of n.
         */
        /*
        assdsdadsadsasd
        adsadsdasdas
        dasasddasdas
         */
        public static long factorial(int n) {
            // Initialize the result to 1, as the factorial of 0 and 1 is 1.
            long result = 1;

            // Multiply numbers from 1 to n to calculate the factorial.
            for (int i = 1; i <= n; i++) {
                result *= i;
            }

            // Return the factorial result.
            return result;
        }

        public static void main(String[] args) {
            int number = 5;
            long factorialResult = factorial(number);
            System.out.println("The factorial of " + number + " is: " + factorialResult);
        }
    }

}
