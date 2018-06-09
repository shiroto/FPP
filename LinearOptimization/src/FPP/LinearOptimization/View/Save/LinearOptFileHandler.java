package FPP.LinearOptimization.View.Save;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LinearOptFileHandler {


	public static void save(String path, BendersSaveClass bs) throws IOException {
		 FileOutputStream fos = new FileOutputStream(path);
		    ObjectOutputStream oos = new ObjectOutputStream(fos);
		    oos.writeObject(bs);
		    oos.close();
	}
	
	public static SaveableIF load(String path) throws IOException, ClassNotFoundException {
		   FileInputStream fin = new FileInputStream(path);
		   ObjectInputStream ois = new ObjectInputStream(fin);
		   SaveableIF obj= (SaveableIF) ois.readObject();
		   ois.close();
		   return obj;
	}

}
