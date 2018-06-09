package FPP.LinearOptimization.View.Gui;

import javax.swing.JTable;

public interface InputScreenIF {
	

	void setSimplexTableau(Double[][] simplexTableau);
	void setFunctionTable(JTable functionTable);
	void save(String path) throws Exception;
	void setNumRestr(int numRestr);
	int getNumRestr();
	void setNumVar(int numVar);
	int getNumVar();
}
