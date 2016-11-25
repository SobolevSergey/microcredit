package ru.simplgroupp.toolkit;

import ru.simplgroupp.toolkit.common.Convertor;

/**
 * Методы для текстовых преобразований
 * Created by Cobalt <unger1984@gmail.com> on 03.11.15.
 */
public class TextUtil {

    /**
     * Выводит слово с окончанием в зависимости от числа. Например 1 день, 2 дня, 5 дней
     * @param number
     * @param word1
     * @param word2
     * @param word3
     * @return
     */
    public static String wordForm(int number, String word1, String word2, String word3){
        if(number>=10 && number<21) return word3;
        String num = Convertor.toString(number);
        int numend = Convertor.toInteger(num.substring(num.length()-1)).intValue();
        if(numend==1) return word1;
        if(numend<5) return word2;
        return word3;
    }

}
