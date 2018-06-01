package FPP.LinearOptimization.View.Gui;

import javax.swing.JTable;

public interface InputScreenIF {
	
	Double[][] simplexTableau = null;

	void setSimplexTableau(Double[][] simplexTableau);
	void setFunctionTable(JTable functionTable);
}
