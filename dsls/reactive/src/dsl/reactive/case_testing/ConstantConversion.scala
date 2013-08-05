package dsl.reactive

object ConvertConstantsRunner extends ReactiveApplicationRunner with ConvertConstants

trait ConvertConstants extends ReactiveApplication {
  def main() = {
    val c1 = ISignal { 42 }
    val c2 = ISignal { 1337 }
    val shouldBecomeConstant = ISignal { c1.get + c2.get }

    println(shouldBecomeConstant.get)
  }
}
