package paq1.parser.exemple

import schemas.Implicits._
import schemas.{ImplSchema, PredicateBuilder, Schema}

case class Chien (nom: String, age: Double)
object Chien {

  val predicates: List[schemas.Predicate[_]] = List(
    PredicateBuilder[String]().withTitle("nom").build,
    PredicateBuilder[Double]().withTitle("age").build
  )

  implicit val schema: Schema[Chien] = ImplSchema[Chien](predicates)
}