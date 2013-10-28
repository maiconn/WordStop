/*
 * @(#)QuaquaLeopardFileChooserUI.java  
 *
 * Copyright (c) 2007-2010 Werner Randelshofer
 * Hausmatt 10, Immensee, CH-6405, Switzerland.
 * http://www.randelshofer.ch
 * All rights reserved.
 *
 * The copyright of this software is owned by Werner Randelshofer. 
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * Werner Randelshofer. For details see accompanying license terms. 
 */
package ch.randelshofer.quaqua.leopard;

import ch.randelshofer.quaqua.*;
import ch.randelshofer.quaqua.filechooser.*;
import ch.randelshofer.quaqua.leopard.filechooser.*;
import ch.randelshofer.quaqua.panther.filechooser.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.beans.*;
import java.io.File;
import java.util.*;

/**
 * A replacement for the AquaFileChooserUI. Provides a column view similar
 * to the one provided with the native Aqua user interface on Mac OS X 10.5
 * (Leopard).
 *
 * @author Werner Randelshofer
 * @version $Id: QuaquaLeopardFileChooserUI.java 174 2010-01-09 16:06:52Z wrandelshofer $
 */
public class QuaquaLeopardFileChooserUI extends BasicFileChooserUI {
    // Implementation derived from MetalFileChooserUI
    /* Models. */

    private DirectoryComboBoxModel directoryComboBoxModel;
    private Action directoryComboBoxAction = new DirectoryComboBoxAction();
    private FileView fileView;
    private FilterComboBoxModel filterComboBoxModel;
    private AliasFileSystemTreeModel model = null;
    private SubtreeTreeModel subtreeModel = null;
    // Labels, mnemonics, and tooltips (oh my!)
    private int fileNameLabelMnemonic = 0;
    private String fileNameLabelText = null;
    private int filesOfTypeLabelMnemonic = 0;
    private String filesOfTypeLabelText = null;
    private String upFolderToolTipText = null;
    private String upFolderAccessibleName = null;
    private String homeFolderToolTipText = null;
    private String homeFolderAccessibleName = null;
    private String newFolderButtonText = null;
    private String newFolderToolTipText = null;
    private String newFolderAccessibleName = null;
    protected String chooseButtonText = null;
    private String newFolderDialogPrompt, newFolderDefaultName, newFolderErrorText, newFolderExistsErrorText, newFolderTitleText;
    private final static File computer = AliasFileSystemTreeModel.COMPUTER;
    private SidebarTreeModel sidebarTreeModel;
    /**
     * This listener is used to determine whether the JFileChooser is showing.
     */
    private AncestorListener ancestorListener;
    /**
     * This listener is used to handle files that were dropped on the dir chooser.
     */
    private FileTransferHandler fileTransferHandler;
    /**
     * Actions.
     */
    private Action newFolderAction = new NewFolderAction();
    private Action approveSelectionAction = new QuaquaApproveSelectionAction();
    /**
     * Values greater zero indicate that the UI is adjusting.
     * This is required to prevent the UI from changing the FileChooser's state
     * while processing a PropertyChangeEvent fired from the FileChooser.
     */
    private int isAdjusting = 0;
    /** XXX - These keystrokes should go into an InputMap created by the
     * BasicQuaquaLookAndFeel class.
     */
    private KeyStroke[] KEYSTROKES = {
        KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.META_MASK | InputEvent.SHIFT_MASK),
        KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.META_MASK | InputEvent.SHIFT_MASK),
        KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.META_MASK),
        KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.META_MASK | InputEvent.SHIFT_MASK),
        KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.META_MASK | InputEvent.SHIFT_MASK),
        KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.META_MASK | InputEvent.SHIFT_MASK),
        KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.META_MASK | InputEvent.SHIFT_MASK),
        KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.META_MASK | InputEvent.SHIFT_MASK),
        KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.META_MASK | InputEvent.SHIFT_MASK),
        KeyStroke.getKeyStroke(KeyEvent.VK_BACK_QUOTE, 0),
        KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0)
    };
    private AbstractAction keyListenerAction = new AbstractAction() {
        // XXX - This should be rewritten using an ActionMap

        public void actionPerformed(ActionEvent ae) {
            File file = null;
            switch (ae.getActionCommand().charAt(0)) {
                case 'd':
                    file = new File(System.getProperty("user.home") + "/Desktop");
                    break;
                case 'c':
                    file = new File("/");
                    break;
                case 'h':
                    file = new File(System.getProperty("user.home"));
                    break;
                case 'k':
                    file = new File("/Network");
                    break;
                case 'i':
                    //not doing iDisk for now
                    file = null;
                    return;
                case 'a':
                    file = new File("/Applications");
                    break;
                /*
                case 'u':
                dir = new File( "/Applications/Utilities" );
                break;*/
                case 'g':
                /* case '`':*/
                case '/':
                    //need to pop up window for user to type dir. need tab completion!
                    file = null;
                    return;
                default:
                    //Unknown Key Command in: + ae );
                    break;
            }
            //set the dir if non-null:
            if (file != null) {
                // if the dir is in the sidebar,
                // select the sidebar, otherwise just
                // select the dir
                // FIXME - Implement me
                /*
                for (int i=0, n = sidebarTreeModel.getSize(); i < n; i++) {
                FileInfo sidebarFile = (FileInfo) sidebarTreeModel.getElementAt(i);
                if (sidebarFile != null && sidebarFile.getFile().equals(dir)) {
                sidebarTree.setSelectedIndex(i);
                return;
                }
                }*/
                getFileChooser().setSelectedFile(file);
            }
        }
    };
    // Variables declaration - do not modify
    // FIXME - accessoryPanel could be moved up to BasicFileChooserUI.
    private javax.swing.JPanel accessoryPanel;
    private javax.swing.JButton approveButton;
    private ch.randelshofer.quaqua.JBrowser browser;
    private javax.swing.JScrollPane browserScrollPane;
    //private javax.swing.JToggleButton browserToggleButton;
    private javax.swing.JPanel cancelOpenPanel;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel controlsPanel;
    private javax.swing.JComboBox directoryComboBox;
    private javax.swing.JLabel fileNameLabel;
    private javax.swing.JPanel fileNamePanel;
    private javax.swing.JPanel fileNameSpringPanel;
    private javax.swing.JTextField fileNameTextField;
    private javax.swing.JLabel filesOfTypeLabel;
    private javax.swing.JComboBox filterComboBox;
    private javax.swing.JPanel formatPanel;
    private javax.swing.JPanel formatSpringPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel navigationButtonsPanel;
    private javax.swing.JPanel navigationPanel;
    private javax.swing.JPanel navigationSpringPanel;
    private javax.swing.JButton newFolderButton;
    //private javax.swing.JButton nextButton;
    //private javax.swing.JButton previousButton;
    private javax.swing.JSeparator separator;
    private javax.swing.JSplitPane splitPane;
    //private javax.swing.JTable table;
    //private javax.swing.JScrollPane tableScrollPane;
    //private javax.swing.JToggleButton tableToggleButton;
    private javax.swing.ButtonGroup viewGroup;
    private javax.swing.JPanel viewsPanel;
    private javax.swing.JTree sidebarTree;
    private javax.swing.JScrollPane sidebarScrollPane;
    // End of variables declaration
    //
    // ComponentUI Interface Implementation methods
    //

    public static ComponentUI createUI(JComponent c) {
        return new QuaquaLeopardFileChooserUI((JFileChooser) c);
    }

    public QuaquaLeopardFileChooserUI(JFileChooser filechooser) {
        super(filechooser);
    }

    public void installComponents(JFileChooser fc) {
        sidebarTree = new javax.swing.JTree() {

            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width = 10;
                return d;
            }
        };

        // Form definition  - do not modify
        java.awt.GridBagConstraints gridBagConstraints;

        viewGroup = new javax.swing.ButtonGroup();
        fileNamePanel = new javax.swing.JPanel();
        fileNameLabel = new javax.swing.JLabel();
        fileNameTextField = new javax.swing.JTextField();
        fileNameSpringPanel = new javax.swing.JPanel();
        separator = new javax.swing.JSeparator();
        mainPanel = new javax.swing.JPanel();
        navigationPanel = new javax.swing.JPanel();
        navigationButtonsPanel = new javax.swing.JPanel();
        //previousButton = new javax.swing.JButton();
        //nextButton = new javax.swing.JButton();
        //tableToggleButton = new javax.swing.JToggleButton();
        //browserToggleButton = new javax.swing.JToggleButton();
        directoryComboBox = new javax.swing.JComboBox();
        navigationSpringPanel = new javax.swing.JPanel();
        splitPane = new javax.swing.JSplitPane();
        sidebarScrollPane = new javax.swing.JScrollPane();
        //sidebarTree = new javax.swing.JTree();
        viewsPanel = new javax.swing.JPanel();
        browserScrollPane = new javax.swing.JScrollPane();
        browser = new ch.randelshofer.quaqua.JBrowser();
        //tableScrollPane = new javax.swing.JScrollPane();
        //table = new javax.swing.JTable();
        controlsPanel = new javax.swing.JPanel();
        accessoryPanel = new javax.swing.JPanel();
        formatPanel = new javax.swing.JPanel();
        filesOfTypeLabel = new javax.swing.JLabel();
        filterComboBox = new javax.swing.JComboBox();
        formatSpringPanel = new javax.swing.JPanel();
        buttonsPanel = new javax.swing.JPanel();
        newFolderButton = new javax.swing.JButton();
        cancelOpenPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        approveButton = new javax.swing.JButton();

        fc.setLayout(new java.awt.BorderLayout());

        fc.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 0, 10, 0));
        fileNamePanel.setLayout(new java.awt.GridBagLayout());

        fileNamePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 0, 1, 0));
        fileNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        fileNameLabel.setText("Save As:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 6);
        fileNamePanel.add(fileNameLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        fileNamePanel.add(fileNameTextField, gridBagConstraints);

        fileNameSpringPanel.setLayout(null);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        fileNamePanel.add(fileNameSpringPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        fileNamePanel.add(separator, gridBagConstraints);

        fc.add(fileNamePanel, java.awt.BorderLayout.NORTH);

        mainPanel.setLayout(new java.awt.BorderLayout());

        navigationPanel.setLayout(new java.awt.GridBagLayout());

        navigationPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 4, 8));
        navigationButtonsPanel.setLayout(new java.awt.GridBagLayout());

        //previousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/randelshofer/quaqua/panther/images/FileChooser.previousIcon.png")));
        //navigationButtonsPanel.add(previousButton, new java.awt.GridBagConstraints());

        //nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/randelshofer/quaqua/panther/images/FileChooser.nextIcon.png")));
        //navigationButtonsPanel.add(nextButton, new java.awt.GridBagConstraints());

        //viewGroup.add(tableToggleButton);
        //tableToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/randelshofer/quaqua/panther/images/FileChooser.tableIcon.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        //navigationButtonsPanel.add(tableToggleButton, gridBagConstraints);

        //viewGroup.add(browserToggleButton);
        //browserToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/randelshofer/quaqua/panther/images/FileChooser.browserIcon.png")));
        //browserToggleButton.setSelected(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        //navigationButtonsPanel.add(browserToggleButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        navigationPanel.add(navigationButtonsPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        navigationPanel.add(directoryComboBox, gridBagConstraints);

        navigationSpringPanel.setLayout(null);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        navigationPanel.add(navigationSpringPanel, gridBagConstraints);

        mainPanel.add(navigationPanel, java.awt.BorderLayout.NORTH);

        splitPane.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        splitPane.setDividerLocation(134);
        splitPane.setDividerSize(1);
        // Setting the background color is needed for the Quaqua FileChooser-only LAF.
        if (UIManager.getColor("FileChooser.splitPaneBackground") != null) {
            splitPane.setBackground(UIManager.getColor("FileChooser.splitPaneBackground"));
        }
        splitPane.setContinuousLayout(true);
        sidebarScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sidebarScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarTree.setRootVisible(false);
        sidebarTree.setShowsRootHandles(true);
        sidebarScrollPane.setViewportView(sidebarTree);

        splitPane.setLeftComponent(sidebarScrollPane);

        viewsPanel.setLayout(new java.awt.CardLayout());

        browserScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        browserScrollPane.setViewportView(browser);

        viewsPanel.add(browserScrollPane, "browser");

        //tableScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        //tableScrollPane.setViewportView(table);

        //viewsPanel.add(tableScrollPane, "table");

        splitPane.setRightComponent(viewsPanel);

        mainPanel.add(splitPane, java.awt.BorderLayout.CENTER);

        fc.add(mainPanel, java.awt.BorderLayout.CENTER);

        controlsPanel.setLayout(new javax.swing.BoxLayout(controlsPanel, javax.swing.BoxLayout.Y_AXIS));

        accessoryPanel.setLayout(new java.awt.BorderLayout());

        accessoryPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 8, 0, 8));
        controlsPanel.add(accessoryPanel);

        formatPanel.setLayout(new java.awt.GridBagLayout());

        formatPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 0, 0, 0));
        filesOfTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        filesOfTypeLabel.setText("Format:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        formatPanel.add(filesOfTypeLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 250;
        formatPanel.add(filterComboBox, gridBagConstraints);

        formatSpringPanel.setLayout(null);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        formatPanel.add(formatSpringPanel, gridBagConstraints);

        controlsPanel.add(formatPanel);

        buttonsPanel.setLayout(new java.awt.GridBagLayout());

        buttonsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 20, 0, 20));
        newFolderButton.setText("New Folder");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        buttonsPanel.add(newFolderButton, gridBagConstraints);

        cancelOpenPanel.setLayout(new java.awt.GridLayout(1, 0, 8, 0));

        cancelButton.setText("Cancel");
        cancelOpenPanel.add(cancelButton);

        approveButton.setText("Open");
        cancelOpenPanel.add(approveButton);

        buttonsPanel.add(cancelOpenPanel, new java.awt.GridBagConstraints());

        controlsPanel.add(buttonsPanel);

        fc.add(controlsPanel, java.awt.BorderLayout.SOUTH);

        //add(fc, java.awt.BorderLayout.CENTER);
        // End of form definition

        // Tweak visual properties
        int dividerSize = UIManager.getInt("FileChooser.splitPaneDividerSize");
        if (dividerSize != 0) {
            splitPane.setDividerSize(dividerSize);
        }
        splitPane.putClientProperty("Quaqua.SplitPane.style", "bar");
        separator.putClientProperty("Quaqua.Component.visualMargin", new Insets(3, 0, 3, 0));
        if (QuaquaManager.getBoolean("FileChooser.enforceQuaquaTreeUI")) {
            sidebarTree.setUI((TreeUI) QuaquaTreeUI.createUI(sidebarTree));
        }
        sidebarTree.putClientProperty("Quaqua.Tree.style", "sideBar");
        sidebarTree.setRequestFocusEnabled(false);

        sidebarTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        int h;
        h = fileNameLabel.getPreferredSize().height;
        fileNameLabel.setMinimumSize(new Dimension(0, h));
        fileNameLabel.setPreferredSize(new Dimension(0, h));
        fileNameLabel.setMaximumSize(new Dimension(32767, h));

        h = fileNameTextField.getPreferredSize().height;
        fileNameTextField.setPreferredSize(new Dimension(0, h));
        fileNameTextField.setMinimumSize(new Dimension(0, h));
        fileNameTextField.setMaximumSize(new Dimension(32767, h));

        h = directoryComboBox.getPreferredSize().height;
        directoryComboBox.setPreferredSize(new Dimension(0, h));
        directoryComboBox.setMinimumSize(new Dimension(0, h));
        directoryComboBox.setMaximumSize(new Dimension(32767, h));

        Dimension d = new Dimension(28, 25);
        Dimension d2 = new Dimension(29, 25);
        //previousButton.setPreferredSize(d);
        //nextButton.setPreferredSize(d2);
        //tableToggleButton.setPreferredSize(d);
        //browserToggleButton.setPreferredSize(d2);
        //previousButton.setMinimumSize(d);
        //nextButton.setMinimumSize(d2);
        //tableToggleButton.setMinimumSize(d);
        //browserToggleButton.setMinimumSize(d2);

        //previousButton.setVisible(false);
        //nextButton.setVisible(false);
        //tableToggleButton.setVisible(false);
        //browserToggleButton.setVisible(false);

        h = navigationButtonsPanel.getPreferredSize().height;
        navigationButtonsPanel.setMinimumSize(new Dimension(0, h));
        navigationButtonsPanel.setPreferredSize(new Dimension(0, h));
        navigationButtonsPanel.setMaximumSize(new Dimension(32767, h));

        h = filesOfTypeLabel.getPreferredSize().height;
        filesOfTypeLabel.setMinimumSize(new Dimension(0, h));
        filesOfTypeLabel.setPreferredSize(new Dimension(0, h));
        filesOfTypeLabel.setMaximumSize(new Dimension(32767, h));

        h = filterComboBox.getPreferredSize().height;
        filterComboBox.setPreferredSize(new Dimension(0, h));
        filterComboBox.setMinimumSize(new Dimension(0, h));
        filterComboBox.setMaximumSize(new Dimension(32767, h));

        //Configure JBrowser
        LeopardFileRenderer fileRenderer = new LeopardFileRenderer(
                fc,
                UIManager.getIcon("Browser.expandingIcon"),
                UIManager.getIcon("Browser.expandedIcon"),
                UIManager.getIcon("Browser.selectedExpandingIcon"),
                UIManager.getIcon("Browser.selectedExpandedIcon"),
                UIManager.getIcon("Browser.focusedSelectedExpandingIcon"),
                UIManager.getIcon("Browser.focusedSelectedExpandedIcon"));
        browser.setColumnCellRenderer(fileRenderer);
        if (fc.isMultiSelectionEnabled()) {
            browser.setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        } else {
            browser.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        }
        browser.setModel(getTreeModel());
        browser.setPrototypeCellValue(getFileSystemTreeModel().getPrototypeValue());
        browser.addTreeSelectionListener(createBrowserSelectionListener(fc));
        browser.addMouseListener(createDoubleClickListener(fc));
        browser.setFixedCellWidth(170);
        browserScrollPane.putClientProperty("Quaqua.Component.visualMargin", new Insets(3, 2, 3, 2));
        browserScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        browser.setShowCellTipOrigin((Point) UIManager.get("FileChooser.cellTipOrigin"));
        browser.setShowCellTips(true);

        // Configure Sidebar Panel
        sidebarScrollPane.putClientProperty("Quaqua.Component.visualMargin", new Insets(3, 2, 3, 2));

        // Configure Format Panel
        formatPanel.setVisible(fc.getChoosableFileFilters().length > 1);

        // Configure Accessory Panel
        JComponent accessory = fc.getAccessory();
        if (accessory != null) {
            getAccessoryPanel().add(accessory);
        } else {
            accessoryPanel.setVisible(false);
        }

        // Configure filename Panel
        separator.putClientProperty("Quaqua.Component.visualMargin", new Insets(3, 0, 3, 0));

        // Text assignment
        newFolderButton.setText(newFolderButtonText);
        newFolderButton.setToolTipText(newFolderToolTipText);
        fileNameLabel.setText(fileNameLabelText);
        fileNameLabel.setDisplayedMnemonic(fileNameLabelMnemonic);

        approveButton.setText(getApproveButtonText(fc));
        // Note: Metal does not use mnemonics for approve and cancel
        approveButton.addActionListener(getApproveSelectionAction());
        approveButton.setToolTipText(getApproveButtonToolTipText(fc));

        cancelButton.setText(cancelButtonText);
        cancelButton.setToolTipText(cancelButtonToolTipText);
        cancelButton.addActionListener(getCancelSelectionAction());

        if (!fc.getControlButtonsAreShown()) {
            cancelButton.setVisible(false);
            approveButton.setVisible(false);
        }
        // End of Text assignment

        // Model and Renderer assignment
        directoryComboBoxModel = createDirectoryComboBoxModel(fc);
        directoryComboBox.setModel(directoryComboBoxModel);
        directoryComboBox.setRenderer(createDirectoryComboBoxRenderer(fc));
        sidebarTree.setModel(sidebarTreeModel = new SidebarTreeModel(fc, new TreePath(getFileSystemTreeModel().getRoot()), getFileSystemTreeModel()));
        sidebarTree.setCellRenderer(createSidebarCellRenderer(fc));
        for (int i = sidebarTree.getRowCount() - 1; i >= 0; i--) {
            sidebarTree.expandRow(i);
        }

        filterComboBoxModel = createFilterComboBoxModel();
        filterComboBox.setModel(filterComboBoxModel);
        filterComboBox.setRenderer(createFilterComboBoxRenderer());
        // Model and Renderer assignment

        // Listener assignment
        directoryComboBox.addActionListener(directoryComboBoxAction);
        newFolderButton.addActionListener(getNewFolderAction());
        fileNameTextField.addFocusListener(new SaveTextFocusListener());
        fileNameTextField.getDocument().addDocumentListener(new SaveTextDocumentListener());
        fileNameTextField.addActionListener(getApproveSelectionAction());
        sidebarTree.addTreeSelectionListener(createSidebarSelectionListener(fc));
        // End of listener assignment

        // Focus traversal
        browserScrollPane.setFocusable(false);
        browserScrollPane.getVerticalScrollBar().setFocusable(false);
        browserScrollPane.getHorizontalScrollBar().setFocusable(false);
        sidebarScrollPane.setFocusable(false);
        sidebarScrollPane.getVerticalScrollBar().setFocusable(false);
        sidebarScrollPane.getHorizontalScrollBar().setFocusable(false);

        // Drag and drop assignment
        fileTransferHandler = new FileTransferHandler(fc);
        Component[] dropComponents = {
            fc,
            accessoryPanel,
            approveButton,
            browser,
            browserScrollPane,
            //browserToggleButton,
            buttonsPanel,
            cancelButton,
            controlsPanel,
            directoryComboBox,
            fileNameLabel,
            fileNamePanel,
            fileNameSpringPanel,
            fileNameTextField,
            filesOfTypeLabel,
            filterComboBox,
            formatPanel,
            formatSpringPanel,
            mainPanel,
            navigationButtonsPanel,
            navigationPanel,
            navigationSpringPanel,
            newFolderButton,
            //nextButton,
            //previousButton,
            separator,
            splitPane,
            //table,
            //tableScrollPane,
            //tableToggleButton,

            viewsPanel,
            sidebarTree,
            sidebarScrollPane
        };
        for (int i = 0; i < dropComponents.length; i++) {
            new DropTarget(dropComponents[i], DnDConstants.ACTION_COPY, fileTransferHandler);
        }
        // End of drag and drop assignment

        // Change component visibility to match the dialog type
        boolean isSave = (fc.getDialogType() == JFileChooser.SAVE_DIALOG) || (fc.getDialogType() == JFileChooser.CUSTOM_DIALOG);
        fileNameTextField.setEnabled(isSave);
        fileNamePanel.setVisible(isSave);

        // Preview column
        browser.setPreviewRenderer((isSave) ? null : new FilePreview(fc));

        // Button state
        updateApproveButtonState();

        // Configure size of split pane
        splitPane.setPreferredSize(new Dimension(518, 298));
        splitPane.setMinimumSize(new Dimension(400, 80));

        // register key events with window
        ActionMap am = mainPanel.getActionMap();
        InputMap im = mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        for (int i = 0; i < KEYSTROKES.length; ++i) {
            im.put(KEYSTROKES[i], KEYSTROKES[i]);
            am.put(KEYSTROKES[i], keyListenerAction);
        }
        controlsPanel.setActionMap(am);
        controlsPanel.setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, im);
        fileNamePanel.setActionMap(am);
        fileNamePanel.setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, im);

        // Enforce layout, so that the selected file is visible when the
        // file chooser is opened with its preferred size.
        Dimension ps = fc.getPreferredSize();
        fc.setBounds(0, 0, ps.width, ps.height);
        fc.doLayout();
    }

    public void uninstallComponents(JFileChooser fc) {
        fc.removeAll();

        // Dispose model
        model.dispose();

        // Remove listeners on UI components
        cancelButton.removeActionListener(getCancelSelectionAction());
        approveButton.removeActionListener(getApproveSelectionAction());
        fileNameTextField.removeActionListener(getApproveSelectionAction());
    }

    /**
     * Installs listeners.
     * We install the same listeners as BasicFileChooserUI plus an
     * AncestorListener and a property change listener.
     */
    protected void installListeners(JFileChooser fc) {
        super.installListeners(fc);
        ancestorListener = createAncestorListener(fc);
        if (ancestorListener != null) {
            fc.addAncestorListener(ancestorListener);
        }
        fc.addPropertyChangeListener(filterComboBoxModel);
    }

    protected void uninstallListeners(JFileChooser fc) {
        super.uninstallListeners(fc);
        if (ancestorListener != null) {
            fc.removeAncestorListener(ancestorListener);
        }
        fc.removePropertyChangeListener(filterComboBoxModel);
    }

    private Locale getLocale() {
        try {
            return getFileChooser().getLocale();
        } catch (IllegalComponentStateException e) {
            return Locale.getDefault();
        }
    }

    protected void installStrings(JFileChooser fc) {
        super.installStrings(fc);

        Locale l;
        try {
            l = getLocale();
        } catch (IllegalComponentStateException e) {
            l = Locale.getDefault();
        }


        // FIXME - We must not read these strings from the UIManager, as long
        //         as we don't provide them with our own Look and Feel. This
        //         is, because these strings are version dependent, and thus
        //         are not necessarily in sync with what we need in our UI.
        chooseButtonText = UIManager.getString("FileChooser.chooseButtonText"/*,l*/);

        fileNameLabelMnemonic = UIManager.getInt("FileChooser.fileNameLabelMnemonic");
        fileNameLabelText = UIManager.getString("FileChooser.fileNameLabelText"/*,l*/);
        // XXX - Localize "Save as:" text.
        //if (fileNameLabelText == null || fileNameLabelText.charAt(fileNameLabelText.length() -1) != ':') fileNameLabelText = "Save as:";

        filesOfTypeLabelMnemonic = UIManager.getInt("FileChooser.filesOfTypeLabelMnemonic");
        filesOfTypeLabelText = UIManager.getString("FileChooser.filesOfTypeLabelText"/*,l*/);

        upFolderToolTipText = UIManager.getString("FileChooser.upFolderToolTipText"/*,l*/);
        upFolderAccessibleName = UIManager.getString("FileChooser.upFolderAccessibleName"/*,l*/);

        homeFolderToolTipText = UIManager.getString("FileChooser.homeFolderToolTipText"/*,l*/);
        homeFolderAccessibleName = UIManager.getString("FileChooser.homeFolderAccessibleName"/*,l*/);

        cancelButtonText = UIManager.getString("FileChooser.cancelButtonText"/*,l*/);
        cancelButtonToolTipText = UIManager.getString("FileChooser.cancelToolTipText"/*,l*/);

        newFolderButtonText = UIManager.getString("FileChooser.newFolderButtonText"/*,l*/);
        newFolderAccessibleName = UIManager.getString("FileChooser.newFolderAccessibleName"/*,l*/);

        newFolderToolTipText = UIManager.getString("FileChooser.newFolderToolTipText"/*,l*/);
        newFolderAccessibleName = UIManager.getString("FileChooser.newFolderAccessibleName"/*,l*/);

        // New Folder Dialog
        newFolderErrorText = getString("FileChooser.newFolderErrorText", l, "Error occured during folder creation");
        newFolderExistsErrorText = getString("FileChooser.newFolderExistsErrorText", l, "That name is already taken");
        // FIXME - There is no "FileChooser.newFolderButtonText", so we use the newFolderTitleText.
        newFolderButtonText = getString("FileChooser.newFolderButtonText", l, "New Folder");
        newFolderTitleText = getString("FileChooser.newFolderTitleText", l, "New Folder");
        newFolderDialogPrompt = getString("FileChooser.newFolderPromptText", l, "Name of new folder:");
        newFolderDefaultName = getString("FileChooser.untitledFolderName", l, "untitled folder");
        newFolderToolTipText = UIManager.getString("FileChooser.newFolderToolTipText"/*, l*/);
        newFolderAccessibleName = getString("FileChooser.newFolderAccessibleName", l, newFolderTitleText);
    }

    /**
     * FIXME - This could be moved up to BasicFileChooserUI.
     */
    public JPanel getAccessoryPanel() {
        return accessoryPanel;
    }

    /**
     * Gets a locale dependent string.
     */
    private String getString(String string, Locale l, String defaultValue) {
        String value = UIManager.getString(string/*, l*/);
        return (value == null) ? defaultValue : value;
    }

    /**
     * Creates an AncestorListener.
     * The AncestorListener is used to take an action when the JFileChooser becomes
     * showing on screen.
     */
    protected AncestorListener createAncestorListener(JFileChooser fc) {
        return new FileChooserAncestorListener();
    }

    public void createModel() {
        JFileChooser fc = getFileChooser();

        // FIXME - We should not overwrite the FileSystemView attribute
        // of the JFileChooser.
        fc.setFileSystemView(QuaquaFileSystemView.getQuaquaFileSystemView());

        // FIXME - We should not overwrite the FileView attribute
        // of the JFileChooser.
        if (QuaquaManager.getBoolean("FileChooser.speed")) {
            fileView = new BasicFileView();
        } else {
            fileView = QuaquaFileSystemView.getQuaquaFileSystemView().createFileView(fc);
        }
        fc.setFileView(fileView);

        model = new AliasFileSystemTreeModel(fc);
        subtreeModel = new SubtreeTreeModel(model);
    }

    public SubtreeTreeModel getTreeModel() {
        return subtreeModel;
    }

    public AliasFileSystemTreeModel getFileSystemTreeModel() {
        return model;
    }

    /**
     * The array contains the selected dir(s) of the JFileChooser.
     * All files have an absolute path.
     * If no dir is selected, the length of the array is 0.
     * Always returns a non-null value.
     * All array elements are non-null.
     */
    private File[] getSelectedFiles() {
        JFileChooser fc = getFileChooser();

        if (fc.isMultiSelectionEnabled()) {
            File[] selectedFiles = fc.getSelectedFiles();
            ArrayList list = new ArrayList(selectedFiles.length);
            for (int i = 0; i < selectedFiles.length; i++) {
                if (selectedFiles[i] != null) {
                    if (selectedFiles[i].isAbsolute()) {
                        list.add(selectedFiles[i]);
                    } else {
                        list.add(new File(fc.getCurrentDirectory(), selectedFiles[i].getName()));
                    }
                }
            }
            return (File[]) list.toArray(new File[list.size()]);
        } else {
            File f = fc.getSelectedFile();
            if (f == null) {
                return new File[0];
            } else {
                if (f.isAbsolute()) {
                    return new File[]{f};
                } else {
                    return new File[]{new File(fc.getCurrentDirectory(), f.getName())};
                }
            }
        }
    }

    /**
     * Updates the selection in the JBrowser, to match the selected dir/s
     * of the JFileChooser.
     */
    private void updateSelection() {
        JFileChooser fc = getFileChooser();
        File[] files = getSelectedFiles();
        TreePath fullPath = null;

        boolean isAtLeastOneFileSelected = false;
        boolean isAtLeastOneDirSelected = false;

        if (files.length != 0) {
            TreePath[] paths = new TreePath[files.length];
            ArrayList list = new ArrayList(paths.length);

            TreePath commonParentPath = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (!file.isAbsolute()) {
                    file = new File(fc.getCurrentDirectory(), file.getPath());
                }

                TreePath selectionPath = subtreeModel.toFullPath(browser.getSelectionPath());
                fullPath = model.toPath(file, selectionPath);
                /*
                fullPath = subtreeModel.toPath(
                dir,
                subtreeModel.toFullPath(browser.getSelectionPath())
                );*/
                AliasFileSystemTreeModel.Node node = (AliasFileSystemTreeModel.Node) fullPath.getLastPathComponent();

                boolean isDirectory = !node.isLeaf();
                if (isDirectory) {
                    isAtLeastOneDirSelected = true;
                } else {
                    isAtLeastOneFileSelected = true;
                }

                if (files.length == 1 || !isDirectory || fc.isDirectorySelectionEnabled()) {
                    TreePath subPath = getTreeModel().toSubPath(fullPath);

                    // All parent paths must be equal

                    TreePath parentPath = (subPath == null) ? null : subPath.getParentPath();

                    if (list.size() == 0) {
                        commonParentPath = parentPath;
                    }
                    if (parentPath == null && commonParentPath == null || parentPath != null && commonParentPath != null && parentPath.equals(commonParentPath)) {
                        list.add(subPath);
                    }
                }
            }
            if (list.size() == 0 && files.length > 0) {
                list.add(fc.getFileSystemView().getParentDirectory(files[0]));
                /*
                // Set the dir name, if it does not exist
                if (! files[0].exists()) {
                setFileName(files[0].getName());
                }
                 */
            }

            if (!subtreeModel.isDescendant(fullPath)) {
                subtreeModel.setPathToRoot(new TreePath(model.getRoot()));
                // FIXME - We must honour multiple selection!!!
                browser.setSelectionPath(fullPath);
                // FIXME - Select the dir system root in the sidebar!!!
                sidebarTree.clearSelection();
            } else {
                browser.setSelectionPaths((TreePath[]) list.toArray(new TreePath[list.size()]));
            }

            if (files.length == 1) {
                AliasFileSystemTreeModel.Node node = (AliasFileSystemTreeModel.Node) fullPath.getLastPathComponent();
                if (node.isLeaf() || !files[0].exists()) {
                    setFileName(files[0].getName());
                }
            }
        }

        if (fullPath != null && fullPath.getPathCount() > 0) {
            AliasFileSystemTreeModel.Node node = (AliasFileSystemTreeModel.Node) fullPath.getLastPathComponent();

            directoryComboBoxModel.setPath(
                    (node.isLeaf()) ? fullPath.getParentPath() : fullPath);
        }


        if (files.length == 1) {
            ensureFileIsVisible(fc, files[0]);
        }
        updateApproveButtonState();
    }

    /**
     * Returns true, if the dir name field contains a dir name.
     */
    private boolean isFileNameFieldValid() {
        String string = getFileName();
        return string != null && !string.equals("");
    }

    /**
     * Returns true, if the dir name field is visible.
     */
    private boolean isFileNameFieldVisible() {
        JFileChooser fc = getFileChooser();
        return (fc.getDialogType() == JFileChooser.SAVE_DIALOG) || (fc.getDialogType() == JFileChooser.CUSTOM_DIALOG);
    }

    private void updateApproveButtonState() {
        JFileChooser fc = getFileChooser();

        if (fc.getControlButtonsAreShown()) {
            File[] files = getSelectedFiles();

            boolean isEnabled = true;
            boolean isSaveDialog = fc.getDialogType() == JFileChooser.SAVE_DIALOG;
            boolean isFileSelected = false;
            boolean isDirectorySelected = false;
            for (int i = 0; i < files.length; i++) {
                if (files[i].exists()) {
                    if (files[i].isDirectory() && fc.isTraversable(files[i])) {
                        isDirectorySelected = true;
                    } else {
                        isFileSelected = true;
                    }
                    isEnabled &= isSaveDialog || fc.accept(files[i]);
                    if (!isEnabled) {
                        System.err.println("ACCEPT? " + fc.accept(files[i]) + " " + files[i]);
                    }
                }
            }

            switch (fc.getFileSelectionMode()) {
                case JFileChooser.FILES_ONLY:
                    isEnabled &= isFileSelected || isFileNameFieldVisible() && isFileNameFieldValid();
                    break;
                case JFileChooser.DIRECTORIES_ONLY:
                    // Note: in the following expression we must not check
                    // whether isDirectorySelected is true, because a
                    // the file chooser always shows a directory of some kind
                    // in its view.
                    isEnabled &= /*isDirectorySelected &&*/ !isFileSelected;
                    break;
                case JFileChooser.FILES_AND_DIRECTORIES:
                    isEnabled &= true;
                    break;
            }
            setApproveButtonEnabled(isEnabled);
        }
    }

    private void setApproveButtonEnabled(boolean isEnabled) {
        JFileChooser fc = getFileChooser();
        if (fc.getControlButtonsAreShown()) {
            approveButton.setEnabled(isEnabled);
            if (isEnabled) {
                JRootPane rp = approveButton.getRootPane();
                if (rp != null) {
                    rp.setDefaultButton(approveButton);
                }
            }
        }
    }

    private void updateApproveButtonText() {
        JFileChooser fc = getFileChooser();

        approveButton.setText(getApproveButtonText(fc));
        approveButton.setToolTipText(getApproveButtonToolTipText(fc));
        approveButton.setMnemonic(getApproveButtonMnemonic(fc));
        //cancelButton.setToolTipText(getCancelButtonToolTipText(fc));
    }

    private void updateFileChooserFromBrowser() {
        JFileChooser fc = getFileChooser();
        TreePath[] paths = browser.getSelectionPaths();

        // Determine the selected files. If multiple files are selected,
        // we strip directories from this list, if the JFileChooser does
        // not allow directory selection.
        int count = 0;
        File[] files = new File[(paths == null) ? 0 : paths.length];
        ArrayList list = new ArrayList(files.length);
        for (int i = 0; i < files.length; i++) {
            AliasFileSystemTreeModel.Node node = (AliasFileSystemTreeModel.Node) paths[i].getLastPathComponent();
            File file = node.lazyGetResolvedFile();
            if (file != null && fc.accept(file)) {
                boolean isDirectory = !node.isLeaf();
                if (files.length == 1 || !isDirectory || fc.isDirectorySelectionEnabled()) {
                    list.add(file);
                }
            }
        }


        if (fc.isMultiSelectionEnabled()) {
            fc.setSelectedFiles((File[]) list.toArray(new File[list.size()]));
        } else {
            fc.setSelectedFile((list.size() > 0) ? (File) list.get(0) : null);
        }
    }

    protected TreeSelectionListener createBrowserSelectionListener(JFileChooser fc) {
        return new BrowserSelectionListener();
    }

    /**
     * Selection listener for the list of files and directories.
     */
    protected class BrowserSelectionListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent e) {
            if (isAdjusting != 0) {
                return;
            }
            JFileChooser fc = getFileChooser();
            FileSystemView fsv = fc.getFileSystemView();
            TreePath path = browser.getSelectionPath();

            if (path != null) {
                model.lazyInvalidatePath(path);
                model.validatePath(path);
            }

            updateFileChooserFromBrowser();
        }
    }

    /* The following methods are used by the PropertyChange Listener */
    private void doSelectedFileChanged(PropertyChangeEvent e) {
        updateSelection();
    }

    private void doSelectedFilesChanged(PropertyChangeEvent e) {
        updateSelection();
    }

    private void doDirectoryChanged(PropertyChangeEvent e) {
        File dir = (File) e.getNewValue();
        File selectedFile = getFileChooser().getSelectedFile();
        /*
        // ignore directory changes to the directory of the selected file
        if (dir != null && selectedFile != null && 
        (dir.equals(selectedFile) || dir.equals(selectedFile.getParentFile()))) {
        return; // nothing do to
        }*/


        TreePath selectionPath = (browser.getSelectionPath() == null) ? null : subtreeModel.toFullPath(browser.getSelectionPath());
        TreePath dirPath = model.toPath(dir, selectionPath);
        TreePath fullDirPath = dirPath;

        // XXX - This code occurs two times in this class - move it into a method
        if (!subtreeModel.getPathToRoot().isDescendant(dirPath)) {
            FileInfo sidebarFileInfo = null;
            TreePath sidebarSelectionPath = null;
            for (Enumeration i = ((DefaultMutableTreeNode) sidebarTree.getModel().getRoot()).preorderEnumeration(); i.hasMoreElements();) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) i.nextElement();
                if (node instanceof FileInfo) {
                    FileInfo info = (FileInfo) node;
                    if (info.getResolvedFile() != null && info.getResolvedFile().equals(dir)) {
                        sidebarFileInfo = info;
                        sidebarSelectionPath = new TreePath(node.getPath());
                        break;
                    }
                }
            }
            if (sidebarFileInfo != null) {
                TreePath sidebarPath = model.toPath(sidebarFileInfo.getResolvedFile(), selectionPath);
                subtreeModel.setPathToRoot(sidebarPath);
                sidebarTree.setSelectionPath(sidebarSelectionPath);
                dirPath = subtreeModel.toSubPath(dirPath);
            } else {
                subtreeModel.setPathToRoot(new TreePath(model.getRoot()));
                sidebarTree.clearSelection();
            }
        }

        directoryComboBoxModel.setPath(fullDirPath);
        browser.setSelectionPath(dirPath);
        browser.ensurePathIsVisible(dirPath);
        model.lazyInvalidatePath(dirPath);
    }

    private void doFilterChanged(PropertyChangeEvent e) {
        clearIconCache();

        model.invalidateAll();
//->the update did not occur from the browser        updateFileChooserFromBrowser();
        /*
        updateSelection();
        updateApproveButtonState();*/
        if (getFileChooser().isShowing()) {
            model.validatePath(subtreeModel.toFullPath(browser.getSelectionPath()));
            browser.repaint();
            browser.updatePreviewColumn();
        }

    }

    private void doFileViewChanged(PropertyChangeEvent e) {
        model.invalidateAll();
        if (getFileChooser().isShowing()) {
            model.validatePath(subtreeModel.toFullPath(browser.getSelectionPath()));
            browser.repaint();
            browser.updatePreviewColumn();
        }
    }

    private void doFileSelectionModeChanged(PropertyChangeEvent e) {
        //Commented out, because there is no reason for clearing the icon cache
        //in this situation.
        //clearIconCache();

        JFileChooser fc = getFileChooser();
        File currentDirectory = fc.getCurrentDirectory();
        //setFileName(null);
        updateApproveButtonText();
        updateApproveButtonState();
    }

    private void doMultiSelectionChanged(PropertyChangeEvent e) {
        if (getFileChooser().isMultiSelectionEnabled()) {
            browser.setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        } else {
            browser.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            getFileChooser().setSelectedFiles(null);
        }
    }

    private void doChoosableFilterChanged(PropertyChangeEvent e) {
        JFileChooser fc = getFileChooser();
        boolean isChooserVisible = ((FileFilter[]) e.getNewValue()).length > 1;
        formatPanel.setVisible(isChooserVisible);
        model.invalidateAll();
        /*
        updateSelection();
        updateApproveButtonState();*/
//->the update did not occur from the browser        updateFileChooserFromBrowser();
        updateApproveButtonState();
        if (fc.isShowing()) {
            model.validatePath(subtreeModel.toFullPath(browser.getSelectionPath()));
            browser.repaint();
            browser.updatePreviewColumn();
        }
    }

    private void doAccessoryChanged(PropertyChangeEvent e) {
        if (getAccessoryPanel() != null) {
            if (e.getOldValue() != null) {
                getAccessoryPanel().remove((JComponent) e.getOldValue());
            }
            JComponent accessory = (JComponent) e.getNewValue();
            if (accessory != null) {
                getAccessoryPanel().add(accessory, BorderLayout.CENTER);
            }
            accessoryPanel.setVisible(accessory != null);
        }
    }

    private void doApproveButtonTextChanged(PropertyChangeEvent e) {
        JFileChooser chooser = getFileChooser();
        approveButton.setText(getApproveButtonText(chooser));
        approveButton.setToolTipText(getApproveButtonToolTipText(chooser));
    }

    private void doDialogTypeChanged(PropertyChangeEvent e) {
        JFileChooser fc = getFileChooser();
        approveButton.setText(getApproveButtonText(fc));
        approveButton.setToolTipText(getApproveButtonToolTipText(fc));
        boolean isSave = isFileNameFieldVisible();
        fileNameTextField.setEnabled(isSave);
        fileNamePanel.setVisible(isSave);
        //model.setResolveAliasesToFiles(!isSave);
        // ResourceBundleUtil bundle = (ResourceBundleUtil) UIManager.get("Labels");
        // boolean isLocalized = bundle.getLocale().getLanguage().equals(getLocale().getLanguage());
        browser.setPreviewRenderer((isSave) ? null : new FilePreview(fc));
    }

    private void doApproveButtonMnemonicChanged(PropertyChangeEvent e) {
        // Note: Metal does not use mnemonics for approve and cancel
    }

    private void doControlButtonsChanged(PropertyChangeEvent e) {
        if (getFileChooser().getControlButtonsAreShown()) {
            addControlButtons();
        } else {
            removeControlButtons();
        }
    }

    private void doFileSystemViewChanged(PropertyChangeEvent e) {
        JFileChooser fc = getFileChooser();

        model = new AliasFileSystemTreeModel(fc);
        subtreeModel = new SubtreeTreeModel(model);

        browser.setModel(getTreeModel());
        sidebarTree.setModel(sidebarTreeModel = new SidebarTreeModel(fc, new TreePath(getFileSystemTreeModel().getRoot()), getFileSystemTreeModel()));
    }
    /*
     * Listen for filechooser property changes, such as
     * the selected dir changing, or the type of the dialog changing.
     */

    public PropertyChangeListener createPropertyChangeListener(final JFileChooser fc) {
        return new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                isAdjusting++;

                String s = e.getPropertyName();
                if (s.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
                    doSelectedFileChanged(e);
                } else if (s.equals(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY)) {
                    doSelectedFilesChanged(e);
                } else if (s.equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
                    doDirectoryChanged(e);
                } else if (s.equals(JFileChooser.FILE_FILTER_CHANGED_PROPERTY)) {
                    doFilterChanged(e);
                } else if (s.equals(JFileChooser.FILE_SYSTEM_VIEW_CHANGED_PROPERTY)) {
                    doFileSystemViewChanged(e);
                } else if (s.equals(JFileChooser.FILE_SELECTION_MODE_CHANGED_PROPERTY)) {
                    doFileSelectionModeChanged(e);
                } else if (s.equals(JFileChooser.MULTI_SELECTION_ENABLED_CHANGED_PROPERTY)) {
                    doMultiSelectionChanged(e);
                } else if (s.equals(JFileChooser.ACCESSORY_CHANGED_PROPERTY)) {
                    doAccessoryChanged(e);
                } else if (s.equals(JFileChooser.CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY)) {
                    doChoosableFilterChanged(e);
                } else if (s.equals(JFileChooser.APPROVE_BUTTON_TEXT_CHANGED_PROPERTY) ||
                        s.equals(JFileChooser.APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY)) {
                    doApproveButtonTextChanged(e);
                } else if (s.equals(JFileChooser.DIALOG_TYPE_CHANGED_PROPERTY)) {
                    doDialogTypeChanged(e);
                } else if (s.equals(JFileChooser.APPROVE_BUTTON_MNEMONIC_CHANGED_PROPERTY)) {
                    doApproveButtonMnemonicChanged(e);
                } else if (s.equals(JFileChooser.CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY)) {
                    doControlButtonsChanged(e);
                } else if (s.equals(JFileChooser.FILE_VIEW_CHANGED_PROPERTY)) {
                    doFileViewChanged(e);
                } else if (s.equals("componentOrientation")) {
                    /* FIXME - This needs JDK 1.4 to work.
                    ComponentOrientation o = (ComponentOrientation)e.getNewValue();
                    JFileChooser fc = (JFileChooser)e.getSource();
                    if (o != (ComponentOrientation)e.getOldValue()) {
                    fc.applyComponentOrientation(o);
                    }
                     */
                } else if (s.equals("ancestor")) {
                    if (e.getOldValue() == null && e.getNewValue() != null) {
                        // Ancestor was added, ensure path is visible and
                        // set initial focus
                        ensurePathIsVisible(subtreeModel.toFullPath(browser.getSelectionPath()));
                        if (fc.getDialogType() == JFileChooser.SAVE_DIALOG) {
                            fileNameTextField.selectAll();
                            fileNameTextField.requestFocus();
                        } else {
                            browser.requestFocus();
                        }
                    }
                }

                isAdjusting--;
            }
        };
    }

    protected void removeControlButtons() {
        cancelButton.setVisible(false);
        approveButton.setVisible(false);
    }

    protected void addControlButtons() {
        cancelButton.setVisible(true);
        approveButton.setVisible(true);
    }

    private void ensurePathIsVisible(TreePath path) {
        if (!subtreeModel.isDescendant(path.getPath())) {
            isAdjusting++;
            if (((AliasFileSystemTreeModel.Node) path.getLastPathComponent()).isLeaf()) {
                subtreeModel.setPathToRoot(path.getParentPath().getPath());
            } else {
                subtreeModel.setPathToRoot(path.getPath());
            }
            isAdjusting--;
        }
        browser.ensurePathIsVisible(path);
    }

    public String getFileName() {
        if (fileNameTextField != null) {
            return fileNameTextField.getText();
        } else {
            return null;
        }
    }

    public void setFileName(String filename) {
        if (fileNameTextField != null &&
                !fileNameTextField.hasFocus() &&
                (filename == null || !fileNameTextField.getText().equals(filename))) {
            fileNameTextField.setText(filename);
        }
    }

    protected DirectoryComboBoxRenderer createDirectoryComboBoxRenderer(JFileChooser fc) {
        return new DirectoryComboBoxRenderer();
    }

    protected SidebarRenderer createSidebarCellRenderer(JFileChooser fc) {
        return new SidebarRenderer();
    }

    protected TreeSelectionListener createSidebarSelectionListener(JFileChooser fc) {
        return new SidebarSelectionListener();
    }

    //
    // Renderer for DirectoryComboBox
    //
    class DirectoryComboBoxRenderer extends DefaultListCellRenderer {

        private Border border = new EmptyBorder(1, 0, 1, 0);
        IndentIcon ii = new IndentIcon();
        private JSeparator separator = new JSeparator();

        public DirectoryComboBoxRenderer() {
            separator.setPreferredSize(new Dimension(9, 9));
        }

        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected,
                boolean cellHasFocus) {


            // String objects are used to denote delimiters.
            if (value instanceof String) {
                super.getListCellRendererComponent(list, value, index, false, cellHasFocus);
                setText((String) value);
                setPreferredSize(new Dimension(10, 14));
                return this;
            }
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setPreferredSize(null);
            if (value instanceof File) {
                setText(value + " " + index);
                return this;
            }
            AliasFileSystemTreeModel.Node node = (AliasFileSystemTreeModel.Node) value;
            if (node == null) {
                return separator;
                /*
                File root = new File("/");
                setText(getFileChooser().getName(root));
                ii.icon = getFileChooser().getIcon(root);
                 */
            } else {
                setText(node.getUserName());
                ii.icon = node.getIcon();
            }
            ii.depth = 0;
            setIcon(ii);
            setBorder(border);
            return this;
        }
    }

    //
    // Renderer for Volumes list
    //
    // CHANGE This class is rewritten after adding support for the Quaqua.Tree.style property
    // CHANGE All methods except the getTreeCellRendererComponent(...) were
    // deleted and are no longer needed
    class SidebarRenderer extends DefaultTreeCellRenderer {

        public SidebarRenderer() {
            if (QuaquaManager.getBoolean("FileChooser.enforceQuaquaTreeUI")) {
                setUI((LabelUI) QuaquaLabelUI.createUI(this));
            }
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean isSelected, boolean isExpanded, boolean isLeaf,
                int row, boolean cellHasFocus) {
            super.getTreeCellRendererComponent(tree, value, isSelected,
                    isExpanded, isLeaf, row, false);

            if (value != null && value instanceof FileInfo) {
                FileInfo info = (FileInfo) value;
                setText(info.getUserName());

                if (isSpecialFolder(info)) {
                    setIcon(getSpecialFolderIcon(info));
                } else {
                    setIcon(info.getIcon());
                }
            }
            return this;
        }

        /**
         * Gets the special icon for the folder.
         * 
         * @param info The {@link FileInfo}.
         * @return The icon.
         **/
        private Icon getSpecialFolderIcon(FileInfo info) {
            // Load the icon from the UIDefaults table
            Icon icon = UIManager.getIcon("FileChooser.sideBarIcon." + info.getFile().getName());

            // If we somehow fail to load the icon, fall back to standard way
            if (icon == null) {
                icon = info.getIcon();
            }

            return icon;
        }

        /**
         * Gets whether the the {@link FileInfo} represents a "special" folder - a folder which
         * is visually different in the side bar than in the browser view of a file chooser.
         * 
         * @param info The {@link FileInfo}.
         * @return <code>true</code> if the OS is Mac OS X and the 
         */
        private boolean isSpecialFolder(FileInfo info) {
            // Only allow this for Mac OS X as directory structures are different on other OSs.
            if (!QuaquaManager.isOSX()) {
                return false;
            }

            File file = info.getFile();
            // Only directories can have special icons.
            if (file == null || file.isFile()) {
                return false;
            }

            if (file.getParentFile() != null) {
                String parentFile = file.getParentFile().getAbsolutePath();
                if (parentFile.equals(System.getProperty("user.home"))) {
                    // Look for user's home special folders
                    String name = file.getName();
                    return name.equals("Applications") || name.equals("Desktop") || name.equals("Documents") || name.equals("Downloads") || name.equals("Library") || name.equals("Movies") || name.equals("Music") || name.equals("Pictures") || name.equals("Public") || name.equals("Sites");
                } else if (parentFile.equals(computer.getAbsolutePath())) {
                    // Look for computer's special folders
                    String name = file.getName();
                    return name.equals("Applications") || name.equals("Library");
                } else if (!parentFile.equals(new File(computer, "Applications").getAbsolutePath())) {
                    // Look for Utility folder in the /Applications folder
                    return file.getName().equals("Utilities");
                }
            }
            // Nothing found - return null
            return false;
        }
    }
    final static int space = 10;

    class IndentIcon implements Icon {

        Icon icon = null;
        int depth = 0;

        public void paintIcon(Component c, Graphics g, int x, int y) {
            if (icon != null) {
                if (c.getComponentOrientation().isLeftToRight()) {
                    icon.paintIcon(c, g, x + depth * space, y);
                } else {
                    icon.paintIcon(c, g, x, y);
                }
            }
        }

        public int getIconWidth() {
            return (icon == null) ? depth * space : icon.getIconWidth() + depth * space;
        }

        public int getIconHeight() {
            return (icon == null) ? 0 : icon.getIconHeight();
        }
    }

    //
    // DataModel for DirectoryComboxbox
    //
    protected DirectoryComboBoxModel createDirectoryComboBoxModel(JFileChooser fc) {
        return new DirectoryComboBoxModel();
    }

    /**
     * Data model for a directory selection combo-box.
     * There is always one node in the tree model: the dir system root (aka
     * the comouter).
     */
    protected class DirectoryComboBoxModel extends AbstractListModel
            implements ComboBoxModel {

        TreePath path;
        AliasFileSystemTreeModel.Node selectedDirectory = null;
        JFileChooser chooser = getFileChooser();
        FileSystemView fsv = chooser.getFileSystemView();

        public DirectoryComboBoxModel() {
        }

        /**
         * Sets the path of the directory combo box.
         * TreePath<AliasFileSystemTreeModel.Node>
         */
        private void setPath(TreePath path) {
            if (this.path != null && this.path.getPathCount() > 0) {
                fireIntervalRemoved(this, 0, this.path.getPathCount() - 1);
            }
            this.path = path;
            if (this.path.getPathCount() > 0) {
                fireIntervalAdded(this, 0, this.path.getPathCount() - 1);
            }
            setSelectedItem(this.path.getLastPathComponent());
        }

        public void setSelectedItem(Object selectedItem) {
            AliasFileSystemTreeModel.Node node = (AliasFileSystemTreeModel.Node) selectedItem;
            this.selectedDirectory = node;
            fireContentsChanged(this, -1, -1);
        }

        public Object getSelectedItem() {
            return selectedDirectory;
        }

        public int getSize() {
            return (path == null) ? 0 : path.getPathCount();
        }

        public Object getElementAt(int index) {
            return path.getPathComponent(path.getPathCount() - index - 1);
        }
    }

    //
    // Renderer for Types ComboBox
    //
    protected FilterComboBoxRenderer createFilterComboBoxRenderer() {
        return new FilterComboBoxRenderer();
    }

    /**
     * Render different type sizes and styles.
     */
    public class FilterComboBoxRenderer extends DefaultListCellRenderer {

        private Border border = new EmptyBorder(1, 0, 1, 0);

        public Component getListCellRendererComponent(JList list,
                Object value, int index, boolean isSelected,
                boolean cellHasFocus) {

            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value != null && value instanceof FileFilter) {
                setText(((FileFilter) value).getDescription());
            }
            setBorder(border);

            return this;
        }
    }

    //
    // DataModel for Types Comboxbox
    //
    protected FilterComboBoxModel createFilterComboBoxModel() {
        return new FilterComboBoxModel();
    }

    /**
     * Data model for a type-face selection combo-box.
     */
    protected class FilterComboBoxModel
            extends AbstractListModel
            implements ComboBoxModel, PropertyChangeListener {

        protected FileFilter[] filters;

        protected FilterComboBoxModel() {
            super();
            filters = getFileChooser().getChoosableFileFilters();
        }

        public void propertyChange(PropertyChangeEvent e) {
            String prop = e.getPropertyName();
            if (prop == JFileChooser.CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY) {
                filters = (FileFilter[]) e.getNewValue();
                fireContentsChanged(this, -1, -1);
            } else if (prop == JFileChooser.FILE_FILTER_CHANGED_PROPERTY) {
                fireContentsChanged(this, -1, -1);
            }
        }

        public void setSelectedItem(Object filter) {
            if (filter != null) {
                getFileChooser().setFileFilter((FileFilter) filter);
                // Don't clear the filename field, when the user changes
                // the filename filter.
                // FIXME - Maybe we should disable the save
                // button when the name is not matched by the filter?
                //setFileName(null);
                fireContentsChanged(this, -1, -1);
            }
        }

        public Object getSelectedItem() {
            // Ensure that the current filter is in the list.
            // NOTE: we shouldnt' have to do this, since JFileChooser adds
            // the filter to the choosable filters list when the filter
            // is set. Lets be paranoid just in case someone overrides
            // setFileFilter in JFileChooser.
            FileFilter currentFilter = getFileChooser().getFileFilter();
            boolean found = false;
            if (currentFilter != null) {
                for (int i = 0; i < filters.length; i++) {
                    if (filters[i] == currentFilter) {
                        found = true;
                    }
                }
                if (found == false) {
                    getFileChooser().addChoosableFileFilter(currentFilter);
                }
            }
            return getFileChooser().getFileFilter();
        }

        public int getSize() {
            if (filters != null) {
                return filters.length;
            } else {
                return 0;
            }
        }

        public Object getElementAt(int index) {
            if (index > getSize() - 1) {
                // This shouldn't happen. Try to recover gracefully.
                return getFileChooser().getFileFilter();
            }
            if (filters != null) {
                return filters[index];
            } else {
                return null;
            }
        }
    }

    /**
     * Acts when DirectoryComboBox has changed the selected item.
     */
    protected class DirectoryComboBoxAction extends AbstractAction {

        protected DirectoryComboBoxAction() {
            super("DirectoryComboBoxAction");
        }

        public void actionPerformed(ActionEvent e) {
            if (isAdjusting != 0) {
                return;
            }

            AliasFileSystemTreeModel.Node chosenNode = (AliasFileSystemTreeModel.Node) directoryComboBox.getSelectedItem();
            if (chosenNode != null) {

                JFileChooser fc = getFileChooser();
                File dir = chosenNode.getResolvedFile();
                isAdjusting++;
                TreePath fullPath = getFileSystemTreeModel().toPath(dir, subtreeModel.getPathToRoot());
                getFileSystemTreeModel().lazyInvalidatePath(fullPath);
                fc.setCurrentDirectory(dir);
                TreePath dirPath = subtreeModel.toSubPath(fullPath);
                TreePath selectionPath = dirPath;

                TreePath sidebarSelectionPath = null;
                FileInfo sidebarFileInfo = null;
                // XXX - This code occurs two times in this class - move it into a method
                for (Enumeration i = ((DefaultMutableTreeNode) sidebarTree.getModel().getRoot()).preorderEnumeration(); i.hasMoreElements();) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) i.nextElement();
                    if (node instanceof FileInfo) {
                        FileInfo info = (FileInfo) node;
                        if (info.getResolvedFile() != null && info.getResolvedFile().equals(dir)) {
                            sidebarFileInfo = info;
                            sidebarSelectionPath = new TreePath(node.getPath());
                            break;
                        }
                    }
                }
                if (sidebarFileInfo != null) {
                    TreePath sidebarPath = model.toPath(sidebarFileInfo.getResolvedFile(), selectionPath);
                    subtreeModel.setPathToRoot(sidebarPath);
                    sidebarTree.setSelectionPath(sidebarSelectionPath);
                    dirPath = subtreeModel.toSubPath(dirPath);
                } else {
                    subtreeModel.setPathToRoot(new TreePath(model.getRoot()));
                    sidebarTree.clearSelection();
                }
                browser.setSelectionPath(dirPath);
                browser.ensurePathIsVisible(dirPath);
                model.lazyInvalidatePath(dirPath);
                isAdjusting--;
            }
        }
    }

    protected JButton getApproveButton(JFileChooser fc) {
        return approveButton;
    }

    public Action getApproveSelectionAction() {
        return approveSelectionAction;
    }

    protected class DoubleClickListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            // Note: We must not react on mouse clicks with clickCount=1.
            //       Because this interfers with the mouse handling code in
            //       the JBrowser which does list selection.
            JFileChooser fc = getFileChooser();
            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2 && fc.getDialogType() != JFileChooser.SAVE_DIALOG) {
                maybeApproveSelection(false);
            }
        }
    }

    /**
     * This method is called, when the user double clicks the JBrowser, or
     * when she clicks at the approve button.
     *
     * @param allowDirectories true to allow selection of directories when
     * isMultiSelectionEnabled == true
     */
    private void maybeApproveSelection(boolean allowDirectories) {
        JFileChooser fc = getFileChooser();
        File selectedFile = null;
        File[] selectedFiles = null;

        String filename = null;
        if (isFileNameFieldVisible()) {
            filename = getFileName();
            if (filename.equals("")) {
                filename = null;
            }
        }

        if (fc.isMultiSelectionEnabled()) {
            TreePath[] selectedPaths = browser.getSelectionPaths();
            if (filename != null) {
                selectedFiles = new File[]{
                            new File(((AliasFileSystemTreeModel.Node) selectedPaths[0].getLastPathComponent()).getResolvedFile().getParent(), filename)
                        };
            } else {
                selectedFiles = new File[selectedPaths.length];
                for (int i = 0; i < selectedPaths.length; i++) {
                    selectedFiles[i] = ((AliasFileSystemTreeModel.Node) selectedPaths[i].getLastPathComponent()).getResolvedFile();
                }
            }

            // see if this is a single directory. If so, don't allow
            // double-click to select it.
            if (!allowDirectories && selectedFiles.length == 1 && selectedFiles[0].isDirectory()) {
                // block selection
                return;
            }
        } else {
            TreePath selectionPath = browser.getSelectionPath();
            if (selectionPath == null) {
                // Abort we cannot approve empty selection
                return;
            }
            AliasFileSystemTreeModel.Node node = (AliasFileSystemTreeModel.Node) browser.getSelectionPath().getLastPathComponent();
            selectedFile = node.getResolvedFile();
            if (filename != null) {
                selectedFile = new File((!node.isLeaf()) ? selectedFile : selectedFile.getParentFile(), filename);
            } else if (fc.getFileSelectionMode() == JFileChooser.FILES_ONLY && !node.isLeaf()) {
                // Abort we cannot approve a directory
                return;
            }
        }

        if (selectedFiles != null || selectedFile != null) {
            if (selectedFiles != null) {
                fc.setSelectedFiles(selectedFiles);
            } else if (fc.isMultiSelectionEnabled()) {
                fc.setSelectedFiles(new File[]{selectedFile});
            } else {
                fc.setSelectedFile(selectedFile);
            }
            fc.approveSelection();
        } else {
            if (fc.isMultiSelectionEnabled()) {
                fc.setSelectedFiles(null);
            } else {
                fc.setSelectedFile(null);
            }
            fc.cancelSelection();
        }

    }

    // *****************************
    // ***** Directory Actions *****
    // *****************************
    public Action getNewFolderAction() {
        return newFolderAction;
    }

    /**
     * Creates a new folder.
     */
    protected class NewFolderAction extends AbstractAction {

        protected NewFolderAction() {
            super("New Folder");
        }

        private String showNewFolderDialog() {
            JOptionPane optionPane = new JOptionPane(
                    newFolderDialogPrompt,
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION);
            optionPane.setWantsInput(true);
            optionPane.setInitialSelectionValue(newFolderDefaultName);
            JDialog dialog = optionPane.createDialog(getFileChooser(), newFolderTitleText);
            dialog.setVisible(true);
            dialog.dispose();

            return (optionPane.getInputValue() == JOptionPane.UNINITIALIZED_VALUE) ? null : (String) optionPane.getInputValue();
        }

        public void actionPerformed(ActionEvent actionevent) {
            JFileChooser fc = getFileChooser();
            String newFolderName = showNewFolderDialog();

            if (newFolderName != null) {

                File newFolder;
                AliasFileSystemTreeModel.Node node = (AliasFileSystemTreeModel.Node) browser.getSelectionPath().getLastPathComponent();
                File currentFile = node.getResolvedFile();
                if (node.isLeaf()) {
                    currentFile = currentFile.getParentFile();
                }
                newFolder = new File(currentFile, newFolderName);
                if (newFolder.exists()) {
                    JOptionPane.showMessageDialog(
                            fc,
                            newFolderExistsErrorText,
                            newFolderTitleText, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    newFolder.mkdir();
                    fc.rescanCurrentDirectory();
                    fc.setCurrentDirectory(newFolder);
                    if (fc.isMultiSelectionEnabled()) {
                        fc.setSelectedFiles(new File[]{newFolder});
                    } else {
                        fc.setSelectedFile(newFolder);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            fc,
                            newFolderErrorText,
                            newFolderTitleText, JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    protected class SaveTextFocusListener implements FocusListener {

        public void focusGained(FocusEvent focusevent) {
            updateApproveButtonState();
        }

        public void focusLost(FocusEvent focusevent) {
            /* empty */
        }
    }

    protected class SaveTextDocumentListener implements DocumentListener {

        public void insertUpdate(DocumentEvent documentevent) {
            textChanged();
        }

        public void removeUpdate(DocumentEvent documentevent) {
            textChanged();
        }

        public void changedUpdate(DocumentEvent documentevent) {
            //textChanged();
        }

        private void textChanged() {
            if (isAdjusting != 0) {
                return;
            }

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    JFileChooser fc = getFileChooser();
                    AliasFileSystemTreeModel.Node node = (AliasFileSystemTreeModel.Node) browser.getSelectionPath().getLastPathComponent();
                    File file = node.getResolvedFile();
                    if (fileNameTextField.getText().length() != 0) {
                        if (!node.isLeaf()) {
                            // Don't change the current directory when the user is entering
                            // text into the text field. It confuses our users!
                            // Instead, we update the state of the approve button
                            // only, and then we return!
                            // dir = new File(dir, fileNameTextField.getText());
                            updateApproveButtonState();
                            return;
                        } else {
                            file = new File(fc.getFileSystemView().getParentDirectory(file), fileNameTextField.getText());
                        }
                    }
                    if (fc.isMultiSelectionEnabled()) {
                        fc.setSelectedFiles(new File[]{file});
                    } else {
                        fc.setSelectedFile(file);
                    }
                    updateApproveButtonState();
                }
            });
        }
    }

    /**
     * The FileChooserAncestorListener listens for visibility changes of
     * the JFileChooser.
     * This is used to do validations (refreshes) of the tree model only,
     * when the JFileChooser is showing.
     */
    private class FileChooserAncestorListener implements AncestorListener {

        public void ancestorAdded(AncestorEvent event) {
            if (model != null) {
                model.setAutoValidate(QuaquaManager.getBoolean("FileChooser.autovalidate"));
                model.validatePath(browser.getSelectionPath());
                if (sidebarTreeModel != null) {
                    sidebarTreeModel.lazyValidate();
                }
            }
            // We update the approve button state here, because the approve 
            // button can only be made the default button, if it has a root pane
            // ancestor.
            updateApproveButtonState();
            JFileChooser fc = getFileChooser();
            if (fc.getSelectedFile() != null) {
                ensureFileIsVisible(fc, fc.getSelectedFile());
            }
            //QuaquaUtilities.setWindowAlpha(SwingUtilities.getWindowAncestor(event.getAncestorParent()), 230);
        }

        public void ancestorRemoved(AncestorEvent event) {
            if (model != null) {
                model.setAutoValidate(false);
                model.stopValidation();
                if (browser.getSelectionPath() != null) {
                    model.invalidatePath(browser.getSelectionPath());
                }
                clearIconCache();
            }
        }

        public void ancestorMoved(AncestorEvent event) {
        }
    }
    // *******************************************************
    // ************ FileChooserUI PLAF methods ***************
    // *******************************************************

    /**
     * API method of FileChooserUI.
     */
    public void ensureFileIsVisible(JFileChooser fc, final File f) {
        if (browser.getSelectionPaths() != null) {
            TreePath[] paths = browser.getSelectionPaths();
            for (int i = 0; i < paths.length; i++) {
                if (((AliasFileSystemTreeModel.Node) paths[i].getLastPathComponent()).getFile().equals(f)) {
                    browser.ensurePathIsVisible(paths[i]);
                    return;
                }
            }
        } else {
            TreePath fullPath = getFileSystemTreeModel().toPath(f,
                    subtreeModel.getPathToRoot());
            TreePath subPath = getTreeModel().toSubPath(fullPath);
            if (subPath == null) {
                isAdjusting++;
                getTreeModel().setPathToRoot(new Object[]{fullPath.getPathComponent(0)});
                isAdjusting--;
                subPath = fullPath;
            }
            ensurePathIsVisible(fullPath);
        }
    }

    /**
     * API method of FileChooserUI.
     */
    public String getApproveButtonText(JFileChooser fc) {
        String buttonText = fc.getApproveButtonText();
        if (buttonText != null) {
            return buttonText;
        } else if (fc.isDirectorySelectionEnabled() && chooseButtonText != null) {
            return chooseButtonText;
        } else if (fc.getDialogType() == JFileChooser.OPEN_DIALOG) {
            return openButtonText;
        } else if (fc.getDialogType() == JFileChooser.SAVE_DIALOG) {
            return saveButtonText;
        } else {
            return null;
        }
    }

    /**
     * API method of FileChooserUI.
     */
    public FileView getFileView(JFileChooser fc) {
        return fileView;
    }

    /**
     * API method of FileChooserUI.
     */
    public void rescanCurrentDirectory(JFileChooser fc) {
        // Validation is only necessary, when the JFileChooser is showing.
        if (fc.isShowing()) {
            //clearIconCache();
            if (browser.getSelectionPath() != null) {
                model.lazyInvalidatePath(browser.getSelectionPath());
                model.validatePath(browser.getSelectionPath());
            }
        }
    }
    // *******************************************************
    // ******** End of FileChooserUI PLAF methods ************
    // *******************************************************
    // *******************************************************
    // ********** BasicFileChooserUI PLAF methods ************
    // *******************************************************

    public void clearIconCache() {
        try {
            fileView.getClass().getMethod("clearIconCache", new Class[0]).invoke(fileView, new Object[0]);
        } catch (Exception e) {
            // empty
        }
    }

    protected MouseListener createDoubleClickListener(JFileChooser fc) {
        return new DoubleClickListener();
    }

    // *******************************************************
    // ******* End of BasicFileChooserUI PLAF methods ********
    // *******************************************************
    private class SidebarSelectionListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent e) {
            if (isAdjusting != 0) {
                return;
            }

            if (sidebarTree != null && sidebarTree.getSelectionPath() != null) {
                if (sidebarTree.getSelectionPath().getLastPathComponent() instanceof FileInfo) {
                    FileInfo info = (FileInfo) sidebarTree.getSelectionPath().getLastPathComponent();
                    File file = info.lazyGetResolvedFile();
                    if (file != null) {
                        JFileChooser fc = getFileChooser();
                        if (file.isDirectory() && fc.isTraversable(file)) {
                            isAdjusting++;
                            TreePath fullPath = getFileSystemTreeModel().toPath(file, subtreeModel.getPathToRoot());
                            subtreeModel.setPathToRoot(fullPath);
                            getFileSystemTreeModel().lazyInvalidatePath(fullPath);
                            fc.setCurrentDirectory(file);
                            TreePath dirPath = subtreeModel.toSubPath(fullPath);
                            browser.setSelectionPath(dirPath);
                            browser.ensurePathIsVisible(dirPath);
                            isAdjusting--;
                        } else {
                            if (fc.isMultiSelectionEnabled()) {
                                fc.setSelectedFiles(new File[]{file});
                            } else {
                                fc.setSelectedFile(file);
                            }
                        }
                    }
                }
                /*
                TreePath path = subtreeModel.getPathToRoot();
                getFileSystemTreeModel().lazyInvalidatePath(path);
                getFileSystemTreeModel().validatePath(path);
                 */
            }
        }
    }

    /**
     * Responds to an Open or Save request
     */
    /**
     * Responds to an Open or Save request
     */
    protected class QuaquaApproveSelectionAction extends AbstractAction {

        protected QuaquaApproveSelectionAction() {
            super("approveSelection");
        }

        public void actionPerformed(ActionEvent e) {
            maybeApproveSelection(true);
        }
    }
}
