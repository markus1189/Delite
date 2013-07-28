package dsl.reactive

object ConvertConstantsRunner extends ReactiveApplicationRunner with ConvertConstants

trait ConvertConstants extends ReactiveApplication {
  def main() = {
    val c1 = Signal() { 42 }
    val c2 = Signal() { 1337 }
    val shouldBecomeConstant = Signal (c1,c2) { c1.get + c2.get }

    println(shouldBecomeConstant.get)
  }
}
