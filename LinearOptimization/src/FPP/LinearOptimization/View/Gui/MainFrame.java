package FPP.LinearOptimization.View.Gui;

import java.awt.EventQueue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollBar;

public class MainFrame {

	private JFrame frame;
	private JTabbedPane tabs;


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
		frame.getContentPane().setLayout(new GridLayout(1, 1));
		tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		
		MainScreen mainScreen = new MainScreen();
		InputScreenBenders bendersScreen = new InputScreenBenders();
		
		mainScreen.setMainFrame(this);
		
		tabs.addTab("Main", mainScreen);
		mainScreen.setVisible(true);
		mainScreen.setLayout(null);
		mainScreen.setSize(400, 600);
		mainScreen.setLayout(new GridLayout(0, 1, 50, 50));
		mainScreen.setBorder(new EmptyBorder(50, 50, 50, 50));
		bendersScreen.setVisible(true);
		bendersScreen.setLayout(null);
		
		tabs.addTab("Input", bendersScreen);
		

		
		//mainScreen.initializeScreen();
		
		
		frame.getContentPane().add(tabs);
		frame.setVisible(true);

	}

	public void setTab(int i) {
		this.tabs.setSelectedIndex(i);
		
	}
}
