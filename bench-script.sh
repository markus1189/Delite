#!/bin/zsh

BASE_LIB_PATH="dsls/reactive/src/dsl/reactive/datastruct/scala/ReactiveLib.scala"
ENABLE_PROP_LINE="  def dependsOnChanged(dep: DepHolder) { reEvaluate() } /*<+PROPAGATION_ENABLED+>*/"
DISABLE_PROP_LINE="  def dependsOnChanged(dep: DepHolder) { /*<+PROPAGATION_DISABLED+>*/ }"

main() {
    RUNS="$(if [[ -z "$1" ]]; then echo 5; else echo $1; fi)"
    THREADS="$(if [[ -z "$2" ]]; then echo 4; else echo $2; fi)"
    FILE=dsl.reactive.BenchmarkRunner

    if [[ -z "$JAVA_HOME" ]]; then
      echo Setting JAVA_HOME >&2
      export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
    fi

    export SCALA_VIRT_HOME=$HOME/.sbt/0.12.0-Beta2/boot/scala-2.10.0-M1-virtualized/lib/

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

function enable_base_propagation {
    sed -i -e "s,.*PROPAGATION_DISABLED.*,$ENABLE_PROP_LINE," $BASE_LIB_PATH
}

function disable_base_propagation {
    sed -i -e "s,.*PROPAGATION_ENABLED.*,$DISABLE_PROP_LINE," $BASE_LIB_PATH
}

function is_propagation_disabled {
    grep -q 'PROPAGATION_DISABLED' $BASE_LIB_PATH

}

function is_propagation_enabled {
    grep -q 'PROPAGATION_ENABLED' $BASE_LIB_PATH
}

is_propagation_enabled
echo enabled: $?
is_propagation_disabled
echo disabled: $?

enable_base_propagation
disable_base_propagation
exit

main $*