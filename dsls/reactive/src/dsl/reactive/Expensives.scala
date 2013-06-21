package dsl.reactive

import scala.virtualization.lms.internal.GraphVizExport

object ExpensivesRunner extends ReactiveApplicationRunner with Expensives

trait Expensives extends ReactiveApplication { 
  def main() = {
    val  x0 = Var(10l)
    val  y0 = Signal(x0){1l+expensive(x0.get)}
    val  y1 = Signal(x0){2l+expensive(x0.get)}
    val  y2 = Signal(x0){3l+expensive(x0.get)}
    val  y3 = Signal(x0){4l+expensive(x0.get)}
    val  y4 = Signal(x0){5l+expensive(x0.get)}
    val  y5 = Signal(x0){6l+expensive(x0.get)}
    val  y6 = Signal(x0){7l+expensive(x0.get)}
    val  y7 = Signal(x0){8l+expensive(x0.get)}
    val  y8 = Signal(x0){9l+expensive(x0.get)}
    val  y9 = Signal(x0){10l+expensive(x0.get)}
    val  y10 = Signal(x0){11l+expensive(x0.get)}
    val  y11 = Signal(x0){12l+expensive(x0.get)}
    val  y12 = Signal(x0){13l+expensive(x0.get)}
    val  y13 = Signal(x0){14l+expensive(x0.get)}
    val  y14 = Signal(x0){15l+expensive(x0.get)}
    val  y15 = Signal(x0){16l+expensive(x0.get)}
    val  y16 = Signal(x0){17l+expensive(x0.get)}
    val  y17 = Signal(x0){18l+expensive(x0.get)}
    val  y18 = Signal(x0){19l+expensive(x0.get)}
    val  y19 = Signal(x0){20l+expensive(x0.get)}
    val  y20 = Signal(x0){21l+expensive(x0.get)}
    val  y21 = Signal(x0){22l+expensive(x0.get)}
    val  y22 = Signal(x0){23l+expensive(x0.get)}
    val  y23 = Signal(x0){24l+expensive(x0.get)}
    val  y24 = Signal(x0){25l+expensive(x0.get)}
    val  y25 = Signal(x0){26l+expensive(x0.get)}
    val  y26 = Signal(x0){27l+expensive(x0.get)}
    val  y27 = Signal(x0){28l+expensive(x0.get)}
    val  y28 = Signal(x0){29l+expensive(x0.get)}
    val  y29 = Signal(x0){30l+expensive(x0.get)}
    val  y30 = Signal(x0){31l+expensive(x0.get)}
    val  y31 = Signal(x0){32l+expensive(x0.get)}
    val  y32 = Signal(x0){33l+expensive(x0.get)}
    val  y33 = Signal(x0){34l+expensive(x0.get)}
    val  y34 = Signal(x0){35l+expensive(x0.get)}
    val  y35 = Signal(x0){36l+expensive(x0.get)}
    val  y36 = Signal(x0){37l+expensive(x0.get)}
    val  y37 = Signal(x0){38l+expensive(x0.get)}
    val  y38 = Signal(x0){39l+expensive(x0.get)}
    val  y39 = Signal(x0){40l+expensive(x0.get)}
    val  y40 = Signal(x0){41l+expensive(x0.get)}
    val  y41 = Signal(x0){42l+expensive(x0.get)}
    val  y42 = Signal(x0){43l+expensive(x0.get)}
    val  y43 = Signal(x0){44l+expensive(x0.get)}
    val  y44 = Signal(x0){45l+expensive(x0.get)}
    val  y45 = Signal(x0){46l+expensive(x0.get)}
    val  y46 = Signal(x0){47l+expensive(x0.get)}
    val  y47 = Signal(x0){48l+expensive(x0.get)}
    val  y48 = Signal(x0){49l+expensive(x0.get)}
    val  y49 = Signal(x0){50l+expensive(x0.get)}
    val  y50 = Signal(x0){51l+expensive(x0.get)}
    val  y51 = Signal(x0){52l+expensive(x0.get)}
    val  y52 = Signal(x0){53l+expensive(x0.get)}
    val  y53 = Signal(x0){54l+expensive(x0.get)}
    val  y54 = Signal(x0){55l+expensive(x0.get)}
    val  y55 = Signal(x0){56l+expensive(x0.get)}
    val  y56 = Signal(x0){57l+expensive(x0.get)}
    val  y57 = Signal(x0){58l+expensive(x0.get)}
    val  y58 = Signal(x0){59l+expensive(x0.get)}
    val  y59 = Signal(x0){60l+expensive(x0.get)}
    val  y60 = Signal(x0){61l+expensive(x0.get)}
    val  y61 = Signal(x0){62l+expensive(x0.get)}
    val  y62 = Signal(x0){63l+expensive(x0.get)}
    val  y63 = Signal(x0){64l+expensive(x0.get)}
    val  y64 = Signal(x0){65l+expensive(x0.get)}
    val  y65 = Signal(x0){66l+expensive(x0.get)}
    val  y66 = Signal(x0){67l+expensive(x0.get)}
    val  y67 = Signal(x0){68l+expensive(x0.get)}
    val  y68 = Signal(x0){69l+expensive(x0.get)}
    val  y69 = Signal(x0){70l+expensive(x0.get)}
    val  y70 = Signal(x0){71l+expensive(x0.get)}
    val  y71 = Signal(x0){72l+expensive(x0.get)}
    val  y72 = Signal(x0){73l+expensive(x0.get)}
    val  y73 = Signal(x0){74l+expensive(x0.get)}
    val  y74 = Signal(x0){75l+expensive(x0.get)}
    val  y75 = Signal(x0){76l+expensive(x0.get)}
    val  y76 = Signal(x0){77l+expensive(x0.get)}
    val  y77 = Signal(x0){78l+expensive(x0.get)}
    val  y78 = Signal(x0){79l+expensive(x0.get)}
    val  y79 = Signal(x0){80l+expensive(x0.get)}
    val  y80 = Signal(x0){81l+expensive(x0.get)}
    val  y81 = Signal(x0){82l+expensive(x0.get)}
    val  y82 = Signal(x0){83l+expensive(x0.get)}
    val  y83 = Signal(x0){84l+expensive(x0.get)}
    val  y84 = Signal(x0){85l+expensive(x0.get)}
    val  y85 = Signal(x0){86l+expensive(x0.get)}
    val  y86 = Signal(x0){87l+expensive(x0.get)}
    val  y87 = Signal(x0){88l+expensive(x0.get)}
    val  y88 = Signal(x0){89l+expensive(x0.get)}
    val  y89 = Signal(x0){90l+expensive(x0.get)}
    val  y90 = Signal(x0){91l+expensive(x0.get)}
    val  y91 = Signal(x0){92l+expensive(x0.get)}
    val  y92 = Signal(x0){93l+expensive(x0.get)}
    val  y93 = Signal(x0){94l+expensive(x0.get)}
    val  y94 = Signal(x0){95l+expensive(x0.get)}
    val  y95 = Signal(x0){96l+expensive(x0.get)}
    val  y96 = Signal(x0){97l+expensive(x0.get)}
    val  y97 = Signal(x0){98l+expensive(x0.get)}
    val  y98 = Signal(x0){99l+expensive(x0.get)}
    val  y99 = Signal(x0){100l+expensive(x0.get)}
    val  y100 = Signal(x0){101l+expensive(x0.get)}

    printTime()

    println(y0.get+y1.get+y2.get+y3.get+y4.get+y5.get+y6.get+y7.get+y8.get+y9.get+y10.get+y11.get+y12.get+y13.get+y14.get+y15.get+y16.get+y17.get+y18.get+y19.get+y20.get+y21.get+y22.get+y23.get+y24.get+y25.get+y26.get+y27.get+y28.get+y29.get+y30.get+y31.get+y32.get+y33.get+y34.get+y35.get+y36.get+y37.get+y38.get+y39.get+y40.get+y41.get+y42.get+y43.get+y44.get+y45.get+y46.get+y47.get+y48.get+y49.get+y50.get+y51.get+y52.get+y53.get+y54.get+y55.get+y56.get+y57.get+y58.get+y59.get+y60.get+y61.get+y62.get+y63.get+y64.get+y65.get+y66.get+y67.get+y68.get+y69.get+y70.get+y71.get+y72.get+y73.get+y74.get+y75.get+y76.get+y77.get+y78.get+y79.get+y80.get+y81.get+y82.get+y83.get+y84.get+y85.get+y86.get+y87.get+y88.get+y89.get+y90.get+y91.get+y92.get+y93.get+y94.get+y95.get+y96.get+y97.get+y98.get+y99.get+y100.get)

    x0.set(11l)

    println(y0.get+y1.get+y2.get+y3.get+y4.get+y5.get+y6.get+y7.get+y8.get+y9.get+y10.get+y11.get+y12.get+y13.get+y14.get+y15.get+y16.get+y17.get+y18.get+y19.get+y20.get+y21.get+y22.get+y23.get+y24.get+y25.get+y26.get+y27.get+y28.get+y29.get+y30.get+y31.get+y32.get+y33.get+y34.get+y35.get+y36.get+y37.get+y38.get+y39.get+y40.get+y41.get+y42.get+y43.get+y44.get+y45.get+y46.get+y47.get+y48.get+y49.get+y50.get+y51.get+y52.get+y53.get+y54.get+y55.get+y56.get+y57.get+y58.get+y59.get+y60.get+y61.get+y62.get+y63.get+y64.get+y65.get+y66.get+y67.get+y68.get+y69.get+y70.get+y71.get+y72.get+y73.get+y74.get+y75.get+y76.get+y77.get+y78.get+y79.get+y80.get+y81.get+y82.get+y83.get+y84.get+y85.get+y86.get+y87.get+y88.get+y89.get+y90.get+y91.get+y92.get+y93.get+y94.get+y95.get+y96.get+y97.get+y98.get+y99.get+y100.get)

    x0.set(9l)

    println(y0.get+y1.get+y2.get+y3.get+y4.get+y5.get+y6.get+y7.get+y8.get+y9.get+y10.get+y11.get+y12.get+y13.get+y14.get+y15.get+y16.get+y17.get+y18.get+y19.get+y20.get+y21.get+y22.get+y23.get+y24.get+y25.get+y26.get+y27.get+y28.get+y29.get+y30.get+y31.get+y32.get+y33.get+y34.get+y35.get+y36.get+y37.get+y38.get+y39.get+y40.get+y41.get+y42.get+y43.get+y44.get+y45.get+y46.get+y47.get+y48.get+y49.get+y50.get+y51.get+y52.get+y53.get+y54.get+y55.get+y56.get+y57.get+y58.get+y59.get+y60.get+y61.get+y62.get+y63.get+y64.get+y65.get+y66.get+y67.get+y68.get+y69.get+y70.get+y71.get+y72.get+y73.get+y74.get+y75.get+y76.get+y77.get+y78.get+y79.get+y80.get+y81.get+y82.get+y83.get+y84.get+y85.get+y86.get+y87.get+y88.get+y89.get+y90.get+y91.get+y92.get+y93.get+y94.get+y95.get+y96.get+y97.get+y98.get+y99.get+y100.get)

    printTime()
  }
}


