#!/bin/zsh

BASE_LIB_PATH="dsls/reactive/src/dsl/reactive/datastruct/scala/ReactiveLib.scala"
ENABLE_PROP_LINE="  def notifyDependents() { dependents foreach (_.dependsOnChanged(this)) } /*<+PROPAGATION_ENABLED+>*/"
DISABLE_PROP_LINE="  def notifyDependents() {}  /*<+PROPAGATION_DISABLED+>*/"

OPTI_PROP_PATH="dsls/reactive/src/dsl/reactive/optimizations/Propagation.scala"
PAR_ENABLE_PROP_LINE="    notify(dh) /*<+PROPAGATION_ENABLED+>*/"
PAR_DISABLE_PROP_LINE="    /*<+PROPAGATION_DISABLED+>*/"

main() {
    only_par_propagation
    setup_required_env

    RUNS="$(if [[ -z "$1" ]]; then echo 5; else echo $1; fi)"
    THREADS="$(if [[ -z "$2" ]]; then echo 4; else echo $2; fi)"
    FILE=dsl.reactive.BenchmarkRunner

    compile_scala

    delite_compile

    OUTPUT=$(run_delite)

    report_results "$OUTPUT"
}

function delite_out_filter() {
    grep "\[TIME\]" | awk '{printf("%s%s", $2, (NR%5 ? "," : "\n")) }'
}

function enable_par_propagation {
    sed -i -e "s,.*PROPAGATION_DISABLED.*,$PAR_ENABLE_PROP_LINE," $OPTI_PROP_PATH
    if is_par_propagation_enabled; then
        info "parallel propagation enabled"
    else
        fail "could not enable parallel propagation"
    fi
}

function disable_par_propagation {
    sed -i -e "s,.*PROPAGATION_ENABLED.*,$PAR_DISABLE_PROP_LINE," $OPTI_PROP_PATH
    if is_par_propagation_disabled; then
        info "parallel propagation disabled"
    else
        fail "could not disable parallel propagation"
    fi
}

function is_par_propagation_enabled {
    grep -q 'PROPAGATION_ENABLED' $OPTI_PROP_PATH
}

function is_par_propagation_disabled {
    grep -q 'PROPAGATION_DISABLED' $OPTI_PROP_PATH
}

function enable_base_propagation {
    sed -i -e "s,.*PROPAGATION_DISABLED.*,$ENABLE_PROP_LINE," $BASE_LIB_PATH
    if is_base_propagation_enabled; then
        info "base propagation enabled"
    else
        fail "could not enable base propagation"
    fi
}

function disable_base_propagation {
    sed -i -e "s,.*PROPAGATION_ENABLED.*,$DISABLE_PROP_LINE," $BASE_LIB_PATH
    if is_base_propagation_disabled; then
        info "base propagation disabled"
    else
        fail "could not disable base propagation"
    fi
}

function is_base_propagation_disabled {
    grep -q 'PROPAGATION_DISABLED' $BASE_LIB_PATH

}

function is_base_propagation_enabled {
    grep -q 'PROPAGATION_ENABLED' $BASE_LIB_PATH
}

function compile_scala {
    info "compiling scala code..."
    SBT_OUT=$(sbt '; project reactive; compile' 2>&1)
    if [[ "0" != "$?" ]]; then
        echo "$SBT_OUT"
        fail "Scala compilation failed"
    fi
}

function setup_required_env {

    if [[ -z "$JAVA_HOME" ]]; then
      info "Setting JAVA_HOME"
      export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
    fi

    export SCALA_VIRT_HOME=$HOME/.sbt/0.12.0-Beta2/boot/scala-2.10.0-M1-virtualized/lib/
}

function delite_compile {

    bin/delitec $FILE &> /dev/null
    if [[ "0" == "$?" ]]; then
        info "Delite compilation successful"
    else
        fail "Delite compilation failed"
    fi

}

function run_delite {
    info "Running ${FILE}: $RUNS times, with $THREADS threads"

    LOCAL_OUTPUT=$(bin/delite -t $THREADS --runs=$RUNS $FILE 2>&1)

    validate_output "$LOCAL_OUTPUT"

    echo "$LOCAL_OUTPUT" | delite_out_filter
}

function report_results {
    INPUT="$1"

    echo "start,setup,1st,2nd,end,setup-time,first-propagation,second-propagation,third-propagation,total-time"

    echo "$INPUT" | awk -F, \
        '{printf("%s,%s,%s,%s,%s,%s\n", $0, $2-$1, $3-$2, $4-$3, $5-$4, $5-$1)}'

    echo ""
}

function validate_output {
    INPUT="$1"

    echo "$INPUT" | grep -q 102334155

    if [[ "0" != "$?" ]]; then
        fail "output is incorrect. did not see 102334155."
    fi

    echo "$INPUT" | grep -q 63245986

    if [[ "0" != "$?" ]]; then
        fail "output is incorrect. did not see 63245986."
    fi

    info "Output was correct."
}

function only_par_propagation {
    disable_base_propagation
    enable_par_propagation
}

function only_base_propagation {
    disable_par_propagation
    enable_base_propagation
}

function info {
    echo "[INFO]: $*" >&2
}

function fail {
    echo "[FAIL]: $*" >&2
    exit 1
}

main $*