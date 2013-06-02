package dsl.reactive

import dsl.reactive.datastruct.scala._
import scala.virtualization.lms.common._

trait Reactivity extends Base {
  implicit def toAccessableDepHolderOps[A:Manifest](dh: Rep[AccessableDepHolder[A]]) = new AccessableDepHolderOps(dh)

  class AccessableDepHolderOps[A:Manifest](dh: Rep[AccessableDepHolder[A]]) {
    def get: Rep[A] = dep_holder_access(dh)
    def set[A:Manifest](value: Rep[A]): Rep[Unit] = dep_holder_set(dh, value)
  }

  def dep_holder_access[A:Manifest](dh: Rep[AccessableDepHolder[A]]): Rep[A]
  def dep_holder_set[A:Manifest](dh: Rep[AccessableDepHolder[A]], value: Rep[A]): Rep[Unit]

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
    AccessDepHolder(dh)

  case class SetDepHolder[A:Manifest](dh: Exp[AccessableDepHolder[A]], value: Exp[A]) extends Def[Unit]
  override def dep_holder_set[A:Manifest](dh: Exp[AccessableDepHolder[A]], value: Exp[A]): Exp[Unit] =
    reflectEffect(SetDepHolder(dh,value))

  case class VarCreation[A:Manifest](value: Exp[A]) extends Def[Var[A]]
  override def new_reactive_var[A:Manifest](v: Exp[A]): Exp[Var[A]] = reflectMutable(VarCreation(v))

  case class SignalCreation[A:Manifest](
    dhs: Seq[Exp[DepHolder]],
    body: Block[A]
  ) extends Def[Signal[A]] 

  override def new_reactive_signal[A:Manifest](
    dhs: Seq[Exp[DepHolder]],
    f: => Exp[A]
  ): Exp[Signal[A]] = reflectMutable(SignalCreation(dhs, reifyEffects(f)))

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
