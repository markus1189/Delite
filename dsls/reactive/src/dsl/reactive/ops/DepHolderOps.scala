package dsl.reactive.ops

import scala.virtualization.lms.common.EffectExp

import dsl.reactive.{AccessableDepHolder, ReactiveEntity, ReactiveEntities, DepHolder}

import dsl.reactive.syntax.DepHolderSyntax

trait DepHolderOps extends EffectExp {
  this: DepHolderSyntax =>

  case class AccessDepHolder[A:Manifest](dh: Exp[AccessableDepHolder[A]]) extends Def[A]

  override def dep_holder_access[A:Manifest](dh: Exp[AccessableDepHolder[A]]): Exp[A] =
    reflectMutable(AccessDepHolder(dh))
}
