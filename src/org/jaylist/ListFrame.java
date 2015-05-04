package org.jaylist;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.border.BevelBorder;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.border.SoftBevelBorder;


/**
 * ListFrame is the main UI window that contains the necessary UI
 * components to represent a usable JayList. 
 *  
 * @author Jacob Wesley Doetsch
 */
@SuppressWarnings("serial")
class ListFrame extends JFrame {
	
	public class HiddenItemPanel extends JPanel {
		
		private JLabel lblChk;
		private JTable parentTable;
		private int rowIndex;
		private JLabel lblMore;
		
		/**
		 * Create the panel.
		 */
		public HiddenItemPanel (JTable parentTable, int rowIndex) {
			this.parentTable = parentTable;	
			this.rowIndex = rowIndex;
			initComponents();
			syncRowHeight();
		}
		
		private void initComponents () {
			setBackground(UI.COLOR_ITEM);
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.lblChk = new JLabel("");
			this.lblChk.setIcon(UI.ICON_HIDDEN);
			this.lblChk.setPreferredSize(new Dimension(32, 8));
			add(this.lblChk);
			
			this.lblMore = new JLabel("");
			this.lblMore.setIcon(UI.ICON_HIDDEN2);
			this.lblMore.setPreferredSize(new Dimension(32, 8));
			add(this.lblMore);
		}
		
		private void syncRowHeight () {
		
			int height = UI.ICON_HIDDEN.getIconHeight() + 1;
			
			if (parentTable.getRowHeight(this.rowIndex) != height) {
				parentTable.setRowHeight(this.rowIndex, height);
			}
		}

	}
	
	private JPanel contentPane;
	
	//UI components
	private JScrollPane uiScrollPane;
	private JTable uiTable;
	JTextPane uiTextPane;
	private JPopupMenu uiPopupMenu;
	private JMenuItem uiMenuItemSave;
	private JMenuItem uiMenuItemSaveAs;
	private JMenuItem uiMenuItemOpen;
	private JMenuItem uiMenuItemAdd;
	private JMenuItem uiMenuItemRemove;
	private JMenuItem uiMenuItemNew;
	private JMenuItem uiMenuItemHide;



	//determines save behavior according to whether the list 
	//is a new list or an opened list
	private Launcher launcher; 
	private boolean isNew;
	private URL path;
	
	//user-input UI config attributes
	private ConfigConstants.ListViewMode viewMode;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JButton btnNewButton;
	
	/**
	 * Instantiates a ListFrame that patterns itself after the
	 * given ListModel.
	 * @param listModel the ListModel to pattern
	 */
	public ListFrame (Launcher launcher, ListModel listModel, boolean isNew) {
		this.launcher = launcher;
		this.isNew = isNew;
		
		initComponents();
		injectListModel(listModel);
		//viewMode = ListViewMode.HIDE_COMPLETED;
		viewMode = ConfigConstants.ListViewMode.DEFAULT;
	}
	
	/**
	 * Applies the given ListModel field values to the ListFrame.
	 *  
	 * @param listModel
	 */
	private void injectListModel (ListModel listModel) {
		
		path = listModel.getPath();
		
//		setTitle();
		this.setTitle(listModel.getHeader());
		
		//set header text
		this.uiTextPane.setText(listModel.getHeader());
		
		//build the table model
		DefaultTableModel tableModel = new DefaultTableModel(
				new ItemModel[][] {
				},
				new String[] {
					"Items"
				}
			);
		for (ItemModel itemModel : listModel.getItemModels()) {
			tableModel.addRow(new ItemModel[] {itemModel});
		}

		//select the header text of new lists
		if (isNew) {
			this.uiTextPane.setSelectionStart(0);
			this.uiTextPane.setSelectionEnd(this.uiTextPane.getText().length());
		}
		
		//inject the new TableModel
		this.uiTable.setModel(tableModel);
		
		this.setBounds(this.getBounds().x, this.getBounds().y, 
				listModel.getFrameSize().width, listModel.getFrameSize().height);
		
	}
	
//	private void setTitle () {
//		//set title bar
//		if (isNew) {
//			this.setTitle("new* - JayList");
//
//		} else {
//			File f = new File(path.getFile());
//			this.setTitle(f.getName().replace("%20", " ") + " - JayList");			
//		}		
//	}
	
	private void newItem () {
		DefaultTableModel tableModel =
				(DefaultTableModel)ListFrame.this.uiTable.getModel();
		tableModel.addRow(new ItemModel[] {new ItemModel("<add a title>", "<add a description>", ItemStatus.INCOMPLETE, false, false)});
		
		if (uiTable.isEditing()) {
			uiTable.getCellEditor().stopCellEditing();
		}
		uiTable.getSelectionModel().clearSelection();
		uiTable.changeSelection(uiTable.getRowCount() - 1, 0, true, false);
	}
	
	private void saveAs () {
		JFileChooser fc = new JFileChooser();
		
		int state = fc.showSaveDialog(ListFrame.this);
		if (state == JFileChooser.APPROVE_OPTION) {
			ListMarshall marshall = new ListMarshall();
			
			try {
				path = fc.getSelectedFile().toURI().toURL();
				marshall.marshall(ListFrame.this.extractListModel(), path);
				isNew = false;
				//setTitle();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}			
	}
	
	/**
	 * Creates and returns a  ListModel instance that models the
	 * field values and table item contents. 
	 * 
	 * @return the ListModel
	 */
	private ListModel extractListModel () {
		ListModel listModel = new ListModel();
		listModel.setHeader(this.uiTextPane.getText());
		listModel.setFrameSize(new Dimension(
				this.getBounds().width, this.getBounds().height));
		
		for (int i = 0; i < uiTable.getRowCount(); i++) {
			ItemModel itemModel = (ItemModel)uiTable.getValueAt(i, 0);
			listModel.addItemModels(itemModel);
		}
		
		
		
		return listModel;
	}

	/**
	 * Initialize UI components
	 */
	private void initComponents () {
		
		//set up the JFrame
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100,
				UI.FRAME_WIDTH, UI.FRAME_HEIGHT);
		
		this.setIconImage(UI.ICON_APP.getImage());
		this.setMinimumSize(new Dimension(UI.FRAME_WIDTH, 192));
		
		//set up content pane
		this.contentPane = new JPanel();
		//this.contentPane.setBackground(JLConstants.COLOR_GRID);
		this.contentPane.addMouseListener(new ContentPaneMouseListener());
		this.contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);

		
		
		//set up the header text pane
		this.uiTextPane = new JTextPane();
		this.contentPane.add(this.uiTextPane, BorderLayout.NORTH);
		//center the header text horizontally and vertically
		StyledDocument doc = uiTextPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		StyleConstants.setSpaceAbove(center, 4f);
		StyleConstants.setSpaceBelow(center, 4f);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		this.uiTextPane.addFocusListener(new FocusAdapter() {
			
			//stop table cell editing and deselect item when the focus gained
			@Override
			public void focusGained (FocusEvent arg0) {
				if (uiTable.isEditing()) {
					ListFrame.this.uiTable.getCellEditor().stopCellEditing();
				}
				ListFrame.this.uiTable.getSelectionModel().clearSelection();
			}
			
			//update the frame title when editing is finished
			@Override
			public void focusLost (FocusEvent arg0) {
				ListFrame.this.setTitle(uiTextPane.getText());
			}
		});
		this.uiTextPane.setFont(UI.FONT_HEADER);
		this.uiTextPane.setBackground(UI.COLOR_HEADER_BG);
		this.uiTextPane.setMinimumSize(new Dimension(6, UI.HEADER_HEIGHT));
		
		//set up table scroll pane
		this.uiScrollPane = new JScrollPane();
		this.uiScrollPane.setBorder(new EmptyBorder(5, 0, 0, 0));
		this.uiScrollPane.setBackground(UI.COLOR_GRID);
		this.contentPane.add(this.uiScrollPane, BorderLayout.CENTER);
		
		//set up the table
		this.uiTable = new JTable();
		this.uiTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.uiTable.setFillsViewportHeight(true);
		this.uiTable.setGridColor(UI.COLOR_GRID);
		this.uiTable.setShowVerticalLines(false);
		this.uiTable.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.uiTable.setTableHeader(null);
		//give the table a single row/column so the renderer and editor can
		//be added
		this.uiTable.setModel(new DefaultTableModel(
			new ItemModel[][] {
				{null},
				{},
			},
			new String[] {
				"Items"
			}
		));
		this.uiScrollPane.setViewportView(this.uiTable);
		this.uiTable.setDefaultRenderer(uiTable.getColumnClass(0), new MagicRenderer());
		this.uiTable.setDefaultEditor(uiTable.getColumnClass(0), new MagicEditor());

		{ //disable the escape keystroke when the jtable is focused
			KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
			
			uiTable.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
					escapeKey, "ESCAPE");
		}
		
		//instantiate list popup menu items
		this.uiMenuItemAdd = new JMenuItem("Add Item");
		this.uiMenuItemAdd.addActionListener(new UIItemAddAction());
		this.uiMenuItemAdd.setIcon(UI.ICON_ADD);
		
		this.uiMenuItemRemove = new JMenuItem("Remove Item");
		this.uiMenuItemRemove.setIcon(UI.ICON_REMOVE);
		this.uiMenuItemRemove.addActionListener(new UIBItemRemoveAction());
		
		this.uiMenuItemSave = new JMenuItem("Save");
		this.uiMenuItemSave.setIcon(UI.ICON_MENU_SAVE);
		this.uiMenuItemSave.addActionListener(new UIMenuSaveAction());

		this.uiMenuItemNew = new JMenuItem("New");
		this.uiMenuItemNew.setIcon(UI.ICON_MENU_NEW);
		this.uiMenuItemNew.addActionListener(new UIMenuNewAction());
		
//		this.uiMenuItemOpen = new JMenuItem("Open");
//		this.uiMenuItemOpen.setIcon(new ImageIcon(UI.ICON_MENU_OPEN));
//		this.uiMenuItemOpen.addActionListener(new UIMenuOpenAction());
		
		this.uiMenuItemSaveAs = new JMenuItem("Save As...");
		this.uiMenuItemSaveAs.setIcon(UI.ICON_SAVEAS);
		this.uiMenuItemSaveAs.addActionListener(new UIMenuSaveAsAction());
		
		//instantiate advanced popup menu items
		JMenu uiMenuAdvanced = new JMenu("Advanced");
		this.uiMenuItemHide = new JMenuItem("Hide Completed");
		this.uiMenuItemHide.addActionListener(new UIMenuHideAction());
		uiMenuAdvanced.add(uiMenuItemHide);
		
//		//instantiate and set up the popup menu
		this.uiPopupMenu = new JPopupMenu();
		this.uiPopupMenu.add(uiMenuItemAdd);
		this.uiPopupMenu.add(uiMenuItemRemove);
		this.uiPopupMenu.add(new JSeparator());
		this.uiPopupMenu.add(uiMenuItemNew);
//		this.uiPopupMenu.add(uiMenuItemOpen);
		this.uiPopupMenu.add(uiMenuItemSave);
		this.uiPopupMenu.add(uiMenuItemSaveAs);
		this.uiPopupMenu.add(new JSeparator());
		this.uiPopupMenu.add(uiMenuAdvanced);

		this.uiTable.addMouseListener(new UIOffsetPopupAction(uiPopupMenu, new Point(-56, -12)));
		this.uiTextPane.addMouseListener(new UIOffsetPopupAction(uiPopupMenu, new Point(-56, -12)));
		
		this.uiTable.setModel(new DefaultTableModel( ));
		this.panel = new JPanel();
		this.panel.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.contentPane.add(this.panel, BorderLayout.SOUTH);
		this.panel.setLayout(new BorderLayout(0, 0));
		this.lblNewLabel = new JLabel("New label");
		this.lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		this.lblNewLabel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		this.panel.add(this.lblNewLabel);
		this.btnNewButton = new JButton("");
		this.btnNewButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.btnNewButton.setPreferredSize(new Dimension(16, 16));
		this.btnNewButton.setMinimumSize(new Dimension(16, 16));
		this.btnNewButton.setMaximumSize(new Dimension(16, 16));
		this.panel.add(this.btnNewButton, BorderLayout.EAST);
		
		initUIKeyBindings();
		
		this.uiTable.addMouseWheelListener(new UIMouseWheelBehavior());
	}
	
	class UIMouseWheelBehavior implements MouseWheelListener {

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int rot = e.getWheelRotation();
			if ((e.getModifiersEx() & (InputEvent.SHIFT_DOWN_MASK)) ==
					(InputEvent.SHIFT_DOWN_MASK)) {
				if (rot < 0) {
					shiftRowUp();	
				} else if (rot > 0) {
					shiftRowDown();
				}
			} 
		}
	}
	
	void initUIKeyBindings () {
		
		InputMap tableInputMap = this.uiTable.getInputMap(
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap actionMap = this.uiTable.getActionMap();
		
		tableInputMap.put(KeyStroke.getKeyStroke(
				KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK), "UP_ARROW");
		actionMap.put("UP_ARROW", new UIRowShiftUpAction());

		tableInputMap.put(KeyStroke.getKeyStroke(
				KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK), "DOWN_ARROW");
		actionMap.put("DOWN_ARROW", new UIRowShiftDownAction());

		
	}
	
	void swapListItems (int index1, int index2) {
		DefaultTableModel tableModel = 
				(DefaultTableModel) this.uiTable.getModel();
		
		ItemModel item1 =
				(ItemModel) tableModel.getValueAt(index1, 0);

		ItemModel item2 =
				(ItemModel) tableModel.getValueAt(index2, 0);

		tableModel.setValueAt(item1, index2, 0);
		tableModel.setValueAt(item2, index1, 0);
		
		
		
	}

	@SuppressWarnings("serial")
	class UIRowShiftUpAction extends AbstractAction {
		@Override
		public void actionPerformed (ActionEvent e) {
			shiftRowUp();
		}
	}

	void shiftRowUp () {
		int rowIndex = uiTable.getSelectedRow();
		if ((rowIndex < 1) || (uiTable.getRowCount() < 2)) {
			return;
		}
		
		//swap the selected item with the one above it
		stopEditingClearSelection();
		swapListItems (rowIndex, rowIndex - 1);
		uiTable.getSelectionModel().setSelectionInterval(rowIndex - 1, rowIndex - 1);
	}
	
	@SuppressWarnings("serial")
	class UIRowShiftDownAction extends AbstractAction {
		@Override
		public void actionPerformed (ActionEvent e) {
			shiftRowDown();
		}
	}
	
	void shiftRowDown () {
		int rowIndex = uiTable.getSelectedRow();
		if ((rowIndex == -1)
				|| (rowIndex == uiTable.getRowCount() - 1)
				|| (uiTable.getRowCount() < 2)) {
			return;
		}
		
		//swap the selected row with the one below
		stopEditingClearSelection();
		swapListItems (rowIndex, rowIndex + 1);	
		uiTable.getSelectionModel().setSelectionInterval(rowIndex + 1, rowIndex + 1);
	}
	
	/*
	 * Instantiates and renders an ItemPanel based on ItemModel
	 * instances encapsulated in the table model.
	 */
	class MagicRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			ItemModel sourceModel = (ItemModel)value;
			
			//if the ui view mode is set to hide completed items
			//and this rendering item is completed then return a blank
			if ((viewMode == ConfigConstants.ListViewMode.HIDE_COMPLETED)
					&& (sourceModel.status == ItemStatus.COMPLETE)) {
				return new HiddenItemPanel(uiTable, row);
				
			} else {
				
				ItemPanel itemPanel = new ItemPanel(sourceModel, isSelected,
						uiTable, row, uiPopupMenu);
				return itemPanel;
			}
				
			
		}
		
	}
	

	/*
	 * 
	 */
	@SuppressWarnings("serial")
	class MagicEditor extends AbstractCellEditor implements TableCellEditor {

		//the itempanel that is currently being added
		ItemPanel itemPanel;
		
		/*
		 * forget the changes and deselect the editing item
		 */
		@Override
		public void cancelCellEditing () {
			ListFrame.this.uiTable.getSelectionModel().clearSelection();
			fireEditingCanceled();			
		}
		
		/*
		 * deselect the editing item and apply the changes
		 */
		@Override
		public boolean stopCellEditing () {
			ListFrame.this.uiTable.getSelectionModel().clearSelection();
			fireEditingStopped();
			return true;
		}
		
		/*
		 * return a new ItemModel instance patterned after the 
		 * field values of the currently editing ItemPanel
		 */
		@Override
		public Object getCellEditorValue () {
			return itemPanel.toItemModel();
		}

		/*
		 * return a new ItemPanel instance representing the
		 * selected table ItemModel
		 */
		@Override
		public Component getTableCellEditorComponent (JTable table, Object value,
				boolean isSelected, int row, int column) {

			itemPanel = new ItemPanel((ItemModel)value, true, uiTable,
					row, uiPopupMenu);
			return itemPanel;
		}
		
	}

	private class UIItemAddAction implements ActionListener {

		/*
		 * add a new ItemModel to the list and select the new
		 * item for editing
		 */
		public void actionPerformed(ActionEvent arg0) {
			newItem();
		}
	}
	
	/*
	 * deselect and stop editing when the content pane is clicked
	 */
	private class ContentPaneMouseListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent arg0) {
			stopEditingClearSelection();
		}
	}
	
	
	void stopEditingClearSelection () {
		if (uiTable.isEditing()) {
			ListFrame.this.uiTable.getCellEditor().stopCellEditing();
		}
		ListFrame.this.uiTable.getSelectionModel().clearSelection();
	}
	
	private class UIMenuSaveAction implements ActionListener {
		
		/*
		 * instantiates a JFileChooser, builds a ListModel from the
		 * and uses the ListMarshal to save an XML representation
		 * of the ListModel
		 */
		public void actionPerformed(ActionEvent arg0) {
			
			URL savePath;
			
			stopEditingClearSelection();
			
			/*
			 * if the list is new then trigger the Save As save action 
			 */
			if (isNew) {
				
				saveAs();
				
			} else {
				
				
				savePath = path;
				ListMarshall marshall = new ListMarshall();
				marshall.marshall(ListFrame.this.extractListModel(), savePath);
			
			}
			
			
			
			
			
		}

	
	}
	
//	/*
//	 * 
//	 */
//	private class UIMenuOpenAction implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			
//			stopEditingClearSelection();
//			
//			launcher.openList();
//			
//		}
//	}
	

	private class UIMenuNewAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			stopEditingClearSelection();
			launcher.newList();
		}
	}
	
	private class UIMenuSaveAsAction implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			saveAs();
		}
	}
	
	private class UIBItemRemoveAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			DefaultTableModel tableModel = (DefaultTableModel)uiTable.getModel();
			int row = uiTable.getSelectedRow();
			
			
			if (row != -1) {
				int choice = JOptionPane.showConfirmDialog(
						ListFrame.this,
						"Are you sure you want to remove this item?",
						"Remove Item Confirmation",
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.WARNING_MESSAGE);
				
				if (choice == JOptionPane.OK_OPTION) {
				
					stopEditingClearSelection();
					
					tableModel.removeRow(row);
					
				}
			}
		}
	}
	
	private class UIMenuHideAction implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (ListFrame.this.viewMode) {
				case DEFAULT:
					ListFrame.this.viewMode =
						ConfigConstants.ListViewMode.HIDE_COMPLETED;
					ListFrame.this.uiMenuItemHide.setSelectedIcon(UI.ICON_CHECKED);
					ListFrame.this.uiMenuItemHide.setSelected(true);
					break;
				case HIDE_COMPLETED:
					ListFrame.this.viewMode =
						ConfigConstants.ListViewMode.DEFAULT;
					ListFrame.this.uiMenuItemHide.setSelected(false);
					break;
				default:
					break;
			}
		}
	}
}
