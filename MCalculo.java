package mpm;

import java.nio.file.Path;
import mmd.DMatrix;
import mmd.DList;
import mmd.DNode;

public class MCalculo {
	
    //------------------------------------------------------------------------------------------------
    // 	Constructors
    //------------------------------------------------------------------------------------------------
    
	public MCalculo(){}
	
    //------------------------------------------------------------------------------------------------
    // Method to construct the serial transition matrix
    //------------------------------------------------------------------------------------------------
	
	public MMatrix SerialMatrixCreate(DMatrix dmatrix, MMatrix smatrix, MVector mvector){
		
		DList d_aux1 = dmatrix.getHome();
  		DNode d_aux2;
  		
  		MList m_aux1 = smatrix.getHome();
  		MNode m_aux2;
  		
  		MNode v_aux2;
  		
  		while(d_aux1 != null)		// fill serial element present on the original matrix
  		{
  			d_aux2 = d_aux1.getFirstDependencie();
  			
  			while( d_aux2 != null )
  			{	
  				m_aux2 = m_aux1.getHome();
  	  			v_aux2 = mvector.getHome();
  	  			
  				while( m_aux2 != null )
  				{	
  					if( d_aux2.getData().endsWith(v_aux2.getData()) == true &&  IsRedundancy(d_aux2) == false)
  					{
  	  					m_aux2.setAvailability(1);
  	  					break;
  					}
  					v_aux2 = v_aux2.getNext();	
  					m_aux2 = m_aux2.getNext();
  				}	
  				d_aux2 = d_aux2.getNext();
  			}
  			d_aux1 = d_aux1.getNext();
  			m_aux1 = m_aux1.getNext();		
  		}
  		
		while( m_aux1 != null )		// fill serial element associated to the CBNs
		{
			m_aux2 = m_aux1.getHome();
			while( m_aux2 != null)
			{
				if( m_aux2.getNumber() == m_aux1.getNumber())
					m_aux2.setAvailability(1);
				
				m_aux2 = m_aux2.getNext();
			}
			m_aux1 = m_aux1.getNext();
		}
		return smatrix;
	}
	

    //------------------------------------------------------------------------------------------------
    // Method to construct the parallel transition matrix
    //------------------------------------------------------------------------------------------------
	
	public MMatrix ParallelMatrixCreate(DMatrix dmatrix, MMatrix pmatrix, MVector mvector){
		
		DList d_aux1 = dmatrix.getHome();
  		DNode d_aux2;
  		
  		MList m_aux1 = pmatrix.getHome();
  		MNode m_aux2;
  		
  		MNode v_aux2;
  		
  		while(d_aux1 != null)		// fill parallel element present on the original matrix
  		{
  			d_aux2 = d_aux1.getFirstDependencie();
  			
  			while( d_aux2 != null )
  			{	
  				v_aux2 = mvector.getHome();
  				m_aux2 = m_aux1.getHome();
  				
  				while( m_aux2 != null )
  				{
  					if( d_aux2.getData().endsWith(v_aux2.getData()) == true && IsRedundancy(d_aux2) == true )
  					{
  						m_aux2.setAvailability((float)1/d_aux1.getRedundancy());
  						break;
  					}
  					
  					m_aux2 = m_aux2.getNext();
  					v_aux2 = v_aux2.getNext();
  				}	
  				d_aux2 = d_aux2.getNext();
  			}
  			d_aux1 = d_aux1.getNext();
  			m_aux1 = m_aux1.getNext();		
  		}
  		
		return pmatrix;
	}

    //------------------------------------------------------------------------------------------------
    // Method to determinate if the element is a dependency matrix  
    //------------------------------------------------------------------------------------------------
	
	public boolean IsRedundancy(DNode aux)
	{
		if(aux.getData().startsWith("R") == true && aux.getData().substring(2).startsWith(":") == true)
		{
			return true;
		}
		else
			return false;
	}
		
	//------------------------------------------------------------------------------------------------
	// 	Combination of serial and parallel matrix with the vector
	//------------------------------------------------------------------------------------------------
	
	public MVector MatrixAplication( DMatrix dmatrix, MMatrix smatrix, MMatrix pmatrix, Path path )
	{
		
		MVector mvector1 = new MVector(dmatrix, 1);
		MVector mvector2 = new MVector(dmatrix, 1);
		
		mvector1.faultElements(path);
		mvector2.faultElements(path);
		
		boolean flat;		// Variable used two times
		
		float product, min, value;
		
		MList s_aux1;		//  indicate the index line of serial matrix
		MNode s_aux2;		//  indicate the index column of serial matrix
		MNode v_aux1;		//  indicate Ri (use to save the result)
		MNode v_aux2;		// 	indicate the index of the vector 
		
		do
		{	
			s_aux1 = smatrix.getEnd();
			v_aux1 = mvector1.getEnd();
			
			while( s_aux1 != null )			//Start the Serial Matrix application
			{
				s_aux2 = s_aux1.getHome();
				v_aux2 = mvector1.getHome();
				min = 1;
				product = 1;
				while( s_aux2 != null )
				{
					if( s_aux2.getAvailability() > 0 )
					{
						product = product * s_aux2.getAvailability();
						
						if( v_aux2.getAvailability() < min )
							min = v_aux2.getAvailability();
						
					}	
						s_aux2 = s_aux2.getNext();
						v_aux2 = v_aux2.getNext();
				}
				
				value = min/product;	// never have division by zero...
				
				//System.out.println(v_aux1.getNumber()+" ");
				
				if(value < v_aux1.getAvailability())		// We can talk less than because serial influence can have an availability higher
					v_aux1.setAvailability(value);
				
				v_aux1 = v_aux1.getPrevious();
				s_aux1 = s_aux1.getPrevious();
			}
			//mvector1.print();
			
			MList p_aux1 = pmatrix.getEnd();
			v_aux1 = mvector1.getEnd();
			MNode p_aux2;
			float sum;
			
			while( p_aux1 != null )			//Start the Parallel Matrix application
			{
				p_aux2 = p_aux1.getHome();
				v_aux2 = mvector1.getHome();
				sum = 0;
				flat = false;
				while( p_aux2 != null )
				{
					if( p_aux2.getAvailability() > 0 )		//	It mean that have a redundant element, where Availability = 1 / Number of redundancies    
					{
						sum = sum + p_aux2.getAvailability() * v_aux2.getAvailability();
						flat = true;						// Use the flat to know if we have to change the Availability value
					}
							
					p_aux2 = p_aux2.getNext();
					v_aux2 = v_aux2.getNext();
				}
				
				if( sum < v_aux1.getCriteria() )  // The last step implemented, used to set availability 0 when the criteria line was overcome... 
					sum = 0;
					
				if( sum < v_aux1.getAvailability() && flat == true) 
					v_aux1.setAvailability(sum);
				
				v_aux1 = v_aux1.getPrevious();
				p_aux1 = p_aux1.getPrevious();
			}
			
			//mvector1.print();
			//mvector2.print();
			
			v_aux1 = mvector1.getHome();
			v_aux2 = mvector2.getHome();
			
			flat = true;
			while(v_aux2 != null)				// Check convergence of the matrices application
			{
				if( v_aux1.getAvailability() != v_aux2.getAvailability() )
				{
					flat = false;
				}
				v_aux2.setAvailability(v_aux1.getAvailability()); 	//Copy the availability to the first element ...
				v_aux1 = v_aux1.getNext();
				v_aux2 = v_aux2.getNext();
			}
		}
		while(flat == false);
		//mvector1.print();
		//mvector2.print();
		return mvector1;
	}
	
	
	//------------------------------------------------------------------------------------------------
	// 	Methods to access the private variables...
	//------------------------------------------------------------------------------------------------
	
}

