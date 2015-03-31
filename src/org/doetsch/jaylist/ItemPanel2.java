package org.doetsch.jaylist;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Rectangle;

public class ItemPanel2 extends JPanel {
	private JPanel panel;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JLabel labelSpacer;
	private JButton buttonStatus;
	private JTextField textField;
	private JButton buttonDrop;
	
	private int statusFlag;
	private ImageIcon[] statusFlagIcons;
	private ImageIcon iconExpand;
	private ImageIcon iconCollapse;
	private boolean expanded;
	private JTable parentTable;
	private int rowIndex;
	private boolean isSelected;
	private JPanel panelDrop;

	/**
	 * Create the panel.
	 */
	ItemPanel2 (ItemModel2 itemModel, JTable parentTable,
			int rowIndex, boolean isSelected) {
		this.parentTable = parentTable;
		this.rowIndex = rowIndex;
		this.isSelected = isSelected;
		
		loadIcons();
		initComponents();
		injectItemModelValues(itemModel);

	}
	


	ItemModel2 toItemModel () {
		ItemModel2 model = new ItemModel2(
				this.textField.getText(),
				this.textArea.getText(),
				this.statusFlag,
				this.expanded);
		return model;
	}
	
	private void injectItemModelValues (ItemModel2 itemModel) {
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
		statusFlagIcons[0] = JLConstants2.ICON_UNCHECKED;
		statusFlagIcons[1] = JLConstants2.ICON_CHECKED;
		statusFlagIcons[2] = JLConstants2.ICON_URGENT;
		statusFlagIcons[3] = JLConstants2.ICON_ISSUE;
		
		
		this.iconExpand = new ImageIcon(
				ItemPanel2.class.getResource("resources/set2/expand.png"));
		this.iconCollapse = new ImageIcon(
				ItemPanel2.class.getResource("resources/set2/collapse.png"));
	}
	
	private void initComponents () {
		setPreferredSize(new Dimension(
				JLConstants2.ITEMPANEL_WIDTH, JLConstants2.ITEMPANEL_HEIGHT_EXPANDED));
		
		setLayout(new BorderLayout(0, 0));
		this.panel = new JPanel();
		
		add(this.panel, BorderLayout.NORTH);
		this.panel.setLayout(new BorderLayout(0, 0));
		this.buttonStatus = new JButton("");
		this.buttonStatus.setBackground(Color.WHITE);
		this.buttonStatus.addActionListener(new ButtonStatusActionListener());
		this.buttonStatus.setPreferredSize(new Dimension(40, 40));
		this.buttonStatus.setBorderPainted(false);
		this.buttonStatus.setFocusPainted(false);
		this.panel.add(this.buttonStatus, BorderLayout.WEST);
		this.textField = new JTextField();
		this.textField.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.textField.setPreferredSize(new Dimension(6, JLConstants2.ITEMPANEL_HEIGHT));
		this.panel.add(this.textField, BorderLayout.CENTER);
		this.textField.setColumns(10);
		this.textField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (ItemPanel2.this.expanded) {
					ItemPanel2.this.textArea.requestFocus();
				}
			}
			
		});
		this.panelDrop = new JPanel();
		this.panelDrop.setBackground(Color.WHITE);
		this.panelDrop.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.panelDrop.setSize(new Dimension(40, 40));
		this.panelDrop.setMaximumSize(new Dimension(40, 40));
		this.panelDrop.setMinimumSize(new Dimension(40, 40));
		this.panelDrop.setPreferredSize(new Dimension(40, 40));
		this.panel.add(this.panelDrop, BorderLayout.EAST);
		this.panelDrop.setLayout(new BorderLayout(0, 0));
		
		
		
		
		this.buttonDrop = new JButton("");
		this.panelDrop.add(this.buttonDrop);
		this.buttonDrop.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.buttonDrop.setBackground(Color.WHITE);
		this.buttonDrop.setPreferredSize(new Dimension(20, 20));
		this.buttonDrop.setBorderPainted(false);
		this.buttonDrop.setFocusPainted(false);
		this.buttonDrop.addActionListener(new BtnNewButton_1ActionListener());
		this.scrollPane = new JScrollPane();
		this.scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		add(this.scrollPane, BorderLayout.CENTER);
		this.textArea = new JTextArea();
		this.textArea.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.textArea.setWrapStyleWord(true);
		this.textArea.setLineWrap(true);
		this.scrollPane.setViewportView(this.textArea);
		this.labelSpacer = new JLabel("");
		this.labelSpacer.setBackground(Color.WHITE);
		this.labelSpacer.setOpaque(true);
		this.labelSpacer.setPreferredSize(new Dimension(40, 0));
		add(this.labelSpacer, BorderLayout.WEST);
		
		/*
		 * should be highlighted?						
		 */
		if (isSelected) {
			this.buttonStatus.setBackground(JLConstants2.COLOR_HIGHLIGHT2);
			this.textField.setBackground(JLConstants2.COLOR_HIGHLIGHT);
			this.buttonDrop.setBackground(JLConstants2.COLOR_HIGHLIGHT2);
			this.textArea.setBackground(JLConstants2.COLOR_HIGHLIGHT);
			this.labelSpacer.setBackground(JLConstants2.COLOR_HIGHLIGHT);
			this.panelDrop.setBackground(JLConstants2.COLOR_HIGHLIGHT2);
		}
		
		
	}
	
	private void setDropButtonIcon () {
		
		if (ItemPanel2.this.expanded) {
			buttonDrop.setIcon(iconCollapse);
		} else {
			buttonDrop.setIcon(iconExpand);
		}
	}
	
	void requestRowResize () {
		parentTable.setRowHeight(this.rowIndex,
				(this.expanded ? 
						JLConstants2.ITEMPANEL_HEIGHT_EXPANDED : 
							JLConstants2.ITEMPANEL_HEIGHT));
	}
	
	void setStatusButtonIcon () {
		ItemPanel2.this.buttonStatus.setIcon(
				statusFlagIcons[ItemPanel2.this.statusFlag]);
	}
	
	private class BtnNewButton_1ActionListener implements ActionListener {
		public void actionPerformed (ActionEvent arg0) {
			ItemPanel2.this.expanded = !ItemPanel2.this.expanded;
			
			setDropButtonIcon();
			
			requestRowResize();
		}
	}
	private class ButtonStatusActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			ItemPanel2.this.statusFlag =
					(ItemPanel2.this.statusFlag + 1) % 4;
			setStatusButtonIcon();

		}
	}
}
