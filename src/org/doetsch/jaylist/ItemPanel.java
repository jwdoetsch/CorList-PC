package org.doetsch.jaylist;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

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
	private final ImageIcon[] iconFlagStatus;
	
	//model fields from source ItemModel
	private int flagStatus;
	private boolean flagExpand;
	private int rowIndex;
	private boolean isSelected;
	private JTable parentTable;
	private JPopupMenu popupMenu;

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
		iconFlagStatus = new ImageIcon[4];
		iconFlagStatus[0] = Constants.ICON_UNCHECKED;
		iconFlagStatus[1] = Constants.ICON_CHECKED;
		iconFlagStatus[2] = Constants.ICON_URGENT;
		iconFlagStatus[3] = Constants.ICON_ISSUE;
		
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
				this.flagStatus,
				this.flagExpand);
	}
	
	/**
	 * Sets the ItemPanel's UI field values according
	 * to the field values in the given ItemModel.
	 * @param itemModel
	 */
	private void injectItemModelValues (ItemPanelModel itemModel) {
		this.uiTextField.setText(itemModel.title);
		this.uiTextArea.setText(itemModel.desc);
		this.flagStatus = itemModel.flagStatus;
		this.flagExpand = itemModel.flagExpand;
		
		//refresh UI components that are flag dependent
		setStatusButtonIcon();
		setDropButtonIcon();
		
		//adjust parent table row height to fit wrapped
		//description text
		requestRowResize();		
	}
	
	/**
	 * Instantiates and constructs UI components.
	 */
	private void initComponents () {
		
		//set up the source panel
		setPreferredSize(new Dimension(
				Constants.ITEMPANEL_WIDTH, Constants.ITEMPANEL_HEIGHT_EXPANDED));
		setLayout(new BorderLayout(0, 0));

		//instantiate and construct the header panel and
		//header components
		this.uiPanelHeader = new JPanel();
		add(this.uiPanelHeader, BorderLayout.NORTH);
		this.uiPanelHeader.setLayout(new BorderLayout(0, 0));
		this.uiBtnStatus = new JButton("");
		this.uiBtnStatus.setBackground(Constants.COLOR_ITEM);
		this.uiBtnStatus.addActionListener(new UIActionStatus());
		this.uiBtnStatus.setPreferredSize(new Dimension(
			Constants.ITEMPANEL_HEIGHT, Constants.ITEMPANEL_HEIGHT));
		this.uiBtnStatus.setBorderPainted(false);
		this.uiBtnStatus.setFocusPainted(false);
		this.uiPanelHeader.add(this.uiBtnStatus, BorderLayout.WEST);
		this.uiTextField = new JTextField();
		this.uiTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.uiTextField.setPreferredSize(new Dimension(6, Constants.ITEMPANEL_HEIGHT));
		this.uiPanelHeader.add(this.uiTextField, BorderLayout.CENTER);
		this.uiTextField.setColumns(10);
		
		this.uiPanelDrop = new JPanel();
		this.uiPanelDrop.setBackground(Constants.COLOR_ITEM);
		this.uiPanelDrop.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.uiPanelDrop.setSize(new Dimension(Constants.ITEMPANEL_HEIGHT, Constants.ITEMPANEL_HEIGHT));
		this.uiPanelDrop.setMaximumSize(new Dimension(Constants.ITEMPANEL_HEIGHT, Constants.ITEMPANEL_HEIGHT));
		this.uiPanelDrop.setMinimumSize(new Dimension(Constants.ITEMPANEL_HEIGHT, Constants.ITEMPANEL_HEIGHT));
		this.uiPanelDrop.setPreferredSize(new Dimension(Constants.ITEMPANEL_HEIGHT, Constants.ITEMPANEL_HEIGHT));
		this.uiPanelHeader.add(this.uiPanelDrop, BorderLayout.EAST);
		this.uiPanelDrop.setLayout(new BorderLayout(0, 0));
		this.uiBtnExpand = new JButton("");
		this.uiBtnExpand.setMargin(new Insets(0, 0, 0, 0));
		this.uiPanelDrop.add(this.uiBtnExpand);
		this.uiBtnExpand.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.uiBtnExpand.setBackground(Constants.COLOR_ITEM);
		this.uiBtnExpand.setPreferredSize(new Dimension(40, 40));
		this.uiBtnExpand.setBorderPainted(false);
		this.uiBtnExpand.setFocusPainted(false);
		this.uiBtnExpand.addActionListener(new UIActionExpand());
		
		//construct description components
		this.uiScrollPane = new JScrollPane();
		this.uiScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		this.uiScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		add(this.uiScrollPane, BorderLayout.CENTER);
		this.uiTextArea = new JTextArea();
		this.uiTextArea.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.uiTextArea.setWrapStyleWord(true);
		this.uiTextArea.setLineWrap(true);
		this.uiTextArea.setBackground(Constants.COLOR_ITEM);
		this.uiTextArea.setFont(Constants.FONT_ITEM_DESCRIPTIOn);
		this.uiTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				requestRowResize();				
			}
		});
		
		this.uiScrollPane.setViewportView(this.uiTextArea);
		this.uiLabelLeftSpacer = new JLabel("");
		this.uiLabelLeftSpacer.setBackground(Constants.COLOR_ITEM);
		this.uiLabelLeftSpacer.setOpaque(true);
		this.uiLabelLeftSpacer.setPreferredSize(new Dimension(Constants.ITEMPANEL_HEIGHT, 0));
		add(this.uiLabelLeftSpacer, BorderLayout.WEST);
		
		this.uiTextField.setFont(Constants.FONT_ITEM_TITLE);
		this.uiTextField.setBackground(Constants.COLOR_ITEM);
	
		//if the item is selected in the table then highlight
		//necessary UI components
		if (isSelected) {
			this.uiBtnStatus.setBackground(Constants.COLOR_HIGHLIGHT2);
			this.uiTextField.setBackground(Constants.COLOR_HIGHLIGHT);
			this.uiBtnExpand.setBackground(Constants.COLOR_HIGHLIGHT2);
			this.uiTextArea.setBackground(Constants.COLOR_HIGHLIGHT);
			this.uiLabelLeftSpacer.setBackground(Constants.COLOR_HIGHLIGHT);
			this.uiPanelDrop.setBackground(Constants.COLOR_HIGHLIGHT2);
		}
		
		//add the control popup menu to panel components
		this.uiTextField.addMouseListener(new PopupListener(popupMenu));
		this.uiBtnStatus.addMouseListener(new PopupListener(popupMenu));
		this.uiTextField.addMouseListener(new PopupListener(popupMenu));
		this.uiBtnExpand.addMouseListener(new PopupListener(popupMenu));
		this.uiTextArea.addMouseListener(new PopupListener(popupMenu));
	}
	
	/**
	 * Sets the drop icon according to the expand/collapse
	 * flag.	
	 */
	private void setDropButtonIcon () {
		uiBtnExpand.setIcon(this.flagExpand ?
				Constants.ICON_COLLAPSE : Constants.ICON_EXPAND);
	}
	
	/**
	 * Determines and returns the table row height necessary
	 * to include all lines (wrapped and breaked) within the
	 * text area.
	 * 
	 * @return
	 */
	private int getTextAreaHeight () {
		JTextArea dummyPane = new JTextArea();
		
		//make sure the dummy pane area is identical to
		//the description text area
		dummyPane.setLineWrap(true);
		dummyPane.setWrapStyleWord(true);
		dummyPane.setBorder(uiTextArea.getBorder());
		
		//width = parent table width - spacer width - 1
		dummyPane.setSize(parentTable.getSize().width
				- Constants.ITEMPANEL_HEIGHT - 1, 32);
		dummyPane.setText(uiTextArea.getText());
		dummyPane.setFont(uiTextArea.getFont());
		return dummyPane.getPreferredSize().height;
	}
	
	/**
	 * Determines if this item's row height in the parent table
	 * matches the height required to display all this item's
	 * UI components (including wrapped description text).
	 */
	private void requestRowResize () {
		int textAreaHeight = getTextAreaHeight();
		int rowHeight = 34 + textAreaHeight;
	
		if (flagExpand && (parentTable.getRowHeight(this.rowIndex) != rowHeight)) {
			parentTable.setRowHeight(rowIndex, rowHeight);
		}
		
		if (!flagExpand && (parentTable.getRowHeight(this.rowIndex) != Constants.ITEMPANEL_HEIGHT)) {
			parentTable.setRowHeight(rowIndex, Constants.ITEMPANEL_HEIGHT);
		}
	}

	/**
	 * Sets the status button icon according to the status
	 * flag.
	 */
	private void setStatusButtonIcon () {
		ItemPanel.this.uiBtnStatus.setIcon(
				iconFlagStatus[ItemPanel.this.flagStatus]);
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
			
			requestRowResize();
		}
	}
	
	/**
	 * Defines the action for the status button; toggles
	 * the status flag and sets its icon.
	 */
	private class UIActionStatus implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			ItemPanel.this.flagStatus =
					(ItemPanel.this.flagStatus + 1) % Constants.ICON_FLAG_COUNT;
			setStatusButtonIcon();

		}
	}
}
