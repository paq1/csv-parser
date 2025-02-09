package schemas

trait Schema[T] {
  def predicates: List[Predicate[_]]
  def fromStringToAnyValues(strValues: List[String]): Either[Throwable, List[Any]]
}

case class ImplSchema[T](predicates: List[Predicate[_]]) extends Schema[T] {

  override def fromStringToAnyValues(strValues: List[String]): Either[Throwable, List[Any]] = {
    for {
      _ <- canZip(strValues)
      zipped = predicates.zip(strValues)
      result <- toValues(zipped)
    } yield result
  }

  private def toValues(zipped: List[(Predicate[_], String)]): Either[Throwable, List[Any]] = {
    zipped.reverse.foldLeft[Either[Throwable, List[Any]]](Right(List.empty[Any])) { (acc, current) =>
      acc.flatMap { values =>
        val (predicate, strValue) = current

        predicate
          .parse(strValue).left.map(err => new Exception(err))
          .map { value =>
            value :: values
          }
      }
    }
  }

  private def canZip(values: List[_]): Either[Throwable, Unit] = {
    if (values.length != predicates.length) {
      Left(new Exception("le nombre de valeur est incompatible"))
    } else {
      Right(())
    }
  }

}