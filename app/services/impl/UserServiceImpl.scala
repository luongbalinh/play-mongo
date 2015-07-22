package services.impl

import java.time.ZonedDateTime
import javax.inject.{Inject, Singleton}
import dao.traits.UserDAO
import models.User
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.{JsObject, JsValue, Json}
import services.UserService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

/**
 *
 * @author luongbalinh (Software Engineer - RedMart)
 *         <linhluongba@gmail.com>
 * @since 3/7/15
 */

@Singleton
class UserServiceImpl @Inject()(userDAO: UserDAO)(implicit ctx: ExecutionContext) extends UserService {
  private final val logger: Logger = LoggerFactory.getLogger(classOf[UserServiceImpl])

  override def create(user: User): Future[Try[User]] = {
    val userToSave = user.copy(id = user.id, createdDate = Some(ZonedDateTime.now),
      updatedDate = Some(ZonedDateTime.now))

    userDAO.create(userToSave) map {
      case Success(id) => Success(userToSave.copy(id = Some(id)))
      case Failure(e) => Failure(e)
    }
  }

  override def read(id: Long): Future[Option[User]] = {
    userDAO.read(id)
  }

  override def update(id: Long, updates: JsValue): Future[Try[Unit]] = {
    logger.info(s"Updated user with id = $id")
    val updateWithDate = updates.as[JsObject] + {
      "updatedDate" -> Json.toJson {
        ZonedDateTime.now
      }
    }
    userDAO.update(id, updateWithDate)
  }

  override def delete(id: Long): Future[Try[Unit]] = {
    logger.info(s"Deleted user with id = $id")
    userDAO.delete(id)
  }

  override def findAll(): Future[List[User]] = {
    userDAO.findAll()
  }

}