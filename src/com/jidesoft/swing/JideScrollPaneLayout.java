/*
 * @(#)${NAME}.java
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */
package com.jidesoft.swing;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * The layout manager used by <code>JideScrollPane</code>.
 * <code>JideScrollPaneLayout</code> is
 * responsible for eleven components: a viewport, two scrollbars,
 * a row header, a column header, a row footer, a column footer, and four "corner" components.
 */
class JideScrollPaneLayout extends ScrollPaneLayout implements JideScrollPaneConstants {
    /**
     * The row footer child.  Default is <code>null</code>.
     *
     * @see JideScrollPane#setRowFooter
     */
    protected JViewport _rowFoot;


    /**
     * The column footer child.  Default is <code>null</code>.
     *
     * @see JideScrollPane#setColumnFooter
     */
    protected JViewport _colFoot;

    /**
     * The component to the left of horizontal scroll bar.
     */
    protected Component _hLeft;
    /**
     * The component to the right of horizontal scroll bar.
     */
    protected Component _hRight;

    /**
     * The component to the top of vertical scroll bar.
     */
    protected Component _vTop;

    /**
     * The component to the bottom of vertical scroll bar.
     */
    protected Component _vBottom;

    public void syncWithScrollPane(JScrollPane sp) {
        super.syncWithScrollPane(sp);
        if (sp instanceof JideScrollPane) {
            _rowFoot = ((JideScrollPane) sp).getRowFooter();
            _colFoot = ((JideScrollPane) sp).getColumnFooter();
            _hLeft = ((JideScrollPane) sp).getScrollBarCorner(HORIZONTAL_LEFT);
            _hRight = ((JideScrollPane) sp).getScrollBarCorner(HORIZONTAL_RIGHT);
            _vTop = ((JideScrollPane) sp).getScrollBarCorner(VERTICAL_TOP);
            _vBottom = ((JideScrollPane) sp).getScrollBarCorner(VERTICAL_BOTTOM);
        }
    }

    protected boolean isHsbCoversWholeWidth(JScrollPane sp) {
        if (sp instanceof JideScrollPane) {
            return ((JideScrollPane) sp).isHorizontalScrollBarCoversWholeWidth();
        }
        else {
            return false;
        }
    }

    protected boolean isVsbCoversWholeHeight(JScrollPane sp) {
        if (sp instanceof JideScrollPane) {
            return ((JideScrollPane) sp).isVerticalScrollBarCoversWholeHeight();
        }
        else {
            return false;
        }
    }

    public void addLayoutComponent(String s, Component c) {
        if (s.equals(ROW_FOOTER)) {
            _rowFoot = (JViewport) addSingletonComponent(_rowFoot, c);
        }
        else if (s.equals(COLUMN_FOOTER)) {
            _colFoot = (JViewport) addSingletonComponent(_colFoot, c);
        }
        else if (s.equals(HORIZONTAL_LEFT)) {
            _hLeft = addSingletonComponent(_hLeft, c);
        }
        else if (s.equals(HORIZONTAL_RIGHT)) {
            _hRight = addSingletonComponent(_hRight, c);
        }
        else if (s.equals(VERTICAL_TOP)) {
            _vTop = addSingletonComponent(_vTop, c);
        }
        else if (s.equals(VERTICAL_BOTTOM)) {
            _vBottom = addSingletonComponent(_vBottom, c);
        }
        else {
            super.addLayoutComponent(s, c);
        }
    }

    public void removeLayoutComponent(Component c) {
        if (c == _rowFoot) {
            _rowFoot = null;
        }
        else if (c == _colFoot) {
            _colFoot = null;
        }
        else if (c == _hLeft) {
            _hLeft = null;
        }
        else if (c == _hRight) {
            _hRight = null;
        }
        else if (c == _vTop) {
            _vTop = null;
        }
        else if (c == _vBottom) {
            _vBottom = null;
        }
        else {
            super.removeLayoutComponent(c);
        }
    }

    /**
     * Returns the <code>JViewport</code> object that is the row footer.
     *
     * @return the <code>JViewport</code> object that is the row footer
     * @see JideScrollPane#getRowFooter
     */
    public JViewport getRowFooter() {
        return _rowFoot;
    }

    /**
     * Returns the <code>JViewport</code> object that is the column footer.
     *
     * @return the <code>JViewport</code> object that is the column footer
     * @see JideScrollPane#getColumnFooter
     */
    public JViewport getColumnFooter() {
        return _colFoot;
    }

    /**
     * Returns the <code>Component</code> at the specified corner.
     *
     * @param key the <code>String</code> specifying the corner
     * @return the <code>Component</code> at the specified corner, as defined in
     *         {@link ScrollPaneConstants}; if <code>key</code> is not one of the
     *         four corners, <code>null</code> is returned
     * @see JScrollPane#getCorner
     */
    public Component getScrollBarCorner(String key) {
        if (key.equals(HORIZONTAL_LEFT)) {
            return _hLeft;
        }
        else if (key.equals(HORIZONTAL_RIGHT)) {
            return _hRight;
        }
        else if (key.equals(VERTICAL_BOTTOM)) {
            return _vBottom;
        }
        else if (key.equals(VERTICAL_TOP)) {
            return _vTop;
        }
        else {
            return super.getCorner(key);
        }
    }

    /**
     * The preferred size of a <code>ScrollPane</code> is the size of the insets,
     * plus the preferred size of the viewport, plus the preferred size of
     * the visible headers, plus the preferred size of the scrollbars
     * that will appear given the current view and the current
     * scrollbar displayPolicies.
     * <p>Note that the rowHeader is calculated as part of the preferred width
     * and the colHeader is calculated as part of the preferred size.
     *
     * @param parent the <code>Container</code> that will be laid out
     * @return a <code>Dimension</code> object specifying the preferred size of the
     *         viewport and any scrollbars
     * @see ViewportLayout
     * @see LayoutManager
     */
    public Dimension preferredLayoutSize(Container parent) {
        /* Sync the (now obsolete) policy fields with the
         * JScrollPane.
         */
        JScrollPane scrollPane = (JScrollPane) parent;
        vsbPolicy = scrollPane.getVerticalScrollBarPolicy();
        hsbPolicy = scrollPane.getHorizontalScrollBarPolicy();

        Insets insets = parent.getInsets();
        int prefWidth = insets.left + insets.right;
        int prefHeight = insets.top + insets.bottom;

        /* Note that viewport.getViewSize() is equivalent to
         * viewport.getView().getPreferredSize() modulo a null
         * view or a view whose size was explicitly set.
         */

        Dimension extentSize = null;
        Dimension viewSize = null;
        Component view = null;

        if (viewport != null) {
            extentSize = viewport.getPreferredSize();
            viewSize = viewport.getViewSize();
            view = viewport.getView();
        }

        /* If there's a viewport add its preferredSize.
         */

        if (extentSize != null) {
            prefWidth += extentSize.width;
            prefHeight += extentSize.height;
        }

        /* If there's a JScrollPane.viewportBorder, add its insets.
         */

        Border viewportBorder = scrollPane.getViewportBorder();
        if (viewportBorder != null) {
            Insets vpbInsets = viewportBorder.getBorderInsets(parent);
            prefWidth += vpbInsets.left + vpbInsets.right;
            prefHeight += vpbInsets.top + vpbInsets.bottom;
        }

        /* If a header exists and it's visible, factor its
         * preferred size in.
         */

        int rowHeaderWidth = 0;
        if (rowHead != null && rowHead.isVisible()) {
            rowHeaderWidth = rowHead.getPreferredSize().width;
        }
        if (upperLeft != null && upperLeft.isVisible()) {
            rowHeaderWidth = Math.max(rowHeaderWidth, upperLeft.getPreferredSize().width);
        }
        if (lowerLeft != null && lowerLeft.isVisible()) {
            rowHeaderWidth = Math.max(rowHeaderWidth, lowerLeft.getPreferredSize().width);
        }
        prefWidth += rowHeaderWidth;

        int upperHeight = getUpperHeight();

        prefHeight += upperHeight;

        if ((_rowFoot != null) && _rowFoot.isVisible()) {
            prefWidth += _rowFoot.getPreferredSize().width;
        }

        int lowerHeight = getLowerHeight();
        prefHeight += lowerHeight;

        /* If a scrollbar is going to appear, factor its preferred size in.
         * If the scrollbars policy is AS_NEEDED, this can be a little
         * tricky:
         *
         * - If the view is a Scrollable then scrollableTracksViewportWidth
         * and scrollableTracksViewportHeight can be used to effectively
         * disable scrolling (if they're true) in their respective dimensions.
         *
         * - Assuming that a scrollbar hasn't been disabled by the
         * previous constraint, we need to decide if the scrollbar is going
         * to appear to correctly compute the JScrollPanes preferred size.
         * To do this we compare the preferredSize of the viewport (the
         * extentSize) to the preferredSize of the view.  Although we're
         * not responsible for laying out the view we'll assume that the
         * JViewport will always give it its preferredSize.
         */

        if ((vsb != null) && (vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
            if (vsbPolicy == VERTICAL_SCROLLBAR_ALWAYS) {
                prefWidth += vsb.getPreferredSize().width;
            }
            else if ((viewSize != null) && (extentSize != null)) {
                boolean canScroll = true;
                if (view instanceof Scrollable) {
                    canScroll = !((Scrollable) view).getScrollableTracksViewportHeight();
                }
                if (canScroll && (viewSize.height > extentSize.height)) {
                    prefWidth += vsb.getPreferredSize().width;
                }
            }
        }

        if ((hsb != null) && (hsbPolicy != HORIZONTAL_SCROLLBAR_NEVER)) {
            if (hsbPolicy == HORIZONTAL_SCROLLBAR_ALWAYS) {
                prefHeight += hsb.getPreferredSize().height;
            }
            else if ((viewSize != null) && (extentSize != null)) {
                boolean canScroll = true;
                if (view instanceof Scrollable) {
                    canScroll = !((Scrollable) view).getScrollableTracksViewportWidth();
                }
                if (canScroll && (viewSize.width > extentSize.width)) {
                    prefHeight += hsb.getPreferredSize().height;
                }
            }
        }

        return new Dimension(prefWidth, prefHeight);
    }

    private int getUpperHeight() {
        int upperHeight = 0;

        if ((upperLeft != null) && upperLeft.isVisible()) {
            upperHeight = upperLeft.getPreferredSize().height;
        }
        if ((upperRight != null) && upperRight.isVisible()) {
            upperHeight = Math.max(upperRight.getPreferredSize().height, upperHeight);
        }

        if ((colHead != null) && colHead.isVisible()) {
            upperHeight = Math.max(colHead.getPreferredSize().height, upperHeight);
        }
        return upperHeight;
    }

    private int getLowerHeight() {
        int lowerHeight = 0;

        if ((lowerLeft != null) && lowerLeft.isVisible()) {
            lowerHeight = lowerLeft.getPreferredSize().height;
        }
        if ((lowerRight != null) && lowerRight.isVisible()) {
            lowerHeight = Math.max(lowerRight.getPreferredSize().height, lowerHeight);
        }
        if ((_colFoot != null) && _colFoot.isVisible()) {
            lowerHeight = Math.max(_colFoot.getPreferredSize().height, lowerHeight);
        }
        return lowerHeight;
    }

    /**
     * The minimum size of a <code>ScrollPane</code> is the size of the insets
     * plus minimum size of the viewport, plus the scrollpane's
     * viewportBorder insets, plus the minimum size
     * of the visible headers, plus the minimum size of the
     * scrollbars whose displayPolicy isn't NEVER.
     *
     * @param parent the <code>Container</code> that will be laid out
     * @return a <code>Dimension</code> object specifying the minimum size
     */
    public Dimension minimumLayoutSize(Container parent) {
        /* Sync the (now obsolete) policy fields with the
         * JScrollPane.
         */
        JScrollPane scrollPane = (JScrollPane) parent;
        vsbPolicy = scrollPane.getVerticalScrollBarPolicy();
        hsbPolicy = scrollPane.getHorizontalScrollBarPolicy();

        Insets insets = parent.getInsets();
        int minWidth = insets.left + insets.right;
        int minHeight = insets.top + insets.bottom;

        /* If there's a viewport add its minimumSize.
         */

        if (viewport != null) {
            Dimension size = viewport.getMinimumSize();
            minWidth += size.width;
            minHeight += size.height;
        }

        /* If there's a JScrollPane.viewportBorder, add its insets.
         */

        Border viewportBorder = scrollPane.getViewportBorder();
        if (viewportBorder != null) {
            Insets vpbInsets = viewportBorder.getBorderInsets(parent);
            minWidth += vpbInsets.left + vpbInsets.right;
            minHeight += vpbInsets.top + vpbInsets.bottom;
        }

        /* If a header exists and it's visible, factor its
         * minimum size in.
         */

        int rowHeaderWidth = 0;
        if (rowHead != null && rowHead.isVisible()) {
            Dimension size = rowHead.getMinimumSize();
            rowHeaderWidth = size.width;
            minHeight = Math.max(minHeight, size.height);
        }
        if (upperLeft != null && upperLeft.isVisible()) {
            rowHeaderWidth = Math.max(rowHeaderWidth, upperLeft.getMinimumSize().width);
        }
        if (lowerLeft != null && lowerLeft.isVisible()) {
            rowHeaderWidth = Math.max(rowHeaderWidth, lowerLeft.getMinimumSize().width);
        }
        minWidth += rowHeaderWidth;

        int upperHeight = 0;

        if ((upperLeft != null) && upperLeft.isVisible()) {
            upperHeight = upperLeft.getMinimumSize().height;
        }
        if ((upperRight != null) && upperRight.isVisible()) {
            upperHeight = Math.max(upperRight.getMinimumSize().height, upperHeight);
        }

        if ((colHead != null) && colHead.isVisible()) {
            Dimension size = colHead.getMinimumSize();
            minWidth = Math.max(minWidth, size.width);
            upperHeight = Math.max(size.height, upperHeight);
        }

        minHeight += upperHeight;

        // JIDE: added for JideScrollPaneLayout
        int lowerHeight = 0;

        if ((lowerLeft != null) && lowerLeft.isVisible()) {
            lowerHeight = lowerLeft.getMinimumSize().height;
        }
        if ((lowerRight != null) && lowerRight.isVisible()) {
            lowerHeight = Math.max(lowerRight.getMinimumSize().height, lowerHeight);
        }

        if ((_colFoot != null) && _colFoot.isVisible()) {
            Dimension size = _colFoot.getMinimumSize();
            minWidth = Math.max(minWidth, size.width);
            lowerHeight = Math.max(size.height, lowerHeight);
        }

        minHeight += lowerHeight;

        if ((_rowFoot != null) && _rowFoot.isVisible()) {
            Dimension size = _rowFoot.getMinimumSize();
            minWidth = Math.max(minWidth, size.width);
            minHeight += size.height;
        }
        // JIDE: End of added for JideScrollPaneLayout

        /* If a scrollbar might appear, factor its minimum
         * size in.
         */

        if ((vsb != null) && (vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
            Dimension size = vsb.getMinimumSize();
            minWidth += size.width;
            minHeight = Math.max(minHeight, size.height);
        }

        if ((hsb != null) && (hsbPolicy != HORIZONTAL_SCROLLBAR_NEVER)) {
            Dimension size = hsb.getMinimumSize();
            minWidth = Math.max(minWidth, size.width);
            minHeight += size.height;
        }

        return new Dimension(minWidth, minHeight);
    }


    /**
     * Lays out the scrollpane. The positioning of components depends on
     * the following constraints:
     * <ul>
     * <li> The row header, if present and visible, gets its preferred
     * width and the viewport's height.
     * <p/>
     * <li> The column header, if present and visible, gets its preferred
     * height and the viewport's width.
     * <p/>
     * <li> If a vertical scrollbar is needed, i.e. if the viewport's extent
     * height is smaller than its view height or if the <code>displayPolicy</code>
     * is ALWAYS, it's treated like the row header with respect to its
     * dimensions and is made visible.
     * <p/>
     * <li> If a horizontal scrollbar is needed, it is treated like the
     * column header (see the paragraph above regarding the vertical scrollbar).
     * <p/>
     * <li> If the scrollpane has a non-<code>null</code>
     * <code>viewportBorder</code>, then space is allocated for that.
     * <p/>
     * <li> The viewport gets the space available after accounting for
     * the previous constraints.
     * <p/>
     * <li> The corner components, if provided, are aligned with the
     * ends of the scrollbars and headers. If there is a vertical
     * scrollbar, the right corners appear; if there is a horizontal
     * scrollbar, the lower corners appear; a row header gets left
     * corners, and a column header gets upper corners.
     * </ul>
     *
     * @param parent the <code>Container</code> to lay out
     */
    public void layoutContainer(Container parent) {
        /* Sync the (now obsolete) policy fields with the
         * JScrollPane.
         */
        JScrollPane scrollPane = (JScrollPane) parent;
        vsbPolicy = scrollPane.getVerticalScrollBarPolicy();
        hsbPolicy = scrollPane.getHorizontalScrollBarPolicy();

        Rectangle availR = scrollPane.getBounds();
        availR.x = availR.y = 0;

        Insets insets = parent.getInsets();
        availR.x = insets.left;
        availR.y = insets.top;
        availR.width -= insets.left + insets.right;
        availR.height -= insets.top + insets.bottom;

        /* Get the scrollPane's orientation.
         */
        boolean leftToRight = scrollPane.getComponentOrientation().isLeftToRight();

        /* If there's a visible column header remove the space it
         * needs from the top of availR.  The column header is treated
         * as if it were fixed height, arbitrary width.
         */

        Rectangle colHeadR = new Rectangle(0, availR.y, 0, 0);

        int upperHeight = getUpperHeight();

        if ((colHead != null) && (colHead.isVisible())) {
            int colHeadHeight = Math.min(availR.height,
                    upperHeight);
            colHeadR.height = colHeadHeight;
            availR.y += colHeadHeight;
            availR.height -= colHeadHeight;
        }

        /* If there's a visible row header remove the space it needs
         * from the left or right of availR.  The row header is treated
         * as if it were fixed width, arbitrary height.
         */

        Rectangle rowHeadR = new Rectangle(0, 0, 0, 0);

        if ((rowHead != null) && (rowHead.isVisible())) {
            int rowHeadWidth = rowHead.getPreferredSize().width;
            if (upperLeft != null && upperLeft.isVisible()) {
                rowHeadWidth = Math.max(rowHeadWidth, upperLeft.getPreferredSize().width);
            }
            if (lowerLeft != null && lowerLeft.isVisible()) {
                rowHeadWidth = Math.max(rowHeadWidth, lowerLeft.getPreferredSize().width);
            }
            rowHeadWidth = Math.min(availR.width, rowHeadWidth);

            rowHeadR.width = rowHeadWidth;
            availR.width -= rowHeadWidth;
            if (leftToRight) {
                rowHeadR.x = availR.x;
                availR.x += rowHeadWidth;
            }
            else {
                rowHeadR.x = availR.x + availR.width;
            }
        }

        /* If there's a JScrollPane.viewportBorder, remove the
         * space it occupies for availR.
         */

        Border viewportBorder = scrollPane.getViewportBorder();
        Insets vpbInsets;
        if (viewportBorder != null) {
            vpbInsets = viewportBorder.getBorderInsets(parent);
            availR.x += vpbInsets.left;
            availR.y += vpbInsets.top;
            availR.width -= vpbInsets.left + vpbInsets.right;
            availR.height -= vpbInsets.top + vpbInsets.bottom;
        }
        else {
            vpbInsets = new Insets(0, 0, 0, 0);
        }

        /* If there's a visible row footer remove the space it needs
         * from the left or right of availR.  The row footer is treated
         * as if it were fixed width, arbitrary height.
         */

        Rectangle rowFootR = new Rectangle(0, 0, 0, 0);

        if ((_rowFoot != null) && (_rowFoot.isVisible())) {
            int rowFootWidth = Math.min(availR.width,
                    _rowFoot.getPreferredSize().width);
            rowFootR.width = rowFootWidth;
            availR.width -= rowFootWidth;
            if (leftToRight) {
                rowFootR.x = availR.x + availR.width;
            }
            else {
                rowFootR.x = availR.x;
                availR.x += rowFootWidth;
            }
        }

        /* If there's a visible column footer remove the space it
         * needs from the top of availR.  The column footer is treated
         * as if it were fixed height, arbitrary width.
         */

        Rectangle colFootR = new Rectangle(0, availR.y, 0, 0);

        int lowerHeight = getLowerHeight();

        if ((_colFoot != null) && (_colFoot.isVisible())) {
            int colFootHeight = Math.min(availR.height,
                    lowerHeight);
            colFootR.height = colFootHeight;
            availR.height -= colFootHeight;
            colFootR.y = availR.y + availR.height;
        }

        /* At this point availR is the space available for the viewport
         * and scrollbars. rowHeadR is correct except for its height and y
* and colHeadR is correct except for its width and x.  Once we're
         * through computing the dimensions  of these three parts we can
         * go back and set the dimensions of rowHeadR.height, rowHeadR.y,
* colHeadR.width, colHeadR.x and the bounds for the corners.
         *
* We'll decide about putting up scrollbars by comparing the
* viewport views preferred size with the viewports extent
         * size (generally just its size).  Using the preferredSize is
         * reasonable because layout proceeds top down - so we expect
         * the viewport to be laid out next.  And we assume that the
         * viewports layout manager will give the view it's preferred
         * size.  One exception to this is when the view implements
         * Scrollable and Scrollable.getViewTracksViewport{Width,Height}
         * methods return true.  If the view is tracking the viewports
         * width we don't bother with a horizontal scrollbar, similarly
         * if view.getViewTracksViewport(Height) is true we don't bother
         * with a vertical scrollbar.
         */

        Component view = (viewport != null) ? viewport.getView() : null;
        Dimension viewPrefSize =
                (view != null) ? view.getPreferredSize()
                        : new Dimension(0, 0);

        Dimension extentSize =
                (viewport != null) ? viewport.toViewCoordinates(availR.getSize())
                        : new Dimension(0, 0);

        boolean viewTracksViewportWidth = false;
        boolean viewTracksViewportHeight = false;
        boolean isEmpty = (availR.width < 0 || availR.height < 0);
        Scrollable sv;
        // Don't bother checking the Scrollable methods if there is no room
        // for the viewport, we aren't going to show any scrollbars in this
        // case anyway.
        if (!isEmpty && view instanceof Scrollable) {
            sv = (Scrollable) view;
            viewTracksViewportWidth = sv.getScrollableTracksViewportWidth();
            viewTracksViewportHeight = sv.getScrollableTracksViewportHeight();
        }
        else {
            sv = null;
        }

        /* If there's a vertical scrollbar and we need one, allocate
         * space for it (we'll make it visible later). A vertical
         * scrollbar is considered to be fixed width, arbitrary height.
         */

        Rectangle vsbR = new Rectangle(0, isVsbCoversWholeHeight(scrollPane) ? -vpbInsets.top : availR.y - vpbInsets.top, 0, 0);

        boolean vsbNeeded;
        if (isEmpty) {
            vsbNeeded = false;
        }
        else if (vsbPolicy == VERTICAL_SCROLLBAR_ALWAYS) {
            vsbNeeded = true;
        }
        else if (vsbPolicy == VERTICAL_SCROLLBAR_NEVER) {
            vsbNeeded = false;
        }
        else {  // vsbPolicy == VERTICAL_SCROLLBAR_AS_NEEDED
            vsbNeeded = !viewTracksViewportHeight && (viewPrefSize.height > extentSize.height);
        }


        if ((vsb != null) && vsbNeeded) {
            adjustForVSB(true, availR, vsbR, vpbInsets, leftToRight);
            extentSize = viewport.toViewCoordinates(availR.getSize());
        }

        /* If there's a horizontal scrollbar and we need one, allocate
         * space for it (we'll make it visible later). A horizontal
         * scrollbar is considered to be fixed height, arbitrary width.
         */

        Rectangle hsbR = new Rectangle(isHsbCoversWholeWidth(scrollPane) ? -vpbInsets.left : availR.x - vpbInsets.left, 0, 0, 0);
        boolean hsbNeeded;
        if (isEmpty) {
            hsbNeeded = false;
        }
        else if (hsbPolicy == HORIZONTAL_SCROLLBAR_ALWAYS) {
            hsbNeeded = true;
        }
        else if (hsbPolicy == HORIZONTAL_SCROLLBAR_NEVER) {
            hsbNeeded = false;
        }
        else {  // hsbPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED
            hsbNeeded = !viewTracksViewportWidth && (viewPrefSize.width > extentSize.width);
        }

        if ((hsb != null) && hsbNeeded) {
            adjustForHSB(true, availR, hsbR, vpbInsets);

            /* If we added the horizontal scrollbar then we've implicitly
             * reduced  the vertical space available to the viewport.
             * As a consequence we may have to add the vertical scrollbar,
             * if that hasn't been done so already.  Of course we
             * don't bother with any of this if the vsbPolicy is NEVER.
             */
            if ((vsb != null) && !vsbNeeded &&
                    (vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {

                extentSize = viewport.toViewCoordinates(availR.getSize());
                vsbNeeded = viewPrefSize.height > extentSize.height;

                if (vsbNeeded) {
                    adjustForVSB(true, availR, vsbR, vpbInsets, leftToRight);
                }
            }
        }

        /* Set the size of the viewport first, and then recheck the Scrollable
         * methods. Some components base their return values for the Scrollable
         * methods on the size of the Viewport, so that if we don't
         * ask after resetting the bounds we may have gotten the wrong
         * answer.
         */

        if (viewport != null) {
            viewport.setBounds(availR);

            if (sv != null) {
                extentSize = viewport.toViewCoordinates(availR.getSize());

                boolean oldHSBNeeded = hsbNeeded;
                boolean oldVSBNeeded = vsbNeeded;
                viewTracksViewportWidth = sv.
                        getScrollableTracksViewportWidth();
                viewTracksViewportHeight = sv.
                        getScrollableTracksViewportHeight();
                if (vsb != null && vsbPolicy == VERTICAL_SCROLLBAR_AS_NEEDED) {
                    boolean newVSBNeeded = !viewTracksViewportHeight &&
                            (viewPrefSize.height > extentSize.height);
                    if (newVSBNeeded != vsbNeeded) {
                        vsbNeeded = newVSBNeeded;
                        adjustForVSB(vsbNeeded, availR, vsbR, vpbInsets,
                                leftToRight);
                        extentSize = viewport.toViewCoordinates
                                (availR.getSize());
                    }
                }
                if (hsb != null && hsbPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED) {
                    boolean newHSBbNeeded = !viewTracksViewportWidth &&
                            (viewPrefSize.width > extentSize.width);
                    if (newHSBbNeeded != hsbNeeded) {
                        hsbNeeded = newHSBbNeeded;
                        adjustForHSB(hsbNeeded, availR, hsbR, vpbInsets);
                        if ((vsb != null) && !vsbNeeded &&
                                (vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {

                            extentSize = viewport.toViewCoordinates
                                    (availR.getSize());
                            vsbNeeded = viewPrefSize.height >
                                    extentSize.height;

                            if (vsbNeeded) {
                                adjustForVSB(true, availR, vsbR, vpbInsets,
                                        leftToRight);
                            }
                        }
                        if (_rowFoot != null && _rowFoot.isVisible()) {
                            vsbR.x += rowFootR.width;
                        }
                    }
                }
                if (oldHSBNeeded != hsbNeeded ||
                        oldVSBNeeded != vsbNeeded) {
                    viewport.setBounds(availR);
                    // You could argue that we should recheck the
                    // Scrollable methods again until they stop changing,
                    // but they might never stop changing, so we stop here
                    // and don't do any additional checks.
                }
            }
        }

        /* We now have the final size of the viewport: availR.
         * Now fixup the header and scrollbar widths/heights.
         */
        vsbR.height = isVsbCoversWholeHeight(scrollPane) ? scrollPane.getHeight() - 1 : availR.height + vpbInsets.top + vpbInsets.bottom;
        hsbR.width = isHsbCoversWholeWidth(scrollPane) ? scrollPane.getWidth() - vsbR.width : availR.width + vpbInsets.left + vpbInsets.right;
        rowHeadR.height = availR.height + vpbInsets.top + vpbInsets.bottom;
        rowHeadR.y = availR.y - vpbInsets.top;
        colHeadR.width = availR.width + vpbInsets.left + vpbInsets.right;
        colHeadR.x = availR.x - vpbInsets.left;

        colFootR.x = availR.x;
        colFootR.y = rowHeadR.y + rowHeadR.height;
        colFootR.width = availR.width;
        rowFootR.x = availR.x + availR.width;
        rowFootR.y = availR.y;
        rowFootR.height = availR.height;

        vsbR.x += rowFootR.width;
        hsbR.y += colFootR.height;

        /* Set the bounds of the remaining components.  The scrollbars
         * are made invisible if they're not needed.
         */

        if (rowHead != null) {
            rowHead.setBounds(rowHeadR);
        }

        if (_rowFoot != null) {
            _rowFoot.setBounds(rowFootR);
        }

        if (colHead != null) {
            int height = Math.min(colHeadR.height, colHead.getPreferredSize().height);
            colHead.setBounds(new Rectangle(colHeadR.x, colHeadR.y + colHeadR.height - height, colHeadR.width, height));
        }

        if (_colFoot != null) {
            int height = Math.min(colFootR.height, _colFoot.getPreferredSize().height);
            _colFoot.setBounds(new Rectangle(colFootR.x, colFootR.y, colFootR.width, height));
        }

        if (vsb != null) {
            if (vsbNeeded) {
                vsb.setVisible(true);
                if (_vTop == null && _vBottom == null)
                    vsb.setBounds(vsbR);
                else {
                    Rectangle rect = new Rectangle(vsbR);
                    if (_vTop != null) {
                        Dimension dim = _vTop.getPreferredSize();
                        rect.y += dim.height;
                        rect.height -= dim.height;
                        _vTop.setVisible(true);
                        _vTop.setBounds(vsbR.x, vsbR.y, vsbR.width, dim.height);
                    }
                    if (_vBottom != null) {
                        Dimension dim = _vBottom.getPreferredSize();
                        rect.height -= dim.height;
                        _vBottom.setVisible(true);
                        _vBottom.setBounds(vsbR.x, vsbR.y + vsbR.height - dim.height, vsbR.width, dim.height);
                    }
                    vsb.setBounds(rect);
                }
            }
            else {
                if (viewPrefSize.height > extentSize.height) {
                    vsb.setVisible(true);
                    vsb.setBounds(vsbR.x, vsbR.y, 0, vsbR.height);
                }
                else {
                    vsb.setVisible(false);
                }
                if (_vTop != null)
                    _vTop.setVisible(false);
                if (_vBottom != null)
                    _vBottom.setVisible(false);
            }
        }

        if (hsb != null) {
            if (hsbNeeded) {
                hsb.setVisible(true);
                if (_hLeft == null && _hRight == null)
                    hsb.setBounds(hsbR);
                else {
                    Rectangle rect = new Rectangle(hsbR);
                    if (_hLeft != null) {
                        Dimension dim = _hLeft.getPreferredSize();
                        rect.x += dim.width;
                        rect.width -= dim.width;
                        _hLeft.setVisible(true);
                        _hLeft.setBounds(hsbR.x, hsbR.y, dim.width, hsbR.height);
                        _hLeft.doLayout();
                    }
                    if (_hRight != null) {
                        Dimension dim = _hRight.getPreferredSize();
                        rect.width -= dim.width;
                        _hRight.setVisible(true);
                        _hRight.setBounds(hsbR.x + hsbR.width - dim.width, hsbR.y, dim.width, hsbR.height);
                    }
                    hsb.setBounds(rect);
                }
            }
            else {
                if (viewPrefSize.width > extentSize.width) {
                    hsb.setVisible(true);
                    hsb.setBounds(hsbR.x, hsbR.y, hsbR.width, 0);
                }
                else {
                    hsb.setVisible(false);
                }
                if (_hLeft != null)
                    _hLeft.setVisible(false);
                if (_hRight != null)
                    _hRight.setVisible(false);
            }
        }

        if (lowerLeft != null && lowerLeft.isVisible()) {
            int height = Math.min(lowerLeft.getPreferredSize().height, colFootR.height);
            lowerLeft.setBounds(leftToRight ? rowHeadR.x : vsbR.x,
                    colFootR.y != 0 ? colFootR.y : hsbR.y,
                    leftToRight ? rowHeadR.width : vsbR.width,
                    height);
        }

        if (lowerRight != null && lowerRight.isVisible()) {
            int height = Math.min(lowerRight.getPreferredSize().height, colFootR.height);
            lowerRight.setBounds(leftToRight ? rowFootR.x : rowHeadR.x,
                    colFootR.y != 0 ? colFootR.y : hsbR.y,
                    leftToRight ? rowFootR.width + (isVsbCoversWholeHeight(scrollPane) ? 0 : vsbR.width) : rowHeadR.width,
                    height);
        }

        if (upperLeft != null && upperLeft.isVisible()) {
            int height = Math.min(upperLeft.getPreferredSize().height, colHeadR.height);
            upperLeft.setBounds(leftToRight ? rowHeadR.x : vsbR.x,
                    colHeadR.y + colHeadR.height - height,
                    leftToRight ? rowHeadR.width : vsbR.width,
                    height);
        }

        if (upperRight != null && upperRight.isVisible()) {
            int height = Math.min(upperRight.getPreferredSize().height, colHeadR.height);
            upperRight.setBounds(leftToRight ? rowFootR.x : rowHeadR.x,
                    colHeadR.y + colHeadR.height - height,
                    leftToRight ? rowFootR.width + (isVsbCoversWholeHeight(scrollPane) ? 0 : vsbR.width) : rowHeadR.width,
                    height);
        }
    }

    /**
     * Adjusts the <code>Rectangle</code> <code>available</code> based on if
     * the vertical scrollbar is needed (<code>wantsVSB</code>).
     * The location of the vsb is updated in <code>vsbR</code>, and
     * the viewport border insets (<code>vpbInsets</code>) are used to offset
     * the vsb. This is only called when <code>wantsVSB</code> has
     * changed, eg you shouldn't invoke adjustForVSB(true) twice.
     */
    private void adjustForVSB(boolean wantsVSB, Rectangle available,
                              Rectangle vsbR, Insets vpbInsets,
                              boolean leftToRight) {
        int oldWidth = vsbR.width;
        if (wantsVSB) {
            int vsbWidth = Math.max(0, Math.min(vsb.getPreferredSize().width,
                    available.width));

            available.width -= vsbWidth;
            vsbR.width = vsbWidth;

            if (leftToRight) {
                vsbR.x = available.x + available.width + vpbInsets.right;
            }
            else {
                vsbR.x = available.x - vpbInsets.left;
                available.x += vsbWidth;
            }
        }
        else {
            available.width += oldWidth;
        }
    }

    /**
     * Adjusts the <code>Rectangle</code> <code>available</code> based on if
     * the horizontal scrollbar is needed (<code>wantsHSB</code>).
     * The location of the hsb is updated in <code>hsbR</code>, and
     * the viewport border insets (<code>vpbInsets</code>) are used to offset
     * the hsb.  This is only called when <code>wantsHSB</code> has
     * changed, eg you shouldn't invoked adjustForHSB(true) twice.
     */
    private void adjustForHSB(boolean wantsHSB, Rectangle available,
                              Rectangle hsbR, Insets vpbInsets) {
        int oldHeight = hsbR.height;
        if (wantsHSB) {
            int hsbHeight = Math.max(0, Math.min(available.height,
                    hsb.getPreferredSize().height));

            available.height -= hsbHeight;
            hsbR.y = available.y + available.height + vpbInsets.bottom;
            hsbR.height = hsbHeight;
        }
        else {
            available.height += oldHeight;
        }
    }

    /**
     * The UI resource version of <code>ScrollPaneLayout</code>.
     */
    static class UIResource extends JideScrollPaneLayout implements javax.swing.plaf.UIResource {
    }
}