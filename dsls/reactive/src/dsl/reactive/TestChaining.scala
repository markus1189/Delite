package dsl.reactive

import scala.virtualization.lms.internal.GraphVizExport

object TestChainingRunner extends ReactiveApplicationRunner with TestChaining

trait TestChaining extends ReactiveApplication { 
  def main() = {
    val root = Var(1)
    val e1 = Signal(root) { root.get + 1 }
    val e2 = Signal(e1) { e1.get + 1 }
    val e3 = Signal(e2) { e2.get + 1 }
    val e4 = Signal(e3) { e3.get + 1 }

    println("0: " + root.get + ", " + e1.get + ", " + e2.get + ", " + e3.get + ", " + e4.get)
    root.set(2)
    println("1: " + root.get + ", " + e1.get + ", " + e2.get + ", " + e3.get + ", " + e4.get)
    root.set(3)
    println("2: " + root.get + ", " + e1.get + ", " + e2.get + ", " + e3.get + ", " + e4.get)
  }
}


