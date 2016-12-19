package models

trait IdModel[ID, T] {
  def id: Option[ID]

  def withNewId(newId: Long): User
}
