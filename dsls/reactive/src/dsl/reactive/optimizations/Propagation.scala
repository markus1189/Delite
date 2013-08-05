package dsl.reactive.optimizations

import scala.virtualization.lms.common._

import ppl.delite.framework.ops.{DeliteCollectionOpsExp,DeliteOpsExp}
import ppl.delite.framework.datastruct.scala.DeliteCollection

import dsl.reactive.{ReactiveEntity, ReactiveEntities}

import dsl.reactive.syntax.{ReactiveEntitySyntax,VarSyntax}
import dsl.reactive.ops.VarOps

trait Propagation extends EffectExp
  with ListOpsExp
  with DeliteOpsExp
  with VarSyntax
  with VarOps
  with DeliteCollectionOpsExp {

  this: ReactiveEntitySyntax =>

  case class NotifyDependents(dh: Exp[ReactiveEntity]) extends DeliteOpForeach[ReactiveEntity] {
    def func: Exp[ReactiveEntity] => Exp[Unit] = _.reEvaluate()
    val in: Exp[DeliteCollection[ReactiveEntity]] = dh.getDependents
    val size: Exp[Int] = dh.getDependents.size
    def sync: Exp[Int] => Exp[List[Any]] = _ => List[Any]()
  }

  override def dep_holder_set[A:Manifest](dh: Exp[dsl.reactive.Var[A]], value: Exp[A]): Exp[Unit] = {
    super.dep_holder_set(dh,value)
    notify(dh)
  }

  private def notify(e: Exp[ReactiveEntity]) {
    reflectEffect(NotifyDependents(e))
    // yypIe.getDependentsList.map(_fexA)^
      e.getDependentsList.map(e => reflectEffect(NotifyDependents(e)))
    e.getDependentsList.map(_.getDependentsList.map(e => reflectEffect(NotifyDependents(e))))
      e.getDependentsList.map(_.getDependentsList.map(_.getDependentsList.map(e => reflectEffect(NotifyDependents(e)))))
    //e.getDependentsList.map(_.getDependentsList.map(_.getDependentsList.map(_.getDependentsList.map(e => reflectEffect(NotifyDependents(e))))))
  }

  case class GetDependentsList(dh: Exp[ReactiveEntity]) extends Def[List[ReactiveEntity]]
  override def reactive_entity_dependents_list(entity: Exp[ReactiveEntity]): Exp[List[ReactiveEntity]] =
    GetDependentsList(entity)

  case class ReEvaluation(elem: Exp[ReactiveEntity]) extends Def[Unit]
  override def re_evaluate(elem: Exp[ReactiveEntity]): Exp[Unit] = {
    reflectEffect(ReEvaluation(elem))
  }

  case class GetSizeReactiveEntities(reSeq: Exp[ReactiveEntities]) extends Def[Int]
  def infix_size(reSeq: Exp[ReactiveEntities]): Exp[Int] = GetSizeReactiveEntities(reSeq)
}
