package schemas

object CaseClassFiller {
  import shapeless.HList
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
