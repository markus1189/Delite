package dsl.reactive.syntax

import scala.virtualization.lms.common._
import dsl.reactive._

trait ReactiveEntitySyntax extends Base {
  implicit def toReactiveEntityOps(entity: Rep[ReactiveEntity]) =
    new ReactiveEntityOps(entity)

  class ReactiveEntityOps(entity: Rep[ReactiveEntity]) {
    def getDependents: Rep[ReactiveEntities] =
      reactive_entity_dependents(entity)

    def getDependentsList: Rep[List[ReactiveEntity]] =
      reactive_entity_dependents_list(entity)

    def reEvaluate() = re_evaluate(entity)
  }

  def reactive_entity_dependents(entity: Rep[ReactiveEntity]): Rep[ReactiveEntities]
  def reactive_entity_dependents_list(entity: Rep[ReactiveEntity]): Rep[List[ReactiveEntity]]
  def re_evaluate(elem: Rep[ReactiveEntity]): Rep[Unit]
}
