package models

trait IdModel[T] {

  // id should start from 1
  val id: Option[Long] = None

  // update id
  def withNewId(id: Long): T
}