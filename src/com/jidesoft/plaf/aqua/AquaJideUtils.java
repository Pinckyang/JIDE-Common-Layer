/*
 * @(#)AquaJideUtils.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */
package com.jidesoft.plaf.aqua;

import com.jidesoft.icons.IconsFactory;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.ExtWindowsDesktopProperty;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.plaf.WindowsDesktopProperty;
import com.jidesoft.plaf.basic.BasicRangeSliderUI;
import com.jidesoft.plaf.vsnet.ConvertListener;
import com.jidesoft.plaf.vsnet.VsnetLookAndFeelExtension;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.JideTabbedPane;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;

/**
 * AquaJideUtils to add Jide extension to AquaLookAndFeel
 */
public class AquaJideUtils extends VsnetLookAndFeelExtension {
    /**
     * Initializes class defaults.
     *
     * @param table
     */
    public static void initClassDefaults(UIDefaults table) {
        VsnetLookAndFeelExtension.initClassDefaults(table);

        final String aquaPackageName = "com.jidesoft.plaf.aqua.";

        int products = LookAndFeelFactory.getProductsUsed();

        table.put("JideSplitButtonUI", aquaPackageName + "AquaJideSplitButtonUI");
        table.put("JidePopupMenuUI", aquaPackageName + "AquaJidePopupMenuUI");
        table.put("JideTabbedPaneUI", aquaPackageName + "AquaJideTabbedPaneUI");
        table.put("GripperUI", aquaPackageName + "AquaGripperUI");

        if ((products & PRODUCT_GRIDS) != 0) {
            table.put("JideTableUI", aquaPackageName + "AquaJideTableUI");
            table.put("CellSpanTableUI", aquaPackageName + "AquaCellSpanTableUI");
            table.put("HierarchicalTableUI", aquaPackageName + "AquaHierarchicalTableUI");
            table.put("NestedTableHeaderUI", aquaPackageName + "AquaNestedTableHeaderUI");
            table.put("EditableTableHeaderUI", aquaPackageName + "AquaEditableTableHeaderUI");
        }

        if ((products & PRODUCT_DOCK) != 0) {
            table.put("SidePaneUI", aquaPackageName + "AquaSidePaneUI");
            table.put("DockableFrameUI", aquaPackageName + "AquaDockableFrameUI");
        }

        if ((products & PRODUCT_ACTION) != 0) {
            table.put("CommandBarUI", aquaPackageName + "AquaCommandBarUI");
        }
    }

    /**
     * Initializes components defaults.
     *
     * @param table
     */
    public static void initComponentDefaults(UIDefaults table) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        WindowsDesktopProperty defaultTextColor = new WindowsDesktopProperty("win.button.textColor", table.get("controlText"), toolkit);

        Object defaultBackgroundColor = table.get("Panel.background"); // AquaImageFactory.getWindowBackgroundColorUIResource();

        WindowsDesktopProperty defaultLightColor = new WindowsDesktopProperty("win.3d.lightColor", table.get("controlHighlight"), toolkit);
        WindowsDesktopProperty defaultHighlightColor = new WindowsDesktopProperty("win.3d.highlightColor", table.get("controlLtHighlight"), toolkit);
        WindowsDesktopProperty defaultShadowColor = new WindowsDesktopProperty("win.3d.shadowColor", table.get("controlShadow"), toolkit);
        WindowsDesktopProperty defaultDarkShadowColor = new WindowsDesktopProperty("win.3d.darkShadowColor", table.get("controlDkShadow"), toolkit);

        Object mdiBackgroundColor = table.get("Panel.background"); // AquaImageFactory.getWindowBackgroundColorUIResource();

        Object controlFont = new UIDefaults.ProxyLazyValue("apple.laf.AquaLookAndFeel", "getControlTextFont");

        Object controlSmallFont = new UIDefaults.ProxyLazyValue("apple.laf.AquaLookAndFeel", "getControlTextSmallFont");

        Object boldFont = new UIDefaults.ProxyLazyValue("apple.laf.AquaLookAndFeel", "getControlTextFont");

        Object resizeBorder = BorderFactory.createLineBorder(new Color(230, 230, 230), 2);

        Object defaultFormBackground = new ExtWindowsDesktopProperty(// Not exactly right
                new String[]{"win.3d.shadowColor"}, new Object[]{table.get("control")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return obj[0];
            }
        });

        Object inactiveTabForground = new ExtWindowsDesktopProperty(// Not exactly right
                new String[]{"win.3d.shadowColor"}, new Object[]{table.get("controlShadow")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return ((Color) obj[0]).darker();
            }
        });

        Object focusedButtonColor = table.get("Menu.selectionBackground"); // AquaImageFactory.getMenuSelectionBackgroundColorUIResource();

        Object selectedAndFocusedButtonColor = table.get("Menu.selectionBackground"); // AquaImageFactory.getMenuSelectionBackgroundColorUIResource();

        Object selectedButtonColor = table.get("Menu.selectionBackground"); // AquaImageFactory.getMenuSelectionBackgroundColorUIResource();

        Object selectionBackgroundColor = table.get("TextField.selectionBackground"); // AquaImageFactory.getTextSelectionBackgroundColorUIResource();

        Object buttonBorder = new BasicBorders.MarginBorder();

        ImageIcon sliderHorizontalImage = IconsFactory.getImageIcon(BasicRangeSliderUI.class, "icons/slider_horizontal.gif");
        ImageIcon sliderVerticalalImage = IconsFactory.getImageIcon(BasicRangeSliderUI.class, "icons/slider_vertical.gif");

        Object uiDefaults[] = {
                "JideButton.selectedAndFocusedBackground", selectedAndFocusedButtonColor,
                "JideButton.focusedBackground", focusedButtonColor,
                "JideButton.selectedBackground", selectedButtonColor,
                "JideButton.borderColor", selectionBackgroundColor,

                "JideButton.font", controlFont,
                "JideButton.background", defaultBackgroundColor,
                "JideButton.foreground", table.get("controlText"),
                "JideButton.shadow", table.getColor("controlShadow"),
                "JideButton.darkShadow", table.getColor("controlDkShadow"),
                "JideButton.light", table.getColor("controlHighlight"),
                "JideButton.highlight", table.getColor("controlLtHighlight"),

                "JideButton.border", buttonBorder,
                "JideButton.margin", new InsetsUIResource(3, 3, 3, 3),
                "JideButton.textIconGap", new Integer(4),
                "JideButton.textShiftOffset", new Integer(0),
                "JideButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{
                "SPACE", "pressed",
                "released SPACE", "released",
                "ENTER", "pressed",
                "released ENTER", "released"
        }),

                "JideSplitPane.dividerSize", new Integer(3),
                "JideSplitPaneDivider.border", new BorderUIResource(BorderFactory.createEmptyBorder()),
                "JideSplitPaneDivider.background", defaultBackgroundColor,

                "JideTabbedPane.defaultTabShape", new Integer(JideTabbedPane.SHAPE_VSNET),
                "JideTabbedPane.defaultResizeMode", new Integer(JideTabbedPane.RESIZE_MODE_NONE),
                "JideTabbedPane.defaultTabColorTheme", new Integer(JideTabbedPane.COLOR_THEME_VSNET),

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

                "JideTabbedPane.border", new BorderUIResource(BorderFactory.createEmptyBorder(0, 0, 0, 0)),
                "JideTabbedPane.background", defaultFormBackground,
                "JideTabbedPane.foreground", defaultTextColor,
                "JideTabbedPane.light", defaultLightColor,
                "JideTabbedPane.highlight", defaultHighlightColor,
                "JideTabbedPane.shadow", defaultShadowColor,
                "JideTabbedPane.tabInsets", new InsetsUIResource(1, 4, 1, 4),
                "JideTabbedPane.contentBorderInsets", new InsetsUIResource(2, 2, 2, 2),
                "JideTabbedPane.ignoreContentBorderInsetsIfNoTabs", Boolean.FALSE,
                "JideTabbedPane.tabAreaInsets", new InsetsUIResource(2, 4, 0, 4),
                "JideTabbedPane.tabAreaBackground", defaultFormBackground,
                "JideTabbedPane.tabRunOverlay", new Integer(2),
                "JideTabbedPane.font", controlSmallFont,
                "JideTabbedPane.selectedTabFont", controlSmallFont,
                "JideTabbedPane.darkShadow", defaultTextColor,
                "JideTabbedPane.selectedTabTextForeground", defaultTextColor,
                "JideTabbedPane.unselectedTabTextForeground", inactiveTabForground,
                "JideTabbedPane.selectedTabBackground", defaultBackgroundColor,
                "JideTabbedPane.textIconGap", new Integer(4),
                "JideTabbedPane.showIconOnTab", Boolean.TRUE,
                "JideTabbedPane.showCloseButtonOnTab", Boolean.TRUE,
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

                "Resizable.resizeBorder", resizeBorder,

                "Contour.color", new ColorUIResource(136, 136, 136),
                "Contour.thickness", new Integer(4),

                "Gripper.size", new Integer(8),
                "Gripper.foreground", defaultShadowColor,

                "Icon.floating", Boolean.FALSE,

                "JideSplitButton.font", controlFont,
                "JideSplitButton.margin", new InsetsUIResource(3, 3, 3, 7),
                "JideSplitButton.border", buttonBorder,
                "JideSplitButton.borderPainted", Boolean.FALSE,
                "JideSplitButton.textIconGap", new Integer(3),
                "JideSplitButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{
                "SPACE", "pressed",
                "released SPACE", "released",
                "ENTER", "pressed",
                "released ENTER", "released",
                "DOWN", "downPressed",
                "released DOWN", "downReleased"
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
        };
        table.putDefaults(uiDefaults);

        int products = LookAndFeelFactory.getProductsUsed();

        if ((products & PRODUCT_DOCK) != 0) {
            Color slidingBorderColor = new Color(190, 190, 190);
            Object slidingEastFrameBorder = BorderFactory.createLineBorder(slidingBorderColor);
            Object slidingWestFrameBorder = BorderFactory.createLineBorder(slidingBorderColor);
            Object slidingNorthFrameBorder = BorderFactory.createLineBorder(slidingBorderColor);
            Object slidingSouthFrameBorder = BorderFactory.createLineBorder(slidingBorderColor);

            uiDefaults = new Object[]{
                    "DockableFrame.defaultIcon", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.BLANK),
                    "DockableFrame.background", defaultBackgroundColor,
                    "DockableFrame.border", new BorderUIResource(BorderFactory.createLineBorder(Color.lightGray, 1)),
                    "DockableFrame.floatingBorder", new BorderUIResource(BorderFactory.createLineBorder(Color.lightGray, 1)),
                    "DockableFrame.slidingEastBorder", slidingEastFrameBorder,
                    "DockableFrame.slidingWestBorder", slidingWestFrameBorder,
                    "DockableFrame.slidingNorthBorder", slidingNorthFrameBorder,
                    "DockableFrame.slidingSouthBorder", slidingSouthFrameBorder,

                    "DockableFrame.activeTitleBackground", UIDefaultsLookup.getColor("InternalFrame.activeTitleBackground"),
                    "DockableFrame.activeTitleForeground", UIDefaultsLookup.getColor("InternalFrame.activeTitleForeground"),
                    "DockableFrame.inactiveTitleBackground", UIDefaultsLookup.getColor("InternalFrame.inactiveTitleBackground"),
                    "DockableFrame.inactiveTitleForeground", UIDefaultsLookup.getColor("InternalFrame.inactiveTitleForeground"),
                    "DockableFrame.titleBorder", new BorderUIResource(BorderFactory.createEmptyBorder(0, 0, 0, 0)),
                    "DockableFrame.activeTitleBorderColor", UIDefaultsLookup.getColor("InternalFrame.activeTitleBackground"),
                    "DockableFrame.inactiveTitleBorderColor", defaultShadowColor,
                    "DockableFrame.font", controlFont,

                    "DockableFrameTitlePane.font", controlSmallFont,
                    "DockableFrameTitlePane.titleBarComponent", Boolean.FALSE,

                    "DockableFrameTitlePane.alwaysShowAllButtons", Boolean.TRUE, // true if show all three buttons no matter if the buttons is available. false if only show buttons which is available
                    "DockableFrameTitlePane.buttonsAlignment", new Integer(SwingConstants.LEADING), // trailing or leading
                    "DockableFrameTitlePane.titleAlignment", new Integer(SwingConstants.CENTER), // trailing or leading or center
                    "DockableFrameTitlePane.buttonGap", new Integer(3), // gap between buttons
                    "DockableFrameTitlePane.showIcon", Boolean.FALSE, // show icon or not, the alignment is the same as titleAlignment
                    "DockableFrameTitlePane.margin", new InsetsUIResource(0, 10, 0, 3), // gap

                    "SidePane.margin", new InsetsUIResource(2, 2, 0, 0),
                    "SidePane.iconTextGap", new Integer(2),
                    "SidePane.textBorderGap", new Integer(13),
                    "SidePane.itemGap", new Integer(5),
                    "SidePane.groupGap", new Integer(8),
                    "SidePane.foreground", defaultDarkShadowColor,
                    "SidePane.background", defaultFormBackground,
                    "SidePane.lineColor", new Color(151, 151, 151),
                    "SidePane.buttonBackground", new Color(133, 133, 133),
                    "SidePane.selectedButtonBackground", new Color(133, 133, 133),
                    "SidePane.selectedButtonForeground", defaultTextColor,
                    "SidePane.font", controlSmallFont,
                    "SidePane.orientation", new Integer(1),
                    "SidePane.showSelectedTabText", Boolean.TRUE,
                    "SidePane.alwaysShowTabText", Boolean.TRUE,

                    "Workspace.background", mdiBackgroundColor,

                    "DockingFramework.changeCursor", Boolean.FALSE,

                    "ContentContainer.background", defaultFormBackground,
                    "ContentContainer.vgap", new Integer(1),
                    "ContentContainer.hgap", new Integer(1),
                    "MainContainer.border", new BorderUIResource(BorderFactory.createEmptyBorder(0, 0, 0, 0)),
            };
            table.putDefaults(uiDefaults);
        }

        if ((products & PRODUCT_COMPONENTS) != 0) {
            ImageIcon collapsiblePaneImage = IconsFactory.getImageIcon(AquaJideUtils.class, "icons/collapsible_pane_aqua.gif"); // 12 x 12 x 2
            final int collapsiblePaneSize = 12;

            uiDefaults = new Object[]{
                    "CollapsiblePanes.border", new BorderUIResource(BorderFactory.createEmptyBorder(12, 12, 0, 12)),
                    "CollapsiblePanes.gap", new Integer(15),

                    "CollapsiblePane.background", UIDefaultsLookup.getColor("InternalFrame.inactiveTitleBackground"),
                    "CollapsiblePane.contentBackground", defaultHighlightColor,
                    "CollapsiblePane.foreground", UIDefaultsLookup.getColor("InternalFrame.inactiveTitleForeground"),
                    "CollapsiblePane.emphasizedBackground", UIDefaultsLookup.getColor("InternalFrame.activeTitleBackground"),
                    "CollapsiblePane.emphasizedForeground", UIDefaultsLookup.getColor("InternalFrame.activeTitleForeground"),
                    "CollapsiblePane.border", new BorderUIResource(BorderFactory.createEmptyBorder(1, 1, 1, 1)),
                    "CollapsiblePane.font", controlFont,

                    "CollapsiblePane.contentBorder", new BorderUIResource(BorderFactory.createEmptyBorder(8, 10, 8, 10)),

                    "CollapsiblePane.titleBorder", new BorderUIResource(BorderFactory.createEmptyBorder(3, 3, 3, 3)),
                    "CollapsiblePane.titleFont", boldFont,
                    "CollapsiblePane.downIcon", IconsFactory.getIcon(null, collapsiblePaneImage, 0, 0, collapsiblePaneSize, collapsiblePaneSize),
                    "CollapsiblePane.upIcon", IconsFactory.getIcon(null, collapsiblePaneImage, 0, collapsiblePaneSize, collapsiblePaneSize, collapsiblePaneSize),

                    "StatusBarItem.border", new BorderUIResource(BorderFactory.createLineBorder(table.getColor("controlShadow"), 1)),

                    "StatusBar.border", new BorderUIResource(BorderFactory.createEmptyBorder(2, 0, 0, 0)),
                    "StatusBar.gap", new Integer(2),
                    "StatusBar.background", defaultBackgroundColor,
                    "StatusBar.font", controlFont,

                    "DocumentPane.groupBorder", new BorderUIResource(BorderFactory.createLineBorder(Color.gray)),
                    "DocumentPane.newHorizontalGroupIcon", JideIconsFactory.getImageIcon(JideIconsFactory.WindowMenu.NEW_HORIZONTAL_TAB),
                    "DocumentPane.newVerticalGroupIcon", JideIconsFactory.getImageIcon(JideIconsFactory.WindowMenu.NEW_VERTICAL_TAB),
                    "DocumentPane.boldActiveTab", Boolean.TRUE,

                    "ButtonPanel.order", "CA",
                    "ButtonPanel.oppositeOrder", "HO",
                    "ButtonPanel.buttonGap", new Integer(6),
                    "ButtonPanel.groupGap", new Integer(12),
                    "ButtonPanel.minButtonWidth", new Integer(69),
            };
            table.putDefaults(uiDefaults);
        }
        if ((products & PRODUCT_ACTION) != 0) {
            uiDefaults = new Object[]{


                    "CommandBar.font", controlFont,
                    "CommandBar.background", defaultBackgroundColor,
                    "CommandBar.foreground", table.get("controlText"),
                    "CommandBar.shadow", table.getColor("controlShadow"),
                    "CommandBar.darkShadow", table.getColor("controlDkShadow"),
                    "CommandBar.light", table.getColor("controlHighlight"),
                    "CommandBar.highlight", table.getColor("controlLtHighlight"),
                    "CommandBar.border", new BorderUIResource(BorderFactory.createEmptyBorder(1, 1, 1, 1)),
                    "CommandBar.borderVert", new BorderUIResource(BorderFactory.createEmptyBorder(1, 1, 1, 1)),
                    "CommandBar.borderFloating", new BorderUIResource(BorderFactory.createEmptyBorder(2, 2, 2, 2)),
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
                    "CommandBar.titleBarBackground", UIDefaultsLookup.getColor("InternalFrame.activeTitleBackground"),
                    "CommandBar.titleBarForeground", table.getColor("controlText"),
                    "CommandBar.titleBarFont", boldFont,
                    "CommandBar.minimumSize", new DimensionUIResource(20, 20),

                    "CommandBar.separatorSize", new Integer(5),

                    // *** Separator
                    "CommandBarSeparator.background", new Color(219, 216, 209),
                    "CommandBarSeparator.foreground", new Color(166, 166, 166),

                    "Chevron.size", new Integer(11),
                    "Chevron.alwaysVisible", Boolean.FALSE,

            };
            table.putDefaults(uiDefaults);
        }

        if ((products & PRODUCT_GRIDS) != 0) {
            uiDefaults = new Object[]{
                    "AbstractComboBox.useJButton", Boolean.FALSE,
            };
            table.putDefaults(uiDefaults);
        }
        table.put("Theme.painter", AquaPainter.getInstance());
    }

    public static boolean isGraphite() {
        String appleAquaColorVariant = AquaPreferences.getString("AppleAquaColorVariant");
        return "6".equals(appleAquaColorVariant);
    }

    // HALF buttons have to colours
    // lighter upper half
    // darker  lower half
    public static final Color[] HALF_LIGHT = {
            new Color(251, 251, 251),
            new Color(237, 237, 237)
    };

    public static final Color[] HALF_DARK = {
            new Color(133, 133, 133),
            new Color(125, 125, 125)
    };

    // AQUA gradients consist of two gradients for each half
    // light upper half
    // dark  upper half
    // light lower half
    // dark  lower half
    public static final Color[] AQUA_WHITE = {
            new Color(252, 252, 252),
            new Color(236, 236, 236),
            new Color(225, 225, 225),
            new Color(255, 255, 255)
    };

    public static final Color[] AQUA_BLUE = {
            new Color(221, 225, 244),
            new Color(139, 187, 238),
            new Color(100, 168, 242),
            new Color(187, 255, 255)
    };

    public static final Color[] AQUA_GRAPHITE = {
            new Color(231, 233, 235),
            new Color(182, 188, 198),
            new Color(158, 158, 180),
            new Color(231, 241, 255)
    };


    public static final Color[] AQUA_BANNER_WHITE = {
            new Color(255, 255, 255),
            new Color(248, 248, 248),
            new Color(228, 228, 228),
            new Color(239, 239, 239)
    };

    public static final Color[] AQUA_BANNER_BLUE = {
            new Color(103, 159, 254),
            new Color(73, 132, 253),
            new Color(51, 132, 253),
            new Color(84, 170, 254)
    };

    public static Color[] reverse(Color[] colors) {
        Color[] reverse = new Color[colors.length];
        for (int i = 0; i < colors.length; i++) {
            reverse[i] = colors[colors.length - i - 1];
        }
        return reverse;
    }

    public static void fillAquaGradientHorizontal(Graphics g, final Shape shape, final Color[] colors) {
        Color[] c = colors;
        if (c == null || c.length != 4) {
            c = AQUA_WHITE;
        }

        Graphics2D g2d = (Graphics2D) g;
// cause icon and text not showing when close button is on tab.
//        Shape oldClipShape = g2d.getClip();
//        g2d.setClip(shape);
        Rectangle rect = shape.getBounds();
        Rectangle r2 = new Rectangle(rect.x, rect.y + rect.height / 2, rect.width, rect.height / 2);
        Rectangle r1 = new Rectangle(rect.x, rect.y, rect.width, rect.height / 2);
        JideSwingUtilities.fillGradient(g2d, r1, c[0], c[1], true);
        JideSwingUtilities.fillGradient(g2d, r2, c[2], c[3], true);
//        g2d.setClip(oldClipShape);
    }

    public static void fillAquaGradientVertical(Graphics g, final Shape shape, final Color[] colors) {
        Color[] c = colors;
        if (c == null || c.length != 4) {
            c = AQUA_WHITE;
        }

        Graphics2D g2d = (Graphics2D) g;
//        Shape oldClipShape = g2d.getClip();
//        g2d.setClip(shape);
        Rectangle rect = shape.getBounds();
        Rectangle r2 = new Rectangle(rect.x + rect.width / 2, rect.y, rect.width / 2, rect.height);
        Rectangle r1 = new Rectangle(rect.x, rect.y, rect.width / 2, rect.height);
        JideSwingUtilities.fillGradient(g2d, r1, c[0], c[1], false);
        JideSwingUtilities.fillGradient(g2d, r2, c[2], c[3], false);
//        g.setClip(oldClipShape);
    }

    public static void fillSquareButtonHorizontal(Graphics g, Shape shape, final Color[] colors) {
        Color[] c = colors;
        if (c == null || c.length != 2) {
            c = HALF_LIGHT;
        }

        Graphics2D g2d = (Graphics2D) g;
//        Shape oldClipShape = g2d.getClip();
//        g2d.setClip(shape);
        Rectangle rect = shape.getBounds();
        g2d.setColor(c[0]);
        g2d.fillRect(rect.x, rect.y, rect.width, rect.height / 2);
        g2d.setColor(c[1]);
        g2d.fillRect(rect.x, rect.y + rect.height / 2, rect.width, rect.height / 2);

//        g2d.setClip(oldClipShape);
    }

    public static void fillSquareButtonVertical(Graphics g, Shape shape, final Color[] colors) {
        Color[] c = colors;
        if (c == null || c.length != 2) {
            c = HALF_LIGHT;
        }

        Graphics2D g2d = (Graphics2D) g;
//        Shape oldClipShape = g2d.getClip();
//        g2d.setClip(shape);
        Rectangle rect = shape.getBounds();
        g.setColor(c[0]);
        g.fillRect(rect.x, rect.y, rect.width / 2, rect.height);
        g.setColor(c[1]);
        g.fillRect(rect.x + rect.width / 2, rect.y, rect.width / 2, rect.height);
//        g2d.setClip(oldClipShape);
    }


    public static void antialiasShape(Graphics g, boolean onoff) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                onoff ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);

    }

    public static void antialiasText(Graphics g, boolean onoff) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                onoff ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

    }

}