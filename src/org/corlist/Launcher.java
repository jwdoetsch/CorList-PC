package org.corlist;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.TexturePaint;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.xml.parsers.ParserConfigurationException;

import org.corlist.util.AdjacentResourceLoader;
import org.xml.sax.SAXException;

/*
 * Launcher
 * 
 * Launcher is public in scope because AdjacentResourceLoader
 * loads adjacent project resources relative to the launcher
 * class Path. 
 *
 * @author Jacob Wesley Doetsch
 */
@SuppressWarnings("serial")
public class Launcher extends JFrame {

	/*
	 * LaunchTableRenderer returns LauncherPanel instances
	 * encapsulating the ListFrame currently selected by
	 * the Launcher table.
	 */
	private class LaunchTableCellRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent (JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			ListFrame listFrame;
			LauncherPanel tile;
			
			//cells may be empty due to the 3-column nature of
			//the table (e.g. 10 items means 2 empty cells)
			if (value == null) {
				return null;
			}
			
			listFrame = (ListFrame) value;
			tile = new LauncherPanel(
					listFrame);
			tile.setBackground(Color.RED);
			
			return tile;
		}
	}
	
	/*
	 * LaunchTableEditor behaves like LaunchTableRenderer.
	 * When editing is finished, the original ListFrame from
	 * the table will be returned unmodified.
	 */
	private class LaunchTableCellEditor
			extends AbstractCellEditor implements TableCellEditor {

		private ListFrame listFrame;
		
		@Override
		public Object getCellEditorValue () {
			return listFrame;
		}

		@Override
		public Component getTableCellEditorComponent (JTable table, Object value,
				boolean isSelected, int row, int col) {

			LauncherPanel tile;
			
			//cells may be empty due to the 3-column nature of
			//the table (e.g. 10 items means 2 empty cells)
			if (value == null) {
				return null;
			}
			
			listFrame = (ListFrame) value;
			tile = new LauncherPanel(
					listFrame);
			return tile;
		}
	}
	
	/*
	 * LauncherPanel encapsulates a ListFrame instance and is
	 * itself a simple JPanel containing one button that will
	 * show the ListFrame.
	 */
	private class LauncherPanel extends JPanel {
		
		private JButton btn;
		private ListFrame listFrame;
		
		private LauncherPanel (ListFrame listFrame) {
			this.listFrame = listFrame;
			initComponents();
		}
		
		private void initComponents () {
			
			setOpaque(false);
			setBorder(new EmptyBorder(tilePadding, tilePadding, tilePadding, tilePadding));
			setPreferredSize(new Dimension(tileWidth, tileHeight));
			setLayout(new BorderLayout());
			
			
			this.btn = new JButton();
			setTileText();
			this.btn.setVerticalTextPosition(SwingConstants.TOP);
			this.btn.setBackground(UI.LAUNCHER_COLOR_BUTTON);
			this.btn.setForeground(Color.WHITE);
			this.btn.setFont(UI.LAUNCHER_FONT);
			this.btn.setBorder(new EmptyBorder(4, 4, 4, 4));
			this.btn.setMargin(new Insets(0, 0, 0, 0));
			this.btn.setFont(new Font("Arial", Font.PLAIN, 16));
			this.btn.setHorizontalAlignment(SwingConstants.LEADING);
			this.btn.setVerticalAlignment(SwingConstants.CENTER);
			add(this.btn, BorderLayout.CENTER);
			
			this.btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (listFrame.isVisible()) {
						listFrame.requestFocus();
					} else {
						//location set relative to the parent launcher
						listFrame.setLocation(
								new Point(
										Launcher.this.getX() + 48,
										Launcher.this.getY() + 48));
						listFrame.setVisible(true);
					}
				}
			});
		}
		
		/*
		 * Sanitizes the given string to made it HTML-friendly
		 * so that it renders properly within the launcher tile.
		 */
		private String format (String str) {
//			String result = str.replace("<", "&lt;");
//			result = result.replace(">", "&gt;");
			String result = str.replace("\n", " ");
			return result;
		}
		
		private void setTileText () {
			//this.btn.setText("<html><p>" + sanitize(listFrame.uiTextPane.getText()) + "</p></html>");
			this.btn.setText(format(listFrame.uiTextPane.getText()));
		}
	}
	
	private JPanel uiContentPane;
	private JPanel uiPanelCtrl;
	private JButton uiBtnNew;
	private JPanel uiPanelNew;
	private JScrollPane uiScrollPane;
	private JTable uiTable;
	
	private DefaultTableModel tableModel;
	private ArrayList<ListFrame> launchModel;
	
	private TrayIcon trayIcon;
	private SystemTray tray;

	private int tilePadding = 2;
	private int tileWidth = 256;
	private int tileHeight = 48;
	private JPanel panel;
	private JPanel panel_1;
	private JButton button_1;
	private JButton button_2;

	/**
	 * Launch CorList.
	 */
	public static void main (String[] args) {

		Border border = BorderFactory.createLineBorder(
				new Color(255, 188, 86), 1);
		UIManager.put("ToolTip.background",
				new Color(251, 251, 251));
		UIManager.put("ToolTip.border", border);
		
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

	/*
	 * Create the frame.
	 */
	public Launcher () {
		initComponents();
		initSystemTray();
	}
	
	private void initSystemTray () {
		tray = SystemTray.getSystemTray();
		if (!SystemTray.isSupported()) {
			return;
		}
		
		trayIcon = new TrayIcon(UI.ICON_TRAY.getImage(), "JayList");
		
		PopupMenu menu = new PopupMenu();
		trayIcon.setPopupMenu(menu);
		MenuItem itemNew = new MenuItem("New");
		MenuItem itemExit = new MenuItem("Exit");
		menu.add(itemNew);
		menu.add(itemExit);

		itemNew.addActionListener(new UILauncherNewAction());
		try {
			tray.add(trayIcon);
			
		} catch (AWTException e) {
			System.out.println("Can't add system tray icon: " + e.getMessage());
		}
		
		itemExit.addActionListener(new UILauncherExitAction());
		
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

	private void initComponents () {

		setTitle("Pinboard - JayList");
		setIconImage(UI.ICON_CORLIST_LAUNCHER.getImage());
		setBounds(368, 16, 64, 64);
		
		//this.uiContentPane = new JPanel();
		this.uiContentPane = new JPanel () {
			
			   //private TexturePaint paint;

			    @Override
			    protected void paintComponent(Graphics g) {
			        super.paintComponent(g);
			        Graphics2D g2d = (Graphics2D) g;
			        TexturePaint texturePaint;
			        BufferedImage textureImg;
					try {
						textureImg = ImageIO.read(Launcher.class.getResource(
								"assets/gfx/texture.png"));
						texturePaint = new TexturePaint(textureImg,
								new Rectangle(0, 0, textureImg.getWidth(), textureImg.getHeight()));
						g2d.setPaint(texturePaint);
				        g2d.fillRect(0, 0, getWidth(), getHeight());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
			    }
		};
		this.uiContentPane.setBackground(UI.LAUNCHER_COLOR_BG);
		this.uiContentPane.setPreferredSize(new Dimension(441, 320));
		this.uiContentPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.uiContentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.uiContentPane);
		this.uiPanelCtrl = new JPanel();
		this.uiPanelCtrl.setOpaque(false);
		this.uiContentPane.add(this.uiPanelCtrl, BorderLayout.NORTH);
		this.uiPanelCtrl.setLayout(new BoxLayout(this.uiPanelCtrl, BoxLayout.X_AXIS));
		this.uiPanelNew = new JPanel();
		this.uiPanelNew.setBorder(new EmptyBorder(2, 2, 2, 2));
		this.uiPanelNew.setOpaque(false);
		this.uiPanelNew.setMaximumSize(new Dimension(100, 56));
		this.uiPanelNew.setMinimumSize(new Dimension(100, 56));
		this.uiPanelNew.setPreferredSize(new Dimension(100, 56));
		this.uiPanelNew.setSize(new Dimension(100, 56));
		this.uiPanelCtrl.add(this.uiPanelNew);
		this.uiPanelNew.setLayout(new BorderLayout(0, 0));
		
		this.uiBtnNew = new JButton("New");
		this.uiBtnNew.setIcon(UI.ICON_LAUNCHER_NEW);
		this.uiBtnNew.setMnemonic('N');
		this.uiBtnNew.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.uiBtnNew.setVerticalTextPosition(SwingConstants.TOP);
		this.uiBtnNew.setBackground(UI.LAUNCHER_COLOR_BUTTON);
		this.uiBtnNew.setForeground(Color.WHITE);
		this.uiBtnNew.setFont(UI.LAUNCHER_FONT);
		this.uiBtnNew.setHorizontalAlignment(SwingConstants.LEADING);
		this.uiBtnNew.setVerticalAlignment(SwingConstants.TOP);
		this.uiPanelNew.add(this.uiBtnNew);
		this.panel = new JPanel();
		this.panel.setSize(new Dimension(100, 56));
		this.panel.setPreferredSize(new Dimension(100, 56));
		this.panel.setOpaque(false);
		this.panel.setMinimumSize(new Dimension(100, 56));
		this.panel.setMaximumSize(new Dimension(100, 56));
		this.panel.setBorder(new EmptyBorder(2, 2, 2, 2));
		this.uiPanelCtrl.add(this.panel);
		this.panel.setLayout(new BorderLayout(0, 0));
		this.button_2 = new JButton("New");
		this.button_2.setVerticalTextPosition(SwingConstants.TOP);
		this.button_2.setVerticalAlignment(SwingConstants.TOP);
		this.button_2.setMnemonic('N');
		this.button_2.setHorizontalAlignment(SwingConstants.LEADING);
		this.button_2.setForeground(Color.WHITE);
		this.button_2.setFont(new Font("Arial", Font.PLAIN, 16));
		this.button_2.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.button_2.setBackground(new Color(54, 138, 230));
		this.panel.add(this.button_2, BorderLayout.CENTER);
		this.panel_1 = new JPanel();
		this.panel_1.setSize(new Dimension(100, 56));
		this.panel_1.setPreferredSize(new Dimension(100, 56));
		this.panel_1.setOpaque(false);
		this.panel_1.setMinimumSize(new Dimension(100, 56));
		this.panel_1.setMaximumSize(new Dimension(100, 56));
		this.panel_1.setBorder(new EmptyBorder(2, 2, 2, 2));
		this.uiPanelCtrl.add(this.panel_1);
		this.panel_1.setLayout(new BorderLayout(0, 0));
		this.button_1 = new JButton("New");
		this.button_1.setVerticalTextPosition(SwingConstants.TOP);
		this.button_1.setVerticalAlignment(SwingConstants.TOP);
		this.button_1.setMnemonic('N');
		this.button_1.setHorizontalAlignment(SwingConstants.LEADING);
		this.button_1.setForeground(Color.WHITE);
		this.button_1.setFont(new Font("Arial", Font.PLAIN, 16));
		this.button_1.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.button_1.setBackground(new Color(54, 138, 230));
		this.panel_1.add(this.button_1, BorderLayout.CENTER);
		this.uiBtnNew.addActionListener(new UILauncherNewAction());

		this.uiScrollPane = new JScrollPane();
		this.uiScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.uiScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.uiScrollPane.setBackground(UI.LAUNCHER_COLOR_BG);
		this.uiScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.uiContentPane.add(this.uiScrollPane, BorderLayout.CENTER);
		this.uiTable = new JTable();
		this.uiTable.setBorder(new EmptyBorder(0, 0, 20, 0));
		this.uiTable.setShowGrid(false);
		this.uiTable.setBackground(UI.LAUNCHER_COLOR_BG);
		this.uiTable.setShowVerticalLines(false);
		this.uiTable.setShowHorizontalLines(false);
		this.uiTable.setRowSelectionAllowed(false);
		this.uiTable.setColumnSelectionAllowed(false);
		this.uiTable.setModel(new DefaultTableModel(
				new ListFrame[][] {
						{},
					},
					new String[] {
						
					}
				));
		this.uiTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.uiTable.setFillsViewportHeight(true);
		this.uiTable.setTableHeader(null);
		this.uiTable.setRowHeight(tileHeight);
		this.uiTable.setDefaultRenderer(Object.class, new LaunchTableCellRenderer());
		this.uiTable.setDefaultEditor(Object.class, new LaunchTableCellEditor());
		this.uiTable.setIntercellSpacing(new Dimension(0, 0));
		this.uiTable.setOpaque(false);
		this.uiScrollPane.setViewportView(this.uiTable);
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
		int cols = 1;
		int rows = (launchModel.size() / cols)
				//if there are only 1 or 2 cells in the row then count the 
				//row as a whole row
				+ (launchModel.size() % cols > 0 ? 1 : 0) ;
		tableModel = new DefaultTableModel(new ListFrame[rows][cols], new Object[cols]);
		
		for (int i = 0; i < launchModel.size(); i++) {
			col = i % cols;
			row = i / cols;
			tableModel.setValueAt(launchModel.get(i), row, col);
		}
		
		uiTable.setModel(tableModel);			
	}
	
	/*
	 * Loads the lists from the lists/ resource folder,
	 * instantiates a ListFrame for each list, and adds
	 * them to the table model.
	 */
	void loadLists () {
		ListMarshall marshaller = new ListMarshall();
		ListModel model;
		File listFolder;
		
		//(re)set list model
		launchModel = new ArrayList<ListFrame>();
		AdjacentResourceLoader loader = AdjacentResourceLoader.getLoader();
		listFolder = new File(loader.getResource("lists/"));
			
		//seek valid lists
		for (File f : listFolder.listFiles()) {
			try {
				model = marshaller.unmarshall(f.toURI().toURL());
				model.setPath(f.toURI().toURL());
				launchModel.add(
						new ListFrame(Launcher.this, model));

			//catches and reports lists that don't validate
			} catch (IOException | SAXException | ParserConfigurationException e) {
				//System.out.println(f.getPath() + " isn't valid");
			}
		}
			
		syncTableModel();
	}

	public void newList () {
		
		AdjacentResourceLoader loader = AdjacentResourceLoader.getLoader();
		ListMarshall listLoader = new ListMarshall();
		ListFrame newListFrame;
		ListModel newListModel;
		
		//generate a hexadecimal file name based on current system time
		String newFileName = String.format("%05X", System.currentTimeMillis());
		URI newFilePath = loader.getResource("lists/" + newFileName + ".corlist");
		
		//try making a new list XML document in the lists/ folder
		try {
			Files.copy(Paths.get(UI.XMl_NEW_LIST.toURI()), 
					Paths.get(newFilePath));
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		//try instantiating the new list
		try {
			newListModel = listLoader.unmarshall(newFilePath.toURL());
			//newListModel.setPath(newFilePath.toURL());
			newListFrame = new ListFrame(Launcher.this, newListModel);
			launchModel.add(newListFrame);
			newListFrame.setVisible(true);
			newListFrame.setLocation(this.getLocation().x + 64,
					this.getLocation().y + 64);
			syncTableModel();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		
//		
//		ListMarshall marshall = new ListMarshall();
//			ListFrame newFrame;
//		try {
//			newFrame = new ListFrame(
//					Launcher.this, marshall.unmarshall(UI.XMl_NEW_LIST), true);
//			newFrame.setVisible(true);
//			newFrame.setLocation(this.getLocation().x + 64,
//					this.getLocation().y + 64);
//		} catch (IOException | SAXException | ParserConfigurationException e) {
//			e.printStackTrace();
//		}
				
	}
	
	/*
	 * Iterate through, close and dispose ListFrame instances
	 * and this Launcher instance.
	 */
	private void terminate () {
		
//		//close and dispose ListFrames
//		for (int row = 0; row < tableModel.getRowCount(); row++) {
//			for (int col = 0; col < tableModel.getColumnCount(); col++) {
//				listFrame = (ListFrame)tableModel.getValueAt(row, col);
//				
//				if (listFrame != null) {
//					listFrame.setVisible(false);
//					listFrame.dispose();
//				}
//			}
//		}
		
		for (ListFrame listFrame : launchModel) {
			if (listFrame != null) {
				listFrame.setVisible(false);
				listFrame.dispose();
			}
		}
		
		//dispose this Launcher
		this.setVisible(false);
		tray.remove(trayIcon);		
		this.dispose();
	}
	
	void escapeTableFocus () {
		if (uiTable.isEditing()) {
			uiTable.getCellEditor().stopCellEditing();
		}
		uiTable.getSelectionModel().clearSelection();
	}
	
	private class UILauncherNewAction implements ActionListener {
		@Override
		public void actionPerformed (ActionEvent arg0) {
			newList();
		}
	}
	
	private class UILauncherExitAction implements ActionListener {
		@Override
		public void actionPerformed (ActionEvent arg0) {
			terminate();
		}
	}
	
}
