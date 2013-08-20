package dsl.reactive

import scala.virtualization.lms.internal.GraphVizExport

object BenchmarkRunner extends ReactiveApplicationRunner with ThesisBenchmark

trait ThesisBenchmark extends ReactiveApplication {
  def main() = {
    val value: Long = 40

    val  x0  = Var(3l)

    val sigs = scala.collection.Seq.fill(20){
      Signal(x0) { expensive(x0.get) }
    }

    printTime() // start time

    sigs.map(x => println(x.get))

    printTime() // all signals setup

    x0.set(value)

    sigs.map(x => println(x.get))

    printTime() // first propagation

    x0.set(value-1)

    sigs.map(x => println(x.get))

    printTime() // second propagation

    x0.set(value+1)

    sigs.map(x => println(x.get))

    printTime() // final time
  }
}