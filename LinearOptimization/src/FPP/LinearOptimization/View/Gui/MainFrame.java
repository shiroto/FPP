package FPP.LinearOptimization.View.Gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.JTableHeader;

import FPP.LinearOptimization.Controller.IController;
import FPP.LinearOptimization.Data.Algorithm;
import FPP.LinearOptimization.Model.BranchAndBound.BranchAndBound;
import FPP.LinearOptimization.View.Save.*;
public class MainFrame {

	private JFrame frame;
	private JTabbedPane tabs;
	private InputScreenMain inputScreen;
	private JButton btnZoomIn, btnZoomOut;
	private JPanel buttonPanel = new JPanel();
	private JMenuBar menubar;
	private JMenu projektmenu;
	private JMenu tabsmenu;
	private JMenuItem save, open, fresh;
	private JMenuItem closeCurrent, closeAll;
	private IController controller;

	/**
	 * Launch the application.
	 */
	public void newWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame(controller);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public IController GetController() {
		return controller;
	}

	/**
	 * Create the application.
	 */
	public MainFrame(IController controller) {
		this.controller = controller;
		initialize();
	}

	/**
	 * Initialize all the contents of the main window.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(10, 10, 1296, 756);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setTitle("Linear Optimization Tool");
		frame.setIconImage(new ImageIcon(MainFrame.class.getResource("images/logov2.png")).getImage());

		// Menu
		menubar = new JMenuBar();
		projektmenu = new JMenu(Helper.Keyword.PROJECT);
		menubar.add(projektmenu);
		projektmenu.add(fresh = new JMenuItem(Helper.Keyword.NEWPROJECT));
		projektmenu.add(save = new JMenuItem(Helper.Keyword.SAVEPROJECT));
		projektmenu.add(open = new JMenuItem(Helper.Keyword.LOADPROJECT));
		tabsmenu = new JMenu(Helper.Keyword.TABS);
		tabsmenu.add(closeCurrent = new JMenuItem(Helper.Keyword.CLOSECURRENT));
		tabsmenu.add(closeAll = new JMenuItem(Helper.Keyword.CLOSEALL));
		menubar.add(tabsmenu);
		frame.setJMenuBar(menubar);

		tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		tabs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				String title = tabs.getTitleAt(tabs.getSelectedIndex());
				if (title.equals(Helper.Keyword.INPUTBANDB) || title.equals(Helper.Keyword.INPUTBENDERS)
						|| title.equals(Helper.Keyword.INPUTDANTZIG)) {
					save.setEnabled(true);
				} else {
					save.setEnabled(false);
				}
				if (title.equals(Helper.Keyword.INPUTSIMPLEX)) {
					closeCurrent.setEnabled(false);
				} else {
					closeCurrent.setEnabled(true);
				}

			}
		});
		inputScreen = new InputScreenMain(this);

		inputScreen.setVisible(true);
		inputScreen.setLayout(null);
		tabs.addTab(Helper.Keyword.INPUTSIMPLEX, inputScreen);

		frame.add(tabs);
		frame.setVisible(true);

		// Zoom Buttons
		btnZoomIn = new JButton();
		btnZoomIn.setIcon(new ImageIcon(MainFrame.class.getResource("images/plus.png")));
		btnZoomIn.setLocation(5, 5);
		btnZoomIn.setSize(10, 10);
		buttonPanel.add(btnZoomIn);
		btnZoomIn.setVisible(true);
		btnZoomIn.setToolTipText("Zoom In");
		btnZoomIn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				for (Component c : getTabs().getComponents())
					zoomIn(c);
			}
		});

		btnZoomOut = new JButton();
		btnZoomOut.setIcon(new ImageIcon(MainFrame.class.getResource("images/minus.png")));
		btnZoomOut.setLocation(40, 5);
		btnZoomOut.setSize(10, 10);
		buttonPanel.add(btnZoomOut);
		btnZoomOut.setVisible(true);
		btnZoomOut.setToolTipText("Zoom Out");
		btnZoomOut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				for (Component c : getTabs().getComponents())
					zoomOut(c);
			}
		});

		frame.add(buttonPanel, BorderLayout.SOUTH);
		inputScreen.revalidate();
		inputScreen.repaint();

		//Save and Load functionality
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
				fc.setFileHidingEnabled(true);
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (0 == fc.showSaveDialog(frame)) {
					String path = fc.getSelectedFile().getPath();
					if (getTabs().getSelectedComponent() instanceof InputScreenIF) {
						try {
							((InputScreenIF) getTabs().getSelectedComponent()).save(path);
							JOptionPane.showMessageDialog(frame,
									fc.getSelectedFile().getName() + " erfolgreich gespeichert.");
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(frame,
									"Datei konnte nicht gespeichert werden. " + ex.getMessage(), "Fehler",
									JOptionPane.ERROR_MESSAGE);
							ex.printStackTrace();
						}
					}
				}
			}
		});

		fresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newWindow();
			}

		});

		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
				fc.setFileHidingEnabled(true);
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (0 == fc.showOpenDialog(frame)) {
					String path = fc.getSelectedFile().getPath();
					if (path.endsWith(Helper.Keyword.PATHBANDB) || path.endsWith(Helper.Keyword.PATHBENDERS)
							|| path.endsWith(Helper.Keyword.PATHDANZIG)) {
						try {
							if (path.endsWith(Helper.Keyword.PATHBANDB)) {
								loadBandB(path);
							}else if (path.endsWith(Helper.Keyword.PATHBENDERS)) {
								loadBenders(path);
							}else if (path.endsWith(Helper.Keyword.PATHDANZIG)) {
								loadDanzig(path);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(frame, "Datei konnte nicht geladen werden", "Fehler",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(frame, "Bitte ein lineares Optimierungsproblem öffnen");
					}
				}

			}

		});
		
		closeCurrent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int current = tabs.getSelectedIndex();
				tabs.remove(current);
				tabs.setSelectedIndex(current - 1);
			}
		});
		
		closeAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int amountTabs = tabs.getTabCount();
				for (int i = amountTabs - 1; i > 0; i--) {
					tabs.remove(i);
				}
				tabs.setSelectedIndex(0);;
				
			}
			
		});
	}

	/**
	 * Loading Benders projects.
	 */
	protected void loadBenders(String path)  {
		try {
			BendersSaveClass obj = (BendersSaveClass) LinearOptFileHandler.load(path);
			InputScreenBenders inputBenders = new InputScreenBenders(getMainFrame());
			inputScreen.loadSimplexTableau(obj);
			inputScreen.setAlgorithm(Algorithm.BendersAlgorithm);
			inputScreen.setMinProblem(obj.getMin());
			inputScreen.setOPs(obj.getOPs());
			inputBenders.setVisible(true);
			inputBenders.setLayout(null);
			inputBenders.setFunctionTable(new JTable(inputScreen.getFunctionTable().getModel()));
			inputBenders.createView(obj);
			tabs.addTab(Helper.Keyword.INPUTBENDERS, inputBenders);
			tabs.setSelectedIndex(tabs.indexOfTab(Helper.Keyword.INPUTBENDERS));
			
			
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(frame,
					"Datei konnte nicht geladen werden. " + e.getMessage(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
		
		
	}

	protected void loadDanzig(String path) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Loading Branch and Bound projects.
	 */
	protected void loadBandB(String path) {
		//Laden l = new Laden(path);
		BranchAndBoundSpeicherKlasse objekt;
		try {
			objekt = (BranchAndBoundSpeicherKlasse) LinearOptFileHandler.load(path);
			inputScreen.inputBB = new InputScreenBB(getMainFrame());
			inputScreen.loadSimplexTableau(objekt);
			inputScreen.setAlgorithm(Algorithm.BranchBoundAlgorithm);
			inputScreen.setMinProblem(objekt.getMin());
			inputScreen.setOPs(objekt.getOPs());
			inputScreen.inputBB.setVisible(true);
			inputScreen.inputBB.setLayout(null);
			JTable functionTableCopy = new JTable(inputScreen.getFunctionTable().getModel());
			JTableHeader functionTableCopyHeader = new JTableHeader();
			functionTableCopyHeader.setColumnModel(inputScreen.getFunctionTable().getColumnModel());
			functionTableCopy.setTableHeader(functionTableCopyHeader);
			inputScreen.inputBB.setFunctionTable(functionTableCopy);
			JTable restrictionTableCopy = new JTable(inputScreen.getRestrictionTable().getModel());
			JTableHeader restrictionTableCopyHeader = new JTableHeader();
			restrictionTableCopyHeader.setColumnModel(inputScreen.getRestrictionTable().getColumnModel());
			restrictionTableCopy.setTableHeader(restrictionTableCopyHeader);
			inputScreen.inputBB.setRestrictionTable(restrictionTableCopy);
			inputScreen.inputBB.setSimplexTableau(objekt.getArray());
			inputScreen.inputBB.setNumRestr(objekt.getNumRestr());
			inputScreen.inputBB.setNumVar(objekt.getNumVar());
			inputScreen.inputBB.initializeScreen();
			inputScreen.inputBB.erhalteBandBundErstelleView(objekt);
			tabs.add(Helper.Keyword.INPUTBANDB, inputScreen.inputBB);
			tabs.setSelectedIndex(tabs.indexOfTab(Helper.Keyword.INPUTBANDB));
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * Zooming in all displayed components.
	 */
	protected void zoomIn(Component c) {
		if (c instanceof JPanel) {
			c = (JPanel) c;
			for (Component comp : ((JPanel) c).getComponents())
				zoomIn(comp);
		}
		if (c instanceof JScrollPane) {
			for (Component comp : ((JScrollPane) c).getViewport().getComponents())
				zoomIn(comp);
		}
		c.setSize(c.getWidth() + 1, c.getHeight() + 1);
		Font f = c.getFont();
		c.setFont(new Font(f.getName(), f.getStyle(), f.getSize() + 1));
		if (c instanceof JTable) {
			updateRowHeights((JTable) c);
		}

	}
	
	/**
	 * Zooming out all currently displayed components.
	 */
	protected void zoomOut(Component c) {
		if (c instanceof JPanel) {
			c = (JPanel) c;
			for (Component comp : ((JPanel) c).getComponents())
				zoomOut(comp);
		}
		if (c instanceof JScrollPane) {
			for (Component comp : ((JScrollPane) c).getViewport().getComponents())
				zoomOut(comp);
		}
		c.setSize(c.getWidth() - 1, c.getHeight() - 1);
		Font f = c.getFont();
		c.setFont(new Font(f.getName(), f.getStyle(), f.getSize() - 1));
		if (c instanceof JTable) {
			updateRowHeights((JTable) c);
		}
	}

	/**
	 * Adjusting and updating row heights of tables when zooming.
	 */
	private void updateRowHeights(JTable table) {
		for (int row = 0; row < table.getRowCount(); row++) {
			int rowHeight = table.getRowHeight();

			for (int col = 0; col < table.getColumnCount(); col++) {
				Component comp = table.prepareRenderer(table.getCellRenderer(row, col), row, col);
				rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
			}
			table.setRowHeight(row, rowHeight);
		}
	}

	public void setTab(int i) {
		this.tabs.setSelectedIndex(i);
	}

	public InputScreenMain getInputScreen() {
		return inputScreen;
	}

	public void setInputScreen(InputScreenMain inputScreen) {
		this.inputScreen = inputScreen;
	}

	public JTabbedPane getTabs() {
		return tabs;
	}

	public void setTabs(JTabbedPane tabs) {
		this.tabs = tabs;
	}
	
	public MainFrame getMainFrame() {
		return this;
	}
}
