/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsproject1;

/**
 *
 * @author Amj
 */
public class Main {



      public static void main(String[] args) {
        BigInt firstNumber;
        BigInt secondNumber;

        System.out.println("Enter first number: ");
        firstNumber = inputBigIntNumber();

        System.out.println("Enter second number: ");
        secondNumber = inputBigIntNumber();

        System.out.println("The result of plus is: " + firstNumber.plus(secondNumber));
        System.out.println("The result of minus is: " + firstNumber.minus(secondNumber));
        System.out.println("The result of multiply is: " + firstNumber.multiply(secondNumber));

        try {
            System.out.println("The result of divide is: " + firstNumber.divide(secondNumber));
        } catch (ArithmeticException ex){
            System.out.println("Can not divide by zero");
        }

    }   

}
    

