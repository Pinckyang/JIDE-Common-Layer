/*
 * @(#)FolderToolBar.java 10/6/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */
package com.jidesoft.plaf.basic;

import com.jidesoft.utils.SystemInfo;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Manages the optional folder toolbar that sits above the folderview's tree view panel
 */
class FolderToolBar extends JToolBar {
    private final static FileSystemView _fsv = FileSystemView.getFileSystemView();

    private JButton _deleteFolderBtn;
    private JButton _newFolderBtn;
    private JComboBox _recentFoldersList;

    private List _listeners = new ArrayList(1);

    public FolderToolBar(boolean showRecentFolders, List recentFoldersList) {
        setFloatable(false);
        setupToolBar(showRecentFolders, recentFoldersList);

    }

    public void enableDelete() {
        _deleteFolderBtn.setEnabled(true);
    }

    public void disableDelete() {
        _deleteFolderBtn.setEnabled(false);
    }

    public void enableNewFolder() {
        _newFolderBtn.setEnabled(true);
    }

    public void disableNewFolder() {
        _newFolderBtn.setEnabled(false);
    }

    /**
     * Creates the toolbar buttons and dropdown
     */
    private void setupToolBar(boolean showRecentFolders, List recentFoldersList) {

        // add to toolbar
        if (showRecentFolders) {
            _recentFoldersList = new JComboBox(new DefaultComboBoxModel());
            if (recentFoldersList != null && recentFoldersList.size() > 0) {
                _recentFoldersList.setModel(new DefaultComboBoxModel((recentFoldersList.toArray())));
            }
            _recentFoldersList.setEditable(false);
            _recentFoldersList.setRenderer(new FileListCellRenderer());
            _recentFoldersList.addPopupMenuListener(new PopupMenuListener() {
                private boolean m_wasCancelled = false;

                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    m_wasCancelled = false;
                }

                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    if (e.getSource() instanceof JComboBox) {
                        JComboBox box = (JComboBox) e.getSource();

                        File selectedFile = (File) box.getModel().getSelectedItem();
                        // if popup was not cancelled then select the folder
                        if (!m_wasCancelled && selectedFile != null) {
//                            System.out.println("User selected file: " + selectedFile.getAbsolutePath());
                            recentFolderSelected(selectedFile);
                        }
                    }
                }

                public void popupMenuCanceled(PopupMenuEvent e) {
                    m_wasCancelled = true;
                }
            });
            _recentFoldersList.setPrototypeDisplayValue("AAAAAAAAAAAAAAAAAA");
            add(new JLabel("Recent: "));
            add(_recentFoldersList);
            addSeparator();
        }
        else {
            add(Box.createHorizontalGlue());
        }

        JButton desktopBtn = new NoFocusButton(new ToolBarAction(null,
                SystemInfo.isWindows() ? _fsv.getSystemIcon(_fsv.getHomeDirectory()) : BasicFolderChooserIconsFactory.getImageIcon(BasicFolderChooserIconsFactory.ToolBar.HOME)) {
            public void actionPerformed(ActionEvent e) {
                desktopButtonClicked();
            }
        });

        final ResourceBundle resourceBundle = FolderChooserResource.getResourceBundle(Locale.getDefault());
        desktopBtn.setToolTipText(SystemInfo.isWindows() ? resourceBundle.getString("FolderChooser.toolbar.desktop") : resourceBundle.getString("FolderChooser.toolbar.home"));
        add(desktopBtn);

        if (SystemInfo.isWindows()) {
            JButton myDocumentsBtn = new NoFocusButton(new ToolBarAction(null, _fsv.getSystemIcon(_fsv.getDefaultDirectory())) {
                public void actionPerformed(ActionEvent e) {
                    myDocumentsButtonClicked();
                }
            });
            myDocumentsBtn.setToolTipText(resourceBundle.getString("FolderChooser.toolbar.mydocuments"));
            add(myDocumentsBtn);
        }
        // dredge up appropriate icons
        Icon deleteIcon = BasicFolderChooserIconsFactory.getImageIcon(BasicFolderChooserIconsFactory.ToolBar.DELETE);

        _deleteFolderBtn = new NoFocusButton(new ToolBarAction(null, deleteIcon) {
            public void actionPerformed(ActionEvent e) {
                deleteFolderButtonClicked();
            }
        });

        _deleteFolderBtn.setToolTipText(resourceBundle.getString("FolderChooser.toolbar.delete"));

        Icon newFolderIcon = BasicFolderChooserIconsFactory.getImageIcon(BasicFolderChooserIconsFactory.ToolBar.NEW);
        _newFolderBtn = new NoFocusButton(new ToolBarAction(null, newFolderIcon) {
            public void actionPerformed(ActionEvent e) {
                newFolderButtonClicked();
            }
        });

        _newFolderBtn.setToolTipText(resourceBundle.getString("FolderChooser.toolbar.new"));

        addSeparator();
        add(_deleteFolderBtn);
        add(_newFolderBtn);
    }


    private class FileListCellRenderer implements ListCellRenderer {
        protected DefaultListCellRenderer m_defaultRenderer = new DefaultListCellRenderer();

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel renderer = (JLabel) m_defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof File) {
                File f = (File) value;
                String text = _fsv.getSystemDisplayName(f);
                Icon icon = _fsv.getSystemIcon(f);
                renderer.setIcon(icon);
                renderer.setText(text);
            }
            return renderer;
        }
    }

    // ----------------------------------------------------------------
    // Listener methods
    // ----------------------------------------------------------------
    public void addListener(FolderToolBarListener listener) {
        _listeners.add(listener);
    }

    public void removeListener(FolderToolBarListener listener) {
        _listeners.remove(listener);
    }

    public void clearListeners() {
        _listeners.clear();
    }

    private void deleteFolderButtonClicked() {
        for (int i = 0; i < _listeners.size(); i++) {
            FolderToolBarListener listener = (FolderToolBarListener) _listeners.get(i);
            listener.deleteFolderButtonClicked();
        }
    }

    private void newFolderButtonClicked() {
        for (int i = 0; i < _listeners.size(); i++) {
            FolderToolBarListener listener = (FolderToolBarListener) _listeners.get(i);
            listener.newFolderButtonClicked();
        }
    }

    private void myDocumentsButtonClicked() {
        for (int i = 0; i < _listeners.size(); i++) {
            FolderToolBarListener listener = (FolderToolBarListener) _listeners.get(i);
            listener.myDocumentsButtonClicked();
        }
    }

    private void desktopButtonClicked() {
        for (int i = 0; i < _listeners.size(); i++) {
            FolderToolBarListener listener = (FolderToolBarListener) _listeners.get(i);
            listener.desktopButtonClicked();
        }
    }

    private void recentFolderSelected(File recentFolder) {
        for (int i = 0; i < _listeners.size(); i++) {
            FolderToolBarListener listener = (FolderToolBarListener) _listeners.get(i);
            listener.recentFolderSelected(recentFolder);
        }
    }

    public void setRecentList(List recentFoldersList) {
        _recentFoldersList.setModel(new DefaultComboBoxModel((recentFoldersList.toArray())));
    }

    private abstract class ToolBarAction extends AbstractAction {
        public ToolBarAction(String name, Icon icon) {
            super(name, icon);
        }
    }

    static class NoFocusButton extends JButton {
        public NoFocusButton(Action a) {
            super(a);
            setRequestFocusEnabled(false);
            setFocusable(false);

            // on jdk1.6, the button size is wrong
            Insets margin = getMargin();
            margin.left = margin.top;
            margin.right = margin.bottom;
            setMargin(margin);
        }
    }
}