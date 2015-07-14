import org.jax.mgi.bio.seqrecord.*;
import org.jax.mgi.bio.seqfilter.*;

public class SPAllRodentDecider extends SPSeqDecider
{
	//Concept:
        //        IS: an object that applies this predicate to a sequence record
	//	      'does this SwissProt/TREMBL sequence record represent a
	//	       mouse or a rat? ' 
        //       HAS: A default name. see also superclass
        //      DOES: see superclass 
        // Implementation:

	//
	//constructors 
	//

	public SPAllRodentDecider()
		// Purpose: initialize "name" in superclass to "defaultName" 
	{
		super(defaultName);
	}

	public SPAllRodentDecider(String s)
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
		//          record. For now 'rodent' = mouse or rat.
		//	    Counts all records processed and all records
		//	    for which this predicate is true
		// Returns: true if this predicate is true for 'sr'
		// Assumes: 'sr' is a SwissProt/TREMBL sequence record
		// Effects: nothing
		// Throws: nothing
		// Notes:

		// initialize answer to the predicate to false
		boolean answer = false;
		
		if (this.si.isOrganism(sr, "mouse") 
			|| this.si.isOrganism(sr, "rat"))
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
	private static String defaultName = "allrodent";
}
