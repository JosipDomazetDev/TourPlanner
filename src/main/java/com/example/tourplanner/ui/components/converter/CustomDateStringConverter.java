package com.example.tourplanner.ui.components.converter;

import javafx.util.converter.DateTimeStringConverter;

import java.util.Date;

public class CustomDateStringConverter extends DateTimeStringConverter {
    public CustomDateStringConverter(String var1) {
        super(var1);
    }

    @Override
    public String toString(Date var1) {
        try {
            return super.toString(var1);
        } catch (RuntimeException ignored) {
            System.out.println("Invalid input: " + var1);
        }
        return null;
    }

    @Override
    public Date fromString(String var1) {
        try {
            return super.fromString(var1);
        } catch (RuntimeException ignored) {
            System.out.println("Invalid input: " + var1);
        }
        return null;
    }

}
