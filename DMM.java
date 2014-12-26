package mmd;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class DMM {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws WriteException 
	 * @throws RowsExceededException 
	 */
	public static void main(String[] args) throws IOException, RowsExceededException, WriteException 
	{
		
		//Interface win = new Interface();
		
		
		Path path = Paths.get("d:/BOX/Dropbox/workspace/Testes/M2/M2.txt");
			
		/*
		Matrix m = new Matrix();
		m.readFile(path);
		m.print();
		
		System.out.println();
		
		Path path1 = Paths.get("d:/BOX/Dropbox/workspace/Testes/M2/M2C1.txt");
		m.faultElements(path1);
		
		
		m.orderMatrix();
		m.print();
		
		m.spreadStatus();
		m.print();
		*/
		
		
		DMatrix level = new DMatrix();

		level.readFile(path);
		//level.print();
		
		System.out.println();
		
		
		level.setLevel();
		
		System.out.println();
		level.quickSort();
		//level.print();
		
		Path path1 = Paths.get("d:/BOX/Dropbox/workspace/Testes/M2/M2C6.txt");
		level.faultElements(path1);
		level.spreadStatus();
		System.out.println();
		level.print();
		
		level.writeExcel();
		
		
		/*
		MatrixOld sweep = new MatrixOld();

		sweep.readFile(path);
		
		Path path1 = Paths.get("d:/BOX/Dropbox/workspace/Testes/M1/M1C1.txt");
		
		sweep.faultElements(path1);
		sweep.print();
		
		System.out.println();
		
		sweep.complexSweep();
		System.out.println();
		sweep.print();
		*/
		
		Runtime.getRuntime().exec(new String[] {"cmd.exe", "/C", "d:/BOX/Dropbox/workspace/Projeto/mmd.xls"});

	}
}
