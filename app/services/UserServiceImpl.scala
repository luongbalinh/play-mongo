package services

import java.time.ZonedDateTime
import javax.inject.Inject

import dao.UserDao
import models.User
import play.api.libs.json.Json._
import play.api.libs.json.{JsObject, JsValue}
import services.UserServiceImpl._

import scala.concurrent.Future
import utils.ZonedDateTimeReadWrite._

class UserServiceImpl @Inject()(userDao: UserDao) extends UserService {

  override def find(id: Long): Future[Option[User]] = userDao.find(id)

  override def findAll(): Future[List[User]] = userDao.findAll()

  override def insert(user: User): Future[User] = userDao.insert(user)

  override def remove(id: Long): Future[Unit] = userDao.remove(id)

  override def update(id: Long, update: JsValue): Future[User] =
    userDao.update(id, updateNow(update))

  private def updateNow(update: JsValue): JsObject =
    update.as[JsObject] ++ obj(UpdatedDateField -> ZonedDateTime.now)
}

object UserServiceImpl {
  val UpdatedDateField = "updatedDate"
}
