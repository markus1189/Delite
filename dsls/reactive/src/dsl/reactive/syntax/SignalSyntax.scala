package dsl.reactive.syntax

import scala.virtualization.lms.common._
import dsl.reactive._

trait SignalSyntax extends Base {
  object Signal {
    def apply[A:Manifest](dhs: Rep[DepHolder]*)(f: => Rep[A]) =
      new_behavior(dhs, f)
  }

  def new_behavior[A:Manifest](dhs: Seq[Rep[DepHolder]], f: => Rep[A]): Rep[Behavior[A]]
}
