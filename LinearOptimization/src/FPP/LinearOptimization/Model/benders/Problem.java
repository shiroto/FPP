package FPP.LinearOptimization.Model.benders;

import java.util.ArrayList;
import java.util.List;

abstract class Problem {

	private Double[] function = new Double[0];
	private List<Double[]> restrictions = new ArrayList<>();
	
	public Problem(Double[] function) {
		this.function = function;
	}
	
	public Double[] getFunction() {
		return function;
	}
	
	public void setFunction(Double[] function) {
		this.function = function;
	}
	
	public Double[][] getRestrictions() {
		if (restrictions.isEmpty()) {
			return new Double[0][0];
		}
		int size2 = restrictions.get(0).length;
		Double[][] returnValue = new Double[restrictions.size()][size2];
		int i = 0;
		for (Double[] restriction : restrictions) {
			for(int j = 0; j < size2; j++) {
				returnValue[i][j] = restriction[j];
			}
			i++;
		}
		return returnValue;
	}
	
	public void addRestriction(Double[] restriction) {
		/*if (restriction.length != getFunction().length) {
			// TODO: ist das so???
			throw new IllegalStateException(
					"Restriction has to be as long as function!");
		}*/
		restrictions.add(restriction);
	}
	
	public Double[][] getSimplexArray() {
		Double[] function = getFunction();
//		if (restrictions.isEmpty()) {
//			// TODO: zu klären ob das passieren darf?!
//			throw new IllegalStateException(
//					"No restrictions for problem!");
//		}
		
		int i = 0;
		int size2 = function.length;
		Double[][] returnValue = new Double[restrictions.size()+1][size2];
		if (!restrictions.isEmpty()) {
		
			for (Double[] restriction : restrictions) {
				for(int j = 0; j < size2; j++) {
					returnValue[i][j] = restriction[j];
				}
				i++;
			}
			
		}
		for (int k = 0; k < function.length; k++) {
			returnValue[i][k] = function[k];
		}
		
//		//set value to 0, if no constant in function is existing
//		if (returnValue[i][returnValue[i].length - 1] == null) {
//			returnValue[i][returnValue[i].length - 1] = 0d;
//		}
		
		return returnValue;
	}
}