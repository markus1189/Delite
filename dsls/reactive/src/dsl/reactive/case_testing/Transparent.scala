package dsl.reactive

import dsl.reactive.optimizations.TransparentReactivity

object TransparencyRunner extends ReactiveApplicationRunner with TestTransparency

trait TestTransparency extends ReactiveApplication with TransparentReactivity {
  def main() = {
    var source1: Rep[dsl.reactive.Var[Int]] = 5

    source1 = 15

    println(source1.get)
  }
}
