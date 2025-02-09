package schemas

import scala.util.Try

trait ImplicitParsing {
  implicit val stringParser: String => Either[String, String] = (value: String) => Right(value)
  implicit val intParser: String => Either[String, Int] = (value: String) => Try {
    value.toInt
  }.toEither.left.map(x => "Erreur lors du parsing : Int")
  implicit val doubleParser: String => Either[String, Double] = (value: String) => Try {
    value.toDouble
  }.toEither.left.map(x => "Erreur lors du parsing : Double")
}
