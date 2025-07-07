package com.quiz.learning.Demo.service;

public class CalculationFunction {
    public static boolean isPrime(long n) {
        if (n <= 1)
            return false;
        if (n == 2)
            return true;
        if (n % 2 == 0)
            return false;

        int sqrt = (int) Math.sqrt(n);
        for (int i = 3; i <= sqrt; i += 2) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    public static int maxDivisorOfNUnder10(long n) {
        for (int i = 10; i >= 1; i--) {
            if (n % i == 0) {
                return i;
            }
        }
        return -1; // Trường hợp bất thường, nếu n < 1
    }

}
