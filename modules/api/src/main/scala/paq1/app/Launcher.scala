package paq1.app

import schemas.Schema
import shapeless._


case class Chien(age: Int, nom: Option[String])

object Chien {
  implicit val t: LabelledGeneric.Aux[Chien, HList] = LabelledGeneric[Chien]
  implicit val schema: Schema[Chien] = Schema.genericSchema[Chien, HList]
}




object Launcher extends App {

  val intSchema = implicitly[Schema[Int]]

  println(intSchema)

  val row1 = Map("age" -> "5", "nom" -> "Rex")
//  val x: Lazy[Either[String, Chien]] = Chien.chienSchema.map(_.parse(row1))
//  println(x)

//  val chien = Chien.schema.parse(row1)
  println(Chien.schema.parse(row1))

  println("ok")
}
