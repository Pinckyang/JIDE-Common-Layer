/*
 * @(#)XertoWindowsUtils.java 6/13/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */
package com.jidesoft.plaf.xerto;

import com.jidesoft.icons.IconsFactory;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.ExtWindowsDesktopProperty;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.WindowsDesktopProperty;
import com.jidesoft.plaf.basic.BasicPainter;
import com.jidesoft.plaf.basic.BasicRangeSliderUI;
import com.jidesoft.plaf.basic.Painter;
import com.jidesoft.plaf.office2003.Office2003WindowsUtils;
import com.jidesoft.plaf.vsnet.ConvertListener;
import com.jidesoft.plaf.vsnet.HeaderCellBorder;
import com.jidesoft.plaf.vsnet.ResizeFrameBorder;
import com.jidesoft.plaf.vsnet.VsnetLookAndFeelExtension;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.utils.SecurityUtils;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;

/**
 * Initialize the uiClassID to BasicComponentUI mapping for JIDE components using Xerto style for WindowsLookAndFeel.
 * Xerto Style is designed by Xerto at http://www.xerto.com.
 */
public class XertoWindowsUtils extends Office2003WindowsUtils {

    /**
     * Initializes class defaults with menu components UIDefaults.
     *
     * @param table
     */
    public static void initClassDefaultsWithMenu(UIDefaults table) {
        VsnetLookAndFeelExtension.initClassDefaultsWithMenu(table);
        initClassDefaults(table);
    }

    /**
     * Initializes class defaults with menu components UIDefaults.
     *
     * @param table
     */
    public static void initClassDefaults(UIDefaults table) {
        Office2003WindowsUtils.initClassDefaults(table, false);
        initClassDefaultsForXerto(table);
    }

    private static void initClassDefaultsForXerto(UIDefaults table) {
        int products = LookAndFeelFactory.getProductsUsed();

        final String xertoPackageName = "com.jidesoft.plaf.xerto.";

        if ((products & PRODUCT_COMPONENTS) != 0) {
            table.put("CollapsiblePaneUI", xertoPackageName + "XertoCollapsiblePaneUI");
        }

        if ((products & PRODUCT_DOCK) != 0) {
            table.put("SidePaneUI", xertoPackageName + "XertoSidePaneUI");
            table.put("DockableFrameUI", xertoPackageName + "XertoDockableFrameUI");
        }
    }

    /**
     * Initializes components defaults.
     *
     * @param table
     */
    public static void initComponentDefaultsWithMenu(UIDefaults table) {
        /// always want shading
        System.setProperty("shadingtheme", "true");

        Toolkit toolkit = Toolkit.getDefaultToolkit();

        WindowsDesktopProperty defaultHighlightColor = new WindowsDesktopProperty("win.3d.lightColor", table.get("controlHighlight"), toolkit);
        WindowsDesktopProperty selectionBackgroundColor = new WindowsDesktopProperty("win.item.highlightColor", table.get("controlShadow"), toolkit);
        WindowsDesktopProperty menuTextColor = new WindowsDesktopProperty("win.menu.textColor", table.get("control"), toolkit);

        Object menuFont = JideSwingUtilities.getMenuFont(toolkit, table);

        Object menuSelectionBackground = new ExtWindowsDesktopProperty(//Actual color 182, 189, 210
                new String[]{"win.item.highlightColor"}, new Object[]{table.get("controlShadow")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new ColorUIResource(XertoUtils.getMenuSelectionColor((Color) obj[0]));
            }
        });

        Object menuBackground = new ExtWindowsDesktopProperty(//Actual color 249, 248, 247
                new String[]{"win.3d.backgroundColor"}, new Object[]{table.get("control")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new ColorUIResource(XertoUtils.getMenuBackgroundColor((Color) obj[0]));
            }
        });

        Object separatorColor = new ExtWindowsDesktopProperty(// Not exactly right
                new String[]{"win.3d.shadowColor"}, new Object[]{table.get("controlShadow")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new ColorUIResource(((Color) obj[0]).brighter());
            }
        });

        Object[] uiDefaults = {
                "PopupMenuSeparator.foreground", separatorColor,
                "PopupMenuSeparator.background", menuBackground,

                "CheckBoxMenuItem.checkIcon", JideIconsFactory.getImageIcon(JideIconsFactory.MENU_CHECKBOX_VSNET),
                "CheckBoxMenuItem.selectionBackground", menuSelectionBackground,
                "CheckBoxMenuItem.selectionForeground", menuTextColor,
                "CheckBoxMenuItem.acceleratorSelectionForeground", menuTextColor,
                "CheckBoxMenuItem.mouseHoverBackground", menuSelectionBackground,
                "CheckBoxMenuItem.mouseHoverBorder", new BorderUIResource(BorderFactory.createLineBorder(new Color(10, 36, 106))),
                "CheckBoxMenuItem.margin", new InsetsUIResource(3, 0, 3, 0),
                "CheckBoxMenuItem.font", menuFont,
                "CheckBoxMenuItem.acceleratorFont", menuFont,
                "CheckBoxMenuItem.textIconGap", new Integer(8),

                "RadioButtonMenuItem.checkIcon", JideIconsFactory.getImageIcon(JideIconsFactory.MENU_CHECKBOX_VSNET),
                "RadioButtonMenuItem.selectionBackground", menuSelectionBackground,
                "RadioButtonMenuItem.selectionForeground", menuTextColor,
                "RadioButtonMenuItem.acceleratorSelectionForeground", menuTextColor,
                "RadioButtonMenuItem.mouseHoverBackground", menuSelectionBackground,
                "RadioButtonMenuItem.mouseHoverBorder", new BorderUIResource(BorderFactory.createLineBorder(new Color(10, 36, 106))),
                "RadioButtonMenuItem.margin", new InsetsUIResource(3, 0, 3, 0),
                "RadioButtonMenuItem.font", menuFont,
                "RadioButtonMenuItem.acceleratorFont", menuFont,
                "RadioButtonMenuItem.textIconGap", new Integer(8),

                "MenuBar.border", new BorderUIResource(BorderFactory.createEmptyBorder(2, 2, 2, 2)),
//            "MenuBar.border", new BorderUIResource(BorderFactory.createCompoundBorder(
//                    new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.SOUTH),
//                    BorderFactory.createEmptyBorder(2, 2, 2, 2))),

                "Menu.selectionBackground", menuSelectionBackground,
                "Menu.selectionForeground", menuTextColor,
                "Menu.mouseHoverBackground", menuSelectionBackground,
                "Menu.mouseHoverBorder", new BorderUIResource(BorderFactory.createLineBorder(new Color(10, 36, 106))),
                "Menu.margin", new InsetsUIResource(2, 7, 1, 7),
                "Menu.checkIcon", JideIconsFactory.getImageIcon(JideIconsFactory.MENU_CHECKBOX_VSNET),
                "Menu.textIconGap", new Integer(2),
                "Menu.font", menuFont,
                "Menu.acceleratorFont", menuFont,
                "Menu.submenuPopupOffsetX", new Integer(0),
                "Menu.submenuPopupOffsetY", new Integer(0),

                "PopupMenu.border", new BorderUIResource(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(102, 102, 102)), BorderFactory.createEmptyBorder(1, 1, 1, 1))),

                "MenuItem.checkIcon", JideIconsFactory.getImageIcon(JideIconsFactory.MENU_CHECKBOX_VSNET),
                "MenuItem.selectionBackground", menuSelectionBackground,
                "MenuItem.selectionForeground", menuTextColor,
                "MenuItem.acceleratorSelectionForeground", menuTextColor,
                "MenuItem.background", menuBackground,
                "MenuItem.selectionBorderColor", selectionBackgroundColor,
                "MenuItem.shadowWidth", new Integer(24),
                "MenuItem.shadowColor", defaultHighlightColor, // TODO: not exactly. The actual one a little bit brighter than it
                "MenuItem.textIconGap", new Integer(8),
                "MenuItem.accelEndGap", new Integer(18),
                "MenuItem.margin", new InsetsUIResource(4, 0, 3, 0),
                "MenuItem.font", menuFont,
                "MenuItem.acceleratorFont", menuFont
        };
        table.putDefaults(uiDefaults);
        initComponentDefaults(table);

        table.put("Theme.painter", XertoPainter.getInstance());

        // since it used BasicPainter, make sure it is after Theme.Painter is set first.
        Object popupMenuBorder = new ExtWindowsDesktopProperty(new String[]{"null"}, new Object[]{((BasicPainter) table.get("Theme.painter")).getMenuItemBorderColor()}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new BorderUIResource(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder((Color) obj[0]), BorderFactory.createEmptyBorder(1, 1, 1, 1)));
            }
        });
        table.put("PopupMenu.border", popupMenuBorder);
    }

    /**
     * Initializes components defaults with menu components UIDefaults.
     *
     * @param table
     */
    public static void initComponentDefaults(UIDefaults table) {
        /// always want shading
        System.setProperty("shadingtheme", "true");

        Toolkit toolkit = Toolkit.getDefaultToolkit();

        WindowsDesktopProperty defaultHighlightColor = new WindowsDesktopProperty("win.3d.lightColor", table.get("controlHighlight"), toolkit);
        WindowsDesktopProperty defaultLtHighlightColor = new WindowsDesktopProperty("win.3d.highlightColor", table.get("controlLtHighlight"), toolkit);
        WindowsDesktopProperty selectionBackgroundColor = new WindowsDesktopProperty("win.item.highlightColor", table.get("controlShadow"), toolkit);
        WindowsDesktopProperty mdiBackgroundColor = new WindowsDesktopProperty("win.mdi.backgroundColor", table.get("controlShadow"), toolkit);
        WindowsDesktopProperty menuTextColor = new WindowsDesktopProperty("win.menu.textColor", table.get("controlText"), toolkit);
        WindowsDesktopProperty defaultTextColor = new WindowsDesktopProperty("win.button.textColor", table.get("controlText"), toolkit);
        WindowsDesktopProperty defaultBackgroundColor = new WindowsDesktopProperty("win.3d.backgroundColor", table.get("control"), toolkit);
        WindowsDesktopProperty defaultShadowColor = new WindowsDesktopProperty("win.3d.shadowColor", table.get("controlShadow"), toolkit);
        WindowsDesktopProperty defaultDarkShadowColor = new WindowsDesktopProperty("win.3d.darkShadowColor", table.get("controlDkShadow"), toolkit);
        WindowsDesktopProperty activeTitleBackgroundColor = new WindowsDesktopProperty("win.frame.activeCaptionColor", table.get("activeCaption"), toolkit);
        WindowsDesktopProperty activeTitleTextColor = new WindowsDesktopProperty("win.frame.captionTextColor", table.get("activeCaptionText"), toolkit);

        Object singleLineBorder = new ExtWindowsDesktopProperty(new String[]{"win.3d.shadowColor"}, new Object[]{table.get("controlShadow")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new BorderUIResource(BorderFactory.createLineBorder((Color) obj[0]));
            }
        });

        Object controlFont = JideSwingUtilities.getControlFont(toolkit, table);
        Object toolbarFont = JideSwingUtilities.getMenuFont(toolkit, table);
        Object boldFont = JideSwingUtilities.getBoldFont(toolkit, table);

        Object resizeBorder = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new XertoFrameBorder(new Insets(4, 4, 4, 4));
            }
        });


        Object defaultFormBackground = new ExtWindowsDesktopProperty(new String[]{"win.3d.backgroundColor"}, new Object[]{table.get("control")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new ColorUIResource(XertoUtils.getDefaultBackgroundColor((Color) obj[0]));
            }
        });

        Object inactiveTabForground = new ExtWindowsDesktopProperty(// Not exactly right
                new String[]{"win.3d.shadowColor"}, new Object[]{table.get("controlShadow")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new ColorUIResource(((Color) obj[0]).darker());
            }
        });

        Object focusedButtonColor = new ExtWindowsDesktopProperty(new String[]{"win.item.highlightColor"}, new Object[]{table.get("textHighlight")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new ColorUIResource(XertoUtils.getFocusedButtonColor((Color) obj[0]));
            }
        });

        Object selectedAndFocusedButtonColor = new ExtWindowsDesktopProperty(new String[]{"win.item.highlightColor"}, new Object[]{table.get("textHighlight")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new ColorUIResource(XertoUtils.getSelectedAndFocusedButtonColor((Color) obj[0]));
            }
        });

        Object selectedButtonColor = new ExtWindowsDesktopProperty(new String[]{"win.item.highlightColor"}, new Object[]{table.get("textHighlight")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new ColorUIResource(XertoUtils.getSelectedButtonColor((Color) obj[0]));
            }
        });


        Object gripperForeground = new ExtWindowsDesktopProperty(new String[]{"win.3d.backgroundColor"}, new Object[]{table.get("control")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new ColorUIResource(XertoUtils.getGripperForegroundColor((Color) obj[0]));
            }
        });

        Object commandBarBackground = new ExtWindowsDesktopProperty(new String[]{"win.3d.backgroundColor"}, new Object[]{table.get("control")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new ColorUIResource(XertoUtils.getToolBarBackgroundColor((Color) obj[0]));
            }
        });

        Painter gripperPainter = new Painter() {
            public void paint(JComponent c, Graphics g, Rectangle rect, int orientation, int state) {
                XertoPainter.getInstance().paintGripper(c, g, rect, orientation, state);
            }
        };

        Object buttonBorder = new BasicBorders.MarginBorder();

        ImageIcon sliderHorizontalImage = IconsFactory.getImageIcon(BasicRangeSliderUI.class, "icons/slider_horizontal.gif");
        ImageIcon sliderVerticalalImage = IconsFactory.getImageIcon(BasicRangeSliderUI.class, "icons/slider_vertical.gif");

        Object[] uiDefaults = new Object[]{
                "JideScrollPane.border", singleLineBorder,

                "JideButton.selectedAndFocusedBackground", selectedAndFocusedButtonColor,
                "JideButton.focusedBackground", focusedButtonColor,
                "JideButton.selectedBackground", selectedButtonColor,
                "JideButton.borderColor", selectionBackgroundColor,

                "JideButton.font", controlFont,
                "JideButton.background", defaultBackgroundColor,
                "JideButton.foreground", defaultTextColor,
                "JideButton.shadow", defaultShadowColor,
                "JideButton.darkShadow", defaultDarkShadowColor,
                "JideButton.light", defaultHighlightColor,
                "JideButton.highlight", defaultLtHighlightColor,
                "JideButton.border", buttonBorder,
                "JideButton.margin", new InsetsUIResource(3, 3, 3, 3),
                "JideButton.textIconGap", new Integer(2),
                "JideButton.textShiftOffset", new Integer(0),
                "JideButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{
                "SPACE", "pressed",
                "released SPACE", "released",
                "ENTER", "pressed",
                "released ENTER", "released"   // no last two for metal
        }),

                "JideSplitPane.dividerSize", new Integer(3),
                "JideSplitPaneDivider.border", new BorderUIResource(BorderFactory.createEmptyBorder()),
                "JideSplitPaneDivider.background", defaultBackgroundColor,
                "JideSplitPaneDivider.gripperPainter", gripperPainter,

                "JideTabbedPane.defaultTabShape", new Integer(JideTabbedPane.SHAPE_ROUNDED_VSNET),
                "JideTabbedPane.defaultResizeMode", new Integer(JideTabbedPane.RESIZE_MODE_NONE),
                "JideTabbedPane.defaultTabColorTheme", new Integer(JideTabbedPane.COLOR_THEME_OFFICE2003),

                "JideTabbedPane.tabRectPadding", new Integer(2),
                "JideTabbedPane.closeButtonMarginHorizonal", new Integer(3),
                "JideTabbedPane.closeButtonMarginVertical", new Integer(3),
                "JideTabbedPane.textMarginVertical", new Integer(4),
                "JideTabbedPane.noIconMargin", new Integer(2),
                "JideTabbedPane.iconMargin", new Integer(5),
                "JideTabbedPane.textPadding", new Integer(6),
                "JideTabbedPane.buttonSize", new Integer(18),
                "JideTabbedPane.buttonMargin", new Integer(5),
                "JideTabbedPane.fitStyleBoundSize", new Integer(8),
                "JideTabbedPane.fitStyleFirstTabMargin", new Integer(4),
                "JideTabbedPane.fitStyleIconMinWidth", new Integer(24),
                "JideTabbedPane.fitStyleTextMinWidth", new Integer(16),
                "JideTabbedPane.compressedStyleNoIconRectSize", new Integer(24),
                "JideTabbedPane.compressedStyleIconMargin", new Integer(12),
                "JideTabbedPane.compressedStyleCloseButtonMarginHorizontal", new Integer(0),
                "JideTabbedPane.compressedStyleCloseButtonMarginVertical", new Integer(0),
                "JideTabbedPane.fixedStyleRectSize", new Integer(60),
                "JideTabbedPane.closeButtonMargin", new Integer(2),
                "JideTabbedPane.gripLeftMargin", new Integer(4),
                "JideTabbedPane.closeButtonMarginSize", new Integer(6),
                "JideTabbedPane.closeButtonLeftMargin", new Integer(2),
                "JideTabbedPane.closeButtonRightMargin", new Integer(2),

                "JideTabbedPane.defaultTabBorderShadowColor", new ColorUIResource(115, 109, 99),

                "JideTabbedPane.gripperPainter", gripperPainter,
                "JideTabbedPane.border", new BorderUIResource(BorderFactory.createEmptyBorder(0, 0, 0, 0)),
                "JideTabbedPane.background", new ColorUIResource(XertoUtils.getControlColor()),
                "JideTabbedPane.foreground", new ColorUIResource(XertoUtils.getTabForgroundColor()),
                "JideTabbedPane.light", defaultHighlightColor,
                "JideTabbedPane.highlight", defaultLtHighlightColor,
                "JideTabbedPane.shadow", defaultShadowColor,
                "JideTabbedPane.darkShadow", new ColorUIResource(Color.GRAY),
                "JideTabbedPane.tabInsets", new InsetsUIResource(1, 4, 1, 4),
                "JideTabbedPane.contentBorderInsets", new InsetsUIResource(1, 1, 1, 1),
                "JideTabbedPane.ignoreContentBorderInsetsIfNoTabs", Boolean.FALSE,
                "JideTabbedPane.tabAreaInsets", new InsetsUIResource(2, 4, 0, 4),
                "JideTabbedPane.tabAreaBackground", new ColorUIResource(XertoUtils.getApplicationFrameBackgroundColor()),
                "JideTabbedPane.tabRunOverlay", new Integer(2),
                "JideTabbedPane.font", controlFont,
                "JideTabbedPane.selectedTabFont", controlFont,
                "JideTabbedPane.selectedTabTextForeground", new ColorUIResource(XertoUtils.getTabForgroundColor()),
                "JideTabbedPane.unselectedTabTextForeground", inactiveTabForground,
                "JideTabbedPane.selectedTabBackground", new ColorUIResource(XertoUtils.getSelectedTabBackgroundColor()),
                "JideTabbedPane.textIconGap", new Integer(4),
                "JideTabbedPane.showIconOnTab", Boolean.TRUE,
                "JideTabbedPane.showCloseButtonOnTab", Boolean.FALSE,
                "JideTabbedPane.closeButtonAlignment", new Integer(SwingConstants.TRAILING),
                "JideTabbedPane.focusInputMap",
                new UIDefaults.LazyInputMap(new Object[]{
                        "RIGHT", "navigateRight",
                        "KP_RIGHT", "navigateRight",
                        "LEFT", "navigateLeft",
                        "KP_LEFT", "navigateLeft",
                        "UP", "navigateUp",
                        "KP_UP", "navigateUp",
                        "DOWN", "navigateDown",
                        "KP_DOWN", "navigateDown",
                        "ctrl DOWN", "requestFocusForVisibleComponent",
                        "ctrl KP_DOWN", "requestFocusForVisibleComponent",
                }),
                "JideTabbedPane.ancestorInputMap",
                new UIDefaults.LazyInputMap(new Object[]{
                        "ctrl PAGE_DOWN", "navigatePageDown",
                        "ctrl PAGE_UP", "navigatePageUp",
                        "ctrl UP", "requestFocus",
                        "ctrl KP_UP", "requestFocus",
                }),

                "Button.font", controlFont,
                "Button.margin", new InsetsUIResource(2, 11, 2, 11),

                "ButtonPanel.order", "ACO",
                "ButtonPanel.oppositeOrder", "H",
                "ButtonPanel.buttonGap", new Integer(6),
                "ButtonPanel.groupGap", new Integer(6),
                "ButtonPanel.minButtonWidth", new Integer(75),

                "JideSplitButton.font", controlFont,
                "JideSplitButton.margin", new InsetsUIResource(3, 3, 3, 7),
                "JideSplitButton.border", buttonBorder,
                "JideSplitButton.borderPainted", Boolean.FALSE,
                "JideSplitButton.textIconGap", new Integer(3),
                "JideSplitButton.selectionForeground", menuTextColor,
                "JideSplitButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{
                "SPACE", "pressed",
                "released SPACE", "released",
                "ENTER", "pressed",
                "released ENTER", "released", // no these two for metal
                "DOWN", "downPressed",
                "released DOWN", "downReleased",
        }),

                "RangeSlider.lowerIcon", IconsFactory.getIcon(null, sliderHorizontalImage, 0, 0, 9, 8),
                "RangeSlider.upperIcon", IconsFactory.getIcon(null, sliderHorizontalImage, 0, 8, 9, 8),
                "RangeSlider.middleIcon", IconsFactory.getIcon(null, sliderHorizontalImage, 0, 16, 9, 6),
                "RangeSlider.lowerVIcon", IconsFactory.getIcon(null, sliderVerticalalImage, 0, 0, 8, 9),
                "RangeSlider.upperVIcon", IconsFactory.getIcon(null, sliderVerticalalImage, 8, 0, 8, 9),
                "RangeSlider.middleVIcon", IconsFactory.getIcon(null, sliderVerticalalImage, 16, 0, 6, 9),

                "Cursor.hsplit", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.HSPLIT),
                "Cursor.vsplit", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.VSPLIT),

                "Cursor.north", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.NORTH),
                "Cursor.south", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.SOUTH),
                "Cursor.east", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.EAST),
                "Cursor.west", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.WEST),
                "Cursor.tab", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.TAB),
                "Cursor.float", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.FLOAT),
                "Cursor.vertical", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.VERTICAL),
                "Cursor.horizontal", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.HORIZONTAL),
                "Cursor.delete", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.DELETE),
                "Cursor.drag", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.DROP),
                "Cursor.dragStop", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.NODROP),
                "Cursor.dragText", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.DROP_TEXT),
                "Cursor.dragTextStop", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.NODROP_TEXT),

                "Gripper.size", new Integer(8),
                "Gripper.foreground", gripperForeground,
                "Gripper.painter", gripperPainter,

                "Icon.floating", Boolean.FALSE,

                "Resizable.resizeBorder", resizeBorder,

                "TextArea.font", controlFont,
        };
        table.putDefaults(uiDefaults);

        int products = LookAndFeelFactory.getProductsUsed();

        if ((products & PRODUCT_DOCK) != 0) {
            ImageIcon titleButtonImage = IconsFactory.getImageIcon(XertoWindowsUtils.class, "icons/title_buttons_xerto.gif"); // 10 x 10 x 8
            final int titleButtonSize = 10;

            FrameBorder frameBorder = new FrameBorder();

            boolean useShadowBorder = "true".equals(SecurityUtils.getProperty("jide.shadeSlidingBorder", "false"));

            Object slidingEastFrameBorder = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                    new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
                public Object convert(Object[] obj) {
                    return new SlidingFrameBorder((Color) obj[0], (Color) obj[1], (Color) obj[2], (Color) obj[3],
                            new Insets(1, SlidingFrameBorder.SHADOW_SIZE + 5, 1, 0));
                }
            });

            Object slidingWestFrameBorder = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                    new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
                public Object convert(Object[] obj) {
                    return new SlidingFrameBorder((Color) obj[0], (Color) obj[1], (Color) obj[2], (Color) obj[3],
                            new Insets(1, 0, 1, SlidingFrameBorder.SHADOW_SIZE + 5));
                }
            });

            Object slidingNorthFrameBorder = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                    new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
                public Object convert(Object[] obj) {
                    return new SlidingFrameBorder((Color) obj[0], (Color) obj[1], (Color) obj[2], (Color) obj[3],
                            new Insets(0, 1, SlidingFrameBorder.SHADOW_SIZE + 5, 1));
                }
            });

            Object slidingSouthFrameBorder = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                    new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
                public Object convert(Object[] obj) {
                    return new SlidingFrameBorder((Color) obj[0], (Color) obj[1], (Color) obj[2], (Color) obj[3],
                            new Insets(SlidingFrameBorder.SHADOW_SIZE + 5, 1, 0, 1));
                }
            });

            Object slidingEastFrameBorder2 = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                    new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
                public Object convert(Object[] obj) {
                    return new ResizeFrameBorder((Color) obj[0], (Color) obj[1], (Color) obj[2], (Color) obj[3],
                            new Insets(0, 4, 0, 0));
                }
            });

            Object slidingWestFrameBorder2 = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                    new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
                public Object convert(Object[] obj) {
                    return new ResizeFrameBorder((Color) obj[0], (Color) obj[1], (Color) obj[2], (Color) obj[3],
                            new Insets(0, 0, 0, 4));
                }
            });

            Object slidingNorthFrameBorder2 = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                    new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
                public Object convert(Object[] obj) {
                    return new ResizeFrameBorder((Color) obj[0], (Color) obj[1], (Color) obj[2], (Color) obj[3],
                            new Insets(0, 0, 4, 0));
                }
            });

            Object slidingSouthFrameBorder2 = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                    new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
                public Object convert(Object[] obj) {
                    return new ResizeFrameBorder((Color) obj[0], (Color) obj[1], (Color) obj[2], (Color) obj[3],
                            new Insets(4, 0, 0, 0));
                }
            });

            uiDefaults = new Object[]{
                    // dock
                    "Workspace.background", mdiBackgroundColor,

                    "SidePane.margin", new InsetsUIResource(2, 2, 0, 0),
                    "SidePane.iconTextGap", new Integer(2),
                    "SidePane.textBorderGap", new Integer(13),
                    "SidePane.itemGap", new Integer(5),
                    "SidePane.groupGap", new Integer(13),
                    "SidePane.foreground", defaultDarkShadowColor,
                    "SidePane.background", new ColorUIResource(XertoUtils.getApplicationFrameBackgroundColor()),
                    "SidePane.lineColor", defaultShadowColor,
                    "SidePane.buttonBackground", new ColorUIResource(XertoUtils.getLightControlColor()),
                    "SidePane.selectedButtonBackground", selectedButtonColor,
                    "SidePane.selectedButtonForeground", defaultTextColor,
                    "SidePane.font", controlFont,
                    "SidePane.orientation", new Integer(1),
                    "SidePane.showSelectedTabText", Boolean.TRUE,
                    "SidePane.alwaysShowTabText", Boolean.FALSE,

                    "DockableFrame.defaultIcon", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.BLANK),
                    "DockableFrame.background", defaultBackgroundColor,
                    "DockableFrame.border", frameBorder,
                    "DockableFrame.floatingBorder", new BorderUIResource(BorderFactory.createLineBorder(XertoUtils.getFrameBorderColor())),
                    "DockableFrame.slidingEastBorder", useShadowBorder ? slidingEastFrameBorder : slidingEastFrameBorder2,
                    "DockableFrame.slidingWestBorder", useShadowBorder ? slidingWestFrameBorder : slidingWestFrameBorder2,
                    "DockableFrame.slidingNorthBorder", useShadowBorder ? slidingNorthFrameBorder : slidingNorthFrameBorder2,
                    "DockableFrame.slidingSouthBorder", useShadowBorder ? slidingSouthFrameBorder : slidingSouthFrameBorder2,

                    "DockableFrame.activeTitleBackground", activeTitleBackgroundColor,
                    "DockableFrame.activeTitleForeground", new ColorUIResource(Color.WHITE),
                    "DockableFrame.inactiveTitleBackground", defaultBackgroundColor,
                    "DockableFrame.inactiveTitleForeground", new ColorUIResource(Color.WHITE),
                    "DockableFrame.titleBorder", new BorderUIResource(BorderFactory.createEmptyBorder(1, 0, 1, 0)),
                    "DockableFrame.activeTitleBorderColor", activeTitleBackgroundColor,
                    "DockableFrame.inactiveTitleBorderColor", defaultShadowColor,
                    "DockableFrame.font", controlFont,

                    "DockableFrameTitlePane.gripperPainter", gripperPainter,
                    "DockableFrameTitlePane.font", controlFont,
                    "DockableFrameTitlePane.hideIcon", IconsFactory.getIcon(null, titleButtonImage, 0, 0, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.unfloatIcon", IconsFactory.getIcon(null, titleButtonImage, 0, titleButtonSize, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.floatIcon", IconsFactory.getIcon(null, titleButtonImage, 0, 2 * titleButtonSize, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.autohideIcon", IconsFactory.getIcon(null, titleButtonImage, 0, 3 * titleButtonSize, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.stopAutohideIcon", IconsFactory.getIcon(null, titleButtonImage, 0, 4 * titleButtonSize, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.hideAutohideIcon", IconsFactory.getIcon(null, titleButtonImage, 0, 5 * titleButtonSize, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.maximizeIcon", IconsFactory.getIcon(null, titleButtonImage, 0, 6 * titleButtonSize, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.restoreIcon", IconsFactory.getIcon(null, titleButtonImage, 0, 7 * titleButtonSize, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.titleBarComponent", Boolean.FALSE,

                    "DockableFrameTitlePane.alwaysShowAllButtons", Boolean.FALSE, // true if show all three buttons no matter if the buttons is available. false if only show buttons which is available
                    "DockableFrameTitlePane.buttonsAlignment", new Integer(SwingConstants.TRAILING), // trailing or leading
                    "DockableFrameTitlePane.titleAlignment", new Integer(SwingConstants.LEADING), // trailing or leading or center
                    "DockableFrameTitlePane.buttonGap", new Integer(0), // gap between buttons
                    "DockableFrameTitlePane.showIcon", Boolean.TRUE, // show icon or not, the alignment is the same as titleAlignment
                    "DockableFrameTitlePane.margin", new InsetsUIResource(0, 3, 0, 3), // gap

                    "Contour.color", new ColorUIResource(136, 136, 136),
                    "Contour.thickness", new Integer(4),

                    "ContentContainer.background", defaultFormBackground,
                    "ContentContainer.vgap", new Integer(3),
                    "ContentContainer.hgap", new Integer(3),

                    "DockingFramework.changeCursor", Boolean.FALSE,

                    "FrameContainer.contentBorderInsets", new InsetsUIResource(0, 0, 0, 0),
            };
            table.putDefaults(uiDefaults);
        }

        if ((products & PRODUCT_COMPONENTS) != 0) {
            ImageIcon collapsiblePaneImage = IconsFactory.getImageIcon(XertoWindowsUtils.class, "icons/collapsible_pane_xerto.gif"); // 12 x 12 x 2
            final int collapsiblePaneSize = 12;

            ColorUIResource collapsiblePaneBackground = new ColorUIResource(172, 168, 153);

            uiDefaults = new Object[]{
                    // components
                    "CollapsiblePanes.border", new BorderUIResource(BorderFactory.createEmptyBorder(12, 12, 0, 12)),
                    "CollapsiblePanes.gap", new Integer(5),

                    "CollapsiblePane.background", collapsiblePaneBackground,
                    "CollapsiblePane.contentBackground", defaultLtHighlightColor,
                    "CollapsiblePane.foreground", new ColorUIResource(XertoUtils.getTextColor(collapsiblePaneBackground)),
                    "CollapsiblePane.emphasizedBackground", collapsiblePaneBackground,
                    "CollapsiblePane.emphasizedForeground", new ColorUIResource(XertoUtils.getTextColor(XertoUtils.getEmBaseColor(collapsiblePaneBackground))),
                    "CollapsiblePane.border", new BorderUIResource(BorderFactory.createEmptyBorder(0, 0, 0, 0)),
                    // this is for metal
                    // "CollapsiblePane.border", new BorderUIResource(BorderFactory.createLineBorder(new Color(172, 168, 153))),

                    "CollapsiblePane.font", controlFont,

                    "CollapsiblePane.contentBorder", new BorderUIResource(BorderFactory.createEmptyBorder(8, 10, 8, 10)),

                    "CollapsiblePane.titleBorder", new BorderUIResource(BorderFactory.createEmptyBorder()),
                    "CollapsiblePane.titleFont", boldFont,

                    "CollapsiblePane.downIcon", IconsFactory.createBrighterImage(IconsFactory.getIcon(null, collapsiblePaneImage, 0, 0, collapsiblePaneSize, collapsiblePaneSize).getImage()),
                    "CollapsiblePane.upIcon", IconsFactory.createBrighterImage(IconsFactory.getIcon(null, collapsiblePaneImage, 0, collapsiblePaneSize, collapsiblePaneSize, collapsiblePaneSize).getImage()),
                    "CollapsiblePane.downRolloverIcon", IconsFactory.getIcon(null, collapsiblePaneImage, 0, 0, collapsiblePaneSize, collapsiblePaneSize),
                    "CollapsiblePane.upRolloverIcon", IconsFactory.getIcon(null, collapsiblePaneImage, 0, collapsiblePaneSize, collapsiblePaneSize, collapsiblePaneSize),

                    "StatusBarItem.border", new BorderUIResource(BorderFactory.createEmptyBorder(0, 1, 0, 1)),

                    "StatusBar.border", new StatusBarBorder(),
                    "StatusBar.gap", new Integer(2),
                    "StatusBar.background", defaultBackgroundColor,
                    "StatusBar.font", controlFont,

                    "DocumentPane.groupBorder", new BorderUIResource(BorderFactory.createLineBorder(Color.gray)),
                    "DocumentPane.newHorizontalGroupIcon", JideIconsFactory.getImageIcon(JideIconsFactory.WindowMenu.NEW_HORIZONTAL_TAB),
                    "DocumentPane.newVerticalGroupIcon", JideIconsFactory.getImageIcon(JideIconsFactory.WindowMenu.NEW_VERTICAL_TAB),
                    "DocumentPane.boldActiveTab", Boolean.FALSE,
            };
            table.putDefaults(uiDefaults);
        }

        if ((products & PRODUCT_ACTION) != 0) {
            uiDefaults = new Object[]{
                    // action
                    "CommandBar.font", toolbarFont,
                    "CommandBar.background", commandBarBackground,
                    "CommandBar.foreground", defaultTextColor,
                    "CommandBar.shadow", defaultShadowColor,
                    "CommandBar.darkShadow", defaultDarkShadowColor,
                    "CommandBar.light", defaultHighlightColor,
                    "CommandBar.highlight", defaultLtHighlightColor,
                    "CommandBar.border", new BorderUIResource(BorderFactory.createEmptyBorder(1, 1, 1, 1)),
                    "CommandBar.borderVert", new BorderUIResource(BorderFactory.createEmptyBorder(1, 1, 1, 1)),
                    "CommandBar.borderFloating", new BorderUIResource(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(table.getColor("activeCaption"), 2),
                    BorderFactory.createEmptyBorder(1, 1, 1, 1))),
                    "CommandBar.ancestorInputMap",
                    new UIDefaults.LazyInputMap(new Object[]{
                            "UP", "navigateUp",
                            "KP_UP", "navigateUp",
                            "DOWN", "navigateDown",
                            "KP_DOWN", "navigateDown",
                            "LEFT", "navigateLeft",
                            "KP_LEFT", "navigateLeft",
                            "RIGHT", "navigateRight",
                            "KP_RIGHT", "navigateRight"
                    }),
                    "CommandBar.titleBarSize", new Integer(17),
                    "CommandBar.titleBarButtonGap", new Integer(1),
                    "CommandBar.titleBarBackground", activeTitleBackgroundColor,
                    "CommandBar.titleBarForeground", activeTitleTextColor,
                    "CommandBar.titleBarFont", boldFont,

                    "CommandBar.separatorSize", new Integer(5),

                    // *** Separator
                    "CommandBarSeparator.background", XertoUtils.getControlColor(),
                    "CommandBarSeparator.foreground", XertoUtils.getControlMidShadowColor(),

                    "Chevron.size", new Integer(11),
            };
            table.putDefaults(uiDefaults);
        }

        if ((products & PRODUCT_DOCK) != 0) {
            uiDefaults = new Object[]{
                    // grid
                    "NestedTableHeader.cellBorder", new HeaderCellBorder(),
            };
            table.putDefaults(uiDefaults);
        }

        if (!JideSwingUtilities.shouldUseSystemFont()) {
            Object uiDefaultsFont[] = {
                    "TabbedPane.font", controlFont,
                    "TitledBorder.font", boldFont,
                    "TableHeader.font", controlFont,
                    "Table.font", controlFont,
                    "List.font", controlFont,
                    "Tree.font", controlFont,
                    "ToolTip.font", controlFont,
                    "CheckBox.font", controlFont,
                    "RadioButton.font", controlFont,
                    "Label.font", controlFont,
                    "Panel.font", controlFont,
                    "TextField.font", controlFont,
                    "ComboBox.font", controlFont,
                    "Button.font", controlFont
            };
            table.putDefaults(uiDefaultsFont);
        }

        table.put("Theme.painter", XertoPainter.getInstance());

        // since it used BasicPainter, make sure it is after Theme.Painter is set first.
        Object popupMenuBorder = new ExtWindowsDesktopProperty(new String[]{"null"}, new Object[]{((BasicPainter) table.get("Theme.painter")).getMenuItemBorderColor()}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new BorderUIResource(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder((Color) obj[0]), BorderFactory.createEmptyBorder(1, 1, 1, 1)));
            }
        });
        table.put("PopupMenu.border", popupMenuBorder);
    }
}