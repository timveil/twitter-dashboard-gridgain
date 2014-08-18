#!/bin/bash

SCRIPT_DIR=$(cd $(dirname "$0"); pwd)
HOSTNAME=`hostname`
IDX=$1

export GRIDGAIN_HOME=/mnt/gridgain/ggprivate

nohup /bin/bash $SCRIPT_DIR/start-node.sh < /dev/null > $SCRIPT_DIR/../logs/gridgain-$HOSTNAME-$IDX.log 2>&1 &

sleep 1
