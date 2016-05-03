package com.anashidgames.gerdoo.utils;

/**
 * Created by psycho on 3/17/16.
 */
public class PsychoUtils {

    public static final float COMPLETE_CIRCLE = 360;

    public static String fixUrl(String url){
        if (url == null)
            return null;

        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        return url;
    }

    public static String toPersianNumber(CharSequence s) {
        if (s == null)
            return null;

        String result = "";
        for(int i=0; i<s.length(); i++){
            if (s.charAt(i) == '0')
                result += "۰";
            else if (s.charAt(i) == '1')
                result += "۱";
            else if (s.charAt(i) == '2')
                result += "۲";
            else if (s.charAt(i) == '3')
                result += "۳";
            else if (s.charAt(i) == '4')
                result += "۴";
            else if (s.charAt(i) == '5')
                result += "۵";
            else if (s.charAt(i) == '6')
                result += "۶";
            else if (s.charAt(i) == '7')
                result += "۷";
            else if (s.charAt(i) == '8')
                result += "۸";
            else if (s.charAt(i) == '9')
                result += "۹";
            else
                result += s.charAt(i);
        }
        return result;
    }

    public static float getNormalAngle(float angle) {
        while (angle < 0) {
            angle += COMPLETE_CIRCLE;
        }

        while (angle >  COMPLETE_CIRCLE){
            angle -= COMPLETE_CIRCLE;
        }
        return angle;
    }

    public static boolean isOnLeft(float angle) {
        angle = PsychoUtils.getNormalAngle(angle);
        return angle > COMPLETE_CIRCLE/4 && angle < COMPLETE_CIRCLE*3/4;
    }

    public static boolean isOnBottom(float angle) {
        angle = PsychoUtils.getNormalAngle(angle);
        return angle > 0 && angle < COMPLETE_CIRCLE/2;
    }
}
