package dsl.reactive.syntax

import scala.virtualization.lms.common.Base
import dsl.reactive.Var

trait VarSyntax extends Base {
  implicit def toVarOps[A:Manifest](v: Rep[Var[A]]): VarOps[A] = new VarOps(v)
  class VarOps[A:Manifest](v: Rep[Var[A]]) {
    def set(value: Rep[A]): Rep[Unit] = dep_holder_set(v, value)
  }

  def dep_holder_set[A:Manifest](v: Rep[Var[A]], value: Rep[A]): Rep[Unit]

  object Var {
    def apply[A:Manifest](v: Rep[A]): Rep[Var[A]] = new_reactive_var(v)
  }

  def new_reactive_var[A:Manifest](v: Rep[A]): Rep[Var[A]]
}
