/*
 * @(#)${NAME}
 *
 * Copyright 2002 - 2004 JIDE Software Inc. All rights reserved.
 */
package com.jidesoft.swing;

import com.jidesoft.swing.event.SearchableEvent;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * <code>ComboBoxSearchable</code> is an concrete implementation of {@link Searchable}
 * that enables the search function in non-editable JComboBox.
 * <p>It's very simple to use it. Assuming you have a JComboBox, all you need to do is to
 * call
 * <code><pre>
 * JComboBox comboBox = ....;
 * ComboBoxSearchable searchable = new ComboBoxSearchable(comboBox);
 * </pre></code>
 * Now the JComboBox will have the search function.
 * <p/>
 * There is very little customization you need to do to ComboBoxSearchable. The only thing you might
 * need is when the element in the JComboBox needs a special conversion to convert to string. If so, you can overide
 * convertElementToString() to provide you own algorithm to do the conversion.
 * <code><pre>
 * JComboBox comboBox = ....;
 * ComboBoxSearchable searchable = new ComboBoxSearchable(comboBox) {
 *      protected String convertElementToString(Object object) {
 *          ...
 *      }
 * };
 * </pre></code>
 * <p/>
 * Additional customization can be done on the base Searchable class such as background and foreground color, keystrokes,
 * case sensitivity,
 */
public class ComboBoxSearchable extends Searchable implements ListDataListener, PropertyChangeListener {

    private boolean _showPopupDuringSearching = true;

    public ComboBoxSearchable(JComboBox comboBox) {
        super(comboBox);

        // to avoid conflict with default type-match feature of JComboBox.
        comboBox.setKeySelectionManager(new JComboBox.KeySelectionManager() {
            public int selectionForKey(char aKey, ComboBoxModel aModel) {
                return -1;
            }
        });
        comboBox.getModel().addListDataListener(this);
        comboBox.addPropertyChangeListener("model", this);
    }

    public void uninstallListeners() {
        super.uninstallListeners();
        if (_component instanceof JComboBox) {
            ((JComboBox) _component).getModel().removeListDataListener(this);
        }
        _component.removePropertyChangeListener("model", this);
    }

    /**
     * Checks if the popup is showing during searching.
     *
     * @return true if popup is visible during searching.
     * @deprecated misspelled. Use {@link #isShowPopupDuringSearching()}.
     */
    public boolean isShowPopupDuringSeaching() {
        return _showPopupDuringSearching;
    }

    /**
     * Sets the property which determines if the popup should be shown during searching.
     *
     * @param showPopupDuringSeaching
     * @deprecated misspelled. Use {@link #setShowPopupDuringSearching(boolean)}
     */
    public void setShowPopupDuringSeaching(boolean showPopupDuringSeaching) {
        _showPopupDuringSearching = showPopupDuringSeaching;
    }

    /**
     * Checks if the popup is showing during searching.
     *
     * @return true if popup is visible during searching.
     */
    public boolean isShowPopupDuringSearching() {
        return _showPopupDuringSearching;
    }

    /**
     * Sets the property which determines if the popup should be shown during searching.
     *
     * @param showPopupDuringSearching
     */
    public void setShowPopupDuringSearching(boolean showPopupDuringSearching) {
        _showPopupDuringSearching = showPopupDuringSearching;
    }

    protected void setSelectedIndex(int index, boolean incremental) {
        if (isShowPopupDuringSearching()) {
            try {
                ((JComboBox) _component).showPopup();
            }
            catch (IllegalComponentStateException e) {
            }
        }
        if (((JComboBox) _component).getSelectedIndex() != index) {
            ((JComboBox) _component).setSelectedIndex(index);
        }
    }

    protected int getSelectedIndex() {
        return ((JComboBox) _component).getSelectedIndex();
    }

    protected Object getElementAt(int index) {
        ComboBoxModel comboBoxModel = ((JComboBox) _component).getModel();
        return comboBoxModel.getElementAt(index);
    }

    protected int getElementCount() {
        ComboBoxModel comboBoxModel = ((JComboBox) _component).getModel();
        return comboBoxModel.getSize();
    }

    /**
     * Converts the element in Jcombobox to string. The returned value will be the
     * <code>toString()</code> of whatever element that returned from <code>list.getModel().getElementAt(i)</code>.
     *
     * @param object
     * @return the string representing the element in the JComboBox.
     */
    protected String convertElementToString(Object object) {
        if (object != null) {
            return object.toString();
        }
        else {
            return "";
        }
    }

    public void contentsChanged(ListDataEvent e) {
        if (e.getIndex0() == -1 && e.getIndex1() == -1) {
        }
        else {
            hidePopup();
            fireSearchableEvent(new SearchableEvent(this, SearchableEvent.SEARCHABLE_MODEL_CHANGE));
        }
    }

    public void intervalAdded(ListDataEvent e) {
        hidePopup();
        fireSearchableEvent(new SearchableEvent(this, SearchableEvent.SEARCHABLE_MODEL_CHANGE));
    }

    public void intervalRemoved(ListDataEvent e) {
        hidePopup();
        fireSearchableEvent(new SearchableEvent(this, SearchableEvent.SEARCHABLE_MODEL_CHANGE));
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if ("model".equals(evt.getPropertyName())) {
            hidePopup();

            if (evt.getOldValue() instanceof ComboBoxModel) {
                ((ComboBoxModel) evt.getOldValue()).removeListDataListener(this);
            }

            if (evt.getNewValue() instanceof ComboBoxModel) {
                ((ComboBoxModel) evt.getNewValue()).addListDataListener(this);
            }
        }
        fireSearchableEvent(new SearchableEvent(this, SearchableEvent.SEARCHABLE_MODEL_CHANGE));
    }

}