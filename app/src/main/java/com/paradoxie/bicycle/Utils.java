package com.paradoxie.bicycle;
/**
 * Created by xiehehe on 16/8/22.
 */

import android.content.Context;
import android.content.Intent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: xiehehe
 * Date: 2016-08-22
 * Time: 14:00
 * FIXME
 */
public class Utils {

    public static boolean isLink(String s) {
        if (s.contains("magnet:?")) {
            return true;
        }
        return false;
    }

    public static boolean isIChina(String s) {
        Pattern p = Pattern.compile("^[\\u3400-\\u9FFF]+$");
        Matcher m = p.matcher(s);
        boolean b = m.matches();
        return b;
    }

    /**
     * 是否为莫斯码
     *
     * @param s
     * @return
     */
    public static boolean isMorse(String s) {

        if (s.contains(".") || s.contains("-")) {
            return true;
        }
        return false;
    }

    public static boolean isBase64(String s) {
        Pattern p = Pattern.compile("^[A-ZA-Z0-9 + /] {3} =）$");
        Matcher m = p.matcher(s);
        boolean b = m.matches();
        return b;
    }

    public static boolean isNet(String s) {
        Pattern p = Pattern.compile("[a-zA-z]+://[^\\s]*");
        Matcher m = p.matcher(s);
        boolean b = m.matches();
        return b;
    }

    //分享
    public static void share(Context context, String Title, String Url) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        //noinspection deprecation
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT,Title + " " + Url + " 分享自知乎网");
        context.startActivity(Intent.createChooser(share, "分享到"));
    }
}
