package dsl.reactive
import scala.virtualization.lms.common._
import scala.virtualization.lms.internal._
import ppl.delite.framework._
import ppl.delite.framework.codegen._
import ppl.delite.framework.ops._
import ppl.delite.framework.datastruct.scala.DeliteCollection
import codegen.delite.overrides._
import codegen.scala.TargetScala
import java.io.File

trait DepHolder
trait AccessableDepHolder[+A] extends DepHolder
trait Dependent
abstract class Var[A:Manifest] extends AccessableDepHolder[A]
abstract class Signal[+A:Manifest] extends Dependent with AccessableDepHolder[A]
abstract class DependentSeq extends DeliteCollection[Dependent]

trait ReactiveApplicationRunner extends ReactiveApplication 
                                with DeliteApplication 
                                with ReactiveExp

trait ReactiveApplication extends Reactive with ReactiveLift {
  var args: Rep[Array[String]]
  def main(): Unit
}

trait ReactiveLift extends LiftScala {
  this: Reactive =>
}

/* IR packages */
trait Reactive extends ScalaOpsPkg with Reactivity

trait ReactiveExp extends Reactive with ScalaOpsPkgExp with ReactivityExp
  with DeliteOpsExp with VariantsOpsExp with DeliteAllOverridesExp {

  this: DeliteApplication with ReactiveApplication with ReactiveExp =>

  def getCodeGenPkg(t: Target{val IR: ReactiveExp.this.type}):
    GenericFatCodegen{val IR: ReactiveExp.this.type} = {
    
    t match {
      case _:TargetScala => new ReactiveCodeGenScala {
        val IR: ReactiveExp.this.type = ReactiveExp.this
      }
      case _ => throw new IllegalArgumentException("unsupported target")
    }
  }
}

/* Code generator packages */
trait ReactiveCodeGenBase extends GenericFatCodegen with codegen.Utils {
  val IR: DeliteApplication with ReactiveExp
  override def initialDefs = IR.deliteGenerator.availableDefs
  
  def dsmap(s: String) = {
    var res = s.replaceAll("dsl.reactive.datastruct", "generated")
    res.replaceAll("dsl.reactive", "generated.scala")
  }
  
  override def remap[A](m: Manifest[A]): String = dsmap(super.remap(m))
  
  override def emitDataStructures(path: String) {
    val s = File.separator
    val dsRoot = Config.homeDir + s+"dsls"+s+"reactive"+s+"src"+s+
                 "dsl"+s+"reactive"+s+"datastruct"+s + this.toString

    copyDataStructures(dsRoot, path, dsmap)
  }
}

trait ReactiveCodeGenScala extends ReactiveCodeGenBase with ScalaCodeGenPkg 
  with ScalaGenDeliteOps with ScalaGenReactivity 
  with ScalaGenVariantsOps with ScalaGenDeliteCollectionOps 
  with DeliteScalaGenAllOverrides {
      
  val IR: DeliteApplication with ReactiveExp
}
