package paq1.app

import paq1.parser.exemple.Chien
import schemas.CaseClassFiller

object Launcher extends App {
  val chienMapped: Either[Throwable, List[Any]] = Chien.schema.fromStringToAnyValues(List("a", "300.0"))
  println(chienMapped)
  val chien = chienMapped.map(fields => CaseClassFiller.fill(Chien.apply _, fields))
  println(chien)
}
