package mpm;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import jxl.write.WriteException;
import mmd.DMatrix;


public class MPM {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Path path = Paths.get("d:/BOX/Dropbox/workspace/Testes/M2/M2.txt");
		
		DMatrix dmatrix = new DMatrix(); // Creation of the Dependencies Matrix  
		
		dmatrix.readFile(path);
		
		//dmatrix.print();
		//System.out.println();
		
		dmatrix.setLevel();
		dmatrix.quickSort();
		
		dmatrix.print();
		System.out.println();
		
		Path path1 = Paths.get("d:/BOX/Dropbox/workspace/Testes/M2/M2C7.txt");
		dmatrix.faultElements(path1);
		dmatrix.spreadStatus();
		System.out.println();
		try {
			dmatrix.writeExcel();
		} catch (WriteException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MVector vector = new MVector(dmatrix,0);	// Creation of the Markov States Vector  
		//vector.print();
		
		int i = vector.getDimension();
		
		MMatrix mmatrix = new MMatrix(i);		// Creation of the Markov Matrix  
		
		MCalculo calculo = new MCalculo(); 		// Creation of the calculate class  
		
		MMatrix smatrix = calculo.SerialMatrixCreate(dmatrix, mmatrix, vector);  // Creation of the Serial Markov Matrix  
		//smatrix.print(smatrix);
		
		System.out.println();
		
		dmatrix.getRedundancies();
		
		MMatrix mmatrix1 = new MMatrix(i);
		
		MMatrix pmatrix = calculo.ParallelMatrixCreate(dmatrix, mmatrix1, vector);  // Creation of the Parallel Markov Matrix  
		//pmatrix.print(pmatrix);
		
		//System.out.println();
		
		Path path2 = Paths.get("d:/BOX/Dropbox/workspace/Testes/M2/M2C70.txt");
		
		vector = calculo.MatrixAplication(dmatrix, smatrix, pmatrix, path2);
		vector.print();
		
		//Runtime.getRuntime().exec(new String[] {"cmd.exe", "/C", "d:/BOX/Dropbox/workspace/Projeto/mmd.xls"});
	
	}

}
