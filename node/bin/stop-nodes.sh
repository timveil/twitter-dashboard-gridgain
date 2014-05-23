#!/bin/bash

echo Stop all GridGain nodes.

SCRIPT_DIR=$(cd $(dirname "$0"); pwd)

PATH=$PATH:$JAVA_HOME/bin

for CLS in 'Harness' ; do
    for X in `sudo jps | grep -i -G $CLS | awk {'print $1'}`; do
        sudo kill $@ $X
    done
done

sleep 1

echo "Rest java processes (ps -A | grep java):"
ps -A | grep java

# Force exit code 0.
echo "Done."
