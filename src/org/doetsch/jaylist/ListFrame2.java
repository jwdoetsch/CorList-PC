package org.doetsch.jaylist;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
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

public class ListFrame2 extends JFrame {

	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTable table;
	private JPanel panel;
	private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListFrame2 frame = new ListFrame2();
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
	public ListFrame2() {
		initComponents();
	}
	private void initComponents() {
		
		//set up this JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100,
				JLConstants2.FRAME_DEFAULT_WIDTH, JLConstants2.FRAME_DEFAULT_HEIGHT);
		this.setIconImage(JLConstants2.ICON_APP.getImage());
		
		//set up content pane
		this.contentPane = new JPanel();
		this.contentPane.addMouseListener(new ContentPaneMouseListener());
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);
		
		//set up scroll pane
		this.scrollPane = new JScrollPane();
		this.scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.contentPane.add(this.scrollPane, BorderLayout.CENTER);
		
		//set up table model
		this.table = new JTable();
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.table.setFillsViewportHeight(true);
		this.table.setGridColor(JLConstants2.COLOR_GRID);
		this.table.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.table.setTableHeader(null);
		this.table.setModel(new DefaultTableModel(
			new ItemModel2[][] {
				{new ItemModel2("Title One", "Description One", 0, true)},
				{new ItemModel2("Title Two", "Description Two", 0, false)},
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
	}
	
	class MagicRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			ItemModel2 renderingModel = (ItemModel2)value;
//			if (renderingModel.expanded) {
//				table.setRowHeight(row, JLConstants2.ITEMPANEL_HEIGHT_EXPANDED);
//			} else {
//				table.setRowHeight(row, JLConstants2.ITEMPANEL_HEIGHT);
//			}
			
			ItemPanel2 itemPanel = new ItemPanel2(renderingModel, ListFrame2.this.table, row, isSelected);
			return itemPanel;
		}
		
	}
	
	class MagicEditor extends AbstractCellEditor implements TableCellEditor {

		ItemPanel2 itemPanel;
		
		@Override
		public void cancelCellEditing () {
			ListFrame2.this.table.getSelectionModel().clearSelection();
			fireEditingCanceled();			
		}
		
		@Override
		public boolean stopCellEditing () {
			ListFrame2.this.table.getSelectionModel().clearSelection();
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

			ItemModel2 model = (ItemModel2)value;
			itemPanel = new ItemPanel2(model, ListFrame2.this.table, row, true);
			
			return itemPanel;
		}
		
	}

	private class BtnNewButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			System.out.println(ListFrame2.this.table.getSelectedRow());
		}
	}
	private class ContentPaneMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			ListFrame2.this.table.getCellEditor().stopCellEditing();
			ListFrame2.this.table.getSelectionModel().clearSelection();
		}
	}
}
