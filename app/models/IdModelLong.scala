package models

trait IdModelLong[T] extends IdModel[Long, T] {

  override val id: Option[Long]
}
