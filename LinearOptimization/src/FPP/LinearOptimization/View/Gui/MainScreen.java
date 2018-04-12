package FPP.LinearOptimization.View.Gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class MainScreen extends JPanel{
	
	private MainFrame mainFrame;
	
	public MainScreen(){
		initializeScreen();
		
	}

	public void initializeScreen(){
		
		JButton btnDanzig = new JButton("Danzig");
		btnDanzig.setBounds(307, 180, 171, 41);
		btnDanzig.setMargin(new Insets(0, 0, 0, 0));
		btnDanzig.setFont(new Font("Arial", Font.PLAIN, 40));
		btnDanzig.setPreferredSize(new Dimension(25, 25));
		
		JButton btnBenders = new JButton("Benders");
		btnBenders.setBounds(307, 96, 171, 41);
		btnBenders.setMargin(new Insets(0, 0, 0, 0));
		btnBenders.setFont(new Font("Arial", Font.PLAIN, 40));
		btnBenders.setPreferredSize(new Dimension(25, 25));
		btnBenders.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.setTab(1);
				
			}
		});
		this.add(btnBenders);
		this.add(btnDanzig);
	
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
	}

}
