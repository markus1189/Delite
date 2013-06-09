package dsl.reactive

import dsl.reactive.datastruct.scala._
import scala.virtualization.lms.common._

trait Reactivity extends Base {
  implicit def toAccessableDepHolderOps[A:Manifest](dh: Rep[AccessableDepHolder[A]]) = new AccessableDepHolderOps(dh)
  implicit def toDepHolderOps[A:Manifest](dh: Rep[DepHolder]) = new DepHolderOps(dh)

  class AccessableDepHolderOps[A:Manifest](dh: Rep[AccessableDepHolder[A]]) {
    def get: Rep[A] = dep_holder_access(dh)
    def set[A:Manifest](value: Rep[A]): Rep[Unit] = dep_holder_set(dh, value)
    def getDependents: Rep[Array[Dependent]] = dep_holder_dependents(dh)
  }

  class DepHolderOps(dh: Rep[DepHolder]) {
    def getDependents: Rep[Array[Dependent]] = dep_holder_dependents(dh)
  }


  def dep_holder_access[A:Manifest](dh: Rep[AccessableDepHolder[A]]): Rep[A]
  def dep_holder_set[A:Manifest](dh: Rep[AccessableDepHolder[A]], value: Rep[A]): Rep[Unit]
  def dep_holder_dependents(dh: Rep[DepHolder]): Rep[Array[Dependent]]

  implicit def toDependentOps(d: Rep[Dependent]) = new DependentOps(d)
  class DependentOps(d: Rep[Dependent]) {
    def reEvaluate() = dependent_re_evaluate(d)
  }

  def dependent_re_evaluate(d: Rep[Dependent]): Rep[Unit]

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

trait ReactivityExp extends Reactivity with EffectExp {
  case class AccessDepHolder[A:Manifest](dh: Exp[AccessableDepHolder[A]]) extends Def[A]
  override def dep_holder_access[A:Manifest](dh: Exp[AccessableDepHolder[A]]): Exp[A] =
    reflectMutable(AccessDepHolder(dh))

  case class SetDepHolder[A:Manifest](dh: Exp[AccessableDepHolder[A]], value: Exp[A]) extends Def[Unit]
  override def dep_holder_set[A:Manifest](dh: Exp[AccessableDepHolder[A]], value: Exp[A]): Exp[Unit] =
    reflectEffect(SetDepHolder(dh,value))

  case class GetDependents(dh: Exp[DepHolder]) extends Def[Array[Dependent]]
  override def dep_holder_dependents(dh: Exp[DepHolder]): Exp[Array[Dependent]] =
    GetDependents(dh)

  case class ReEvaluation(d: Exp[Dependent]) extends Def[Unit]
  override def dependent_re_evaluate(d: Exp[Dependent]): Exp[Unit] = ReEvaluation(d)

  type MyVar[A] = dsl.reactive.datastruct.scala.Var[A]
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

trait ScalaGenReactivity extends ScalaGenBase with ScalaGenEffect {
  val IR: ReactivityExp
  import IR._

  override def emitNode(sym: Sym[Any], node: Def[Any]): Unit = node match {
    case AccessDepHolder(dh) => emitValDef(sym, quote(dh) + ".get")
    case ReEvaluation(d) => emitValDef(sym, quote(d) + ".reEvaluate()")
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
