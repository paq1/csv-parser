package paq1.app

import shapeless._
import shapeless.labelled._
import shapeless.ops.hlist.{ToTraversable, Tupler}
import shapeless.ops.record.Keys
import shapeless.syntax.SingletonOps
import shapeless.syntax.singleton.mkSingletonOps
import shapeless.tag._

import java.lang.reflect.Field
import scala.reflect.ClassTag
import scala.util.Try


case class Chien(nom: String, age: Double)

object Chien {
  implicit val objectDesc: ObjectDesc[Chien] = ObjectDesc(
    fields = List(
      FieldDesc("nom", "String"),
      FieldDesc("age", "Double"),
    )
  )
}


case class FieldDesc(name: String, fieldType: String) // todo : passer par un trait / enum pour le type
case class ObjectDesc[T](fields: List[FieldDesc]) {

  def fromValueTiHlist(list: List[String])(implicit label: LabelledGeneric[T]): Either[Throwable, label.Repr] = {

    if (list.length != fields.length) {
      return Left(new Exception("500 - le schema ne correspond pas"))
    }

    val keyValueString: List[(FieldDesc, String)] = fields.zip(list)

    keyValueString.reverse.foldLeft[Either[Throwable, HList]](Right(HNil)) { (acc, current) =>
      acc.flatMap { currentHlist =>
        Try {
          val field = Symbol(current._1.name)
          val typeFieldsStr = current._1.fieldType
          val hlist: Either[Throwable, HList] = typeFieldsStr match {
            case "String" => {
              val value = field ->> current._2
              Right(value :: currentHlist)
            }
            case "Double" => {
              val value = field ->> current._2.toDouble
              Right(value :: currentHlist)
            }
            case _ => Left(new Exception(s"type $typeFieldsStr non prit en compte"))
          }
          hlist
        }.toEither.flatten
      }
    }.map(d => d.asInstanceOf[label.Repr])

  }

}


object Launcher extends App {

  def parse[T](
    l : List[String]
  )(
    implicit sc: ObjectDesc[T],
    label: LabelledGeneric[T]
  ): Either[String, T] = {
    val maybeHlist: Either[Throwable, label.Repr] = sc.fromValueTiHlist(l)
    println(maybeHlist.left.map(_ => "pouet"))

    maybeHlist match {
      case Left(value) => Left("err")
      case Right(value) => Right(label.from(value))
    }
    Left("Not implemented")
  }


//  transform()
  val result = parse[Chien](List("toto", "5.4"))
  println(result)

  def transform(): Unit = {
    val value1 = Symbol("nom") ->> "a"
    val value2 = Symbol("age") ->> 12.0
    val hlist: label.Repr = value1 :: value2 :: HNil

    val label = LabelledGeneric[Chien]
    val chien = label.from(hlist)
    println(chien)
  }

}


