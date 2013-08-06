package dsl.reactive.syntaxops

import scala.virtualization.lms.common.{Base,EffectExp}
import dsl.reactive.{ReactiveEntities,ReactiveEntity}

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

  def reactive_entity_dependents(
    entity: Rep[ReactiveEntity]): Rep[ReactiveEntities]

  def reactive_entity_dependents_list(
    entity: Rep[ReactiveEntity]): Rep[List[ReactiveEntity]]

  def re_evaluate(elem: Rep[ReactiveEntity]): Rep[Unit]
}

trait ReactiveEntityOps extends EffectExp {
  this: ReactiveEntitySyntax =>

  case class GetDependents(
    dh: Exp[ReactiveEntity]) extends Def[ReactiveEntities]

  override def reactive_entity_dependents(
    entity: Exp[ReactiveEntity]): Exp[ReactiveEntities] =
    GetDependents(entity)

  case class GetDependentsList(
    dh: Exp[ReactiveEntity]) extends Def[List[ReactiveEntity]]

  override def reactive_entity_dependents_list(
    entity: Exp[ReactiveEntity]): Exp[List[ReactiveEntity]] =
    GetDependentsList(entity)

  case class ReEvaluation(elem: Exp[ReactiveEntity]) extends Def[Unit]

  override def re_evaluate(elem: Exp[ReactiveEntity]): Exp[Unit] = {
    reflectEffect(ReEvaluation(elem))
  }

}
