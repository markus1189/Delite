package dsl.reactive.datastruct.scala

import scala.collection.mutable.Buffer
import scala.collection.mutable.ListBuffer

trait ReactiveEntity {
  def getDependents: ReactiveEntities
  def getDependentsList: List[ReactiveEntity]
  def forceReEval(): Unit
}

case class ReactiveEntities(unwrap: Seq[ReactiveEntity]) {
  def size = unwrap.size

  def dcSize = size
  def dcApply(i: Int) = unwrap(i)
}

/* A node that has nodes that depend on it */
trait DepHolder extends ReactiveEntity {
  val dependents: Buffer[Dependent] = ListBuffer()

  def addDependent(dep: Dependent) { dependents += dep }
  def <+ : Dependent => Unit = addDependent _

  def removeDependent(dep: Dependent) { dependents -= dep }

  def notifyDependents() {}

  def getDependents: ReactiveEntities = ReactiveEntities(dependents.toSeq)
  def getDependentsList: List[ReactiveEntity] = dependents.toList
}

trait AccessableDepHolder[+T] extends DepHolder {
  def get: T
}

/* A node that depends on other nodes */
trait Dependent extends ReactiveEntity {
  private val dependOn: Buffer[DepHolder] = new ListBuffer()

  def addDependOn(dep: DepHolder) { dependOn += dep }
  def +> : DepHolder => Unit = addDependOn _

  def removeDependOn(dep: DepHolder) { dependOn -= dep }

  /* A node on which this one depends is changed */
  def dependsOnChanged(dep: DepHolder)

  def forceReEval(): Unit
}

object Dependent {
  implicit def fromExpression[T](exp: => T): Handler[T] = Handler(exp)
}

class Var[T] private (initialValue: T) extends AccessableDepHolder[T] {
  private var heldValue: T = initialValue

  def get = heldValue

  def set(newValue: T) {
    if (newValue != heldValue) {
      heldValue = newValue
      notifyDependents()
    }
  }

  def modify(f: T => T) = set(f(get))

  def forceReEval() { }
}

object Var {
  def apply[T](initialValue: T) = new Var(initialValue)
}

class Signal[+T] private (depHolders: Seq[DepHolder])(expr: => T)
  extends Dependent with AccessableDepHolder[T] {

  private[this] var heldValue = expr

  def get: T = heldValue

  depHolders foreach addDependOn
  depHolders foreach (_.addDependent(this)) // check

  def reEvaluate() {
    val evaluated = expr

    if (evaluated != heldValue) {
      heldValue = evaluated
      notifyDependents
    }
  }

  def forceReEval() = reEvaluate()

  def dependsOnChanged(dep: DepHolder) { reEvaluate() }
}

object Signal {
  def apply[T](depHolders: DepHolder*)(expr: => T) =
    new Signal(depHolders)(expr)
}

/**
 * A callback called when a signal changes
 */
class Handler[T] private (exp: => T) extends Dependent {
  def dependsOnChanged(dep: DepHolder) = exp
  def reEvaluate = exp
  def forceReEval() = exp
  def getDependents = ReactiveEntities(List.empty)
  def getDependentsList = List.empty
}

object Handler{
	def apply[T] (exp: => T) = new Handler(exp)
}

