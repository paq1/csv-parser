package paq1.deprecated

import shapeless.{HList, LabelledGeneric, Typeable}
import shapeless.ops.hlist.ToTraversable
import shapeless.ops.record.Keys

@deprecated("juste ici pour tester des truc lol")
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
