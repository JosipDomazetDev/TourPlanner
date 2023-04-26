package com.example.tourplanner.ui.components.converter;

import javafx.util.StringConverter;


public class CustomDoubleStringConverter extends StringConverter<Double> {


    public Double fromString(String var1) {
        try {
            var1 = var1.trim();
            return var1.length() < 1 ? null : Double.valueOf(var1);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: " + var1);
            return null;
        }
    }

    public String toString(Double var1) {
        return var1 == null ? "" : Double.toString(var1);
    }
}
