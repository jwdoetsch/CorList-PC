package org.doetsch.jaylist;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class Launcher extends JFrame {

	class MagicRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			if (value != null) {
				ListFrame listFrame = (ListFrame) value;
				LauncherPanel tile = new LauncherPanel(
						listFrame, Launcher.this);
				return tile;
			} else {
				return null;
			}
		}
		
	}
	
	class MagicEditor extends AbstractCellEditor implements TableCellEditor {

		//ListModel lfm;
		ListFrame listFrame;
		
		@Override
		public Object getCellEditorValue() {
			return listFrame;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int col) {

			if (value != null) {
				listFrame = (ListFrame) value;
				LauncherPanel tile = new LauncherPanel(
						listFrame, Launcher.this);
				return tile;
			} else {
				return null;
			}
		}
	}
	
	private JPanel contentPane;
	private JPanel panel;
	private JButton btnNew;
	private JPanel panel_1;
	private JPanel panel_2;
	private JButton btnOpen;
	private JScrollPane scrollPane;
	private JTable table;

	//map of open entries
	private ArrayList<ListFrame> listFrames;
	
	private TrayIcon trayIcon;
	private SystemTray systemTray;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Launcher frame = new Launcher();
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
	public Launcher() {
		setResizable(false);
		initComponents();
		initSystemTray();
	}
	
	private void initSystemTray() {
		systemTray = SystemTray.getSystemTray();
		if (!SystemTray.isSupported()) {
			return;
		}
		
		trayIcon = new TrayIcon(
				new ImageIcon(Launcher.class.
						getResource("resources/tray.png")).getImage(), 
					
				"JayList");
		
		PopupMenu menu = new PopupMenu();
		trayIcon.setPopupMenu(menu);
		MenuItem itemNew = new MenuItem("New");
		MenuItem itemOpen = new MenuItem("Open");
		MenuItem itemExit = new MenuItem("Exit");
		menu.add(itemNew);
		menu.add(itemOpen);
		menu.add(itemExit);
		itemNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newList();
			}
		});
//		itemOpen.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				openList();
//			}
//			
//		});
		
		
		try {
			systemTray.add(trayIcon);
			
		} catch (AWTException e) {
			System.out.println("Can't add system tray icon: " + e.getMessage());
			//e.printStackTrace();
		}
		
		trayIcon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Launcher.this.isActive()) {
					Launcher.this.setVisible(true);
					Launcher.this.requestFocus();
				}
			}
		});
		
		
	}

	private void initComponents() {
		
		//models = new ArrayList<LauncherModel>();
		setTitle("Pinboard - JayList");
		//setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setDefaultCloseOperation()
		//setBounds(100, 100, 358, 368);
		setBounds(368, 16, 64, 64);
		this.contentPane = new JPanel();
		this.contentPane.setBackground(UI.LAUNCHER_COLOR_BG);
		this.contentPane.setPreferredSize(new Dimension(440, 320));
		this.contentPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);
		this.panel = new JPanel();
		this.panel.setOpaque(false);
		this.contentPane.add(this.panel, BorderLayout.WEST);
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
		this.panel_1 = new JPanel();
		this.panel_1.setBorder(new EmptyBorder(2, 2, 2, 2));
		this.panel_1.setOpaque(false);
		//this.panel_1.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.panel_1.setMaximumSize(new Dimension(104, 104));
		this.panel_1.setMinimumSize(new Dimension(104, 104));
		this.panel_1.setPreferredSize(new Dimension(104, 104));
		this.panel_1.setSize(new Dimension(104, 104));
		this.panel.add(this.panel_1);
		this.panel_1.setLayout(new BorderLayout(0, 0));
		
		this.btnNew = new JButton("New");
		this.btnNew.setIcon(new ImageIcon(Launcher.class.getResource("/org/doetsch/jaylist/resources/new_16x16.png")));
		//this.btnNew.setContentAreaFilled(false);
		this.btnNew.setMnemonic('N');
		this.btnNew.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.btnNew.setVerticalTextPosition(SwingConstants.TOP);
		this.btnNew.setBackground(UI.LAUNCHER_COLOR_BUTTON);
		this.btnNew.setForeground(Color.WHITE);
		this.btnNew.setFont(UI.LAUNCHER_FONT);
		this.btnNew.setHorizontalAlignment(SwingConstants.LEADING);
		this.btnNew.setVerticalAlignment(SwingConstants.TOP);
		this.panel_1.add(this.btnNew);
		this.btnNew.setMinimumSize(new Dimension(104, 104));
		this.btnNew.setMaximumSize(new Dimension(104, 104));
		this.btnNew.setPreferredSize(new Dimension(104, 104));
		this.btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newList();
			}
		});
		
//		this.panel_2 = new JPanel();
//		this.panel_2.setOpaque(false);
//		this.panel_2.setSize(new Dimension(104, 104));
//		this.panel_2.setPreferredSize(new Dimension(104, 104));
//		this.panel_2.setMinimumSize(new Dimension(104, 104));
//		this.panel_2.setMaximumSize(new Dimension(104, 104));
//		this.panel_2.setBorder(new EmptyBorder(2, 2, 2, 2));
//		this.panel.add(this.panel_2);
//		this.panel_2.setLayout(new BorderLayout(0, 0));
//		
//		this.btnOpen = new JButton("Open");
//		this.btnOpen.setBorder(new EmptyBorder(4, 4, 4, 4));
//		this.btnOpen.setVerticalAlignment(SwingConstants.TOP);
//		this.btnOpen.setVerticalTextPosition(SwingConstants.BOTTOM);
//		this.btnOpen.setIcon(new ImageIcon(Launcher.class.getResource("/org/doetsch/jaylist/resources/new_16x16.png")));
//		this.btnOpen.setMnemonic('O');
//		this.btnOpen.setBackground(UI.LAUNCHER_COLOR_BUTTON);
//		this.btnOpen.setForeground(Color.WHITE);
//		this.btnOpen.setPreferredSize(new Dimension(104, 104));
//		this.btnOpen.setMinimumSize(new Dimension(104, 104));
//		this.btnOpen.setMaximumSize(new Dimension(104, 104));
//		this.btnOpen.setMargin(new Insets(0, 0, 0, 0));
//		this.btnOpen.setHorizontalAlignment(SwingConstants.LEADING);
//		this.btnOpen.setFont(new Font("Arial", Font.PLAIN, 16));
//		this.btnOpen.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				openList();
//			}
//		});
//		
//		this.panel_2.add(this.btnOpen, BorderLayout.CENTER);
		this.scrollPane = new JScrollPane();
		this.scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.scrollPane.setBackground(UI.LAUNCHER_COLOR_BG);
		this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.contentPane.add(this.scrollPane, BorderLayout.CENTER);
		this.table = new JTable();
		this.table.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.table.setShowGrid(false);
		this.table.setBackground(UI.LAUNCHER_COLOR_BG);
		this.table.setShowVerticalLines(false);
		this.table.setShowHorizontalLines(false);
		this.table.setRowSelectionAllowed(false);
		this.table.setColumnSelectionAllowed(false);

		this.table.setModel(new DefaultTableModel(
				new ListFrame[][] {
						{},
					},
					new String[] {
						
					}
				));
		//this.table.setCellSelectionEnabled(true);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.table.setFillsViewportHeight(true);
		this.scrollPane.setViewportView(this.table);
		this.table.setTableHeader(null);
		this.table.setRowHeight(104);
		this.table.setDefaultRenderer(Object.class, new MagicRenderer());
		this.table.setDefaultEditor(Object.class, new MagicEditor());
		this.table.setIntercellSpacing(new Dimension(0, 0));

		
		pack();

		//search lists/ and instantiate array of ListFrameModels
		//refresh the table model with the current ListFrameModels ..syncTable;;;
		loadLists();
		
	}

   
	/*
	 * Patterns the table's model after the contents of
	 * the history array. Renders the linear list of 
	 * LauncherModel objects to the n x 3 table, where
	 * n is the number of rows necessary to accommodate
	 * all the LauncherModels.
	 */
	void syncTableModel () {
		
		int col, row;
		int cols = 3;
		
		int rows = (listFrames.size() / cols)
				//if there are only 1 or 2 cells in the row then count the 
				//row as a whole row
				+ (listFrames.size() % cols > 0 ? 1 : 0) ;
		
		DefaultTableModel model = new DefaultTableModel(new LauncherPanel[rows][cols], new Object[cols]);
		
		for (int i = 0; i < listFrames.size(); i++) {
			col = i % cols;
			row = i / cols;
			//model.setValueAt(((List<LauncherEntry>)values).get(i).listModel, row, col);
			model.setValueAt(listFrames.get(i), row, col);
		}
		
		table.setModel(model);				
	}
	
	void loadLists () {

		ListMarshall marshaller = new ListMarshall();
		ListModel model;
		listFrames = new ArrayList<ListFrame>();
		
		
		//open lists folder and find lists

		File listFolder;
		try {
			listFolder = new File(
					Launcher.class.getResource("../../../lists/").toURI());
		
			for (File f : listFolder.listFiles()) {
				
				try {
					model = marshaller.unmarshall(f.toURI().toURL());
					model.setPath(f.toURI().toURL());
					
//					if (!entries.containsKey(model.getPath())) {
//						LauncherEntry entry =
//								new LauncherEntry(
//										model, false, new ListFrame(this, model, true));
//						entries.put(model.getPath(), entry);
//					}
					listFrames.add(
							new ListFrame(Launcher.this, model));
					
							
				} catch (IOException | SAXException | ParserConfigurationException e) {
					System.out.println(f.getPath() + " isn't valid");
				}
			}
			
			
			
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		
		syncTableModel();
		
	}

	public void newList() {
		ListMarshall marshall = new ListMarshall();
			ListFrame newFrame;
		try {
			//newFrame = new ListFrame(this, marshall.unmarshall(UI.XMl_NEW_LIST), false);
			newFrame = new ListFrame(
					Launcher.this, marshall.unmarshall(UI.XMl_NEW_LIST));
			newFrame.setVisible(true);
			newFrame.setLocation(this.getLocation().x + 64,
					this.getLocation().y + 64);
		} catch (IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}
				
	}
	
//	void openList () {
//		JFileChooser fc = new JFileChooser();
//
//		int state = fc.showOpenDialog(this);
//		if (state == JFileChooser.APPROVE_OPTION) {
//			ListMarshall marshall = new ListMarshall();
//			try {
//				ListFrame newList;
//				newList = new ListFrame(this, marshall.unmarshall(fc.getSelectedFile().toURI().toURL()), true);
//				newList.setVisible(true);
//				
//			/*
//			 * catch XML decode and file IO exceptions and notify the
//			 * user via dialog
//			 */
//			} catch (IOException | SAXException
//					| ParserConfigurationException e1) {
//				JOptionPane.showMessageDialog(this, "Can't open the selected file.");
//			}
//		}
//	}

//	void open (ListModel listModel) {
////		ListMarshall marshall = new ListMarshall();
////		
////		try {
//			
//			if (entries.containsKey(listModel.getPath())) {
//				LauncherEntry entry = entries.get(listModel.getPath());
//				if (entry.isOpen) {
//					entry.listFrame.requestFocus();
//					entry.listFrame.setVisible(true);
//				} else {
//					entry.isOpen = true;
//					entry.listFrame.setLocation(
//							new Point(
//									this.getX(), this.getY()));
//					entry.listFrame.setVisible(true);
//				}
//			}
//			
////			ListFrame newList;
////			newList = new ListFrame(this, marshall.unmarshall(listModel.getPath()), true);
////			Point coords = new Point(
////					this.getX() + 48, this.getY() + 48);
////			newList.setLocation(coords);
////			newList.setVisible(true);
//			
//			
//		/*
//		 * catch XML decode and file IO exceptions and notify the
//		 * user via dialog
//		 */
////		} catch (IOException | SAXException
////				| ParserConfigurationException e1) {
////			JOptionPane.showMessageDialog(this, "Can't open the selected file.");
////		}
//		
//	}
}
