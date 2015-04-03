package org.doetsch.jaylist;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.CellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;



import javax.swing.ListSelectionModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextPane;

import java.awt.Dimension;
import java.awt.Color;



import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.swing.Box;
import javax.swing.JPopupMenu;
import javax.swing.BoxLayout;

/**
 * ListFrame is the main UI window that contains the necessary UI
 * components to represent a usable JayList. 
 *  
 * @author Jacob Wesley Doetsch
 */
public class ListFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1516813645310303053L;
	
	private JPanel contentPane;
	
	//UI components
	private JScrollPane uiScrollPane;
	private JTable uiTable;
	private JPanel uiPanelControls;
	private JButton uiBtnAdd;
	private JTextPane uiTextPane;
	private JMenuItem uiMenuItemSave;
	private JMenuItem uiMenuItemOpen;
	private JButton uiBtnRemove;
	private Component uiHorStrut;
	private JMenuItem uiMenuItemNew;
	private JButton uiBtnMenu;
	private JPopupMenu uiPopupMenu;
	
	private boolean isNew;

	/**
	 * Launch a ListFrame instance that opens a new list.
	 * Uses the Metal look and feel.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					//pass the new list template to the new frame
					ListMarshall marshall = new ListMarshall();
					ListFrame frame = new ListFrame(marshall.unmarshall(
							ListFrame.class.getResource("xml/new.xml")), true);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Instantiates a ListFrame that patterns itself after the
	 * given ListModel.
	 * @param listModel the ListModel to pattern
	 */
	public ListFrame (ListModel listModel, boolean isNew) {
		this.isNew = isNew;
		
		initComponents();
		injectListModel(listModel);
	}
	
	/**
	 * Applies the given ListModel field values to the ListFrame.
	 *  
	 * @param listModel
	 */
	private void injectListModel (ListModel listModel) {
		
		//set title bar
		if (isNew) {
			this.setTitle("new* - JayList");
			
		} else {
			File f = new File(listModel.getPath().getFile());
			this.setTitle(f.getName().replace("%20", " ") + " - JayList");
		}
		
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
			this.uiTextPane.setSelectionEnd(25);
		}
		
		//inject the new TableModel
		this.uiTable.setModel(tableModel);
		
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
				JLConstants.FRAME_DEFAULT_WIDTH, JLConstants.FRAME_DEFAULT_HEIGHT);
		this.setIconImage(JLConstants.ICON_APP.getImage());
		this.setMinimumSize(new Dimension(JLConstants.FRAME_DEFAULT_WIDTH, 192));
		
		//set up content pane
		this.contentPane = new JPanel();
		this.contentPane.setBackground(Color.WHITE);
		this.contentPane.addMouseListener(new ContentPaneMouseListener());
		this.contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);

		//set up the menu items
		this.uiMenuItemSave = new JMenuItem("Save");
		this.uiMenuItemSave.addActionListener(new UIMenuItemSaveActionListener());
		this.uiMenuItemNew = new JMenuItem("New");
		this.uiMenuItemNew.addActionListener(new UIMenuItemNewActionListener());
		this.uiMenuItemNew.setIcon(new ImageIcon(ListFrame.class.getResource("/org/doetsch/jaylist/resources/set2/new_20x20.png")));
		this.uiMenuItemOpen = new JMenuItem("Open");
		this.uiMenuItemOpen.addActionListener(new UIMenuItemOpenActionListener());
		this.uiMenuItemOpen.setIcon(new ImageIcon(ListFrame.class.getResource("/org/doetsch/jaylist/resources/set2/open_20x20.png")));
		this.uiMenuItemSave.setIcon(new ImageIcon(ListFrame.class.getResource("/org/doetsch/jaylist/resources/set2/save_20x20.png")));
		
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
			
			//stop table cell editing and deselect item when the header
			//gains focus
			@Override
			public void focusGained(FocusEvent arg0) {
				if (uiTable.isEditing()) {
					ListFrame.this.uiTable.getCellEditor().stopCellEditing();
				}
				ListFrame.this.uiTable.getSelectionModel().clearSelection();
			}
			
		});
		this.uiTextPane.setFont(JLConstants.FONT_HEADER);
		this.uiTextPane.setBackground(JLConstants.COLOR_HEADER_BG);
		this.uiTextPane.setMinimumSize(new Dimension(6, JLConstants.HEADER_HEIGHT));
		
		//set up table scroll pane
		this.uiScrollPane = new JScrollPane();
		this.uiScrollPane.setBorder(new EmptyBorder(5, 0, 0, 0));
		this.contentPane.add(this.uiScrollPane, BorderLayout.CENTER);
		
		//set up the table
		this.uiTable = new JTable();
		this.uiTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.uiTable.setFillsViewportHeight(true);
		this.uiTable.setGridColor(JLConstants.COLOR_GRID);
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
		
		//set up the controls panel
		this.uiPanelControls = new JPanel();
		this.uiPanelControls.setOpaque(false);
		this.uiPanelControls.setBorder(new EmptyBorder(5, 0, 5, 0));
		this.uiPanelControls.setLayout(new BoxLayout(this.uiPanelControls, BoxLayout.X_AXIS));
		this.contentPane.add(this.uiPanelControls, BorderLayout.SOUTH);
		
		//set up the add item button
		this.uiBtnAdd = new JButton("Add");
		this.uiBtnAdd.setBorderPainted(false);
		this.uiBtnAdd.setBackground(JLConstants.COLOR_BUTTON_BG);
		this.uiBtnAdd.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.uiBtnAdd.setPreferredSize(new Dimension(80, 24));
		this.uiBtnAdd.addActionListener(new UIBtnNewActionListener());
		this.uiBtnAdd.setForeground(JLConstants.COLOR_BUTTON_FG);
		
		
		//set up the menu (popup) button
		this.uiBtnMenu = new JButton("");
		this.uiPanelControls.add(this.uiBtnMenu);
		this.uiBtnMenu.setFocusPainted(false);
		this.uiBtnMenu.addMouseListener(new MouseAdapter() {

			/*
			 * show the popup menu on click. opted for a
			 * MouseListener so that mouse coordinates would get passed 
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				uiPopupMenu.show(uiBtnMenu, e.getX(), 
						e.getY() - 80);				
			}

		});
		this.uiBtnMenu.setBackground(Color.WHITE);
		this.uiBtnMenu.setOpaque(true);
		this.uiBtnMenu.setIcon(JLConstants.ICON_BIRD);
		this.uiBtnMenu.setBorder(null);
		this.uiBtnMenu.setBorderPainted(false);
		this.uiBtnMenu.setMinimumSize(new Dimension(JLConstants.HEADER_HEIGHT + 4, JLConstants.HEADER_HEIGHT + 4));
		this.uiBtnMenu.setMaximumSize(new Dimension(JLConstants.HEADER_HEIGHT + 4, JLConstants.HEADER_HEIGHT + 4));
		this.uiBtnMenu.setPreferredSize(new Dimension(JLConstants.HEADER_HEIGHT + 4, JLConstants.HEADER_HEIGHT + 4));
		this.uiPanelControls.add(this.uiBtnAdd);

		//add the spacer
		this.uiHorStrut = Box.createHorizontalStrut(20);
		this.uiHorStrut.setPreferredSize(new Dimension(16, 0));
		this.uiPanelControls.add(this.uiHorStrut);
		
		//set up the remove item button
		this.uiBtnRemove = new JButton("Remove");
		this.uiBtnRemove.addActionListener(new UIBtnRemoveActionListener());
		this.uiBtnRemove.setBorderPainted(false);
		this.uiBtnRemove.setBackground(JLConstants.COLOR_BUTTON_BG);
		this.uiBtnRemove.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.uiBtnRemove.setPreferredSize(new Dimension(80, 24));
		this.uiBtnRemove.setForeground(Color.WHITE);
		this.uiPanelControls.add(this.uiBtnRemove);

		//set up the popup menu
		this.uiPopupMenu = new JPopupMenu();
		this.uiPopupMenu.add(uiMenuItemNew);
		this.uiPopupMenu.add(uiMenuItemOpen);
		this.uiPopupMenu.add(uiMenuItemSave);
		
		this.uiTable.setModel(new DefaultTableModel( ));
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

			ItemPanel itemPanel = new ItemPanel((ItemModel)value, ListFrame.this.uiTable, row, isSelected);
			return itemPanel;
		}
		
	}
	
	/*
	 * 
	 */
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

			itemPanel = new ItemPanel((ItemModel)value, ListFrame.this.uiTable, row, true);
			return itemPanel;
		}
		
	}

	private class UIBtnNewActionListener implements ActionListener {

		/*
		 * add a new ItemModel to the list and select the new
		 * item for editing
		 */
		public void actionPerformed(ActionEvent arg0) {
			DefaultTableModel tableModel =
					(DefaultTableModel)ListFrame.this.uiTable.getModel();
			tableModel.addRow(new ItemModel[] {new ItemModel("<add a title>", "<add a description>", 0, false)});
			
			if (uiTable.isEditing()) {
				uiTable.getCellEditor().stopCellEditing();
			}
			uiTable.getSelectionModel().clearSelection();
			uiTable.changeSelection(uiTable.getRowCount() - 1, 0, true, false);
		}
	}
	
	/*
	 * deselect and stop editing when the content pane is clicked
	 */
	private class ContentPaneMouseListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent arg0) {
			if (uiTable.isEditing()) {
				ListFrame.this.uiTable.getCellEditor().stopCellEditing();
			}
			ListFrame.this.uiTable.getSelectionModel().clearSelection();
		}
	}
	
	
	private class UIMenuItemSaveActionListener implements ActionListener {
		
		/*
		 * instantiates a JFileChooser, builds a ListModel from the
		 * and uses the ListMarshal to save an XML representation
		 * of the ListModel
		 */
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fc = new JFileChooser();
			
			if (uiTable.isEditing()) {
				ListFrame.this.uiTable.getCellEditor().stopCellEditing();
			}
			ListFrame.this.uiTable.getSelectionModel().clearSelection();
			
			int state = fc.showSaveDialog(ListFrame.this);
			if (state == JFileChooser.APPROVE_OPTION) {
				ListMarshall marshall = new ListMarshall();
				try {
					
					marshall.marshall(ListFrame.this.extractListModel(), fc.getSelectedFile().toURI().toURL());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * 
	 */
	private class UIMenuItemOpenActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			
			if (uiTable.isEditing()) {
				ListFrame.this.uiTable.getCellEditor().stopCellEditing();
			}
			ListFrame.this.uiTable.getSelectionModel().clearSelection();
			
			int state = fc.showOpenDialog(ListFrame.this);
			if (state == JFileChooser.APPROVE_OPTION) {
				ListMarshall marshall = new ListMarshall();
				try {
					ListFrame newList;
					newList = new ListFrame(marshall.unmarshall(fc.getSelectedFile().toURI().toURL()), false);
					newList.setVisible(true);
					
				/*
				 * catch XML decode and file IO exceptions and notify the
				 * user via dialog
				 */
				} catch (IOException | SAXException
						| ParserConfigurationException e1) {
					JOptionPane.showMessageDialog(ListFrame.this, "Can't open the selected file.");
				}
			}
			
		}
	}
	
	
	private class UIMenuItemNewActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if (uiTable.isEditing()) {
				ListFrame.this.uiTable.getCellEditor().stopCellEditing();
			}
			ListFrame.this.uiTable.getSelectionModel().clearSelection();
			
			ListMarshall marshall = new ListMarshall();
 			ListFrame newFrame;
			try {
				newFrame = new ListFrame(marshall.unmarshall(ListFrame.class.getResource("xml/new.xml")), true);
				newFrame.setVisible(true);
				newFrame.setLocation(ListFrame.this.getLocation().x + 64,
						ListFrame.this.getLocation().y + 64);
			} catch (IOException | SAXException | ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
	}
	
	/*
	 * 
	 */
	private class UIBtnRemoveActionListener implements ActionListener {
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
				
					if (uiTable.isEditing()) {
						ListFrame.this.uiTable.getCellEditor().stopCellEditing();
					}
					ListFrame.this.uiTable.getSelectionModel().clearSelection();
					
					tableModel.removeRow(row);
					
				}
			}
		}
	}
}
