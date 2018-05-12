package FPP.LinearOptimization.View.Gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class MainFrame {

	private JFrame frame;
	private JTabbedPane tabs;
	private JPanel panel;
	private InputScreenMain inputScreen;
	private JButton btnZoomIn, btnZoomOut;
	private double currentZoom = 1.0;
	private JPanel buttonPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(10, 10, 1296, 756);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

		// MainScreen mainScreen = new MainScreen();
		inputScreen = new InputScreenMain(this);

		inputScreen.setVisible(true);
		inputScreen.setLayout(null);
		tabs.addTab("Input", inputScreen);

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
				zoomIn();
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
				zoomOut();
			}
		});

		frame.add(buttonPanel, BorderLayout.SOUTH);
	}

	//Not working yet
	private void zoomIn() {
		if (currentZoom < 1.5) {
			currentZoom += 0.1;
			for (Component c : ((JPanel) tabs.getSelectedComponent()).getComponents()) {
				if (c instanceof Container) {
					Container subContainer = (Container) c;
					for (Component subc : subContainer.getComponents()) {

						subc.setSize(subc.getWidth() + 3, subc.getHeight() + 3);
						Font f = subc.getFont();
						subc.setFont(new Font(f.getName(), f.getStyle(), f.getSize() + 1));
					}
				} else {
					c.setSize(c.getWidth() + 3, c.getHeight() + 3);
					Font f = c.getFont();
					c.setFont(new Font(f.getName(), f.getStyle(), f.getSize() + 1));
					
				}
				if (c instanceof JTable) {
					updateRowHeights((JTable) c);
				}
			}
		}
	}

	private void zoomOut() {
		if (currentZoom > 0.5) {
			currentZoom -= 0.1;
			Component[] components = ((JPanel) tabs.getSelectedComponent()).getComponents();
			for (int i = 0; i < components.length; i++) {
				if ((components[i] instanceof Container)) {
					Container subContainer = (Container) components[i];
					Component[] subComps = subContainer.getComponents();
					for (int c = 0; c < subComps.length; c++) {
						if (subComps[c] instanceof JLabel) {
							Font f = subComps[c].getFont();
							subComps[c].setFont(new Font(f.getName(), f.getStyle(), f.getSize() - 1));
						} else {
							subComps[c].setSize(subComps[c].getSize().width + 20, subComps[c].getSize().height - 20);
						}
					}
				} else {
					components[i].setSize(components[i].getSize().width - 20, components[i].getSize().height - 20);
				}
			}
		}

	}

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

}
