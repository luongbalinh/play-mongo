package services

import java.time.ZonedDateTime
import javax.inject.Inject

import daos.UserDAO
import daos.impl._
import models.User
import play.api.libs.json.Json._
import play.api.libs.json.{JsObject, JsValue}
import helpers.ZonedDateTimeReadWrite._

import scala.concurrent.Future

class UserServiceImpl @Inject()(userDAO: UserDAO) extends UserService {
  override def find(id: Long): Future[Option[User]] =
    userDAO.find(id)

  override def findAll(): Future[List[User]] =
    userDAO.findAll()

  override def insert(user: User): Future[Long] =
    userDAO.insert(user)

  override def remove(id: Long): Future[Unit] =
    userDAO.remove(id)

  override def update(id: Long, update: JsValue): Future[Unit] =
    userDAO.update(id, updateNow(update))

  private def updateNow(update: JsValue): JsObject =
    update.as[JsObject] ++ obj(userUpdatedDateField -> ZonedDateTime.now)
}
