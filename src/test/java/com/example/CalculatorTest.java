package com.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorTest {

    @Test
    public void testAddition() {
        Calculator calculator = new Calculator();
        int result = calculator.add(3, 4);
        assertEquals(7, result);
    }

    @Test
    public void testSubtraction() {
        Calculator calculator = new Calculator();
        int result = calculator.subtract(5, 3);
        assertEquals(2, result);
    }

    @Test
    public void testMultiplication() {
        Calculator calculator = new Calculator();
        int result = calculator.multiply(2, 6);
        assertEquals(12, result);
    }

    @Test
    public void testDivision() {
        Calculator calculator = new Calculator();
        int result = calculator.divide(10, 2);
        assertEquals(5, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivisionByZero() {
        Calculator calculator = new Calculator();
        calculator.divide(10, 0);
    }
}
