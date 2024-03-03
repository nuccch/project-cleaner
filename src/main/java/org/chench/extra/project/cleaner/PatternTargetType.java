package org.chench.extra.project.cleaner;

/**
 * @author chench
 * @desc org.chench.extra.project.cleaner.PatternTargetType
 * @date 2024.03.02
 */
public enum PatternTargetType {
    /** 文件 */
    FILE(0x02),
    /** 目录 */
    DIRECTORY(0x04);

    private int type;
    PatternTargetType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static PatternTargetType resolve(int type) {
        PatternTargetType[] types = PatternTargetType.values();
        for (PatternTargetType t : types) {
            if (t.getType() == type) {
                return t;
            }
        }
        return null;
    }
}