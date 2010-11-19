package ppl.delite.framework.codegen.delite.generators

import java.io.{FileWriter, File, PrintWriter}
import ppl.delite.framework.codegen.delite.DeliteCodegen
import ppl.delite.framework.ops.DeliteOpsExp
import scala.virtualization.lms.internal.{ScalaGenEffect, GenericCodegen}
import ppl.delite.framework.{Util, Config}
import collection.mutable.ListBuffer

trait DeliteGenTaskGraph extends DeliteCodegen {
  val IR: DeliteOpsExp
  import IR._

  private def vals(sym: Sym[_]) : List[Sym[_]] = sym match {
    case Def(Reify(s, effects)) => Nil
    case Def(Reflect(NewVar(v), effects)) => Nil
    case _ => List(sym)
  }

  private def vars(sym: Sym[_]) : List[Sym[_]] = sym match {
    case Def(Reflect(NewVar(v), effects)) => List(sym)
    case _ => Nil
  }

  override def emitNode(sym: Sym[_], rhs: Def[_])(implicit stream: PrintWriter) : Unit = {
    assert(generators.length >= 1)

    var resultIsVar = false

    // we will try to generate any node that is not purely an effect node
    rhs match {
      case Reflect(s, effects) => super.emitNode(sym, rhs); return
      case Reify(s, effects) => super.emitNode(sym, rhs); return
      case NewVar(x) => resultIsVar = true // if sym is a NewVar - if so, we must mangle the result type
      case _ => // continue and attempt to generate kernel
    }

    // TODO: validate that generators agree on inputs (similar to schedule validation in DeliteCodegen)
    generators(0).shallow = true
    val dataDeps = (generators(0).syms(rhs) ++ generators(0).getFreeVarNode(rhs)).distinct
    generators(0).shallow = false

    val inVals = dataDeps.flatMap(vals(_))
    val inVars = dataDeps.flatMap(vars(_))

    implicit val supportedTargets = new ListBuffer[String]
    for (gen <- generators) {
      val build_path = Config.build_dir + gen + "/"
      val outf = new File(build_path)
      outf.mkdirs()
      val kstream = new PrintWriter(new FileWriter(build_path + quote(sym) + "." + gen.kernelFileExt))

      try{
        // emit kernel
        gen.emitKernelHeader(sym, inVals, inVars, resultIsVar)(kstream)
        gen.emitNode(sym, rhs)(kstream)
        gen.emitKernelFooter(sym, inVals, inVars, resultIsVar)(kstream)

        //record that this kernel was succesfully generated
        supportedTargets += gen.toString
      }
      catch {
        case e: Exception => // no generator found
      }
      finally {
        kstream.close()
      }
    }

    // emit task graph node
    val inputs = inVals ++ inVars
    rhs match {
      case DeliteOP_SingleTask(block) => emitSingleTask(sym, inputs, List())
      case DeliteOP_Map(block) => emitMap(sym, inputs, List())
      case DeliteOP_ZipWith(block) => emitZipWith(sym, inputs, List())
      case _ => emitSingleTask(sym, inputs, List()) // things that are not specified as DeliteOPs, emit as SingleTask nodes
    }

    // whole program gen (for testing)
    //emitValDef(sym, "embedding.scala.gen.kernel_" + quote(sym) + "(" + inputs.map(quote(_)).mkString(",") + ")")
  }

  def emitSingleTask(sym: Sym[_], inputs: List[Exp[_]], control_deps: List[Sym[_]])(implicit stream: PrintWriter, supportedTgt: ListBuffer[String]) = {
    stream.print("{\"type\":\"SingleTask\"")
    stream.print(",\"kernelId\":\"" + quote(sym) + "\"")
    stream.print(",\"supportedTargets\": [" + supportedTgt.mkString("\"","\",\"","\"") + "]\n")
    val inputsStr = if(inputs.isEmpty) "" else inputs.map(quote(_)).mkString("\"","\",\"","\"")
    stream.print("  \"inputs\":[" + inputsStr + "]")
    stream.println("},")
  }
  def emitMap(sym: Sym[_], inputs: List[Exp[_]], control_deps: List[Sym[_]])(implicit stream: PrintWriter, supportedTgt: ListBuffer[String]) = nop
  def emitZipWith(sym: Sym[_], inputs: List[Exp[_]], control_deps: List[Sym[_]])(implicit stream: PrintWriter, supportedTgt: ListBuffer[String]) = nop

  def nop = throw new RuntimeException("Not Implemented Yet")

}
