package dsl.reactive

import scala.virtualization.lms.common.FunctionBlocksExp
import dsl.reactive.datastruct.scala._
import scala.virtualization.lms.common.ScalaGenBase
import scala.virtualization.lms.common.{ScalaGenEffect, Base, EffectExp}
//import dsl.reactive.Types._

trait ReactiveOps extends ReactiveVars

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

trait ReactiveOpsExp extends ReactiveVarsExp

trait ReactiveVarsExp extends ReactiveVars with EffectExp {
  case class VarCreation[T:Manifest](x: Rep[T]) extends Def[ReactiveVar[T]]
  def varNew[T:Manifest](x: Rep[T]) = VarCreation[T](x)

  case class VarGetContent[T:Manifest](v: Rep[ReactiveVar[T]]) extends Def[T]
  def varGetContent[T:Manifest](v: Rep[ReactiveVar[T]]) = reflectEffect(VarGetContent(v))

  case class VarSetContent[T:Manifest](v: Rep[ReactiveVar[T]], x: Rep[T]) extends Def[Unit]
  def varSetContent[T:Manifest](v: Rep[ReactiveVar[T]], x: Rep[T]) = reflectEffect(VarSetContent(v,x))

  case class VarModifyContent[T:Manifest](v: Rep[ReactiveVar[T]], f: Rep[T] => Rep[T]) extends Def[ReactiveVar[T]]
  def varModifyContent[T:Manifest](v: Rep[ReactiveVar[T]], f: Rep[T] => Rep[T]) = VarModifyContent(v,f)
}

trait ScalaGenReactiveOps extends ScalaGenBase {
  val IR: ReactiveOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case VarCreation(x) => stream.println("val " + quote(sym) + " = ReactiveVar(" + quote(x) + ")")
    case VarGetContent(v) => stream.println("val " + quote(sym) + " = " + quote(v) + ".get")
    case VarSetContent(v,x) => stream.println("val " + quote(sym) + " = " + quote(v) + ".set(" + quote(x) + ")")
    case _ => super.emitNode(sym,rhs)
  }
}
