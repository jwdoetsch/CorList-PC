 package org.doetsch.jaylist;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;

import javax.swing.JButton;

import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.event.MouseAdapter;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.SwingConstants;

public class ItemPanel extends JPanel {
	
	private JTextField textField;
	private JPanel panel;
	private JButton buttonEdit;

	private ItemModel itemModel;
	private boolean isSelected;
	private ListFrame parentFrame;
	private int row;
	private ImageIcon[] icons;
	private JLabel labelFlag;
	
	
//	/**
//	 * Create the panel.
//	 */
//	public ItemPanel (boolean isMarked, String title) {
//		initComponents (isMarked, title);
//	}

	public ItemPanel (ItemModel itemModel, boolean isSelected,
			ListFrame parentFrame, int row) {
		this.itemModel = itemModel;
		this.isSelected = isSelected;
		this.parentFrame = parentFrame;
		this.row = row;
		
		initComponents();
		render();
	}
	
//	private void setFlagIcon () {
//		
//	}
	
	private void initComponents () {
		
		int panelHight = 40;
		
		setBackground(UIManager.getColor("Panel.background"));
		setPreferredSize(new Dimension(256, panelHight));
		setMaximumSize(new Dimension(256, panelHight));
		setMinimumSize(new Dimension(256, panelHight));
		setLayout(new BorderLayout(0, 0));
		this.textField = new JTextField();
		this.textField.setBorder(new EmptyBorder(0, 0, 0, 0));
		if (isSelected) {
			highlight();
		}
		add(this.textField, BorderLayout.CENTER);
		this.textField.setColumns(10);
		this.textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate (DocumentEvent arg0) {
				setItemModelTitle();
			}

			@Override
			public void insertUpdate (DocumentEvent arg0) {
				setItemModelTitle();
			}

			@Override
			public void removeUpdate (DocumentEvent arg0) {
				setItemModelTitle();
			}
			
			void setItemModelTitle () {
				itemModel.setTitle(textField.getText());
			}
			
		});
		
		this.panel = new JPanel();
		this.panel.setBorder(new EmptyBorder(0, 0, 0, 0));
		add(this.panel, BorderLayout.EAST);
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.X_AXIS));
		this.buttonEdit = new JButton("");
		this.buttonEdit.addActionListener(new ButtonEditActionListener());
		this.buttonEdit.setBorderPainted(false);
		this.buttonEdit.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.buttonEdit.setBackground(Color.WHITE);
		this.buttonEdit.setIcon(new ImageIcon(ItemPanel.class.getResource("resources/edit.gif")));
		this.buttonEdit.setPreferredSize(new Dimension(24, 40));
		this.buttonEdit.setMinimumSize(new Dimension(24, 40));
		this.buttonEdit.setMaximumSize(new Dimension(24, 40));
		this.panel.add(this.buttonEdit);
		
		icons = new ImageIcon[4];
		icons[0] = new ImageIcon(ItemPanel.class.getResource("resources/unchecked.gif"));
		icons[1] = new ImageIcon(ItemPanel.class.getResource("resources/checked.gif"));
		icons[2] = new ImageIcon(ItemPanel.class.getResource("resources/alert.gif"));
		icons[3] = new ImageIcon(ItemPanel.class.getResource("resources/question.gif"));
	
		
		this.labelFlag = new JLabel();
		this.labelFlag.setHorizontalAlignment(SwingConstants.CENTER);
		this.labelFlag.addMouseListener(new LabelFlagMouseListener());
		this.labelFlag.setSize(new Dimension(40, 40));
		this.labelFlag.setPreferredSize(new Dimension(40, 40));
		this.labelFlag.setMaximumSize(new Dimension(40, 40));
		this.labelFlag.setMinimumSize(new Dimension(40, 40));
		this.labelFlag.setBackground(Color.WHITE);
		this.labelFlag.setOpaque(true);
		add(this.labelFlag, BorderLayout.WEST);
		
		
		
	}
	
	/*
	 * Sets the ItemPanel components according to the ItemModel
	 */
	private void render () {
		this.textField.setText(itemModel.getTitle());
		this.labelFlag.setIcon(icons[itemModel.getFlag()]);
		if (itemModel.getFlag() == 1) {
			Map atts = (new Font("Arial",Font.PLAIN, 14)).getAttributes();
			atts.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
			
			this.textField.setFont(new Font(atts));
		}
	}
	
//	/*
//	 * Sets the ItemModel fields according to the ItemPanel component values
//	 */
//	ItemModel model () {
//		
//	}
	
	public ItemModel getItemModel () {
		return this.itemModel;
	}
	
//	public void clone (ItemPanel item) {
//		this.itemModel = item.getItemModel();
//		model();
//	}
	
	public void highlight () {
		this.textField.setBackground(JayListConstants.HIGHLIGHT_COLOR);
	}
	private class ButtonEditActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			parentFrame.removeItemModel(row);
		}
	}
	private class LabelFlagMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			itemModel.setFlag((itemModel.getFlag() + 1) % 4);
			render();
		}
	}
}
