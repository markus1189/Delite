package dsl.reactive.ops

import scala.virtualization.lms.common.EffectExp

import dsl.reactive.syntax.VarSyntax
import dsl.reactive.Var

trait VarOps extends EffectExp {
  this: VarSyntax =>

  case class VarCreation[A:Manifest](value: Exp[A]) extends Def[Var[A]]
  override def new_reactive_var[A:Manifest](v: Exp[A]): Exp[Var[A]] = VarCreation(v)
}

