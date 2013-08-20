#!/bin/zsh

set -e

THREADS=4
RUNS=10
FILE=dsl.reactive.BenchmarkRunner

if [[ -z "$JAVA_HOME" ]]; then
    echo Setting JAVA_HOME >&2
    export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
fi

export SCALA_VIRT_HOME=$HOME/.sbt/0.12.0-Beta2/boot/scala-2.10.0-M1-virtualized/lib/

main() {
    bin/delitec $FILE &> /dev/null
    if [[ "0" == "$?" ]]; then
        echo Generation successful >&2
    else
        echo Generation failed >&2
        exit
    fi

    echo Running ${FILE}: $RUNS times, with $THREADS threads >&2
    OUTPUT=$(bin/delite -t $THREADS --runs=$RUNS $FILE 1>/dev/null 2>&1 | filter)

    echo "start,setup,1st,2nd,end,setup-time,first-propagation,second-propagation,third-propagation,total-time"

    echo "$OUTPUT" | awk -F, \
        '{printf("%s,%s,%s,%s,%s,%s\n", $0, $2-$1, $3-$2, $4-$3, $5-$4, $5-$1)}'

    echo ""
}
filter() {
    grep "\[TIME\]" | awk '{printf("%s%s", $2, (NR%5 ? "," : "\n")) }'
}

main $*