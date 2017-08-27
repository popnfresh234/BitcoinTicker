package com.dmtaiwan.alexander.bitcointicker.utility;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Alexander on 8/26/2017.
 */

public class Utils {

    public static String getDate(long time) {
        Date date = new Date(time * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss a");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Vancouver"));
        String formattedTime = sdf.format(date);
        return formattedTime;
    }

    public static String formatCurrency(String input) {
        if (input == null) {
            return "";
        }
        float floatPrice = Float.parseFloat(input);
        return DecimalFormat.getCurrencyInstance().format(floatPrice);
    }

    public static String formatCurrencyVolumeOrCap(String input) {
        if (input == null) {
            return "";
        }
        float floatPrice = Float.parseFloat(input);
        String formattedPrice = DecimalFormat.getCurrencyInstance().format(floatPrice);
        return formattedPrice.replaceAll("\\.0*$", "");
    }

    public static String formatPercentage(String input) {
        if (input == null) {
            return "";
        }
        float floatPercentage = Float.parseFloat(input);
        DecimalFormat df2;
        if (floatPercentage > 0) {
            df2 = new DecimalFormat("+#,##0.00 '%'");
        } else {
            df2 = new DecimalFormat(" #,##0.00 '%'");
        }


        return df2.format(floatPercentage);
    }

    public static String formatSupply(String input) {
        if (input == null) {
            return "";
        }
        float floatSupply = Float.parseFloat(input);
        DecimalFormat decimalFormat = new DecimalFormat("#");
        return decimalFormat.format(floatSupply);
    }
}
