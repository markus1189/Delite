package dsl.reactive
import dsl.reactive.datastruct.scala._

trait BenchmarkUtils {
  def printTime() = System.err.println("[TIME]: " + System.currentTimeMillis)

  def expensive(x: Long): Long = x match {
    case 1 => 1
    case 2 => 1
    case _ => expensive(x-1) + expensive(x-2)
  }
}

object OnlyLibrary extends App with LibThesisBenchmark with BenchmarkUtils

trait LibThesisBenchmark {
  this: App with BenchmarkUtils =>

  override def main(args: Array[String]) = {
    val numberOfRuns = args.head.toInt

    for ( i <- 1 to numberOfRuns) {
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
}