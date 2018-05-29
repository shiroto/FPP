package FPP.LinearOptimization.View.Gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class Helper {
	
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
	
	public static Double[][] roundStepData(Double[][] array, int dec) {
		Double[][] roundArray = new Double[array.length][array[0].length];
		for(int i = 0; i < array.length;i++) {
			for(int y = 0; y < array[i].length; y++) {
				Double temp = array[i][y];
				temp = temp * Math.pow(10, dec);
				temp = (double) Math.round(temp);
				temp = temp / Math.pow(10, dec);
				roundArray[i][y] = temp;
			}
		}
		return roundArray;
		
	}
	
	public static Double[] roundSolutionData(Double[] array, int dec) {
		Double[] roundArray = new Double[array.length];
		for(int i = 0; i < array.length;i++) {
				Double temp = array[i];
				temp = temp * Math.pow(10, dec);
				temp = (double) Math.round(temp);
				temp = temp / Math.pow(10, dec);
				roundArray[i] = temp;
			}
		return roundArray;
		
	}
}
