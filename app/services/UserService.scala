package services

import models.User
import play.api.libs.json.JsValue

import scala.concurrent.Future

trait UserService {
  def find(id: Long): Future[Option[User]]

  def findAll(): Future[List[User]]

  def insert(user: User): Future[Long]

  def remove(id: Long): Future[Unit]

  def update(id: Long, update: JsValue): Future[Unit]
}
