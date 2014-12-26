package mmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import mmd.DList;
import mmd.DNode;

import java.io.File;

public class DMatrix {
		
	private DList home = null;
	private DList end = null;
	
	private int line;
		
		
	//------------------------------------------------------------------------------------------------
	// 	Constructors
	//------------------------------------------------------------------------------------------------
		
	public DMatrix(){} 
		
	//------------------------------------------------------------------------------
	//This method insert a new list with the parameter number to enumerate the lines
	//------------------------------------------------------------------------------
		
	    public void insertList(int number){
	    	
	    	DList aux = new DList(number);
	    	
	        if(home == null){            
	        	this.home = aux;
	            this.end = aux;
	        }
	        
	        else{        	
	        	end.setNext(aux);
	        	aux.setPrevious(end);
	        	this.end = aux;
	        }
	    }
	    
	    //------------------------------------------------------------------------
	    //	This method read the file and save all string into a node of a matrix
	    //------------------------------------------------------------------------
		
	    public void readFile(Path directory){
	    	
	    	StringTokenizer st; //Receive the partitioned element when the string is been read.
	    	Charset utf8 = StandardCharsets.UTF_8;	//import a class...
	    	
	    	try(BufferedReader reader = Files.newBufferedReader(directory, utf8)){			
			
	    		String line;
	    		int i=0, j;
				
	    		while((line = reader.readLine()) != null)
	    		{
	    			st = new StringTokenizer(line);
	    			this.insertList(i);
	    			j=0;
	    			while(st.hasMoreTokens() == true)
	    			{			
	    				this.getEnd().insertNode(j,st.nextToken());
	    				j++;
	    			}
	    			i++;
				}
	    		this.setLine(i);	//save the number of lines in the variable line.
			}catch(IOException e){
				e.printStackTrace();
			}
	    }
		
		//-------------------------------------------------------
		//	Method used to print all the elements of the matrix
		//-------------------------------------------------------
		
	    public void print(){
			DList aux1 = this.home;
			DNode aux2;
			while(aux1 != null){
				aux2 = aux1.getHome();
				System.out.print(aux1.getNumber()+" ");
				System.out.print(aux1.getLevel()+" ");
				while(aux2 != null){
					System.out.print(aux2.getStatus() +" ");
					System.out.print(aux2.getData() +" ");
					aux2 = aux2.getNext();
				}
				aux1 = aux1.getNext();
				System.out.println();
			}
		} 
		
		//--------------------------------------------------
		//This method read the file with the elements fault
		//--------------------------------------------------
		public void faultElements(Path directory){
			
			Byte fault = 2;
			StringTokenizer st; //Receive the partitioned element when the string is been read.
			Charset utf8 = StandardCharsets.UTF_8;	//import a class...
	    	
	    	try(BufferedReader reader = Files.newBufferedReader(directory, utf8)){			
			
	    		String line;
				
	    		while((line = reader.readLine()) != null){
	    			st = new StringTokenizer(line);
	    			while(st.hasMoreTokens() == true){			
	    				this.markElement(st.nextToken(), fault, false, this.getEnd().getNumber());
	    			}
				}	
			}catch(IOException e){
				e.printStackTrace();
			}  	
	    }
		
		//-------------------------------------------------------------------
		//This method mark the fault element in the matrix already organized
		//-------------------------------------------------------------------
		public void markElement(String element, byte status, boolean mark, int line){  	//element is to find the element on the list. 
			DList aux1 = this.home;											 			//status is to mark the element.
			DNode aux2;																	//mark said if we have to mark the elements with the character "@".																		//line said the place where we must start find and at the same time don't let us mark a line that depend on it self. 
			byte fault = 2;
			byte secure = 4;
			while(aux1 != null){
				{
					aux2 = aux1.getHome(); 
					while(aux2 != null)
					{
						if(aux2.getData().endsWith(element) == true && aux2.getStatus() != fault ){	//use endsWith because of elements of the string not always are presented as redundancies or secure fault.   
							if(mark == true && aux2.getStatus() != fault && aux2.getNumber() != 3)
								aux2.setStatus(status); 	//we can't return because CANs can be more than one influences
							else if(aux2.getData().startsWith("FS"))
								aux2.setStatus(secure);
							else if(aux2.getData().contains("@") == false) //mark the fault element in the begin. 
									aux2.setStatus(status);
									
						}
						aux2 = aux2.getNext();
					}
					aux1 = aux1.getNext();
				}
			}
		}
		
		//-------------------------------------------------------
		//This methods get the number of dependencies per line
		//-------------------------------------------------------
	    public void getDependencies(){
	    	DList aux1 = this.home;
	    	DNode aux2;
	    	int i;
	    	String name = null;
	    	while(aux1 != null){
	    		i=0;
	    		aux2 = aux1.getHome(); 
				while(aux2 != null){
					if(aux2.getData().indexOf('@') > -1)	//I want to know if the string have e character @ and if it's a dependences...
						i++;
					if(aux2.getNumber() == 3)
						name = aux2.getData();	//Get the thirst element data to find influences in others lines...
					aux2 = aux2.getNext();
				}
				aux1.setNd(i);  //Set the number of dependencies of line...
				aux1.setNi(findElement(name)); //Set the number of influences of line case exist...
				//aux1.setZbu(aux1.getNd() - aux1.getNi());
				aux1 = aux1.getNext();
				
			}	
	    }
	    
	    //--------------------------------------------------------
	    //This method search the number of redundancies per line. 
	    //--------------------------------------------------------
	    public void getRedundancies(){
	    	DList aux1 = this.home;
			DNode aux2;
			int i;
			while(aux1 != null){
				aux2 = aux1.getHome(); 
				i = 0;
				while(aux2 != null){
					if(aux2.getData().startsWith("R") == true && aux2.getData().substring(2).startsWith(":") == true)	//if the element star with R increment i... 
						i++;
					aux2 = aux2.getNext();
				}
				aux1.setRedundancy(i);			//set the number of redundancies of the line...
				aux1 = aux1.getNext();
			}
	    }
	   
	    //-------------------------------------------------------------------------------
	    //This method find the criteria of redundancies in the last element of the line
	    //-------------------------------------------------------------------------------
	    public void getCriteria(){
	    	DList aux1 = this.home;
			DNode aux2;
			while(aux1 != null){
				aux2 = aux1.getEnd(); 
				if(aux2.getData().startsWith("!") == true)	//if the element star with ! increment save data... 
					aux1.setCriteria((double)Integer.valueOf((aux2.getData().substring(1,2)))/Integer.valueOf((aux2.getData().substring(3,4)))); //convert the string to integer and save.
				aux1 = aux1.getNext();
			}
	    }

	    //-------------------------------------------------------------------------
	    // Find one element and return the number that it have ...
	    //-------------------------------------------------------------------------
	    
	    public int findElement(String name){
	    	DList aux1 = this.home;
			DNode aux2;
			int i=0;
			while(aux1 != null){
				aux2 = aux1.getHome(); 
				while(aux2 != null){
					if(aux2.getData().endsWith(name) == true)	//if the element is in the line increment i... 
						i++;
					aux2 = aux2.getNext();
				}
				aux1 = aux1.getNext();
			}
			return i;			// return i as a number of influence of the line...
		} 
	    
	    //-------------------------------------------------------------------------
	    // Find one element and return if it's in the matrix...
	    //-------------------------------------------------------------------------
	    
	    public boolean checkElement(String name){
	    	DList aux1 = this.home;
			DNode aux2;
			while(aux1 != null){
				aux2 = aux1.getHome(); 
				while( aux2 != null){
					if( aux2.getNumber() > 3  &&  aux2.getData().endsWith(name) )	//if the element is in the line increment i... 
						return true;	
					aux2 = aux2.getNext();
				}
				aux1 = aux1.getNext();
			}
			return false;			// return false if the element  isn't found...
		}
	    
	    //-------------------------------------------------------------------------
	    // Find all the element that haven't influences and mark it like father 
	    // setting it level in one...
	    // Is used the function checkElement() 
	    //-------------------------------------------------------------------------
	    
	    public boolean findFirsts(){
	    	DList aux1 = this.home;
			DNode aux2;
			boolean status = false;  
			while(aux1 != null){
				aux2 = aux1.getHome(); 
				while(aux2 != null){
					if(aux2.getNumber()==3)	//if the element is in the line increment i... 
					{
						if(checkElement(aux2.getData()) == false)
						{
							aux1.setLevel(1);
							status = true;
						}				
					}
					aux2 = aux2.getNext();
				}
				aux1 = aux1.getNext();
			}
			return status;			// return i as a number of influence of the line...
		} 
	    
	    
	    //---------------------------------------------------------------------------
	    // Find the dependencies of the line to establish the levels in the lines...
	    //---------------------------------------------------------------------------
	    
	    public void findDependencies(String name, int level)
	    {
	    	DList aux1 = this.home;
			DNode aux2;
			while(aux1 != null){
				aux2 = aux1.getHome(); 
				while(aux2 != null)
				{
					if(aux2.getNumber()==3 && name.endsWith(aux2.getData()) == true)	//if the line is found set level... 
					{
						aux1.setLevel(level);
						//return;
					}
					aux2 = aux2.getNext();
				}
				aux1 = aux1.getNext();
			}
		} 
	    
	    //---------------------------------------------------------------------
	    // This method set geneological levels to. the lines of the matrix.... 
	    //---------------------------------------------------------------------
	    
	    public void setLevel()
	    {
	    	DList aux1; 
	    	DList aux2 = this.home;
			DNode aux;
			int  level = 1;  
			
			if(findFirsts() == false)
			{
				System.out.println("First element didn't found, we can't order the matrix!!!");
				return;
			}
			
			while( aux2 != null )
			{
				aux1 = this.home;
				while(aux1 != null)
				{
					if(aux1.getLevel() == level)	//If we find one element higher than level, find its sons and increment level... 
					{								//until reach the number of lines...
						aux = aux1.getHome(); 
						while( aux != null )
						{	
							if( aux.getNumber() > 3  && aux.getData().contains("@") == true )
								this.findDependencies(aux.getData(), level+1);
							
							aux = aux.getNext();
						 }	
					}	
					aux1 = aux1.getNext();
				}
				level++;
				aux2 = aux2.getNext();
	    	}	
		}
	    
	    //----------------------------------------------------------------------
	    // This method order the matrix lines using Quit Sort classification.... 
	    //----------------------------------------------------------------------
	    
	    public void quickSort()
	    {
	    	DList []stack = new DList[this.getEnd().getNumber()];
	    	
	    	DList lastElement;
	    	DList firstElement, aux;
	    	DList pivot;
	    	
	    	int top = 0;
	    	
	    	stack[top++] = this.getHome(); //add first element
	    	stack[top++] = this.getEnd();  //add last element	
	    		    	
	    	while ( top > 0 )
	    	{
	    		
	    		lastElement = stack[--top];
	    		firstElement = stack[--top];
	    		aux = firstElement;
	    		
	    		if(firstElement != lastElement)
	    		{		    		
		    		pivot = partition( firstElement, lastElement );
	    		
		    		if ( pivot ==  aux )
		    		{
		    			stack[top++] = pivot.getNext();
		    			stack[top++] = lastElement;
		    		}
		    		else
		    		{
		    			stack[top++] = pivot.getNext(); //Insert the first element on the stack
		    			stack[top++] = lastElement;		//Insert the last element on the stack
		    			
		    			stack[top++] = firstElement;		//Insert the first element on the stack
		    			stack[top++] = pivot.getPrevious();	//Insert the last element on the stack
		    		}

	    		}
	    	}
	    	alphabeticalOrder();  		//call order by name function
	    }
	    
	    //----------------------------------------------------------------------
	    // This method order the matrix levels alphabetically....
	    //----------------------------------------------------------------------
	    
	    public void alphabeticalOrder()
	    {
		 	DList aux1;
		 	DList aux2;
		 	int i = this.getEnd().getNumber();
		 	while(i != 0){
			 	aux1 = this.home;
			 	aux2 = aux1.getNext();
		 		while(aux2 != null){	
		 			if ( aux1.getLevel() == aux2.getLevel() && aux1.getEquipment().compareTo(aux2.getEquipment())>=0)
		 			{
		 				changeProperties ( aux1, aux2 );
		 			}
					aux1 = aux1.getNext();
					aux2 = aux2.getNext();
				}
		 		i--;
			}	    	
	    }
	    
	    //-----------------------------------------------------------------
	    //	This method interchange pointers links
	    //-----------------------------------------------------------------	
	    
	    public void changeProperties(DList first, DList last)
	    {	    	
	    	int level = first.getLevel();
	    	DNode home = first.getHome();
	    	DNode end = first.getEnd();

	    	first.setLevel(last.getLevel());
	    	first.setHome(last.getHome());
	    	first.setEnd(last.getEnd());
	    	
	    	last.setLevel(level);
	    	last.setHome(home);
	    	last.setEnd(end);
	    }
	    
	    //-----------------------------------------------------------------
	    //	This method separate part of a vector
	    //-----------------------------------------------------------------	 
	    
	    public DList partition(DList low, DList high)
	    {
	    	
	    	DList aux = new DList();   	
	    	
	    	aux = low;
	   
	    	while ( aux != high )
	    	{	    		
	    		if ( aux.getLevel() < high.getLevel() )
	    		{
	    			changeProperties ( low, aux );
	    			low = low.getNext();
	    		}
	    		aux = aux.getNext();
	    	}
	    	changeProperties( low, high );
	    	return low;
	    }    
	    
	    //-----------------------------------------------------------------
	    //This method propagate the fault status in the base of the matrix
	    //-----------------------------------------------------------------
	    public void spreadStatus(){
			DList aux1 = this.end;
			DNode aux2;
			byte degraded = 1;
			byte fault = 2;	
			byte hdegraded = 3;
			byte secure = 4;
			boolean s1;		//state 1 degraded
			boolean s2;		//state 2 fault
			boolean s3;		//state 3 high degraded
			byte nr; 		//number of redundancies
			int kden;		//criteria of redundancy (numeric value)
			int kdin;
			
			this.getRedundancies();
			this.getCriteria();
			
			while(aux1 != null)
			{
				aux2 = aux1.getEnd();
				nr = 1;
				s1 = false;
				s2 = false;
				s3 = false;
				
				kden = (int)Math.ceil((double)(aux1.getCriteria() * aux1.getRedundancy()));
				
				kdin = (int)Math.ceil((double)(aux1.getCriteria()* aux1.getRedundancy() / 2)) ; //total percent less criteria division by 2  
				
				while(aux2 != null){
					if(aux2.getStatus() == degraded)	//if the status is degrades, isn't matter how many exit, the last status is degrade. 
						s1 = true;
					else if(aux2.getStatus() == hdegraded) //if the status is high degrades, isn't matter how many exit, the last status is high degrade.
						s3 = true;
					else if(aux2.getStatus() == fault || aux2.getStatus() == secure)	//if the status is fault, we have to analyze if there are redundancies and if there are the same type. 	
					{
						if(aux2.getData().startsWith("R") && nr <= kden) //check if it's a redundancy and if there is higher of kden criteria... Suppose that we have only n dependencies 
						{
							if(nr <= kdin && kden == 1)
							{  
								s3 = true;
								nr++;
							}
							else if(nr <= kdin)
							{  
								s1 = true;
								nr++;
							}
							else
							{
								s3 = true;
								nr++;
							}
						}
						else
							s2 = true;
					}
						
					if(aux2.getNumber() == 3){
						if(s2 == true){
							aux2.setStatus(fault);
							markElement(aux2.getData(), fault, true, aux1.getNumber());
						}
						else if(s3 == true){
							aux2.setStatus(hdegraded);
							markElement(aux2.getData(), hdegraded, true, aux1.getNumber());
						}
						else if(s1 == true){
							aux2.setStatus(degraded);
							markElement(aux2.getData(), degraded, true, aux1.getNumber());	//search and mark (propagate) the next dependent element on the matrix.    
						}													//implement in the function, not to mark if the element already is fault.	
						
					}
					aux2 = aux2.getPrevious();
				}
				aux1 = aux1.getPrevious();
			}
		} 
	    
//------------------------------------------------------------------------------------------------
//	Methods to save the MD in excel file...
//------------------------------------------------------------------------------------------------  
	    
	    public void writeExcel() throws IOException, RowsExceededException, WriteException{
	    
	    	try{    	
	    		WritableWorkbook workbook = Workbook.createWorkbook(new File("mmd.xls")); 
		    	WritableSheet sheet = workbook.createSheet("First Sheet", 0);
		    	 	
		    	DList aux1 = this.home;
				DNode aux2;
				Label label = null;
				
				while(aux1 != null){
					aux2 = aux1.getHome(); 
					while(aux2 != null){		// Additional condition not to print the three first element... 
						
						//System.out.println(aux2.getNumber());
						
						if(aux2.getData().startsWith("!")==true) //Just no to print redundancy
							break;
						if(aux2.getStatus() == 0)
				        	label = new Label(aux2.getNumber(), aux1.getNumber(), aux2.getData()); 
						else if(aux2.getStatus() == 1)
				        	label = new Label(aux2.getNumber(), aux1.getNumber(), aux2.getData(), setCellFormat(Colour.YELLOW)); 
			        	else if(aux2.getStatus() == 2)
				        	label = new Label(aux2.getNumber(), aux1.getNumber(), aux2.getData(), setCellFormat(Colour.RED)); 
			        	else if(aux2.getStatus() == 3)
			        		label = new Label(aux2.getNumber(), aux1.getNumber(), aux2.getData(), setCellFormat(Colour.ORANGE));
			        	else if(aux2.getStatus() == 4)
			        		label = new Label(aux2.getNumber(), aux1.getNumber(), aux2.getData(), setCellFormat(Colour.GREEN));
			        	
			        	sheet.addCell(label);
			        	
			        	aux2 = aux2.getNext();
					}
					aux1 = aux1.getNext();
				}
		    	workbook.write();
		    	workbook.close();
	    	} catch (IOException e) {
			e.printStackTrace();
	    	}
	    }	

//------------------------------------------------------------------------------------------------
//
//------------------------------------------------------------------------------------------------	    
	    
	    
	    
//------------------------------------------------------------------------------------------------
// 	Methods to access the private variables...
//------------------------------------------------------------------------------------------------
	    
	    public DList getHome(){
	        return home;
	    }
	    public DList getEnd(){
	        return end;
	    }
	    public int getLine(){
	    	return line;
	    }
	    public void setLine(int line){
	    	this.line = line;
	    }
	    private static WritableCellFormat setCellFormat(Colour colour) throws WriteException {
	        WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
	        WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
	        cellFormat.setBackground(colour);
	        cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
	        return cellFormat;
	    }
	    
}
