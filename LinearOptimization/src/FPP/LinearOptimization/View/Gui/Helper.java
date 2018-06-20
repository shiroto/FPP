package FPP.LinearOptimization.View.Gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class Helper {
	public static  int KOMMASTELLEN = 3;
	
	/**
	 * Help method for checking if the input string of a text field is a number.
	 */
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	/**
	 * Help method for checking if the input string of a text field is an integer.
	 */
	public static boolean isInteger(String str)  
	{  
	  try  
	  {  
	    double d = Integer.parseInt(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	/**
	 * Help method for adding a component to the GridbagLayout in the BendersSolutionStepScreen.
	 */
	public static void addComponent(Container cont, GridBagLayout gbl, Component c, int x, int y, int width, int height,
			double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbl.setConstraints(c, gbc);
		cont.add(c);
	}
	
	/**
	 * Help method for adding a component with padding to the GridbagLayout in the BendersSolutionStepScreen.
	 */
	public static void addComponentWithPadding(Container cont, GridBagLayout gbl, Component c, int x, int y, int width, int height,
			double weightx, double weighty, int insetTop, int insetLeft, int insetBottom, int insetRight) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.insets = new Insets(insetTop, insetLeft, insetBottom, insetRight);
		gbl.setConstraints(c, gbc);
		cont.add(c);
	}
	
	/**
	 * Help method for rounding the BendersSolutionStepData.
	 */
	public static Double[][] roundStepData(Double[][] array, int dec) {
		Double[][] roundArray = new Double[array.length][array[0].length];
		for(int i = 0; i < array.length;i++) {
			for(int y = 0; y < array[i].length; y++) {
				roundArray[i][y] = Helper.round(array[i][y],dec);
			}
		}
		return roundArray;
		
	}
	
	/**
	 * Help method for rounding the BendersSolutionStepData.
	 */
	public static Double round(Double d, int dec) {
		Double temp;
		temp = d * Math.pow(10, dec);
		temp = (double) Math.round(temp);
		temp = temp / Math.pow(10, dec);
		return temp;
	}
	
	/**
	 * Help method for rounding the BendersSolutionStepData.
	 */
	public static Double[] roundSolutionData(Double[] array, int dec) {
		Double[] roundArray = new Double[array.length];
		for(int i = 0; i < array.length;i++) {
				roundArray[i] = Helper.round(array[i], dec);
			}
		return roundArray;
		
	}
	
	/**
	 * Help method for aligning Table cells.
	 */
	public static void alignCells(JTable table) {
		for (int i = 0; i < table.getColumnCount(); i++) {
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
	}
	
	public static class Keyword{
		
		//Menu
		static final String PROJECT = "Projekt";
		static final String NEWPROJECT = "Neues Projekt";
		static final String SAVEPROJECT = "Projekt speichern";
		static final String LOADPROJECT = "Projekt öffnen";
		
		//MenuTabs
		static final String TABS = "Tabs";
		static final String CLOSECURRENT = "Aktuellen Tab schließen";
		static final String CLOSEALL = "Alle Tabs schließen";
		
		//Tabs
		static final String INPUTBENDERS = "Eingabe Benders";
		static final String INPUTDANTZIG = "Eingabe Dantzig-Wolfe";
		static final String INPUTBANDB = "Eingabe Branch & Bound";
		static final String SOLUTIONBENDERS = "Ergebnis Benders";
		static final String SOLUTIONDANTZIG = "Ergebnis Dantzig-Wolfe";
		static final String SOLUTIONBANDB = "Ergebnis Branch & Bound";
		static final String INPUTSIMPLEX = "Eingabe Normalform";
		static final String STEPBENDERS = "Benders Step: ";
		
		//Paths
		static final String PATHBANDB = ".bAndB";
		static final String PATHDANZIG = ".dw";
		static final String PATHBENDERS = ".benders";
	}
}
