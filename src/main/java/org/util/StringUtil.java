package org.util;

/**
 * Created by li on 17-7-27.
 */
public class StringUtil {

    public static boolean isBlankOrEmpty(String str) {
        if (str == null || CommonConst.BLANK.equals(str)) {
            return true;
        } else {
            return false;
        }
    }
}
