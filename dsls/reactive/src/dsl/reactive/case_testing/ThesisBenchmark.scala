package dsl.reactive

import scala.virtualization.lms.internal.GraphVizExport

object BenchmarkRunner extends ReactiveApplicationRunner with ThesisBenchmark

trait ThesisBenchmark extends ReactiveApplication {
  def main() = {
    val value: Long = 40

    val  x0  = Var(3l)

    val NUMBER_OF_SIGNALS = 20
    val NUMBER_OF_REFERENCED_SIGNALS = 10

    val sigs = scala.collection.Seq.fill(NUMBER_OF_SIGNALS){
      Signal(x0) { expensive(x0.get) }
    }

    def printSigs() {
      sigs.take(NUMBER_OF_REFERENCED_SIGNALS).map(x => println(x.get))
    }

    printTime() // start time

    printSigs()

    printTime() // all signals setup

    x0.set(value)

    printSigs()

    printTime() // first propagation

    x0.set(value-1)

    printSigs()

    printTime() // second propagation

    x0.set(value+1)

    printSigs()

    printTime() // final time
  }
}