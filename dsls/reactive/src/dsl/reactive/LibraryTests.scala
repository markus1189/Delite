import dsl.reactive.datastruct.scala._

object Main extends App {
  def printTime() = println("Current time: " + System.currentTimeMillis)
  def expensive(x: Long): Long = x match {
    case 1 => 1
    case 2 => 1
    case _ => expensive(x-1) + expensive(x-2)
  }

  for ( i <- 1 to 4 ) {
  val value = 40

  val  x0  = Var(3l)
  val  y0  = Signal(x0){1l+expensive(x0.get)}
  val  y1  = Signal(x0){2l+expensive(x0.get)}
  val  y2  = Signal(x0){3l+expensive(x0.get)}
  val  y3  = Signal(x0){4l+expensive(x0.get)}
  val  y4  = Signal(x0){5l+expensive(x0.get)}
  val  y5  = Signal(x0){6l+expensive(x0.get)}
  val  y6  = Signal(x0){7l+expensive(x0.get)}
  val  y7  = Signal(x0){8l+expensive(x0.get)}
  val  y8  = Signal(x0){9l+expensive(x0.get)}
  val  y9  = Signal(x0){10l+expensive(x0.get)}
  val  y10 = Signal(x0){11l+expensive(x0.get)}
  val  y11 = Signal(x0){12l+expensive(x0.get)}
  val  y12 = Signal(x0){13l+expensive(x0.get)}
  val  y13 = Signal(x0){14l+expensive(x0.get)}
  val  y14 = Signal(x0){15l+expensive(x0.get)}
  val  y15 = Signal(x0){16l+expensive(x0.get)}
  val  y16 = Signal(x0){17l+expensive(x0.get)}
  val  y17 = Signal(x0){18l+expensive(x0.get)}
  val  y18 = Signal(x0){19l+expensive(x0.get)}
  val  y19 = Signal(x0){20l+expensive(x0.get)}
  val  y20 = Signal(x0){21l+expensive(x0.get)}
  val  y21 = Signal(x0){22l+expensive(x0.get)}
  val  y22 = Signal(x0){23l+expensive(x0.get)}
  val  y23 = Signal(x0){24l+expensive(x0.get)}
  val  y24 = Signal(x0){25l+expensive(x0.get)}
  val  y25 = Signal(x0){26l+expensive(x0.get)}
  val  y26 = Signal(x0){27l+expensive(x0.get)}
  val  y27 = Signal(x0){28l+expensive(x0.get)}
  val  y28 = Signal(x0){29l+expensive(x0.get)}
  val  y29 = Signal(x0){30l+expensive(x0.get)}
  val  y30 = Signal(x0){31l+expensive(x0.get)}

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
