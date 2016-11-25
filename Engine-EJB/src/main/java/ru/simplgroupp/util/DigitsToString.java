package ru.simplgroupp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by rinat on 07.10.2015.
 */

/**
 * Класс переводит число в прописную форму
 *
 * new DigitsToString().printString("1123456.582");
 */
public class DigitsToString {
    static String[] nums1 = {"", "один ", "два ", "три ", "четыре ", "пять ", "шесть ", "семь ", "восемь ", "девять "};
    static String[] nums2 = {"", "десять ", "двадцать ", "тридцать ", "сорок ", "пятьдесят ", "шестьдесят ", "семьдесят ", "восемьдесят ", "девяносто "};
    static String[] nums3 = {"", "сто ", "двести ", "триста ", "четыреста ", "пятьсот ", "шестьсот ", "семьсот ", "восемьсот ", "девятьсот "};
    static String[] nums4 = {"", "одна ", "две ", "три ", "четыре ", "пять ", "шесть ", "семь ", "восемь ", "девять "};
    static String[] nums5 = {"десять ", "одиннадцать ", "двенадцать ", "тринадцать ", "четырнадцать ", "пятнадцать ", "шестнадцать ", "семнадцать ", "восемнадцать ", "девятнадцать "};

    /**
     * @param digits
     * @return
     */
    public static String printString(BigDecimal digits) {
        String hundredMillionStr = "";
        String hundredsOfThousandsStr = "";
        String hundredthsStr = "";
        String fractionStr = "";

        // тысячные
        Integer units = (int) (digits.doubleValue() % Math.pow(10, 1) / Math.pow(10, 1 - 1));
        Integer tens = (int) (digits.doubleValue() % Math.pow(10, 2) / Math.pow(10, 2 - 1));
        Integer hundredths = (int) (digits.doubleValue() % Math.pow(10, 3) / Math.pow(10, 3 - 1));

        // сотни тысяч
        Integer thousandths = (int) (digits.doubleValue() % Math.pow(10, 4) / Math.pow(10, 4 - 1));
        Integer tenThousandth = (int) (digits.doubleValue() % Math.pow(10, 5) / Math.pow(10, 5 - 1));
        Integer hundredsOfThousands = (int) (digits.doubleValue() % Math.pow(10, 6) / Math.pow(10, 6 - 1));

        // миллионы
        Integer millions = (int) (digits.doubleValue() % Math.pow(10, 7) / Math.pow(10, 7 - 1));
        Integer tenMillionth = (int) (digits.doubleValue() % Math.pow(10, 8) / Math.pow(10, 8 - 1));
        Integer hundredMillion = (int) (digits.doubleValue() % Math.pow(10, 9) / Math.pow(10, 9 - 1));

        hundredMillionStr = nums3[hundredMillion];
        hundredMillionStr += tenMillionth == 1 && millions != 0 ? nums5[millions] : nums2[tenMillionth];
        hundredMillionStr += tenMillionth == 1 ? "" : nums1[millions];

        if (millions == 0 || millions >= 5 && millions <= 9 || tenMillionth == 1) {
            if (hundredMillion != 0 || tenMillionth != 0 || millions != 0) {
                hundredMillionStr += "миллионов ";
            }
        } else if (millions == 1) {
            hundredMillionStr += "миллион ";
        } else if (millions >= 2 && millions <= 4) {
            hundredMillionStr += "миллиона ";
        }

        hundredsOfThousandsStr = nums3[hundredsOfThousands];
        hundredsOfThousandsStr += tenThousandth == 1 && tenThousandth != 0 ? nums5[thousandths] : nums2[tenThousandth];
        hundredsOfThousandsStr += tenThousandth == 1 ? "" : nums4[thousandths];

        if (thousandths == 0 || thousandths >= 5 && thousandths <= 9 || tenThousandth == 1) {
            if (hundredsOfThousands != 0 || tenThousandth != 0 || thousandths != 0) {
                hundredsOfThousandsStr += "тысяч ";
            }
        } else if (thousandths == 1) {
            hundredsOfThousandsStr += "тысяча ";
        } else if (thousandths >= 2 && thousandths <= 4) {
            hundredsOfThousandsStr += "тысячи ";
        }

        hundredthsStr = nums3[hundredths];
        hundredthsStr += tens == 1 ? nums5[units] : nums2[tens];
        hundredthsStr += tens == 1 ? "" : nums4[units];

        if (units == 0 && tens == 0 && hundredths == 0 && thousandths == 0 && tenThousandth == 0 &&
                hundredsOfThousands == 0 && millions == 0 && tenMillionth == 0 && hundredMillion == 0) {
            hundredthsStr += "ноль ";
        }

        // проверяем есть ли дробная 1.000 - нули так же учитываются
        digits = digits.setScale(3, RoundingMode.HALF_UP);
        String[] splitDigits = digits.toString().split("\\D");
        if (splitDigits.length > 1) {
            if (units == 1 && tens != 1) {
                fractionStr = "целая, ";
            } else {
                fractionStr = "целых, ";
            }

            Integer len = splitDigits[splitDigits.length - 1].length();
            Integer val = digits.subtract(new BigDecimal(digits.intValue())).multiply(new BigDecimal(Math.pow(10, len))).intValue();

            Integer fracUnits = (int) (val % Math.pow(10, 1) / Math.pow(10, 1 - 1));
            Integer fracTens = (int) (val % Math.pow(10, 2) / Math.pow(10, 2 - 1));
            Integer fracHundredths = (int) (val % Math.pow(10, 3) / Math.pow(10, 3 - 1));

            fractionStr += nums3[fracHundredths];
            fractionStr += fracTens == 1 ? nums5[fracUnits] : nums2[fracTens];
            fractionStr += fracTens == 1 ? "" : nums4[fracUnits];

            if (fracUnits == 0 && fracTens == 0 && fracHundredths == 0) {
                fractionStr += "ноль ";
            }
            if (fracUnits == 1 && fracTens != 1) {
                fractionStr += "тысячная ";
            } else {
                fractionStr += "тысячных ";
            }
        }

        return (hundredMillionStr + hundredsOfThousandsStr + hundredthsStr + fractionStr).trim();
    }

    /**
     * Перегрузка метода возвращает число прописью
     *
     * @param digits число
     * @return число прописью
     */
    public static String printString(String digits) {
        return printString(new BigDecimal(digits));
    }
}