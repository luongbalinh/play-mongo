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

  override def find(id: Long): Future[Option[User]] = dao.findOne("id" $eq id)

  override def findAll(): Future[List[User]] = dao.findAll()

  override def insert(user: User): Future[User] =
    nextUserId flatMap { newId =>
      val enrichedUser = enrichUserWithId(user, newId)
      insertUserWithId(enrichedUser)
    }

  def enrichUserWithId(user: User, newId: Long): User =
    user.copy(
      id = Some(newId),
      createdDate = Some(ZonedDateTime.now),
      updatedDate = Some(ZonedDateTime.now))

  override def remove(id: Long): Future[Unit] =
    dao.remove("id" $eq id) map { lastError =>
      if (lastError.n == 0) throw UserDaoException(s"Cannot delete user id=$id")
    }

  override def update(id: Long, update: JsValue): Future[User] =
    dao.update("id" $eq id, obj("$set" -> update)) flatMap { lastError =>
      if (lastError.ok) {
        find(id) map {
          case Some(user) => user
          case None =>
            throw UserDaoException(s"Cannot get user id=$id after updating of collection ${UserDaoMongo.CollectionName}")
        }
      }
      else {
        throw UserDaoException(s"Cannot update user with id=$id and update=$update", lastError.getCause)
      }
    }

  private def nextUserId: Future[Long] =
    counterDao.nextId(UserDaoMongo.CollectionName)

  private def insertUserWithId(user: User): Future[User] =
    dao.insert(user) map { result =>
      if (result.ok) user else throw UserDaoException(s"Cannot insert new user $user")
    }
}

object UserDaoMongo {
  val CollectionName = "users"
}
