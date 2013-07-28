package dsl.reactive

import scala.virtualization.lms.internal.GraphVizExport

object ExpensivesRunner extends ReactiveApplicationRunner with Expensives

trait Expensives extends ReactiveApplication {
  def main() = {
  val value = 40

  val  x0  = Var(3l)
  val  y0  = Signal(x0){expensive(x0.get)}
  val  y1  = Signal(x0){expensive(x0.get)}
  val  y2  = Signal(x0){expensive(x0.get)}
  val  y3  = Signal(x0){expensive(x0.get)}
  val  y4  = Signal(x0){expensive(x0.get)}
  val  y5  = Signal(x0){expensive(x0.get)}
  val  y6  = Signal(x0){expensive(x0.get)}
  val  y7  = Signal(x0){expensive(x0.get)}
  val  y8  = Signal(x0){expensive(x0.get)}
  val  y9  = Signal(x0){expensive(x0.get)}
  val  y10 = Signal(x0){expensive(x0.get)}
  val  y11 = Signal(x0){expensive(x0.get)}
  val  y12 = Signal(x0){expensive(x0.get)}
  val  y13 = Signal(x0){expensive(x0.get)}
  val  y14 = Signal(x0){expensive(x0.get)}
  val  y15 = Signal(x0){expensive(x0.get)}
  val  y16 = Signal(x0){expensive(x0.get)}
  val  y17 = Signal(x0){expensive(x0.get)}
  val  y18 = Signal(x0){expensive(x0.get)}
  val  y19 = Signal(x0){expensive(x0.get)}
  val  y20 = Signal(x0){expensive(x0.get)}
  val  y21 = Signal(x0){expensive(x0.get)}
  val  y22 = Signal(x0){expensive(x0.get)}
  val  y23 = Signal(x0){expensive(x0.get)}
  val  y24 = Signal(x0){expensive(x0.get)}
  val  y25 = Signal(x0){expensive(x0.get)}
  val  y26 = Signal(x0){expensive(x0.get)}
  val  y27 = Signal(x0){expensive(x0.get)}
  val  y28 = Signal(x0){expensive(x0.get)}
  val  y29 = Signal(x0){expensive(x0.get)}
  val  y30 = Signal(x0){expensive(x0.get)}

  printTime()

  println(y0.get)
  println(y1.get)
  println(y2.get)
  println(y3.get)
  println(y4.get)
  println(y5.get)
  println(y6.get)
  println(y7.get)
  println(y8.get)
  println(y9.get)
  println(y10.get)

  printTime()

  x0.set(value)

  println(y0.get)
  println(y1.get)
  println(y2.get)
  println(y3.get)
  println(y4.get)
  println(y5.get)
  println(y6.get)
  println(y7.get)
  println(y8.get)
  println(y9.get)
  println(y10.get)

  x0.set(value-1)

  println(y0.get)
  println(y1.get)
  println(y2.get)
  println(y3.get)
  println(y4.get)
  println(y5.get)
  println(y6.get)
  println(y7.get)
  println(y8.get)
  println(y9.get)
  println(y10.get)

  printTime()

  x0.set(value)

  println(y0.get)
  println(y1.get)
  println(y2.get)
  println(y3.get)
  println(y4.get)
  println(y5.get)
  println(y6.get)
  println(y7.get)
  println(y8.get)
  println(y9.get)
  println(y10.get)

  printTime()
  }
}
