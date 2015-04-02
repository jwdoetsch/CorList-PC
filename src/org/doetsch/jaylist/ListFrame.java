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
import javax.swing.JPanel;
import javax.swing.KeyStroke;
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
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ListSelectionModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextPane;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Color;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.awt.Window.Type;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.swing.Box;

public class ListFrame extends JFrame {

	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTable table;
	private JPanel panel;
	private JButton btnAdd;
	private JTextPane textPane;
	private Point pt1;
	private Point pt2;
	private JMenuBar menuBar;
	private JMenuItem menuItemSave;
	private JMenuItem mntmOpen;
	private JButton btnRemove;
	private Component horizontalStrut;
	private JMenuItem mntmNewMenuItem;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListMarshall marshall = new ListMarshall();
					ListFrame frame = new ListFrame(marshall.unmarshall(
							ListFrame.class.getResource("xml/new.xml")));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ListFrame () {
		initComponents();
	}
	
	public ListFrame (ListModel listModel) {
		initComponents();
		injectListModel(listModel);
	}
	
	private void injectListModel (ListModel listModel) {
		//this.setTitle(listModel.getHeader());
		this.setTitle(listModel.getPath().getFile() + " - JayList");
		try {
			System.out.println(listModel.getPath().toURI().getPath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.textPane.setText(listModel.getHeader());
		
		DefaultTableModel tableModel = new DefaultTableModel(
				new ItemModel[][] {
				},
				new String[] {
					"Items"
				}
			);
			
		this.table.setModel(tableModel);
		
		for (ItemModel itemModel : listModel.getItemModels()) {
			tableModel.addRow(new ItemModel[] {itemModel});
		}
		
	}
	
	private ListModel extractListModel () {
		ListModel listModel = new ListModel();
		listModel.setHeader(this.textPane.getText());
		
		
		for (int i = 0; i < table.getRowCount(); i++) {
			
			ItemModel itemModel = (ItemModel)table.getValueAt(i, 0);
			listModel.addItemModels(itemModel);
		}
		
		return listModel;
	}
	
	private void initComponents () {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100,
				JLConstants.FRAME_DEFAULT_WIDTH, JLConstants.FRAME_DEFAULT_HEIGHT);
		this.setIconImage(JLConstants.ICON_APP.getImage());
		this.setMinimumSize(new Dimension(JLConstants.FRAME_DEFAULT_WIDTH, 192));
		
		
		//set up content pane
		this.contentPane = new JPanel();
		this.contentPane.setBackground(Color.WHITE);
		this.contentPane.addMouseListener(new ContentPaneMouseListener());
		this.menuBar = new JMenuBar();
		setJMenuBar(this.menuBar);
		this.menuItemSave = new JMenuItem("Save");
		this.menuItemSave.addActionListener(new MenuItemSaveActionListener());
		this.mntmNewMenuItem = new JMenuItem("New");
		this.mntmNewMenuItem.addActionListener(new MntmNewMenuItemActionListener());
		this.mntmNewMenuItem.setIcon(new ImageIcon(ListFrame.class.getResource("/org/doetsch/jaylist/resources/set2/new_20x20.png")));
		this.menuBar.add(this.mntmNewMenuItem);
		this.mntmOpen = new JMenuItem("Open");
		this.mntmOpen.addActionListener(new MntmOpenActionListener());
		this.mntmOpen.setIcon(new ImageIcon(ListFrame.class.getResource("/org/doetsch/jaylist/resources/set2/open_20x20.png")));
		this.menuBar.add(this.mntmOpen);
		this.menuItemSave.setIcon(new ImageIcon(ListFrame.class.getResource("/org/doetsch/jaylist/resources/set2/save_20x20.png")));
		this.menuBar.add(this.menuItemSave);
		this.contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);
		
		
		//set up scroll pane
		this.scrollPane = new JScrollPane();
		this.scrollPane.setBorder(new EmptyBorder(5, 0, 0, 0));
		this.contentPane.add(this.scrollPane, BorderLayout.CENTER);
		
		//set up table model
		this.table = new JTable();
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.table.setFillsViewportHeight(true);
		this.table.setGridColor(JLConstants.COLOR_GRID);
		this.table.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.table.setTableHeader(null);
		this.table.setModel(new DefaultTableModel(
			new ItemModel[][] {
				{new ItemModel("Title One", "Description One", 0, true)},
				{new ItemModel("Title Two", "Description Two", 0, false)},
			},
			new String[] {
				"Items"
			}
		));
		this.scrollPane.setViewportView(this.table);
		this.table.setDefaultRenderer(table.getColumnClass(0), new MagicRenderer());
		this.table.setDefaultEditor(table.getColumnClass(0), new MagicEditor());
//		{
//			/*
//			 * force cancelCellEditing() to be called when table cell editing
//			 * stops because the escape key is pressed
//			 */
			KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
			
			//registers the keystroke with the ESCAPE action
			table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
					escapeKey, "ESCAPE");
//					
//			//registers the ESCAPE action
//			table.getActionMap().put("ESCAPE", new AbstractAction() {
//	
//				@Override
//				public void actionPerformed(ActionEvent arg0) {
//					
//					if (table.isEditing()) {
//						table.getCellEditor()
//								.cancelCellEditing();
//					}
//				}
//				
//			});
//		}
		
		this.panel = new JPanel();
		this.panel.setOpaque(false);
		FlowLayout flowLayout = (FlowLayout) this.panel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		this.panel.setBorder(new EmptyBorder(5, 0, 5, 0));
		this.contentPane.add(this.panel, BorderLayout.SOUTH);
		this.btnAdd = new JButton("Add");
		this.btnAdd.setPreferredSize(new Dimension(96, 24));
		this.btnAdd.addActionListener(new BtnNewButtonActionListener());
		this.panel.add(this.btnAdd);
		this.horizontalStrut = Box.createHorizontalStrut(20);
		this.horizontalStrut.setPreferredSize(new Dimension(16, 0));
		this.panel.add(this.horizontalStrut);
		this.btnRemove = new JButton("Remove");
		this.btnRemove.setPreferredSize(new Dimension(96, 24));
		this.panel.add(this.btnRemove);
		this.textPane = new JTextPane();
		this.contentPane.add(this.textPane, BorderLayout.NORTH);
		StyledDocument doc = textPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		this.textPane.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent arg0) {
				if (table.isEditing()) {
					ListFrame.this.table.getCellEditor().stopCellEditing();
				}
				ListFrame.this.table.getSelectionModel().clearSelection();
			}
//
//			@Override
//			public void focusLost(FocusEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
			
		});
		this.textPane.setFont(JLConstants.FONT_HEADER);

	}
	
	class MagicRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			ItemModel renderingModel = (ItemModel)value;
//			if (renderingModel.expanded) {
//				table.setRowHeight(row, JLConstants2.ITEMPANEL_HEIGHT_EXPANDED);
//			} else {
//				table.setRowHeight(row, JLConstants2.ITEMPANEL_HEIGHT);
//			}
			
			ItemPanel itemPanel = new ItemPanel(renderingModel, ListFrame.this.table, row, isSelected);
			return itemPanel;
		}
		
	}
	
	class MagicEditor extends AbstractCellEditor implements TableCellEditor {

		ItemPanel itemPanel;
		
		@Override
		public void cancelCellEditing () {
			ListFrame.this.table.getSelectionModel().clearSelection();
			fireEditingCanceled();			
		}
		
		@Override
		public boolean stopCellEditing () {
			ListFrame.this.table.getSelectionModel().clearSelection();
			fireEditingStopped();
			return true;
		}
		
		@Override
		public Object getCellEditorValue () {

			return itemPanel.toItemModel();
		}

		@Override
		public Component getTableCellEditorComponent (JTable table, Object value,
				boolean isSelected, int row, int column) {

			ItemModel model = (ItemModel)value;
			itemPanel = new ItemPanel(model, ListFrame.this.table, row, true);
			
			return itemPanel;
		}
		
	}

	private class BtnNewButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			System.out.println(ListFrame.this.table.getSelectedRow());
			
			
			DefaultTableModel tableModel =
					(DefaultTableModel)ListFrame.this.table.getModel();
			
			tableModel.addRow(new ItemModel[] {new ItemModel("<add a title>", "<add a description>", 0, false)});
			
			if (table.isEditing()) {
				table.getCellEditor().stopCellEditing();
			}
			table.getSelectionModel().clearSelection();
			table.changeSelection(table.getRowCount() - 1, 0, true, false);
		}
	}
	private class ContentPaneMouseListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent arg0) {
			if (table.isEditing()) {
				ListFrame.this.table.getCellEditor().stopCellEditing();
			}
			ListFrame.this.table.getSelectionModel().clearSelection();
		}
	}
	private class MenuItemSaveActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fc = new JFileChooser();
			
			if (table.isEditing()) {
				ListFrame.this.table.getCellEditor().stopCellEditing();
			}
			ListFrame.this.table.getSelectionModel().clearSelection();
			
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
	private class MntmOpenActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			
			if (table.isEditing()) {
				ListFrame.this.table.getCellEditor().stopCellEditing();
			}
			ListFrame.this.table.getSelectionModel().clearSelection();
			
			int state = fc.showOpenDialog(ListFrame.this);
			if (state == JFileChooser.APPROVE_OPTION) {
				ListMarshall marshall = new ListMarshall();
				
					
				//marshall.marshall(ListFrame.this.extractListModel(), fc.getSelectedFile().toURI().toURL());
				try {
					ListFrame newFrame = new ListFrame(marshall.unmarshall(fc.getSelectedFile().toURI().toURL()));
					newFrame.setVisible(true);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParserConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		}
	}
	private class MntmNewMenuItemActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//JFileChooser fc = new JFileChooser();
			
			if (table.isEditing()) {
				ListFrame.this.table.getCellEditor().stopCellEditing();
			}
			ListFrame.this.table.getSelectionModel().clearSelection();
			
//			int state = fc.showOpenDialog(ListFrame.this);
//			if (state == JFileChooser.APPROVE_OPTION) {
				ListMarshall marshall = new ListMarshall();
				
					
				//marshall.marshall(ListFrame.this.extractListModel(), fc.getSelectedFile().toURI().toURL());
			try {
				ListFrame newFrame = new ListFrame(marshall.unmarshall(ListFrame.class.getResource("xml/new.xml")));
				newFrame.setVisible(true);
				newFrame.setLocation(ListFrame.this.getLocation().x + 64,
						ListFrame.this.getLocation().y + 64);
				
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
