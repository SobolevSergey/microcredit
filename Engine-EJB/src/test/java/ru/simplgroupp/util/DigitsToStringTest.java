package ru.simplgroupp.util;

import junit.framework.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by rinat on 09.10.2015.
 */

public class DigitsToStringTest {
    @Test
    public void testUnits() {
        BigDecimal val = new BigDecimal(1);
        String result = DigitsToString.printString(val);
        Assert.assertEquals("одна целая, ноль тысячных", result);

        val = new BigDecimal(11);
        result = DigitsToString.printString(val);
        Assert.assertEquals("одиннадцать целых, ноль тысячных", result);

        val = new BigDecimal(21);
        result = DigitsToString.printString(val);
        Assert.assertEquals("двадцать одна целая, ноль тысячных", result);

        val = new BigDecimal("1111111");
        result = DigitsToString.printString(val);
        Assert.assertEquals("один миллион сто одиннадцать тысяч сто одиннадцать целых, ноль тысячных", result);

        val = new BigDecimal("1121121");
        result = DigitsToString.printString(val);
        Assert.assertEquals("один миллион сто двадцать одна тысяча сто двадцать одна целая, ноль тысячных", result);
    }

    @Test
    public void fracTest() {
        BigDecimal val = new BigDecimal("1.542");
        String result = DigitsToString.printString(val);
        Assert.assertEquals("одна целая, пятьсот сорок две тысячных", result);

        val = new BigDecimal(0.110);
        result = DigitsToString.printString(val);
        Assert.assertEquals("ноль целых, сто десять тысячных", result);

        val = new BigDecimal("1.000");
        result = DigitsToString.printString(val);
        Assert.assertEquals("одна целая, ноль тысячных", result);

        val = new BigDecimal("0.011");
        result = DigitsToString.printString(val);
        Assert.assertEquals("ноль целых, одиннадцать тысячных", result);

        val = new BigDecimal("111.021");
        result = DigitsToString.printString(val);
        Assert.assertEquals("сто одиннадцать целых, двадцать одна тысячная", result);

        val = new BigDecimal("4.02");
        result = DigitsToString.printString(val);
        Assert.assertEquals("четыре целых, двадцать тысячных", result);

        val = new BigDecimal("4.01");
        result = DigitsToString.printString(val);
        Assert.assertEquals("четыре целых, десять тысячных", result);

        val = new BigDecimal("4.1");
        result = DigitsToString.printString(val);
        Assert.assertEquals("четыре целых, сто тысячных", result);

        val = new BigDecimal("4.9");
        result = DigitsToString.printString(val);
        Assert.assertEquals("четыре целых, девятьсот тысячных", result);

        val = new BigDecimal(113.100);
        result = DigitsToString.printString(val);
        Assert.assertEquals("сто тринадцать целых, сто тысячных", result);

        val = new BigDecimal(113.040);
        result = DigitsToString.printString(val);
        Assert.assertEquals("сто тринадцать целых, сорок тысячных", result);

        val = new BigDecimal(113.045);
        result = DigitsToString.printString(val);
        Assert.assertEquals("сто тринадцать целых, сорок пять тысячных", result);

        val = new BigDecimal(113.001);
        result = DigitsToString.printString(val);
        Assert.assertEquals("сто тринадцать целых, одна тысячная", result);

        val = new BigDecimal(1440.0);
        result = DigitsToString.printString(val);
        Assert.assertEquals("одна тысяча четыреста сорок целых, ноль тысячных", result);
    }

    @Test
    public void notNullTest() {
        BigDecimal val = new BigDecimal(1);
        String result = DigitsToString.printString(val);
        Assert.assertNotNull(result);

        val = new BigDecimal("1");
        result = DigitsToString.printString(val);
        Assert.assertNotNull(result);
    }
}