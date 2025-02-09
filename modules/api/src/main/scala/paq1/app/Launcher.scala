package paq1.app

import shapeless._
import shapeless.labelled._
import shapeless.ops.hlist.Tupler
import shapeless.syntax.singleton.mkSingletonOps
import shapeless.tag._


case class Chien(nom: String, age: Double)

object Chien {
  type ChienTup = Generic[Chien]#Repr
}


object Launcher extends App {

  def parse[T](data: List[String]): Option[T] = {
    None
  }


//  transform()
  val result = parse[Chien](List("toto", "5.4"))
  println(result)

  def transform(): Unit = {
    val value1 = Symbol("nom") ->> "a"
    val value2 = Symbol("age") ->> 12.0
    val hlist = value1 :: value2 :: HNil

    val label = LabelledGeneric[Chien]
    val chien = label.from(hlist)
    println(chien)
  }

}


