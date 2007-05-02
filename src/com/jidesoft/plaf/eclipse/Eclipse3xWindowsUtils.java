// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2006-4-29 10:33:49
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   EclipseWindowsUtils.java

package com.jidesoft.plaf.eclipse;

import com.jidesoft.swing.JideTabbedPane;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.InsetsUIResource;

/**
 * Utility Class for WindowsLookAndFeel to add Eclipse3x related LookAndFeel style
 */
public class Eclipse3xWindowsUtils extends EclipseWindowsUtils {

    /**
     * Initializes class defaults with menu components UIDefaults.
     *
     * @param table
     */
    public static void initClassDefaultsWithMenu(UIDefaults table) {
        EclipseWindowsUtils.initClassDefaultsWithMenu(table);
        initClassDefaults(table);
    }

    /**
     * Initializes class defaults.
     *
     * @param table
     */
    public static void initClassDefaults(UIDefaults table) {
        EclipseWindowsUtils.initClassDefaults(table);
        table.put("JideTabbedPaneUI", "com.jidesoft.plaf.eclipse.Eclipse3xJideTabbedPaneUI");
    }

    /**
     * Initializes components defaults.
     *
     * @param table
     */
    public static void initComponentDefaults(UIDefaults table) {
        initComponentDefaults(table);
        initComponentDefaultsForEclipse3x(table);
    }

    /**
     * Initializes components defaults with menu components UIDefaults.
     *
     * @param table
     */
    public static void initComponentDefaultsWithMenu(UIDefaults table) {
        EclipseWindowsUtils.initComponentDefaultsWithMenu(table);
        initComponentDefaultsForEclipse3x(table);
    }

    private static void initComponentDefaultsForEclipse3x(UIDefaults table) {
        Object uiDefaults[] = {
                "JideTabbedPane.defaultTabShape", new Integer(JideTabbedPane.SHAPE_ECLIPSE3X),
                "JideTabbedPane.defaultTabColorTheme", new Integer(JideTabbedPane.COLOR_THEME_WIN2K),
                "JideTabbedPane.defaultResizeMode", new Integer(JideTabbedPane.RESIZE_MODE_NONE),
                "JideTabbedPane.closeButtonMarginSize", new Integer(10),
                "JideTabbedPane.iconMarginHorizon", new Integer(8),
                "JideTabbedPane.iconMarginVertical", new Integer(6),

                "JideTabbedPane.border", new BorderUIResource(BorderFactory.createEmptyBorder(1, 1, 1, 1)),
                "JideTabbedPane.contentBorderInsets", new InsetsUIResource(2, 2, 2, 2),
        };

        table.putDefaults(uiDefaults);
    }
}