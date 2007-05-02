/*
 * @(#)JidePopupMenu.java
 *
 * Copyright 2002 JIDE Software. All rights reserved.
 */
package com.jidesoft.swing;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.utils.PortingUtils;

import javax.swing.*;
import javax.swing.plaf.PopupMenuUI;
import java.awt.*;

/**
 * This component extends JPopupMenu and adds a method to
 * display the menu inside the screen even if the mouse
 * pointer is near the edge of the screen.
 */
public class JidePopupMenu extends JPopupMenu implements Scrollable {

    private static final String uiClassID = "JidePopupMenuUI";

    /**
     * Constructs a <code>JPopupMenu</code> without an "invoker".
     */
    public JidePopupMenu() {
    }

    /**
     * Constructs a <code>JPopupMenu</code> with the specified title.
     *
     * @param label the string that a UI may use to display as a title
     *              for the popup menu.
     */
    public JidePopupMenu(String label) {
        super(label);
    }

    public String getUIClassID() {
        return uiClassID;
    }

    public void updateUI() {
        if (UIDefaultsLookup.get(uiClassID) == null) {
            LookAndFeelFactory.installJideExtension();
        }
        setUI((PopupMenuUI) UIManager.getUI(this));
    }

    /**
     * Displays the PopUpMenu at a specified position.
     *
     * @param invoker the component that triggers the event
     * @param x       mouse X position on screen
     * @param y       mouse Y position on screen
     */
    public void show(Component invoker, int x, int y) {
        Point p = getPopupMenuOrigin(invoker, x, y);
        super.show(invoker, p.x, p.y);
    }


    /**
     * Figures out the sizes needed to calculate the menu position.
     *
     * @param invoker the component that triggers the event
     * @param x       mouse X position on screen
     * @param y       mouse Y position on screen
     * @return new position
     */
    protected Point getPopupMenuOrigin(Component invoker, int x, int y) {

        Rectangle bounds = PortingUtils.getScreenBounds(invoker);

        Dimension size = this.getSize();

        if (size.width == 0) {
            size = this.getPreferredSize();
        }

        Point p = new Point(x, y);
        SwingUtilities.convertPointToScreen(p, invoker);
        int left = p.x + size.width;
        int bottom = p.y + size.height;

        if (x < bounds.x) {
            x = bounds.x;
        }
        if (left > bounds.width) {
            x -= size.width;
        }

        if (bottom > bounds.height) {
            y -= size.height;
        }

        if (y < bounds.y) {
            y = bounds.y;
        }

        return new Point(x, y);
    }

    public void setLocation(int x, int y) {
        // TODO: this is really a hack. Two classes will call this method. One is when the JPopupMenu is show. The other
        if (isVisible() && y <= 0) {
            move(x, y); // cannot call setLocation because it will be recursive call. So call deprecated move in order to bypass this.
        }
        else {
            super.setLocation(x, y);
        }
    }

    public Dimension getPreferredScrollableViewportSize() {
        Dimension size = getPreferredSize();
        Dimension screenSize = PortingUtils.getLocalScreenSize(this);
        Container container = SwingUtilities.getAncestorOfClass(SimpleScrollPane.class, this);
        if (container instanceof SimpleScrollPane) {
            SimpleScrollPane scrollPane = (SimpleScrollPane) container;
            size.height = Math.min(size.height, screenSize.height - scrollPane.getScrollUpButton().getPreferredSize().height - scrollPane.getScrollDownButton().getPreferredSize().height);
        }
        return size;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return new JMenuItem("ABC").getPreferredSize().height;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return new JMenuItem("ABC").getPreferredSize().height * 5;
    }

    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}