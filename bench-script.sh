#!/bin/zsh

BASE_LIB_PATH="dsls/reactive/src/dsl/reactive/datastruct/scala/ReactiveLib.scala"
ENABLE_PROP_LINE="  def notifyDependents() { dependents foreach (_.dependsOnChanged(this)) } /*<+PROPAGATION_ENABLED+>*/"
DISABLE_PROP_LINE="  def notifyDependents() {}  /*<+PROPAGATION_DISABLED+>*/"

OPTI_PROP_PATH="dsls/reactive/src/dsl/reactive/optimizations/Propagation.scala"
PAR_ENABLE_PROP_LINE="    notify(dh) /*<+PROPAGATION_ENABLED+>*/"
PAR_DISABLE_PROP_LINE="    /*<+PROPAGATION_DISABLED+>*/"

DELITE_SIGNAL_FILE="dsls/reactive/src/dsl/reactive/case_testing/ThesisBenchmark.scala"
LIBRARY_SIGNAL_FILE="dsls/reactive/src/dsl/reactive/LibraryTests.scala"

main() {
    setup_required_env

    RUNS="$(if [[ -z "$1" ]]; then echo 5; else echo $1; fi)"
    THREADS="$(if [[ -z "$2" ]]; then echo 4; else echo $2; fi)"
    NUM_SIGNALS=20
    NUM_REF=20

    set_number_of_signals 20
    set_number_of_referenced_signals 20

    info "Using $NUM_SIGNALS Signals, of which $NUM_REF will actually be referenced"

    benchmark_parallel_propagation
    benchmark_base_propagation
    benchmark_library
}

function benchmark_parallel_propagation {
    bench_describe "Delite runtime with parallel propagation"
    FILE=dsl.reactive.BenchmarkRunner
    OUTPUT_FILE="only_parallel_${RUNS}_${THREADS}_${NUM_SIGNALS}_${NUM_REF}.csv"

    only_par_propagation
    compile_scala
    delite_compile
    OUTPUT=$(run_delite)
    report_results "$OUTPUT" > $OUTPUT_FILE
    info "wrote results to: $OUTPUT_FILE"
    bench_conclude
}

function benchmark_base_propagation {
    bench_describe "Delite runtime with base propagation"
    FILE=dsl.reactive.BenchmarkRunner
    OUTPUT_FILE="only_base_${RUNS}_${THREADS}_${NUM_SIGNALS}_${NUM_REF}.csv"

    only_base_propagation
    compile_scala
    delite_compile
    OUTPUT=$(run_delite)
    report_results "$OUTPUT" > $OUTPUT_FILE
    info "wrote results to: $OUTPUT_FILE"
    bench_conclude
}

function benchmark_library {
    bench_describe "Vanilla library without delite"
    OUTPUT_FILE="only_library_${RUNS}_${THREADS}_${NUM_SIGNALS}_${NUM_REF}.csv"

    compile_scala
    info "Running library version."
    OUTPUT=$(sbt "; project reactive; run-main dsl.reactive.OnlyLibrary $RUNS" 2>&1)

    validate_output "$OUTPUT"

    OUTPUT=$(echo "$OUTPUT" | postprocess_output)

    report_results "$OUTPUT" > $OUTPUT_FILE
    info "wrote results to: $OUTPUT_FILE"

    bench_conclude
}

function postprocess_output {
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

    echo "$LOCAL_OUTPUT" | postprocess_output
}

function report_results {
    INPUT="$1"

    echo "# Setup: RUNS=$RUNS, THREADS=$THREADS, NUM_SIGNALS=$NUM_SIGNALS, NUM_REF=$NUM_REF"

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

    echo "$INPUT" | grep -q 165580141

    if [[ "0" != "$?" ]]; then
        fail "output is incorrect. did not see 165580141."
    fi

    info "Output was correct."
}

function set_number_of_signals {
    sed -i -e "s/val NUMBER_OF_SIGNALS = .*/val NUMBER_OF_SIGNALS = $1/" $DELITE_SIGNAL_FILE
    sed -i -e "s/val NUMBER_OF_SIGNALS = .*/val NUMBER_OF_SIGNALS = $1/" $LIBRARY_SIGNAL_FILE
}

function set_number_of_referenced_signals {
    sed -i -e "s/val NUMBER_OF_REFERENCED_SIGNALS = .*/val NUMBER_OF_REFERENCED_SIGNALS = $1/" $DELITE_SIGNAL_FILE
    sed -i -e "s/val NUMBER_OF_REFERENCED_SIGNALS = .*/val NUMBER_OF_REFERENCED_SIGNALS = $1/" $LIBRARY_SIGNAL_FILE
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

function bench_describe {
    echo "[BENCHMARK]: $*"
}

function bench_conclude {
    echo "[BENCHMARK FINISHED]"
}

main $*