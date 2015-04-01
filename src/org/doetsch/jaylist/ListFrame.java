package org.doetsch.jaylist;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.CellEditor;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.ListSelectionModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextPane;

public class ListFrame extends JFrame {

	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTable table;
	private JPanel panel;
	private JButton btnNewButton;
	private JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListFrame frame = new ListFrame();
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
	public ListFrame() {
		initComponents();
	}
	private void initComponents() {
		
		//set up this JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100,
				JLConstants.FRAME_DEFAULT_WIDTH, JLConstants.FRAME_DEFAULT_HEIGHT);
		this.setIconImage(JLConstants.ICON_APP.getImage());
		
		//set up content pane
		this.contentPane = new JPanel();
		this.contentPane.addMouseListener(new ContentPaneMouseListener());
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
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
				"New column"
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
		this.contentPane.add(this.panel, BorderLayout.SOUTH);
		this.btnNewButton = new JButton("New button");
		this.btnNewButton.addActionListener(new BtnNewButtonActionListener());
		this.panel.add(this.btnNewButton);
		this.textPane = new JTextPane();
		this.contentPane.add(this.textPane, BorderLayout.NORTH);
		
		//this.textPane.getDocument()getCon
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
		}
	}
	private class ContentPaneMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (table.isEditing()) {
				ListFrame.this.table.getCellEditor().stopCellEditing();
			}
			ListFrame.this.table.getSelectionModel().clearSelection();
		}
	}
}
