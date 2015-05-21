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
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
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
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.xml.parsers.ParserConfigurationException;

import org.corlist.util.AdjacentResourceLoader;
import org.xml.sax.SAXException;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import javax.swing.Box;

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
		
		JButton btn;
		private ListFrame listFrame;
		
		private LauncherPanel (ListFrame listFrame) {
			this.listFrame = listFrame;
			initComponents();
		}
		
		private void initComponents () {
			
			setOpaque(false);
			setBorder(new EmptyBorder(0, tilePadding, tilePadding * 2, tilePadding + 2));
			setPreferredSize(new Dimension(tileWidth, tileHeight));
			setLayout(new BorderLayout());
			
			
			this.btn = new JButton();
			setTileText();
			this.btn.setVerticalTextPosition(SwingConstants.TOP);
			
			if (listFrame.isVisible()) {
				this.btn.setBackground(new Color(102, 176, 255));
				this.btn.setFont(new Font("Arial", Font.ITALIC, 16));
				
			} else {
				this.btn.setBackground(UI.LAUNCHER_COLOR_BUTTON);
				this.btn.setFont(UI.LAUNCHER_FONT);

			}
			
			this.btn.setForeground(Color.WHITE);
			this.btn.setBorder(new EmptyBorder(4, 4, 4, 4));
//			this.btn.setMargin(new Insets(0, 0, 0, 0));
			this.btn.setHorizontalAlignment(SwingConstants.LEADING);
			this.btn.setVerticalAlignment(SwingConstants.CENTER);
			
			
			JPanel bgPanel = new JPanel();
			bgPanel.setLayout(new BorderLayout());;
			bgPanel.setBorder(new EmptyBorder(0, 1, 1, 0));
			bgPanel.setBackground(new Color(192, 192, 192));
			bgPanel.add(this.btn, BorderLayout.CENTER);
			add(bgPanel, BorderLayout.CENTER);
			
			
			
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
						refreshLauncherTable();
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
	
	private class LauncherScrollBar extends BasicScrollBarUI {
		
		private JButton blankButton;
		
		private LauncherScrollBar () {
			init();
		}
		
		private void init () {
			blankButton = new JButton();
			blankButton.setSize(new Dimension(0, 0));
			blankButton.setPreferredSize(new Dimension(0, 0));
			blankButton.setMinimumSize(new Dimension(0, 0));
			blankButton.setMaximumSize(new Dimension(0, 0));
			
		}
		
		@Override
        protected void paintThumb (Graphics g, JComponent c, Rectangle r) {
            g.setColor(new Color(54, 138, 230));
            Graphics2D g2d = (Graphics2D)g;
            g2d.drawRect(r.x + 4, r.y, r.width - 9, r.height);
            g2d.fillRect(r.x + 4, r.y, r.width - 9, r.height);
        }

        protected void paintTrack (Graphics g, JComponent c, Rectangle r) {
            g.setColor(new Color(216, 216, 216));
            Graphics2D g2d = (Graphics2D)g;
            g2d.drawRect(r.x + 7, r.y, r.width - 15, r.height);
            g2d.fillRect(r.x + 7, r.y, r.width - 15, r.height);
        }

        @Override
        protected JButton createDecreaseButton (int orientation) {
            return blankButton;
        }

        @Override
        protected JButton createIncreaseButton (int orientation) {
           return blankButton;
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
	
//	private TrayIcon trayIcon;
//	private SystemTray tray;
	private SystemTrayCoupler systemTrayCoupler;

	private int tilePadding = 2;
	private int tileWidth = 256;
	private int tileHeight = 40;
	private JPanel uiPanelNewBG;
	private Component horizontalStrut;

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

		PopupMenu menu = new PopupMenu();
		MenuItem itemNew = new MenuItem("New");
		MenuItem itemExit = new MenuItem("Exit");
		menu.add(itemNew);
		menu.add(itemExit);
		
		itemNew.addActionListener(new UILauncherNewAction());
		itemExit.addActionListener(new UILauncherExitAction());
		
		try {
			systemTrayCoupler = new SystemTrayCoupler(
					"CorList", menu, UI.ICON_TRAY.getImage(),
					new UILauncherRestoreAction());

		} catch (AWTException e) {
			systemTrayCoupler = null;
			System.out.println("System tray unavailable" + e.getMessage());
		}
		
	}

	private void initComponents () {

		setTitle(UI.LAUNCHER_TITLE);
		setIconImage(UI.ICON_CORLIST_LAUNCHER.getImage());
		setBounds(368, 16, 64, 64);
		
		try {
			this.uiContentPane = new TexturedPanel(Launcher.class.getResource(
					"assets/gfx/texture.png"));
			
		//fallback is a JPanel instance
		} catch (IOException e) {
			e.printStackTrace();
			this.uiContentPane = new JPanel();
		}
		
		this.uiContentPane.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				
				int y = uiContentPane.getHeight();
				int m = y % 40;
				
				System.out.println("y:" + y + " m:" + m);
				
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		this.uiContentPane.setBackground(UI.LAUNCHER_COLOR_BG);
		this.uiContentPane.setPreferredSize(new Dimension(441, 320));
		this.uiContentPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.uiContentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.uiContentPane);
		this.uiPanelCtrl = new JPanel();
		this.uiPanelCtrl.setBorder(new EmptyBorder(4, 0, 0, 0));
		this.uiPanelCtrl.setOpaque(false);
		this.uiContentPane.add(this.uiPanelCtrl, BorderLayout.SOUTH);
		this.uiPanelCtrl.setLayout(new BorderLayout(0, 0));
		this.uiPanelNew = new JPanel();
		this.uiPanelNew.setBorder(new EmptyBorder(2, 2, 2, 2));
		this.uiPanelNew.setOpaque(false);
		this.uiPanelNew.setMaximumSize(new Dimension(96, 48));
		this.uiPanelNew.setMinimumSize(new Dimension(96, 48));
		this.uiPanelNew.setPreferredSize(new Dimension(96, 48));
		this.uiPanelNew.setSize(new Dimension(96, 48));
		this.uiPanelCtrl.add(this.uiPanelNew);
		this.uiPanelNew.setLayout(new BorderLayout(0, 0));
		this.uiPanelNewBG = new JPanel();
		this.uiPanelNewBG.setOpaque(false);
		this.uiPanelNewBG.setBorder(new EmptyBorder(0, 1, 1, 0));
		//this.panel_3.setBackground(Color.LIGHT_GRAY);
		this.uiPanelNewBG.setBackground(new Color(208, 208, 208));
		this.uiPanelNew.add(this.uiPanelNewBG, BorderLayout.CENTER);
		this.uiPanelNewBG.setLayout(new BorderLayout(0, 0));
		
		this.uiBtnNew = new JButton("New");
		this.uiPanelNewBG.add(this.uiBtnNew, BorderLayout.CENTER);
		this.uiBtnNew.setIcon(UI.ICON_LAUNCHER_NEW);
		this.uiBtnNew.setMnemonic('N');
		this.uiBtnNew.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.uiBtnNew.setVerticalTextPosition(SwingConstants.TOP);
		this.uiBtnNew.setBackground(UI.LAUNCHER_COLOR_BUTTON);
		this.uiBtnNew.setForeground(Color.WHITE);
		this.uiBtnNew.setFont(UI.LAUNCHER_FONT);
		this.uiBtnNew.setHorizontalAlignment(SwingConstants.LEADING);
		this.uiBtnNew.setVerticalAlignment(SwingConstants.TOP);
		this.horizontalStrut = Box.createHorizontalStrut(20);
		this.horizontalStrut.setPreferredSize(new Dimension(19, 0));
		this.uiPanelNewBG.add(this.horizontalStrut, BorderLayout.EAST);
		this.uiBtnNew.addActionListener(new UILauncherNewAction());

		this.uiScrollPane = new JScrollPane();
		this.uiScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		this.uiScrollPane.getVerticalScrollBar().setUI(new LauncherScrollBar());
		
		
		this.uiScrollPane.setBorder(new EmptyBorder(2, 0, 0, 0));
		//this.uiScrollPane.setBackground(UI.LAUNCHER_COLOR_BG);
		this.uiScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.uiScrollPane.setOpaque(false);
		this.uiScrollPane.getViewport().setOpaque(false);
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
		buildLauncherModel();
		refreshLauncherTable();
	}

   
	/*
	 * Patterns the table's model after the contents of
	 * the history array. Renders the linear list of 
	 * LauncherModel objects to the n x 3 table, where
	 * n is the number of rows necessary to accommodate
	 * all the LauncherModels.
	 */
	void refreshLauncherTable () {
		
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
		
		//repaint();
	}
	
	/*
	 * Loads the lists from the lists/ resource folder,
	 * instantiates a ListFrame for each list, and adds
	 * them to the table model.
	 */
	void buildLauncherModel () {
		ListMarshall marshaller = new ListMarshall();
		ListFrameModel model;
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
			
	}

	public void newList () {
		
		AdjacentResourceLoader loader = AdjacentResourceLoader.getLoader();
		ListMarshall listLoader = new ListMarshall();
		ListFrame newListFrame;
		ListFrameModel newListModel;
		
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
			refreshLauncherTable();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Iterate through, close and dispose ListFrame instances
	 * and this Launcher instance.
	 */
	private void terminate () {
		
		for (ListFrame listFrame : launchModel) {
			if (listFrame != null) {
				listFrame.setVisible(false);
				listFrame.dispose();
			}
		}
		
		//remove the system tray icon
		if (systemTrayCoupler != null) {
			systemTrayCoupler.dispose();
		}
		
		//dispose this Launcher
		this.setVisible(false);
		this.dispose();
	}
	
	void escapeTableFocus () {
		if (uiTable.isEditing()) {
			uiTable.getCellEditor().stopCellEditing();
		}
		uiTable.getSelectionModel().clearSelection();
	}
	
	private class UILauncherNewAction extends AbstractAction {
		@Override
		public void actionPerformed (ActionEvent arg0) {
			newList();
			refreshLauncherTable();
		}
	}
	
	private class UILauncherExitAction extends AbstractAction {
		@Override
		public void actionPerformed (ActionEvent arg0) {
			terminate();
		}
	}
	
	private class UILauncherRestoreAction extends AbstractAction {

		@Override
		public void actionPerformed (ActionEvent e) {
			if (!isActive()) {
				setVisible(true);
				requestFocus();
				setExtendedState(NORMAL);
			}
		}
	}
	
}
