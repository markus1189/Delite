package dsl.reactive

object HelloReactiveRunner extends ReactiveApplicationRunner with HelloReactive

trait HelloReactive extends ReactiveApplication { 
  def main() = { }
}
