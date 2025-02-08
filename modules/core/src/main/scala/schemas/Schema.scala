package schemas

import shapeless._
import shapeless.labelled._
import scala.util.Try


trait Schema[T] {
  def parse(row: Map[String, String]): Either[String, T]
}

object Schema {

  def apply[T](implicit schema: Schema[T]): Schema[T] = schema

  implicit def genericSchema[T, Repr <: HList](
                                                implicit gen: LabelledGeneric.Aux[T, Repr],
                                                reprSchema: Lazy[Schema[Repr]]
                                              ): Schema[T] = new Schema[T] {
    override def parse(row: Map[String, String]): Either[String, T] = reprSchema.value.parse(row).map(gen.from)
  }

  implicit def hnilSchema: Schema[HNil] = new Schema[HNil] {
    override def parse(row: Map[String, String]): Either[String, HNil] = Right(HNil)
  }


  // Cas récursif pour `HList` (head :: tail)
  implicit def hconsSchema[K <: Symbol, V, Tail <: HList](implicit
                                                          key: Witness.Aux[K],
                                                          pred: Predicate[V],
                                                          tailSchema: Lazy[Schema[Tail]]
                                                         ): Schema[FieldType[K, V] :: Tail] = new Schema[FieldType[K, V] :: Tail] {
    override def parse(row: Map[String, String]): Either[String, FieldType[K, V] :: Tail] = {
      val headValue: Either[String, V] = pred.parse(row.get(key.value.name)) // Utiliser le nom du champ via Witness
      val tailValue: Either[String, Tail] = tailSchema.value.parse(row)
      for {
        head <- headValue
        tail <- tailValue
      } yield {
        field[K](head) :: tail
      }
    }
  }

}
