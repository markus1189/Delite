package dsl.reactive

object HelloReactiveRunner extends ReactiveApplicationRunner with HelloReactive

trait HelloReactive extends ReactiveApplication { 
  def main() = {
    val x = ReactiveVar(5)
    println(x.get)
    x.set(42)
    println(x.get)
    x.set(1337)
    println(x.get)
  }
}
