package dsl.reactive

import scala.virtualization.lms.common._
import ppl.delite.framework.ops.{DeliteCollectionOpsExp,DeliteOpsExp}
import ppl.delite.framework.datastruct.scala.DeliteCollection

import dsl.reactive.syntax._
import dsl.reactive.ops._
import dsl.reactive.optimizations._

trait Reactivity
    extends MeasureOps
    with ExpensiveOps
    with VarSyntax
    with SignalSyntax
    with DepHolderSyntax
    with ReactiveEntitySyntax
    with InferredSignals

trait ReactivityExp extends Reactivity
                    with MeasureOpsExp
                    with ExpensiveOpsExp
                    with ListOpsExp
                    with SeqOpsExp
                    with EffectExp
                    with DeliteCollectionOpsExp
                    with DeliteOpsExp
                    with InferredSignalsExp
                    with VarOps
                    with SignalOps {

  case class AccessDepHolder[A:Manifest](dh: Exp[AccessableDepHolder[A]]) extends Def[A]
  override def dep_holder_access[A:Manifest](dh: Exp[AccessableDepHolder[A]]): Exp[A] =
    reflectMutable(AccessDepHolder(dh))

  case class GetSizeReactiveEntities(reSeq: Exp[ReactiveEntities]) extends Def[Int]
  def infix_size(reSeq: Exp[ReactiveEntities]): Exp[Int] = GetSizeReactiveEntities(reSeq)

  case class NotifyDependents(dh: Exp[ReactiveEntity]) extends DeliteOpForeach[ReactiveEntity] {
    def func: Exp[ReactiveEntity] => Exp[Unit] = _.reEvaluate()
    val in: Exp[DeliteCollection[ReactiveEntity]] = dh.getDependents
    val size: Exp[Int] = dh.getDependents.size
    def sync: Exp[Int] => Exp[List[Any]] = _ => List[Any]()
  }

  case class SetDepHolder[A:Manifest](dh: Exp[dsl.reactive.Var[A]], value: Exp[A]) extends Def[Unit]
  override def dep_holder_set[A:Manifest](dh: Exp[dsl.reactive.Var[A]], value: Exp[A]): Exp[Unit] = {
    reflectEffect(SetDepHolder(dh,value))
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

  case class GetDependents(dh: Exp[ReactiveEntity]) extends Def[ReactiveEntities]
  override def dep_holder_dependents(dh: Exp[DepHolder]): Exp[ReactiveEntities] =
    GetDependents(dh)

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

// Optimize Signals with constant dependencies
trait ReactivityExpOpt extends ReactivityExp {
  private def onlyConstants(dhs: Seq[Exp[DepHolder]]): Boolean =  {
    val syms: Seq[Sym[DepHolder]] = dhs.filter {
      case Sym(x) => true
      case _ => false }.asInstanceOf[Seq[Sym[DepHolder]]]

    val defs: Seq[Def[Any]] = syms.map(findDefinition(_)).map {
      case Some(TP(_,rhs)) => Some(rhs)
      case _ => None
    }.filter(_.isDefined).map(_.get)

    defs.forall {
      case ConstantCreation(_) => true
      case _ => false
    }
  }

  override def new_behavior[A:Manifest](
    dhs: Seq[Exp[DepHolder]], f: => Exp[A]): Exp[Behavior[A]] = {

    if (dhs.isEmpty || onlyConstants(dhs)) {
      // Signal creation without dependency holders => constant
      // Signal with only constant deps => constant
      ConstantCreation(reifyEffects(f))
    } else {
      SignalCreation(dhs, reifyEffects(f))
    }
  }

  case class ConstantCreation[A:Manifest]( body: Block[A]) extends Def[Behavior[A]]
  case class ConstantAccess[A:Manifest](body : Block[A]) extends Def[A]

  override def dep_holder_access[A:Manifest](dh: Exp[AccessableDepHolder[A]]): Exp[A] = dh match {
    case Def(ConstantCreation(x)) => ConstantAccess(x)
    case _ => super.dep_holder_access(dh)
  }

}

// Infer the dependencies

trait TransparentReactivity {
  this: VarSyntax with LiftVariables =>

  def __newVar[T:Manifest](value: T): Rep[dsl.reactive.Var[T]] = new_reactive_var(unit(value))
  def __assign[T:Manifest](lhs: Rep[dsl.reactive.Var[T]], rhs: Rep[T]): Rep[Unit] = dep_holder_set(lhs,rhs)

}

trait ScalaGenReactivity extends ScalaGenBase
                         with ScalaGenMeasureOps
                         with ScalaGenExpensiveOps
                         with ScalaGenEffect
                         with ScalaGenOrderingOps
                         with ScalaGenWhile {
  val IR: ReactivityExp
  import IR._

  override def emitNode(sym: Sym[Any], node: Def[Any]): Unit =  node match {
    case GetSizeReactiveEntities(reSeq) => emitValDef(sym, quote(reSeq) + ".size")
    case AccessDepHolder(dh)            => emitValDef(sym, quote(dh) + ".get")
    case ReEvaluation(elem)             => emitValDef(sym, quote(elem) + ".forceReEval()")
    case GetDependents(dh)              => emitValDef(sym, quote(dh) + ".getDependents")
    case GetDependentsList(dh)          => emitValDef(sym, quote(dh) + ".getDependentsList")
    case SetDepHolder(dh,value)         => emitValDef(sym, quote(dh) + ".set(" + quote(value) + ")")
    case VarCreation(v)                 => emitValDef(sym, "Var(" + quote(v) + ")")
    case SignalCreation(dhs,f)          => emitValDef(sym, "Signal(" + dhs.map(quote).mkString(", ") + ") { ")
                                             emitBlock(f)
                                             stream.println(quote(getBlockResult(f)) + "\n")
                                           stream.println("}")
    case _                              => super.emitNode(sym,node)
  }
}

trait ScalaGenReactivityOpt extends ScalaGenReactivity {
  val IR: ReactivityExpOpt
  import IR._

  override def emitNode(sym: Sym[Any], node: Def[Any]): Unit =  node match {
    case ConstantAccess(f) => emitValDef(sym, quote(getBlockResult(f)))
    case ConstantCreation(f) => emitValDef(sym, "Constant {")
                                  emitBlock(f)
                                  stream.println(quote(getBlockResult(f)) + "\n")
                                stream.println("}")
    case _ => super.emitNode(sym,node)

  }
}
