package com.estatehub.util;

public class EMICalculator {

    public static double calculateEMI(
            double loanAmount,
            double annualInterestRate,
            int tenureYears) {

        if (loanAmount <= 0 || annualInterestRate < 0
                || tenureYears <= 0) {
            throw new IllegalArgumentException(
                    "Invalid loan details");
        }

        double monthlyInterestRate =
                annualInterestRate / (12 * 100);

        int totalMonths = tenureYears * 12;

        if (monthlyInterestRate == 0) {
            return loanAmount / totalMonths;
        }

        double power = Math.pow(
                1 + monthlyInterestRate, totalMonths);

        return loanAmount * monthlyInterestRate * power
                / (power - 1);
    }
}