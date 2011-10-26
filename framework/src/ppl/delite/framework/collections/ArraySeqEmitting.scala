package ppl.delite.framework.collections



import java.io.PrintWriter
import ppl.delite.framework.DeliteApplication
import ppl.delite.framework.datastruct.scala.DeliteCollection
import scala.virtualization.lms.common._
import ppl.delite.framework.ops.DeliteOpsExp



trait ArraySeqEmitting {
  
  def arraySeqEmitterFactory[T] = new EmitterFactory {
    def needsCombine = false
    def needsPostProcess = true
    def needsPostProcess2 = false
    
    def scala = new ScalaEmitter {
      def emitBufferDefs(kernelname: String, basename: String, elemtype: String)(implicit stream: PrintWriter) {
        stream.println("var " + basename + "_buf: Array[" + elemtype + "] = _")
        stream.println("var " + basename + "_size = 0")
        stream.println("var " + basename + "_offset = 0")
        stream.println("def " + basename + "_buf_init: Unit = {"/*}*/)
        stream.println(basename + "_buf = new Array(128)")
        stream.println(/*{*/"}")
        stream.println("def " + basename + "_buf_append(x: " + elemtype + "): Unit = {"/*}*/)
        stream.println("if (" + basename + "_size >= " + basename + "_buf.length) {"/*}*/)
        stream.println("val old = " + basename + "_buf")
        stream.println(basename + "_buf = new Array(2*old.length)")
        stream.println("System.arraycopy(old, 0, " + basename + "_buf, 0, old.length)")
        stream.println(/*{*/"}")
        stream.println(basename + "_buf(" + basename + "_size) = x")
        stream.println(basename + "_size += 1")
        stream.println(/*{*/"}")
      }
      def emitInitSubActivation(basename: String, activname: String, chunkIdxVar: String, numChunksVar: String)(implicit stream: PrintWriter) {
        stream.println(activname + "." + basename + "_buf_init")
      }
      def emitAddToBuffer(prefixSym: String, basename: String, elemname: String)(implicit stream: PrintWriter) {
        stream.println(prefixSym + basename + "_buf_append(" + elemname + ")")
      }
      def emitAddToDataStructure(prefixSym: String, basename: String, elemname: String)(implicit stream: PrintWriter) {
        stream.println(prefixSym + basename + ".+=(" + elemname + ")")
      }
      def emitPostCombine(basename: String, activname: String, lhsname: String)(implicit stream: PrintWriter) {
        stream.println(activname + "." + basename + "_offset = " + lhsname + "." + basename + "_offset + " + lhsname + "." + basename + "_size")
      }
      def emitPostProcInit(basename: String, activname: String)(implicit stream: PrintWriter) {
        stream.println("if (" + activname + "." + basename + "_offset > 0) {"/*}*/) // set data array for result object
        stream.println("val len = " + activname + "." + basename + "_offset + " + activname + "." + basename + "_size")
        stream.println("" + activname + "." + basename + ".unsafeSetData(new Array(len), len)")
        stream.println(/*{*/"} else {"/*}*/)
        stream.println("" + activname + "." + basename + ".unsafeSetData(" + activname + "." +basename + "_buf, " + activname + "." + basename + "_size)")
        stream.println(/*{*/"}")
      }
      def emitPostProcess(basename: String, activname: String)(implicit stream: PrintWriter) {
        stream.println("if (" + activname + "." + basename + ".unsafeData ne " + activname + "." + basename + "_buf)")
        stream.println("System.arraycopy(" + activname + "." + basename + "_buf, 0, " + activname + "." + basename + ".unsafeData, " + activname + "." + basename + "_offset, " + activname + "." + basename + "_size)")
        stream.println("" + activname + "." + basename + "_buf = null")
      }
      def emitPostCombine2(basename: String, activname: String, lhsname: String)(implicit stream: PrintWriter) {
      }
      def emitPostProcInit2(basename: String, activname: String)(implicit stream: PrintWriter) {
      }
      def emitPostProcess2(basename: String, activname: String)(implicit stream: PrintWriter) {
      }
      def emitDataDeclaration(basename: String, activname: String, dataname: String)(implicit stream: PrintWriter) {
        // TODO
      }
    }
  }
  
}

