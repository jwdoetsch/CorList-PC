package org.doetsch.jaylist;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
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
	private JPanel panelCtrl;
	private JButton btnNew;
	private JButton btnCfg;
	private JPanel panelNew;
	private JPanel panelCfg;
	
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
		MenuItem itemExit = new MenuItem("Exit");
		menu.add(itemNew);
		menu.add(itemExit);
		itemNew.addActionListener(new UILauncherNewAction());
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
		this.contentPane.setPreferredSize(new Dimension(441, 320));
		this.contentPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);
		this.panelCtrl = new JPanel();
		this.panelCtrl.setOpaque(false);
		this.contentPane.add(this.panelCtrl, BorderLayout.WEST);
		this.panelCtrl.setLayout(new BoxLayout(this.panelCtrl, BoxLayout.Y_AXIS));
		this.panelNew = new JPanel();
		this.panelNew.setBorder(new EmptyBorder(2, 2, 2, 2));
		this.panelNew.setOpaque(false);
		//this.panel_1.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.panelNew.setMaximumSize(new Dimension(104, 208));
		this.panelNew.setMinimumSize(new Dimension(104, 208));
		this.panelNew.setPreferredSize(new Dimension(104, 208));
		this.panelNew.setSize(new Dimension(104, 208));
		this.panelCtrl.add(this.panelNew);
		this.panelNew.setLayout(new BorderLayout(0, 0));
		
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
		this.panelNew.add(this.btnNew);
		this.panelCfg = new JPanel();
		this.panelCfg.setSize(new Dimension(104, 104));
		this.panelCfg.setPreferredSize(new Dimension(104, 104));
		this.panelCfg.setMinimumSize(new Dimension(104, 104));
		this.panelCfg.setMaximumSize(new Dimension(104, 104));
		this.panelCfg.setBorder(new EmptyBorder(2, 2, 2, 2));
		this.panelCtrl.add(this.panelCfg);
		this.panelCfg.setLayout(new BorderLayout(0, 0));
		this.btnCfg = new JButton("");
		this.btnCfg.setBorderPainted(false);
		this.btnCfg.setBackground(Color.WHITE);
		this.btnCfg.setIcon(new ImageIcon(Launcher.class.getResource("/org/doetsch/jaylist/resources/icon_32.png")));
		this.btnCfg.setFocusPainted(false);
		this.panelCfg.add(this.btnCfg, BorderLayout.CENTER);
		this.btnNew.addActionListener(new UILauncherNewAction());
		

		this.scrollPane = new JScrollPane();
		this.scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.scrollPane.setBackground(UI.LAUNCHER_COLOR_BG);
		this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.contentPane.add(this.scrollPane, BorderLayout.CENTER);
		this.table = new JTable();
		this.table.setBorder(new EmptyBorder(0, 0, 20, 0));
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
		DefaultTableModel launcherModel = new DefaultTableModel(new LauncherPanel[rows][cols], new Object[cols]);
		
		for (int i = 0; i < listFrames.size(); i++) {
			col = i % cols;
			row = i / cols;
			launcherModel.setValueAt(listFrames.get(i), row, col);
		}
		
		table.setModel(launcherModel);				
	}
	
	void loadLists () {
		ListMarshall marshaller = new ListMarshall();
		ListModel model;
		File listFolder;
		
		//(re)set list model
		listFrames = new ArrayList<ListFrame>();

		try {
			//search for the lists/ folder
			listFolder = new File(
					ClassLoader.getSystemClassLoader().getResource("lists/").toURI());
			//seek valid lists
			for (File f : listFolder.listFiles()) {
				
				try {
					model = marshaller.unmarshall(f.toURI().toURL());
					model.setPath(f.toURI().toURL());
					listFrames.add(
							new ListFrame(Launcher.this, model, false));
				
				//catches and reports lists that don't validate
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
			newFrame = new ListFrame(
					Launcher.this, marshall.unmarshall(UI.XMl_NEW_LIST), true);
			newFrame.setVisible(true);
			newFrame.setLocation(this.getLocation().x + 64,
					this.getLocation().y + 64);
		} catch (IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}
				
	}
	
	private class UILauncherNewAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			newList();
		}
	}
	
}
