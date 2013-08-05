package dsl.reactive.syntax

import scala.virtualization.lms.common.Base
import dsl.reactive.{AccessableDepHolder,ReactiveEntities,DepHolder}

trait DepHolderSyntax extends Base {
  class AccessableDepHolderOps[A:Manifest](dh: Rep[AccessableDepHolder[A]]) {
    def get: Rep[A] = dep_holder_access(dh)
  }

  implicit def toAccessableDepHolderOps[A:Manifest](
    dh: Rep[AccessableDepHolder[A]]) =
      new AccessableDepHolderOps(dh)

  def dep_holder_access[A:Manifest](dh: Rep[AccessableDepHolder[A]]): Rep[A]

  implicit def toDepHolderOps(dh: Rep[DepHolder]): DepHolderOps = new DepHolderOps(dh)
  class DepHolderOps(dh: Rep[DepHolder]) {
    def getDependents: Rep[ReactiveEntities] = dep_holder_dependents(dh)
  }

  def dep_holder_dependents(dh: Rep[DepHolder]): Rep[ReactiveEntities]
}
