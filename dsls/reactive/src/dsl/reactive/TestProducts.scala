package dsl.reactive

import scala.virtualization.lms.internal.GraphVizExport

object TestProductsRunner extends ReactiveApplicationRunner with TestProducts

trait TestProducts extends ReactiveApplication { 
  def main() = {
    println(System.currentTimeMillis)
    val x0 = Var(5)
    val x1 = Var(5)
    val x2 = Var(5)
    val x3 = Var(5)
    val x4 = Var(5)
    val x5 = Var(5)
    val x6 = Var(5)
    val x7 = Var(5)
    val x8 = Var(5)
    val x9 = Var(5)

    val y0 = Signal(x0,x1) { x0.get * x1.get }
    val y1 = Signal(x1,x2) { x1.get * x2.get }
    val y2 = Signal(x2,x3) { x2.get * x3.get }
    val y3 = Signal(x3,x4) { x3.get * x4.get }
    val y4 = Signal(x4,x5) { x4.get * x5.get }
    val y5 = Signal(x5,x6) { x5.get * x6.get }
    val y6 = Signal(x6,x7) { x6.get * x7.get }
    val y7 = Signal(x7,x8) { x7.get * x8.get }
    val y8 = Signal(x8,x9) { x8.get * x9.get }

    val prod = Signal(y0,y1,y2,y3,y4,y5,y6,y7,y8) { 
      y0.get *
      y1.get *
      y2.get *
      y3.get *
      y4.get *
      y5.get *
      y6.get *
      y7.get *
      y8.get
    }

    println(prod.get)
    println(System.currentTimeMillis)

    x0.set(util.Random.nextInt)
    x1.set(util.Random.nextInt)
    x2.set(util.Random.nextInt)
    x3.set(util.Random.nextInt)
    x4.set(util.Random.nextInt)
    x5.set(util.Random.nextInt)
    x6.set(util.Random.nextInt)
    x7.set(util.Random.nextInt)
    x8.set(util.Random.nextInt)
    x9.set(util.Random.nextInt)

    println(prod.get)
    println(System.currentTimeMillis)
  }
}

