package models

trait IdModel[ID, T] {

  def id: Option[ID]
}
