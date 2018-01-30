package com.zfhy.egold.common.util;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.Objects;


public class DoubleUtil {

    
    private static final double EXP = 0.00001;

    public static String FORMAT_4 = "0.000";


    public static String toString(Double douVal) {
        if (Objects.isNull(douVal)) {
            return "0.00";
        }

        return new DecimalFormat("0.00").format(douVal);

    }

    public static String toScal4(Double douVal) {
        if (Objects.isNull(douVal)) {
            return "0.000";
        }

        return new DecimalFormat(FORMAT_4).format(douVal);

    }

    public static String toString(double douVal, String format) {

        return new DecimalFormat(format).format(douVal);

    }

    
    public static double setScaleDown(Double value, int num) {
        if (Objects.isNull(value)) {
            return 0D;
        }

        BigDecimal b = new BigDecimal(value);
        return b.setScale(num, BigDecimal.ROUND_DOWN).doubleValue();
    }



    
    public static double changeDecimal(Double value, int num) {
        if (Objects.isNull(value)) {
            return 0D;
        }
            
        BigDecimal b = new BigDecimal(value);
        double v = b.setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
        return v;
    }

    
    public static Double doubleAdd(Double a, Double b) {
        if (Objects.isNull(a)) {
            a = 0D;
        }
        if (Objects.isNull(b)) {
            b = 0D;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return b1.add(b2).doubleValue();
    }

    
    public static Double doubleAdd(Double a, Double b, int num) {
        if (Objects.isNull(a)) {
            a = 0D;
        }
        if (Objects.isNull(b)) {
            b = 0D;
        }

        return changeDecimal(doubleAdd(a, b), num);
    }

    
    public static Double doubleSub(Double a, Double b) {
        if (Objects.isNull(a)) {
            a = 0D;
        }
        if (Objects.isNull(b)) {
            b = 0D;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return b1.subtract(b2).doubleValue();
    }

    
    public static Double doubleSub(Double a, Double b, int num) {
        return changeDecimal(doubleSub(a, b), num);
    }

    
    public static Double doubleMul(Double a, Double b) {
        if (Objects.isNull(a)) {
            a = 0D;
        }
        if (Objects.isNull(b)) {
            b = 0D;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return b1.multiply(b2).doubleValue();
    }

    
    public static Double doubleMul(Double a, Double b, int num) {
        if (Objects.isNull(a)) {
            a = 0D;
        }
        if (Objects.isNull(b)) {
            b = 0D;
        }
        return changeDecimal(doubleMul(a, b), num);
    }

    
    public static Double doubleDiv(Double a, Double b, int scale) {
        if (Objects.isNull(a)) {
            a = 0D;
        }
        if (Objects.isNull(b)) {
            b = 0D;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return Double.valueOf(b1.divide(b2, scale, 4).doubleValue());
    }


    public static int compareTo(Double d1, Double d2){

        if (Math.abs(doubleSub(d1, d2)) < EXP) {
            
            return 0;
        }

        if (d1 > d2) {
            return 1;
        }

        if (d1 < d2) {
            return -1;
        }
        return 0;

    }

    public static boolean equal(Double d1, Double d2) {
        return compareTo(d1, d2) == 0;
    }

    public static boolean notEqual(Double d1, Double d2) {
        return compareTo(d1, d2) != 0;
    }


    public static void main(String[] args) {

        System.out.println(equal(1.010001, 1.01));
        System.out.println(LocalTime.now().getSecond());

        System.out.println(doubleMul(1.22, 2.33, 0).intValue());

        System.out.println(doubleDiv(12234.23D, 100D, 2));

        System.out.println(DoubleUtil.toString(33333.2354D));

        System.out.println(DoubleUtil.setScaleDown(1.099999, 3));

        System.out.println(DoubleUtil.graterOrEqual(1.2,2.1));


    }

    public static boolean graterOrEqual(Double d1, Double d2) {
        d1 = trimToZero(d1);
        d2 = trimToZero(d2);

        if (d1 < d2) {
            return false;
        }

        
        return Math.abs(d1 - d2) > 1;

    }

    public static boolean lessOrEqual(Double d1, Double d2) {
        d1 = trimToZero(d1);
        d2 = trimToZero(d2);

        if (d1 > d2) {
            return false;
        }

        
        return Math.abs(d1 - d2) > 1;

    }


    public static Double trimToZero(Double val) {
        return Objects.isNull(val) ? 0D : val;
    }
}
