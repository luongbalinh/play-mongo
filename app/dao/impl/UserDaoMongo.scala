package dao.impl

import java.time.ZonedDateTime
import javax.inject.Inject

import dao.exception.UserDaoException
import dao.{CounterDao, UserDao}
import models.User
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.JsValue
import play.api.libs.json.Json._
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.json.dao.JsonDao
import reactivemongo.extensions.json.dsl.JsonDsl._

import scala.concurrent.Future

class UserDaoMongo @Inject()(reactiveMongoApi: ReactiveMongoApi, counterDao: CounterDao) extends UserDao {

  val dao = new JsonDao[User, BSONObjectID](reactiveMongoApi.db, UserDaoMongo.CollectionName) {}

  override def findUser(id: Long): Future[Option[User]] = dao.findOne("id" $eq id)

  override def findAllUsers(): Future[List[User]] = dao.findAll()

  override def insertUser(user: User): Future[Option[User]] = {
    nextUserId flatMap { newId =>
      val userToSave = user.copy(
        id = Some(newId),
        createdDate = Some(ZonedDateTime.now),
        updatedDate = Some(ZonedDateTime.now)
      )
      dao.insert(userToSave) map {
        result =>
          if (result.ok) Some(userToSave)
          else None
      }
    }
  }

  private def nextUserId: Future[Long] = {
    counterDao.nextId(UserDaoMongo.CollectionName)
  }

  override def removeUser(id: Long): Future[Boolean] =
    dao.remove("id" $eq id) map (_.n > 0)

  override def updateUser(id: Long, update: JsValue): Future[User] = {
    dao.update("id" $eq id, obj("$set" -> update)) flatMap { lastError =>
      if (lastError.ok) {
        findUser(id) map {
          case Some(user) => user
          case None =>
            throw UserDaoException(s"Cannot get user id=$id after updating of collection ${
              UserDaoMongo
                .CollectionName
            }")
        }
      }
      else {
        throw UserDaoException(s"Cannot update user with id=$id and update=$update", lastError.getCause)
      }
    }
  }
}

object UserDaoMongo {
  val CollectionName = "users"
}
