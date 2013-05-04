package dsl.reactive

import scala.virtualization.lms.common.ScalaGenBase
import scala.virtualization.lms.common.{ScalaGenEffect, Base, EffectExp}

trait ReactiveOps extends Base {
  abstract class ReactiveVar[T]

  def Var[T:Manifest](x: Rep[T]): Rep[ReactiveVar[T]] = varNew(x)
  def varNew[T:Manifest](x: Rep[T]): Rep[ReactiveVar[T]]

  def infix_get[T:Manifest](v: Rep[ReactiveVar[T]]) = varGetContent(v)
  def varGetContent[T:Manifest](v: Rep[ReactiveVar[T]]): Rep[T]

  def infix_set[T:Manifest](v: Rep[ReactiveVar[T]], x: Rep[T]) = varSetContent(v,x)
  def varSetContent[T:Manifest](v: Rep[ReactiveVar[T]], x: Rep[T]): Rep[Unit]

  def infix_modify[T:Manifest](v: Rep[ReactiveVar[T]], f: Rep[T] => Rep[T]) = varModifyContent(v,f)
  def varModifyContent[T:Manifest](v: Rep[ReactiveVar[T]], f: Rep[T] => Rep[T]): Rep[ReactiveVar[T]]
}

trait ReactiveOpsExp extends ReactiveOps with EffectExp {
  case class VarCreation[T](x: Rep[T]) extends Def[ReactiveVar[T]]
  def varNew[T:Manifest](x: Rep[T]) = VarCreation[T](x)

  case class VarGetContent[T](v: Rep[ReactiveVar[T]]) extends Def[T]
  def varGetContent[T:Manifest](v: Rep[ReactiveVar[T]]) = VarGetContent(v)

  case class VarSetContent[T](v: Rep[ReactiveVar[T]], x: Rep[T]) extends Def[Unit]
  def varSetContent[T:Manifest](v: Rep[ReactiveVar[T]], x: Rep[T]) = VarSetContent(v,x)

  case class VarModifyContent[T](v: Rep[ReactiveVar[T]], f: Rep[T] => Rep[T]) extends Def[ReactiveVar[T]]
  def varModifyContent[T:Manifest](v: Rep[ReactiveVar[T]], f: Rep[T] => Rep[T]) = VarModifyContent(v,f)
}

trait ScalaGenReactiveOps extends ScalaGenBase {
  val IR: ReactiveOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case VarCreation(x) => stream.println("FOOOOOOOOOOOOOO")
    case _ => super.emitNode(sym,rhs)
  }
}
