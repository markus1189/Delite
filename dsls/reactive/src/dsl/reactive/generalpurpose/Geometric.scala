package dsl.reactive.generalpurpose

import scala.virtualization.lms.common.{Base,EffectExp,ScalaGenEffect,ScalaGenBase}

trait PointSyntax extends Base {
  type Point

  object Point {
    def apply(x: Rep[Int], y: Rep[Int]): Rep[Point] = new_point(x,y)
  }

  def new_point(x: Rep[Int],y: Rep[Int]): Rep[Point]

  def euclidDistance(p1: Rep[Point], p2: Rep[Point]): Rep[Double]
}

trait PointExp extends PointSyntax with EffectExp {
  type Point = (Int,Int)

  case class CreatePoint(x: Exp[Int], y: Exp[Int]) extends Def[Point]
  override def new_point(x: Exp[Int], y: Exp[Int]) = new CreatePoint(x,y)

  case class EuclideanDistance(p1: Exp[Point], p2: Exp[Point]) extends Def[Double]
  override def euclidDistance(p1: Exp[Point], p2: Exp[Point]): Exp[Double] = new EuclideanDistance(p1,p2)
}

trait ScalaGenPoint extends ScalaGenBase with ScalaGenEffect {
  val IR: PointExp
  import IR._


  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case CreatePoint(x,y) => emitValDef(sym,"(" + quote(x) + "," + quote(y) + ")")
    case EuclideanDistance(p1,p2) => emitValDef(sym,
      "math.sqrt(" +
        "math.pow(" + quote(p1) + "._1 - " + quote(p2) + "._1, 2) " +
        "+" +
        "math.pow(" + quote(p1) + "._2 - " + quote(p2) + "._2, 2) " +
      ")"
    )
    case _ => super.emitNode(sym,rhs)
  }
}