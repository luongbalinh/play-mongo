package daos.impl

import java.time.ZonedDateTime
import javax.inject.Inject

import daos.exceptions.MongoInsertException
import daos.{CounterDAO, UserDAO}
import helpers.DbHelper._
import models.User
import play.api.{Configuration, Logger}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.JsValue
import play.api.libs.json.Json._
import play.modules.reactivemongo.json._
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.api.commands.CommandError

import scala.concurrent.Future

class UserMongo @Inject()(counterDAO: CounterDAO)(implicit config: Configuration) extends UserDAO {

  private val usersFuture = getCollection(userCollectionName)

  override def find(id: Long): Future[Option[User]] =
    usersFuture flatMap { coll =>
      coll
        .find(obj("id" -> id))
        .one[User](readPreference = ReadPreference.primary)
    }

  override def findAll(): Future[List[User]] =
    usersFuture flatMap { coll =>
      coll
        .find(obj())
        .cursor[User](readPreference = ReadPreference.primary)
        .collect[List](-1, Cursor.FailOnError[List[User]]())
    }

  override def insert(user: User): Future[Long] =
    usersFuture flatMap { _ =>
      getNextUserId flatMap { newId =>
        insertWithId(user
          .withNewId(newId).copy(createdDate = Some(ZonedDateTime.now), updatedDate = Some(ZonedDateTime.now)))
      }
    }

  def enrichUserWithId(user: User, newId: Long): User =
    user.copy(
      id = Some(newId),
      createdDate = Some(ZonedDateTime.now),
      updatedDate = Some(ZonedDateTime.now))


  override def update(id: Long, value: JsValue): Future[Unit] =
    usersFuture flatMap { coll =>
      MongoUpdateHelper.update(coll)(id, value)
    }

  override def remove(id: Long): Future[Unit] =
    usersFuture flatMap { coll =>
      MongoUpdateHelper.remove(coll)(id)
    }

  private def getNextUserId: Future[Long] =
    counterDAO.nextId(userCollectionName)

  private def insertWithId(t: User): Future[Long] = {
    usersFuture flatMap { coll =>
      coll.insert(t) map (_ => t.id.get) recover {
        case err: CommandError if err.code contains 11000 =>
          // if the result is defined with the error code 11000 (duplicate error)
          Logger.warn(s"Object $t already exists")
          t.id.get
        case e => throw MongoInsertException(s"Failed to insert object with id = ${t.id}: ${e.getMessage}")
      }
    }
  }
}
