#!/bin/sh
#
# uniprotmusfilter.sh
#
###########################################################################
#
#  Purpose:  This script controls the execution of the
#            uniprot mouse sequence filter
#
#  Outputs:
#
#      - An archive file
#      - Log files defined by the environment variables ${LOG_PROC},
#        ${LOG_DIAG}, ${LOG_CUR} and ${LOG_VAL}
#      - Exceptions written to standard error
#      - Configuration and initialization errors are written to a log file
#        for the shell script
#
#  Exit Codes:
#
#      0:  Successful completion
#      1:  Fatal error occurred
#      2:  Non-fatal error occurred
#
#  Assumes:  Nothing
#
#  Implementation:  Description
#
#
#  Notes:  None
#
###########################################################################

cd `dirname $0`/..
CONFIG_LOAD=`pwd`/uniprotmusfilter.config

LOG=`pwd`/uniprotmusfilter.log
rm -f ${LOG}

USAGE='Usage: uniprotmusfilter.sh'

#
#  Verify the argument(s) to the shell script.
#
if [ $# -ne 0 ]
then
    echo ${Usage} | tee -a ${LOG}
    exit 1
fi

#
# verify & source the configuration file
#

if [ ! -r ${CONFIG_LOAD} ]
then
    echo "Cannot read configuration file: ${CONFIG_LOAD}"
    exit 1
fi

. ${CONFIG_LOAD}

#
#  Source the DLA library functions.
#
if [ "${DLAJOBSTREAMFUNC}" != "" ]
then
    if [ -r ${DLAJOBSTREAMFUNC} ]
    then
        . ${DLAJOBSTREAMFUNC}
    else
        echo "Cannot source DLA functions script: ${DLAJOBSTREAMFUNC}"
        exit 1
    fi
else
    echo "Environment variable DLAJOBSTREAMFUNC has not been defined."
fi

#
# createArchive including OUTPUTDIR, startLog, getConfigEnv
# sets "JOBKEY"
preload ${OUTPUTDIR}

#
# rm all files/dirs from OUTPUTDIR
#
cleanDir ${OUTPUTDIR}

#
# verify input files exist and are readable
#
if [ ! -r ${INPUT_FILE_SP} ]
then
    # set STAT for endJobStream.py
    STAT=1
    checkStatus ${STAT} "Cannot read from input file: ${INPUT_FILE_SP}"
fi

if [ ! -r ${INPUT_FILE_TR} ]
then
    # set STAT for endJobStream.py
    STAT=1
    checkStatus ${STAT} "Cannot read from input file: ${INPUT_FILE_TR}"
fi

# Get read lock on the input directory
echo "${MIRROR_LOCK} ${READ_LOCK} ${LOCKNAME} ${UNIPROTDIR}"
${MIRROR_LOCK} ${READ_LOCK} ${LOCKNAME} ${UNIPROTDIR}
STAT=$?
if [ $STAT -gt 0 ]
then
    checkStatus $STAT "There is a write lock on the input directory ${UNIPROTDIR}. ${UNIPROTMUSFILTER} exiting"
fi

#
# There should be a "lastrun" file in the input directory that was created
# the last time the load was run for this input file. If this file exists
# and is more recent than the input file, the load does not need to be run.
#
LASTRUN_FILE=${INPUTDIR}/lastrun
NEW_FILES=1
if [ -f ${LASTRUN_FILE} ]
then
    echo 'testing date of trembl file'
    if env test ${LASTRUN_FILE} -nt ${INPUT_FILE_TR} 
    then

        echo "${INPUT_FILE_TR}  has not been updated" | tee -a ${LOG_PROC}
	NEW_FILES=0
    fi
    if env test ${LASTRUN_FILE} -nt ${INPUT_FILE_SP}
    then

        echo "${INPUT_FILE_SP}  has not been updated" | tee -a ${LOG_PROC}
        NEW_FILES=0
    fi
    if [ ${NEW_FILES} -eq 1 ]
    then
        echo "Input files have not been updated - skipping load" | tee -a ${LOG_PROC}
        # unlock the input directory
        ${MIRROR_LOCK} ${UNLOCK} ${LOCKNAME} ${UNIPROTDIR}

        # set STAT for shutdown
        STAT=0
        echo 'shutting down'
        shutDown
        exit 0
    fi
fi

#
# Function that runs the java load
#

run ()
{
    #
    # log time and input files to process
    #
    echo "" >> ${LOG_DIAG} 
    echo "`date`" >> ${LOG_DIAG} 
    echo "Files from stdin: ${INPUT_FILE_SP} ${INPUT_FILE_TR}" >> ${LOG_DIAG}

    #
    # run uniprot mus filter
    #
    gunzip -c ${INPUT_FILE_SP} ${INPUT_FILE_TR} \
        | java -DLOG=${LOG_PROC} SPOrganismFilter \
        --mouse -o ${SPTR_MOUSE_DAT}
    STAT=$?
    checkStatus ${STAT} ${UNIPROTMUSFILTER} ${CONFIG_LOAD}}
}
##################################################################
##################################################################
#
# main
#
##################################################################
##################################################################


# run the filter
run

#
# unlock the input directory
#
${MIRROR_LOCK} ${UNLOCK} ${LOCKNAME} ${UNIPROTDIR}

#
# Touch the "lastrun" file to note when the load was run.
#
if [ ${STAT} = 0 ]
then
    touch ${LASTRUN_FILE}
fi

#
# run postload cleanup and email logs
#
shutDown

exit 0
