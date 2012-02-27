package src.ppl.dsl.optisdr

case class ComplexInt(val real: Int, val imag: Int) {
  override def toString = real + " + " + imag + "i"
  
  def +(that: ComplexInt) = ComplexInt(this.real + that.real, this.imag + that.imag)
  def +(that: Int) = ComplexInt(this.real + that, this.imag)
  def +(that: Long) = ComplexInt(this.real + that, this.imag)
  def +(that: Float) = ComplexInt(this.real + that, this.imag)
  def +(that: Double) = ComplexInt(this.real + that, this.imag)

  def -(that: ComplexInt) = ComplexInt(this.real - that.real, this.imag - that.imag)
  def -(that: Int) = ComplexInt(this.real - that, this.imag)
  def -(that: Long) = ComplexInt(this.real - that, this.imag)
  def -(that: Float) = ComplexInt(this.real - that, this.imag)
  def -(that: Double) =
    ComplexInt(this.real - that, this.imag)

  def *(that: ComplexInt) =
    ComplexInt(this.real * that.real - this.imag * that.imag,
            this.real * that.imag + this.imag * that.real)
  def *(that: Int) = ComplexInt(this.real * that, this.imag * that)
  def *(that: Long) = ComplexInt(this.real * that, this.imag * that)
  def *(that: Float) = ComplexInt(this.real * that, this.imag * that)
  def *(that: Double) = ComplexInt(this.real * that, this.imag * that)

  def /(that: ComplexInt) = {
    val denom = that.real * that.real + that.imag * that.imag
    ComplexInt((this.real * that.real + this.imag * that.imag) / denom,
            (this.imag * that.real - this.real * that.imag) / denom)
  }
  def /(that: Int) = ComplexInt(this.real / that, this.imag / that)
  def /(that: Long) = ComplexInt(this.real / that, this.imag / that)
  def /(that: Float) = ComplexInt(this.real / that, this.imag / that)
  def /(that: Double) = ComplexInt(this.real / that, this.imag / that)

  def unary_- = ComplexInt(-real, -imag)

  def abs = math.sqrt(real*real + imag*imag)

  def conjugate = ComplexInt(real, -imag)

  override def equals(that: Any) = that match {
    case that: ComplexInt => this.real == that.real && this.imag == that.imag
    case real : Double => this.real == real && this.imag == 0
    case real : Int => this.real == real && this.imag == 0
    case real : Short => this.real == real && this.imag == 0
    case real : Long => this.real == real && this.imag == 0
    case real : Float => this.real == real && this.imag == 0
    case _ => false
  }
}

object ComplexInt { outer =>

  /** Constant ComplexInt(0,0). */
  val zero = new ComplexInt(0,0);

  /** Constant ComplexInt(1,0). */
  val one = new ComplexInt(1,0);

  /** Constant ComplexInt(NaN, NaN). */
  val nan = new ComplexInt(Double.NaN, Double.NaN);

  /** Constant ComplexInt(0,1). */
  val i = new ComplexInt(0,1);
  trait ComplexIsConflicted extends Numeric[ComplexInt] {
    def plus(x: ComplexInt, y: ComplexInt): ComplexInt = x + y
    def minus(x: ComplexInt, y: ComplexInt): ComplexInt = x - y
    def times(x: ComplexInt, y: ComplexInt): ComplexInt = x * y
    def negate(x: ComplexInt): ComplexInt = -x
    def fromInt(x: Int): ComplexInt = ComplexInt(x, 0)
    def toInt(x: ComplexInt): Int = strictlyReal(x).toInt
    def toLong(x: ComplexInt): Long = strictlyReal(x).toLong
    def toFloat(x: ComplexInt): Float = strictlyReal(x).toFloat
    def toDouble(x: ComplexInt): Double = strictlyReal(x)

    /** Checks that a `ComplexInt` number is strictly real, and returns the real
      * component. */
    private def strictlyReal(x: ComplexInt): Double = {
      require(x.imag == 0.0)  // only proceed if x.imag is *exactly* zero
      x.real
    }
  }
  /** `ComplexInt` as `scala.math.Fractional` trait. */
  trait ComplexIsFractional extends ComplexIsConflicted
		            with Fractional[ComplexInt]
  {
    def div(x: ComplexInt, y: ComplexInt): ComplexInt = x / y
  }
  /** Ordering for ComplexInt numbers: orders lexicographically first
    * on the real, then on the imaginary part of the number. */ 
  trait ComplexOrdering extends Ordering[ComplexInt] {
    override def compare(a : ComplexInt, b : ComplexInt) = {
      if (a.real < b.real) -1
      else if (a.real > b.real) 1
      else if (a.imag < b.imag) -1
      else if (a.imag > b.imag) 1
      else 0;
    }
  }
  /** Implicit object providing `scala.math.Fractional` capabilities.
    * Although ComplexInt numbers have no natural ordering, some kind of
    * `Ordering` is required because `Numeric` extends `Ordering`.  Hence,
    * an ordering based upon the real then imaginary components is used. */
  implicit object ComplexIsFractional extends ComplexIsFractional
                                      with ComplexOrdering
}