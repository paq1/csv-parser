package schemas

import scala.util.Try

trait Predicate[A] {
  def name: String
  def parse(value: Option[String]): Either[String, A]
  def isOptionnal: Boolean
}

object Predicate {
  def apply[A](colName: String, pIsOptionnal: Boolean)(implicit parser: String => Either[String, A]): Predicate[A] =
    new Predicate[A] {
      override def name: String = colName
      override def isOptionnal: Boolean = pIsOptionnal
      override def parse(value: Option[String]): Either[String, A] = value.map(parser).getOrElse(Left(s"missing field for : $colName"))
    }

  implicit val intParser: String => Either[String, Int] = s => Try(s.toInt).toEither.left.map(_ => s"cannot parse $s to Int")
  implicit val stringParse: String => Either[String, String] = s => Right(s)
  implicit def optionParser[A](implicit p: String => Either[String, A]): String => Either[String, Option[A]] =
    s => if (s.isEmpty) Right(None) else p(s).map(Some(_))
}
