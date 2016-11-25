package ru.simplgroupp.services.impl;

import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.util.DatesUtils;

import java.util.Date;

import static java.lang.Math.abs;

/**
 * Matching helper
 */
public final class MatchingHelper {

    private static final double EPSILON = 0.000001;

    public static boolean isMatch(final Object object1, final Object object2, final Double diff) {
        if (object1 == null || object2 == null) {
            return false;
        }
        
        if (diff==null){
        	return Utils.equalsNull(object1, object2);
        }
        
        if (object1 instanceof Number) {
            double number1 = ((Number) object1).doubleValue();
            double number2 = ((Number) object2).doubleValue();
            if (diff < EPSILON) {
                return abs(number1 - number2) < EPSILON;
            }

            double diffValue = abs(number1 - number2);
            double diffPercent = diffValue / number1;

            return diffPercent <= diff;
        }

        if (object1 instanceof Date) {
            Date date1 = (Date) object1;
            Date date2 = (Date) object2;
            int diffDays = DatesUtils.daysDiffAny(date2, date1);

            return abs(diffDays) <= diff;
        }

        return object1.equals(object2);
    }

    public static double diff(Object object1, Object object2) {
        if (object1==null||object2==null){
        	return 1;
        }

        if (object1 instanceof Number) {
            double number1 = ((Number) object1).doubleValue();
            double number2 = ((Number) object2).doubleValue();

            double diffValue = abs(number1 - number2);
            return diffValue / number1;
        }

        if (object1 instanceof Date) {
            Date date1 = (Date) object1;
            Date date2 = (Date) object2;
            int diffDays = DatesUtils.daysDiffAny(date2, date1);

            return abs(diffDays);
        }

        return object1.equals(object2) ? 0 : 1;
    }

    private MatchingHelper() {
    }
}
