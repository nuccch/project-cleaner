package org.chench.extra.project.cleaner;

/**
 * @author chench
 * @desc org.chench.extra.project.cleaner.PatternTarget
 * @date 2024.03.02
 */
public class PatternTarget {
    private String name;
    private PatternTargetType type;

    public PatternTarget(String name, PatternTargetType type) {
        this.name = name;
        this.type = type;
    }

    public PatternTarget(String name, int type) {
        this.name = name;
        this.type = PatternTargetType.resolve(type);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PatternTargetType getType() {
        return type;
    }

    public void setType(PatternTargetType type) {
        this.type = type;
    }
}