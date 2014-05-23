#!/bin/sh

SCRIPT_DIR=$(cd $(dirname "$0"); pwd)

. $SCRIPT_DIR/common.sh

export GRIDGAIN_HOME=/mnt/gridgain/ggprivate

"$JAVA" ${JVM_OPTS} -DGRIDGAIN_QUIET=false -DGRIDGAIN_DEBUG_ENABLED=true \
    -DGRIDGAIN_UPDATE_NOTIFIER=false -DGRIDGAIN_SCRIPT -DGRIDGAIN_HOME="${GRIDGAIN_HOME}" \
    -DGRIDGAIN_PROG_NAME="$0" ${JVM_XOPTS} -cp "${CP}" dashboard.Harness
