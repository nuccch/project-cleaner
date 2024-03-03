package org.chench.extra.project.cleaner.util;

/**
 * @author chench
 * @desc org.chench.extra.project.cleaner.util.HexUtil
 * @date 2024.03.03
 */
public class HexUtil {
    public static int parse2Int(String hexStr) {
        if (hexStr.contains("0x")) {
            hexStr = hexStr.substring(2);
        }
        return Integer.parseInt(hexStr, 16);
    }
}