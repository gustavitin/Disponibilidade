package mpm;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

import mmd.DMatrix;
import mmd.DList;
import mmd.DNode;


public class MVector {
	
	private MNode home = null;
    private MNode end = null;
    
    private int i = 0;			//have array dimension

    //------------------------------------------------------------------------------------------------
    // 	Constructors
    //------------------------------------------------------------------------------------------------
    
    public MVector(){}
    
    
	public MVector(DMatrix matrix, float value)  // built a vector from the addition of CANs plus CBNs before 
	{
		DList aux1 = matrix.getHome();	// Maybe in some moment we have to check the vector before insert a new element, because can be CBNs that influence more than one CAN... 
	  	DNode aux2;
	  		 	
	  	aux1 = matrix.getHome();
	  	
	 	while(aux1 != null) 	//incorporate the CANs to the vector
	 	{		
	  		aux2 = aux1.getHome();
	  		while(aux2 != null)
	  		{
	  			if( aux2.getNumber() == 3 )//&& aux2.getData().contains("@") == true && aux2.getData().contains("!") == false)	// the last condition is only for testing
	  			{
	  				this.insertNode(i, aux2.getData(), value, (float)aux1.getCriteria()); 
	  				i++;
	  			}
	  			aux2 = aux2.getNext();
	  		}
	  		aux1 = aux1.getNext();
	  	}
	  	aux1 = matrix.getHome();
	 	while(aux1 != null) 	//incorporate the CBN to the vector
	 	{		
	  		aux2 = aux1.getHome();
	  		while(aux2 != null)
	  		{
	  			if( aux2.getNumber() > 3 && aux2.getData().contains("@") == false && aux2.getData().contains("!") == false)	// the last condition is only for testing
	  			{
	  				this.insertNode(i, aux2.getData(), value, 0);
	  				i++;
	  			}
	  			aux2 = aux2.getNext();
	  		}
	  		aux1 = aux1.getNext();
	  	}

	}
	
	
    //------------------------------------------------------------------------------------------------
    // Method that insert a information (String) in the end of the linked list
    //------------------------------------------------------------------------------------------------
	
    public void insertNode(int number, String data, float value, float c)
    {	
    	MNode aux = new MNode(number, data, value, c);
        
        if(home == null)
        {
        	this.home = aux;
        	this.end = aux;
        }
        else
        {
        	end.setNext(aux);
        	aux.setPrevious(end);
            this.end = aux;
        }
    }	
    
    //------------------------------------------------------------------------------------------------
    // Method that print contend of the linked list.
    //------------------------------------------------------------------------------------------------
    
    public void print()
    {	
        MNode aux = this.home;
        System.out.println();
        while(aux != null)
        {
        	System.out.print(aux.getNumber()+1+" ");
        	System.out.println(aux.getData()+" "+aux.getAvailability()+"");
            aux = aux.getNext();
        }
    }
    

    //------------------------------------------------------
    //	This method read the file with the elements fault
    //------------------------------------------------------
  	
    public void faultElements(Path directory){
  		
    	StringTokenizer st; //Receive the partitioned element when the string is been read.
    	Charset utf8 = StandardCharsets.UTF_8;	//import a class...
  	    	
  	   	try(BufferedReader reader = Files.newBufferedReader(directory, utf8)){			
  			
  	    	String line;
  				
  	    	while((line = reader.readLine()) != null){
  	    		st = new StringTokenizer(line);
  	    		while(st.hasMoreTokens() == true){			
  	    			this.markElement(st.nextToken(), st.nextToken());
  	   			}
  			}	
  		}catch(IOException e){
			e.printStackTrace();
  		}  	  
  	}
    
	//---------------------------------------------------------------------
	// This method mark the fault element in the matrix already organized
	//---------------------------------------------------------------------
    
	public void markElement(String element, String value){
					
		MNode aux2 = this.getHome();
		float availability;
		
		availability = Float.parseFloat(value);
		 
		while(aux2 != null)
		{
			if( aux2.getData().endsWith(element) == true ){	   
				aux2.setAvailability(availability);
			}	
			aux2 = aux2.getNext();
		}
	}	
	
	//------------------------------------------------------------------------------------------------
	// 	Methods to access the private variables...
	//------------------------------------------------------------------------------------------------
	    
	public MNode getHome()
	{
		return home;
	}    
	public MNode getEnd()
	{
		return end;
	}
	public int getDimension()
	{
		return i;
	}
}
