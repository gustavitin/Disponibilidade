package mpm;

public class MMatrix {
	
	private MList home = null;
	private MList end = null;
	
	private int line;
	
	//------------------------------------------------------------------------------------------------
    // 	Constructors
    //------------------------------------------------------------------------------------------------
	
	public MMatrix( int number )
	{	
  		for(int i=0; i<number; i++)
  		{
  			this.insertMList(i);
  			for(int j=0; j<number; j++)
  				this.getEnd().insertNode(j, null, 0);
  		}
	}
	
	//------------------------------------------------------------------------------
	//This method insert a new list with the parameter number to enumerate the lines
	//------------------------------------------------------------------------------
    
	public void insertMList(int number){
    	
    	MList aux = new MList(number);
    	
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
	
    //------------------------------------------------------------------------------------------------
    // Method that print contend of the linked list.
    //------------------------------------------------------------------------------------------------
    
    public void print(MMatrix matrix)
    {	
    	MList aux1 = matrix.home;
    	MNode aux2;
        
    	while(aux1 != null)
        {
    		System.out.println();
    		System.out.print(aux1.getNumber()+" ");
    		aux2 = aux1.getHome();
    		while(aux2 != null)
        	{
        		System.out.print(aux2.getAvailability()+" "); 
        		//System.out.print(aux2.getData()+" ");
        		aux2 = aux2.getNext();
        	}
            aux1 = aux1.getNext();
        }
    }
  	
	//------------------------------------------------------------------------------------------------
	// 	Methods to access the private variables...
	//------------------------------------------------------------------------------------------------
	    
	public MList getHome(){
		return home;
	}
	    
	public MList getEnd(){
		return end;
	}
	    
	public int getLine(){
		return line;   
	}
	
	public void setLine(int line){
		this.line = line;
	}
		
}
