import org.jax.mgi.bio.seqrecord.*;
import org.jax.mgi.bio.seqfilter.*;

public class SPOrganismFilter
{
	// Program: SPOrganismFilter
        // Purpose: Filters mouse, rat, human, and rodent
        //              sequence records from SwissProt and TREMBL sequence
        //              record files and writes them to corresponding output
        //              files; writes record counts to a log file
        // User Requirements Satisfied by This Program: See the Requirements/
        //                      Design document in system_docs/
        // System Requirements Satisfied by This Program: See the Requirements/
        //                      Design document in system_docs/
        // Usage: "inputFileName" | java -DLOG="LOGVALUE" SPOrganismFilter
        //                      --"decider" -a "deciderOutputFile"
        //                      ... 2 to 4 more "decider"/"deciderOutputFile"
	//	                    pairs
        //      where:
        //           -) "inputFileName" = the SwissProt/TREMBL flat file to be
        //                 piped, "|", to this application
        //           -) "-D" = assign a Java system property
        //           -) "LOG" = name of the Java system property to create
        //           -) "LOGVALUE" =  abs path to log file for this filter run
	//               to set the Java system property "LOG"
        //           -) "SPOrganismFilter" = the name of this application
        //           -) "--decider" = This application
        //                 supports (--mouse | --rat | --human | --rodent)
        //           -) -o = open the argument to this option in overwrite mode
        //              -a = open the argument to this option in append mode
        //           -) "deciderOutputFile" = the output file name for the
        //                preceding decider on the command line
        //           -) May have as many decider/output file pairs on the
        //                command line as deciders supported by this filter
        // Envvars: -) ${LOG_DIR} = the log path for this application
        // Inputs:  -) input file must be piped to stdin
        //          -) log file path comes from java system properties via
        //              the environment
        // Outputs: -) log file reports counts of total records and number
        //              of records per decider processed, total runtime of
        //              the application
        //          -) One "deciderOutputFile" for each decider on the
        //                   command line
        // Exit Codes: none
	//	 Note: All exceptions are handled in the SeqRecordFilter
        //              object "f"
        // Assumes: -) input file is a EMBL-format sequence record file
        //          -) command line is in decider/outputfileName pairs
        // Implementation: See the Requirements/design document in system_docs
        //       Modules:
        //              -) Package: org.jax.mgi.seqfilter
        //              -) Package: org.jax.mgi.seqrecord
        //  Dependencies: See the DEPENDENCIES file
        //              -) Class: gnu.getopt.Getopt
        //

	public static void main(String[] args)
	{
		// An array of predicate objects one each of all
                // predicates supported by this filter.
		SeqDecider[] deciders = {
        	    new SPMouseDecider( ),
                    new SPRatDecider( ),
                    new SPHumanDecider( ),
                    new SPRodentDecider( ),
		    new SPAllRodentDecider( ),
		    new SPNonHumanMouseRatDecider()};

		// A sequence record object for reading/parsing
                // EMBL-format sequence records
		SeqRecord sr = new EMBLSeqRecord ( );

		// A generic filter object for filtering sequence records
		SeqRecordFilter f = new SeqRecordFilter(
		    deciders, 	// The SwissProt/TREMBL predicate objects
		    args, 	// Command line arguments
		    sr);	// The EMBL-format sequence record object

		// parse args, read and filter records, log
		f.go();
	}

}
