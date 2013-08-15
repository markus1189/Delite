package dsl.reactive

object ConvertPointRunner extends ReactiveApplicationRunner with ConvertingPoint

trait ConvertingPoint extends ReactiveApplication {
  def main() = {
    val p1 = ISignal { Point( 5, 5) }
    val p2 = ISignal { Point(10, 5) }

    val distance = ISignal { euclidDistance(p1.get,p2.get) }
    val sqDistance = ISignal { distance.get * distance.get }

    println(sqDistance.get)
  }
}