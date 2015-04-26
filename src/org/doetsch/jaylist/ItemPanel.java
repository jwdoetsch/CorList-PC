package org.doetsch.jaylist;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
class ItemPanel extends JPanel {

	//UI components
	private JPanel uiPanelHeader;
	private JScrollPane uiScrollPane;
	private JTextArea uiTextArea;
	private JLabel uiLabelLeftSpacer;
	private JButton uiBtnStatus;
	private JTextField uiTextField;
	private JButton uiBtnExpand;
	private JPanel uiPanelDrop;

	//UI constants
	private final ImageIcon[] statusIcons;
	
	//model fields from source ItemModel
	//private int flagStatus;
	private StatusFlag status;
	private boolean flagExpand;
	private int rowIndex;
	private boolean isSelected;
	private boolean isHidden;
	private JTable parentTable;
	private JPopupMenu popupMenu;
	private JPopupMenu statusMenu;

	/**
	 * Creates a new ItemPanel instance modeled after the given
	 * ItemModel.
	 * 
	 * @param itemModel
	 * @param isSelected determines if this ItemPanel's UI components
	 * should be highlighted
	 * @param parentTable the table within which the ItemPanel will be
	 * used
	 * @param rowIndex the row at which this ItemPanel will be used
	 * in the parent table
	 * @param popupMenu the JPopupMenu to be displayed on right-clicking
	 * the ItemPanel
	 */
	ItemPanel (ItemPanelModel itemModel, boolean isSelected,
			JTable parentTable, int rowIndex, JPopupMenu popupMenu) {
		this.parentTable = parentTable;
		this.rowIndex = rowIndex;
		this.isSelected = isSelected;
		this.popupMenu = popupMenu;
		
		//load icon constants
		statusIcons = new ImageIcon[4];
		statusIcons[0] = UI.ICON_UNCHECKED;
		statusIcons[1] = UI.ICON_CHECKED;
		statusIcons[2] = UI.ICON_URGENT;
		statusIcons[3] = UI.ICON_ISSUE;
		
		initComponents();
		injectItemModelValues(itemModel);
	}
	
	/**
	 * Instantiates and returns an ItemModel encapsulating
	 * the UI field values of the ItemPanel. 
	 * @return
	 */
	ItemPanelModel toItemModel () {
		return new ItemPanelModel(
				this.uiTextField.getText(),
				this.uiTextArea.getText(),
				this.status,
				this.flagExpand,
				this.isHidden);
	}
	
	/**
	 * Sets the ItemPanel's UI field values according
	 * to the field values in the given ItemModel.
	 * @param itemModel
	 */
	private void injectItemModelValues (ItemPanelModel itemModel) {
		this.uiTextField.setText(itemModel.title);
		this.uiTextArea.setText(itemModel.desc);
		//this.status = itemModel.status;
		setStatus(itemModel.status);
		this.flagExpand = itemModel.isExpanded;
		
		//refresh UI components that are flag dependent
		//setStatus();
		setDropButtonIcon();
		
		//adjust parent table row height to fit wrapped
		//description text
		syncRowHeight();		
	}
	
	/**
	 * Instantiates and constructs UI components.
	 */
	private void initComponents () {
		
		//set up the source panel
		setPreferredSize(new Dimension(
				UI.ITEMPANEL_WIDTH, UI.ITEMPANEL_HEIGHT_EXPANDED));
		setLayout(new BorderLayout(0, 0));

		//instantiate and construct the header panel and
		//header components
		this.uiPanelHeader = new JPanel();
		add(this.uiPanelHeader, BorderLayout.NORTH);
		this.uiPanelHeader.setLayout(new BorderLayout(0, 0));
		this.uiBtnStatus = new JButton("");
		this.uiBtnStatus.setBackground(UI.COLOR_ITEM);
		this.uiBtnStatus.addActionListener(new UIActionStatus());
		this.uiBtnStatus.setPreferredSize(new Dimension(
			UI.ITEMPANEL_HEIGHT, UI.ITEMPANEL_HEIGHT));
		this.uiBtnStatus.setBorderPainted(false);
		this.uiBtnStatus.setFocusPainted(false);
		this.uiPanelHeader.add(this.uiBtnStatus, BorderLayout.WEST);
		this.uiTextField = new JTextField();
		this.uiTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.uiTextField.setPreferredSize(new Dimension(6, UI.ITEMPANEL_HEIGHT));
		this.uiTextField.setColumns(10);
		this.uiTextField.setFont(UI.FONT_ITEM_TITLE);
		this.uiTextField.setBackground(UI.COLOR_ITEM);
		this.uiPanelHeader.add(this.uiTextField, BorderLayout.CENTER);
		
		this.uiPanelDrop = new JPanel();
		this.uiPanelDrop.setBackground(UI.COLOR_ITEM);
		this.uiPanelDrop.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.uiPanelDrop.setSize(new Dimension(UI.ITEMPANEL_HEIGHT, UI.ITEMPANEL_HEIGHT));
		this.uiPanelDrop.setMaximumSize(new Dimension(UI.ITEMPANEL_HEIGHT, UI.ITEMPANEL_HEIGHT));
		this.uiPanelDrop.setMinimumSize(new Dimension(UI.ITEMPANEL_HEIGHT, UI.ITEMPANEL_HEIGHT));
		this.uiPanelDrop.setPreferredSize(new Dimension(32, 32));
		this.uiPanelHeader.add(this.uiPanelDrop, BorderLayout.EAST);
		this.uiPanelDrop.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		this.uiBtnExpand = new JButton("");
		this.uiBtnExpand.setSize(new Dimension(24, 24));
		this.uiBtnExpand.setMinimumSize(new Dimension(24, 24));
		this.uiBtnExpand.setMaximumSize(new Dimension(24, 24));
		this.uiBtnExpand.setMargin(new Insets(0, 0, 0, 0));
		this.uiPanelDrop.add(this.uiBtnExpand);
		this.uiBtnExpand.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.uiBtnExpand.setBackground(UI.COLOR_ITEM);
		this.uiBtnExpand.setPreferredSize(new Dimension(20, 20));
		this.uiBtnExpand.setBorderPainted(false);
		this.uiBtnExpand.setFocusPainted(false);
		this.uiBtnExpand.addActionListener(new UIActionExpand());
		this.uiBtnExpand.addMouseListener(new PopupAction(popupMenu, new Point(-56, -12)));
		
		//construct description components
		this.uiScrollPane = new JScrollPane();
		this.uiScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		this.uiScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		add(this.uiScrollPane, BorderLayout.CENTER);
		this.uiTextArea = new JTextArea();
		this.uiTextArea.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.uiTextArea.setWrapStyleWord(true);
		this.uiTextArea.setLineWrap(true);
		this.uiTextArea.setBackground(UI.COLOR_ITEM);
		this.uiTextArea.setFont(UI.FONT_ITEM_DESCRIPTIOn);
		this.uiTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				syncRowHeight();				
			}
		});
		
		//set up the spacer
		this.uiScrollPane.setViewportView(this.uiTextArea);
		this.uiLabelLeftSpacer = new JLabel("");
		this.uiLabelLeftSpacer.setBackground(UI.COLOR_ITEM);
		this.uiLabelLeftSpacer.setOpaque(true);
		this.uiLabelLeftSpacer.setPreferredSize(new Dimension(UI.ITEMPANEL_HEIGHT, 0));
		add(this.uiLabelLeftSpacer, BorderLayout.WEST);
		
		//set up the status button icon popup menu
		this.statusMenu = new JPopupMenu();
//		JMenuItem item0 = new JMenuItem("None");
//		JMenuItem item1 = new JMenuItem("Incomplete");
//		JMenuItem item2 = new JMenuItem("Complete");
//		JMenuItem item3 = new JMenuItem("Alert");
//		JMenuItem item4 = new JMenuItem("Question");
//		item0.setIcon(StatusFlag.NONE.icon);
//		item0.addActionListener(new UIActionStatusPopup(StatusFlag.NONE));
//		item1.setIcon(StatusFlag.INCOMPLETE.icon);
//		item1.addActionListener(new UIActionStatusPopup(StatusFlag.INCOMPLETE));
//		item2.setIcon(StatusFlag.COMPLETE.icon);
//		item2.addActionListener(new UIActionStatusPopup(StatusFlag.COMPLETE));
//		item3.setIcon(StatusFlag.ALERT.icon);
//		item3.addActionListener(new UIActionStatusPopup(StatusFlag.ALERT));
//		item4.setIcon(StatusFlag.QUESTION.icon);
//		item4.addActionListener(new UIActionStatusPopup(StatusFlag.QUESTION));
//		this.statusMenu.add(item1);
//		this.statusMenu.add(item2);
//		this.statusMenu.add(item3);
//		this.statusMenu.add(item4);
		
		for (StatusFlag status : StatusFlag.values()) {
			JMenuItem popupItem = new JMenuItem(status.desc);
			popupItem.setIcon(status.icon);
			popupItem.addActionListener(new UIActionStatusPopup(status));
			this.statusMenu.add(popupItem);
		}
		
		//this.uiBtnStatus.addMouseListener(new PopupListener(statusMenu, new Point(-8, -8)));
	
		
		//if the item is selected in the table then highlight
		//necessary UI components
		if (isSelected) {
			this.uiBtnStatus.setBackground(UI.COLOR_HIGHLIGHT2);
			this.uiTextField.setBackground(UI.COLOR_HIGHLIGHT);
			this.uiBtnExpand.setBackground(UI.COLOR_HIGHLIGHT2);
			this.uiTextArea.setBackground(UI.COLOR_HIGHLIGHT);
			this.uiLabelLeftSpacer.setBackground(UI.COLOR_HIGHLIGHT);
			this.uiPanelDrop.setBackground(UI.COLOR_HIGHLIGHT2);
		}
		
		//add the control popup menu to panel components
		this.uiTextField.addMouseListener(new PopupAction(popupMenu, new Point(-56, -12)));
		this.uiBtnStatus.addMouseListener(new PopupAction(statusMenu, new Point(-8, -8)));
		this.uiTextField.addMouseListener(new PopupAction(popupMenu, new Point(-56, -12)));
		this.uiTextArea.addMouseListener(new PopupAction(popupMenu, new Point(-56, -12)));
	}
	
	/**
	 * Sets the drop icon according to the expand/collapse
	 * flag.	
	 */
	private void setDropButtonIcon () {
		uiBtnExpand.setIcon(this.flagExpand ?
				UI.ICON_COLLAPSE : UI.ICON_EXPAND);
	}
	
	/**
	 * Determines and returns the table row height necessary
	 * to include all lines (wrapped and breaked) within the
	 * text area.
	 * 
	 * @return
	 */
	private int getPanelHeight () {
		JTextArea dummyPane = new JTextArea();
		
		//if this item is expanded then calculate the row height necessary
		//to display all description text, if collapsed then return the
		//default row height
		if (flagExpand) {
			//make sure the dummy pane area is identical to
			//the description text area
			dummyPane.setLineWrap(true);
			dummyPane.setWrapStyleWord(true);
			dummyPane.setBorder(uiTextArea.getBorder());
			
			//width = parent table width - spacer width - 1
			dummyPane.setSize(parentTable.getSize().width
					- UI.ITEMPANEL_HEIGHT - 1, 32);
			dummyPane.setText(uiTextArea.getText());
			dummyPane.setFont(uiTextArea.getFont());
			return dummyPane.getPreferredSize().height + 34;

		} else {
			return UI.ITEMPANEL_HEIGHT;
		}		
	}
	
	/**
	 * Determines if this item's row height in the parent table
	 * matches the height required to display all this item's
	 * UI components (including wrapped description text).
	 */
	private void syncRowHeight () {
		int rowHeight = getPanelHeight();
	
		if (parentTable.getRowHeight(this.rowIndex) != rowHeight) {
			parentTable.setRowHeight(rowIndex, rowHeight);
		}
		
	}

	/**
	 * Sets the status button icon according to the status
	 * flag.
	 */
	private void setStatus (StatusFlag status) {
		ItemPanel.this.status = status; //StatusFlag.getStatusFlag(flag);
		ItemPanel.this.uiBtnStatus.setIcon(
				ItemPanel.this.status.icon);
	}
	
	/**
	 * Defines the action for the expand button; toggles
	 * the flag, sets the icon accordingly, and resizes the
	 * parent table row. 
	 */
	private class UIActionExpand implements ActionListener {
		public void actionPerformed (ActionEvent arg0) {
			ItemPanel.this.flagExpand = !ItemPanel.this.flagExpand;
			
			setDropButtonIcon();
			
			syncRowHeight();
		}
	}
	
	/**
	 * Defines the action for the status button; toggles
	 * the status flag and sets its icon.
	 */
	private class UIActionStatus implements ActionListener {
		public void actionPerformed (ActionEvent arg0) {
//			ItemPanel.this.status =
//					StatusFlag.getStatusFlag(
//							(ItemPanel.this.status.code + 1) % UI.ICON_FLAG_COUNT);
			//toggle between incomplete status (code 0) and
			//completed status (code 1)
			setStatus(StatusFlag.getStatusFlag(
					(ItemPanel.this.status.code + 1) % UI.ICON_FLAG_COUNT));

		} 
	}
	
	/**
	 * Defines the action for each of the status menu item popups
	 */
	private class UIActionStatusPopup implements ActionListener {

		//private int flag;
		private StatusFlag status;
		
		UIActionStatusPopup (StatusFlag status) {
			this.status = status;
		}
		
		@Override
		public void actionPerformed (ActionEvent e) {
			//ItemPanel.this.status = StatusFlag.getStatusFlag(flag);
			setStatus(status);
		}
		
	}
}
