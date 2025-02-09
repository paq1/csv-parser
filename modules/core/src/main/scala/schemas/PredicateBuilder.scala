package schemas

case class PredicateBuilder[T](
    title: String = "",
    optionnal: Boolean = false
)(implicit fn: String => Either[String, T]) {

  def withTitle(value: String): PredicateBuilder[T] = copy(title = value)
  def isOptionnal: PredicateBuilder[T] = copy(optionnal = true)

  def build: Predicate[T] = new Predicate[T] {
    override def name: String = title
    override def parse(value: String): Either[String, T] = fn(value)
    override def isOptionnal: Boolean = optionnal
  }
}
