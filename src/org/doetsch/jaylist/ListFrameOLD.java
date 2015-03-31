package org.doetsch.jaylist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;

import javax.swing.ImageIcon;

public class ListFrameOLD extends JFrame {

	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenuItem menuItemSave;
	private JMenuItem menuItemClose;
	private JTextPane textPane;
	private JScrollPane scrollPane;
	private JTable table;
	private Box horizontalBox;
	private JButton buttonAdd;
	private JButton buttonRemove;
	private JMenu menuWindow;
	private JMenuItem menuItemReset;
	private MagicEditor magicEditor;
	private Component horizontalGlue;
	private Component horizontalGlue_1;
	private Component horizontalStrut;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListFrameOLD frame = new ListFrameOLD();
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
	public ListFrameOLD() {
		initComponents();
	}
	
	private void initComponents() {
		
		//set up the frame itself
		this.setIconImage(new ImageIcon(ListFrameOLD.class.getResource("resources/icon_32.png")).getImage());
		this.setTitle("To Do - March 29, 2015.xml - JayList");
		
		//set up frame content pane
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBackground(Color.WHITE);
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		this.contentPane.setPreferredSize(new Dimension(
				JayListConstantsOLD.DEFAULT_FRAME_WIDTH, JayListConstantsOLD.DEFAULT_FRAME_HEIGHT));
		setContentPane(this.contentPane);
		this.contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				clearSelection();
				//table.repaint();
			}
		});
		
		//set up menu and items
		this.menuBar = new JMenuBar();
		setJMenuBar(this.menuBar);
		this.menuFile = new JMenu("File");
		this.menuBar.add(this.menuFile);
		this.menuItemSave = new JMenuItem("Save");
		this.menuFile.add(this.menuItemSave);
		this.menuItemClose = new JMenuItem("Close");
		this.menuFile.add(this.menuItemClose);
		this.menuWindow = new JMenu("Window");
		this.menuBar.add(this.menuWindow);
		this.menuItemReset = new JMenuItem("Reset");
		this.menuItemReset.addActionListener(new MenuItemResetActionListener());
		this.menuWindow.add(this.menuItemReset);
		//this.menuWindow.setFon
		
		
		
		//set up the header text pane such that text centers
		this.textPane = new JTextPane();
		this.textPane.setFont(new Font("Tahoma", Font.PLAIN, 17));
		StyledDocument doc = textPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		this.contentPane.add(this.textPane, BorderLayout.NORTH);
		this.contentPane.add(this.textPane, BorderLayout.NORTH);
		this.textPane.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				clearSelection();
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});

		//set up the scroll pane and table
		this.scrollPane = new JScrollPane();
		this.scrollPane.setBorder(new EmptyBorder(5, 0, 5, 0));
		this.contentPane.add(this.scrollPane, BorderLayout.CENTER);
		this.table = new JTable();
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.table.setTableHeader(null);
		this.table.setRowHeight(40);
		this.table.setModel(new DefaultTableModel(
			new ItemModelOLD[][] {
				{ItemModelOLD.create(1, "1. Foo", "")},
				{ItemModelOLD.create(0, "2. Bar", "")},
			},
			new String[] {
				"Items"
			}
		));
		//if empty white space in the table is clicked then deselect row
		this.table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (table.rowAtPoint(e.getPoint()) == -1) {
					clearSelection();
				}
			}

		});
		this.table.setShowVerticalLines(false);
		this.table.setShowHorizontalLines(false);
		this.table.setFillsViewportHeight(true);
		this.scrollPane.setViewportView(this.table);
		this.table.setDefaultRenderer(this.table.getColumnClass(0), new MagicRenderer());
		this.magicEditor = new MagicEditor();
		this.table.setDefaultEditor(this.table.getColumnClass(0), magicEditor);
		
		
		
		//set up control buttons
		this.horizontalBox = Box.createHorizontalBox();
		this.horizontalBox.setBorder(new EmptyBorder(5, 0, 0, 0));
		this.contentPane.add(this.horizontalBox, BorderLayout.SOUTH);
		this.buttonAdd = new JButton("");
		this.buttonAdd.setBorderPainted(false);
		this.buttonAdd.setMaximumSize(new Dimension(128, 64));
		this.buttonAdd.setMinimumSize(new Dimension(128, 64));
		this.buttonAdd.setPreferredSize(new Dimension(128, 64));
		this.buttonAdd.setBackground(Color.WHITE);
		this.buttonAdd.setIcon(new ImageIcon(ListFrameOLD.class.getResource("/org/doetsch/jaylist/resources/add.png")));
		this.buttonAdd.addActionListener(new ButtonAddActionListener());
		//this.buttonAdd.setColor
		
		this.horizontalGlue = Box.createHorizontalGlue();
		this.horizontalBox.add(this.horizontalGlue);
		this.horizontalBox.add(this.buttonAdd);
		this.buttonRemove = new JButton("");
		this.buttonRemove.setBorderPainted(false);
		this.buttonRemove.setPreferredSize(new Dimension(128, 64));
		this.buttonRemove.setMinimumSize(new Dimension(128, 64));
		this.buttonRemove.setMaximumSize(new Dimension(128, 64));
		this.buttonRemove.setBackground(Color.WHITE);
		this.buttonRemove.setIcon(new ImageIcon(ListFrameOLD.class.getResource("/org/doetsch/jaylist/resources/remove.png")));
		this.buttonRemove.addActionListener(new ButtonRemoveActionListener());
		this.buttonRemove.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		this.horizontalStrut = Box.createHorizontalStrut(5);
		this.horizontalBox.add(this.horizontalStrut);
		this.horizontalBox.add(this.buttonRemove);
		this.horizontalGlue_1 = Box.createHorizontalGlue();
		this.horizontalBox.add(this.horizontalGlue_1);
		
		pack();
	}
	

	/*
	 * Renders the ItemModel selected in the table as an ItemPanel 
	 */
	private class MagicRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus,
				int row, int col) {
			
			ItemPanelOLD curItemPanel = new ItemPanelOLD((ItemModelOLD)value, isSelected,
					ListFrameOLD.this, table.getRowCount());

			//curItemPanel.highlight();
			
			return curItemPanel;
			
		}
		
	}
	
	private class MagicEditor extends AbstractCellEditor implements TableCellEditor {

		private ItemPanelOLD itemPanel;
		
		@Override
		public Object getCellEditorValue() {
			
			return itemPanel.getItemModel();
		}

		
		/*
		 * Renders the ItemModel that's currently being edited as a ItemPanel
		 */
		@Override
		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int col) {

			ItemModelOLD curItemModel = (ItemModelOLD)value;
			itemPanel = new ItemPanelOLD(curItemModel, true, ListFrameOLD.this, row);
			
			
			return itemPanel;
		}
		
	}
	
	void removeItemModel (int row) {
		magicEditor.cancelCellEditing();
		((DefaultTableModel)table.getModel()).removeRow(row);
	}
	
	void clearSelection () {
		table.getSelectionModel().clearSelection();
		magicEditor.cancelCellEditing();
	}

	private class ButtonRemoveActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println(table.getSelectedRow());
			if (table.getSelectedRow() > -1) {
				
//				final JOptionPane choice =
//						new JOptionPane(
//								"Are you sure you want to remove this item?",
//								JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
//				
				int choice = JOptionPane.showConfirmDialog(
						ListFrameOLD.this,
						"Are you sure you want to remove this item?",
						"Remove Item Warning",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
				
				if (choice == JOptionPane.YES_OPTION) {
					removeItemModel(table.getSelectedRow());
				}
			}
		}
	}
	private class ButtonAddActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
			
			tableModel.addRow(new ItemModelOLD[]
					{ItemModelOLD.create(0, "type in the item's title here", "")});
		}
	}
	private class MenuItemResetActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			pack();
		}
	}
}
