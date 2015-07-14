import org.jax.mgi.bio.seqrecord.*;
import org.jax.mgi.bio.seqfilter.*;
import java.util.*;

public class SPNonHumanMouseRatDecider extends SPSeqDecider
{
	//Concept:
        //        IS: an object that applies this predicate to a sequence record
	//	       'does this SwissProt/TREMBL sequence record represent 
	//	        a single species and is that species any species other 
	//		than human, mouse, or rat? ' 
        //       HAS: A default name. see also superclass
        //      DOES: see superclass 
        // Implementation:

	//
	//constructors 
	//

	public SPNonHumanMouseRatDecider()
		// Purpose: initialize "name" in superclass to "defaultName" 
	{
		super(defaultName);
	}

	public SPNonHumanMouseRatDecider(String s)
		// Purpose: initialize "name" in superclass to 's'
	{
		super(s);
	}

	//
	//methods
	//

	public boolean isA(SeqRecord sr)
	{
		// Purpose: Decides if a SwissProt sequence record is for a 
		//           single species and is not for human, mouse, or rat 
		//	    Counts all records processed and all records
		//	     for which this predicate is true
		// Returns: true if this predicate is true for 'sr'
		// Assumes: 'sr' is a SwissProt/TREMBL sequence record
		// Effects: nothing
		// Throws: nothing
		// Notes:

		// initialize answer to the predicate to false
		boolean answer = false;
		
		// do we have more than one species? split on comma
                tokenizedOrganism = new StringTokenizer(sr.getOrganism(), ",");

		// 
		if(!(tokenizedOrganism.countTokens() == 1 && 
			(this.si.isOrganism(sr, "human") 
				|| this.si.isOrganism(sr, "mouse")
				|| this.si.isOrganism(sr, "rat"))))
                {
			// if true increment counter for true predicates
                        // and set answer to true.
                        this.incrementTrueCtr();
                        answer = true;
                }
		
		// increment counter for total number of records processed.
		this.incrementAllCtr();

		return answer;
	}
	
	// default name for this decider
	private static String defaultName = "nonhumanmouserat";

	// each token represents a species
        private StringTokenizer tokenizedOrganism;
}
