package cx.leo.rankup.utils;

import java.text.DecimalFormat;

public class FormatUtils {

    private static final DecimalFormat CURRENCY = new DecimalFormat("#,###.##");

    public static String currency(double number) {
        if (number % 1 != 0) return currency(number, 2, 2);
        else return currency(number, 0, 0);
    }

    public static String currency(double number, int minDigits, int maxDigits) {
        DecimalFormat format = CURRENCY;
        format.setMaximumFractionDigits(maxDigits);
        format.setMinimumFractionDigits(minDigits);
        return format.format(number);
    }

}
