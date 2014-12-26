package mpm;

public class MNode {
	
	private int number;
	private String data;

	private float available;
	private float criteria;
	
	private MNode next; 		//Initialized null.
    private MNode previous;	//Initialized null.
    
    //------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------
    
    public MNode(){}
    
    public MNode(int number){
    	this.number = number;
    }
    
    public MNode(int number, String data, float value){    	
     	this.number = number;
    	this.data = data;
        available = value;
    	next = null;
        previous = null;
    }
    
    public MNode(int number, String data, float value, float c){    	
     	this.number = number;
    	this.data = data;
        available = value;
        criteria = c;
    	next = null;
        previous = null;
    }
    
    
    //------------------------------------------------------------------------------------------------
    // Basic methods to access private information(data, status, next, previous)
    //------------------------------------------------------------------------------------------------

    public void setData(String data){    	
        this.data = data;
    }
    
    public void setAvailability(float available){        
    	this.available = available;
    }
    public void setCriteria(float criteria){        
    	this.criteria = criteria;
    }
    public float getCriteria(){        
    	return criteria;
    }
    
    public void setNext(MNode next){
    	this.next = next;
    }

    public void setPrevious(MNode previous){
        this.previous = previous;
    }
    
    public void setPrevious(int number){
        this.number = number;
    }
     
    public String getData(){    	
        return data;
    }    
    
    public float getAvailability(){    	
        return available;
    }   
    
    public MNode getNext(){    	
        return next;
    }

    public MNode getPrevious(){    	
        return previous;
    }
    
    public int getNumber(){    	
        return number;
    }
}

