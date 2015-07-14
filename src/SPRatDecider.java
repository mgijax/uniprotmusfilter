import org.jax.mgi.bio.seqrecord.*;
import org.jax.mgi.bio.seqfilter.*;
import java.util.*;

public class SPRatDecider extends SPSeqDecider
{
	//Concept:
        //        IS: an object that applies this predicate to a sequence record
	//	       'does this SwissProt/TREMBL sequence record represent  
	//		only one species and is that species rat? '
        //       HAS: A default name. see also superclass
        //      DOES: see superclass 
        // Implementation:

	//
	//constructors 
	//

	public SPRatDecider() 
		// Purpose: initialize "name" in superclass to "defaultName"
	{
		super(defaultName);
	}

	public SPRatDecider(String s)
		// Purpose: initialize "name" in superclass to 's'
	{
		super(s);
	}

	//
	//methods
	//
	
	public boolean isA(SeqRecord sr)
	{
		// Purpose: Decides if a SwissProt sequence record is *only*
		//           a rat record.
		//          Counts all records processed and all records
                //           for which this predicate is true
		// Returns: true if this predicate is true for 'sr'
		// Assumes: 'sr' is a SwissProt/TREMBL sequence record
		// Effects: nothing
		// Throws: nothing

		// initialize answer to the predicate to false
		boolean answer = false;

		// do we have more than one species? split on comma
                tokenizedOrganism = new StringTokenizer(sr.getOrganism(), ",");

		// apply the predicate - see "IS" in class concept
		if (tokenizedOrganism.countTokens() == 1 &&
				this.si.isOrganism(sr, "rat")) 
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
	private static String defaultName = "rat";

	// each token represents a species
        private StringTokenizer tokenizedOrganism;
}
