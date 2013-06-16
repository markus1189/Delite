package dsl.reactive

import scala.virtualization.lms.common._
import ppl.delite.framework.ops.{DeliteCollectionOpsExp,DeliteOpsExp}
import ppl.delite.framework.datastruct.scala.DeliteCollection

trait Reactivity extends Base {
  implicit def toAccessableDepHolderOps[A:Manifest](dh: Rep[AccessableDepHolder[A]]) = new AccessableDepHolderOps(dh)

  class AccessableDepHolderOps[A:Manifest](dh: Rep[AccessableDepHolder[A]]) {
    def get: Rep[A] = dep_holder_access(dh)
    def set[A:Manifest](value: Rep[A]): Rep[Unit] = dep_holder_set(dh, value)
    def getDependents: Rep[ReactiveEntitySeq] = dep_holder_dependents(dh)
  }

  implicit def toReactiveEntityOps(entity: Rep[ReactiveEntity]) = new ReactiveEntityOps(entity)
  class ReactiveEntityOps(entity: Rep[ReactiveEntity]) {
    def getDependents: Rep[ReactiveEntitySeq] = reactive_entity_dependents(entity)
    def reEvaluate() = re_evaluate(entity)
  }

  def reactive_entity_dependents(entity: Rep[ReactiveEntity]): Rep[ReactiveEntitySeq]
  def re_evaluate(elem: Rep[ReactiveEntity]): Rep[Unit]

  implicit def toDepHolderOps(dh: Rep[DepHolder]) = new DepHolderOps(dh)
  class DepHolderOps(dh: Rep[DepHolder]) {
    def getDependents: Rep[ReactiveEntitySeq] = dep_holder_dependents(dh)
  }

  def dep_holder_access[A:Manifest](dh: Rep[AccessableDepHolder[A]]): Rep[A]
  def dep_holder_set[A:Manifest](dh: Rep[AccessableDepHolder[A]], value: Rep[A]): Rep[Unit]
  def dep_holder_dependents(dh: Rep[DepHolder]): Rep[ReactiveEntitySeq]

  object Var {
    def apply[A:Manifest](v: Rep[A]): Rep[AccessableDepHolder[A]] = new_reactive_var(v)
  }

  def new_reactive_var[A:Manifest](v: Rep[A]): Rep[Var[A]]

  object Signal {
    def apply[A:Manifest](dhs: Rep[DepHolder]*)(f: => Rep[A]) =
      new_reactive_signal(dhs, f)
  }

  def new_reactive_signal[A:Manifest](dhs: Seq[Rep[DepHolder]], f: => Rep[A]): Rep[Signal[A]]
}

trait ReactivityExp extends Reactivity 
                    with WhileExp
                    with ListOpsExp
                    with SeqOpsExp
                    with EffectExp 
                    with DeliteCollectionOpsExp 
                    with DeliteOpsExp 
                    with IfThenElseExp {

  case class AccessDepHolder[A:Manifest](dh: Exp[AccessableDepHolder[A]]) extends Def[A]
  override def dep_holder_access[A:Manifest](dh: Exp[AccessableDepHolder[A]]): Exp[A] =
    reflectMutable(AccessDepHolder(dh))

  case class GetSizeReactiveEntitySeq(reSeq: Exp[ReactiveEntitySeq]) extends Def[Int]
  case class UnwrapReactiveEntitySeq(reSeq: Exp[ReactiveEntitySeq]) extends Def[List[ReactiveEntity]]
  def infix_size(reSeq: Exp[ReactiveEntitySeq]): Exp[Int] = GetSizeReactiveEntitySeq(reSeq)
  def infix_unwrap(reSeq: Exp[ReactiveEntitySeq]): Exp[List[ReactiveEntity]] = UnwrapReactiveEntitySeq(reSeq)


  case class NotifyDependents(dh: Exp[ReactiveEntity]) extends DeliteOpForeach[ReactiveEntity] {
    def func: Exp[ReactiveEntity] => Exp[Unit] = _.reEvaluate()
    val in: Exp[DeliteCollection[ReactiveEntity]] = dh.getDependents
    val size: Exp[Int] = dh.getDependents.size
    def sync: Exp[Int] => Exp[List[Any]] = _ => List[Any]()
  }

  case class SetDepHolder[A:Manifest](dh: Exp[AccessableDepHolder[A]], value: Exp[A]) extends Def[Unit]
  override def dep_holder_set[A:Manifest](dh: Exp[AccessableDepHolder[A]], value: Exp[A]): Exp[Unit] = {
    reflectEffect(SetDepHolder(dh,value))
    notify(dh)
  }

  private def notify(e: Exp[ReactiveEntity]) {
    reflectEffect(NotifyDependents(e))
    e.getDependents.unwrap.map(e => reflectEffect(NotifyDependents(e))) // yypIe.getDependents.unwrap.map(_fexA)^
  }

  case class GetDependents(dh: Exp[ReactiveEntity]) extends Def[ReactiveEntitySeq]
  override def dep_holder_dependents(dh: Exp[DepHolder]): Exp[ReactiveEntitySeq] =
    GetDependents(dh)

  override def reactive_entity_dependents(entity: Exp[ReactiveEntity]): Exp[ReactiveEntitySeq] =
    GetDependents(entity)

  case class ReEvaluation(elem: Exp[ReactiveEntity]) extends Def[Unit]
  override def re_evaluate(elem: Exp[ReactiveEntity]): Exp[Unit] = {
    reflectEffect(ReEvaluation(elem))
  }

  type MyVar[A] = dsl.reactive.Var[A]
  case class VarCreation[A:Manifest](value: Exp[A]) extends Def[MyVar[A]]
  override def new_reactive_var[A:Manifest](v: Exp[A]): Exp[MyVar[A]] = VarCreation(v)

  case class SignalCreation[A:Manifest](
    dhs: Seq[Exp[DepHolder]],
    body: Block[A]
  ) extends Def[Signal[A]]

  override def new_reactive_signal[A:Manifest](
    dhs: Seq[Exp[DepHolder]],
    f: => Exp[A]
  ): Exp[Signal[A]] = SignalCreation(dhs, reifyEffects(f))

  override def boundSyms(e: Any): List[Sym[Any]] = e match {
    case SignalCreation(dhs,body) => effectSyms(body)
    case _ => super.boundSyms(e)
  }
}

trait ScalaGenReactivity extends ScalaGenBase with ScalaGenEffect with ScalaGenWhile {
  val IR: ReactivityExp
  import IR._

  override def emitNode(sym: Sym[Any], node: Def[Any]): Unit = node match {
    case GetSizeReactiveEntitySeq(reSeq) => emitValDef(sym, quote(reSeq) + ".size")
    case UnwrapReactiveEntitySeq(reSeq) => emitValDef(sym, quote(reSeq) + ".re.toList")
    case AccessDepHolder(dh) => emitValDef(sym, quote(dh) + ".get")
    case ReEvaluation(elem) => emitValDef(sym, quote(elem) + ".forceReEval()")
    case GetDependents(dh) => emitValDef(sym, quote(dh) + ".getDependents")
    case SetDepHolder(dh,value) => emitValDef(sym, quote(dh) + ".set(" + quote(value) + ")")
    case VarCreation(v) =>
      emitValDef(sym, "Var(" + quote(v) + ")")
    case SignalCreation(dhs,f) =>
      emitValDef(sym, "Signal(" + dhs.map(quote).mkString(", ") + ") { ")
      emitBlock(f)
      stream.println(quote(getBlockResult(f)) + "\n}")
    case _ => super.emitNode(sym,node)
  }
}
