package ru.sks.util;

import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, ComparisonOperator> {
    @Override
    public ComparisonOperator convert(String source) {

        try {
            return ComparisonOperator.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}