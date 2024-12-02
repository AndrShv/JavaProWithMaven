

import java.util.Scanner;

import java.util.Scanner;

public class MainStarter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the calculator!");

        System.out.println("Enter the first number: ");
        double num1 = scanner.nextDouble();

        System.out.println("Enter the second number: ");
        double num2 = scanner.nextDouble();

        System.out.println("Enter the operator (+, -, *, /): ");
        String operator = scanner.next();

        double result = calc(num1, num2, operator);
        System.out.println("Result: " + result);
    }

    public static double calc(double num1, double num2, String operator) {
        double result = 0;
        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    System.out.println("Error: Division by zero.");
                    return Double.NaN;
                }
                break;
            default:
                System.out.println("Error: Invalid operator. Use +, -, *, or /.");
                return Double.NaN;
        }
        return result;
    }
}

