import org.jax.mgi.bio.seqrecord.*;
import org.jax.mgi.bio.seqfilter.*;
import java.util.*;

public class SPRodentDecider extends SPSeqDecider
{
	//Concept:
        //        IS: an object that applies this predicate to a sequence record
	//	      'does this SwissProt/TREMBL sequence record represent 
	//	       rodent, but not just mouse and just rat? ' 
        //       HAS: A default name. see also superclass
        //      DOES: see superclass 
        // Implementation:

	//
	//constructors 
	//

	public SPRodentDecider()
		// Purpose: initialize "name" in superclass to "defaultName" 
	{
		super(defaultName);
	}

	public SPRodentDecider(String s)
		// Purpose: initialize "name" in superclass to 's'
	{
		super(s);
	}

	//
	//methods
	//

	public boolean isA(SeqRecord sr)
	{
		// Purpose: Decides if a SwissProt sequence record is a rodent 
		//          record - a rodent, but not just a mouse and not
		//	    just a rat.
		//	    Counts all records processed and all records
		//	    for which this predicate is true
		// Returns: true if this predicate is true for 'sr'
		// Assumes: 'sr' is a SwissProt/TREMBL sequence record
		// Effects: nothing
		// Throws: nothing
		// Notes:

		// initialize answer to the predicate to false
		boolean answer = false;
		
		// do we have more than one species? split on comma
                tokenizedOrganism = new StringTokenizer(sr.getOrganism(), ",");

		// if only one species, is rodent, but not (mouse or rat) OR 
		// (this is euristic) multi species and one of those species is 
		// mouse or rat. Note: if multi species, the classification
		// is for the first listed species only, so 
		// the case of multi species, not(mouse or rat), and second
		// species being rodent cannot be filtered for unless specify
		// each individual rodent species. 

		if ((tokenizedOrganism.countTokens() == 1 && 
			!(this.si.isOrganism(sr, "mouse") 
				|| this.si.isOrganism(sr, "rat")) &&
			this.si.isOrganismClassif(sr, "rodent")) ||
			(tokenizedOrganism.countTokens() > 1 && 
			(this.si.isOrganism(sr, "mouse")
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
	private static String defaultName = "rodent";

	// each token represents a species
        private StringTokenizer tokenizedOrganism;
}
