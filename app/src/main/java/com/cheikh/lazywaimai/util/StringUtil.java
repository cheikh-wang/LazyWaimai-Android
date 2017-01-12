package com.cheikh.lazywaimai.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat")
public class StringUtil {

    private static final String MAIL_REGEX = "([_A-Za-z0-9-]+)(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})";
    private static final String CELL_PHONE_REGEX = "(1)[0-9]{10}$";

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param value
     * @return
     */
    public static String friendlyMoney(float value) {
        String s = String.valueOf(value);
        if (s.indexOf(".") > 0) {
            // 去掉多余的0
            s = s.replaceAll("0+?$", "");
            // 如最后一位是.则去掉
            s = s.replaceAll("[.]$", "");
        }
        return s;
    }

    /**
     * 使用java正则表达式判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static String formatMoney(double value) {
        DecimalFormat format = new DecimalFormat("####.#");
        format.setRoundingMode(RoundingMode.HALF_UP);
        return "¥" + format.format(value);
    }

    public static String formatDiscount(float value) {
        return new DecimalFormat("#.#").format(value * 10) + "折";
    }


    public static float formatInteger(double value) {
        DecimalFormat format = new DecimalFormat("####");
        format.setRoundingMode(RoundingMode.HALF_UP);
        return Float.parseFloat(format.format(value));
    }

    /**
     * 将金额格式化为整形
     * @param value
     * @return
     */
    public static String formatIntegerMoney(double value) {
        DecimalFormat format = new DecimalFormat("####");
        format.setRoundingMode(RoundingMode.HALF_UP);
        return "¥" + format.format(value);
    }

    /**
     * 判断是否是邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return Pattern.matches(MAIL_REGEX, email);
    }

    /**
     * 判断是否是电话号码
     * @param phone
     * @return
     */
    public static boolean isCellPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return Pattern.matches(CELL_PHONE_REGEX, phone);
    }

    /**
     * 是否不为空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * 移除字符串两侧的空白字符
     * @param str
     * @return
     */
    public static String trim(String str) {
        if ((str != null) && (str.length() != 0)) {
            str = str.trim();
        }
        return str;
    }

    /**
     * 将字符数组转化为字符串
     * @param chars
     * @return
     */
    public static String charArray2String(char[] chars) {
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 将byte数组转化为十六进制
     * @param buffer
     * @return
     */
    public static String toHex(byte[] buffer) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);

        for (int i = 0; i < buffer.length; ++i) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }
        return sb.toString();
    }

    @SuppressWarnings("deprecation")
    public static void copyToClipboard(Context context, String copyText) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(copyText);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("label", copyText);
            clipboard.setPrimaryClip(clip);
        }
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String stringToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 将电话号码格式化为xxx-xxxx-xxxx格式
     *
     * @param input 原始电话号码
     * @return 格式化的电话号码
     */
    public static String formatPhoneNumber(@NonNull String input) {
        if (input.length() <= 3) {
            return input;
        } else if (input.length() > 3 && input.length() < 7) {
            return input.substring(0, 3) + "-"
                    + input.substring(3, input.length());
        } else if (input.length() >= 7) {
            return input.substring(0, 3) + "-" + input.substring(3, 7) + "-"
                    + input.substring(7, input.length());
        }
        return "";
    }

    /**
     * 比较字符串A和B的大小(float比较，B必须大于0)
     *
     * @param str1 A
     * @param str2 B
     * @return true(A>=B), or false(A<B or B<=0)
     */
    public static boolean compareA2B(String str1, String str2) {
        try {
            float s1 = Float.parseFloat(str1);
            float s2 = Float.parseFloat(str2);
            return s2 > 0 && s1 >= s2;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
