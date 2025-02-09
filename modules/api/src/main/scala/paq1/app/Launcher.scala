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
      FieldDesc("age", "Double")
    )
  )
}

case class FieldDesc(
    name: String,
    fieldType: String
)

object FieldsExtractor {
  def fields[P <: Product, L <: HList, R <: HList](a: P)(implicit
      gen: LabelledGeneric.Aux[P, L],
      keys: Keys.Aux[L, R],
      list: ToTraversable.Aux[R, List, Symbol],
      typeable: Typeable[P]
  ): List[(String, String)] = {
    val fieldNames = keys().toList.map(_.name)
    println(keys().toList)

//    val fieldType = keys().toList.map(_.name)
    val values = a.productIterator.toList.map(_.toString)
    fieldNames zip values
  }
}

object Builder {
  import shapeless.{Generic, HList}
  import shapeless.ops.function.FnToProduct
  import shapeless.ops.traversable.FromTraversable

  // cc is used only to infer types, constructor is used
  def fill[F, L <: HList, R](
      cc: F,
      values: List[_]
  )(implicit
      fnToProduct: FnToProduct.Aux[F, L => R],
      fromTraversable: FromTraversable[L]
  ): R = fnToProduct(cc)(fromTraversable(values).get)
}

case class ObjectDesc[T: ClassTag](fields: List[FieldDesc]) {
  def fromValuesToT(values: List[String]): Either[Throwable, List[Any]] = {
    for {
      _ <- checkCanCombined(values)
      schemaWithValues = unsafeZipData(values)
      parsedValues <- parseValues(schemaWithValues)
//      result <- fromParsedValuesToT(parsedValues)
    } yield { parsedValues }
  }

  private def fromParsedValuesToT(values: List[Any]): Either[Throwable, T] = {
    Left(new Exception("fromParsedValuesToT - Not implemented"))
  }

  private def parseValues(
      fieldsAndValues: List[(FieldDesc, String)]
  ): Either[Throwable, List[Any]] = {

    fieldsAndValues.reverse.foldLeft[Either[Throwable, List[Any]]](Right(List.empty[Any])) { (acc, current) =>
      acc.flatMap { values =>
        val (fieldDescription, strValue) = current

        (fieldDescription.fieldType match {
          case "String" => Right(strValue)
          case "Double" =>
            Try {
              strValue.toDouble
            }.toEither
          case _ =>
            Left(
              new Exception(s"type : ${fieldDescription.fieldType} can't be parsed for the moment")
            )
        }).map { parsed =>
          parsed :: values
        }
      }
    }
  }

  private def unsafeZipData(values: List[String]): List[(FieldDesc, String)] =
    fields zip values

  private def checkCanCombined(
      values: List[String]
  ): Either[Throwable, Unit] = {
    if (values.length == fields.length) {
      Right(())
    } else {
      Left(new Exception("schema non comforme"))
    }
  }
}

object Launcher extends App {

  val c = Builder.fill(Chien.apply _, List("foo", 4.2))

  val info = FieldsExtractor.fields(c)
  println(info)
  println(c)

  val chienMapped: Either[Throwable, List[Any]] = Chien.objectDesc.fromValuesToT(List("a", "12.0"))
  println(chienMapped)
  val chien = chienMapped.map(fields => Builder.fill(Chien.apply _, fields))
  println(chien)

  def transform(): Unit = {
    val value1 = Symbol("nom") ->> "a"
    val value2 = Symbol("age") ->> 12.0
    val hlist: label.Repr = value1 :: value2 :: HNil

    val label = LabelledGeneric[Chien]
    val chien = label.from(hlist)
    println(chien)
  }

}
