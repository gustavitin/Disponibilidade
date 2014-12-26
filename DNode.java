package mmd;

public class DNode {
	
	private int number;
	private String data;
    private byte status;	//Status can be:	0 - Working
    						//				  	1 - Degraded
    						//				  	2 - Failure

    private DNode next; 	//Initialized null.
    private DNode previous;	//Initialized null.
    
    //------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------
    
    public DNode(){}
    
    public DNode(int number, String data){    	
    	this.number = number;
    	this.data = data;
        status = 0;
    	next = null;
        previous = null;
     
    }
    
    
    //------------------------------------------------------------------------------------------------
    // Basic methods to access private information(data, status, next, previous)
    //------------------------------------------------------------------------------------------------

    public void setData(String data){    	
        this.data = data;
    }
    
    public void setStatus(byte status){        
    	this.status = status;
    }   
    
    public void setNext(DNode next){
    	this.next = next;
    }

    public void setPrevious(DNode previous){
        this.previous = previous;
    }
    
    //public void setPrevious(int number){
    //    this.number = number;
    //}
     
    public String getData(){    	
        return data;
    }    
    
    public byte getStatus(){    	
        return status;
    }   
    
    public DNode getNext(){    	
        return next;
    }

    public DNode getPrevious(){    	
        return previous;
    }
    
    public int getNumber(){    	
        return number;
    }
}
