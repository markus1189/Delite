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
  println(y11.get)
  println(y12.get)
  println(y13.get)
  println(y14.get)
  println(y15.get)
  println(y16.get)
  println(y17.get)
  println(y18.get)
  println(y19.get)
  println(y20.get)
  println(y21.get)
  println(y22.get)
  println(y23.get)
  println(y24.get)
  println(y25.get)
  println(y26.get)
  println(y27.get)
  println(y28.get)
  println(y29.get)
  println(y30.get)
  //println(y0.get+y1.get+y2.get+y3.get+y4.get+y5.get)

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
  println(y11.get)
  println(y12.get)
  println(y13.get)
  println(y14.get)
  println(y15.get)
  println(y16.get)
  println(y17.get)
  println(y18.get)
  println(y19.get)
  println(y20.get)
  println(y21.get)
  println(y22.get)
  println(y23.get)
  println(y24.get)
  println(y25.get)
  println(y26.get)
  println(y27.get)
  println(y28.get)
  println(y29.get)
  println(y30.get)
  //println(y0.get+y1.get+y2.get+y3.get+y4.get+y5.get)

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
  println(y11.get)
  println(y12.get)
  println(y13.get)
  println(y14.get)
  println(y15.get)
  println(y16.get)
  println(y17.get)
  println(y18.get)
  println(y19.get)
  println(y20.get)
  println(y21.get)
  println(y22.get)
  println(y23.get)
  println(y24.get)
  println(y25.get)
  println(y26.get)
  println(y27.get)
  println(y28.get)
  println(y29.get)
  println(y30.get)
  //println(y0.get+y1.get+y2.get+y3.get+y4.get+y5.get)

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
  println(y11.get)
  println(y12.get)
  println(y13.get)
  println(y14.get)
  println(y15.get)
  println(y16.get)
  println(y17.get)
  println(y18.get)
  println(y19.get)
  println(y20.get)
  println(y21.get)
  println(y22.get)
  println(y23.get)
  println(y24.get)
  println(y25.get)
  println(y26.get)
  println(y27.get)
  println(y28.get)
  println(y29.get)
  println(y30.get)
  //println(y0.get+y1.get+y2.get+y3.get+y4.get+y5.get)

  printTime()
  }
}
