package dsl.reactive

import dsl.reactive.datastruct.scala._
import scala.virtualization.lms.common._

trait ReactiveOps extends ReactiveVars with ReactiveSignals
trait ReactiveOpsExp extends ReactiveVarsExp with ReactiveSignalsExp

trait ReactiveVars extends Base {
  def ReactiveVar[T:Manifest](x: Rep[T]): Rep[ReactiveVar[T]] = varNew(x)
  def varNew[T:Manifest](x: Rep[T]): Rep[ReactiveVar[T]]

  def infix_get[T:Manifest](v: Rep[ReactiveVar[T]]) = varGetContent(v)
  def varGetContent[T:Manifest](v: Rep[ReactiveVar[T]]): Rep[T]

  def infix_set[T:Manifest](v: Rep[ReactiveVar[T]], x: Rep[T]) = varSetContent(v,x)
  def varSetContent[T:Manifest](v: Rep[ReactiveVar[T]], x: Rep[T]): Rep[Unit]

  def infix_modify[T:Manifest](v: Rep[ReactiveVar[T]], f: Rep[T] => Rep[T]) = varModifyContent(v,f)
  def varModifyContent[T:Manifest](v: Rep[ReactiveVar[T]], f: Rep[T] => Rep[T]): Rep[ReactiveVar[T]]
}

trait ReactiveVarsExp extends ReactiveVars with EffectExp {
  case class VarCreation[T:Manifest](x: Rep[T]) extends Def[ReactiveVar[T]] { val m = manifest[T] }
  def varNew[T:Manifest](x: Rep[T]) = VarCreation[T](x)

  case class VarGetContent[T:Manifest](v: Rep[ReactiveVar[T]]) extends Def[T]
  def varGetContent[T:Manifest](v: Rep[ReactiveVar[T]]) = reflectEffect(VarGetContent(v))

  case class VarSetContent[T:Manifest](v: Rep[ReactiveVar[T]], x: Rep[T]) extends Def[Unit]
  def varSetContent[T:Manifest](v: Rep[ReactiveVar[T]], x: Rep[T]) = reflectEffect(VarSetContent(v,x))

  case class VarModifyContent[T:Manifest](v: Rep[ReactiveVar[T]], f: Rep[T] => Rep[T]) extends Def[ReactiveVar[T]]
  def varModifyContent[T:Manifest](v: Rep[ReactiveVar[T]], f: Rep[T] => Rep[T]) = VarModifyContent(v,f)
}

trait ReactiveSignals extends Base with Functions with TupledFunctions {
  def ReactiveSignal[T:Manifest](ds: Rep[Seq[DepHolder]])(f: () => Rep[T]): Rep[ReactiveSignal[T]] = signalNew(ds,fun(f))
  def signalNew[T:Manifest](ds: Rep[Seq[DepHolder]], f: Rep[Unit => T]): Rep[ReactiveSignal[T]]

  def infix_getS[T:Manifest](v: Rep[ReactiveSignal[T]]) = signalGetContent(v)
  def signalGetContent[T:Manifest](v: Rep[ReactiveSignal[T]]): Rep[T]
}

trait ReactiveSignalsExp extends ReactiveSignals with EffectExp with FunctionsExp {

  case class SignalCreation[T:Manifest](ds: Rep[Seq[DepHolder]], f: Rep[Unit => T]) extends Def[ReactiveSignal[T]] { val m = manifest[T] }
  def signalNew[T:Manifest](ds: Rep[Seq[DepHolder]], f: Rep[Unit => T]) = SignalCreation[T](ds,f)

  case class SignalGetContent[+T:Manifest](v: Rep[ReactiveSignal[T]]) extends Def[T]
  def signalGetContent[T:Manifest](v: Rep[ReactiveSignal[T]]) =
    reflectEffect(SignalGetContent(v))
}

trait ScalaGenReactiveOps extends ScalaGenBase {
  val IR: ReactiveOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case v@VarCreation(x) =>
      emitValDef(sym, remap("generated.scala.ReactiveVar[" + remap(v.m) + "]") + "(" + quote(x) + ")")
    case VarGetContent(v) =>
      emitValDef(sym, quote(v) + ".get")
    case VarSetContent(v,x) =>
      emitValDef(sym, quote(v) + ".set(" + quote(x) + ")")
    case s@SignalCreation(x,f) =>
      emitValDef(sym, remap("generated.scala.ReactiveSignal[" + remap(s.m) + "]") + "(" + quote(x) + ":_* ) { " + quote(f) + " }")
    case SignalGetContent(v) =>
      emitValDef(sym, quote(v) + ".get")
    case _ =>
      super.emitNode(sym,rhs)
  }

}
