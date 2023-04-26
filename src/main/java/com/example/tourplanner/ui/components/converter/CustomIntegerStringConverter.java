package com.example.tourplanner.ui.components.converter;

import javafx.util.StringConverter;

public class CustomIntegerStringConverter extends StringConverter<Integer> {
    int upperLimit;
    int lowerLimit;

    public CustomIntegerStringConverter(int lowerLimit,int upperLimit) {
        super();
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

    @Override
    public Integer fromString(String value) {
        try {
            value = value.trim();
            Integer integer = value.length() < 1 ? null : Integer.valueOf(value);

            if (integer != null && integer > upperLimit) {
                integer = upperLimit;
            }

            if (integer != null && integer < lowerLimit) {
                integer = lowerLimit;
            }

            return integer;
        } catch (NumberFormatException e) {
            // handle invalid input, e.g. show an error message
            System.out.println("Invalid input: " + value);
            return null;
        }
    }

    @Override
    public String toString(Integer value) {
        return value == null ? "" : Integer.toString(value);
    }

}
