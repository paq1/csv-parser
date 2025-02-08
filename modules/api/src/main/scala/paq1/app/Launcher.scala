package paq1.app

import schemas.Schema
import shapeless.Lazy

// 3️⃣ Définition d'un modèle et test du parsing
case class Chien(age: Int, nom: Option[String])

object Chien {
  // Auto-génération du schema de `Chien`
  implicit val chienSchema: Lazy[Schema[Chien]] = Lazy(Schema[Chien])
}




object Launcher extends App {
  val row1 = Map("age" -> "5", "nom" -> "Rex")
  val x: Lazy[Either[String, Chien]] = Chien.chienSchema.map(_.parse(row1))
  println(x)
  println("ok")
}
