#!/bin/sh


# # Import common GridGain functions.
# 
. "${GRIDGAIN_HOME}"/os/bin/include/functions.sh


# # Check JAVA_HOME and version
# 
checkJava


# # Check GRIDGAIN_HOME
# 
if [ "$GRIDGAIN_HOME" = "" ]; then
    echo $0", ERROR: GRIDGAIN_HOME environment variable is not found."

    exit 1
fi


# # Set necessary environment variables
# 
unset GRIDGAIN_LIBS

. "${GRIDGAIN_HOME}"/os/bin/include/setenv.sh


# # Set Classpath: includes GridGain and POC dependencies
# 
TMP_PROJECT_HOME="$(dirname "$(cd "$(dirname "$0")"; "pwd")")";

CP="${GRIDGAIN_LIBS}":${TMP_PROJECT_HOME}/*:${TMP_PROJECT_HOME}/lib/*


# # JVM options. See http://java.sun.com/javase/technologies/hotspot/vmoptions.jsp for more details.
# 
JVM_OPTS="-Xms10g -Xmx10g -Djava.net.preferIPv4Stack=true -XX:NewSize=64m -XX:MaxNewSize=64m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+UseTLAB -XX:+CMSClassUnloadingEnabled -XX:MaxTenuringThreshold=0 -XX:SurvivorRatio=1024 -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=60"


# # Call Test Harness
# 
echo " "
echo ">>> +---------------------------------------------------------------------------------+"
echo -e ">>> TMP_PROJECT_HOME: ${TMP_PROJECT_HOME}"
echo -e ">>> CP:  ${CP}"
echo -e ">>> JVM_OPTS: ${JVM_OPTS}"
echo ">>> +---------------------------------------------------------------------------------+"
echo " "
