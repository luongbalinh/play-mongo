package controllers

import javax.inject.Inject

import models._
import org.slf4j.{Logger, LoggerFactory}
import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.mvc._
import services.UserService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class UserController @Inject()(val userService: UserService)(implicit val ctx:
ExecutionContext) extends Controller {
  private final val logger: Logger = LoggerFactory.getLogger(classOf[UserController])

  import UserController._

  def create = Action.async(parse.json) {
    request =>
      println(s"Creating a new user")
      request.body.validate[User] fold(
          validationError,
          createUser
          )
  }

  def findById(id: Long) = Action.async { request =>
    logger.info(s"Finding user by id = $id")
    userService.read(id) map {
      case Some(user) => Ok(Json.toJson(user))
      case None => NotFound
    }
  }

  def update(id: Long) = Action.async(parse.json) { request =>
    val updates = request.body
    logger.info(s"Updating user with id $id and updates = $updates")
    userService.update(id, updates) map {
      case Success(_) => Ok
      case Failure(e) => InternalServerError(UpdateFailed withDetails e.getMessage)
    }
  }

  def delete(id: Long) = Action.async { request =>
    logger.info(s"Deleting user with id $id")
    userService.delete(id) map {
      case Success(_) => Ok
      case Failure(e) => InternalServerError(DeleteFailed withDetails e.getMessage)
    }
  }

  def findAll = Action.async { request =>
    logger.info(s"Finding all users")
    userService.findAll() map { res =>
      Ok(Json.toJson(res))
    }
  }

  private def validationError: (Seq[(JsPath, Seq[ValidationError])]) => Future[Result] = { errors =>
    Future {
      BadRequest(InvalidJson withDetails JsError.toJson(errors))
    }
  }

  private def createUser: (User) => Future[Result] = { user =>
    userService.create(user) map {
      case Success(savedUser) => Ok(Json.toJson(savedUser))
      case Failure(e) => InternalServerError(SaveFailed withDetails e.getMessage)
    }
  }
}

object UserController {
  val InvalidJson = Error("UserInvalidJson", "Invalid json category format")
  val SaveFailed = Error("UserCreatedFailed", "Failed to create user")
  val UpdateFailed = Error("UserUpdateFailed", "Failed to update user")
  val DeleteFailed = Error("UserDeleteFailed", "Failed to delete user")
}

