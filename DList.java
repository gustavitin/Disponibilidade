package mmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

import mmd.DNode;

public class DList {

	private DNode home = null;
    private DNode end = null;
    
    private int number;
    private int nd, ni;				//nd (dependencies), ni (influence)
    private double criteria;		//Element that receive the criteria of redundancies analysis in %, kden...
    private int redundancy;			//Number of redundancies.
    private int level;				//Element that contend the level of the line, use to order it.
    private boolean mark = false;
    
    private DList next;
    private DList previous;
  
    
    //------------------------------------------------------------------------------------------------
    // 	Constructors
    //------------------------------------------------------------------------------------------------
    
	public DList(){}
	
	
	public DList(int number){
		this.number = number;
		this.setLevel(0);
	}
	
	public DList(int number, String data){
		this.insertNode(number, data);
		this.setLevel(0);
	}
	
    
    //------------------------------------------------------------------------------------------------
    // Methods
    //------------------------------------------------------------------------------------------------
    
    //------------------------------------------------------------------------------------------------
    // Method that insert a information (String) in the end of the linked list
    //------------------------------------------------------------------------------------------------
 
    public void insertNode(int number, String data){
    	
    	DNode aux = new DNode(number, data);
        
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
    // Method that insert information (String) on the beginning of the linked list.
    //------------------------------------------------------------------------------------------------
    
    //------------------------------------------------------------------------------------------------
    // Method that print contend of the linked list.
    //------------------------------------------------------------------------------------------------
    public void print(){
    	
        DNode aux = this.home;

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
    	
    	try(BufferedReader reader = Files.newBufferedReader(directory, utf8)){			
		
    		String line;
    		StringTokenizer st;
    		int j=0;

    		while((line = reader.readLine()) != null){
    			st = new StringTokenizer(line);
    			while(st.hasMoreTokens() == true){
    				this.insertNode(j, st.nextToken());
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
    public void setNext(DList next){
    	this.next = next;
    }
    public void setPrevious(DList previous){
    	this.previous = previous;
    }
    public void setHome(DNode home)
    {
    	this.home = home;
    }
    public void setEnd(DNode end)
    {
    	this.end = end;
    }
    public int getNumber(){
    	 return number;
    }
    public DList getNext(){
    	return next;
    }
    public DList getPrevious(){
    	return previous;
    }
    public DNode getHome(){
    	return home;
    }
    public DNode getEnd(){
    	return end;
    }
    
    public void setLevel(int level){
    	this.level = level;
    }

    public int getLevel(){
    	return level;
    }

	public void setCriteria(double criteria){
		this.criteria = criteria;
	}
	public void setRedundancy(int redundancy){
		this.redundancy = redundancy;
	}
	public double getCriteria(){
		return criteria;
	}
	public int getRedundancy(){
		return redundancy;
	}
	public void setMark(boolean mark){
		this.mark = mark;
	}
	public boolean getMark(){
		return mark;	
	}
	public String getSystem()
	{
		return this.getHome().getData();
	}
	public String getEquipment()
	{
		return this.getHome().getNext().getNext().getNext().getData();	
	}
	public DNode getFirstDependencie()
	{
		return this.getHome().getNext().getNext().getNext().getNext();	
	}
    public void setNd(int nd){
    	this.nd = nd;
    }
    public void setNi(int ni){
    	this.ni = ni;
    }
    public int getNd(){
    	return nd;
    }
    public int getNi(){
    	return ni;
    }
}
 