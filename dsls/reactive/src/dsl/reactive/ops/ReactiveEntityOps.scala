package dsl.reactive.ops

import scala.virtualization.lms.common.EffectExp

import dsl.reactive.syntax.ReactiveEntitySyntax
import dsl.reactive.{ReactiveEntities,ReactiveEntity}

trait ReactiveEntityOps extends EffectExp {
  this: ReactiveEntitySyntax =>

  case class GetDependents(dh: Exp[ReactiveEntity]) extends Def[ReactiveEntities]
  override def reactive_entity_dependents(entity: Exp[ReactiveEntity]): Exp[ReactiveEntities] =
    GetDependents(entity)

  case class GetDependentsList(dh: Exp[ReactiveEntity]) extends Def[List[ReactiveEntity]]
  override def reactive_entity_dependents_list(entity: Exp[ReactiveEntity]): Exp[List[ReactiveEntity]] =
    GetDependentsList(entity)

  case class ReEvaluation(elem: Exp[ReactiveEntity]) extends Def[Unit]
  override def re_evaluate(elem: Exp[ReactiveEntity]): Exp[Unit] = {
    reflectEffect(ReEvaluation(elem))
  }

}
