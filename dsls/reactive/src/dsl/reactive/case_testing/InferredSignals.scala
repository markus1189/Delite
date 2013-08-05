package dsl.reactive

object InferenceRunner extends ReactiveApplicationRunner with TestInference

trait TestInference extends ReactiveApplication {
  def main() = {
    val a = Var(42)
    val b = Var(1)

    val s1 = ISignal { a.get + b.get }
    val s2 = ISignal { a.get * b.get }

    val s3 = ISignal { s1.get + s2.get }
    println(s1.get + " & " + s2.get + " & " + s3.get)

    b.set(2)

    println(s1.get + " & " + s2.get + " & " + s3.get)
  }
}
