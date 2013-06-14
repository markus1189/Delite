package dsl.reactive

import scala.virtualization.lms.internal.GraphVizExport

object HelloReactiveRunner extends ReactiveApplicationRunner with HelloReactive

trait HelloReactive extends ReactiveApplication { 
  def main() = {
    val x = Var(5)
    val y = Var(7)
    val z = Var(10)
    val sum = Signal(x,y,z) { x.get + y.get + z.get }
    val product = Signal(x,y,z) { x.get * y.get * z.get }

    val sumAndProduct = Signal(sum,product) { "Sum is: " + sum.get + "; Product is: " + product.get }
    val threeLevelSignal = Signal(sumAndProduct) { "Level three: " + sumAndProduct.get }

    println("Sum is: " + sum.get)
    println(sumAndProduct.get)
    println(threeLevelSignal.get)

    x.set(10)
    y.set(2)
    z.set(3)

    println("Sum is: " + sum.get)
    println(sumAndProduct.get)
    println(threeLevelSignal.get)
  }
}
