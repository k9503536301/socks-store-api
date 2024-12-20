package ru.sks.util;

public enum ComparisonOperator {
    MORE_THAN("moreThan"),
    LESS_THAN("lessThan"),
    EQUAL("equal");

    private final String value;

    ComparisonOperator(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ComparisonOperator fromString(String text) {
        for (ComparisonOperator b : ComparisonOperator.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}