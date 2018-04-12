package FPP.LinearOptimization.View.Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class InputScreenBenders extends JPanel{
	
	private JTextField tf_xVariables;
	private JTextField tf_yVariables;
	private JTextField tf_restrictions;
	private JTable restrictionTable;
	private JTable functionTable;
	private JButton btnSubmit;
	public InputScreenBenders(){
		initializeScreen();
	}
	
	public void initializeScreen(){
		JPanel jp_xVariables = new JPanel();
		jp_xVariables.setBounds(59, 61, 325, 102);
		this.add(jp_xVariables);
		
		JLabel lb_xVariables = new JLabel("Anzahl x Variablen");
		jp_xVariables.add(lb_xVariables);
		
		tf_xVariables = new JTextField();
		jp_xVariables.add(tf_xVariables);
		tf_xVariables.setColumns(10);
		
		JPanel jp_yVariables = new JPanel();
		jp_yVariables.setBounds(59, 179, 325, 90);
		this.add(jp_yVariables);
		
		JLabel lbl_yVariables = new JLabel("Anzahl Y Variablen");
		jp_yVariables.add(lbl_yVariables);
		
		tf_yVariables = new JTextField();
		jp_yVariables.add(tf_yVariables);
		tf_yVariables.setColumns(10);
		
		JPanel jp_restrictions = new JPanel();
		jp_restrictions.setBounds(59, 294, 325, 95);
		this.add(jp_restrictions);
		
		JLabel lb_restrictions = new JLabel("Anzahl Restriktionen");
		jp_restrictions.add(lb_restrictions);
		
		tf_restrictions = new JTextField();
		jp_restrictions.add(tf_restrictions);
		tf_restrictions.setColumns(10);
		
		JButton btnInput = new JButton("Input");
		btnInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//TODO: Eingaben prüfen
				int xVariables = Integer.parseInt(tf_xVariables.getText());
				int yVariables = Integer.parseInt(tf_yVariables.getText());
				int restrictions = Integer.parseInt(tf_restrictions.getText());
				restrictionTable = new JTable(restrictions, xVariables+yVariables+2);
				restrictionTable.setBounds(446, 74, 435, 225);
				restrictionTable.setVisible(true);
				
				functionTable = new JTable(1, xVariables+yVariables);
				functionTable.setBounds(446, 374, 435, 225);
				functionTable.setVisible(true);
				
				JTableHeader restTh = restrictionTable.getTableHeader();
				TableColumnModel restTcm = restTh.getColumnModel();
				//restriction
				for(int i = 1; i <= xVariables; i++){
					TableColumn tc = restTcm.getColumn(i-1);
					tc.setHeaderValue("x"+i);
				}
				for(int i = 1; i <= yVariables; i++){
					TableColumn tc = restTcm.getColumn(xVariables+i-1);
					tc.setHeaderValue("y"+i);
				}
				TableColumn tc = restTcm.getColumn(xVariables+yVariables);
				tc.setHeaderValue("OP");
				tc = restTcm.getColumn(xVariables+yVariables+1);
				tc.setHeaderValue("b");
				loadProblemTable();
				
				//function
				JTableHeader funcTh = functionTable.getTableHeader();
				TableColumnModel funcTcm = funcTh.getColumnModel();
				for(int i = 1; i <= xVariables; i++){
					TableColumn funcTc = funcTcm.getColumn(i-1);
					funcTc.setHeaderValue("x"+i);
				}
				for(int i = 1; i <= yVariables; i++){
					TableColumn funcTc = funcTcm.getColumn(xVariables+i-1);
					funcTc.setHeaderValue("y"+i);
				}
				loadFunctionTable();
				//Load Submit btn
				btnSubmit = new JButton("Submit");
				btnSubmit.setBounds(1033, 527, 171, 41);
				
				
				}

			
		});
		
		btnInput.setBounds(132, 417, 171, 41);
		this.add(btnInput);
		this.add(btnInput);
		
		btnInput.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				validateInput();
				createDataObject();
				
			}
		});
		
		
		
		
		
	}

	protected void createDataObject() {
		
		
	}

	protected void validateInput() {
		// TODO Flo
		
	}

	protected void loadFunctionTable() {
		JScrollPane scrollPane = new JScrollPane(functionTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(399, 28, 523, 50);
		this.add(scrollPane);
		functionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setBorder(null);
		
	}

	protected void loadProblemTable() {
		JScrollPane scrollPane = new JScrollPane(restrictionTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(399, 100, 523, 325);
		this.add(scrollPane);
		restrictionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
	}
}
