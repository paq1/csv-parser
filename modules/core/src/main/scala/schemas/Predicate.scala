package schemas

trait Predicate[A] {
  def name: String
  def parse(value: String): Either[String, A]
  def isOptionnal: Boolean
}
