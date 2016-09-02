package com.paradoxie.bicycle.Util;
/**
 * Created by xiehehe on 16/8/22.
 */

import android.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: xiehehe
 * Date: 2016-08-22
 * Time: 09:21
 * FIXME
 */
public class TransManager {

    public static Map<String, String> morseCode = new HashMap<>();
    public static Map<String, String> iChina = new HashMap<>();

    public TransManager() {
        initMorse();
        initIChina();
    }

    private void initIChina() {
        iChina.put("A", "坤");
        iChina.put("B", "剥");
        iChina.put("C", "比");
        iChina.put("D", "观");
        iChina.put("E", "豫");
        iChina.put("G", "萃");
        iChina.put("H", "否");
        iChina.put("I", "谦");
        iChina.put("J", "艮");
        iChina.put("K", "蹇");
        iChina.put("L", "渐");
        iChina.put("M", "小过");
        iChina.put("N", "旅");
        iChina.put("O", "咸");
        iChina.put("P", "遁");
        iChina.put("Q", "师");
        iChina.put("R", "蒙");
        iChina.put("S", "坎");
        iChina.put("T", "涣");
        iChina.put("U", "解");
        iChina.put("V", "未济");
        iChina.put("W", "困");
        iChina.put("X", "讼");
        iChina.put("Y", "升");
        iChina.put("Z", "蛊");
        iChina.put("a", "井");
        iChina.put("b", "巽");
        iChina.put("c", "恒");
        iChina.put("d", "鼎");
        iChina.put("e", "大过");
        iChina.put("f", "姤");
        iChina.put("g", "复");
        iChina.put("h", "颐");
        iChina.put("i", "屯");
        iChina.put("j", "益");
        iChina.put("k", "震");
        iChina.put("l", "噬嗑");
        iChina.put("m", "随");
        iChina.put("n", "无妄");
        iChina.put("o", "明夷");
        iChina.put("p", "贲");
        iChina.put("q", "既济");
        iChina.put("r", "家人");
        iChina.put("s", "丰");
        iChina.put("t", "离");
        iChina.put("u", "革");
        iChina.put("v", "同人");
        iChina.put("w", "临");
        iChina.put("x", "损");
        iChina.put("y", "节");
        iChina.put("z", "中孚");
        iChina.put("0", "归妹");
        iChina.put("1", "睽");
        iChina.put("2", "兑");
        iChina.put("3", "履");
        iChina.put("4", "泰");
        iChina.put("5", "大畜");
        iChina.put("6", "需");
        iChina.put("7", "小畜");
        iChina.put("8", "大壮");
        iChina.put("9", "大有");
        iChina.put(" ", "开");
        iChina.put("+", "夬");
        iChina.put("/", "乾");
        iChina.put(".", "死");
        iChina.put(",", "伤");
        iChina.put("?", "生");
        iChina.put(":", "休");
        iChina.put(";", "泽");
        iChina.put("=", "山");
        iChina.put("-", "雷");
        iChina.put("_", "风");
        iChina.put("\"", "火");
        iChina.put("$", "水");
        iChina.put("!", "地");
        iChina.put("@", "天");

    }

    private void initMorse() {
        morseCode.put("a", ".-");
        morseCode.put("b", "-...");
        morseCode.put("c", "-.-.");
        morseCode.put("d", "-..");
        morseCode.put("e", ".");
        morseCode.put("f", "..-.");
        morseCode.put("g", "--.");
        morseCode.put("h", "....");
        morseCode.put("i", "..");
        morseCode.put("j", ".---");
        morseCode.put("k", "-.-");
        morseCode.put("l", ".-..");
        morseCode.put("m", "--");
        morseCode.put("n", "-.");
        morseCode.put("o", "---");
        morseCode.put("p", ".--.");
        morseCode.put("q", "--.-");
        morseCode.put("r", ".-.");
        morseCode.put("s", "...");
        morseCode.put("t", "-");
        morseCode.put("u", "..-");
        morseCode.put("v", "...-");
        morseCode.put("w", ".--");
        morseCode.put("x", "-..-");
        morseCode.put("y", "-.--");
        morseCode.put("z", "--..");
        morseCode.put(" ", " ");

        morseCode.put("0", "-----");
        morseCode.put("1", ".----");
        morseCode.put("2", "..---");
        morseCode.put("3", "...--");
        morseCode.put("4", "....-");
        morseCode.put("5", ".....");
        morseCode.put("6", "-....");
        morseCode.put("7", "--...");
        morseCode.put("8", "---..");
        morseCode.put("9", "----.");

        morseCode.put(".", ".-.-.-");
        morseCode.put(",", "--..--");
        morseCode.put("?", "..--..");
        morseCode.put("/", "-..-.");
        morseCode.put(":", "---...");
        morseCode.put(";", "-.-.-.");
        morseCode.put("=", "-...-");
        morseCode.put("+", ".-.-.");
        morseCode.put("-", "-....-");
        morseCode.put("_", "..--.-");
        morseCode.put("\"", ".-..-.");
        morseCode.put("$", "...-..-");
        morseCode.put("!", "-.-.--");
        morseCode.put("@", ".--.-.");
    }

    /**
     * 明文转换为秘文
     *
     * @param s 传入需要转换的文本
     * @return 转换结果
     */
    public String codeTo(Map map, String s) {
        String response = "";
        String decodedChar = "";

        for (char ch : s.toCharArray()) {
            decodedChar = (String) map.get(Character.toString(ch));
            if (decodedChar != null) {
                response += decodedChar + " ";
            }
        }
        response = response.substring(0, response.length() - 1);
        return response;
    }

    /**
     * 转换为明文
     *
     * @param s 传入的秘文
     * @return 明文磁力链
     */
    public String ToCode(Map map, String s) {
        String res = "";
        String Char = "";
        String[] arrays = s.split(" ");
        for (int i = 0; i < arrays.length; i++) {
            Char = getKeyByValue(map, arrays[i]);
            if (Char != null) {
                res += Char;
            }
        }
        return res;
    }

    /**
     * 根据值获取键
     *
     * @param map   传入集合
     * @param value 传入值
     * @return
     */
    private String getKeyByValue(Map map, Object value) {
        String keys = "";
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object obj = entry.getValue();
            if (obj != null && obj.equals(value)) {
                keys = (String) entry.getKey();
            }
        }
        return keys;
    }



    /*************************************Base64加密解密********************************************
     * Base64编码
     *
     * @param input 要编码的字符串
     * @return Base64编码后的字符串
     */
    public static byte[] base64Encode(String input) {
        return base64Encode(input.getBytes());
    }

    /**
     * Base64编码
     *
     * @param input 要编码的字节数组
     * @return Base64编码后的字符串
     */
    public static byte[] base64Encode(byte[] input) {
        return Base64.encode(input, Base64.NO_WRAP);
    }

    /**
     * Base64编码
     *
     * @param input 要编码的字节数组
     * @return Base64编码后的字符串
     */
    public static String base64Encode2String(byte[] input) {
        return Base64.encodeToString(input, Base64.NO_WRAP);
    }

    /**
     * Base64解码
     *
     * @param input 要解码的字符串
     * @return Base64解码后的字符串
     */
    public static byte[] base64Decode(String input) {
        return Base64.decode(input, Base64.NO_WRAP);
    }

    /**
     * Base64解码
     * @param input 传入的byte数组
     * @return 解码完成后的字符串
     */
    public static String base64Decode2String(byte[] input) {
        String result =  new String(Base64.decode(input,Base64.NO_WRAP));
            return result;
    }
}
