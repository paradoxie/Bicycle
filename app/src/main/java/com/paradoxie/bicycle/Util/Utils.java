package com.paradoxie.bicycle.Util;
/**
 * Created by xiehehe on 16/8/22.
 */

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: xiehehe
 * Date: 2016-08-22
 * Time: 14:00
 * FIXME
 */
public class Utils {
    public static boolean isShow = true;
    private static Toast mToast;


    /**
     * 编码验证:是否为磁力链
     *
     * @param s
     * @return
     */
    public static boolean isLink(String s) {
        if (s.contains("magnet:?")) {
            return true;
        }
        return false;
    }

    /**
     * 是否为伏羲码
     *
     * @param s
     * @return
     */
    public static boolean isIChina(String s) {
        if (s.contains("magnet:?")) {
            return false;
        } else {
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher matcher = p.matcher(s);
            boolean flg = false;
            if (matcher.find()) {
                flg = true;
            }
            return flg;
        }
    }

    /**
     * 是否为莫斯码
     *
     * @param s
     * @return
     */
    public static boolean isMorse(String s) {
        if (s.contains("magnet:?")) {
            return false;
        } else {
            if (s.contains(".") && s.contains("-")) {
                return true;
            }
            return false;
        }
    }

    /**
     * 是否为Base64
     *
     * @param s
     * @return
     */
    public static boolean isBase64(String s) {
        if (s.contains("magnet:?")) {
            return false;
        } else {
            if (s.length() % 4 == 0)
                return true;
            return false;
        }
    }

    /**
     * 是否是网址
     *
     * @param s
     * @return
     */
    public static boolean isNet(String s) {
        Pattern p = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)" +
                "(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$");
        Matcher m = p.matcher(s);
        boolean b = m.matches();
        return b;
    }

    /**
     * Toast显示优化
     * @param context
     * @param message
     */
    public static void showShort(Context context,CharSequence message) {

        if (isShow && message != null && !Utils.isStrNull(message + ""))
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(message);
            }
        mToast.show();
    }

    /**
     * 判断为空,可以empty替换??
     * @param str
     * @return
     */
    public static boolean isStrNull(String str) {
        if (null == str) {
            return true;
        } else if ("".equals(str.trim())) {
            return true;
        } else if ("null".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 分享功能
     * @param context
     * @param Title
     * @param Url
     */
    public static void share(Context context, String Title, String Url) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, Title + " " + Url + " 分享自Bycicle");
        context.startActivity(Intent.createChooser(share, "分享到"));
    }
}
