#!/bin/bash

SCRIPT_DIR=$(cd $(dirname "$0"); pwd)

for i in `seq 1 1 $1`; do
    $SCRIPT_DIR/start-daemon-node.sh $i
    echo started node $i
done

sleep 1
