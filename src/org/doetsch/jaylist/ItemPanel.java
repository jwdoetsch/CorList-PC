package org.doetsch.jaylist;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Insets;

import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class ItemPanel extends JPanel {
	private JPanel panel;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JLabel labelSpacerRight;
	private JButton buttonStatus;
	private JTextField textField;
	private JButton buttonDrop;
	
	private int statusFlag;
	private ImageIcon[] statusFlagIcons;
	private ImageIcon iconExpand;
	private ImageIcon iconCollapse;
	private boolean expanded;
	//private ListFrame parentFrame;
	private JTable parentTable;
	private JPopupMenu popupMenu;
	private int rowIndex;
	private boolean isSelected;
	private JPanel panelDrop;

	/**
	 * Creates a new ItemPanel instance modeled after the given
	 * ItemModel.
	 * 
	 * @param itemModel
	 * @param isSelected determines if this ItemPanel's UI components
	 * should be highlighted
	 * @param parentTable the table within which the ItemPanel will be
	 * used
	 * @param rowIndex the row at quich this ItemPanel will be used
	 * in the parent table
	 * @param popupMenu the JPopupMenu to be displayed on right-clicking
	 * the ItemPanel
	 */
	ItemPanel (ItemModel itemModel, boolean isSelected, JTable parentTable,
			int rowIndex, JPopupMenu popupMenu) {
		this.parentTable = parentTable;
		this.rowIndex = rowIndex;
		this.isSelected = isSelected;
		this.popupMenu = popupMenu;
		
		loadIcons();
		initComponents();
		injectItemModelValues(itemModel);
	}
	


	ItemModel toItemModel () {
		ItemModel model = new ItemModel(
				this.textField.getText(),
				this.textArea.getText(),
				this.statusFlag,
				this.expanded);
		return model;
	}
	
	private void injectItemModelValues (ItemModel itemModel) {
		this.textField.setText(itemModel.title);
		this.textArea.setText(itemModel.desc);
		this.statusFlag = itemModel.flag;
		this.expanded = itemModel.expanded;
		
		//this.buttonStatus.setIcon(statusFlagIcons[this.statusFlag]);
		setStatusButtonIcon();
		setDropButtonIcon();
		requestRowResize();
		
	}
	
	private void loadIcons () {
		
		
		statusFlagIcons = new ImageIcon[4];
//		statusFlagIcons[0] = new ImageIcon(
//				ItemPanel2.class.getResource("resources/unchecked.png"));
		statusFlagIcons[0] = Constants.ICON_UNCHECKED;
		statusFlagIcons[1] = Constants.ICON_CHECKED;
		statusFlagIcons[2] = Constants.ICON_URGENT;
		statusFlagIcons[3] = Constants.ICON_ISSUE;
		
		
		this.iconExpand = new ImageIcon(
				ItemPanel.class.getResource("resources/set2/expand.png"));
		this.iconCollapse = new ImageIcon(
				ItemPanel.class.getResource("resources/set2/collapse.png"));
	}
	
	private void initComponents () {
		setPreferredSize(new Dimension(
				Constants.ITEMPANEL_WIDTH, Constants.ITEMPANEL_HEIGHT_EXPANDED));
		
		setLayout(new BorderLayout(0, 0));
		this.panel = new JPanel();
		
		add(this.panel, BorderLayout.NORTH);
		this.panel.setLayout(new BorderLayout(0, 0));
		this.buttonStatus = new JButton("");
		this.buttonStatus.setBackground(Constants.COLOR_ITEM);
		this.buttonStatus.addActionListener(new ButtonStatusActionListener());
		this.buttonStatus.setPreferredSize(new Dimension(
			Constants.ITEMPANEL_HEIGHT, Constants.ITEMPANEL_HEIGHT));
		this.buttonStatus.setBorderPainted(false);
		this.buttonStatus.setFocusPainted(false);
		this.panel.add(this.buttonStatus, BorderLayout.WEST);
		this.textField = new JTextField();
		this.textField.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.textField.setPreferredSize(new Dimension(6, Constants.ITEMPANEL_HEIGHT));
		this.panel.add(this.textField, BorderLayout.CENTER);
		this.textField.setColumns(10);
		this.textField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (ItemPanel.this.expanded) {
					ItemPanel.this.textArea.requestFocus();
				}
			}
			
		});
		
		
		
		
		this.panelDrop = new JPanel();
		this.panelDrop.setBackground(Constants.COLOR_ITEM);
		this.panelDrop.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.panelDrop.setSize(new Dimension(Constants.ITEMPANEL_HEIGHT, Constants.ITEMPANEL_HEIGHT));
		this.panelDrop.setMaximumSize(new Dimension(Constants.ITEMPANEL_HEIGHT, Constants.ITEMPANEL_HEIGHT));
		this.panelDrop.setMinimumSize(new Dimension(Constants.ITEMPANEL_HEIGHT, Constants.ITEMPANEL_HEIGHT));
		this.panelDrop.setPreferredSize(new Dimension(Constants.ITEMPANEL_HEIGHT, Constants.ITEMPANEL_HEIGHT));
		this.panel.add(this.panelDrop, BorderLayout.EAST);
		this.panelDrop.setLayout(new BorderLayout(0, 0));
		
		
		
		
		this.buttonDrop = new JButton("");
		this.buttonDrop.setMargin(new Insets(0, 0, 0, 0));
		this.panelDrop.add(this.buttonDrop);
		this.buttonDrop.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.buttonDrop.setBackground(Constants.COLOR_ITEM);
		this.buttonDrop.setPreferredSize(new Dimension(40, 40));
		this.buttonDrop.setBorderPainted(false);
		this.buttonDrop.setFocusPainted(false);
		this.buttonDrop.addActionListener(new BtnNewButton_1ActionListener());
		this.scrollPane = new JScrollPane();
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		this.scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		add(this.scrollPane, BorderLayout.CENTER);
		this.textArea = new JTextArea();
		this.textArea.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.textArea.setWrapStyleWord(true);
		this.textArea.setLineWrap(true);
		this.textArea.setBackground(Constants.COLOR_ITEM);
		this.scrollPane.setViewportView(this.textArea);
		this.labelSpacerRight = new JLabel("");
		this.labelSpacerRight.setBackground(Constants.COLOR_ITEM);
		this.labelSpacerRight.setOpaque(true);
		this.labelSpacerRight.setPreferredSize(new Dimension(Constants.ITEMPANEL_HEIGHT, 0));
		add(this.labelSpacerRight, BorderLayout.WEST);
		
		this.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				requestRowResize();
			}
			
		});
		
		this.textField.setFont(Constants.FONT_ITEM_TITLE);
		this.textField.setBackground(Constants.COLOR_ITEM);
		this.textArea.setFont(Constants.FONT_ITEM_DESCRIPTIOn);
		this.textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				requestRowResize();				
			}
		});
		
		/*
		 * should be highlighted?						
		 */
		if (isSelected) {
			this.buttonStatus.setBackground(Constants.COLOR_HIGHLIGHT2);
			this.textField.setBackground(Constants.COLOR_HIGHLIGHT);
			this.buttonDrop.setBackground(Constants.COLOR_HIGHLIGHT2);
			this.textArea.setBackground(Constants.COLOR_HIGHLIGHT);
			this.labelSpacerRight.setBackground(Constants.COLOR_HIGHLIGHT);
			//this.labelSpacerLeft.setBackground(Constants.COLOR_HIGHLIGHT);
			this.panelDrop.setBackground(Constants.COLOR_HIGHLIGHT2);
		}
		
//		this.setComponentPopupMenu(parentTable.uiPopupMenu);
//		this.buttonStatus.setComponentPopupMenu(parentTable.uiPopupMenu);
//		this.textField.setComponentPopupMenu(parentTable.uiPopupMenu);
//		this.buttonDrop.setComponentPopupMenu(parentTable.uiPopupMenu);
//		this.textArea.setComponentPopupMenu(parentTable.uiPopupMenu);
//		this.labelSpacer.setComponentPopupMenu(parentTable.uiPopupMenu);
//		this.panelDrop.setComponentPopupMenu(parentTable.uiPopupMenu);
	
		
		this.textField.addMouseListener(new PopupListener(popupMenu));
		this.buttonStatus.addMouseListener(new PopupListener(popupMenu));
		this.textField.addMouseListener(new PopupListener(popupMenu));
		this.buttonDrop.addMouseListener(new PopupListener(popupMenu));
		this.textArea.addMouseListener(new PopupListener(popupMenu));
	}
	
	private void setDropButtonIcon () {
		
		if (ItemPanel.this.expanded) {
			buttonDrop.setIcon(iconCollapse);
		} else {
			buttonDrop.setIcon(iconExpand);
		}
	}
	
	/**
	 * Determines and returns the table row height necessary
	 * to include all lines (wrapped and breaked) within the
	 * text area.
	 * 
	 * @return
	 */
	int getTextAreaHeight () {
		JEditorPane dummyPane = new JEditorPane();
		dummyPane.setSize(parentTable.getSize().width - Constants.ITEMPANEL_HEIGHT, 32);
		dummyPane.setText(textArea.getText());
		dummyPane.setFont(textArea.getFont());
		return dummyPane.getPreferredSize().height;
	}
	
	void requestRowResize () {
		
		int textAreaHeight = getTextAreaHeight();
		textAreaHeight = getTextAreaHeight();
		
		int rowHeight = 34 + textAreaHeight;
	
		if (expanded && (parentTable.getRowHeight(this.rowIndex) != rowHeight)) {
			parentTable.setRowHeight(rowIndex, rowHeight);
		}
		
		if (!expanded && (parentTable.getRowHeight(this.rowIndex) != Constants.ITEMPANEL_HEIGHT)) {
			parentTable.setRowHeight(rowIndex, Constants.ITEMPANEL_HEIGHT);
		}
				
		
	}
	
	void setStatusButtonIcon () {
		ItemPanel.this.buttonStatus.setIcon(
				statusFlagIcons[ItemPanel.this.statusFlag]);
	}
	
	private class BtnNewButton_1ActionListener implements ActionListener {
		public void actionPerformed (ActionEvent arg0) {
			ItemPanel.this.expanded = !ItemPanel.this.expanded;
			
			setDropButtonIcon();
			
			requestRowResize();
		}
	}
	private class ButtonStatusActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			ItemPanel.this.statusFlag =
					(ItemPanel.this.statusFlag + 1) % Constants.ICON_FLAG_COUNT;
			setStatusButtonIcon();

		}
	}
}
