package dsl.reactive.ops

import scala.virtualization.lms.common.EffectExp

import dsl.reactive.syntax.SignalSyntax
import dsl.reactive.{DepHolder, Behavior}

trait SignalOps extends EffectExp {
  this: SignalSyntax =>

  case class SignalCreation[A:Manifest](
    dhs: Seq[Exp[DepHolder]],
    body: Block[A]
  ) extends Def[Behavior[A]]

  override def new_behavior[A:Manifest](
    dhs: Seq[Exp[DepHolder]],
    f: => Exp[A]
  ): Exp[Behavior[A]] = SignalCreation(dhs, reifyEffects(f))

  override def boundSyms(e: Any): List[Sym[Any]] = e match {
    case SignalCreation(dhs,body) => effectSyms(body)
    case _ => super.boundSyms(e)
  }
}
