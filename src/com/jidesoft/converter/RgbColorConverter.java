/*
 * @(#) RgbColorConverter.java
 *
 * Copyright 2002 - 2003 JIDE Software. All rights reserved.
 */
package com.jidesoft.converter;

import java.awt.*;
import java.util.StringTokenizer;

/**
 * Converts Color to/from "XXX, XXX, XXX" format. For example "0, 0, 0" is Color(0, 0, 0)
 * and "255, 0, 255" is Color(255, 0, 255).
 */
public class RgbColorConverter extends ColorConverter {

    /**
     * Creates a RgbColorConverter.
     */
    public RgbColorConverter() {
    }

    public String toString(Object object, ConverterContext context) {
        if (object instanceof Color) {
            Color color = (Color) object;
            StringBuffer colorText = new StringBuffer();
            colorText.append(color.getRed()).append(", ");
            colorText.append(color.getGreen()).append(", ");
            colorText.append(color.getBlue());
            return new String(colorText);
        }
        else {
            return "";
        }
    }

    public boolean supportToString(Object object, ConverterContext context) {
        return true;
    }

    public boolean supportFromString(String string, ConverterContext context) {
        return true;
    }

    public Object fromString(String string, ConverterContext context) {
        if (string == null || string.trim().length() == 0) {
            return null;
        }
        StringTokenizer token = new StringTokenizer(string, ",; ");
        int r = 0, g = 0, b = 0;
        if (token.hasMoreTokens()) {
            String s = token.nextToken();
            try {
                r = Integer.parseInt(s, 10) % 256;
            }
            catch (NumberFormatException e) {
            }
        }
        if (token.hasMoreTokens()) {
            String s = token.nextToken();
            try {
                g = Integer.parseInt(s, 10) % 256;
            }
            catch (NumberFormatException e) {
            }
        }
        if (token.hasMoreTokens()) {
            String s = token.nextToken();
            try {
                b = Integer.parseInt(s, 10) % 256;
            }
            catch (NumberFormatException e) {
            }
        }
        return new Color(r, g, b);
    }
}