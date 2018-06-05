package FPP.LinearOptimization.View.Gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.*;



public class LoesungsPanel extends JPanel {

	MainFrame hauptFenster;
	
	
	public LoesungsPanel(MainFrame mainFrame) {
		super();
		this.hauptFenster = mainFrame;
	
		this.setLayout(new BorderLayout());
	}

	

	public void addPanel(JComponent c) {
		
		this.removeAll();
		this.add(c, BorderLayout.CENTER);
	
		
		this.repaint();

	}
	

}
