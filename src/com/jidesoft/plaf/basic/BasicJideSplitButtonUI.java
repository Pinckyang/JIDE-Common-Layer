/*
 * @(#)VsnetMenuUI.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */

package com.jidesoft.plaf.basic;

import com.jidesoft.icons.IconsFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.plaf.vsnet.VsnetMenuUI;
import com.jidesoft.swing.*;
import com.jidesoft.utils.SecurityUtils;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


/**
 * Menu UI implementation
 */
public class BasicJideSplitButtonUI extends VsnetMenuUI {

    protected ThemePainter _painter;

    protected Color _shadowColor;
    protected Color _darkShadowColor;
    protected Color _highlight;
    protected Color _lightHighlightColor;

    protected int _splitButtonMargin = 12;
    protected int _splitButtonMarginOnMenu = 20;

    protected boolean _isFloatingIcon = false;

    private FocusListener _focusListener;

    private final static String propertyPrefix = "JideSplitButton";

    public static ComponentUI createUI(JComponent x) {
        return new BasicJideSplitButtonUI();
    }

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    protected void installDefaults() {
        _painter = (ThemePainter) UIDefaultsLookup.get("Theme.painter");
        _isFloatingIcon = UIDefaultsLookup.getBoolean("Icon.floating");

        _shadowColor = UIDefaultsLookup.getColor("controlShadow");
        _darkShadowColor = UIDefaultsLookup.getColor("controlDkShadow");
        _highlight = UIDefaultsLookup.getColor("controlHighlight");
        _lightHighlightColor = UIDefaultsLookup.getColor("controlLtHighlight");

        super.installDefaults();
    }

    protected void uninstallDefaults() {
        _painter = null;

        _shadowColor = null;
        _highlight = null;
        _lightHighlightColor = null;
        _darkShadowColor = null;

        super.uninstallDefaults();
    }

    protected void installListeners() {
        super.installListeners();
        if (_focusListener == null) {
            _focusListener = new FocusListener() {
                public void focusGained(FocusEvent e) {
                    menuItem.repaint();
                }

                public void focusLost(FocusEvent e) {
                    menuItem.repaint();
                }
            };
        }
        menuItem.addFocusListener(_focusListener);
    }

    protected void uninstallListeners() {
        super.uninstallListeners();
        if (_focusListener != null) {
            menuItem.removeFocusListener(_focusListener);
        }
    }

    /**
     * Returns the ui that is of type <code>klass</code>, or null if
     * one can not be found.
     */
    static Object getUIOfType(ComponentUI ui, Class klass) {
        if (klass.isInstance(ui)) {
            return ui;
        }
        return null;
    }

    /**
     * Returns the InputMap for condition <code>condition</code>. Called as
     * part of <code>installKeyboardActions</code>.
     */
    public InputMap getInputMap(int condition, JComponent c) {
        if (condition == JComponent.WHEN_FOCUSED) {
            BasicJideSplitButtonUI ui = (BasicJideSplitButtonUI) getUIOfType(
                    ((JideSplitButton) c).getUI(), BasicJideSplitButtonUI.class);
            if (ui != null) {
                return (InputMap) UIDefaultsLookup.get(ui.getPropertyPrefix() + ".focusInputMap");
            }
        }
        return null;
    }

    protected void installKeyboardActions() {
        super.installKeyboardActions();
        AbstractButton b = menuItem;

        LazyActionMap.installLazyActionMap(b, BasicJideSplitButtonUI.class,
                "JideSplitButton.actionMap");

        InputMap km = getInputMap(JComponent.WHEN_FOCUSED, b);

        SwingUtilities.replaceUIInputMap(b, JComponent.WHEN_FOCUSED, km);
    }

    protected void uninstallKeyboardActions() {
        AbstractButton b = menuItem;
        SwingUtilities.replaceUIInputMap(b, JComponent.
                WHEN_IN_FOCUSED_WINDOW, null);
        SwingUtilities.replaceUIInputMap(b, JComponent.WHEN_FOCUSED, null);
        SwingUtilities.replaceUIActionMap(b, null);
        super.uninstallKeyboardActions();
    }

    protected MouseInputListener createMouseInputListener(JComponent c) {
        return new MouseInputHandler();
    }

    protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
        ButtonModel model = menuItem.getModel();
        int menuWidth = 0;
        int menuHeight = 0;
        if (JideSwingUtilities.getOrientationOf(menuItem) == SwingConstants.HORIZONTAL) {
            menuWidth = menuItem.getWidth();
            menuHeight = menuItem.getHeight();
        }
        else {
            menuWidth = menuItem.getHeight();
            menuHeight = menuItem.getWidth();
        }

        if (!((JMenu) menuItem).isTopLevelMenu()) {
            super.paintBackground(g, menuItem, bgColor);
            if (menuItem.isEnabled()) {
                if (model.isArmed() || model.isPressed() || isMouseOver()) {
                    g.setColor(selectionForeground);
                    g.drawLine(menuWidth - _splitButtonMarginOnMenu, 0, menuWidth - _splitButtonMarginOnMenu, menuHeight - 2);
                    JideSwingUtilities.paintArrow(g, selectionForeground, menuWidth - _splitButtonMarginOnMenu / 2 - 2, menuHeight / 2 - 3, 7, SwingConstants.VERTICAL);
                }
                else {
                    g.setColor(menuItem.getForeground());
                    g.drawLine(menuWidth - _splitButtonMarginOnMenu, 0, menuWidth - _splitButtonMarginOnMenu, menuHeight - 2);
                    JideSwingUtilities.paintArrow(g, menuItem.getForeground(), menuWidth - _splitButtonMarginOnMenu / 2 - 2, menuHeight / 2 - 3, 7, SwingConstants.VERTICAL);
                }
            }
            else {
                g.setColor(UIDefaultsLookup.getColor("controlDkShadow"));
                g.drawLine(menuWidth - _splitButtonMarginOnMenu, 0, menuWidth - _splitButtonMarginOnMenu, menuHeight - 2);
                JideSwingUtilities.paintArrow(g, UIDefaultsLookup.getColor("controlDkShadow"), menuWidth - _splitButtonMarginOnMenu / 2 - 2, menuHeight / 2 - 3, 7, SwingConstants.VERTICAL);
            }
            return;
        }

        if (menuItem.isOpaque()) {
            if (menuItem.getParent() != null) {
                g.setColor(menuItem.getParent().getBackground());
            }
            else {
                g.setColor(menuItem.getBackground());
            }
            g.fillRect(0, 0, menuWidth, menuHeight);
        }

        JideSplitButton b = (JideSplitButton) menuItem;
        if (menuItem instanceof ButtonStyle && ((ButtonStyle) menuItem).getButtonStyle() == ButtonStyle.TOOLBAR_STYLE) {
            if ((menuItem instanceof JMenu && model.isSelected())) {
                // Draw a dark shadow border without bottom
                getPainter().paintSelectedMenu(menuItem, g, new Rectangle(0, 0, menuWidth, menuHeight), JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_SELECTED);
            }
            else if (model.isArmed() || model.isPressed()) {
                Rectangle rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                if (((JideSplitButton) menuItem).isButtonEnabled()) {
                    getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_PRESSED);
                }
                rect = new Rectangle(menuWidth - _splitButtonMargin - 1 + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_ROLLOVER);
            }
            else if (model instanceof SplitButtonModel && ((DefaultSplitButtonModel) model).isButtonSelected()) {
                if ((isMouseOver() || b.hasFocus()) && model.isEnabled()) {
                    Rectangle rect = new Rectangle(menuWidth - _splitButtonMargin - 1 + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                    getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_ROLLOVER);
                    rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                    if (((JideSplitButton) menuItem).isButtonEnabled()) {
                        getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_PRESSED);
                    }
                }
                else {
                    Rectangle rect = new Rectangle(menuWidth - _splitButtonMargin - 1 + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                    getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_DEFAULT);
                    rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                    getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_SELECTED);
                }
            }
            else {
                if ((isMouseOver() || b.hasFocus()) && model.isEnabled()) {
                    if (isAlwaysDropdown(menuItem)) {
                        Rectangle rect = new Rectangle(0, 0, menuWidth, menuHeight);
                        getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_ROLLOVER);
                    }
                    else {
                        // Draw a line border with background
                        Rectangle rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                        if (((JideSplitButton) menuItem).isButtonEnabled()) {
                            getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_ROLLOVER);
                        }
                        rect = new Rectangle(menuWidth - _splitButtonMargin - 1 + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                        getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_ROLLOVER);
                    }
                }
            }
        }
        else
        if (menuItem instanceof ButtonStyle && ((ButtonStyle) menuItem).getButtonStyle() == ButtonStyle.FLAT_STYLE) {
            if ((menuItem instanceof JMenu && model.isSelected())) {
                // Draw a dark shadow border without bottom
                getPainter().paintSelectedMenu(menuItem, g, new Rectangle(0, 0, menuWidth, menuHeight), JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_SELECTED);
            }
            else if (model.isArmed() || model.isPressed()) {
                Rectangle rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                if (((JideSplitButton) menuItem).isButtonEnabled()) {
                    JideSwingUtilities.paintBackground(g, rect, _highlight, _highlight);
                }
                rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                JideSwingUtilities.paintBackground(g, rect, _highlight, _highlight);

                if (!b.isOpaque()) {
                    rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                    paintSunkenBorder(g, rect);
                    rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                    paintRaisedBorder(g, rect);
                }
            }
            else if (model instanceof SplitButtonModel && ((DefaultSplitButtonModel) model).isButtonSelected()) {
                if ((isMouseOver() || b.hasFocus()) && model.isEnabled()) {
                    Rectangle rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                    JideSwingUtilities.paintBackground(g, rect, _highlight, _highlight);
                    rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                    if (((JideSplitButton) menuItem).isButtonEnabled()) {
                        JideSwingUtilities.paintBackground(g, rect, _highlight, _highlight);
                    }
                    if (!b.isOpaque()) {
                        rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                        paintSunkenBorder(g, rect);
                        rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                        paintRaisedBorder(g, rect);
                    }
                }
                else {
                    Rectangle rect = null;
                    if (b.isOpaque()) {
                        rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                        JideSwingUtilities.paintBackground(g, rect, _highlight, _highlight);
                    }
                    rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                    JideSwingUtilities.paintBackground(g, rect, _highlight, _highlight);

                    if (!b.isOpaque()) {
                        rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                        paintSunkenBorder(g, rect);
//                        rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
//                        paintRaisedBorder(g, rect);
                    }
                }

            }
            else {
                if ((isMouseOver() || b.hasFocus()) && model.isEnabled()) {
                    // Draw a line border with background
                    Rectangle rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                    if (((JideSplitButton) menuItem).isButtonEnabled()) {
                        JideSwingUtilities.paintBackground(g, rect, _highlight, _highlight);
                    }
                    rect = new Rectangle(menuWidth - _splitButtonMargin - getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                    JideSwingUtilities.paintBackground(g, rect, _highlight, _highlight);

                    rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                    paintRaisedBorder(g, rect);
                    rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                    paintRaisedBorder(g, rect);
                }
                else {
                    if (b.isOpaque()) {
                        Rectangle rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                        if (((JideSplitButton) menuItem).isButtonEnabled()) {
                            getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_DEFAULT);
                        }
                        rect = new Rectangle(menuWidth - _splitButtonMargin - 1 + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                        getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_DEFAULT);
                    }
                }
            }
        }
        else
        if (menuItem instanceof ButtonStyle && ((ButtonStyle) menuItem).getButtonStyle() == ButtonStyle.TOOLBOX_STYLE) {
            if ((menuItem instanceof JMenu && model.isSelected())) {
                // Draw a dark shadow border without bottom
                getPainter().paintSelectedMenu(menuItem, g, new Rectangle(0, 0, menuWidth, menuHeight), JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_SELECTED);
            }
            else if (model.isArmed() || model.isPressed()) {
                Rectangle rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                if (((JideSplitButton) menuItem).isButtonEnabled()) {
                    getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_PRESSED);
                }
                rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_ROLLOVER);

                if (!b.isOpaque()) {
                    rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                    paintSunken2Border(g, rect);
                    rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                    paintRaisedBorder(g, rect);
                }
            }
            else if (model instanceof SplitButtonModel && ((DefaultSplitButtonModel) model).isButtonSelected()) {
                if (isMouseOver() && model.isEnabled()) {
                    Rectangle rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                    getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_ROLLOVER);
                    rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                    if (((JideSplitButton) menuItem).isButtonEnabled()) {
                        getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_PRESSED);
                    }
                    if (!b.isOpaque()) {
                        rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                        paintSunken2Border(g, rect);
                        rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                        paintRaisedBorder(g, rect);
                    }
                }
                else {
                    Rectangle rect = null;
                    if (b.isOpaque()) {
                        rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                        getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_DEFAULT);
                    }
                    rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                    getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_SELECTED);

                    if (!b.isOpaque()) {
                        rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                        paintSunken2Border(g, rect);
                        rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                        paintRaisedBorder(g, rect);
                    }
                }

            }
            else {
                if (isMouseOver() && model.isEnabled()) {
                    // Draw a line border with background
                    Rectangle rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                    if (((JideSplitButton) menuItem).isButtonEnabled()) {
                        getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_ROLLOVER);
                    }
                    rect = new Rectangle(menuWidth - _splitButtonMargin - getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                    getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_ROLLOVER);

                    rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                    paintRaised2Border(g, rect);
                    rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                    paintRaised2Border(g, rect);
                }
                else {
                    if (b.isOpaque()) {
                        Rectangle rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                        if (((JideSplitButton) menuItem).isButtonEnabled()) {
                            getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_DEFAULT);
                        }
                        rect = new Rectangle(menuWidth - _splitButtonMargin - 1 + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                        getPainter().paintButtonBackground(menuItem, g, rect, JideSwingUtilities.getOrientationOf(menuItem), ThemePainter.STATE_DEFAULT);
                    }
                    else {
                        Rectangle rect = new Rectangle(0, 0, menuWidth - _splitButtonMargin, menuHeight);
                        paintRaisedBorder(g, rect);
                        rect = new Rectangle(menuWidth - _splitButtonMargin + getOffset(), 0, _splitButtonMargin - getOffset(), menuHeight);
                        paintRaisedBorder(g, rect);
                    }
                }
            }
        }

        if (menuItem.isEnabled()) {
            JideSwingUtilities.paintArrow(g, menuItem.getForeground(), menuWidth - 9, menuHeight / 2 - 1, 5, SwingConstants.HORIZONTAL);
        }
        else {
            JideSwingUtilities.paintArrow(g, UIDefaultsLookup.getColor("controlShadow"), menuWidth - 9, menuHeight / 2 - 1, 5, SwingConstants.HORIZONTAL);
        }
    }

    private void paintSunkenBorder(Graphics g, Rectangle b) {
        Color old = g.getColor();
        g.setColor(_shadowColor);    // inner 3D border
        g.drawLine(b.x, b.y, b.x + b.width - 1, b.y);
        g.drawLine(b.x, b.y, b.x, b.y + b.height - 1);

        g.setColor(_lightHighlightColor);     // black drop shadow  __|
        g.drawLine(b.x, b.y + b.height - 1, b.x + b.width - 1, b.y + b.height - 1);
        g.drawLine(b.x + b.width - 1, b.y, b.x + b.width - 1, b.y + b.height - 1);
        g.setColor(old);
    }

    private void paintSunken2Border(Graphics g, Rectangle b) {
        Color old = g.getColor();
        g.setColor(_darkShadowColor);    // inner 3D border
        g.drawLine(b.x, b.y, b.x + b.width - 2, b.y);
        g.drawLine(b.x, b.y, b.x, b.y + b.height - 2);

        g.setColor(_shadowColor);    // inner 3D border
        g.drawLine(b.x + 1, b.y + 1, b.x + b.width - 3, b.y + 1);
        g.drawLine(b.x + 1, b.y + 1, b.x + 1, b.y + b.height - 3);

        g.setColor(_lightHighlightColor);     // black drop shadow  __|
        g.drawLine(b.x, b.y + b.height - 1, b.x + b.width - 1, b.y + b.height - 1);
        g.drawLine(b.x + b.width - 1, b.x, b.x + b.width - 1, b.y + b.height - 1);
        g.setColor(old);
    }

    private void paintRaised2Border(Graphics g, Rectangle b) {
        Color old = g.getColor();
        g.setColor(_lightHighlightColor);    // inner 3D border
        g.drawLine(b.x, b.y, b.x + b.width - 1, b.y);
        g.drawLine(b.x, b.y, b.x, b.y + b.height - 1);

        g.setColor(_shadowColor);     // gray drop shadow  __|
        g.drawLine(b.x + 1, b.y + b.height - 2, b.x + b.width - 2, b.y + b.height - 2);
        g.drawLine(b.x + b.width - 2, 1, b.x + b.width - 2, b.y + b.height - 2);

        g.setColor(_darkShadowColor);     // black drop shadow  __|
        g.drawLine(b.x, b.y + b.height - 1, b.x + b.width - 1, b.y + b.height - 1);
        g.drawLine(b.x + b.width - 1, b.y, b.x + b.width - 1, b.y + b.height - 1);
        g.setColor(old);
    }

    private void paintRaisedBorder(Graphics g, Rectangle b) {
        Color old = g.getColor();
        g.setColor(_lightHighlightColor);    // inner 3D border
        g.drawLine(b.x, b.y, b.x + b.width - 1, b.y);
        g.drawLine(b.x, b.y, b.x, b.y + b.height - 1);

        g.setColor(_shadowColor);     // black drop shadow  __|
        g.drawLine(b.x, b.y + b.height - 1, b.x + b.width - 1, b.y + b.height - 1);
        g.drawLine(b.x + b.width - 1, b.y, b.x + b.width - 1, b.y + b.height - 1);
        g.setColor(old);
    }

    protected class MouseInputHandler implements MouseInputListener {
        public void mouseClicked(MouseEvent e) {
            cancelMenuIfNecessary(e);
        }

        /**
         * Invoked when the mouse has been clicked on the menu. This
         * method clears or sets the selection path of the
         * MenuSelectionManager.
         *
         * @param e the mouse event
         */
        public void mousePressed(MouseEvent e) {
            JMenu menu = (JMenu) menuItem;
            if (!menu.isEnabled())
                return;

            setMouseOver(true);

            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            if (isClickOnButton(e, menu)) {
                if (((JideSplitButton) menuItem).isButtonEnabled()) {
                    // click button
                    menu.getModel().setArmed(true);
                    menu.getModel().setPressed(true);
                }
                if (!menu.hasFocus() && menu.isRequestFocusEnabled()) {
                    menu.requestFocus();
                }
            }
            else {
                downButtonPressed(menu);
            }
        }

        private boolean isClickOnButton(MouseEvent e, JMenu menu) {
            if (((JideSplitButton) menu).isAlwaysDropdown()) {
                return false;
            }

            boolean clickOnDropDown = false;
            int size = ((JMenu) menuItem).isTopLevelMenu() ? _splitButtonMargin : _splitButtonMarginOnMenu;
            if (JideSwingUtilities.getOrientationOf(menuItem) == SwingConstants.HORIZONTAL) {
                if (e.getPoint().getX() < menu.getWidth() - size) {
                    clickOnDropDown = true;
                }
            }
            else {
                if (e.getPoint().getY() < menu.getHeight() - size) {
                    clickOnDropDown = true;
                }
            }
            return clickOnDropDown;
        }

        /**
         * Invoked when the mouse has been released on the menu. Delegates the
         * mouse event to the MenuSelectionManager.
         *
         * @param e the mouse event
         */
        public void mouseReleased(MouseEvent e) {
            if (!isMouseOver()) {
                // these two lines order matters. In this order, it would not trigger actionPerformed.
                menuItem.getModel().setArmed(false);
                menuItem.getModel().setPressed(false);
            }
            cancelMenuIfNecessary(e);
        }

        private void cancelMenuIfNecessary(MouseEvent e) {
            JMenu menu = (JMenu) menuItem;
            if (!menu.isEnabled())
                return;
            if (isClickOnButton(e, menu)) {
                if (((JideSplitButton) menuItem).isButtonEnabled()) {
                    // click button
                    // these two lines order matters. In this order, it would trigger actionPerformed.
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        menu.getModel().setPressed(false);
                        menu.getModel().setArmed(false);
                    }
                    else {
                        menu.getModel().setArmed(false);
                        menu.getModel().setPressed(false);
                    }

                    MenuSelectionManager manager = MenuSelectionManager.defaultManager();
                    MenuElement[] menuElements = manager.getSelectedPath();
                    for (int i = menuElements.length - 1; i >= 0; i--) {
                        MenuElement menuElement = menuElements[i];
                        if (menuElement instanceof JPopupMenu && ((JPopupMenu) menuElement).isAncestorOf(menu)) {
                            menu.getModel().setRollover(false);
                            setMouseOver(false);
                            manager.clearSelectedPath();
                        }
                    }
                }
            }
            else {
//                MenuSelectionManager manager =
//                        MenuSelectionManager.defaultManager();
//                manager.processMouseEvent(e);
//                if (!e.isConsumed())
//                    manager.clearSelectedPath();
            }
        }

        /**
         * Invoked when the cursor enters the menu. This method sets the selected
         * path for the MenuSelectionManager and handles the case
         * in which a menu item is used to pop up an additional menu, as in a
         * hierarchical menu system.
         *
         * @param e the mouse event; not used
         */
        public void mouseEntered(MouseEvent e) {
            JMenu menu = (JMenu) menuItem;
            if (!menu.isEnabled())
                return;

            MenuSelectionManager manager =
                    MenuSelectionManager.defaultManager();
            MenuElement selectedPath[] = manager.getSelectedPath();
            if (!menu.isTopLevelMenu()) {
                if (!(selectedPath.length > 0 &&
                        selectedPath[selectedPath.length - 1] ==
                                menu.getPopupMenu())) {
                    if (menu.getDelay() == 0) {
                        appendPath(getPath(), menu.getPopupMenu());
                    }
                    else {
                        manager.setSelectedPath(getPath());
                        setupPostTimer(menu);
                    }
                }
            }
            else {
                if (selectedPath.length > 0 &&
                        selectedPath[0] == menu.getParent()) {
                    MenuElement newPath[] = new MenuElement[3];
                    // A top level menu's parent is by definition
                    // a JMenuBar
                    newPath[0] = (MenuElement) menu.getParent();
                    newPath[1] = menu;
                    newPath[2] = menu.getPopupMenu();
                    manager.setSelectedPath(newPath);
                }
            }

            if (!SwingUtilities.isLeftMouseButton(e)) {
                setMouseOver(true);
            }
            menuItem.repaint();
        }

        public void mouseExited(MouseEvent e) {
            setMouseOver(false);
            menuItem.repaint();
        }

        /**
         * Invoked when a mouse button is pressed on the menu and then dragged.
         * Delegates the mouse event to the MenuSelectionManager.
         *
         * @param e the mouse event
         * @see java.awt.event.MouseMotionListener#mouseDragged
         */
        public void mouseDragged(MouseEvent e) {
            JMenu menu = (JMenu) menuItem;
            if (!menu.isEnabled())
                return;
            MenuSelectionManager.defaultManager().processMouseEvent(e);
        }

        public void mouseMoved(MouseEvent e) {
        }
    }

    public Dimension getPreferredSize(JComponent c) {
        if (!(c instanceof JMenu) || !((JMenu) c).isTopLevelMenu()) {
            return super.getPreferredSize(c);
        }

        AbstractButton b = (AbstractButton) c;

        boolean isHorizontal = true;
        if (JideSwingUtilities.getOrientationOf(c) == SwingConstants.VERTICAL) {
            isHorizontal = false;
        }

        // JDK PORTING HINT
        // JDK1.3: No getIconTextGap, use defaultTextIconGap
        Dimension d = BasicGraphicsUtils.getPreferredButtonSize(b, defaultTextIconGap);
//        d.width += b.getMargin().left + b.getMargin().right;
//        d.height += b.getMargin().bottom + b.getMargin().top;

        int size = ((JMenu) menuItem).isTopLevelMenu() ? _splitButtonMargin : _splitButtonMarginOnMenu;
        d.width += size;

        if (isHorizontal)
            return d;
        else
            return new Dimension(d.height, d.width); // swap width and height
    }

    public Dimension getMinimumSize(JComponent c) {
        if (!(c instanceof JMenu) || !((JMenu) c).isTopLevelMenu()) {
            return super.getMinimumSize(c);
        }

        Dimension d = getPreferredSize(c);
        View v = (View) c.getClientProperty(BasicHTML.propertyKey);
        if (v != null) {
            if (JideSwingUtilities.getOrientationOf(c) == SwingConstants.HORIZONTAL)
                d.width -= v.getPreferredSpan(View.X_AXIS) - v.getMinimumSpan(View.X_AXIS);
            else        // TODO: not sure if this is correct
                d.height -= v.getPreferredSpan(View.X_AXIS) - v.getMinimumSpan(View.X_AXIS);
        }

        return d;
    }

    public Dimension getMaximumSize(JComponent c) {
        if (!(c instanceof JMenu) || !((JMenu) c).isTopLevelMenu()) {
            return super.getMaximumSize(c);
        }

        Dimension d = getPreferredSize(c);
        View v = (View) c.getClientProperty(BasicHTML.propertyKey);
        if (v != null) {
            if (JideSwingUtilities.getOrientationOf(c) == SwingConstants.HORIZONTAL)
                d.width += v.getMaximumSpan(View.X_AXIS) - v.getPreferredSpan(View.X_AXIS);
            else        // TODO: not sure if this is correct
                d.height += v.getMaximumSpan(View.X_AXIS) - v.getPreferredSpan(View.X_AXIS);
        }

        return d;
    }

    protected String layoutMenuItem1(FontMetrics fm,
                                     String text,
                                     FontMetrics fmAccel,
                                     String acceleratorText,
                                     Icon icon,
                                     Icon checkIcon,
                                     Icon arrowIcon,
                                     int verticalAlignment,
                                     int horizontalAlignment,
                                     int verticalTextPosition,
                                     int horizontalTextPosition,
                                     Rectangle viewRect,
                                     Rectangle iconRect,
                                     Rectangle textRect,
                                     Rectangle acceleratorRect,
                                     Rectangle checkIconRect,
                                     Rectangle arrowIconRect,
                                     int textIconGap,
                                     int menuItemGap) {
        textRect.width -= _splitButtonMargin;
        text = JideSwingUtilities.layoutCompoundLabel(menuItem, fm, text, icon, JideSwingUtilities.getOrientationOf(menuItem) == SwingConstants.HORIZONTAL,
                verticalAlignment, horizontalAlignment, verticalTextPosition,
                horizontalTextPosition, viewRect, iconRect, textRect,
                textIconGap);

        textIconGap = defaultTextIconGap;

        // get viewRect which is the bounds of menuitem
        viewRect.x = viewRect.y = 0;
        if (JideSwingUtilities.getOrientationOf(menuItem) == SwingConstants.HORIZONTAL) {
            //   Dimension size = b.getSize();
            viewRect.height = menuItem.getHeight();
            viewRect.width = menuItem.getWidth();
        }
        else {
            viewRect.height = menuItem.getWidth();
            viewRect.width = menuItem.getHeight();
        }

        if ((text == null) || text.equals("")) {
            textRect.width = textRect.height = 0;
            text = "";
        }
        else {
            textRect.width = SwingUtilities.computeStringWidth(fm, text);
            textRect.height = fm.getHeight();
        }

        if (icon == null) {
            if (useCheckAndArrow())
                iconRect.width = iconRect.height = 16;
            else
                iconRect.width = iconRect.height = 0;
        }
        else {
            iconRect.width = icon.getIconWidth();
            iconRect.height = icon.getIconHeight();
        }

        if (menuItem.getComponentOrientation().isLeftToRight()) {
            if (icon != null) {
                iconRect.x = 3;
                textRect.x = iconRect.x + iconRect.width + textIconGap;
            }
            else {
                iconRect.x = 0;
                textRect.x = 3;
            }
            iconRect.y = (viewRect.height - iconRect.height) / 2;
            textRect.y = (viewRect.height - textRect.height) / 2;
        }
        else {
            if (icon != null) {
                iconRect.y = 3;
                textRect.y = iconRect.y + iconRect.height + textIconGap;
            }
            else {
                iconRect.y = 0;
                textRect.y = 3;
            }
            iconRect.x = (viewRect.width - iconRect.width) / 2;
            textRect.x = (viewRect.width - textRect.width) / 2;
        }

//        System.out.println("Layout: text=" + menuItem.getText() + "\n\tv="
//                + viewRect + "\n\tc=" + checkIconRect + "\n\ti="
//                + iconRect + "\n\tt=" + textRect + "\n\tacc="
//                + acceleratorRect + "\n\ta=" + arrowIconRect + "\n");


        return text;
    }

    protected void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text) {
        // Note: This method is almost identical to the same method in WindowsMenuItemUI
        ButtonModel model = menuItem.getModel();

        if (!(menuItem instanceof JMenu) || !((JMenu) menuItem).isTopLevelMenu()) {
            if (menuItem.getComponentOrientation().isLeftToRight()) {
                int defaultTextIconGap = UIDefaultsLookup.getInt("MenuItem.textIconGap");
                int defaultShadowWidth = UIDefaultsLookup.getInt("MenuItem.shadowWidth");
                textRect.x = defaultShadowWidth + defaultTextIconGap;
            }
            else {
                // isLeftToRight is false
            }
        }

        FontMetrics fm = g.getFontMetrics();
        int mnemonicIndex = menuItem.getDisplayedMnemonicIndex();
        // W2K Feature: Check to see if the Underscore should be rendered.
        if (WindowsLookAndFeel.isMnemonicHidden()) {
            mnemonicIndex = -1;
        }

        Color oldColor = g.getColor();

        if (!model.isEnabled() || !((JideSplitButton) menuItem).isButtonEnabled()) {
            // *** paint the text disabled
            g.setColor(menuItem.getBackground().brighter());

            // JDK PORTING HINT
            // JDK1.3: No drawStringUnderlineCharAt, draw the string then draw the underline
            JideSwingUtilities.drawStringUnderlineCharAt(menuItem, g, text, mnemonicIndex,
                    textRect.x, textRect.y + fm.getAscent());
            g.setColor(menuItem.getBackground().darker());

            // JDK PORTING HINT
            // JDK1.3: No drawStringUnderlineCharAt, draw the string then draw the underline
            JideSwingUtilities.drawStringUnderlineCharAt(menuItem, g, text, mnemonicIndex,
                    textRect.x - 1, textRect.y + fm.getAscent() - 1);
        }
        else {
            // For Win95, the selected text color is the selection forground color
            if (model.isSelected()) {
                if (/*SystemInfo.isClassicWindows() ||
                        */!((JMenu) menuItem).isTopLevelMenu()) {

                    g.setColor(selectionForeground); // Uses protected field.
                }
            }
            else {
                int state = JideSwingUtilities.getButtonState(menuItem);
                Color foreground = null;
                if (menuItem instanceof ComponentStateSupport) {
                    foreground = ((ComponentStateSupport) menuItem).getForegroundOfState(state);
                }
                if (foreground == null || foreground instanceof UIResource) {
                    foreground = menuItem.getForeground();
                }
                g.setColor(foreground);
            }
            JideSwingUtilities.drawStringUnderlineCharAt(menuItem, g, text,
                    mnemonicIndex,
                    textRect.x,
                    textRect.y + fm.getAscent());
        }
        g.setColor(oldColor);
    }

    protected void paintIcon(JMenuItem b, Graphics g) {
        ButtonModel model = b.getModel();

        // Paint the Icon
        if (b.getIcon() != null) {
//            if (JideSwingUtilities.getOrientationOf(b) == SwingConstants.VERTICAL) {
//                ((Graphics2D) g).translate(0, b.getWidth() - 1);
//                ((Graphics2D) g).rotate(-Math.PI / 2);
//            }

            Icon icon = getIcon(b);

            if (icon != null) {
                boolean enabled = model.isEnabled() && (model instanceof SplitButtonModel ? ((SplitButtonModel) model).isButtonEnabled() : true);
                if (isFloatingIcon() && enabled) {
                    if (model.isRollover() && !model.isPressed() && !model.isSelected()) {
                        if (!"true".equals(SecurityUtils.getProperty("shadingtheme", "false"))) {
                            if (icon instanceof ImageIcon) {
                                ImageIcon shadow = IconsFactory.createGrayImage(((ImageIcon) icon).getImage());
                                shadow.paintIcon(b, g, iconRect.x + 1, iconRect.y + 1);
                            }
                            else {
                                ImageIcon shadow = IconsFactory.createGrayImage(b, icon);
                                shadow.paintIcon(b, g, iconRect.x + 1, iconRect.y + 1);
                            }
                            icon.paintIcon(b, g, iconRect.x - 1, iconRect.y - 1);
                        }
                        else {
                            icon.paintIcon(b, g, iconRect.x, iconRect.y);
                        }
                    }
                    else {
                        icon.paintIcon(b, g, iconRect.x, iconRect.y);
                    }
                }
                else {
                    icon.paintIcon(b, g, iconRect.x, iconRect.y);
                }
            }

//            if (JideSwingUtilities.getOrientationOf(b) == SwingConstants.VERTICAL) {
//                ((Graphics2D) g).rotate(Math.PI / 2);
//                ((Graphics2D) g).translate(0, -b.getHeight() + 1);
//            }
        }
    }

    protected boolean isFloatingIcon() {
        return _isFloatingIcon;
    }

    protected Icon getIcon(AbstractButton b) {
        ButtonModel model = b.getModel();
        Icon icon = b.getIcon();
        Icon tmpIcon = null;
        if (!model.isEnabled() || !((JideSplitButton) menuItem).isButtonEnabled()) {
            if (model.isSelected()) {
                tmpIcon = (Icon) b.getDisabledSelectedIcon();
            }
            else {
                tmpIcon = (Icon) b.getDisabledIcon();
            }

            // create default diabled icon
            if (tmpIcon == null) {
                if (icon instanceof ImageIcon) {
                    icon = IconsFactory.createGrayImage(((ImageIcon) icon).getImage());
                }
                else {
                    icon = IconsFactory.createGrayImage(b, icon);
                }
            }
        }
        else if (model.isPressed() && model.isArmed()) {
            tmpIcon = (Icon) b.getPressedIcon();
            if (tmpIcon != null) {
                // revert back to 0 offset
                // clearTextShiftOffset();
            }
        }
        else if (b.isRolloverEnabled() && model.isRollover()) {
            if (model.isSelected()) {
                tmpIcon = (Icon) b.getRolloverSelectedIcon();
            }
            else {
                tmpIcon = (Icon) b.getRolloverIcon();
            }
        }
        else if (model.isSelected()) {
            tmpIcon = (Icon) b.getSelectedIcon();
        }

        if (tmpIcon != null) {
            icon = tmpIcon;
        }
        return icon;
    }

    /**
     * The gap between the button part and the drop down menu part.
     *
     * @return the gap.
     */
    protected int getOffset() {
        return 0;
    }

    protected boolean isAlwaysDropdown(JMenuItem menuItem) {
        if (menuItem instanceof JideSplitButton) {
            return ((JideSplitButton) menuItem).isAlwaysDropdown();
        }
        else {
            return false;
        }
    }

    protected int getRightMargin() {
        return _splitButtonMargin - 1;
    }

    /**
     * Actions for Buttons. Two type of action are supported:
     * pressed: Moves the button to a pressed state
     * released: Disarms the button.
     */
    private static class Actions extends UIAction {
        private static final String PRESS = "pressed";
        private static final String RELEASE = "released";
        private static final String DOWN_PRESS = "downPressed";
        private static final String DOWN_RELEASE = "downReleased";

        Actions(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            AbstractButton b = (AbstractButton) e.getSource();
            String key = getName();

            // if isAlwaysDropDown it true, treat PRESS as DOWN_PRESS
            if (PRESS.equals(key) && ((JideSplitButton) b).isAlwaysDropdown()) {
                key = DOWN_PRESS;
            }

            if (PRESS.equals(key)) {
                ButtonModel model = b.getModel();
                model.setArmed(true);
                model.setPressed(true);
                if (!b.hasFocus()) {
                    b.requestFocus();
                }
            }
            else if (RELEASE.equals(key)) {
                ButtonModel model = b.getModel();
                model.setPressed(false);
                model.setArmed(false);
            }
            else if (DOWN_PRESS.equals(key)) {
                downButtonPressed((JMenu) b);
            }
            else if (DOWN_RELEASE.equals(key)) {
            }
        }

        public boolean isEnabled(Object sender) {
            if (sender != null && (sender instanceof AbstractButton) &&
                    !((AbstractButton) sender).getModel().isEnabled()) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    /**
     * Populates Buttons actions.
     */
    public static void loadActionMap(LazyActionMap map) {
        map.put(new Actions(Actions.PRESS));
        map.put(new Actions(Actions.RELEASE));
        map.put(new Actions(Actions.DOWN_PRESS));
        map.put(new Actions(Actions.DOWN_RELEASE));
    }

    protected static void downButtonPressed(JMenu menu) {
        MenuSelectionManager manager = MenuSelectionManager.defaultManager();
        if (menu.isTopLevelMenu()) {
            if (menu.isSelected()) {
                manager.clearSelectedPath();
            }
            else {
                Container cnt = menu.getParent();
                if (cnt != null && cnt instanceof MenuElement) {
                    ArrayList parents = new ArrayList();
                    while (cnt instanceof MenuElement) {
                        parents.add(0, cnt);
                        if (cnt instanceof JPopupMenu) {
                            cnt = (Container) ((JPopupMenu) cnt).getInvoker();
                        }
                        else {
                            cnt = cnt.getParent();
                        }
                    }

                    MenuElement me[] = new MenuElement[parents.size() + 1];
                    for (int i = 0; i < parents.size(); i++) {
                        Container container = (Container) parents.get(i);
                        me[i] = (MenuElement) container;
                    }
                    me[parents.size()] = menu;
                    manager.setSelectedPath(me);
                }
                else {
                    MenuElement me[] = new MenuElement[1];
                    me[0] = menu;
                    manager.setSelectedPath(me);
                }
            }
        }

        MenuElement selectedPath[] = manager.getSelectedPath();
        if (selectedPath.length > 0 &&
                selectedPath[selectedPath.length - 1] != menu.getPopupMenu()) {
            if (menu.isTopLevelMenu() ||
                    menu.getDelay() == 0) {
                appendPath(selectedPath, menu.getPopupMenu());
            }
            else {
                setupPostTimer(menu);
            }
        }
    }
}


