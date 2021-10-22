/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsproject1;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Amj
 */
public class DSProject1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
       
public class DSProject1 implements Comparable<DSProject1> {

    private static final char MINUS_CHAR = '-';
    private static final char PLUS_CHAR = '+';

    private ArrayList<Integer> numberDigits = new ArrayList<>();
    private boolean negative;
    private String stringNumber;
    private Scanner scanner = new Scanner(System.in);

    DSProject1(String number){

        if (number.equals("")){
            stringNumber = "0";
            numberDigits.add(0);
        }
        else {
            char firstChar = number.charAt(0);
            if (firstChar == MINUS_CHAR || firstChar == PLUS_CHAR) {
                if (firstChar == MINUS_CHAR)
                    negative = true;
                number = number.substring(1);
            }
	    number = number.replaceFirst("^0+(?!$)", "");
            stringNumber = number;

            for (int index = 0; index < number.length(); index++) {
                int curDigNumericVal = Character.getNumericValue(number.charAt(index));

                if (curDigNumericVal == -1)
                    throw new IllegalArgumentException();
		    numberDigits.add(curDigNumericVal);
            }
        }
    }

    private boolean isNegative() {
        return negative;
    }

    private void flipNegativity() {
        if (stringNumber == "0")
            return;

        negative = !negative;
        if (stringNumber.charAt(0) == MINUS_CHAR){
            stringNumber = stringNumber.substring(1);
        } else {
            stringNumber = MINUS_CHAR + stringNumber;
        }
    }

    DSProject1 plus(DSProject1 otherNumber) {

        if (negative && !otherNumber.isNegative()) {
            return otherNumber.minus(new DSProject1(stringNumber));
        }

        if (otherNumber.isNegative()) {
            return minus(new DSProject1(otherNumber.toString()));
        }

        ArrayList<Integer> longerNumber, shorterNumber;
        if (numberDigits.size() >= otherNumber.numberDigits.size()) {
            longerNumber = numberDigits;
            shorterNumber = otherNumber.numberDigits;
        }
        else {
            longerNumber = otherNumber.numberDigits;
            shorterNumber = numberDigits;
        }

        int lengthsDifferences = longerNumber.size() - shorterNumber.size();


        StringBuilder resultString = new StringBuilder();

        int carry = 0;


        for (int index = shorterNumber.size() - 1; index >= 0; index--) {
            int shorterNumberDigit = shorterNumber.get(index);
            int longerNumberDigit = longerNumber.get(index + lengthsDifferences);

            int newDigit = shorterNumberDigit + longerNumberDigit + carry;

            carry = newDigit / 10;
            newDigit = newDigit % 10;

            resultString.append(newDigit);
        }

        for (int index = lengthsDifferences - 1; index >= 0; index--) {
            int currDig = longerNumber.get(index);

            if (currDig + carry == 10) {
                resultString.append(0);
                carry = 1;
            } else {
                resultString.append(currDig + carry);
                carry = 0;
            }
        }

        if (carry > 0)
            resultString.append(carry);

        return new DSProject1(resultString.reverse().toString());
    }

    DSProject1 minus(DSProject1 otherNumber){

        if (otherNumber.isNegative()) {
            return plus(new DSProject1(otherNumber.stringNumber));
        }

        if (this.compareTo(otherNumber) < 0) {
            DSProject1 result = otherNumber.minus(this);
            result.flipNegativity();
            return result;
        }

        int lengthsDifferences = numberDigits.size() - otherNumber.numberDigits.size();

        StringBuilder resultString = new StringBuilder();

        int carry = 0;

        for (int index = otherNumber.numberDigits.size() - 1; index >=0 ; index--) {
            int biggerNumDig = numberDigits.get(index + lengthsDifferences) - carry;
            int smallerNumDig = otherNumber.numberDigits.get(index);

            carry = 0;

            if (biggerNumDig < smallerNumDig){
                carry = 1;
                biggerNumDig += 10;
            }

            resultString.append(biggerNumDig - smallerNumDig);
        }

        for (int index = lengthsDifferences - 1; index >=0 ; index--) {
            int currDig = numberDigits.get(index);

            if (carry > currDig){
                resultString.append(currDig + 10 - carry);
                carry = 1;
            } else {
                resultString.append(currDig - carry);
                carry = 0;
            }
        }

        return new DSProject1(resultString.reverse().toString());
    }

    DSProject1 multiply(DSProject1 otherNumber){

        DSProject1 finalResult = new DSProject1("0");
        DSProject1 currentUnit = new DSProject1("1");

        for (int otherNumIndex = otherNumber.numberDigits.size() - 1; otherNumIndex >=0; otherNumIndex--){
            int currentOtherNumDigit = otherNumber.numberDigits.get(otherNumIndex);

            DSProject1 currentResult = new DSProject1("0");
            DSProject1 currentDigitUnit = new DSProject1(currentUnit.toString());

            for (int index = numberDigits.size() - 1; index >=0; index--) {
                int currentDigit = numberDigits.get(index);
                int digitsMultiplication = currentDigit * currentOtherNumDigit;

                currentResult = currentDigitUnit.MultiplyUnit(digitsMultiplication);
                currentDigitUnit.multiplyByTen();
            }

            currentUnit.multiplyByTen();
            finalResult = finalResult.plus(currentResult);
        }

        if (otherNumber.isNegative() && !isNegative() || isNegative() && !otherNumber.isNegative())
            finalResult.flipNegativity();

        return finalResult;
    }

    DSProject1 divide(DSProject1 otherNumber) {

        if (isBigIntZero(otherNumber))
            throw new ArithmeticException();

        if (otherNumber.isNegative() && !isNegative()) {
            DSProject1 result = divide(new DSProject1(otherNumber.stringNumber));
            result.flipNegativity();
            return result;

        } else if (!otherNumber.isNegative() && isNegative()) {
            DSProject1 result = new DSProject1(stringNumber).divide(otherNumber);
            result.flipNegativity();
            return result;
        }

        int compareResult = this.compareTo(otherNumber);
        if (compareResult == 0)
            return new DSProject1("1");
        else if (compareResult < 0)
            return new DSProject1("0");

        DSProject1 result = new DSProject1("0");
        DSProject1 tempNumber = new DSProject1("0");

        while (tempNumber.compareTo(this) < 0) {
            tempNumber = tempNumber.plus(otherNumber);
            result = result.plus(new DSProject1("1"));
        }

        return result;

    }

    private boolean isBigIntZero(DSProject1 number) {
        return number.stringNumber.replace("0", "").equals("");

    }

    private DSProject1 MultiplyUnit(int majorUnits){

        String majorUnitsString = String.valueOf(majorUnits);
        String newNumber = majorUnitsString + stringNumber.substring(1);

        return new DSProject1(newNumber);
    }

    private void multiplyByTen() {
        this.numberDigits.add(0);
        stringNumber += '0';
    }

    @Override
    public int compareTo(DSProject1 other) {

        if (isNegative() && !other.isNegative())
             return -1;

        else if (!isNegative() && other.isNegative()){
            return 1;
        }

        else if (isNegative()){
            if (numberDigits.size() > other.numberDigits.size())
                return -1;
            else if (numberDigits.size() < other.numberDigits.size())
                return 1;

            else
                for (int index = 0; index < numberDigits.size(); index++) {

                    if (numberDigits.get(index) > other.numberDigits.get(index))
                        return -1;

                    else if (numberDigits.get(index) < other.numberDigits.get(index))
                            return 1;
                }

                return 0;
        }

        if (numberDigits.size() > other.numberDigits.size()) {
            return 1;
        }

        else if (numberDigits.size() < other.numberDigits.size())
            return -1;

        else
            for (int index = 0; index < numberDigits.size(); index++) {

                if (numberDigits.get(index) > other.numberDigits.get(index))
                    return 1;

                else if (numberDigits.get(index) < other.numberDigits.get(index))
                    return -1;
            }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null)
            return false;

        if (getClass() != o.getClass())
            return false;

        DSProject1 other = (DSProject1) o;
        
        return other.toString().equals(stringNumber);
    }

    @Override
    public String toString() {
        return stringNumber;
    }

 
}


    }
    

