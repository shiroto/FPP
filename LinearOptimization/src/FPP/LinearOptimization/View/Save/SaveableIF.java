package FPP.LinearOptimization.View.Save;

public interface SaveableIF {
	
	//array = SimplexTableau
	void setArray(Double[][] array);
	Double[][] getArray();
	void setNumVar(int numVar);
	void setNumRestr(int numRestr);
	int getNumVar();
	int getNumRestr();
	Double[] getFunction();
	void setFunction(Double[] function);


	
}
