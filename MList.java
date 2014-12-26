package mpm;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

public class MList {
	
	private MNode home = null;
    private MNode end = null;
    
    private int number;
    
    private MList next;
    private MList previous;
  
    
    //------------------------------------------------------------------------------------------------
    // 	Constructors
    //------------------------------------------------------------------------------------------------
    
	public MList(){}
	
	
	public MList(int number){
		this.number = number;
	}
	
	//public MList(int number, String data){
	//	this.insertNode(number, data);
	//}
	
    
    //------------------------------------------------------------------------------------------------
    // Methods
    //------------------------------------------------------------------------------------------------
    
    //------------------------------------------------------------------------------------------------
    // Method that insert a information (String) in the end of the linked list
    //------------------------------------------------------------------------------------------------
 
    public void insertNode(int number, String data, float value){
    	
    	MNode aux = new MNode(number, data, value);
        
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
    
    public void print(){
    	
        MNode aux = this.home;

        while(aux != null){
        	System.out.println(aux.getData()+" ");
            aux = aux.getNext();
        }
    }
    
    //----------------------------------------------------------------------------------------------------
    // Method who read the contend of the file (directory received like parameter) and put it into a list. 
    // The contend of the file must be separated by LINES.
    //----------------------------------------------------------------------------------------------------
    
    public void readFile(Path directory){
    	
    	Charset utf8 = StandardCharsets.UTF_8;	//import a class...
    
    	int j=0;
    	
    	try(BufferedReader reader = Files.newBufferedReader(directory, utf8)){			
		
    		String line;
    		StringTokenizer st;
    
    		while((line = reader.readLine()) != null){
    			st = new StringTokenizer(line);
    			while(st.hasMoreTokens() == true){
    				this.insertNode(j, st.nextToken(),0);
    			}
    			j++;
    		}	
		}catch(IOException e){
			e.printStackTrace();
		}  	
    }
    
    //------------------------------------------------------------------------------------------------
    //	Basics methods(to access a private information)
    //------------------------------------------------------------------------------------------------
    
    public void setNumber(int number){
    	this.number = number;
    }
    public void setNext(MList next){
    	this.next = next;
    }
    public void setPrevious(MList previous){
    	this.previous = previous;
    }
    public void setHome(MNode home)
    {
    	this.home = home;
    }
    public void setEnd(MNode end)
    {
    	this.end = end;
    }
    public int getNumber(){
    	 return number;
    }
    public MList getNext(){
    	return next;
    }
    public MList getPrevious(){
    	return previous;
    }
    public MNode getHome(){
    	return home;
    }
    public MNode getEnd(){
    	return end;
    }
	public String getSystem()
	{
		return this.getHome().getData();
	}
	public String getEquipment()
	{
		return this.getHome().getNext().getNext().getNext().getData();	
	}
	public MNode getFirstDependencie()
	{
		return this.getHome().getNext().getNext().getNext().getNext();	
	}

}
