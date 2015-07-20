package dao

import models.IdModel
import play.api.libs.json.JsValue

import scala.concurrent.Future
import scala.util.Try

trait CrudDAO[T <: IdModel[T]] {
  def create(entity: T): Future[Try[Long]]

  def read(id: Long): Future[Option[T]]

  def delete(id: Long): Future[Try[Unit]]

  def update(id: Long, updates: JsValue): Future[Try[Unit]]

  def findAll(): Future[List[T]]
}
